$(document).ready(function() {
	
	$.get("tabs/newname/newname.html", function($template) {
		$("#addNewNames").append($template);
	});
	
	$.get("tabs/checkout/checkout.html", function($template) {
		$("#checkoutNames").append($template);
	});
	
	$.get("tabs/markcomplete/markcomplete.html", function($template) {
		$("#completeNames").append($template);
	});
	
	$.get("tabs/statistics/statistics.html", function($template) {
		$("#stats").append($template);
	});
	
	$.get("api/member", function(members) {
		console.log(members);
	});
});
