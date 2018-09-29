package com.jeffrpowell.templenamepool.ws;

import com.jeffrpowell.templenamepool.dao.NamePoolDao;
import com.jeffrpowell.templenamepool.model.WardMember;
import java.util.ArrayList;
import java.util.List;
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
    public List<WardMember> getWardMembers() {
        Set<WardMember> members = namePoolDao.getWardMembers();
		members.add(new WardMember("1", "Jeff Powell", "jeff@email.com", "208-123-4567"));
		members.add(new WardMember("2", "Lyn Misner", "lyn@email.com", "208-123-1234"));
		members.add(new WardMember("3", "Wayne Milward", "wayne@email.com", "208-123-4312"));
		return new ArrayList<>(members);
    }
}
