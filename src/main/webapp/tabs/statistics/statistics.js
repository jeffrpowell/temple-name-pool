$(document).ready(function () {
	
	$.get("api/stats", function(response) {
		/*
			{
				"percentOrdinancesCompleted" : 0.0,
				"numOrdinancesPerformed" : 0,
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
				"nameSuppliersAndCountOfSubmissions" : null,
				"nameRequestersAndCountOfOrdinancesCompleted" : null
			}
		 */
		buildPoolContentsChart(response.numMaleOrdinancesRemaining, response.numUnblockedMaleOrdinancesRemaining, response.numFemaleOrdinancesRemaining, response.numUnblockedFemaleOrdinancesRemaining);
	});
	
	function buildPoolContentsChart(totalMale, unblockedMale, totalFemale, unblockedFemale) {
		var ords = ["BAPTISM_CONFIRMATION", "INITIATORY", "ENDOWMENT", "SEALING_PARENTS", "SEALING_SPOUSE"];
		var unblockedMales = ords.map(o => unblockedMale[o]);
		var unblockedFemales = ords.map(o => unblockedFemale[o]);
		var blockedMales = ords.map(o => totalMale[o] - unblockedMale[o]);
		var blockedFemales = ords.map(o => totalFemale[o] - unblockedFemale[o]);
		new Chart($("#poolContents"), {
			type: 'bar',
			data: {
				labels: ["B/C", "Initiatory", "Endowment", "Sealing to Parents", "Sealing to Spouse"],
				datasets: [{
						label: 'Male unblocked',
						stack: "male",
						data: unblockedMales,
						backgroundColor: 'rgba(54, 162, 235, 0.2)',
						borderColor: 'rgba(54, 162, 235, 1)',
						borderWidth: 1
					},{
						label: 'Male blocked',
						stack: "male",
						data: blockedMales,
						backgroundColor: 'rgba(54, 162, 235, 0.1)',
						borderColor: 'rgba(54, 162, 235, 0.1)',
						borderWidth: 1
					},{
						label: 'Female unblocked',
						stack: "female",
						data: unblockedFemales,
						backgroundColor: 'rgba(255, 99, 132, 0.2)',
						borderColor: 'rgba(255,99,132,1)',
						borderWidth: 1
					},{
						label: 'Female blocked',
						stack: "female",
						data: blockedFemales,
						backgroundColor: 'rgba(255, 99, 132, 0.1)',
						borderColor: 'rgba(255,99,132,0.1)',
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
});
