$(document).ready(function() {
	$newNameTemplate = "";
	
	$.get("tabs/newname/namefield.tmpl.html", function($template) {
		$newNameTemplate = $template;
		$("#newNames").append($newNameTemplate);
	});
	
	$("#addNames").click(function() {
        var count = parseInt($("#addNewNames [name='new-name-count']").val());
		for (; count > 0; count--) {
            $("#newNames").append($newNameTemplate);
        }
	});
    
    $("#submitNames").click(function() {
        var submissions = $('#newNames .new-name').each(function(i, nameField){
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
                ordinances: $(this).find("[name='name-ordinances']:checked").val(),
                submitter: getWardMemberObject()
            };
        });
        console.log(submissions);
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
