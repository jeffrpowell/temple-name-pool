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
//                male: true,
//                supplier:{
//                    id: "1",
//                    email: "email@email.com",
//                    phone: "123-123-4567",
//                    name: "name"
//                }
//            };
			return {
				familySearchId: $(this).find("[name='name-id']").val(),
				pdf: $(this).find("[name='name-pdf']"),
				ordinances: $(this).find("[name='name-ordinances']:checked").map(function () {
					return $(this).data('enum-value');
				}).get(),
                male: $(this).find("[name='name-gender']:checked").val()
			};
		}).get();
        var formData = new FormData();
        formData.append("wardMember", new Blob([JSON.stringify(getWardMemberObject())], {type: "application/json"}));
        formData.append("numSubmissions", submissions.length);
        for (var i = 0; i < submissions.length; i++) {
            var submission = submissions[i];
            formData.append("familySearchId"+i, submission.familySearchId);
            formData.append("pdf"+i, submission.pdf[0].files[0]);
            formData.append("ordinances"+i, new Blob([JSON.stringify(submission.ordinances)], {type: "application/json"}));
            formData.append("male"+i, submission.male);
        }
		var request = $.ajax({
			url: "api/name",
			method: "POST",
			data: formData,
            processData: false,
            contentType: false,
			async: false,
			cache: false
		});

		request.done(function (msg) {});
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
