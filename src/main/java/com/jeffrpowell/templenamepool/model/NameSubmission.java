package com.jeffrpowell.templenamepool.model;

import java.util.Objects;
import java.util.Set;

public class NameSubmission extends TempleName {

    private WardMember supplier;
    private boolean checkedOut;
    
    public NameSubmission() {
        super();
        this.supplier = null;
    }

    public NameSubmission(String familySearchId, WardMember supplier, byte[] pdf, Set<Ordinance> ordinances, boolean male, boolean checkedOut) {
        super(familySearchId, pdf, ordinances, male);
        this.supplier = supplier;
        this.checkedOut = checkedOut;
    }

    public WardMember getSupplier() {
        return supplier;
    }

    public void setSupplier(WardMember supplier) {
        this.supplier = supplier;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + super.hashCode();
        hash = 19 * hash + Objects.hashCode(this.supplier);
        hash = 19 * hash + (this.checkedOut ? 1 : 0);
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
        if (!Objects.equals(this.familySearchId, other.familySearchId)) {
            return false;
        }
        if (!Objects.equals(this.ordinances, other.ordinances)) {
			return false;
		}
        if (!Objects.equals(this.male, other.male)) {
			return false;
		}
        if (this.checkedOut != other.checkedOut) {
            return false;
        }
        return Objects.equals(this.supplier, other.supplier);
    }


}
