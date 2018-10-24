$(document).ready(function() {
	var allMembers = {};
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
	
	$(".existing-members").change(function(){
		$(this).find("option:selected").each(function(i, option){
			var member = allMembers[option.value];
			$('#wardMember input[name="ward-member-id"]').val(member.id);
			$('#wardMember input[name="ward-member-name"]').val(member.name);
			$('#wardMember input[name="ward-member-email"]').val(member.email);
			$('#wardMember input[name="ward-member-phone"]').val(member.phone);
		});
		$(this).val("");
	});
	$.get("api/member", function(members) {
		/*
		 * [ {
				"id" : "3",
				"name" : "Wayne Milward",
				"email" : "wayne@email.com",
				"phone" : "208-123-4312"
			  }, {
				"id" : "1",
				"name" : "Jeff Powell",
				"email" : "jeff@email.com",
				"phone" : "208-123-4567"
			  }, {
				"id" : "2",
				"name" : "Lyn Misner",
				"email" : "lyn@email.com",
				"phone" : "208-123-1234"
			  } ]
		 */
		$(".existing-members").empty().append(function(){
			var options = "<option value=''></option>";
			for (var i = 0; i < members.length; i++) {
				var member = members[i];
				allMembers[member.name] = member;
				options += "<option value='"+member.name+"' data-obj='"+member+"'>"+member.name+"</option>";
			}
			return options;
		});
	});
});
