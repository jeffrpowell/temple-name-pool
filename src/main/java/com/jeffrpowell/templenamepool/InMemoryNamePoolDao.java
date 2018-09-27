package com.jeffrpowell.templenamepool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryNamePoolDao implements NamePoolDao {

    private final Map<String, TempleName> templeNames;
    private final Map<String, NameSubmission> submittedNames;
    private final Map<Ordinance, Collection<String>> availableOrdinances;
    private final Map<String, WardMember> checkedOutNames;
    private final List<CompletedTempleOrdinances> completedOrdinances;

    public InMemoryNamePoolDao() {
        this.templeNames = new ConcurrentHashMap<>();
        this.submittedNames = new ConcurrentHashMap<>();
        this.availableOrdinances = new ConcurrentHashMap<>();
        this.availableOrdinances.putAll(EnumSet.allOf(Ordinance.class).stream().collect(Collectors.toMap(k->k, k -> new ArrayList<>())));
        this.checkedOutNames = new ConcurrentHashMap<>();
        this.completedOrdinances = new ArrayList<>();
    }

    InMemoryNamePoolDao(Map<String, TempleName> templeNames, Map<String, NameSubmission> submittedNames, Map<Ordinance, Collection<String>> availableOrdinances, Map<String, WardMember> checkedOutNames, List<CompletedTempleOrdinances> completedOrdinances) {
        this.templeNames = templeNames;
        this.submittedNames = submittedNames;
        this.availableOrdinances = availableOrdinances;
        this.checkedOutNames = checkedOutNames;
        this.completedOrdinances = completedOrdinances;
    }

    @Override
    public void addNames(Collection<NameSubmission> names) {
        Map<String, TempleName> idKeyedNames = names.stream().collect(Collectors.toMap(NameSubmission::getFamilySearchId, name -> name));
        templeNames.putAll(idKeyedNames);
        Map<Ordinance, List<String>> groupedNamesByOrdinance = names.stream()
            .map(TempleNameOrdinanceKey::fullPack)
            .flatMap(List::stream)
            .collect(Collectors.groupingBy(TempleNameOrdinanceKey::getOrdinance, Collectors.mapping(TempleNameOrdinanceKey::getFamilySearchId, Collectors.toList())));
        groupedNamesByOrdinance.entrySet().stream().forEach(entry -> {
            availableOrdinances.putIfAbsent(entry.getKey(), new ArrayList<>());
            availableOrdinances.get(entry.getKey()).addAll(entry.getValue());
        });
        submittedNames.putAll(names.stream().collect(Collectors.toMap(NameSubmission::getFamilySearchId, n->n)));
    }

    @Override
    public List<TempleName> checkoutNames(NameRequest request) {
        return availableOrdinances.get(request.getOrdinance()).stream()
            .filter(id -> !checkedOutNames.containsKey(id))
            .limit(request.getNumRequested())
            .peek(name -> checkedOutNames.put(name, request.getRequester()))
            .map(templeNames::get)
            .collect(Collectors.toList());
    }

    @Override
    public void markNamesAsCompleted(Collection<CompletedTempleOrdinances> names) {
        completedOrdinances.addAll(names.stream()
            .filter(name -> checkedOutNames.get(name.getFamilySearchId()) == name.getCompleter()) //ignore unexpected input
            .peek(name -> checkedOutNames.remove(name.getFamilySearchId()))
            .filter(name -> !name.getOrdinances().isEmpty())
            .peek(name -> {
                name.getOrdinances().forEach(ordinance -> 
                    availableOrdinances.get(ordinance).remove(name.getFamilySearchId())
                );
            })
            .collect(Collectors.toList())
        );
    }

    @Override
    public Statistics generateStatistics() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<WardMember, List<TempleName>> getCompletedOrdinancesBySubmitter() {
        Map<String, Set<Ordinance>> completedOrdinancesByName = completedOrdinances.stream()
            .collect(Collectors.groupingBy(
                CompletedTempleOrdinances::getFamilySearchId,
                Collectors.collectingAndThen(
                    Collectors.mapping(CompletedTempleOrdinances::getOrdinances, Collectors.toList()),
                    listOfSets -> listOfSets.stream().flatMap(Collection::stream).sorted().collect(Collectors.toSet())
                )
            ));
        List<TempleName> completedNames = completedOrdinancesByName.entrySet().stream()
            .map(entry -> new TempleName(entry.getKey(), null, entry.getValue()))
            .collect(Collectors.toList());
        Map<String, WardMember> submitters = completedOrdinancesByName.keySet().stream()
            .map(submittedNames::get)
            .collect(Collectors.toMap(NameSubmission::getFamilySearchId, NameSubmission::getSupplier));
        return completedNames.stream()
            .collect(Collectors.groupingBy(name -> submitters.get(name.getFamilySearchId())));
    }

    @Override
    public Map<WardMember, List<CompletedTempleOrdinances>> getCompletedOrdinancesByCompleter() {
        return completedOrdinances.stream().collect(Collectors.groupingBy(CompletedTempleOrdinances::getCompleter));
    }

    private static class TempleNameOrdinanceKey {

        private final String familySearchId;
        private final Ordinance ordinance;

        public static List<TempleNameOrdinanceKey> fullPack(TempleName name) {
            return name.getOrdinances().stream().map(ordinance -> new TempleNameOrdinanceKey(name.getFamilySearchId(), ordinance)).collect(Collectors.toList());
        }

        public TempleNameOrdinanceKey(String familySearchId, Ordinance ordinance) {
            this.familySearchId = familySearchId;
            this.ordinance = ordinance;
        }

        public String getFamilySearchId() {
            return familySearchId;
        }

        public Ordinance getOrdinance() {
            return ordinance;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 19 * hash + Objects.hashCode(this.familySearchId);
            hash = 19 * hash + Objects.hashCode(this.ordinance);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TempleNameOrdinanceKey other = (TempleNameOrdinanceKey) obj;
            if (!Objects.equals(this.familySearchId, other.familySearchId)) {
                return false;
            }
            return this.ordinance == other.ordinance;
        }
    }
}
