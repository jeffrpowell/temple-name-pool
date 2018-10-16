package com.jeffrpowell.templenamepool.ws;

import com.jeffrpowell.templenamepool.dao.NamePoolDao;
import com.jeffrpowell.templenamepool.model.CompletedTempleOrdinances;
import com.jeffrpowell.templenamepool.model.OverdueName;
import com.jeffrpowell.templenamepool.model.Statistics;
import com.jeffrpowell.templenamepool.model.TempleName;
import com.jeffrpowell.templenamepool.model.WardMember;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("stats")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsResource {
    
    private final NamePoolDao namePoolDao;

	@Inject
    public StatisticsResource(NamePoolDao namePoolDao) {
        this.namePoolDao = namePoolDao;
    }
    
    @GET
    public Statistics getStatistics() {
        return namePoolDao.generateStatistics();
    }
	
	@GET
	@Path("wardMember")
	public Map<String, List<OverdueName>> getCheckedOutNamesByWardMember(@QueryParam("includeNotDue") @DefaultValue("false") boolean includeNotDue) {
		return namePoolDao.getOverdueNameCheckouts(includeNotDue).entrySet().stream()
			.collect(Collectors.toMap(
				entry -> entry.getKey().getName(),
				entry -> entry.getValue()
			));
	}
    
    @GET
    @Path("ordinances/submitter")
    public Map<WardMember, List<TempleName>> getCompletedOrdinancesBySubmitter() {
        return namePoolDao.getCompletedOrdinancesBySubmitter();
    }
    
    @GET
    @Path("ordinances/completer")
    public Map<WardMember, List<CompletedTempleOrdinances>> getCompletedOrdinancesByCompleter() {
        return namePoolDao.getCompletedOrdinancesByCompleter();
    }
}
