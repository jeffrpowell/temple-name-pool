$(document).ready(function () {
	
	$.get("api/stats", function(response) {
		/*
			{
				"numOrdinancesPerformed" : {
					"ENDOWMENT" : 26,
					"SEALING_PARENTS" : 0,
					"INITIATORY" : 15,
					"BAPTISM_CONFIRMATION" : 41,
					"SEALING_SPOUSE" : 0
				},
				"numMaleOrdinancesRemaining" : {
				  "INITIATORY" : 18,
				  "ENDOWMENT" : 31,
				  "SEALING_SPOUSE" : 48,
				  "BAPTISM_CONFIRMATION" : 14,
				  "SEALING_PARENTS" : 14
				},
				"numFemaleOrdinancesRemaining" : {
				  "INITIATORY" : 25,
				  "ENDOWMENT" : 43,
				  "SEALING_SPOUSE" : 0,
				  "BAPTISM_CONFIRMATION" : 25,
				  "SEALING_PARENTS" : 24
				},
				"numUnblockedMaleOrdinancesRemaining" : {
				  "INITIATORY" : 6,
				  "ENDOWMENT" : 15,
				  "SEALING_SPOUSE" : 39,
				  "BAPTISM_CONFIRMATION" : 14,
				  "SEALING_PARENTS" : 14
				},
				"numUnblockedFemaleOrdinancesRemaining" : {
				  "INITIATORY" : 2,
				  "ENDOWMENT" : 18,
				  "SEALING_SPOUSE" : 0,
				  "BAPTISM_CONFIRMATION" : 25,
				  "SEALING_PARENTS" : 24
				},
				"numCheckedOutMaleOrdinances" : {
					"ENDOWMENT" : 26,
					"SEALING_PARENTS" : 0,
					"INITIATORY" : 15,
					"BAPTISM_CONFIRMATION" : 41,
					"SEALING_SPOUSE" : 0
				},
				"numCheckedOutFemaleOrdinances" : {
				  "INITIATORY" : 18,
				  "ENDOWMENT" : 31,
				  "SEALING_SPOUSE" : 48,
				  "BAPTISM_CONFIRMATION" : 14,
				  "SEALING_PARENTS" : 14
				},
				"nameSuppliersAndCountOfSubmissions" : null,
				"nameRequestersAndCountOfOrdinancesCompleted" : {
					"Glenna Parkinson" : 1,
					"Wayne Millward" : 1,
					"Tyson Harlin" : 17,
					"Lissa Hall" : 27,
					"Rick Bassett" : 15,
					"David Walker" : 6,
					"Brenda Arnold" : 7
				}
			}
		 */
		buildPoolContentsChart(response.numMaleOrdinancesRemaining, response.numUnblockedMaleOrdinancesRemaining, response.numFemaleOrdinancesRemaining, response.numUnblockedFemaleOrdinancesRemaining, response.numCheckedOutMaleOrdinances, response.numCheckedOutFemaleOrdinances);
		buildCompletedOrdinancesChart(response.numOrdinancesPerformed);
		buildOrdinancesByRequesterTable(response.nameRequestersAndCountOfOrdinancesCompleted);
	});
	
	function buildPoolContentsChart(totalMale, unblockedMale, totalFemale, unblockedFemale, checkedOutMale, checkedOutFemale) {
		var ords = ["BAPTISM_CONFIRMATION", "INITIATORY", "ENDOWMENT", "SEALING_PARENTS", "SEALING_SPOUSE"];
		var unblockedMales = ords.map(o => unblockedMale[o]);
		var unblockedFemales = ords.map(o => unblockedFemale[o]);
		var blockedMales = ords.map(o => totalMale[o] - unblockedMale[o]);
		var blockedFemales = ords.map(o => totalFemale[o] - unblockedFemale[o]);
		var checkedOutMales = ords.map(o => checkedOutMale[o]);
		var checkedOutFemales = ords.map(o => checkedOutFemale[o]);
		new Chart($("#statsPoolContents"), {
			type: 'bar',
			data: {
				labels: ["B/C", "Initiatory", "Endowment", "Sealing to Parents", "Sealing to Spouse"],
				datasets: [{
						label: 'Male unblocked',
						stack: "male",
						data: unblockedMales,
						backgroundColor: 'rgba(54, 162, 235, 0.3)',
						borderColor: 'rgba(54, 162, 235, 1)',
						borderWidth: 1
					},{
						label: 'Male checked out',
						stack: "male",
						data: checkedOutMales,
						backgroundColor: 'rgba(25, 74, 107, 0.2)',
						borderColor: 'rgba(25, 74, 107, 0.3)',
						borderWidth: 1
					},{
						label: 'Male blocked',
						stack: "male",
						data: blockedMales,
						backgroundColor: 'rgba(126, 193, 238, 0.2)',
						borderColor: 'rgba(126, 193, 238, 0.3)',
						borderWidth: 1
					},{
						label: 'Female unblocked',
						stack: "female",
						data: unblockedFemales,
						backgroundColor: 'rgba(255, 99, 132, 0.3)',
						borderColor: 'rgba(255,99,132,1)',
						borderWidth: 1
					},{
						label: 'Female checked out',
						stack: "female",
						data: checkedOutFemales,
						backgroundColor: 'rgba(127, 49, 66, 0.2)',
						borderColor: 'rgba(127, 49, 66,0.3)',
						borderWidth: 1
					},{
						label: 'Female blocked',
						stack: "female",
						data: blockedFemales,
						backgroundColor: 'rgba(255, 175, 192, 0.2)',
						borderColor: 'rgba(255,99,132,0.3)',
						borderWidth: 1
					}]
			},
			options: {
				scales: {
					xAxes: [{
							stacked: true
						}],
					yAxes: [{
							stacked: true,
							ticks: {
								beginAtZero: true
							}
						}]
				}
			}
		});
	}
	function buildCompletedOrdinancesChart(completedOrdinancesRaw) {
		var ords = ["BAPTISM_CONFIRMATION", "INITIATORY", "ENDOWMENT", "SEALING_PARENTS", "SEALING_SPOUSE"];
		var completedOrdinances = ords.map(o => completedOrdinancesRaw[o]);
		new Chart($("#statsOrdinancesCompleted"), {
			type: 'bar',
			data: {
				labels: ["B/C", "Initiatory", "Endowment", "Sealing to Parents", "Sealing to Spouse"],
				datasets: [{
						label: 'Completed',
						data: completedOrdinances,
						backgroundColor: 'rgba(34, 178, 57, 0.2)',
						borderColor: 'rgba(34, 178, 57,1)', //34 178 57; 99 255 124; 44 246 219
						borderWidth: 1
					}]
			},
			options: {
				scales: {
					yAxes: [{
							ticks: {
								beginAtZero: true
							}
						}]
				}
			}
		});
	}
	function buildOrdinancesByRequesterTable(ordinancesByRequester) {
		$.each(ordinancesByRequester, function(name, count) {
			$("#statsOrdinancesByRequester").append("<tr><td>"+name+"</td><td>"+count+"</td><tr>");
		});
	}
});
