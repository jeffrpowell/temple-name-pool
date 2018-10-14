package com.jeffrpowell.templenamepool.model;

import java.util.Objects;
import java.util.Set;

public class CompletedTempleOrdinances extends TempleName{
    private final WardMember completer;
    
    public CompletedTempleOrdinances(String familySearchId, WardMember completer, Set<Ordinance> ordinances, boolean male) {
        super(familySearchId, null, ordinances, male);
        this.completer = completer;
    }

    public WardMember getCompleter() {
        return completer;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + super.hashCode();
        hash = 97 * hash + Objects.hashCode(this.completer);
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
        final CompletedTempleOrdinances other = (CompletedTempleOrdinances) obj;
        if (!Objects.equals(this.familySearchId, other.familySearchId)) {
            return false;
        }
        if (!Objects.equals(this.ordinances, other.ordinances)) {
            return false;
        }
        if (!Objects.equals(this.male, other.male)) {
            return false;
        }
        return Objects.equals(this.completer, other.completer);
    }
    
}
