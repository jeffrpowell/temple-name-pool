package com.jeffrpowell.templenamepool.model;

import java.util.Objects;
import java.util.Set;

public class TempleName {

    protected final String familySearchId;
    protected final byte[] pdf;
    protected final Set<Ordinance> ordinances;

    public TempleName(String familySearchId, byte[] pdf, Set<Ordinance> ordinances) {
        this.familySearchId = familySearchId;
        this.pdf = pdf;
        this.ordinances = ordinances;
    }

    public String getFamilySearchId() {
        return familySearchId;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public Set<Ordinance> getOrdinances() {
        return ordinances;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.familySearchId);
        hash = 61 * hash + Objects.hashCode(this.ordinances);
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
        final TempleName other = (TempleName) obj;
        if (!Objects.equals(this.familySearchId, other.familySearchId)) {
            return false;
        }
        return Objects.equals(this.ordinances, other.ordinances);
    }
    
}
