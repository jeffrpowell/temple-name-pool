package com.jeffrpowell.templenamepool.ws;

import com.jeffrpowell.templenamepool.dao.NamePoolDao;
import com.jeffrpowell.templenamepool.model.WardMember;
import java.util.Set;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("member")
public class WardMemberResource {
    
    private final NamePoolDao namePoolDao;

	@Inject
    public WardMemberResource(NamePoolDao namePoolDao) {
        this.namePoolDao = namePoolDao;
    }
    
    @GET
    public Set<WardMember> getWardMembers() {
        return namePoolDao.getWardMembers();
    }
}
