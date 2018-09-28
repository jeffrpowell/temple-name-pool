package com.jeffrpowell.templenamepool.dao;

import com.jeffrpowell.templenamepool.model.Statistics;
import com.jeffrpowell.templenamepool.model.WardMember;
import com.jeffrpowell.templenamepool.model.NameSubmission;
import com.jeffrpowell.templenamepool.model.NameRequest;
import com.jeffrpowell.templenamepool.model.CompletedTempleOrdinances;
import com.jeffrpowell.templenamepool.model.TempleName;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface NamePoolDao {
    public void addNames(Collection<NameSubmission> names);
    public List<TempleName> checkoutNames(NameRequest request);
    public void markNamesAsCompleted(Collection<CompletedTempleOrdinances> names);
    public Statistics generateStatistics();
    public Map<WardMember, List<TempleName>> getCompletedOrdinancesBySubmitter();
    public Map<WardMember, List<CompletedTempleOrdinances>> getCompletedOrdinancesByCompleter();
}
