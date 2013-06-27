
		function split( val ) {
			return val.split( /;\s*/ );
		}
		function extractLast( term ) {
			return split( term ).pop();
		}
function insertDialog(cacheKey, repository)
{

	$('#dialogInsert').html('');
	$('#dialogInsert').append("<div id='insertAllergy'></div>");
	
		$.get("addAllergy.jsp?cacheKey="+cacheKey, function(data){				
				 			//Make sure to clear this out first
				 			$("#insertAllergy").html(data);
										


			



			$("#reactiondate").datepicker();
			$("#allergens").autocomplete({
			source: function(request, response){
			$.ajax({
				url:"/medcafe/c/repositories/"+repository+"/lookup/allergens/"+request.term,
				dataType:"json",
				success: function(data) {
													response($.map(data.allergensList, function(item) 
													{
													return { label: item,
																value: item
															  }
													}));
												}
						});
				},
			minLength:3,
			select: function (event, ui)
			{
				$("#allergens").val(ui.item.value)
			} 
				});
			




		$("#reactions").autocomplete({
			source: function(request, response){
			$.ajax({


				url:"/medcafe/c/repositories/"+repository+"/lookup/reactions/"+extractLast(request.term),
				dataType:"json",
				success: function(data) {
													response($.map(data.reactionsList, function(item) 
													{
													return { label: item,
																value: item
															  }
													}));
												}
						});
				},
			search: function() {
					// custom minLength
					var term = extractLast( this.value );
					if ( term.length < 1 ) {
						return false;
						}
			},

			select: function (event, ui)
			{
				var terms = split( this.value );
					// remove the current input
					terms.pop();
					// add the selected item
					terms.push( ui.item.value );
					// add placeholder to get the comma-and-space at the end
					terms.push( "" );
					this.value = terms.join( "; " );
					return false;

			} 
				});

	

	var marginHDialog = 25; marginWDialog  = 25;
	marginHDialog = $(window).height()-marginHDialog;

	var marginWDialog = $(window.body).width()-marginWDialog;
	$("#dialogInsert").dialog({
						 autoOpen: false,
						 modal:true,
						 resizable: true,
						 title: "Insert",
						 height: marginHDialog,
						 width: marginWDialog,
						 minWidth: 600,
						 buttons : {
						 	 "Submit" : function() {
						 	 $.ajax({
						 	 	type: "POST",
						 	 	url: $('#restlet').val(),
						 	 	data: $("#insertAllergyForm").serialize(),
						 	 	dataType: "json",
						 	 	success: function(data, status){
						 	 		alert(data.announce.message);
						 	 		
						 	 	},
						 	 	error: function(jqXHR, textStatus, errorThrown) {
						 	 		alert("Error performing insert: " + textStatus);
						 	 	}
						 	 });
						 	 $(this).dialog("destroy");
						 	 
						 	 $('#dialogInsert').html('');
						 	 },
						    "Cancel" : function() {

						   	 //Put in code to make sure that template code is removed
						     $(this).dialog("destroy");
						     $('#dialogInsert').html('');

						   }
						},
						close: function() {
                    		 $(this).dialog("destroy");
                    		 $('#dialogInsert').html('');

              			}
					});
      $("#ui-datepicker-div").removeClass("ui-helper-hidden-accessible");
		$("#dialogInsert").dialog("open");

					});	
}
