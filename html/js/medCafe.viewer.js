function processViewerImages(repId, patientId, patientRepId, data, type, tab_num)
{
		var server = $('#viewerImageName').text();
           	 
        $("#viewer" + tab_num).iviewer(
        {
            src: server
        });       

}