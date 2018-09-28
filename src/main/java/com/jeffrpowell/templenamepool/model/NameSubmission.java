package com.jeffrpowell.templenamepool.model;

import java.util.Objects;
import java.util.Set;

public class NameSubmission extends TempleName {

    private final WardMember supplier;

    public NameSubmission(String familySearchId, WardMember supplier, byte[] pdf, Set<Ordinance> ordinances) {
        super(familySearchId, pdf, ordinances);
        this.supplier = supplier;
    }

    public WardMember getSupplier() {
        return supplier;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + super.hashCode();
        hash = 89 * hash + Objects.hashCode(this.supplier);
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
        final NameSubmission other = (NameSubmission) obj;
        return Objects.equals(this.supplier, other.supplier);
    }

}
