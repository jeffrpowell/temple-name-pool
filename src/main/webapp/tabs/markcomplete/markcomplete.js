$(document).ready(function () {
	
	var $nameTemplate = "";
	var checkedOutNames = {};

	$.get("tabs/markcomplete/checkedOutNameField.tmpl.html", function ($template) {
		$nameTemplate = $template;
	});
	
	function loadCheckedOutNames() {
		$.get("api/stats/wardMember", {includeNotDue: true}, function(response) {
			/*
				{
					"Lissa Hall" : [ {
					  "name" : {
						"familySearchId" : "L14D-T69",
						"pdf" : "",
						"ordinances" : [ "BAPTISM_CONFIRMATION" ],
						"male" : true
					  },
					  "dueDate" : [ 2018, 10, 11 ]
					} ]
				}
			 */
			checkedOutNames = response;
			refreshPage();
		});
	}
	
	loadCheckedOutNames();
	function refreshPage() {
		var wardMemberName = $("#wardMember [name='ward-member-name']").val();
		$("#checkedOutNamesList").empty();
		if (checkedOutNames.hasOwnProperty(wardMemberName)){
			var namesToDisplay = checkedOutNames[wardMemberName];
			for (var i = 0; i < namesToDisplay.length; i++){
				var checkedOutName = namesToDisplay[i];
				$("#checkedOutNamesList").append($nameTemplate);
				var $nameFieldRoot = $("#checkedOutNamesList .checked-out-name:last-child");
				$nameFieldRoot.find("input[name='complete-id']").val("FamilySearch Id: " + checkedOutName.name.familySearchId);
				var dateArr = checkedOutName.dueDate;
				$nameFieldRoot.find("input[name='complete-date']").val("Target Date: " + dateArr[0] + "-" + dateArr[1] + "-" + dateArr[2]);
				var ordinances = checkedOutName.name.ordinances;
				$nameFieldRoot.find("input[name='complete-ordinances']").each(function(){
					if (!ordinances.includes($(this).data('enum-value'))){
						$(this).prop('disabled', 'disabled');
						$(this).addClass("disabled");
					}
				});
			}
		}
	}
	
	$("#reloadCompletedNames").click(function(){
		loadCheckedOutNames();
	});
	
	$("#markNamesComplete").click(function() {
		var completions = $('#checkedOutNamesList .checked-out-name').map(function () {
			return {
				completed: $(this).find("[name='complete-completed']").prop('checked'),
				familySearchId: $(this).find("[name='complete-id']").val(),
				ordinances: $(this).find("[name='complete-ordinances']:checked").map(function () {
					return $(this).data('enum-value');
				}).get()
			};
		}).get();
        var formData = new FormData();
        formData.append("wardMember", new Blob([JSON.stringify(getWardMemberObject())], {type: "application/json"}));
		var count = 0;
        for (var i = 0; i < completions.length; i++) {
            var completion = completions[i];
			if (completion.completed){
				formData.append("familySearchId"+count, completion.familySearchId.replace("FamilySearch Id: ", ""));
				formData.append("ordinances"+count, new Blob([JSON.stringify(completion.ordinances)], {type: "application/json"}));
				count++;
			}
        }
        formData.append("numCompletions", count);
		var request = $.ajax({
			url: "api/name/complete",
			method: "POST",
			data: formData,
            processData: false,
            contentType: false,
			async: false,
			cache: false
		});

		request.done(function (msg) {
			loadCheckedOutNames();
		});
		request.fail(function (jqXHR, textStatus) {
			alert("Request failed: " + textStatus);
		});
	});
	
	function getWardMemberObject() {
		return {
			id: $("#wardMember [name='ward-member-id']").val(),
			name: $("#wardMember [name='ward-member-name']").val(),
			email: $("#wardMember [name='ward-member-email']").val(),
			phone: $("#wardMember [name='ward-member-phone']").val()
		};
	}
});