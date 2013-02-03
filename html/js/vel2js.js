function v2js_announcements(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<h3 class="');
t.p( context.announce.type);
t.p('">');
t.p( context.announce.message);
t.p('</h3>');
return t.toString();
}
function v2js_basicDataView(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
t.p('<div class="repository-content">	');
var firstRepository = true;
var loopCount = 0;
t.p('	');
if (context.announce) {
t.p('		');
t.p( context.repos.announce.message);
t.p('	');
}
else {
t.p('		');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('						');
if (repos.announce && firstRepository == true) {
t.p('				');
t.p( repos.announce.message);
t.p('				');
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('			');
}
t.p('			');
if (!( repos.announce )) {
t.p('				');
if (firstRepository != true) {
t.p('					');
if (loopCount == 1) {
t.p('						<div id="collapseInfo');
t.p( context.widget_id);
t.p('">										<a href="');
t.p('#" class="collapse">More</a>						<div style="height: 25px"></div>						<div id="add_repos');
t.p( context.widget_id);
t.p('" style= "display: none">						<p></p>					');
}
t.p('					<br/>					<b><u>Repository: <i>');
t.p( repos.repository);
t.p('</i></u></b><br/><br/>				');
}
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('				');
t.p( repos);
t.p('			');
}
t.p('		');
}

t.p('		');
if (loopCount >= 2) {
t.p('				</div>			</div>		');
}
t.p('	');
}
t.p('</div>');
}
return t.toString();
}
function v2js_head(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<div class="widget" id="medCafeWidget-');
t.p( context.tab_set);
t.p( context.id);
t.p('">	<div id = ');
t.p( context.name);
t.p( context.patient_id);
t.p(' class = "id">');
return t.toString();
}
function v2js_inettutsHead(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<div class="widget color-');
t.p( context.color_num);
t.p('" id="medCafeWidget-');
t.p( context.tab_set);
t.p( context.id);
t.p('"><div id = ');
t.p( context.name);
t.p( context.patient_id);
t.p(' class = "id">    	<div style="cursor: move;" class="widget-head">         <a href="');
t.p('#" class="collapse">COLLAPSE</a><h3>');
t.p( context.label);
t.p('</h3><a href="');
t.p('#" class="remove">CLOSE</a><a href="');
t.p('#" class="edit">EDIT</a><a href="');
t.p('#" class="maximize">MAXIMIZE</a>    	</div>    <div class="edit-box" style="display: none;">        <ul>            <li class="item">                <label>Change the title?</label>                <input value="');
t.p( context.label);
t.p('"/>            </li>           <li class="item">            <label>Available colors:</label>            <ul class="colors"><li class="color-1"></li><li class="color-2"></li><li class="color-3"></li><li class="color-4"></li><li class="color-5"></li><li class="color-6"></li></ul>        </li>        </ul>    </div>    <div class="ui-widget-content widget-content no-copy" id="widget-content');
t.p( context.id);
t.p('">        <span>            <div id="aaa');
t.p( context.id);
t.p('" class="no-copy">   ');
return t.toString();
}
function v2js_inettutsTail(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('            </div>        </span>        <div id="dialog');
t.p( context.id);
t.p('">            <div id="modalaaa');
t.p( context.id);
t.p('">            </div>        	</div>    	</div>    </div></div>');
return t.toString();
}
function v2js_listAddress(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<table cellpadding="1.0" cellspacing="1.0" border="0"  >		');
for (var i1=0;  i1<context.address.length; i1++) {
var patient_address = context.address[i1];
velocityCount = i1;
t.p('    ');
if (patient_address.street) {
t.p('    	<tr class="gradeX"><td>Street: </td><td value="');
t.p( patient_address.street);
t.p('">');
t.p( patient_address.street);
t.p('</td></tr>	');
}
else {
t.p('	   	<tr class="gradeX"><td>Street: </td><td><input name="street_address" type="text">&nbsp</input></td></tr>		');
}
t.p('		');
if (patient_address.city) {
t.p('		<tr class="gradeX"><td>City: </td><td value="');
t.p( patient_address.city);
t.p('">');
t.p( patient_address.city);
t.p('</td></tr>	');
}
else {
t.p('	   	<tr class="gradeX"><td>City: </td><td><input name="city_address" type="text">&nbsp</input></td></tr>		');
}
t.p('		');
if (patient_address.state) {
t.p('		<tr class="gradeX"><td>State: </td><td value="');
t.p( patient_address.state);
t.p('">');
t.p( patient_address.state);
t.p('</td></tr>	');
}
else {
t.p('	   	<tr class="gradeX"><td>State: </td><td><input name="state_address" type="text">&nbsp</input></td></tr>	');
}
t.p('		');
if (patient_address.state) {
t.p('		<tr class="gradeX"><td>Zip: </td><td value="');
t.p( patient_address.zip);
t.p('">');
t.p( patient_address.zip);
t.p('</td></tr>	');
}
else {
t.p('	   	<tr class="gradeX"><td>Zip: </td><td><input name="zip_address" type="text">&nbsp</input></td></tr>	');
}
t.p('		');
if (patient_address.state) {
t.p('		<tr class="gradeX"><td>Country: </td><td value="');
t.p( patient_address.country);
t.p('">');
t.p( patient_address.country);
t.p('</td></tr>   ');
}
else {
t.p('	   	<tr class="gradeX"><td>Country: </td><td><input name="country_address" type="text">&nbsp</input></td></tr>	');
}
t.p('	');
}
velocityCount = 0;
t.p('</table>');
return t.toString();
}
function v2js_listAlertList(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
var printFirst = 0;
t.p('</p><br/><p>	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('		');
if (printFirst == 0) {
printFirst = 1;
t.p('			');
if (repos.allergies) {
t.p('				');
for (var i5=0;  i5<repos.allergies.length; i5++) {
var allergyDetail = repos.allergies[i5];
velocityCount = i5;
t.p('	   			');
t.p( allergyDetail.product.value);
t.p('<br/>				   		');
}
velocityCount = i2;
t.p('	   	');
}
t.p('		');
}
t.p('	');
}

}
return t.toString();
}
function v2js_listDates(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
for (var i1=0;  i1<context.years.length; i1++) {
var year = context.years[i1];
velocityCount = i1;
t.p('    <optgroup label="');
t.p( year.year);
t.p('">        ');
for (var i2=0;  i2<year.months.length; i2++) {
var month = year.months[i2];
velocityCount = i2;
t.p('    	');
if (month == year.startSelected) {
t.p('        	<option value="');
t.p( month);
t.p('/');
t.p( year.year);
t.p('" selected="true">');
t.p( month);
t.p('/');
t.p( year.year);
t.p('</option>        ');
}
else {
t.p('     		<option value="');
t.p( month);
t.p('/');
t.p( year.year);
t.p('">');
t.p( month);
t.p('/');
t.p( year.year);
t.p('</option>        ');
}
t.p('      	    ');
}
velocityCount = i1;
t.p('    </optgroup>');
}
velocityCount = 0;
return t.toString();
}
function v2js_listEndDates(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
for (var i1=0;  i1<context.years.length; i1++) {
var year = context.years[i1];
velocityCount = i1;
t.p('    <optgroup label="');
t.p( year.year);
t.p('">        ');
for (var i2=0;  i2<year.months.length; i2++) {
var month = year.months[i2];
velocityCount = i2;
t.p('    	');
if (month == year.endSelected) {
t.p('        	<option value="');
t.p( month);
t.p('/');
t.p( year.year);
t.p('" selected="true">');
t.p( month);
t.p('/');
t.p( year.year);
t.p('</option>        ');
}
else {
t.p('     		<option value="');
t.p( month);
t.p('/');
t.p( year.year);
t.p('">');
t.p( month);
t.p('/');
t.p( year.year);
t.p('</option>        ');
}
t.p('      	    ');
}
velocityCount = i1;
t.p('    </optgroup>');
}
velocityCount = 0;
return t.toString();
}
function v2js_listHistoryTemplate(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
var count = 0;
var trueFlag = "true";
t.p('<div class="" id="patient_history">');
for (var i1=0;  i1<context.categories.length; i1++) {
var category = context.categories[i1];
velocityCount = i1;
t.p('		<br/>			<div class="ui-state-highlight ui-corner-all" style="padding: .3em;">		<table cellpadding="0" cellspacing="0" border="0">					<tr class="gradeX">			<td value="');
t.p( category.category);
t.p('"><b>');
t.p( category.category);
t.p('</b></td>						');
for (var i2=0;  i2<category.symptoms.length; i2++) {
var symptom = category.symptoms[i2];
velocityCount = i2;
t.p('							');
if (symptom.hasSymptom == trueFlag) {
t.p('					<tr><td value="');
t.p( symptom.name);
t.p('">');
t.p( symptom.name);
t.p('</td><td><input type=\'checkbox\' checked="checked"  name="symptom_check" value="');
t.p( symptom.id);
t.p('"></input></td></tr>				');
}
else {
t.p('					<tr><td value="');
t.p( symptom.name);
t.p('">');
t.p( symptom.name);
t.p('</td><td><input type=\'checkbox\' name="symptom_check" value="');
t.p( symptom.id);
t.p('"></input></td></tr>				');
}
t.p('			');
}
velocityCount = i1;
t.p('					</tr>		</table>		</div>');
}
velocityCount = 0;
t.p('</tbody></div>');
return t.toString();
}
function v2js_listImageTags(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
var shapeCount = 0;
if (context.imageTags) {
t.p('	');
for (var i2=0;  i2<context.imageTags.length; i2++) {
var imageTag = context.imageTags[i2];
velocityCount = i2;
t.p('		<div class="shape" id="shape');
t.p( shapeCount);
t.p('" name="shape');
t.p( shapeCount);
t.p('" custom:type="');
t.p( imageTag.type);
t.p('" custom:x="');
t.p( imageTag.x);
t.p('" custom:y="');
t.p( imageTag.y);
t.p('" custom:width="');
t.p( imageTag.width);
t.p('" custom:height="');
t.p( imageTag.height);
t.p('" custom:color="');
t.p( imageTag.color);
t.p('" custom:offset="0"></div>	 		 	');
shapeCount = ( shapeCount + 1 );
t.p('	  		 ');
}

}
return t.toString();
}
function v2js_listImages(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('		');
if (repos.images && repos.repository == "local") {
t.p('		');
t.p('			');
for (var i4=0;  i4<repos.images.length; i4++) {
var image = repos.images[i4];
velocityCount = i4;
t.p('  				<div class="item">    			');
t.p('        		<img class="content" href="<:prefix:>');
t.p( image.source);
t.p('" src="<:prefix:>');
t.p( image.thumb);
t.p('"/>        		<div class="caption">');
t.p( image.name);
t.p('</div>    			</div>    		');
}
velocityCount = i2;
t.p('    	');
}
t.p('    ');
}

}
return t.toString();
}
function v2js_listInsertStatements(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
for (var i1=0;  i1<context.patients.length; i1++) {
var patient = context.patients[i1];
velocityCount = i1;
t.p('    insert into patient (rep_id, first_name, last_name, repository) values ( \'');
t.p( patient.id);
t.p('\', \'');
t.p( patient.name);
t.p('\',null,\'');
t.p( context.repository);
t.p('\');    <br/>  ');
}
velocityCount = 0;
return t.toString();
}
function v2js_listMedicationsGrid(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
var printFirst = 0;
var patcnt = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('		');
if (printFirst == 0) {
printFirst = 1;
t.p('			');
if (repos.medications) {
t.p('				{ "titleLabels": ["Name","Description","Start Date"],				 "modelNames": ["Type","Description","Date"],				"patientData":[				');
for (var i5=0;  i5<repos.medications.length; i5++) {
var medDetail = repos.medications[i5];
velocityCount = i5;
t.p('    		   	    ');
if (patcnt > 0) {
t.p('    		   	    ,    		   	    ');
}
patcnt = ( patcnt + 1 );
t.p('    		   	    {"id":"');
t.p( patcnt);
t.p('"                        		   		');
if (medDetail.codes) {
t.p('    		   		     ');
for (var i7=0;  i7<medDetail.codes.RxNorm.length; i7++) {
var rxCode = medDetail.codes.RxNorm[i7];
velocityCount = i7;
t.p('                    		   		         ,"Type": "RxNorm: ');
t.p( rxCode);
t.p('"     		   		     ');
}
velocityCount = i5;
t.p('                                        ');
}
else {
if (medDetail.type) {
t.p('                            ');
if (medDetail.type.displayName) {
t.p('                             ,"Type": "');
t.p( medDetail.type.displayName);
t.p('"                              ');
}
t.p('                    ');
}
}
t.p('    		   		');
if (medDetail.description) {
t.p('    		  		    ,"Description": "');
t.p( medDetail.description);
t.p('"     		  			    					');
if (medDetail.start_time) {
t.p('    			          , "Date":"');
t.p( medDetail.start_time);
t.p('"    			                   		            ');
}
t.p('    		            }    	   			');
}
t.p('    	   		');
}
velocityCount = i2;
t.p('	   			]}	   		');
}
t.p('		');
}
t.p('	');
}

}
return t.toString();
}
function v2js_listMedicineList(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
var printFirst = 0;
t.p('</p><br/><p>	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('		');
if (printFirst == 0) {
printFirst = 1;
t.p('			');
if (repos.medications) {
t.p('				');
for (var i5=0;  i5<repos.medications.length; i5++) {
var medicationDetail = repos.medications[i5];
velocityCount = i5;
t.p('				 *	   			');
if (medicationDetail.medicationInformation.manufacturedMaterial.freeTextBrandName) {
t.p('	  				   ');
t.p( medicationDetail.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('<br/>	  				');
}
else {
if (medicationDetail.narrative) {
t.p('	  					');
t.p( medicationDetail.narrative);
t.p('<br/>	  				');
}
}
t.p('				');
}
velocityCount = i2;
t.p('	   	');
}
t.p('	   ');
}
t.p('	');
}

}
return t.toString();
}
function v2js_listPatientAllergies(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
t.p('		<div class="repository-content">	');
var firstRepository = true;
var loopCount = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('					');
if (repos.announce && firstRepository == true) {
t.p('			');
t.p( repos.announce.message);
t.p('			<br>			<br>			Add a new allergy			<a onclick="insertDialog(\'');
t.p( context.cacheKey);
t.p('\', \'');
t.p( repos.repository);
t.p('\')" href="');
t.p('#" class="ui-icon ui-icon-circle-plus" style="float: right; margin-left: .3em;"></a>			');
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('		');
}
t.p('		');
if (firstRepository == true) {
t.p('		<a onclick="insertDialog(\'');
t.p( context.cacheKey);
t.p('\', \'');
t.p( repos.repository);
t.p('\')" href="');
t.p('#" class="ui-icon ui-icon-circle-plus" style="float: right; margin-left: .3em;"></a>		');
}
t.p('		');
if (repos.allergies) {
t.p('			');
if (firstRepository != true) {
t.p('				');
if (loopCount == 1) {
t.p('					<div id="collapseInfo');
t.p( context.widget_id);
t.p('">								<a href="');
t.p('#" class="collapse">More</a>					<div style="height: 25px"></div>					<div id="add_repos');
t.p( context.widget_id);
t.p('" style= "display: none">					<p></p>				');
}
t.p('				<br/>				<b><u>Repository: <i>');
t.p( repos.repository);
t.p('</i></u></b><br/><br/>			');
}
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('			');
for (var i4=0;  i4<repos.allergies.length; i4++) {
var allergy = repos.allergies[i4];
velocityCount = i4;
t.p('		    <b>');
t.p( allergy.product.value);
t.p('</b><br/>    			<span class="ui-icon ui-icon-triangle-1-e"  style="float: left; margin-right: .3em;"></span>Reaction: ');
t.p( allergy.reaction.value);
t.p('<br clear="all"/>			');
}
velocityCount = i2;
t.p('			<br/>		');
}
t.p('	');
}

t.p('	');
if (loopCount >= 2) {
t.p('			</div>		</div>	');
}
t.p('	</div>');
}
return t.toString();
}
function v2js_listPatientBio(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
var printed = 0;
var space = " ";
t.p('   ');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('   	');
if (repos.patient_data) {
t.p('   		');
if (printed == 0) {
t.p('   			<tr><th colspan="2" style="text-align:left;">   			');
for (var i5=0;  i5<repos.patient_data.name.given.length; i5++) {
var givenName = repos.patient_data.name.given[i5];
velocityCount = i5;
t.p('   				');
t.p( givenName);
t.p( space);
t.p('   			');
}
velocityCount = i2;
t.p('   			');
t.p( repos.patient_data.name.lastname);
t.p('</th></tr>   			');
printed = 1;
t.p('   			<tr><td colspan="2">   			');
if (repos.age) {
t.p('					');
t.p( repos.age);
t.p( space);
t.p('				');
}
t.p('				');
if (repos.patient_data.gender) {
t.p('					');
t.p( repos.patient_data.gender.displayName);
t.p('				');
}
t.p('     			</td></tr>     		');
}
t.p('     	');
}
t.p('	');
}

}
return t.toString();
}
function v2js_listPatientHistory(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
var printFirst = 0;
t.p('</p><br><p>	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('		');
if (printFirst == 0) {
printFirst = 1;
t.p('			<table cellpadding="0" cellspacing="0" border="0" class="display" id="problemListSummary">			<thead></thead><tbody>			');
if (repos.patient_history) {
t.p('				');
for (var i5=0;  i5<repos.patient_history.length; i5++) {
var history = repos.patient_history[i5];
velocityCount = i5;
t.p('	   			<tr>	  				');
if (history.color) {
t.p('	  					<td style="background-color:');
t.p( history.color);
t.p('">');
t.p( history.title);
t.p('	  				');
}
else {
t.p('						<td>');
t.p( history.title);
t.p('	  			 	');
}
t.p('	  			 	<div style="display: none;" class="ui-corner-all" id="detail">');
t.p( history.note);
t.p('</div></td> 	  			 	</tr>				');
}
velocityCount = i2;
t.p('			');
}
t.p('			</tbody>			<table>		');
}
t.p('	');
}

}
return t.toString();
}
function v2js_listPatientHistoryTable(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
t.p('<a onclick="alert(\'Method to add medical history goes here\')" href="');
t.p('#" class="ui-icon ui-icon-circle-plus" style="float: right; margin-left: .3em;"></a><div class="repository-content">	');
var firstRepository = true;
var loopCount = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('					');
if (repos.announce && firstRepository == true) {
t.p('			');
t.p( repos.announce.message);
t.p('			');
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('		');
}
t.p('		');
if (repos.patient_history) {
t.p('			');
if (firstRepository != true) {
t.p('				');
if (loopCount == 1) {
t.p('					<div id="collapseInfo');
t.p( context.widget_id);
t.p('">								<a href="');
t.p('#" class="collapse">More</a>					<div style="height: 25px"></div>					<div id="add_repos');
t.p( context.widget_id);
t.p('" style= "display: none">					<p></p>				');
}
t.p('				<br/>				<b><u>Repository: <i>');
t.p( repos.repository);
t.p('</i></u></b><br/><br/>			');
}
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('			');
for (var i4=0;  i4<repos.patient_history.length; i4++) {
var history = repos.patient_history[i4];
velocityCount = i4;
t.p('				<p class="');
t.p( history.priority);
t.p('">');
t.p( history.title);
t.p('<br/>				<span class="ui-icon ui-icon-triangle-1-e"  style="float: left; margin-right: .3em;"></span>');
t.p( history.note);
t.p('<br clear="all"/></p>			');
}
velocityCount = i2;
t.p('		');
}
t.p('	');
}

t.p('	');
if (loopCount >= 2) {
t.p('			</div>		</div>	');
}
t.p('	</div>');
}
return t.toString();
}
function v2js_listPatientImmunizations(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
t.p('<a onclick="alert(\'Method to add a new immunization goes here\')" href="');
t.p('#" class="ui-icon ui-icon-circle-plus" style="float: right; margin-left: .3em;"></a><div class="repository-content">	');
var firstRepository = true;
var loopCount = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('					');
if (repos.announce && firstRepository == true) {
t.p('			');
t.p( repos.announce.message);
t.p('			');
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('		');
}
t.p('		');
if (repos.immunizations) {
t.p('			');
if (firstRepository != true) {
t.p('				');
if (loopCount == 1) {
t.p('					<div id="collapseInfo');
t.p( context.widget_id);
t.p('">								<a href="');
t.p('#" class="collapse">More</a>					<div style="height: 25px"></div>					<div id="add_repos');
t.p( context.widget_id);
t.p('" style= "display: none">					<p></p>				');
}
t.p('				<br/>				<b><u>Repository: <i>');
t.p( repos.repository);
t.p('</i></u></b><br/><br/>			');
}
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('			');
var space = " ";
t.p('    ');
t.p('			');
for (var i4=0;  i4<repos.immunizations.length; i4++) {
var immunization = repos.immunizations[i4];
velocityCount = i4;
t.p('				<b>');
t.p( immunization.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('</b><br/>				<span class="ui-icon ui-icon-triangle-1-e"  style="float: left; margin-right: .3em;"></span>Administered by:');
t.p( space);
t.p('				');
t.p('				');
if (immunization.performer.person.name.given) {
t.p('					');
for (var i6=0;  i6<immunization.performer.person.name.given.length; i6++) {
var givenNameDetail = immunization.performer.person.name.given[i6];
velocityCount = i6;
t.p('						');
t.p( givenNameDetail);
t.p( space);
t.p(' 					');
}
velocityCount = i4;
t.p('				');
}
t.p('				');
t.p( immunization.performer.person.name.lastname);
t.p(' on');
t.p( space);
t.p('				');
t.p( immunization.administeredDate.month);
t.p('/');
t.p( immunization.administeredDate.day);
t.p('/');
t.p( immunization.administeredDate.year);
t.p('<br clear="all"/>				');
var comments = immunization.narrative.split('^');
t.p('				');
for (var i5=0;  i5<comments.length; i5++) {
var comment = comments[i5];
velocityCount = i5;
t.p('					<span class="ui-icon ui-icon-triangle-1-e"  style="float: left; margin-right: .3em;"></span>');
t.p( comment);
t.p('<br clear="all"/>				');
}
velocityCount = i4;
t.p('				<br/>			');
}
velocityCount = i2;
t.p('		');
}
t.p('	');
}

t.p('	');
if (loopCount >= 2) {
t.p('			</div>		</div>	');
}
t.p('	</div>');
}
return t.toString();
}
function v2js_listPatientMeds(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<table cellpadding="0" cellspacing="0" border="0" class="display" id="example');
t.p( context.id);
t.p('"><thead><tr><th></th><th></th></tr></thead><tbody><tr class="gradeX"><td>Patient ID</td><td value="');
t.p( context.id);
t.p('">');
t.p( context.id);
t.p('</td></tr><tr class="gradeX"><td>Patient Name</td><td value="');
t.p( context.name.given);
t.p(' ');
t.p( context.name.lastname);
t.p('">');
t.p( context.name.given);
t.p(' ');
t.p( context.name.lastname);
t.p('</td></tr><tr class="gradeX"><td>Gender</td><td value="');
t.p( context.gender.displayName);
t.p('">');
t.p( context.gender.displayName);
t.p('</td></tr><tr class="gradeX"><td>Birth Date</td><td value="');
t.p( context.birthtime.month);
t.p('/');
t.p( context.birthtime.day);
t.p('/');
t.p( context.birthtime.year);
t.p(' ');
t.p( context.birthtime.hour);
t.p(':');
t.p( context.birthtime.minute);
t.p('">');
t.p( context.birthtime.month);
t.p('/');
t.p( context.birthtime.day);
t.p('/');
t.p( context.birthtime.year);
t.p(' ');
t.p( context.birthtime.hour);
t.p(':');
t.p( context.birthtime.minute);
t.p('</td></tr></tbody><table>');
return t.toString();
}
function v2js_listPatientMedsVert(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
t.p('<a onclick="alert(\'Method to add a new medication goes here\')" href="');
t.p('#" class="ui-icon ui-icon-circle-plus" style="float: right; margin-left: .3em;"></a><div class="repository-content">	');
var firstRepository = true;
var loopCount = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('					');
if (repos.announce && firstRepository == true) {
t.p('			');
t.p( repos.announce.message);
t.p('			');
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('		');
}
t.p('		');
if (repos.medications) {
t.p('			');
if (firstRepository != true) {
t.p('				');
if (loopCount == 1) {
t.p('					<div id="collapseInfo');
t.p( context.widget_id);
t.p('">								<a href="');
t.p('#" class="collapse">More</a>					<div style="height: 25px"></div>					<div id="add_repos');
t.p( context.widget_id);
t.p('" style= "display: none">					<p></p>				');
}
t.p('				<br/>				<b><u>Repository: <i>');
t.p( repos.repository);
t.p('</i></u></b><br/><br/>			');
}
firstRepository = false;
loopCount = ( loopCount + 1 );
var space = " ";
t.p('    ');
t.p('			');
var oparen = "(";
var cparen = ")";
t.p('			');
for (var i4=0;  i4<repos.medications.length; i4++) {
var medication = repos.medications[i4];
velocityCount = i4;
t.p('				');
if (medication.description) {
t.p('    				<b>');
t.p( medication.description);
t.p('</b><br/>    			');
}
else {
t.p('    				<b>');
t.p( medication.free_text);
t.p('</b><br/>    			');
}
t.p('        		');
t.p('        		');
if (medication.start_time) {
t.p('            	<span class="ui-icon ui-icon-triangle-1-e"  style="float: left; margin-right: .3em;"></span>Started  ');
t.p( medication.start_time);
t.p('<br clear="all"/>        		');
}
t.p('        		');
if (medication.dose) {
t.p('            	<span class="ui-icon ui-icon-triangle-1-e"  style="float: left; margin-right: .3em;"></span>Dosage: ');
t.p( medication.dose);
t.p('<br clear="all"/>            	');
if (medication.deliveryMethod.displayName) {
t.p('                	');
t.p( oparen);
t.p(' ');
t.p( medication.deliveryMethod.displayName);
t.p(' ');
t.p( cparen);
t.p('            	');
}
t.p('        		');
}
t.p('        		');
if (medication.patientInstructions) {
t.p('            	<span class="ui-icon ui-icon-triangle-1-e"  style="float: left; margin-right: .3em;"></span>Instructions: ');
t.p( medication.patientInstructions);
t.p('<br clear="all"/>        		');
}
t.p('    			');
t.p('    			<br/>			');
}
velocityCount = i2;
t.p('		');
}
t.p('	');
}

t.p('	');
if (loopCount >= 2) {
t.p('			</div>		</div>	');
}
t.p('	</div>');
}
return t.toString();
}
function v2js_listPatientResultsVert(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
t.p('<a onclick="alert(\'Method to add a new lab result goes here\')" href="');
t.p('#" class="ui-icon ui-icon-circle-plus" style="float: right; margin-left: .3em;"></a><div class="repository-content">	');
var firstRepository = true;
var loopCount = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('					');
if (repos.announce && firstRepository == true) {
t.p('			');
t.p( repos.announce.message);
t.p('			');
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('		');
}
t.p('		');
if (repos.results) {
t.p('			');
if (firstRepository != true) {
t.p('				');
if (loopCount == 1) {
t.p('					<div id="collapseInfo');
t.p( context.widget_id);
t.p('">								<a href="');
t.p('#" class="collapse">More</a>					<div style="height: 25px"></div>					<div id="add_repos');
t.p( context.widget_id);
t.p('" style= "display: none">					<p></p>				');
}
t.p('				<br/>				<b><u>Repository: <i>');
t.p( repos.repository);
t.p('</i></u></b><br/><br/>			');
}
firstRepository = false;
loopCount = ( loopCount + 1 );
var space = " ";
t.p('    ');
t.p('			');
var oparen = "(";
var cparen = ")";
t.p('			');
for (var i4=0;  i4<repos.results.length; i4++) {
var result = repos.results[i4];
velocityCount = i4;
t.p('				');
if (result.description) {
t.p('    				<b>');
t.p( result.description);
t.p('</b><br/>    			');
}
else {
t.p('    				<b>');
t.p( result.free_text);
t.p('</b><br/>    			');
}
t.p('        		');
if (result.time) {
t.p('            	<span class="ui-icon ui-icon-triangle-1-e"  style="float: left; margin-right: .3em;"></span>Taken  ');
t.p( result.time);
t.p('<br clear="all"/>        		');
}
t.p('        		');
if (result.codes) {
t.p('                         ');
for (var i6=0;  i6<result.codes.LOINC.length; i6++) {
var snomed = result.codes.LOINC[i6];
velocityCount = i6;
t.p('                                            <span class="ui-icon ui-icon-triangle-1-e"  style="float: left; margin-right: .3em;"></span>LOINC Code: ');
t.p( result.codes.LOINC);
t.p('<br clear="all"/>                                                   ');
}
velocityCount = i4;
t.p('                ');
}
t.p('                                ');
if (result.values) {
t.p('                    ');
for (var i6=0;  i6<result.values.length; i6++) {
var resultVal = result.values[i6];
velocityCount = i6;
t.p('                                                    <span class="ui-icon ui-icon-triangle-1-e"  style="float: left; margin-right: .3em;"></span>Result: <b>');
t.p( resultVal.scalar);
t.p(' </b><br clear="all"/>                                                  ');
}
velocityCount = i4;
t.p('        		');
}
t.p('    			<br/>			');
}
velocityCount = i2;
t.p('		');
}
t.p('	');
}

t.p('	');
if (loopCount >= 2) {
t.p('			</div>		</div>	');
}
t.p('	</div>');
}
return t.toString();
}
function v2js_listPatientTable(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
t.p('<div class="repository-content">	');
var firstRepository = true;
var loopCount = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('					');
if (repos.announce && firstRepository == true) {
t.p('			');
t.p( repos.announce.message);
t.p('			');
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('		');
}
t.p('		');
if (repos.patient_data) {
t.p('			');
if (firstRepository != true) {
t.p('				');
if (loopCount == 1) {
t.p('					<div id="collapseInfo');
t.p( context.widget_id);
t.p('">								<a href="');
t.p('#" class="collapse">More</a>					<div style="height: 25px"></div>					<div id="add_repos');
t.p( context.widget_id);
t.p('" style= "display: none">					<p></p>				');
}
t.p('				<br/>				<b><u>Repository: <i>');
t.p( repos.repository);
t.p('</i></u></b>			');
}
firstRepository = false;
loopCount = ( loopCount + 1 );
var space = " ";
t.p('    ');
t.p('			');
var oparen = "(";
var cparen = ")";
t.p('			');
t.p('			');
t.p('			<p>			');
if (repos.patient_data.name.given) {
t.p('				<b>				');
for (var i5=0;  i5<repos.patient_data.name.given.length; i5++) {
var givenNameDetail = repos.patient_data.name.given[i5];
velocityCount = i5;
t.p('					');
t.p( givenNameDetail);
t.p( space);
t.p('				');
}
velocityCount = i2;
t.p('			');
}
t.p('			');
t.p( repos.patient_data.name.lastname);
t.p('</b></p>			<div style="float:left; margin-right:2em;">			');
t.p('			');
if (repos.patient_data.address) {
t.p('			    <i><u>Address(es)</u></i><br/>				');
for (var i5=0;  i5<repos.patient_data.address.length; i5++) {
var addressDetail = repos.patient_data.address[i5];
velocityCount = i5;
var linecount = 0;
t.p('					');
if (addressDetail.streetAddress) {
t.p('						');
for (var i7=0;  i7<addressDetail.streetAddress.length; i7++) {
var streetAdd = addressDetail.streetAddress[i7];
velocityCount = i7;
t.p('							');
if (linecount > 0) {
t.p(' ');
t.p( streetAdd);
t.p('<br/>							');
}
else {
t.p(' ');
t.p( streetAdd);
t.p('<br/>							');
}
linecount = ( linecount + 1 );
t.p('						');
}
velocityCount = i5;
t.p('					');
}
t.p('      		  ');
t.p( addressDetail.city);
t.p(', ');
t.p( addressDetail.stateOrProvince);
t.p('  ');
t.p( addressDetail.zip);
t.p('<br/>				');
}
velocityCount = i2;
t.p('			');
}
t.p('			');
t.p('			');
if (repos.patient_data.telecom) {
t.p('			    <i><u>Contact Information</u></i><br/>				');
for (var i5=0;  i5<repos.patient_data.telecom.length; i5++) {
var telecomDetail = repos.patient_data.telecom[i5];
velocityCount = i5;
t.p('					');
if (telecomDetail.type == "email" || telecomDetail.type == "im") {
t.p('						');
t.p( telecomDetail.value);
t.p(' ');
t.p( oparen);
t.p(' ');
t.p( telecomDetail.type);
t.p(' ');
t.p( cparen);
t.p('<br/>					');
}
else {
t.p('						');
t.p( telecomDetail.value);
t.p(' ');
t.p( oparen);
t.p(' ');
t.p( telecomDetail.use);
t.p(' ');
t.p( cparen);
t.p('<br/>					');
}
t.p('				');
}
velocityCount = i2;
t.p('			');
}
t.p('			</div>			');
t.p('			');
if (repos.patient_data.birthtime) {
t.p('				Born ');
t.p( repos.patient_data.birthtime.month);
t.p('/');
t.p( repos.patient_data.birthtime.day);
t.p('/');
t.p( repos.patient_data.birthtime.year);
t.p('    			');
if (repos.patient_data.birthPlace) {
t.p('        			in        			');
if (repos.patient_data.birthPlace.city && repos.patient_data.birthPlace.stateOrProvince) {
t.p('            		');
t.p( repos.patient_data.birthPlace.city);
t.p(', ');
t.p( repos.patient_data.birthPlace.stateOrProvince);
t.p('     			   ');
}
else {
if (repos.patient_data.birthPlace.stateOrProvince) {
t.p('     			   	');
t.p( repos.patient_data.birthPlace.stateOrProvince);
t.p('       			');
}
else {
if (repos.patient_data.birthPlace.city) {
t.p('            		');
t.p( repos.patient_data.birthPlace.city);
t.p('        			');
}
}
}
t.p('    			');
}
t.p('    			<br/>			');
}
t.p('			');
t.p('			');
if (repos.patient_data.language) {
t.p('				Primarily speaks ');
t.p( repos.patient_data.language);
t.p('<br/>			');
}
t.p('			');
t.p('			');
if (repos.patient_data.maritialStatus) {
t.p('				');
t.p( repos.patient_data.maritialStatus.displayName);
t.p('<br/>			');
}
t.p('			');
t.p('			');
if (repos.patient_data.race) {
t.p('    			Ethnicity:				');
var first = true;
t.p('				');
for (var i5=0;  i5<repos.patient_data.race.length; i5++) {
var raceDetail = repos.patient_data.race[i5];
velocityCount = i5;
t.p('					');
if (!( first )) {
t.p('						,');
t.p( space);
t.p('					');
}
t.p('					');
t.p( raceDetail.displayName);
t.p('					');
first = false;
t.p('				');
}
velocityCount = i2;
t.p('			<br/>			');
}
t.p('			<br clear="all"/>			');
t.p('			');
if (repos.patient_data.guardian && repos.patient_data.guardian.name) {
t.p('    			');
t.p('				<u>Guardian</u><br/>				');
if (repos.patient_data.guardian.name.given) {
t.p('	   	 		<b>					');
for (var i6=0;  i6<repos.patient_data.guardian.name.given.length; i6++) {
var givenNameDetail = repos.patient_data.guardian.name.given[i6];
velocityCount = i6;
t.p('						');
t.p( givenNameDetail);
t.p( space);
t.p('					');
}
velocityCount = i2;
t.p('				');
}
t.p('				');
t.p( repos.patient_data.guardian.name.lastname);
t.p('</b><br/>				');
t.p('				');
if (repos.patient_data.guardian.address) {
t.p('        			<i><u>Address(es)</u></i><br/>					');
for (var i6=0;  i6<repos.patient_data.guardian.address.length; i6++) {
var addressDetail = repos.patient_data.guardian.address[i6];
velocityCount = i6;
var linecount = 0;
t.p('						');
if (addressDetail.streetAddress) {
t.p('							');
for (var i8=0;  i8<addressDetail.streetAddress.length; i8++) {
var streetAdd = addressDetail.streetAddress[i8];
velocityCount = i8;
t.p('                    		');
if (linecount > 0) {
t.p(' ');
t.p( streetAdd);
t.p('<br/>								');
}
else {
t.p(' ');
t.p( streetAdd);
t.p('<br/>								');
}
linecount = ( linecount + 1 );
t.p('							');
}
velocityCount = i6;
t.p('						');
}
t.p('            		');
t.p( addressDetail.city);
t.p(', ');
t.p( addressDetail.stateOrProvince);
t.p('  ');
t.p( addressDetail.zip);
t.p('<br/>					');
}
velocityCount = i2;
t.p('				');
}
t.p('    			');
t.p('				');
if (repos.patient_data.guardian.telecom) {
t.p('        			<i><u>Contact Information</u></i><br/>					');
for (var i6=0;  i6<repos.patient_data.guardian.telecom.length; i6++) {
var telecomDetail = repos.patient_data.guardian.telecom[i6];
velocityCount = i6;
t.p('						');
if (telecomDetail.type == "email" || telecomDetail.type == "im") {
t.p('							');
t.p( telecomDetail.value);
t.p(' ');
t.p( oparen);
t.p(' ');
t.p( telecomDetail.type);
t.p(' ');
t.p( cparen);
t.p('						');
}
else {
t.p('							');
t.p( telecomDetail.value);
t.p(' ');
t.p( oparen);
t.p(' ');
t.p( telecomDetail.use);
t.p(' ');
t.p( cparen);
t.p(' <br/>						');
}
t.p('					');
}
velocityCount = i2;
t.p('				');
}
t.p('			');
}
t.p('		');
}
t.p('	');
}

t.p('	');
if (loopCount >= 2) {
t.p('			</div>		</div>	');
}
t.p('	</div>');
}
return t.toString();
}
function v2js_listPatientVitals(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
var printFirst = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('		');
if (repos.vitals && printFirst == 0) {
t.p('			');
var print = 0;
printFirst = 1;
t.p('			');
for (var i4=0;  i4<repos.vitals.length; i4++) {
var vitalDetail = repos.vitals[i4];
velocityCount = i4;
t.p('				');
if (print == 0) {
t.p('					<tr><td>					Vitals on</td>					<td>');
t.p( vitalDetail.resultDateTime.low.month);
t.p('/');
t.p( vitalDetail.resultDateTime.low.day);
t.p('/					');
t.p( vitalDetail.resultDateTime.low.year);
t.p('@');
t.p( vitalDetail.resultDateTime.low.hour);
t.p(':					');
if (vitalDetail.resultDateTime.low.minute < 10) {
t.p('						0');
t.p( vitalDetail.resultDateTime.low.minute);
t.p('					');
}
else {
t.p('						');
t.p( vitalDetail.resultDateTime.low.minute);
t.p('					');
}
t.p('					</td></tr>					');
print = 1;
t.p('				');
}
t.p('				<tr><td>');
t.p( vitalDetail.resultType.value);
t.p('</td>				<td>				');
var alert = 0;
t.p('	   		');
if (vitalDetail.narrative != "") {
t.p('	   			');
if (vitalDetail.narrative.substring(0, 1) == "*") {
alert = 1;
t.p('	   				<font color = "red">					');
}
t.p('				');
}
t.p('				');
t.p( vitalDetail.resultValue);
t.p('				');
if (alert == 1) {
t.p('					</font>				');
}
t.p('				</td></tr>			   	');
}
velocityCount = i2;
t.p('	   ');
}
t.p('	');
}

}
return t.toString();
}
function v2js_listPatientsBookmarksTable(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
var count = 0;
t.p('	<a onclick="alert(\'Method to add a new allergy goes here\')" href="');
t.p('#" class="ui-icon ui-icon-circle-plus" style="float: right; margin-left: .3em;"></a>');
if (!( context.bookmarks )) {
t.p('	<i>No bookmarks defined for this patient.</i>');
}
else {
t.p('    ');
for (var i1=0;  i1<context.bookmarks.length; i1++) {
var bookmark = context.bookmarks[i1];
velocityCount = i1;
t.p('        <span class="ui-icon ui-icon-triangle-1-e"  style="float: left; margin-right: .3em;"></span>        <a href="');
t.p( bookmark.url);
t.p('" title="');
t.p( bookmark.description);
t.p('" target="_blank" class="qtip">');
t.p( bookmark.name);
t.p('</a>        <a onclick="alert(\'this should delete this bookmark\')" href="');
t.p('#" class="ui-icon ui-icon-trash"  style="float: right; margin-left: .3em;"></a>        <br clear="all"/>    ');
}
velocityCount = 0;
}
return t.toString();
}
function v2js_listPatientsSearchResults(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<table cellpadding="0" cellspacing="0" border="0" class="display" id="example');
t.p( context.repository);
t.p('"><thead><tr><th></th><th></th></tr></thead><tbody>');
if (context.patients) {
t.p('	');
for (var i2=0;  i2<context.patients.length; i2++) {
var patient = context.patients[i2];
velocityCount = i2;
t.p('    	<tr class="gradeX"><td value="');
t.p( patient.id);
t.p('"><span class="summary">');
t.p( patient.id);
t.p('</span></td>    	<td><input type="hidden" id="patient_');
t.p( patient.id);
t.p('" name="patient_');
t.p( patient.id);
t.p('" value="');
t.p( patient.name);
t.p('"/>');
t.p( patient.name);
t.p('</td>    	<td><input type=checkbox name="patient_rep_id" id="patient_rep_id" value="');
t.p( patient.id);
t.p('"></input></td>    	</tr>	');
}

}
t.p('</tbody></table>');
return t.toString();
}
function v2js_listPatientsTable(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<table cellpadding="0" cellspacing="0" border="0" class="display" id="Repository');
t.p( context.repository);
t.p('"><thead><tr><th></th><th></th></tr></thead><tbody>');
if (context.patients) {
t.p('	');
for (var i2=0;  i2<context.patients.length; i2++) {
var patient = context.patients[i2];
velocityCount = i2;
t.p('    	<tr class="gradeX"><td value="');
t.p( patient.id);
t.p('"><span class="summary"><a href="');
t.p('#" class="details">');
t.p( patient.id);
t.p('</a></span></td><td value="');
t.p( patient.name);
t.p('">');
t.p( patient.name);
t.p('</td></tr>	');
}

}
t.p('</tbody></table>');
return t.toString();
}
function v2js_listProblemList(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<table cellpadding="0" cellspacing="0" border="0" class="display" id="problemListSummary">');
if (context.repositoryList) {
var printFirst = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('		');
if (printFirst == 0) {
printFirst = 1;
t.p('			');
if (repos.problem) {
t.p('				');
for (var i5=0;  i5<repos.problem.length; i5++) {
var problemDetail = repos.problem[i5];
velocityCount = i5;
t.p('	   	<tr>	  		<td> ');
t.p( problemDetail.problemName);
t.p(' </td>			<td class="ui-corner-all">				');
t.p( problemDetail.problemDate.low.month);
t.p('/');
t.p( problemDetail.problemDate.low.day);
t.p('/');
t.p( problemDetail.problemDate.low.year);
t.p('			</td> 	   		</tr>	   		');
}
velocityCount = i2;
t.p('	   	');
}
t.p('		');
}
t.p('	');
}

}
t.p('</table>');
return t.toString();
}
function v2js_listProblemListGrid(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
var printFirst = 0;
var patcnt = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('		');
if (printFirst == 0) {
printFirst = 1;
t.p('			');
if (repos.problems.problem) {
t.p('				{ "titleLabels": ["Problem","Type","Problem Date"],				 "modelNames": ["Problem","Type","ProblemDate"],				"patientData":[				');
for (var i5=0;  i5<repos.problems.problem.length; i5++) {
var problemDetail = repos.problems.problem[i5];
velocityCount = i5;
t.p('		   	    ');
if (patcnt > 0) {
t.p('		   	    ,		   	    ');
}
patcnt = ( patcnt + 1 );
t.p('		   	    {"id":"');
t.p( patcnt);
t.p('"		   		');
if (problemDetail.description.text) {
t.p('		  		    ,"Problem": "');
t.p( problemDetail.description.text);
t.p('" 		  			');
}
else {
if (problemDetail.description.code) {
t.p('		  					  				');
var codecnt = 0;
t.p('		  				');
for (var i7=0;  i7<problemDetail.description.code.length; i7++) {
var code = problemDetail.description.code[i7];
velocityCount = i7;
t.p('		  					');
if (codecnt > 0) {
t.p('		  						;		  					');
}
t.p('		  					,"code": "');
t.p( code.value);
t.p('"		  					');
if (code.codingSystem) {
t.p('		  						(');
t.p( code.codingSystem);
t.p(')		  					');
}
codecnt = ( codecnt + 1 );
t.p('		  				');
}
velocityCount = i5;
t.p('		  						  				');
}
else {
t.p('		  						  				');
}
}
t.p('									');
if (problemDetail.dateTime) {
t.p('		                ');
for (var i7=0;  i7<problemDetail.dateTime.length; i7++) {
var dateElem = problemDetail.dateTime[i7];
velocityCount = i7;
t.p('		                	');
if (dateElem.type.text) {
t.p('		                		,"Type": "');
t.p( dateElem.type.text);
t.p('"		                	');
}
t.p('		                	');
if (dateElem.exactDateTime.length() > 10) {
t.p('			                , "ProblemDate":"');
t.p( dateElem.exactDateTime.substring(0, 10));
t.p('"			                ');
}
else {
t.p('			                ,"ProblemDate":"');
t.p( dateElem.exactDateTime);
t.p('"			                ');
}
t.p('		                ');
}
velocityCount = i5;
t.p('		            ');
}
t.p('		            }	   			');
}
velocityCount = i2;
t.p('	   			]}	   		');
}
t.p('		');
}
t.p('	');
}

}
return t.toString();
}
function v2js_listProblemListTable(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
t.p('<a onclick="alert(\'Method to add a new problem goes here\')" href="');
t.p('#" class="ui-icon ui-icon-circle-plus" style="float: right; margin-left: .3em;"></a><div class="repository-content">	');
var firstRepository = true;
var loopCount = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('					');
if (repos.announce && firstRepository == true) {
t.p('			');
t.p( repos.announce.message);
t.p('			');
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('		');
}
t.p('		');
if (repos.problem) {
t.p('			');
if (firstRepository != true) {
t.p('				');
if (loopCount == 1) {
t.p('					<div id="collapseInfo');
t.p( context.widget_id);
t.p('">								<a href="');
t.p('#" class="collapse">More</a>					<div style="height: 25px"></div>					<div id="add_repos');
t.p( context.widget_id);
t.p('" style= "display: none">					<p></p>				');
}
t.p('				<br/>				<b><u>Repository: <i>');
t.p( repos.repository);
t.p('</i></u></b><br/><br/>			');
}
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('			');
for (var i4=0;  i4<repos.problem.length; i4++) {
var problemDetail = repos.problem[i4];
velocityCount = i4;
t.p('   			 ');
if (problemDetail.narrative == "A") {
var status = "Active";
t.p('   			 ');
}
else {
var status = "Inactive";
t.p('			    ');
}
t.p('			    <p class="');
t.p( status);
t.p('"><b>');
t.p( problemDetail.problemName);
t.p('</b><br/>			    <span class="ui-icon ui-icon-triangle-1-e"  style="float: left; margin-right: .3em;"></span>Code ');
t.p( problemDetail.problemCode.code);
t.p('<br clear="all"/>			    <span class="ui-icon ui-icon-triangle-1-e"  style="float: left; margin-right: .3em;"></span>Started ');
t.p( problemDetail.problemDate.low.month);
t.p('/');
t.p( problemDetail.problemDate.low.day);
t.p('/');
t.p( problemDetail.problemDate.low.year);
t.p(' (');
t.p( status);
t.p(')<br clear="all"/>			    </p>			');
}
velocityCount = i2;
t.p('		');
}
t.p('	');
}

t.p('	');
if (loopCount >= 2) {
t.p('			</div>		</div>	');
}
t.p('	</div>');
}
return t.toString();
}
function v2js_listProceduresGrid(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
var printFirst = 0;
var patcnt = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('		');
if (printFirst == 0) {
printFirst = 1;
t.p('			');
if (repos.procedures) {
t.p('				{ "titleLabels": ["Type","Description","Date"],				 "modelNames": ["Type","Description","Date"],				"patientData":[				');
for (var i5=0;  i5<repos.procedures.length; i5++) {
var procDetail = repos.procedures[i5];
velocityCount = i5;
t.p('    		   	    ');
if (patcnt > 0) {
t.p('    		   	    ,    		   	    ');
}
patcnt = ( patcnt + 1 );
t.p('    		   	    {"id":"');
t.p( patcnt);
t.p('"                        		   		');
if (procDetail.mood_code) {
t.p('    		   		     ,"Type": "');
t.p( procDetail.mood_code);
t.p('"                     ');
}
t.p('    		   		');
if (procDetail.description) {
t.p('    		  		    ,"Description": "');
t.p( procDetail.description);
t.p('"     		  			    					');
if (procDetail.time) {
t.p('    			          , "Date":"');
t.p( procDetail.time);
t.p('"    			                   		            ');
}
t.p('    		            }    	   			');
}
t.p('    	   		');
}
velocityCount = i2;
t.p('	   			]}	   		');
}
t.p('		');
}
t.p('	');
}

}
return t.toString();
}
function v2js_listRepositorySelect(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<select id="repositorySelect"><option value="">--Select--</option>');
for (var i1=0;  i1<context.repositories.length; i1++) {
var repository = context.repositories[i1];
velocityCount = i1;
t.p('    <option value="');
t.p( repository.name);
t.p('">');
t.p( repository.name);
t.p('</option>');
}
velocityCount = 0;
t.p('</search>');
return t.toString();
}
function v2js_listResultsGrid(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
var printFirst = 0;
var patcnt = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('		');
if (printFirst == 0) {
printFirst = 1;
t.p('			');
if (repos.results) {
t.p('				{ "titleLabels": ["Type","Description","Date"],				 "modelNames": ["Type","Description","Date"],				"patientData":[				');
for (var i5=0;  i5<repos.results.length; i5++) {
var resultDetail = repos.results[i5];
velocityCount = i5;
t.p('    		   	    ');
if (patcnt > 0) {
t.p('    		   	    ,    		   	    ');
}
patcnt = ( patcnt + 1 );
t.p('    		   	    {"id":"');
t.p( patcnt);
t.p('"    		   		');
if (resultDetail.mood_code) {
t.p('    		   		     ,"Type": "');
t.p( resultDetail.mood_code);
t.p('"                     ');
}
t.p('    		   		');
if (resultDetail.description) {
t.p('    		  		    ,"Description": "');
t.p( resultDetail.description);
t.p('"     		  			    					');
if (resultDetail.time) {
t.p('    		               	    			          , "Date":"');
t.p( resultDetail.time);
t.p('"    			                   		            ');
}
t.p('    		            }    	   			');
}
t.p('    	   		');
}
velocityCount = i2;
t.p('	   			]}	   		');
}
t.p('		');
}
t.p('	');
}

}
return t.toString();
}
function v2js_listSearchPatientsSelect(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<select id="patientsList"><option value="">--Select--</option>');
for (var i1=0;  i1<context.patients.length; i1++) {
var patient = context.patients[i1];
velocityCount = i1;
t.p('    <option value="');
t.p( patient.id);
t.p('">');
t.p( patient.first_name);
t.p(' ');
t.p( patient.last_name);
t.p('</option>');
}
velocityCount = 0;
t.p('</select>');
return t.toString();
}
function v2js_listSocialHistoryGrid(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
var printFirst = 0;
var patcnt = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('		');
if (printFirst == 0) {
printFirst = 1;
t.p('			');
if (repos.socialhistory) {
t.p('				{ "titleLabels": ["Type","Description","Date"],				 "modelNames": ["Type","Description","Date"],				"patientData":[				');
for (var i5=0;  i5<repos.socialhistory.length; i5++) {
var socialDetail = repos.socialhistory[i5];
velocityCount = i5;
t.p('    		   	    ');
if (patcnt > 0) {
t.p('    		   	    ,    		   	    ');
}
patcnt = ( patcnt + 1 );
t.p('    		   	    {"id":"');
t.p( patcnt);
t.p('"    		   		');
if (socialDetail.mood_code) {
t.p('    		   		     ,"Type": "');
t.p( socialDetail.mood_code);
t.p('"                     ');
}
t.p('    		   		');
if (socialDetail.version) {
t.p('    		  		    ,"Description": "');
t.p( socialDetail.version);
t.p('"     		  		');
}
t.p('    				');
if (socialDetail.time) {
t.p('    		               	    			          , "Date":"');
t.p( socialDetail.time);
t.p('"           		        ');
}
t.p('    		        }    	   		    	   	    ');
}
velocityCount = i2;
t.p('	   			]}	   		');
}
t.p('		');
}
t.p('	');
}

}
return t.toString();
}
function v2js_listStartDates(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
for (var i1=0;  i1<context.years.length; i1++) {
var year = context.years[i1];
velocityCount = i1;
t.p('    <optgroup label="');
t.p( year.year);
t.p('">        ');
for (var i2=0;  i2<year.months.length; i2++) {
var month = year.months[i2];
velocityCount = i2;
t.p('    	');
if (month == year.startSelected) {
t.p('        	<option value="');
t.p( month);
t.p('/');
t.p( year.year);
t.p('" selected="true">');
t.p( month);
t.p('/');
t.p( year.year);
t.p('</option>        ');
}
else {
t.p('     		<option value="');
t.p( month);
t.p('/');
t.p( year.year);
t.p('">');
t.p( month);
t.p('/');
t.p( year.year);
t.p('</option>        ');
}
t.p('      	    ');
}
velocityCount = i1;
t.p('    </optgroup>');
}
velocityCount = 0;
return t.toString();
}
function v2js_listSupportInfo(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
t.p('<a onclick="alert(\'Method to add a new contact goes here\')" href="');
t.p('#" class="ui-icon ui-icon-circle-plus" style="float: right; margin-left: .3em;"></a><div class="repository-content">	');
var firstRepository = true;
var loopCount = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('					');
if (repos.announce && firstRepository == true) {
t.p('			');
t.p( repos.announce.message);
t.p('			');
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('		');
}
t.p('		');
if (repos.supportInfo) {
t.p('			');
if (firstRepository != true) {
t.p('				');
if (loopCount == 1) {
t.p('					<div id="collapseInfo');
t.p( context.widget_id);
t.p('">								<a href="');
t.p('#" class="collapse">More</a>					<div style="height: 25px"></div>					<div id="add_repos');
t.p( context.widget_id);
t.p('" style= "display: none">					<p></p>				');
}
t.p('				<br/>				<b><u>Repository: <i>');
t.p( repos.repository);
t.p('</i></u></b><br/><br/>			');
}
firstRepository = false;
loopCount = ( loopCount + 1 );
t.p('			');
for (var i4=0;  i4<repos.supportInfo.length; i4++) {
var support = repos.supportInfo[i4];
velocityCount = i4;
t.p('			   ');
t.p('				');
var conType = 'Undefined';
t.p('				');
if (support.contactType.valueOf() == 'AGNT') {
conType = 'Agent';
t.p('				');
}
else {
if (support.contactType.valueOf() == 'ECON') {
conType = 'Emergency Contact';
t.p('				');
}
else {
if (support.contactType.valueOf() == 'CAREGIVER') {
conType = 'Caregiver';
t.p('				');
}
else {
if (support.contactType.valueOf() == 'NOK') {
conType = 'Next of Kin';
t.p('				');
}
else {
if (support.contactType.valueOf() == 'PRS') {
conType = support.contactRelationship.displayName;
t.p('				');
}
}
}
}
}
t.p('			    ');
t.p('			    <p><b>				');
if (support.contact.name.given) {
t.p('					');
for (var i6=0;  i6<support.contact.name.given.length; i6++) {
var givenNameDetail = support.contact.name.given[i6];
velocityCount = i6;
t.p('						');
t.p( givenNameDetail);
t.p( context.space);
t.p('					');
}
velocityCount = i4;
t.p('				');
}
t.p('				');
t.p( support.contact.name.lastname);
t.p('</b><br>				');
t.p( conType);
t.p('				');
if (support.contactRelationship.displayName) {
t.p('					( ');
t.p( support.contactRelationship.displayName);
t.p(' )				');
}
t.p('</p>				');
if (support.contact.address) {
t.p('			        <i><u>Address</u></i><br/>					');
for (var i6=0;  i6<support.contact.address.length; i6++) {
var addressDetail = support.contact.address[i6];
velocityCount = i6;
t.p('						');
if (addressDetail.streetAddress) {
t.p('							');
for (var i8=0;  i8<addressDetail.streetAddress.length; i8++) {
var streetAdd = addressDetail.streetAddress[i8];
velocityCount = i8;
t.p('								');
t.p( streetAdd);
t.p(' <br>							');
}
velocityCount = i6;
t.p('						');
}
t.p('						');
t.p( addressDetail.city);
t.p(', ');
t.p( addressDetail.stateOrProvince);
t.p('  ');
t.p( addressDetail.zip);
t.p(' <br/>					');
}
velocityCount = i4;
t.p('				');
}
t.p('				');
t.p('				');
if (support.contact.telecom) {
t.p('	    			<br/><i><u>Contact Information</u></i><br/>					');
for (var i6=0;  i6<support.contact.telecom.length; i6++) {
var telecomDetail = support.contact.telecom[i6];
velocityCount = i6;
t.p('						');
if (telecomDetail.type == "email" || telecomDetail.type == "im") {
t.p('							');
t.p( telecomDetail.value);
t.p(' ( ');
t.p( telecomDetail.type);
t.p(' )<br>						');
}
else {
t.p('							');
t.p( telecomDetail.value);
t.p(' ( ');
t.p( telecomDetail.use);
t.p(' )<br>						');
}
t.p('					');
}
velocityCount = i4;
t.p('				');
}
t.p('			');
}
velocityCount = i2;
t.p('		');
}
t.p('	');
}

t.p('	');
if (loopCount >= 2) {
t.p('			</div>		</div>	');
}
t.p('	</div>');
}
return t.toString();
}
function v2js_listVitalsListGrid(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.repositoryList) {
var printFirst = 0;
var patcnt = 0;
t.p('	');
for (var i2=0;  i2<context.repositoryList.length; i2++) {
var repos = context.repositoryList[i2];
velocityCount = i2;
t.p('		');
if (printFirst == 0) {
printFirst = 1;
t.p('			');
if (repos.vitals) {
t.p('				{ "titleLabels": ["Type","Description","VitalDate"],				 "modelNames": ["Type","Description","VitalDate"],				"patientData":[				');
for (var i5=0;  i5<repos.vitals.length; i5++) {
var vitalDetail = repos.vitals[i5];
velocityCount = i5;
t.p('    		   	    ');
if (patcnt > 0) {
t.p('    		   	    ,    		   	    ');
}
patcnt = ( patcnt + 1 );
t.p('    		   	    {"id":"');
t.p( patcnt);
t.p('"    		   		');
if (vitalDetail.mood_code) {
t.p('    		   		     ,"Type": "');
t.p( vitalDetail.mood_code);
t.p('"                     ');
}
t.p('    		   		');
if (vitalDetail.description) {
t.p('    		  		    ,"Description": "');
t.p( vitalDetail.description);
t.p('"     		  			    					');
if (vitalDetail.time) {
t.p('    		               	    			          , "VitalDate":"');
t.p( vitalDetail.time);
t.p('"    			                   		            ');
}
t.p('    		            }    	   			');
}
t.p('    	   		');
}
velocityCount = i2;
t.p('	   			]}	   		');
}
t.p('		');
}
t.p('	');
}

}
return t.toString();
}
function v2js_listWidgets(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
for (var i1=0;  i1<context.widgets.length; i1++) {
var widget = context.widgets[i1];
velocityCount = i1;
t.p('    <div class="imageContain">    	<img src="');
t.p( widget.image);
t.p('" alt="');
t.p( widget.name);
t.p('" custom:url="');
t.p( widget.clickUrl);
t.p('" custom:type="');
t.p( widget.type);
t.p('" custom:Id="');
t.p( widget.id);
t.p('" custom:params="');
t.p( widget.params);
t.p('"  custom:script="');
t.p( widget.script);
t.p('" custom:script_file="');
t.p( widget.script_file);
t.p('" custom:template="');
t.p( widget.template);
t.p('"  custom:jsonProcess = "');
t.p( widget.jsonProcess);
t.p('" custom:isINettuts = "');
t.p( widget.isINettuts);
t.p('" custom:cacheKey = "');
t.p( widget.cacheKey);
t.p('"></img>    	<p>');
t.p( widget.name);
t.p('</p>    </div><br/>  ');
}
velocityCount = 0;
return t.toString();
}
function v2js_tail(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('    </div></div>');
return t.toString();
}
