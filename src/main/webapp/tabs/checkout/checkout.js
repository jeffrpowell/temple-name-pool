$(document).ready(function () {
	
	$("#requestCheckout").click(function(){
		var nameRequest = {
			ordinance: $("#checkoutNames input[name='checkoutOrdinance']:checked").val(),
			requester: getWardMemberObject(),
			numRequested: parseInt($("#checkoutNames input[name='numNamesCheckout']").val()),
			targetDate: $("#checkoutNames input[name='returnDate']").val()
		};
		var request = $.ajax({
			url: "api/name/checkout",
			method: "POST",
			data: JSON.stringify(nameRequest),
			contentType: "application/json",
			xhrFields: {
				responseType: 'arraybuffer'
			},
			success: function (data, textStatus, jqXHR) {
				var fileBlob = new Blob([data], {type: 'application/octet-stream'});
				var contentDisposition = jqXHR.getResponseHeader('Content-Disposition');
				var filename = contentDisposition.split('"')[1];
				var a = document.createElement('a');
				var url = window.URL.createObjectURL(fileBlob);
				a.href = url;
				a.download = filename;
				document.body.appendChild(a);
				a.click();
				window.URL.revokeObjectURL(url);
				document.body.removeChild(a);
			}
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