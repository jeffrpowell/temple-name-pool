package com.jeffrpowell.templenamepool.ws;

import com.jeffrpowell.templenamepool.dao.NamePoolDao;
import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("name")
public class NameResource {
    
    private final NamePoolDao namePoolDao;

    @Inject
    public NameResource(NamePoolDao namePoolDao) {
        this.namePoolDao = namePoolDao;
    }
}
