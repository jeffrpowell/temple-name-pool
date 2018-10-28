package com.jeffrpowell.templenamepool.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class TempleName {

    protected String familySearchId;
    protected byte[] pdf;
    protected Set<Ordinance> ordinances;
    protected boolean male;
    
    public TempleName() {
        familySearchId = null;
        pdf = new byte[0];
        ordinances = Collections.emptySet();
        male = false;
    }

    public TempleName(String familySearchId, byte[] pdf, Set<Ordinance> ordinances, boolean male) {
        this.familySearchId = familySearchId;
        this.pdf = pdf;
        this.ordinances = ordinances;
        this.male = male;
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

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public void setFamilySearchId(String familySearchId) {
        this.familySearchId = familySearchId;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public void setOrdinances(Set<Ordinance> ordinances) {
        this.ordinances = ordinances;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.familySearchId);
        hash = 79 * hash + Arrays.hashCode(this.pdf);
        hash = 79 * hash + Objects.hashCode(this.ordinances);
        hash = 79 * hash + (this.male ? 1 : 0);
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
        if (this.male != other.male) {
            return false;
        }
        if (!Objects.equals(this.familySearchId, other.familySearchId)) {
            return false;
        }
        if (!Arrays.equals(this.pdf, other.pdf)) {
            return false;
        }
        return Objects.equals(this.ordinances, other.ordinances);
    }

    
}
