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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
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
    private Map<String, NameSubmission> submittedNames;
    private Map<Ordinance, Collection<String>> availableOrdinances;
    private Map<String, WardMember> checkedOutNames;
    private List<CompletedTempleOrdinances> completedOrdinances;
    
    @Before
    public void setUp() {
        templeNames = spy(new ConcurrentHashMap<>());
        submittedNames = spy(new ConcurrentHashMap<>());
        availableOrdinances = spy(new ConcurrentHashMap<>());
        availableOrdinances.putAll(EnumSet.allOf(Ordinance.class).stream().collect(Collectors.toMap(k->k, k -> new ArrayList<>())));
        checkedOutNames = spy(new ConcurrentHashMap<>());
        completedOrdinances = spy(new ArrayList<>());
        instance = new InMemoryNamePoolDao(templeNames, submittedNames, availableOrdinances, checkedOutNames, completedOrdinances);
    }

    @org.junit.Test
    public void testAddNames() {
        List<NameSubmission> input = Collections.singletonList(new NameSubmission(FSID1, LYN, new byte[0], EnumSet.of(Ordinance.ENDOWMENT, Ordinance.SEALING_SPOUSE)));
        instance.addNames(input);
        assertTrue("Didn't add name", templeNames.containsKey(FSID1));
        assertTrue("Didn't make endowment available", availableOrdinances.containsKey(Ordinance.ENDOWMENT));
        assertTrue("Didn't make sealing available", availableOrdinances.containsKey(Ordinance.SEALING_SPOUSE));
        assertTrue("Not tracking submitter names", submittedNames.containsKey(FSID1));
    }

    @org.junit.Test
    public void testAddMultipleNames() {
        List<NameSubmission> input = new ArrayList<>();
        input.add(new NameSubmission(FSID1, LYN, new byte[0], EnumSet.of(Ordinance.ENDOWMENT, Ordinance.SEALING_SPOUSE)));
        input.add(new NameSubmission(FSID2, LYN, new byte[0], EnumSet.of(Ordinance.BAPTISM_CONFIRMATION, Ordinance.INITIATORY, Ordinance.ENDOWMENT)));
        instance.addNames(input);
        assertEquals("Didn't add all names", 2, templeNames.size());
        assertEquals("Didn't add all endowments available", 2, availableOrdinances.get(Ordinance.ENDOWMENT).size());
        assertTrue("Not tracking submitter names", submittedNames.containsKey(FSID1));
        assertTrue("Not tracking submitter names", submittedNames.containsKey(FSID2));
    }

    @org.junit.Test
    public void testCheckoutNames() {
        availableOrdinances.put(Ordinance.ENDOWMENT, Stream.of(FSID1, FSID2).collect(Collectors.toList()));
        TempleName name1 = new TempleName(FSID1, new byte[0], EnumSet.of(Ordinance.ENDOWMENT));
        TempleName name2 = new TempleName(FSID2, new byte[0], EnumSet.of(Ordinance.ENDOWMENT));
        templeNames.put(FSID1, name1);
        templeNames.put(FSID2, name2);
        List<TempleName> checkout = instance.checkoutNames(new NameRequest(WAYNE, Ordinance.ENDOWMENT, 1, LocalDate.of(2018, Month.OCTOBER, 1)));
        verify(checkedOutNames).containsKey(FSID1);
        assertEquals("Didn't obey request limit", 1, checkout.size());
        assertEquals("Wrong name checked out", name1, checkout.get(0));
        assertTrue("Didn't add name to checkoutOut list", checkedOutNames.containsKey(FSID1)); //assumes always grabbing first names in list
        assertEquals("Wrong requester added", WAYNE, checkedOutNames.get(FSID1));
    }

    @org.junit.Test
    public void testCheckoutNames_none() {
        availableOrdinances.put(Ordinance.INITIATORY, Stream.of(FSID1, FSID2).collect(Collectors.toList()));
        TempleName name1 = new TempleName(FSID1, new byte[0], EnumSet.of(Ordinance.INITIATORY));
        TempleName name2 = new TempleName(FSID2, new byte[0], EnumSet.of(Ordinance.INITIATORY));
        templeNames.put(FSID1, name1);
        templeNames.put(FSID2, name2);
        List<TempleName> checkout = instance.checkoutNames(new NameRequest(WAYNE, Ordinance.ENDOWMENT, 1, LocalDate.of(2018, Month.OCTOBER, 1)));
        verify(checkedOutNames, never()).containsKey(anyString());
        assertTrue("Grabbed wrong ordinance", checkout.isEmpty());
        assertTrue("No names have been checked out", checkedOutNames.isEmpty());
    }

    @org.junit.Test
    public void testCheckoutNames_lessThanRequested() {
        availableOrdinances.put(Ordinance.INITIATORY, Stream.of(FSID1, FSID2).collect(Collectors.toList()));
        availableOrdinances.put(Ordinance.ENDOWMENT, Stream.of(FSID2, FSID3).collect(Collectors.toList()));
        TempleName name1 = new TempleName(FSID1, new byte[0], EnumSet.of(Ordinance.INITIATORY));
        TempleName name2 = new TempleName(FSID2, new byte[0], EnumSet.of(Ordinance.INITIATORY, Ordinance.ENDOWMENT));
        TempleName name3 = new TempleName(FSID3, new byte[0], EnumSet.of(Ordinance.INITIATORY));
        templeNames.put(FSID1, name1);
        templeNames.put(FSID2, name2);
        templeNames.put(FSID3, name3);
        List<TempleName> checkout = instance.checkoutNames(new NameRequest(WAYNE, Ordinance.INITIATORY, 5, LocalDate.of(2018, Month.OCTOBER, 1)));
        verify(checkedOutNames).containsKey(FSID1);
        verify(checkedOutNames).containsKey(FSID2);
        assertEquals("Tripped up on number of names checked out", 2, checkout.size());
        assertTrue("Missing a name", checkout.contains(name1));
        assertTrue("Missing a name", checkout.contains(name2));
        assertTrue("Didn't add name to checkoutOut list", checkedOutNames.containsKey(FSID1));
        assertTrue("Didn't add name to checkoutOut list", checkedOutNames.containsKey(FSID2));
    }

    @org.junit.Test
    public void testCheckoutNames_alreadyCheckedOut() {
        availableOrdinances.put(Ordinance.INITIATORY, Stream.of(FSID1, FSID2).collect(Collectors.toList()));
        availableOrdinances.put(Ordinance.ENDOWMENT, Stream.of(FSID2, FSID3).collect(Collectors.toList()));
        TempleName name1 = new TempleName(FSID1, new byte[0], EnumSet.of(Ordinance.INITIATORY));
        TempleName name2 = new TempleName(FSID2, new byte[0], EnumSet.of(Ordinance.INITIATORY, Ordinance.ENDOWMENT));
        TempleName name3 = new TempleName(FSID3, new byte[0], EnumSet.of(Ordinance.INITIATORY));
        templeNames.put(FSID1, name1);
        templeNames.put(FSID2, name2);
        templeNames.put(FSID3, name3);
        checkedOutNames.put(FSID1, JEFF);
        List<TempleName> checkout = instance.checkoutNames(new NameRequest(WAYNE, Ordinance.INITIATORY, 2, LocalDate.of(2018, Month.OCTOBER, 1)));
        verify(checkedOutNames).containsKey(FSID1);
        verify(checkedOutNames).containsKey(FSID2);
        assertEquals("Tripped up on number of names checked out", 1, checkout.size());
        assertFalse("Name1 is already checked out", checkout.contains(name1));
        assertTrue("Missing a name", checkout.contains(name2));
        assertEquals("Overwrote who checked out name1", JEFF, checkedOutNames.get(FSID1));
        assertEquals("Wrong person checking out name2", WAYNE, checkedOutNames.get(FSID2));
    }

    @org.junit.Test
    public void testMarkNamesAsCompleted() {
        TempleName nameSubmission = new TempleName(FSID1, new byte[0], EnumSet.of(Ordinance.BAPTISM_CONFIRMATION, Ordinance.INITIATORY, Ordinance.ENDOWMENT));
        templeNames.put(FSID1, nameSubmission);
        availableOrdinances.put(Ordinance.BAPTISM_CONFIRMATION, Stream.of(FSID1).collect(Collectors.toList()));
        availableOrdinances.put(Ordinance.INITIATORY, Stream.of(FSID1).collect(Collectors.toList()));
        availableOrdinances.put(Ordinance.ENDOWMENT, Stream.of(FSID1).collect(Collectors.toList()));
        checkedOutNames.put(FSID1, WAYNE);
        CompletedTempleOrdinances nameCompletion = new CompletedTempleOrdinances(FSID1, WAYNE, EnumSet.of(Ordinance.BAPTISM_CONFIRMATION));
        instance.markNamesAsCompleted(Collections.singletonList(nameCompletion));
        assertFalse("Didn't remove available ordinance", availableOrdinances.get(Ordinance.BAPTISM_CONFIRMATION).contains(FSID1));
        assertTrue("Removed an ordinance that wasn't completed yet", availableOrdinances.get(Ordinance.INITIATORY).contains(FSID1));
        assertTrue("Removed an ordinance that wasn't completed yet", availableOrdinances.get(Ordinance.ENDOWMENT).contains(FSID1));
        assertTrue("Didn't remove checkout lock", checkedOutNames.isEmpty());
        assertEquals("Wrong number of completed ordinances", 1, completedOrdinances.size());
        assertEquals("Wrong completed ordinance added", nameCompletion, completedOrdinances.get(0));
    }

    @org.junit.Test
    public void testMarkNamesAsCompleted_noneCompleted() {
        TempleName nameSubmission = new TempleName(FSID1, new byte[0], EnumSet.of(Ordinance.BAPTISM_CONFIRMATION, Ordinance.INITIATORY, Ordinance.ENDOWMENT));
        templeNames.put(FSID1, nameSubmission);
        availableOrdinances.put(Ordinance.BAPTISM_CONFIRMATION, Stream.of(FSID1).collect(Collectors.toList()));
        availableOrdinances.put(Ordinance.INITIATORY, Stream.of(FSID1).collect(Collectors.toList()));
        availableOrdinances.put(Ordinance.ENDOWMENT, Stream.of(FSID1).collect(Collectors.toList()));
        checkedOutNames.put(FSID1, WAYNE);
        CompletedTempleOrdinances nameCompletion = new CompletedTempleOrdinances(FSID1, WAYNE, EnumSet.noneOf(Ordinance.class));
        instance.markNamesAsCompleted(Collections.singletonList(nameCompletion));
        assertTrue("Removed an ordinance that wasn't completed yet", availableOrdinances.get(Ordinance.BAPTISM_CONFIRMATION).contains(FSID1));
        assertTrue("Removed an ordinance that wasn't completed yet", availableOrdinances.get(Ordinance.INITIATORY).contains(FSID1));
        assertTrue("Removed an ordinance that wasn't completed yet", availableOrdinances.get(Ordinance.ENDOWMENT).contains(FSID1));
        assertTrue("Didn't remove checkout lock", checkedOutNames.isEmpty());
        assertTrue("No ordinances should be completed", completedOrdinances.isEmpty());
    }

    @org.junit.Test
    public void testGenerateStatistics() {
    }

    @org.junit.Test
    public void testGetCompletedOrdinancesByCompleter() {
        TempleName name1 = new TempleName(FSID1, new byte[0], EnumSet.allOf(Ordinance.class));
        TempleName name2 = new TempleName(FSID2, new byte[0], EnumSet.allOf(Ordinance.class));
        TempleName name3 = new TempleName(FSID3, new byte[0], EnumSet.allOf(Ordinance.class));
        templeNames.put(FSID1, name1);
        templeNames.put(FSID2, name2);
        templeNames.put(FSID3, name3);
        completedOrdinances.add(new CompletedTempleOrdinances(FSID1, JEFF, EnumSet.allOf(Ordinance.class)));
        completedOrdinances.add(new CompletedTempleOrdinances(FSID2, JEFF, EnumSet.of(Ordinance.BAPTISM_CONFIRMATION, Ordinance.INITIATORY)));
        completedOrdinances.add(new CompletedTempleOrdinances(FSID2, JEFF, EnumSet.of(Ordinance.ENDOWMENT, Ordinance.SEALING_PARENTS, Ordinance.SEALING_SPOUSE)));
        completedOrdinances.add(new CompletedTempleOrdinances(FSID3, WAYNE, EnumSet.of(Ordinance.BAPTISM_CONFIRMATION)));
        completedOrdinances.add(new CompletedTempleOrdinances(FSID3, JEFF, EnumSet.of(Ordinance.INITIATORY, Ordinance.ENDOWMENT, Ordinance.SEALING_PARENTS, Ordinance.SEALING_SPOUSE)));
        Map<WardMember, List<CompletedTempleOrdinances>> completedOrdinancesByCompleter = instance.getCompletedOrdinancesByCompleter();
        assertTrue(completedOrdinancesByCompleter.containsKey(JEFF));
        assertTrue(completedOrdinancesByCompleter.containsKey(WAYNE));
        assertEquals(1, completedOrdinancesByCompleter.get(WAYNE).size());
        assertEquals(4, completedOrdinancesByCompleter.get(JEFF).size());
    }
    
}
