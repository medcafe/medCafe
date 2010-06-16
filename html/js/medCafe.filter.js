var retry = true;
function addFilter(callObj, server, tab_num, patientId, patientRepId)
{	
			
		$(callObj).delay(200,function()
		{
			var height = '380';
			var width ='800';
			var isiPad = navigator.userAgent.match(/iPad/i) != null;
			
			if (isiPad)
			{
				height = '380';
				width = '400';
			}
				
			iNettuts.refresh("yellow-widget" + tab_num);

			$("#aaa" + tab_num).append('<iframe frameborder="0" id="iframe'+ tab_num+ '" name="iframe'+ tab_num+ '" width="720" height="350"/>');
			$(callObj).delay(100,function()
			{
				$('#iframe'+ tab_num).attr('src', server +"?tab_num=" + tab_num + "&patient_id=" + patientId + "&rep_patient_id="  + patientRepId);
				
			} );

			//iNettuts.makeSortable();
			setHasContent(tab_num);
			
			//Try to add a scroll
			$(callObj).delay(100,function()
			{
				if (typeof isScrollable == 'undefined')
				{

					$.getScript('js/jScrollTouch.js', function()
					{
						$("#aaa" + tab_num).jScrollTouch({height:height,width:width});
					});
				}
				else
				{
					if (isiPad)
					{
						//console.log("medCafe.filter.js . addFilter script loaded ");
					}
					$("#aaa" + tab_num).jScrollTouch({height:height,width:width});
				}
			
			} );
		} );
		
}
