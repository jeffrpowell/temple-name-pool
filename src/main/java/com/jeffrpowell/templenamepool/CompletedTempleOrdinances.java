package com.jeffrpowell.templenamepool;

import java.util.Set;

public class CompletedTempleOrdinances extends TempleName{
    private final WardMember completer;
    
    public CompletedTempleOrdinances(String familySearchId, WardMember completer, Set<Ordinance> ordinances) {
        super(familySearchId, null, ordinances);
        this.completer = completer;
    }

    public WardMember getCompleter() {
        return completer;
    }
    
}
