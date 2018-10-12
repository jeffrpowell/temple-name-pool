package com.jeffrpowell.templenamepool;

import com.jeffrpowell.templenamepool.dao.MongoNamePoolDao;
import com.jeffrpowell.templenamepool.dao.NamePoolDao;
import com.jeffrpowell.templenamepool.model.mongo.TempleNamePoolCodecProvider;
import com.jeffrpowell.templenamepool.model.mongo.WardMemberCodec;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import javax.inject.Singleton;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class AppBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bindMongoClient();
        bind(MongoNamePoolDao.class).to(NamePoolDao.class).in(Singleton.class);
    }

    private void bindMongoClient() {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
			CodecRegistries.fromCodecs(new WardMemberCodec()),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build(), new TempleNamePoolCodecProvider())
        );
        MongoClientSettings settings = MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .build();
        bind(MongoClients.create(settings)).to(MongoClient.class);
    }

}
