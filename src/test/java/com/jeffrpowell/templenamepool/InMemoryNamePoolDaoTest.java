package com.jeffrpowell.templenamepool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import static org.mockito.Mockito.spy;

public class InMemoryNamePoolDaoTest {
    
    private static final WardMember JEFF = new WardMember("1", "Jeff Powell", "jeff@email.com", "208-123-4567");
    private static final WardMember LYN = new WardMember("2", "Lyn Misner", "lyn@email.com", "208-123-1234");
    private static final WardMember WAYNE = new WardMember("3", "Wayne Milward", "wayne@email.com", "208-123-4312");
    
    private InMemoryNamePoolDao instance;
    private Map<String, TempleName> templeNames;
    private Map<WardMember, Collection<String>> submittedNames;
    private Map<Ordinance, Collection<String>> availableOrdinances;
    private Map<String, WardMember> checkedOutNames;
    private Map<WardMember, Collection<InMemoryNamePoolDao.TempleNameOrdinanceKey>> completedOrdinances;
    
    @Before
    public void setUp() {
        templeNames = spy(new ConcurrentHashMap<>());
        submittedNames = spy(new ConcurrentHashMap<>());
        availableOrdinances = spy(new ConcurrentHashMap<>());
        checkedOutNames = spy(new ConcurrentHashMap<>());
        completedOrdinances = spy(new ConcurrentHashMap<>());
        instance = new InMemoryNamePoolDao(templeNames, submittedNames, availableOrdinances, checkedOutNames, completedOrdinances);
    }

    @org.junit.Test
    public void testAddNames() {
        List<NameSubmission> input = Collections.singletonList(new NameSubmission("123-4567", LYN, new byte[0], EnumSet.of(Ordinance.ENDOWMENT, Ordinance.SEALING_SPOUSE)));
        instance.addNames(input);
        assertTrue("Didn't add name", templeNames.containsKey("123-4567"));
        assertTrue("Didn't make endowment available", availableOrdinances.containsKey(Ordinance.ENDOWMENT));
        assertTrue("Didn't make sealing available", availableOrdinances.containsKey(Ordinance.SEALING_SPOUSE));
        assertTrue("Not tracking submitter names", submittedNames.containsKey(LYN));
        assertEquals("Didn't track submitter name correctly", 1, submittedNames.get(LYN).size());
    }

    @org.junit.Test
    public void testAddMultipleNames() {
        List<NameSubmission> input = new ArrayList<>();
        input.add(new NameSubmission("123-4567", LYN, new byte[0], EnumSet.of(Ordinance.ENDOWMENT, Ordinance.SEALING_SPOUSE)));
        input.add(new NameSubmission("342-5892", LYN, new byte[0], EnumSet.of(Ordinance.BAPTISM_CONFIRMATION, Ordinance.INITIATORY, Ordinance.ENDOWMENT)));
        instance.addNames(input);
        assertEquals("Didn't add all names", 2, templeNames.size());
        assertEquals("Didn't add all endowments available", 2, availableOrdinances.get(Ordinance.ENDOWMENT).size());
        assertEquals("Didn't track submitter name correctly", 2, submittedNames.get(LYN).size());
    }

    @org.junit.Test
    public void testCheckoutNames() {
    }

    @org.junit.Test
    public void testMarkNamesAsCompleted() {
    }

    @org.junit.Test
    public void testGenerateStatistics() {
    }

    @org.junit.Test
    public void testGetCompletedOrdinances() {
    }
    
}
