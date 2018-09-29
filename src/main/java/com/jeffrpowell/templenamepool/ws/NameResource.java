package com.jeffrpowell.templenamepool.ws;

import com.jeffrpowell.templenamepool.dao.NamePoolDao;
import com.jeffrpowell.templenamepool.model.CompletedTempleOrdinances;
import com.jeffrpowell.templenamepool.model.NameRequest;
import com.jeffrpowell.templenamepool.model.NameSubmission;
import com.jeffrpowell.templenamepool.model.TempleName;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("name")
public class NameResource {
    
    private final NamePoolDao namePoolDao;

    @Inject
    public NameResource(NamePoolDao namePoolDao) {
        this.namePoolDao = namePoolDao;
    }
    
    @PUT
    public Response addNamesToPool(List<NameSubmission> names) {
        namePoolDao.addNames(names);
        return Response.ok().build();
    }
    
    @POST
    public List<TempleName> checkoutNames(NameRequest nameRequest) {
        return namePoolDao.checkoutNames(nameRequest);
    }
    
    @DELETE
    public Response markNamesAsCompleted(List<CompletedTempleOrdinances> completedOrdinances) {
        namePoolDao.markNamesAsCompleted(completedOrdinances);
        return Response.ok().build();
    }
}
