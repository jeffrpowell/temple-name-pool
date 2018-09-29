$(document).ready(function() {
	$newNameTemplate = "";
	
	$.get("tabs/newname/namefield.tmpl.html", function($template) {
		$newNameTemplate = $template;
		$("#newNames").append($newNameTemplate);
	});
	
	$("#addNames").click(function() {
		$("#newNames").append($newNameTemplate);
	});
});
