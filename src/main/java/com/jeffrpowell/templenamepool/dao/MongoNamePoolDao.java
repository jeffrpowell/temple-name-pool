package com.jeffrpowell.templenamepool.dao;

import com.jeffrpowell.templenamepool.model.CheckedOutName;
import com.jeffrpowell.templenamepool.model.CompletedTempleOrdinances;
import com.jeffrpowell.templenamepool.model.NameRequest;
import com.jeffrpowell.templenamepool.model.NameSubmission;
import com.jeffrpowell.templenamepool.model.Ordinance;
import com.jeffrpowell.templenamepool.model.OverdueName;
import com.jeffrpowell.templenamepool.model.Statistics;
import com.jeffrpowell.templenamepool.model.TempleName;
import com.jeffrpowell.templenamepool.model.WardMember;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;

@Service
public class MongoNamePoolDao implements NamePoolDao{
    private final MongoCollection<NameSubmission> submissionsCollection;
    private final MongoCollection<CheckedOutName> workerCollection;
    
    @Inject
    public MongoNamePoolDao(MongoClient mongoClient) {
        MongoDatabase db = mongoClient.getDatabase("TempleNamePool");
        this.submissionsCollection = db.getCollection("submissions", NameSubmission.class);
		/*{
			familySearchId: "",
			pdf: [],
			ordinances: [],
            male: false,
            checkedOut: false,
			supplier: {
				id: "",
				name: "",
				email: "",
				phone: ""
			}
		}*/

        this.workerCollection = db.getCollection("workers", CheckedOutName.class);
		/*{
			targetDate: "",
			completed: false,
			name: {
				familySearchId: "",
				pdf: null,
				ordinances: [],
			}
			requester: {
				id: "",
				name: "",
				email: "",
				phone: ""
			}
		}*/
    }
    
    @Override
    public void addNames(Collection<NameSubmission> names) {
        submissionsCollection.insertMany(new ArrayList<>(names));
    }

    @Override
    public List<TempleName> checkoutNames(NameRequest request) {
        List<NameSubmission> matchingSubmissions = submissionsCollection.find(
            Filters.and(
                Filters.eq("checkedOut", false), 
                Filters.eq("male", request.isMaleRequested()), 
                Filters.eq("remainingOrdinances", request.getOrdinance().name()),
				Filters.nin("remainingOrdinances", request.getOrdinance().getPrerequisiteOrdinances().stream().map(Ordinance::name).collect(Collectors.toList()))
            )
        ).limit(request.getNumRequested()).into(new ArrayList<>());
		submissionsCollection.updateMany(Filters.in("familySearchId", matchingSubmissions.stream().map(NameSubmission::getFamilySearchId).collect(Collectors.toList())), Updates.set("checkedOut", true));
		workerCollection.insertMany(matchingSubmissions.stream().map(name -> new CheckedOutName(request.getRequester(), false, name, request.getTargetDate())).collect(Collectors.toList()));
        return matchingSubmissions.stream()
            .map(submission -> (TempleName) submission)
            .collect(Collectors.toList());
    }
	
    @Override
    public void markNamesAsCompleted(Collection<CompletedTempleOrdinances> names) {
		WardMember completer = names.stream().findAny().get().getCompleter();
		Map<String, List<NameSubmission>> submittedNames = submissionsCollection
			.find(Filters.and(
				Filters.in("familySearchId", names.stream().map(CompletedTempleOrdinances::getFamilySearchId).collect(Collectors.toList())),
				Filters.in("remainingOrdinances", names.stream().map(CompletedTempleOrdinances::getOrdinances).flatMap(Set::stream).map(Ordinance::name).collect(Collectors.toList()))
			))
			.into(new ArrayList<>())
			.stream()
			.collect(Collectors.groupingBy(NameSubmission::getFamilySearchId));
		Map<String, CheckedOutName> checkedOutNames = workerCollection
			.find(Filters.and(
				Filters.eq("requester.name", completer.getName()),
				Filters.in("name.familySearchId", names.stream().map(CompletedTempleOrdinances::getFamilySearchId).collect(Collectors.toList()))
			))
			.into(new ArrayList<>())
			.stream()
			.collect(Collectors.toMap(
				name -> name.getName().getFamilySearchId(),
				Function.identity()
			));
		names.forEach(name ->
		{
			NameSubmission nameSubmission = submittedNames.get(name.getFamilySearchId()).stream().filter(submission -> submission.getRemainingOrdinances().containsAll(name.getOrdinances())).findFirst().get();
			Set<Ordinance> remainingOrdinances = nameSubmission.getRemainingOrdinances();
			CheckedOutName checkedOutName = checkedOutNames.get(name.getFamilySearchId());
			nameSubmission.setCheckedOut(false);
			checkedOutName.setCompleted(true);
			nameSubmission.setRemainingOrdinances(nameSubmission.getRemainingOrdinances().stream().filter(ord -> !name.getOrdinances().contains(ord)).collect(Collectors.toSet()));
			checkedOutName.getName().setOrdinances(checkedOutName.getName().getOrdinances().stream().filter(ord -> name.getOrdinances().contains(ord)).collect(Collectors.toSet()));
			submissionsCollection.replaceOne(Filters.and(
				Filters.eq("familySearchId", name.getFamilySearchId()),
				Filters.in("remainingOrdinances", remainingOrdinances.stream().map(Ordinance::name).collect(Collectors.toList()))
			), nameSubmission);
			workerCollection.replaceOne(Filters.and(
				Filters.eq("requester.name", checkedOutName.getRequester().getName()),
				Filters.eq("name.familySearchId", checkedOutName.getName().getFamilySearchId())
			), checkedOutName);
		});
	}

    @Override
    public Statistics generateStatistics() {
        List<NameSubmission> availableMaleSubmissions = submissionsCollection.find(
            Filters.and(
                Filters.eq("checkedOut", false), 
                Filters.eq("male", true), 
                Filters.ne("remainingOrdinances", Collections.emptyList())
            )
        ).into(new ArrayList<>());
        List<NameSubmission> availableFemaleSubmissions = submissionsCollection.find(
            Filters.and(
                Filters.eq("checkedOut", false), 
                Filters.eq("male", false), 
                Filters.ne("remainingOrdinances", Collections.emptyList())
            )
        ).into(new ArrayList<>());
		List<CheckedOutName> completedOrdinances = workerCollection.find(Filters.eq("completed", true)).into(new ArrayList<>());
		Map<Ordinance, Integer> numMaleOrdinancesRemaining = availableMaleSubmissions.stream()
			.map(NameSubmission::getRemainingOrdinances)
			.flatMap(Set::stream)
			.collect(Collectors.groupingBy(Function.identity(), Collectors.reducing(0, ord -> 1, Math::addExact)));
		Map<Ordinance, Integer> numUnblockedMaleOrdinancesRemaining = availableMaleSubmissions.stream()
			.map(NameSubmission::getRemainingOrdinances)
			.map(this::filterUnblockedOrdinances)
			.flatMap(Set::stream)
			.collect(Collectors.groupingBy(Function.identity(), Collectors.reducing(0, ord -> 1, Math::addExact)));
		Map<Ordinance, Integer> numFemaleOrdinancesRemaining = availableFemaleSubmissions.stream()
			.map(NameSubmission::getRemainingOrdinances)
			.flatMap(Set::stream)
			.collect(Collectors.groupingBy(Function.identity(), Collectors.reducing(0, ord -> 1, Math::addExact)));
		Map<Ordinance, Integer> numUnblockedFemaleOrdinancesRemaining = availableFemaleSubmissions.stream()
			.map(NameSubmission::getRemainingOrdinances)
			.map(this::filterUnblockedOrdinances)
			.flatMap(Set::stream)
			.collect(Collectors.groupingBy(Function.identity(), Collectors.reducing(0, ord -> 1, Math::addExact)));
		Arrays.stream(Ordinance.values()).forEach(ord -> {
			numMaleOrdinancesRemaining.putIfAbsent(ord, 0);
			numUnblockedMaleOrdinancesRemaining.putIfAbsent(ord, 0);
			numFemaleOrdinancesRemaining.putIfAbsent(ord, 0);
			numUnblockedFemaleOrdinancesRemaining.putIfAbsent(ord, 0);
		});
		Map<WardMember, Integer> completedOrdinancesByMember = completedOrdinances.stream().collect(Collectors.groupingBy(CheckedOutName::getRequester, Collectors.reducing(0, member -> 1, Math::addExact)));
		Map<Ordinance, Integer> completedOrdinancesByOrdinance = completedOrdinances.stream().map(CheckedOutName::getName).flatMap(name -> name.getOrdinances().stream()).collect(Collectors.groupingBy(o -> o, Collectors.reducing(0, ord -> 1, Math::addExact)));
		return new Statistics.Builder()
			.setNameRequestersAndCountOfOrdinancesCompleted(completedOrdinancesByMember)
			.setNumOrdinancesPerformed(completedOrdinancesByOrdinance)
			.setNumMaleOrdinancesRemaining(numMaleOrdinancesRemaining)
			.setNumUnblockedMaleOrdinancesRemaining(numUnblockedMaleOrdinancesRemaining)
			.setNumFemaleOrdinancesRemaining(numFemaleOrdinancesRemaining)
			.setNumUnblockedFemaleOrdinancesRemaining(numUnblockedFemaleOrdinancesRemaining)
			.build();
    }
	
	private Set<Ordinance> filterUnblockedOrdinances(Set<Ordinance> ordinances) {
		return ordinances.stream()
			.filter(o -> !setContainsAny(ordinances, o.getPrerequisiteOrdinances()))
			.collect(Collectors.toSet());
	}
	
	private <T> boolean setContainsAny(Set<T> set, Set<T> subset) {
		return set.stream().anyMatch(t -> subset.contains(t));
	}

    @Override
    public Map<WardMember, List<OverdueName>> getOverdueNameCheckouts(boolean includeNotOverdue) {
        List<CheckedOutName> overdueNames;
		if (includeNotOverdue) {
			overdueNames = workerCollection.find(Filters.eq("completed", false)).into(new ArrayList<>());
		}
		else {
			overdueNames = workerCollection.find(Filters.and(Filters.eq("completed", false), Filters.lt("targetDate", LocalDate.now()))).into(new ArrayList<>());
		}
		return overdueNames.stream().collect(Collectors.groupingBy(CheckedOutName::getRequester,
                Collectors.mapping(name -> new OverdueName(name.getName(), name.getTargetDate()), Collectors.toList())));
    }

    @Override
    public Map<WardMember, List<TempleName>> getCompletedOrdinancesBySubmitter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<WardMember, List<CompletedTempleOrdinances>> getCompletedOrdinancesByCompleter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<WardMember> getWardMembers() {
		Set<NameSubmission> submissions = submissionsCollection.find().into(new HashSet<>());
		Set<CheckedOutName> checkouts = workerCollection.find().into(new HashSet<>());
		Set<WardMember> members = submissions.stream().map(NameSubmission::getSupplier).collect(Collectors.toSet());
		members.addAll(checkouts.stream().map(CheckedOutName::getRequester).collect(Collectors.toSet()));
		return members;
    }
}
