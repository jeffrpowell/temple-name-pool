package com.jeffrpowell.templenamepool;

import com.jeffrpowell.templenamepool.dao.InMemoryNamePoolDao;
import com.jeffrpowell.templenamepool.dao.NamePoolDao;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class AppBinder extends AbstractBinder{

    @Override
    protected void configure() {
        bind(InMemoryNamePoolDao.class).to(NamePoolDao.class);
    }
    
}
