package com.jeffrpowell.templenamepool.dao;

import com.jeffrpowell.templenamepool.model.WardMember;
import com.jeffrpowell.templenamepool.model.CompletedTempleOrdinances;
import com.jeffrpowell.templenamepool.model.Ordinance;
import com.jeffrpowell.templenamepool.model.NameSubmission;
import com.jeffrpowell.templenamepool.model.NameRequest;
import com.jeffrpowell.templenamepool.model.OverdueName;
import com.jeffrpowell.templenamepool.model.TempleName;
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

public class InMemoryNamePoolDaoTest
{

	private static final WardMember JEFF = new WardMember("1", "Jeff Powell", "jeff@email.com", "208-123-4567");
	private static final WardMember LYN = new WardMember("2", "Lyn Misner", "lyn@email.com", "208-123-1234");
	private static final WardMember WAYNE = new WardMember("3", "Wayne Milward", "wayne@email.com", "208-123-4312");
	private static final String FSID1 = "123-4567";
	private static final String FSID2 = "765-4321";
	private static final String FSID3 = "987-6543";

	private InMemoryNamePoolDao instance;
	private Map<String, NameSubmission> submittedNames;
	private Map<Ordinance, Collection<String>> availableOrdinances;
	private Map<String, NameRequest> checkedOutNames;
	private List<CompletedTempleOrdinances> completedOrdinances;

	@Before
	public void setUp()
	{
		submittedNames = spy(new ConcurrentHashMap<>());
		availableOrdinances = spy(new ConcurrentHashMap<>());
		availableOrdinances.putAll(EnumSet.allOf(Ordinance.class).stream().collect(Collectors.toMap(k -> k, k -> new ArrayList<>())));
		checkedOutNames = spy(new ConcurrentHashMap<>());
		completedOrdinances = spy(new ArrayList<>());
		instance = new InMemoryNamePoolDao(submittedNames, availableOrdinances, checkedOutNames, completedOrdinances);
	}

	@org.junit.Test
	public void testAddNames()
	{
		List<NameSubmission> input = Collections.singletonList(new NameSubmission(FSID1, LYN, new byte[0], EnumSet.of(Ordinance.ENDOWMENT, Ordinance.SEALING_SPOUSE), true, false));
		instance.addNames(input);
		assertTrue("Didn't add name", submittedNames.containsKey(FSID1));
		assertTrue("Didn't make endowment available", availableOrdinances.containsKey(Ordinance.ENDOWMENT));
		assertTrue("Didn't make sealing available", availableOrdinances.containsKey(Ordinance.SEALING_SPOUSE));
		assertTrue("Not tracking submitter names", submittedNames.containsKey(FSID1));
	}

	@org.junit.Test
	public void testAddMultipleNames()
	{
		List<NameSubmission> input = new ArrayList<>();
		input.add(new NameSubmission(FSID1, LYN, new byte[0], EnumSet.of(Ordinance.ENDOWMENT, Ordinance.SEALING_SPOUSE), true, false));
		input.add(new NameSubmission(FSID2, LYN, new byte[0], EnumSet.of(Ordinance.BAPTISM_CONFIRMATION, Ordinance.INITIATORY, Ordinance.ENDOWMENT), true, false));
		instance.addNames(input);
		assertEquals("Didn't add all names", 2, submittedNames.size());
		assertEquals("Didn't add all endowments available", 2, availableOrdinances.get(Ordinance.ENDOWMENT).size());
		assertTrue("Not tracking submitter names", submittedNames.containsKey(FSID1));
		assertTrue("Not tracking submitter names", submittedNames.containsKey(FSID2));
	}

	@org.junit.Test
	public void testCheckoutNames()
	{
		availableOrdinances.put(Ordinance.ENDOWMENT, Stream.of(FSID1, FSID2).collect(Collectors.toList()));
		NameSubmission name1 = new NameSubmission(FSID1, LYN, new byte[0], EnumSet.of(Ordinance.ENDOWMENT), true, false);
		NameSubmission name2 = new NameSubmission(FSID2, LYN, new byte[0], EnumSet.of(Ordinance.ENDOWMENT), true, false);
		submittedNames.put(FSID1, name1);
		submittedNames.put(FSID2, name2);
		List<TempleName> checkout = instance.checkoutNames(new NameRequest(WAYNE, Ordinance.ENDOWMENT, 1, true, LocalDate.of(2018, Month.OCTOBER, 1)));
		verify(checkedOutNames).containsKey(FSID1);
		assertEquals("Didn't obey request limit", 1, checkout.size());
		assertEquals("Wrong name checked out", name1, checkout.get(0));
		assertTrue("Didn't add name to checkoutOut list", checkedOutNames.containsKey(FSID1)); //assumes always grabbing first names in list
		assertEquals("Wrong requester added", WAYNE, checkedOutNames.get(FSID1).getRequester());
	}

	@org.junit.Test
	public void testCheckoutNames_none()
	{
		availableOrdinances.put(Ordinance.INITIATORY, Stream.of(FSID1, FSID2).collect(Collectors.toList()));
		NameSubmission name1 = new NameSubmission(FSID1, LYN, new byte[0], EnumSet.of(Ordinance.INITIATORY), true, false);
		NameSubmission name2 = new NameSubmission(FSID2, LYN, new byte[0], EnumSet.of(Ordinance.INITIATORY), true, false);
		submittedNames.put(FSID1, name1);
		submittedNames.put(FSID2, name2);
		List<TempleName> checkout = instance.checkoutNames(new NameRequest(WAYNE, Ordinance.ENDOWMENT, 1, true, LocalDate.of(2018, Month.OCTOBER, 1)));
		verify(checkedOutNames, never()).containsKey(anyString());
		assertTrue("Grabbed wrong ordinance", checkout.isEmpty());
		assertTrue("No names have been checked out", checkedOutNames.isEmpty());
	}

	@org.junit.Test
	public void testCheckoutNames_lessThanRequested()
	{
		availableOrdinances.put(Ordinance.INITIATORY, Stream.of(FSID1, FSID2).collect(Collectors.toList()));
		availableOrdinances.put(Ordinance.ENDOWMENT, Stream.of(FSID2, FSID3).collect(Collectors.toList()));
		NameSubmission name1 = new NameSubmission(FSID1, LYN, new byte[0], EnumSet.of(Ordinance.INITIATORY), true, false);
		NameSubmission name2 = new NameSubmission(FSID2, LYN, new byte[0], EnumSet.of(Ordinance.INITIATORY, Ordinance.ENDOWMENT), true, false);
		NameSubmission name3 = new NameSubmission(FSID3, LYN, new byte[0], EnumSet.of(Ordinance.INITIATORY), true, false);
		submittedNames.put(FSID1, name1);
		submittedNames.put(FSID2, name2);
		submittedNames.put(FSID3, name3);
		List<TempleName> checkout = instance.checkoutNames(new NameRequest(WAYNE, Ordinance.INITIATORY, 5, true, LocalDate.of(2018, Month.OCTOBER, 1)));
		verify(checkedOutNames).containsKey(FSID1);
		verify(checkedOutNames).containsKey(FSID2);
		assertEquals("Tripped up on number of names checked out", 2, checkout.size());
		assertTrue("Missing a name", checkout.contains(name1));
		assertTrue("Missing a name", checkout.contains(name2));
		assertTrue("Didn't add name to checkoutOut list", checkedOutNames.containsKey(FSID1));
		assertTrue("Didn't add name to checkoutOut list", checkedOutNames.containsKey(FSID2));
	}

	@org.junit.Test
	public void testCheckoutNames_alreadyCheckedOut()
	{
		availableOrdinances.put(Ordinance.INITIATORY, Stream.of(FSID1, FSID2).collect(Collectors.toList()));
		availableOrdinances.put(Ordinance.ENDOWMENT, Stream.of(FSID2, FSID3).collect(Collectors.toList()));
		NameSubmission name1 = new NameSubmission(FSID1, LYN, new byte[0], EnumSet.of(Ordinance.INITIATORY), true, false);
		NameSubmission name2 = new NameSubmission(FSID2, LYN, new byte[0], EnumSet.of(Ordinance.INITIATORY, Ordinance.ENDOWMENT), true, false);
		NameSubmission name3 = new NameSubmission(FSID3, LYN, new byte[0], EnumSet.of(Ordinance.ENDOWMENT), true, false);
		submittedNames.put(FSID1, name1);
		submittedNames.put(FSID2, name2);
		submittedNames.put(FSID3, name3);
		checkedOutNames.put(FSID1, new NameRequest(JEFF, Ordinance.INITIATORY, 1, true, LocalDate.of(2018, Month.OCTOBER, 1)));
		List<TempleName> checkout = instance.checkoutNames(new NameRequest(WAYNE, Ordinance.INITIATORY, 2, true, LocalDate.of(2018, Month.OCTOBER, 1)));
		verify(checkedOutNames).containsKey(FSID1);
		verify(checkedOutNames).containsKey(FSID2);
		assertEquals("Tripped up on number of names checked out", 1, checkout.size());
		assertFalse("Name1 is already checked out", checkout.contains(name1));
		assertTrue("Missing a name", checkout.contains(name2));
		assertEquals("Overwrote who checked out name1", JEFF, checkedOutNames.get(FSID1).getRequester());
		assertEquals("Wrong person checking out name2", WAYNE, checkedOutNames.get(FSID2).getRequester());
	}

	@org.junit.Test
	public void testGetOverdueNameCheckouts_none()
	{
		Map<WardMember, List<OverdueName>> overdueNames = instance.getOverdueNameCheckouts();
		assertTrue(overdueNames.isEmpty());
	}

	@org.junit.Test
	public void testGetOverdueNameCheckouts_basicOverdue()
	{
		NameSubmission name = new NameSubmission(FSID1, LYN, new byte[0], EnumSet.allOf(Ordinance.class), true, false);
		submittedNames.put(FSID1, name);
		checkedOutNames.put(FSID1, new NameRequest(JEFF, Ordinance.BAPTISM_CONFIRMATION, 1, true, LocalDate.MIN));
		Map<WardMember, List<OverdueName>> overdueNames = instance.getOverdueNameCheckouts();
		assertEquals(1, overdueNames.size());
		assertTrue(overdueNames.containsKey(JEFF));
		assertEquals(new OverdueName(name, LocalDate.MIN), overdueNames.get(JEFF).get(0));
	}

	@org.junit.Test
	public void testGetOverdueNameCheckouts_basicNotOverdue()
	{
		NameSubmission name = new NameSubmission(FSID1, LYN, new byte[0], EnumSet.allOf(Ordinance.class), true, false);
		submittedNames.put(FSID1, name);
		checkedOutNames.put(FSID1, new NameRequest(JEFF, Ordinance.BAPTISM_CONFIRMATION, 1, true, LocalDate.MAX));
		Map<WardMember, List<OverdueName>> overdueNames = instance.getOverdueNameCheckouts();
		assertTrue(overdueNames.isEmpty());
	}

	@org.junit.Test
	public void testGetOverdueNameCheckouts_mixedOneRequester()
	{
		NameSubmission name1 = new NameSubmission(FSID1, LYN, new byte[0], EnumSet.allOf(Ordinance.class), true, false);
		NameSubmission name2 = new NameSubmission(FSID2, LYN, new byte[0], EnumSet.allOf(Ordinance.class), true, false);
		submittedNames.put(FSID1, name1);
		submittedNames.put(FSID2, name2);
		checkedOutNames.put(FSID1, new NameRequest(JEFF, Ordinance.BAPTISM_CONFIRMATION, 1, true, LocalDate.MAX));
		checkedOutNames.put(FSID2, new NameRequest(JEFF, Ordinance.BAPTISM_CONFIRMATION, 1, true, LocalDate.MIN));
		Map<WardMember, List<OverdueName>> overdueNames = instance.getOverdueNameCheckouts();
		assertEquals(1, overdueNames.size());
		assertTrue(overdueNames.containsKey(JEFF));
		assertEquals(new OverdueName(name2, LocalDate.MIN), overdueNames.get(JEFF).get(0));
	}

	@org.junit.Test
	public void testGetOverdueNameCheckouts_mixedTwoRequesters()
	{
		NameSubmission name1 = new NameSubmission(FSID1, LYN, new byte[0], EnumSet.allOf(Ordinance.class), true, false);
		NameSubmission name2 = new NameSubmission(FSID2, LYN, new byte[0], EnumSet.allOf(Ordinance.class), true, false);
		NameSubmission name3 = new NameSubmission(FSID3, LYN, new byte[0], EnumSet.allOf(Ordinance.class), true, false);
		submittedNames.put(FSID1, name1);
		submittedNames.put(FSID2, name2);
		submittedNames.put(FSID3, name3);
		checkedOutNames.put(FSID1, new NameRequest(JEFF, Ordinance.BAPTISM_CONFIRMATION, 1, true, LocalDate.MAX));
		checkedOutNames.put(FSID2, new NameRequest(JEFF, Ordinance.BAPTISM_CONFIRMATION, 1, true, LocalDate.MIN));
		checkedOutNames.put(FSID3, new NameRequest(WAYNE, Ordinance.BAPTISM_CONFIRMATION, 1, true, LocalDate.MIN));
		Map<WardMember, List<OverdueName>> overdueNames = instance.getOverdueNameCheckouts();
		assertEquals(2, overdueNames.size());
		assertTrue(overdueNames.containsKey(JEFF));
		assertTrue(overdueNames.containsKey(WAYNE));
		assertEquals(new OverdueName(name2, LocalDate.MIN), overdueNames.get(JEFF).get(0));
		assertEquals(new OverdueName(name3, LocalDate.MIN), overdueNames.get(WAYNE).get(0));
	}

	@org.junit.Test
	public void testMarkNamesAsCompleted()
	{
		NameSubmission nameSubmission = new NameSubmission(FSID1, LYN, new byte[0], EnumSet.of(Ordinance.BAPTISM_CONFIRMATION, Ordinance.INITIATORY, Ordinance.ENDOWMENT), true, false);
		submittedNames.put(FSID1, nameSubmission);
		availableOrdinances.put(Ordinance.BAPTISM_CONFIRMATION, Stream.of(FSID1).collect(Collectors.toList()));
		availableOrdinances.put(Ordinance.INITIATORY, Stream.of(FSID1).collect(Collectors.toList()));
		availableOrdinances.put(Ordinance.ENDOWMENT, Stream.of(FSID1).collect(Collectors.toList()));
		checkedOutNames.put(FSID1, new NameRequest(WAYNE, Ordinance.BAPTISM_CONFIRMATION, 2, true, LocalDate.of(2018, Month.OCTOBER, 1)));
		CompletedTempleOrdinances nameCompletion = new CompletedTempleOrdinances(FSID1, WAYNE, EnumSet.of(Ordinance.BAPTISM_CONFIRMATION), true);
		instance.markNamesAsCompleted(Collections.singletonList(nameCompletion));
		assertFalse("Didn't remove available ordinance", availableOrdinances.get(Ordinance.BAPTISM_CONFIRMATION).contains(FSID1));
		assertTrue("Removed an ordinance that wasn't completed yet", availableOrdinances.get(Ordinance.INITIATORY).contains(FSID1));
		assertTrue("Removed an ordinance that wasn't completed yet", availableOrdinances.get(Ordinance.ENDOWMENT).contains(FSID1));
		assertTrue("Didn't remove checkout lock", checkedOutNames.isEmpty());
		assertEquals("Wrong number of completed ordinances", 1, completedOrdinances.size());
		assertEquals("Wrong completed ordinance added", nameCompletion, completedOrdinances.get(0));
	}

	@org.junit.Test
	public void testMarkNamesAsCompleted_noneCompleted()
	{
		NameSubmission nameSubmission = new NameSubmission(FSID1, LYN, new byte[0], EnumSet.of(Ordinance.BAPTISM_CONFIRMATION, Ordinance.INITIATORY, Ordinance.ENDOWMENT), true, false);
		submittedNames.put(FSID1, nameSubmission);
		availableOrdinances.put(Ordinance.BAPTISM_CONFIRMATION, Stream.of(FSID1).collect(Collectors.toList()));
		availableOrdinances.put(Ordinance.INITIATORY, Stream.of(FSID1).collect(Collectors.toList()));
		availableOrdinances.put(Ordinance.ENDOWMENT, Stream.of(FSID1).collect(Collectors.toList()));
		checkedOutNames.put(FSID1, new NameRequest(WAYNE, Ordinance.BAPTISM_CONFIRMATION, 2, true, LocalDate.of(2018, Month.OCTOBER, 1)));
		CompletedTempleOrdinances nameCompletion = new CompletedTempleOrdinances(FSID1, WAYNE, EnumSet.noneOf(Ordinance.class), true);
		instance.markNamesAsCompleted(Collections.singletonList(nameCompletion));
		assertTrue("Removed an ordinance that wasn't completed yet", availableOrdinances.get(Ordinance.BAPTISM_CONFIRMATION).contains(FSID1));
		assertTrue("Removed an ordinance that wasn't completed yet", availableOrdinances.get(Ordinance.INITIATORY).contains(FSID1));
		assertTrue("Removed an ordinance that wasn't completed yet", availableOrdinances.get(Ordinance.ENDOWMENT).contains(FSID1));
		assertTrue("Didn't remove checkout lock", checkedOutNames.isEmpty());
		assertTrue("No ordinances should be completed", completedOrdinances.isEmpty());
	}

	@org.junit.Test
	public void testGenerateStatistics()
	{
	}

	@org.junit.Test
	public void testGetCompletedOrdinancesByCompleter()
	{
		NameSubmission name1 = new NameSubmission(FSID1, LYN, new byte[0], EnumSet.allOf(Ordinance.class), true, false);
		NameSubmission name2 = new NameSubmission(FSID2, LYN, new byte[0], EnumSet.allOf(Ordinance.class), true, false);
		NameSubmission name3 = new NameSubmission(FSID3, LYN, new byte[0], EnumSet.allOf(Ordinance.class), true, false);
		submittedNames.put(FSID1, name1);
		submittedNames.put(FSID2, name2);
		submittedNames.put(FSID3, name3);
		completedOrdinances.add(new CompletedTempleOrdinances(FSID1, JEFF, EnumSet.allOf(Ordinance.class), true));
		completedOrdinances.add(new CompletedTempleOrdinances(FSID2, JEFF, EnumSet.of(Ordinance.BAPTISM_CONFIRMATION, Ordinance.INITIATORY), true));
		completedOrdinances.add(new CompletedTempleOrdinances(FSID2, JEFF, EnumSet.of(Ordinance.ENDOWMENT, Ordinance.SEALING_PARENTS, Ordinance.SEALING_SPOUSE), true));
		completedOrdinances.add(new CompletedTempleOrdinances(FSID3, WAYNE, EnumSet.of(Ordinance.BAPTISM_CONFIRMATION), true));
		completedOrdinances.add(new CompletedTempleOrdinances(FSID3, JEFF, EnumSet.of(Ordinance.INITIATORY, Ordinance.ENDOWMENT, Ordinance.SEALING_PARENTS, Ordinance.SEALING_SPOUSE), true));
		Map<WardMember, List<CompletedTempleOrdinances>> completedOrdinancesByCompleter = instance.getCompletedOrdinancesByCompleter();
		assertTrue(completedOrdinancesByCompleter.containsKey(JEFF));
		assertTrue(completedOrdinancesByCompleter.containsKey(WAYNE));
		assertEquals(1, completedOrdinancesByCompleter.get(WAYNE).size());
		assertEquals(4, completedOrdinancesByCompleter.get(JEFF).size());
	}

	@org.junit.Test
	public void testGetCompletedOrdinancesBySubmitter()
	{
		TempleName name1 = new TempleName(FSID1, null, EnumSet.complementOf(EnumSet.of(Ordinance.SEALING_SPOUSE)), true);
		TempleName name2 = new TempleName(FSID2, null, EnumSet.allOf(Ordinance.class), true);
		TempleName name3 = new TempleName(FSID3, null, EnumSet.allOf(Ordinance.class), true);
		NameSubmission submit1 = new NameSubmission(FSID1, WAYNE, new byte[0], EnumSet.allOf(Ordinance.class), true, false);
		NameSubmission submit2 = new NameSubmission(FSID2, JEFF, new byte[0], EnumSet.allOf(Ordinance.class), true, false);
		NameSubmission submit3 = new NameSubmission(FSID3, LYN, new byte[0], EnumSet.allOf(Ordinance.class), true, false);
		submittedNames.put(FSID1, submit1);
		submittedNames.put(FSID2, submit2);
		submittedNames.put(FSID3, submit3);
		completedOrdinances.add(new CompletedTempleOrdinances(FSID1, JEFF, EnumSet.complementOf(EnumSet.of(Ordinance.SEALING_SPOUSE)), true));
		completedOrdinances.add(new CompletedTempleOrdinances(FSID2, JEFF, EnumSet.of(Ordinance.BAPTISM_CONFIRMATION, Ordinance.INITIATORY), true));
		completedOrdinances.add(new CompletedTempleOrdinances(FSID2, JEFF, EnumSet.of(Ordinance.ENDOWMENT, Ordinance.SEALING_PARENTS, Ordinance.SEALING_SPOUSE), true));
		completedOrdinances.add(new CompletedTempleOrdinances(FSID3, WAYNE, EnumSet.of(Ordinance.BAPTISM_CONFIRMATION), true));
		completedOrdinances.add(new CompletedTempleOrdinances(FSID3, JEFF, EnumSet.of(Ordinance.INITIATORY, Ordinance.ENDOWMENT, Ordinance.SEALING_PARENTS, Ordinance.SEALING_SPOUSE), true));
		Map<WardMember, List<TempleName>> completedOrdinancesBySubmitter = instance.getCompletedOrdinancesBySubmitter();
		assertTrue(completedOrdinancesBySubmitter.containsKey(WAYNE));
		assertTrue(completedOrdinancesBySubmitter.containsKey(JEFF));
		assertTrue(completedOrdinancesBySubmitter.containsKey(LYN));
		assertEquals(1, completedOrdinancesBySubmitter.get(WAYNE).size());
		assertEquals(1, completedOrdinancesBySubmitter.get(JEFF).size());
		assertEquals(1, completedOrdinancesBySubmitter.get(LYN).size());
		assertEquals(name1, completedOrdinancesBySubmitter.get(WAYNE).get(0));
		assertEquals(name2, completedOrdinancesBySubmitter.get(JEFF).get(0));
		assertEquals(name3, completedOrdinancesBySubmitter.get(LYN).get(0));
	}

}
