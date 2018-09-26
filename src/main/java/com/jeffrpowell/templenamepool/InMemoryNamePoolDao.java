package com.jeffrpowell.templenamepool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryNamePoolDao implements NamePoolDao {

    private final Map<String, TempleName> templeNames;
    private final Map<WardMember, Collection<String>> submittedNames;
    private final Map<Ordinance, Collection<String>> availableOrdinances;
    private final Map<String, WardMember> checkedOutNames;
    private final Map<WardMember, Collection<TempleNameOrdinanceKey>> completedOrdinances;

    public InMemoryNamePoolDao() {
        this.templeNames = new ConcurrentHashMap<>();
        this.submittedNames = new ConcurrentHashMap<>();
        this.availableOrdinances = new ConcurrentHashMap<>();
        this.checkedOutNames = new ConcurrentHashMap<>();
        this.completedOrdinances = new ConcurrentHashMap<>();
    }

    InMemoryNamePoolDao(Map<String, TempleName> templeNames, Map<WardMember, Collection<String>> submittedNames, Map<Ordinance, Collection<String>> availableOrdinances, Map<String, WardMember> checkedOutNames, Map<WardMember, Collection<TempleNameOrdinanceKey>> completedOrdinances) {
        this.templeNames = templeNames;
        this.submittedNames = submittedNames;
        this.availableOrdinances = availableOrdinances;
        this.checkedOutNames = checkedOutNames;
        this.completedOrdinances = completedOrdinances;
    }

    @Override
    public void addNames(Collection<NameSubmission> names) {
        templeNames.putAll(names.stream().collect(Collectors.toMap(NameSubmission::getFamilySearchId, name -> name)));
        Map<Ordinance, List<String>> groupedNamesByOrdinance = names.stream()
            .map(TempleNameOrdinanceKey::fullPack)
            .flatMap(List::stream)
            .collect(Collectors.groupingBy(TempleNameOrdinanceKey::getOrdinance, Collectors.mapping(TempleNameOrdinanceKey::getFamilySearchId, Collectors.toList())));
        groupedNamesByOrdinance.entrySet().stream().forEach(entry -> {
            availableOrdinances.putIfAbsent(entry.getKey(), new ArrayList<>());
            availableOrdinances.get(entry.getKey()).addAll(entry.getValue());
        });
        Map<WardMember, List<NameSubmission>> groupedNamesBySubmitter = names.stream().collect(Collectors.groupingBy(NameSubmission::getSupplier));
        groupedNamesBySubmitter.entrySet().stream().forEach(entry -> {
            submittedNames.putIfAbsent(entry.getKey(), new ArrayList<>());
            submittedNames.get(entry.getKey()).addAll(entry.getValue().stream().map(NameSubmission::getFamilySearchId).collect(Collectors.toList()));
        });
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
    public void markNamesAsCompleted(Collection<TempleName> names) {
        WardMember member = checkedOutNames.get(names.iterator().next().getFamilySearchId());
        completedOrdinances.putIfAbsent(member, new ArrayList<>());
        completedOrdinances.get(member).addAll(
            names.stream()
                .peek(name -> checkedOutNames.remove(name.getFamilySearchId()))
                .map(TempleNameOrdinanceKey::fullPack)
                .filter(list -> !list.isEmpty())
                .flatMap(List::stream)
                .peek(idOrdinanceKey -> availableOrdinances.get(idOrdinanceKey.getOrdinance()).remove(idOrdinanceKey.getFamilySearchId()))
                .collect(Collectors.toList())
        );
    }

    @Override
    public Statistics generateStatistics() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<WardMember, List<TempleName>> getCompletedOrdinances() {
        return completedOrdinances.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .map(idOrdinanceKey -> templeNames.get(idOrdinanceKey.getFamilySearchId()))
                    .collect(Collectors.toList())
            ));
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
