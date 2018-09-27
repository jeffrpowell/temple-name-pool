package com.jeffrpowell.templenamepool;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface NamePoolDao {
    public void addNames(Collection<NameSubmission> names);
    public List<TempleName> checkoutNames(NameRequest request);
    public void markNamesAsCompleted(Collection<CompletedTempleOrdinances> names);
    public Statistics generateStatistics();
    public Map<WardMember, List<TempleName>> getCompletedOrdinancesBySubmitter();
    public Map<WardMember, List<CompletedTempleOrdinances>> getCompletedOrdinancesByCompleter();
}
