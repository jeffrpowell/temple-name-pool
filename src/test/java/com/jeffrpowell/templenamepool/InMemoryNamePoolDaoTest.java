package com.jeffrpowell.templenamepool;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class InMemoryNamePoolDaoTest {
    
    private static final WardMember JEFF = new WardMember("1", "Jeff Powell", "jeff@email.com", "208-123-4567");
    private static final WardMember LYN = new WardMember("2", "Lyn Misner", "lyn@email.com", "208-123-1234");
    private static final WardMember WAYNE = new WardMember("3", "Wayne Milward", "wayne@email.com", "208-123-4312");
    private static final String FSID1 = "123-4567";
    private static final String FSID2 = "765-4321";
    private static final String FSID3 = "987-6543";
    
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
        List<NameSubmission> input = Collections.singletonList(new NameSubmission(FSID1, LYN, new byte[0], EnumSet.of(Ordinance.ENDOWMENT, Ordinance.SEALING_SPOUSE)));
        instance.addNames(input);
        assertTrue("Didn't add name", templeNames.containsKey(FSID1));
        assertTrue("Didn't make endowment available", availableOrdinances.containsKey(Ordinance.ENDOWMENT));
        assertTrue("Didn't make sealing available", availableOrdinances.containsKey(Ordinance.SEALING_SPOUSE));
        assertTrue("Not tracking submitter names", submittedNames.containsKey(LYN));
        assertEquals("Didn't track submitter name correctly", 1, submittedNames.get(LYN).size());
    }

    @org.junit.Test
    public void testAddMultipleNames() {
        List<NameSubmission> input = new ArrayList<>();
        input.add(new NameSubmission(FSID1, LYN, new byte[0], EnumSet.of(Ordinance.ENDOWMENT, Ordinance.SEALING_SPOUSE)));
        input.add(new NameSubmission(FSID2, LYN, new byte[0], EnumSet.of(Ordinance.BAPTISM_CONFIRMATION, Ordinance.INITIATORY, Ordinance.ENDOWMENT)));
        instance.addNames(input);
        assertEquals("Didn't add all names", 2, templeNames.size());
        assertEquals("Didn't add all endowments available", 2, availableOrdinances.get(Ordinance.ENDOWMENT).size());
        assertEquals("Didn't track submitter name correctly", 2, submittedNames.get(LYN).size());
    }

    @org.junit.Test
    public void testCheckoutNames() {
        availableOrdinances.put(Ordinance.ENDOWMENT, Stream.of(FSID1, FSID2).collect(Collectors.toList()));
        TempleName name = new TempleName(FSID1, new byte[0], EnumSet.of(Ordinance.ENDOWMENT));
        templeNames.put(FSID1, name);
        List<TempleName> checkout = instance.checkoutNames(new NameRequest(WAYNE, Ordinance.ENDOWMENT, 1, LocalDate.of(2018, Month.OCTOBER, 1)));
        verify(checkedOutNames).containsKey(FSID1);
        assertEquals("Didn't obey request limit", 1, checkout.size());
        assertEquals("Wrong name checked out", name, checkout.get(0));
        assertTrue("Didn't add name to checkoutOut list", checkedOutNames.containsKey(FSID1));
        assertEquals("Wrong requester added", WAYNE, checkedOutNames.get(FSID1));
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
