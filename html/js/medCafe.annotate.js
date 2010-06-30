function processAnnotateImages(repId, patientId, patientRepId, data, type, tab_num)
{
		$("#toAnnotate" + tab_num).annotateImage({
					editable: true,
					useAjax: false,
					notes: [ { "top": 286,
							   "left": 161,
							   "width": 52,
							   "height": 37,
							   "text": "Of Interest",
							   "id": "e69213d0-2eef-40fa-a04b-0ed998f9f1f5",
							   "editable": false },
							 { "top": 134,
							   "left": 179,
							   "width": 68,
							   "height": 74,
							   "text": "What's here?",
							   "id": "e7f44ac5-bcf2-412d-b440-6dbb8b19ffbe",
							   "editable": true } ]
		});  

}