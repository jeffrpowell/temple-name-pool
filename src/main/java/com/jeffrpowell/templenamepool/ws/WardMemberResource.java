package com.jeffrpowell.templenamepool.ws;

import com.jeffrpowell.templenamepool.dao.NamePoolDao;
import com.jeffrpowell.templenamepool.model.WardMember;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("member")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WardMemberResource {
    
    private final NamePoolDao namePoolDao;

	@Inject
    public WardMemberResource(NamePoolDao namePoolDao) {
        this.namePoolDao = namePoolDao;
    }
    
    @GET
    public List<WardMember> getWardMembers() {
        Set<WardMember> members = namePoolDao.getWardMembers();
		return new ArrayList<>(members);
    }
}
