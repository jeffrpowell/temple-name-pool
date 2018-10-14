package com.jeffrpowell.templenamepool.dao;

import com.jeffrpowell.templenamepool.model.CompletedTempleOrdinances;
import com.jeffrpowell.templenamepool.model.NameRequest;
import com.jeffrpowell.templenamepool.model.NameSubmission;
import com.jeffrpowell.templenamepool.model.OverdueName;
import com.jeffrpowell.templenamepool.model.Statistics;
import com.jeffrpowell.templenamepool.model.TempleName;
import com.jeffrpowell.templenamepool.model.WardMember;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;

@Service
public class MongoNamePoolDao implements NamePoolDao{
    private final MongoCollection<NameSubmission> submissionsCollection;
    private final MongoCollection workerCollection;
    
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

        this.workerCollection = db.getCollection("workers");
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
                Filters.eq("ordinances", request.getOrdinance().name())
            )
        ).into(new ArrayList<>());
        return matchingSubmissions.stream()
            .map(submission -> (TempleName) submission)
            .collect(Collectors.toList());
    }

    @Override
    public void markNamesAsCompleted(Collection<CompletedTempleOrdinances> names) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Statistics generateStatistics() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<WardMember, List<OverdueName>> getOverdueNameCheckouts() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
		Collection<NameSubmission> submissions = submissionsCollection.find().into(new HashSet<>());
		return submissions.stream().map(NameSubmission::getSupplier).collect(Collectors.toSet());
    }
}
