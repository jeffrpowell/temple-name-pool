package com.jeffrpowell.templenamepool.dao;

import com.jeffrpowell.templenamepool.model.CompletedTempleOrdinances;
import com.jeffrpowell.templenamepool.model.NameRequest;
import com.jeffrpowell.templenamepool.model.NameSubmission;
import com.jeffrpowell.templenamepool.model.OverdueName;
import com.jeffrpowell.templenamepool.model.Statistics;
import com.jeffrpowell.templenamepool.model.TempleName;
import com.jeffrpowell.templenamepool.model.WardMember;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;

@Service
public class MongoNamePoolDao implements NamePoolDao{
    private final MongoCollection nameCollection;
    
    @Inject
    public MongoNamePoolDao(MongoClient mongoClient) {
        MongoDatabase db = mongoClient.getDatabase("TempleNamePool");
        this.nameCollection = db.getCollection("names");
    }
    
    @Override
    public void addNames(Collection<NameSubmission> names) {
        nameCollection.insertMany(new ArrayList<>(names));
    }

    @Override
    public List<TempleName> checkoutNames(NameRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void markNamesAsCompleted(Collection<CompletedTempleOrdinances> names) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Statistics generateStatistics() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<WardMember, List<OverdueName>> getOverdueNameCheckouts() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<WardMember, List<TempleName>> getCompletedOrdinancesBySubmitter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<WardMember, List<CompletedTempleOrdinances>> getCompletedOrdinancesByCompleter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<WardMember> getWardMembers() {
        return (HashSet) nameCollection.aggregate(Arrays.asList(
            Aggregates.project(
                Projections.fields(
                    Projections.include("supplier"), Projections.excludeId())
            )
        )).into(new HashSet<>());
    }
    
}
