$(document).ready(function () {
	$newNameTemplate = "";

	$.get("tabs/newname/namefield.tmpl.html", function ($template) {
		$newNameTemplate = $template;
		$("#newNames").append($newNameTemplate);
	});

	$("#addNames").click(function () {
		var count = parseInt($("#addNewNames [name='new-name-count']").val());
		for (; count > 0; count--) {
			$("#newNames").append($newNameTemplate);
		}
	});

	$("#submitNames").click(function () {
		var submissions = $('#newNames .new-name').map(function () {
//            submission = {   
//                familySearchId: "123-4567",
//                pdf:[],
//                ordinances:[],
//                supplier:{
//                    id: "1",
//                    email: "email@email.com",
//                    phone: "123-123-4567",
//                    name: "name"
//                }
//            };
			return {
				familySearchId: $(this).find("[name='name-id']").val(),
				pdf: $(this).find("[name='name-pdf']").val(),
				ordinances: $(this).find("[name='name-ordinances']:checked").map(function () {
					return $(this).data('enum-value');
				}).get(),
				submitter: getWardMemberObject()
			};
		}).get();
		var request = $.ajax({
			url: "api/name",
			method: "POST",
			data: submissions,
			dataType: "json"
		});

		request.done(function (msg) {
			$("#log").html(msg);
		});

		request.fail(function (jqXHR, textStatus) {
			alert("Request failed: " + textStatus);
		});
	});

	function getWardMemberObject() {
//            {
//                id: "1",
//                name: "name",
//                email: "email@email.com",
//                phone: "123-123-4567"
//            }
		return {
			id: $("#wardMember [name='ward-member-id']").val(),
			name: $("#wardMember [name='ward-member-name']").val(),
			email: $("#wardMember [name='ward-member-email']").val(),
			phone: $("#wardMember [name='ward-member-phone']").val()
		};
	}

});
