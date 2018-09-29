package com.jeffrpowell.templenamepool.model;

import java.time.LocalDate;
import java.util.Objects;

public class OverdueName {
    private final TempleName name;
    private final LocalDate dueDate;

    public OverdueName(TempleName name, LocalDate dueDate) {
        this.name = name;
        this.dueDate = dueDate;
    }

    public TempleName getName() {
        return name;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.name);
        hash = 53 * hash + Objects.hashCode(this.dueDate);
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
        final OverdueName other = (OverdueName) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.dueDate, other.dueDate);
    }
    
}
