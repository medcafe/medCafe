<html>
	<head>
		<title>Image Annotations</title>
		<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
		<script type="text/javascript" src="js/ui.all-1.7.1.js"></script>
		<script type="text/javascript" src="js/jquery.annotate.js"></script>
		<link type="text/css" rel="stylesheet" href="css/annotation.css" />
	
		<script language="javascript">
			$(window).load(function() {
				$("#toAnnotate").annotateImage({
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
			});
		</script>
	</head>
	<body>
		<div>
		<img id="toAnnotate" src="http://127.0.0.1:8080/medcafe/images/patients/1/chest-xray.jpg" alt="http://127.0.0.1:8080/medcafe/images/patients/1/chest-xray.jpg" width="600" height="398" />
			<!--  img id="toAnnotate" src="images/patients/1/mri.jpg" alt="MRI" width="600" height="398" /-->
		</div>
	</body>
</html>