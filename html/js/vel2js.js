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
function v2js_inettutsHead(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<div class="widget color-');
t.p( context.tab_num);
t.p('" id="yellow-widget');
t.p( context.tab_num);
t.p('">    <div style="cursor: move;" class="widget-head">        <a href="');
t.p('#" class="collapse">COLLAPSE</a><h3>');
t.p( context.title);
t.p('</h3><a href="');
t.p('#" class="remove">CLOSE</a><a href="');
t.p('#" class="edit">EDIT</a><a href="');
t.p('#" class="maximize">MAXIMIZE</a>    </div>    <div class="edit-box" style="display: none;">        <ul>            <li class="item">                <label>Change the title?</label>                <input value="');
t.p( context.title);
t.p('"/>            </li>        </ul>        <li class="item">            <label>Available colors:</label>            <ul class="colors"><li class="color-1"></li><li class="color-2"></li><li class="color-3"></li><li class="color-4"></li><li class="color-5"></li><li class="color-6"></li></ul>        </li>    </div>    <div class="widget-content no-copy" id="widget-content');
t.p( context.tab_num);
t.p('">        <p>            <div id="aaa');
t.p( context.tab_num);
t.p('" class="no-copy">            </div>        </p>        <div id="dialog');
t.p( context.tab_num);
t.p('">            <div id="modalaaa');
t.p( context.tab_num);
t.p('">');
return t.toString();
}
function v2js_inettutsTail(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('            </div>        </div>        <div id="hasContent" custom:hascontent="false">        </div>    </div></div>');
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
if (context.alerts) {
t.p('</p><br><p>	');
for (var i2=0;  i2<context.alerts.length; i2++) {
var repos = context.alerts[i2];
velocityCount = i2;
t.p('		');
if (repos.allergies) {
t.p('			');
for (var i4=0;  i4<repos.allergies.length; i4++) {
var allergyDetail = repos.allergies[i4];
velocityCount = i4;
t.p('	   		');
t.p( allergyDetail.product.value);
t.p('<br/>			   	');
}
velocityCount = i2;
t.p('	   ');
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
t.p('<div class="ui-widget top-panel" id="patient_history">');
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
for (var i1=0;  i1<context.images.length; i1++) {
var image = context.images[i1];
velocityCount = i1;
t.p('    <div class="item">        ');
t.p('        <img class="content" href="<:prefix:>');
t.p( image.source);
t.p('" src="<:prefix:>');
t.p( image.thumb);
t.p('"/>        <div class="caption">');
t.p( image.name);
t.p('</div>    </div>');
}
velocityCount = 0;
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
function v2js_listMedicineList(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.medicines) {
t.p('</p><br/><p>	');
for (var i2=0;  i2<context.medicines.length; i2++) {
var repos = context.medicines[i2];
velocityCount = i2;
t.p('		');
if (repos.medications) {
t.p('			');
for (var i4=0;  i4<repos.medications.length; i4++) {
var medicationDetail = repos.medications[i4];
velocityCount = i4;
t.p('			 *	   		');
if (medicationDetail.medicationInformation.manufacturedMaterial.freeTextBrandName) {
t.p('	  			   ');
t.p( medicationDetail.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('<br/>	  			');
}
else {
if (medicationDetail.narrative) {
t.p('	  					');
t.p( medicationDetail.narrative);
t.p('<br/>	  			');
}
}
t.p('			   	');
}
velocityCount = i2;
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
context.velocityCount=velocityCount;
t.p( v2js_inettutsHead(context));
t.p('<table cellpadding="0" cellspacing="0" border="0" class="display" id="Allergies');
t.p( context.patient_id);
t.p('"><thead><tr><th>Product</th><th>Reaction</th></tr></thead><tbody>');
for (var i1=0;  i1<context.allergies.length; i1++) {
var allergy = context.allergies[i1];
velocityCount = i1;
t.p('<tr class="gradeX"><td value="');
t.p( allergy.product.value);
t.p('">');
t.p( allergy.product.value);
t.p('</td><td value="');
t.p( allergy.reaction.value);
t.p('">');
t.p( allergy.reaction.value);
t.p('</td></tr>');
}
velocityCount = 0;
t.p('</tbody><table>');
context.velocityCount=velocityCount;
t.p( v2js_inettutsTail(context));
return t.toString();
}
function v2js_listPatientBio(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.patient) {
var printed = 0;
var space = " ";
t.p('   ');
for (var i2=0;  i2<context.patient.length; i2++) {
var repos = context.patient[i2];
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
t.p('<table cellpadding="0" cellspacing="0" border="0" class="display" id="problemListSummary"><thead></thead><tbody>');
if (context.patient_history) {
t.p('	');
for (var i2=0;  i2<context.patient_history.length; i2++) {
var history = context.patient_history[i2];
velocityCount = i2;
t.p('	   <tr>	  	');
if (history.color) {
t.p('	  		<td style="background-color:');
t.p( history.color);
t.p('">');
t.p( history.title);
t.p('	  	');
}
else {
t.p('			<td>');
t.p( history.title);
t.p('	   	');
}
t.p('	   	<div style="display: none;" class="ui-corner-all" id="detail">');
t.p( history.note);
t.p('</div></td> 	   	</tr>	');
}

}
t.p('</tbody><table>');
return t.toString();
}
function v2js_listPatientHistoryTable(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
var count = 0;
if (context.patient_history) {
t.p('	');
for (var i2=0;  i2<context.patient_history.length; i2++) {
var history = context.patient_history[i2];
velocityCount = i2;
t.p('				');
if (count == 0) {
t.p('			<table cellpadding="0" cellspacing="0" border="0" class="display" id="history');
t.p( history.patient_id);
t.p('">			<thead><tr><th>History Title</th><th>Note</th><th>Priority</th></tr></thead><tbody>		');
}
t.p('		<tr class="gradeX">		<td value="');
t.p( history.title);
t.p('">');
t.p( history.title);
t.p('</td>		<td value="');
t.p( history.note);
t.p('">');
t.p( history.note);
t.p('</td>		<td value="');
t.p( history.priority);
t.p('">');
t.p( history.priority);
t.p('</td>		</tr>		');
count = ( count + 1 );
t.p('			');
}

}
t.p('</tbody><table>');
return t.toString();
}
function v2js_listPatientImmunizations(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<table cellpadding="0" cellspacing="0" border="0" class="display" id="Immunizations');
t.p( context.patient_id);
t.p('"><thead><tr><th></th><th></th><th></th></tr></thead><tbody>');
for (var i1=0;  i1<context.immunizations.length; i1++) {
var immunization = context.immunizations[i1];
velocityCount = i1;
t.p('<tr class="gradeX"><td>');
t.p( immunization.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('</td><td>Immunization</td><td value="');
t.p( immunization.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('">');
t.p( immunization.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('</td></tr><tr class="gradeX"><td>');
t.p( immunization.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('</td><td>Administered Date</td><td value="');
t.p( immunization.administeredDate.month);
t.p('"/"');
t.p( immunization.administeredDate.day);
t.p('"/"');
t.p( immunization.administeredDate.year);
t.p('">');
t.p( immunization.administeredDate.month);
t.p('/');
t.p( immunization.administeredDate.day);
t.p('/');
t.p( immunization.administeredDate.year);
t.p('</td></tr><tr class="gradeX"><td>');
t.p( immunization.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('</td><td>Details</td><td value="');
t.p( immunization.narrative);
t.p('">');
t.p( immunization.narrative);
t.p('</td></tr><tr class="gradeX"><td>');
t.p( immunization.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('</td><td>Administered by</td>');
var space = " ";
t.p('    ');
t.p('<td value =');
if (immunization.performer.person.name.given) {
t.p('	');
for (var i3=0;  i3<immunization.performer.person.name.given.length; i3++) {
var givenNameDetail = immunization.performer.person.name.given[i3];
velocityCount = i3;
t.p('		"');
t.p( givenNameDetail);
t.p(' " 			');
}
velocityCount = i1;
}
t.p('"');
t.p( immunization.performer.person.name.lastname);
t.p('">');
if (immunization.performer.person.name.given) {
t.p('	');
for (var i3=0;  i3<immunization.performer.person.name.given.length; i3++) {
var givenNameDetail = immunization.performer.person.name.given[i3];
velocityCount = i3;
t.p('		');
t.p( givenNameDetail);
t.p( space);
t.p(' 	');
}
velocityCount = i1;
}
t.p( immunization.performer.person.name.lastname);
t.p('</td></tr>');
}
velocityCount = 0;
t.p('</tbody><table>');
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
context.velocityCount=velocityCount;
t.p( v2js_inettutsHead(context));
t.p('<table cellpadding="0" cellspacing="0" border="0" class="display" id="Medications');
t.p( context.patient_id);
t.p('"><thead><tr><th></th><th></th><th></th></tr></thead><tbody>');
for (var i1=0;  i1<context.medications.length; i1++) {
var medication = context.medications[i1];
velocityCount = i1;
t.p('<tr class="gradeX"><td>');
t.p( medication.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('</td><td>Manufacturer Info</td><td value="');
t.p( medication.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('">');
t.p( medication.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('</td></tr><tr class="gradeX"><td>');
t.p( medication.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('</td><td>Patient Information</td><td value="');
t.p( medication.patientInstructions);
t.p('">');
t.p( medication.patientInstructions);
t.p('</td></tr><tr class="gradeX"><td>');
t.p( medication.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('</td><td>Effective Time</td><td value="');
t.p( medication.effectiveTime.value);
t.p('">');
t.p( medication.effectiveTime.value);
t.p('</td></tr><tr class="gradeX"><td>');
t.p( medication.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('</td><td>Dose</td><td value="');
t.p( medication.dose.value);
t.p('">');
t.p( medication.dose.value);
t.p('</td></tr><tr class="gradeX"><td>');
t.p( medication.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('</td><td>Delivery Method</td><td value="');
t.p( medication.deliveryMethod.value);
t.p('">');
t.p( medication.deliveryMethod.value);
t.p('</td></tr><tr class="gradeX"><td>');
t.p( medication.medicationInformation.manufacturedMaterial.freeTextBrandName);
t.p('</td><td>Narrative</td><td value="');
t.p( medication.narrative);
t.p('">');
t.p( medication.narrative);
t.p('</td></tr>');
}
velocityCount = 0;
t.p('</tbody><table>');
context.velocityCount=velocityCount;
t.p( v2js_inettutsTail(context));
return t.toString();
}
function v2js_listPatientTable(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
context.velocityCount=velocityCount;
t.p( v2js_inettutsHead(context));
t.p('<table cellpadding="0" cellspacing="0" border="0" class="display" id="Detail');
t.p( context.patient_id);
t.p('"><thead><tr><th></th><th></th><th></th></tr></thead><tbody>');
var orderno = 1;
t.p('<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Patient ID</td><td value="');
t.p( context.patient_id);
t.p('">');
t.p( context.patient_id);
t.p('</td></tr>');
orderno = ( orderno + 1 );
t.p('<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Patient Name</td>');
var space = " ";
t.p('    ');
t.p('<td value =');
if (context.patient_data.name.given) {
t.p('	');
for (var i2=0;  i2<context.patient_data.name.given.length; i2++) {
var givenNameDetail = context.patient_data.name.given[i2];
velocityCount = i2;
t.p('		"');
t.p( givenNameDetail);
t.p(' "	');
}

}
t.p('"');
t.p( context.patient_data.name.lastname);
t.p('">');
if (context.patient_data.name.given) {
t.p('	');
for (var i2=0;  i2<context.patient_data.name.given.length; i2++) {
var givenNameDetail = context.patient_data.name.given[i2];
velocityCount = i2;
t.p('		');
t.p( givenNameDetail);
t.p( space);
t.p('	');
}

}
t.p( context.patient_data.name.lastname);
t.p('</td></tr>');
if (context.patient_data.address) {
var addresscount = 0;
t.p('	');
for (var i2=0;  i2<context.patient_data.address.length; i2++) {
var addressDetail = context.patient_data.address[i2];
velocityCount = i2;
orderno = ( orderno + 1 );
var linecount = 0;
t.p('		');
if (addressDetail.streetAddress) {
t.p('			');
for (var i4=0;  i4<addressDetail.streetAddress.length; i4++) {
var streetAdd = addressDetail.streetAddress[i4];
velocityCount = i4;
t.p('				');
if (linecount > 0) {
t.p(' <tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td></td><td value = "');
t.p( streetAdd);
t.p('">');
t.p( streetAdd);
t.p('</td></tr>				');
}
else {
t.p(' <tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Patient Address</td><td value = "');
t.p( streetAdd);
t.p('">');
t.p( streetAdd);
t.p('</td></tr>				');
}
linecount = ( linecount + 1 );
t.p('			');
}
velocityCount = i2;
t.p('		');
}
orderno = ( orderno + 1 );
t.p('		<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td></td><td value= "');
t.p( addressDetail.city);
t.p('", "');
t.p( addressDetail.stateOrProvince);
t.p('"  "');
t.p( addressDetail.zip);
t.p('">');
t.p( addressDetail.city);
t.p(', ');
t.p( addressDetail.stateOrProvince);
t.p('  ');
t.p( addressDetail.zip);
t.p('</td></tr>		');
addresscount = ( addresscount + 1 );
t.p('	');
}

}
if (context.patient_data.telecom) {
t.p('	');
for (var i2=0;  i2<context.patient_data.telecom.length; i2++) {
var telecomDetail = context.patient_data.telecom[i2];
velocityCount = i2;
orderno = ( orderno + 1 );
t.p('		<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td>		');
if (telecomDetail.type == "email" || telecomDetail.type == "im") {
t.p('			<td value = Patient "');
t.p( telecomDetail.type);
t.p('" address>Patient ');
t.p( telecomDetail.type);
t.p(' address</td><td value = "');
t.p( telecomDetail.value);
t.p('">');
t.p( telecomDetail.value);
t.p('</td></td>		');
}
else {
t.p('			<td value = Patient "');
t.p( telecomDetail.use);
t.p('" number>Patient ');
t.p( telecomDetail.use);
t.p(' number</td><td value = "');
t.p( telecomDetail.value);
t.p('">');
t.p( telecomDetail.value);
t.p('</td></tr>		');
}
t.p('	');
}

}
orderno = ( orderno + 1 );
if (context.patient_data.language) {
t.p('	<tr class = "gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Language Spoken</td><td value = ');
t.p( context.patient_data.language);
t.p('>');
t.p( context.patient_data.language);
t.p('</td>');
}
orderno = ( orderno + 1 );
t.p('<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Gender</td><td value="');
t.p( context.patient_data.gender.displayName);
t.p('">');
t.p( context.patient_data.gender.displayName);
t.p('</td></tr>');
if (context.patient_data.maritialStatus) {
orderno = ( orderno + 1 );
t.p('	<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Marital Status</td><td value="');
t.p( context.patient_data.maritialStatus.displayName);
t.p('">');
t.p( context.patient_data.maritialStatus.displayName);
t.p('</td></tr>');
}
if (context.patient_data.race) {
var first = true;
orderno = ( orderno + 1 );
t.p('<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Race</td><td value =	');
for (var i2=0;  i2<context.patient_data.race.length; i2++) {
var raceDetail = context.patient_data.race[i2];
velocityCount = i2;
t.p('		');
if (!( first )) {
t.p('			", "		');
}
t.p('		"');
t.p( raceDetail.displayName);
t.p('"		');
first = false;
t.p('	');
}

t.p('>	');
first = true;
t.p('	');
for (var i2=0;  i2<context.patient_data.race.length; i2++) {
var raceDetail = context.patient_data.race[i2];
velocityCount = i2;
t.p('		');
if (!( first )) {
t.p('			,');
t.p( space);
t.p('		');
}
t.p('		');
t.p( raceDetail.displayName);
t.p('		');
first = false;
t.p('	');
}

t.p('	</td></tr>');
}
orderno = ( orderno + 1 );
t.p('<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Birth Date</td><td value="');
t.p( context.patient_data.birthtime.month);
t.p('/');
t.p( context.patient_data.birthtime.day);
t.p('/');
t.p( context.patient_data.birthtime.year);
t.p(' ');
t.p( context.patient_data.birthtime.hour);
t.p(':');
t.p( context.patient_data.birthtime.minute);
t.p('">');
t.p( context.patient_data.birthtime.month);
t.p('/');
t.p( context.patient_data.birthtime.day);
t.p('/');
t.p( context.patient_data.birthtime.year);
t.p(' ');
t.p( context.patient_data.birthtime.hour);
t.p(':');
t.p( context.patient_data.birthtime.minute);
t.p('</td></tr>');
orderno = ( orderno + 1 );
if (context.patient_data.birthPlace) {
t.p('	');
if (context.patient_data.birthPlace.city && context.patient_data.birthPlace.stateOrProvince) {
t.p('		<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Birth Place</td><td value="');
t.p( context.patient_data.birthPlace.city);
t.p('", "');
t.p( context.patient_data.birthPlace.stateOrProvince);
t.p('">');
t.p( context.patient_data.birthPlace.city);
t.p(', ');
t.p( context.patient_data.birthPlace.stateOrProvince);
t.p('</td></tr>	');
}
else {
if (context.patient_data.birthPlace.stateOrProvince) {
t.p('		<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Birth Place</td><td value="');
t.p( context.patient_data.birthPlace.stateOrProvince);
t.p('">');
t.p( context.patient_data.birthPlace.stateOrProvince);
t.p('</td></tr>	');
}
else {
if (context.patient_data.birthPlace.city) {
t.p('		<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Birth Place</td><td value="');
t.p( context.patient_data.birthPlace.city);
t.p('">');
t.p( context.patient_data.birthPlace.city);
t.p('</td></tr>	');
}
}
}
}
if (context.patient_data.guardian && context.patient_data.guardian.name) {
t.p('	');
orderno = ( orderno + 1 );
t.p('	<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Guardian</td>	<td value =	');
if (context.patient_data.guardian.name.given) {
t.p('		');
for (var i3=0;  i3<context.patient_data.guardian.name.given.length; i3++) {
var givenNameDetail = context.patient_data.guardian.name.given[i3];
velocityCount = i3;
t.p('			"');
t.p( givenNameDetail);
t.p(' "		');
}

t.p('	');
}
t.p('	"');
t.p( context.patient_data.guardian.name.lastname);
t.p('">	');
if (context.patient_data.guardian.name.given) {
t.p('		');
for (var i3=0;  i3<context.patient_data.guardian.name.given.length; i3++) {
var givenNameDetail = context.patient_data.guardian.name.given[i3];
velocityCount = i3;
t.p('			');
t.p( givenNameDetail);
t.p( space);
t.p('		');
}

t.p('	');
}
t.p('	');
t.p( context.patient_data.guardian.name.lastname);
t.p('</td></tr>');
t.p('	');
if (context.patient_data.guardian.address) {
var addresscount = 0;
t.p('		');
for (var i3=0;  i3<context.patient_data.guardian.address.length; i3++) {
var addressDetail = context.patient_data.guardian.address[i3];
velocityCount = i3;
var linecount = 0;
t.p('			');
if (addressDetail.streetAddress) {
t.p('				');
for (var i5=0;  i5<addressDetail.streetAddress.length; i5++) {
var streetAdd = addressDetail.streetAddress[i5];
velocityCount = i5;
orderno = ( orderno + 1 );
t.p('					');
if (linecount > 0) {
t.p(' <tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td></td><td value = "');
t.p( streetAdd);
t.p('">');
t.p( streetAdd);
t.p('</td></tr>					');
}
else {
t.p(' <tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Guardian Address</td><td value = "');
t.p( streetAdd);
t.p('">');
t.p( streetAdd);
t.p('</td></tr>					');
}
linecount = ( linecount + 1 );
t.p('				');
}
velocityCount = i3;
t.p('			');
}
orderno = ( orderno + 1 );
t.p('			<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td></td><td value= "');
t.p( addressDetail.city);
t.p('", "');
t.p( addressDetail.stateOrProvince);
t.p('"  "');
t.p( addressDetail.zip);
t.p('">');
t.p( addressDetail.city);
t.p(', ');
t.p( addressDetail.stateOrProvince);
t.p('  ');
t.p( addressDetail.zip);
t.p('</td></tr>			');
addresscount = ( addresscount + 1 );
t.p('		');
}

t.p('	');
}
t.p('	');
if (context.patient_data.guardian.telecom) {
t.p('		');
for (var i3=0;  i3<context.patient_data.guardian.telecom.length; i3++) {
var telecomDetail = context.patient_data.guardian.telecom[i3];
velocityCount = i3;
orderno = ( orderno + 1 );
t.p('			<tr class="gradeX"><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td>			');
if (telecomDetail.type == "email" || telecomDetail.type == "im") {
t.p('				<td value = Guardian "');
t.p( telecomDetail.type);
t.p('" address>Guardian ');
t.p( telecomDetail.type);
t.p(' address</td><td value = "');
t.p( telecomDetail.value);
t.p('">');
t.p( telecomDetail.value);
t.p('</td></td>			');
}
else {
t.p('				<td value = Guardian "');
t.p( telecomDetail.use);
t.p('" number>Guardian ');
t.p( telecomDetail.use);
t.p(' number</td><td value = "');
t.p( telecomDetail.value);
t.p('">');
t.p( telecomDetail.value);
t.p('</td></tr>			');
}
t.p('		');
}

t.p('	');
}
}
t.p('</tbody><table>');
context.velocityCount=velocityCount;
context.orderno=orderno;
context.space=space;
t.p( v2js_inettutsTail(context));
return t.toString();
}
function v2js_listPatientVitals(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
if (context.vitalRecords) {
t.p('	');
for (var i2=0;  i2<context.vitalRecords.length; i2++) {
var repos = context.vitalRecords[i2];
velocityCount = i2;
t.p('		');
if (repos.vitals) {
var print = 0;
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
t.p(':');
t.p( vitalDetail.resultDateTime.low.minute);
t.p('</td></tr>					');
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
t.p('<form id="BookmarksForm');
t.p( context.patient);
t.p('" name="BookmarksForm');
t.p( context.patient);
t.p('"><input type="submit" value="save"></input><table cellpadding="0" cellspacing="0" border="0" class="display" id="Bookmarks');
t.p( context.patient);
t.p('"><thead><tr><th>Bookmark</th><th>URL</th><th>Description</th><th></th></tr></thead><tbody>');
if (!( context.bookmarks )) {
t.p('	<tr class="gradeX"><td name="name" value="" class="editInput"></td>    <td name="url" class="editInput" value=""></td>    <td name="description" class="editInput" value=""></td>    <td></td>    </tr>');
}
else {
for (var i1=0;  i1<context.bookmarks.length; i1++) {
var bookmark = context.bookmarks[i1];
velocityCount = i1;
t.p('    <tr class="gradeX"><td name="name" class="editInput" value="');
t.p( bookmark.name);
t.p('">');
t.p( bookmark.name);
t.p('</td>    <td name="url" value="');
t.p( bookmark.url);
t.p('" class="editInput">');
t.p( bookmark.url);
t.p('</td>    <td name="description" class="editInput" value="');
t.p( bookmark.description);
t.p('">');
t.p( bookmark.description);
t.p('</td>    <td><div>    <input class="addLink" id="AddLink');
t.p( count);
t.p('" value="Add Link" type="button"></input>    <div id="bookmarkName');
t.p( count);
t.p('" style="display:none;">');
t.p( bookmark.name);
t.p('</div>    <div id="bookmarkUrl');
t.p( count);
t.p('" style="display:none;">');
t.p( bookmark.url);
t.p('</div>    </div></td>    </tr>    ');
count = ( count + 1 );
}
velocityCount = 0;
}
t.p('</tbody></table></form>');
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
t.p('<table cellpadding="0" cellspacing="0" border="0" class="display" id="problemListSummary"><thead></thead><tbody>');
if (context.problems) {
t.p('	');
for (var i2=0;  i2<context.problems.length; i2++) {
var repos = context.problems[i2];
velocityCount = i2;
t.p('		');
if (repos.problem) {
t.p('			');
for (var i4=0;  i4<repos.problem.length; i4++) {
var problemDetail = repos.problem[i4];
velocityCount = i4;
t.p('	   <tr>	  	<td> ');
t.p( problemDetail.problemName);
t.p(' </td>		<td>		<div class="ui-corner-all" id="detail">			');
t.p( problemDetail.problemDate.low.month);
t.p('/');
t.p( problemDetail.problemDate.low.day);
t.p('/');
t.p( problemDetail.problemDate.low.year);
t.p('		</div></td> 	   	</tr>	   	');
}
velocityCount = i2;
t.p('	   ');
}
t.p('	');
}

}
t.p('</tbody></table>');
return t.toString();
}
function v2js_listProblemListTable(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
var count = 0;
t.p('	');
for (var i1=0;  i1<context.problem.length; i1++) {
var problemDetail = context.problem[i1];
velocityCount = i1;
var ActiveStatus = "Active";
var InActiveStatus = "Inactive";
t.p('		');
if (count == 0) {
t.p('			<table cellpadding="0" cellspacing="0" border="0" class="display" id="Problem');
t.p( context.patient_id);
t.p('">			<thead><tr><th>Problem Name</th><th>Code</th><th>Date</th><th>Status</th></tr></thead><tbody>		');
}
t.p('			<tr class="gradeX"  >			<td  value="');
t.p( problemDetail.problemName);
t.p('">');
t.p( problemDetail.problemName);
t.p('</td>		<td value="');
t.p( problemDetail.problemCode.code);
t.p('">');
t.p( problemDetail.problemCode.code);
t.p('</td>		<td value="');
t.p( problemDetail.problemDate);
t.p('">');
t.p( problemDetail.problemDate.low.month);
t.p('/		');
t.p( problemDetail.problemDate.low.day);
t.p('/');
t.p( problemDetail.problemDate.low.year);
t.p('</td>		');
if (problemDetail.narrative == "A") {
t.p('			<td  style = "background-color:yellow" value="');
t.p( problemDetail.narrative);
t.p('">');
t.p( ActiveStatus);
t.p('</td>		');
}
else {
t.p('			<td style = "background-color:gray"value="');
t.p( problemDetail.narrative);
t.p('">');
t.p( InActiveStatus);
t.p('</td>			');
}
t.p('		</tr>		');
count = ( count + 1 );
t.p('	');
}
velocityCount = 0;
t.p('</tbody><table>');
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
t.p('</search>');
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
t.p('<table cellpadding="0" cellspacing="0" border="0" class="display" id="Support');
t.p( context.patient_id);
t.p('"><thead><tr><th></th><th></th><th></th><th></th><th></th></tr></thead><tbody>');
var contactNumber = 0;
for (var i1=0;  i1<context.supportInfo.length; i1++) {
var support = context.supportInfo[i1];
velocityCount = i1;
t.p('	');
var conType = 'undefined';
t.p('	');
if (support.contactType.valueOf() == 'AGNT') {
conType = 'Agent';
t.p('	');
}
else {
if (support.contactType.valueOf() == 'ECON') {
conType = 'Emergency Contact';
t.p('	');
}
else {
if (support.contactType.valueOf() == 'CAREGIVER') {
conType = 'Caregiver';
t.p('	');
}
else {
if (support.contactType.valueOf() == 'NOK') {
conType = 'Next of Kin';
t.p('	');
}
else {
if (support.contactType.valueOf() == 'PRS') {
conType = support.contactRelationship.displayName;
t.p('	');
}
}
}
}
}
var orderno = 1;
t.p('	<tr class="gradeX"><td value = "');
t.p( contactNumber);
t.p('">');
t.p( contactNumber);
t.p('</td><td value = "');
t.p( conType);
t.p('">');
t.p( conType);
t.p('</td><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Name</td>	<td value =	');
if (support.contact.name.given) {
t.p('		');
for (var i3=0;  i3<support.contact.name.given.length; i3++) {
var givenNameDetail = support.contact.name.given[i3];
velocityCount = i3;
t.p('			"');
t.p( givenNameDetail);
t.p(' " 				');
}
velocityCount = i1;
t.p('	');
}
t.p('	"');
t.p( support.contact.name.lastname);
t.p('">	');
if (support.contact.name.given) {
t.p('		');
for (var i3=0;  i3<support.contact.name.given.length; i3++) {
var givenNameDetail = support.contact.name.given[i3];
velocityCount = i3;
t.p('			');
t.p( givenNameDetail);
t.p( context.space);
t.p(' 		');
}
velocityCount = i1;
t.p('	');
}
t.p('	');
t.p( support.contact.name.lastname);
t.p('</td></tr>	');
if (support.contact.address) {
var addresscount = 0;
t.p('		');
for (var i3=0;  i3<support.contact.address.length; i3++) {
var addressDetail = support.contact.address[i3];
velocityCount = i3;
var linecount = 0;
t.p('			');
if (addressDetail.streetAddress) {
t.p('				');
for (var i5=0;  i5<addressDetail.streetAddress.length; i5++) {
var streetAdd = addressDetail.streetAddress[i5];
velocityCount = i5;
orderno = ( orderno + 1 );
t.p('					');
if (linecount > 0) {
t.p(' <tr class="gradeX"><td value = "contactNumber">');
t.p( contactNumber);
t.p('</td><td value = "');
t.p( conType);
t.p('">');
t.p( conType);
t.p('</td><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td></td><td value = "');
t.p( streetAdd);
t.p('">');
t.p( streetAdd);
t.p('</td></tr>					');
}
else {
t.p(' <tr class="gradeX"><td value = "contactNumber">');
t.p( contactNumber);
t.p('</td><td value = "');
t.p( conType);
t.p('">');
t.p( conType);
t.p('</td><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Address</td><td value = "');
t.p( streetAdd);
t.p('">');
t.p( streetAdd);
t.p('</td></tr>					');
}
linecount = ( linecount + 1 );
t.p('								');
}
velocityCount = i3;
t.p('							');
}
orderno = ( orderno + 1 );
t.p('			<tr class="gradeX"><td value = "contactNumber">');
t.p( contactNumber);
t.p('</td><td value = "');
t.p( conType);
t.p('">');
t.p( conType);
t.p('</td><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td></td><td value= "');
t.p( addressDetail.city);
t.p('", "');
t.p( addressDetail.stateOrProvince);
t.p('"  "');
t.p( addressDetail.zip);
t.p('">');
t.p( addressDetail.city);
t.p(', ');
t.p( addressDetail.stateOrProvince);
t.p('  ');
t.p( addressDetail.zip);
t.p('</td></tr>			');
addresscount = ( addresscount + 1 );
t.p('		');
}
velocityCount = i1;
t.p('	');
}
t.p('	');
if (support.contact.telecom) {
t.p('		');
for (var i3=0;  i3<support.contact.telecom.length; i3++) {
var telecomDetail = support.contact.telecom[i3];
velocityCount = i3;
orderno = ( orderno + 1 );
t.p('			<tr class="gradeX"><td value = "contactNumber">');
t.p( contactNumber);
t.p('</td><td value = "');
t.p( conType);
t.p('">');
t.p( conType);
t.p('</td><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td>			');
if (telecomDetail.type == "email" || telecomDetail.type == "im") {
t.p('				<td value = "');
t.p( conType);
t.p('"\'s "');
t.p( telecomDetail.type);
t.p('" address>');
t.p( conType);
t.p('\'s ');
t.p( telecomDetail.type);
t.p(' address</td><td value = "');
t.p( telecomDetail.value);
t.p('">');
t.p( telecomDetail.value);
t.p('</td></tr>			');
}
else {
t.p('				<td value = "');
t.p( conType);
t.p('"\'s "');
t.p( telecomDetail.use);
t.p('" number>');
t.p( conType);
t.p('\'s ');
t.p( telecomDetail.use);
t.p(' number</td><td value = "');
t.p( telecomDetail.value);
t.p('">');
t.p( telecomDetail.value);
t.p('</td></tr>			');
}
t.p('		');
}
velocityCount = i1;
t.p('	');
}
orderno = ( orderno + 1 );
t.p('	');
if (support.contactRelationship.displayName) {
t.p('		<tr class="gradeX"><td value = "contactNumber">');
t.p( contactNumber);
t.p('</td><td value = "');
t.p( conType);
t.p('">');
t.p( conType);
t.p('</td><td value = "');
t.p( orderno);
t.p('">');
t.p( orderno);
t.p('</td><td>Relationship</td><td value= "');
t.p( support.contactRelationship.displayName);
t.p('">');
t.p( support.contactRelationship.displayName);
t.p('</td></tr>	');
}
t.p('	');
contactNumber = ( contactNumber + 1 );
}
velocityCount = 0;
t.p('</tbody></table>');
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
t.p('" custom:repository="');
t.p( widget.repository);
t.p('" custom:script="');
t.p( widget.script);
t.p('" custom:script_file="');
t.p( widget.scriptFile);
t.p('" custom:template="');
t.p( widget.template);
t.p('"></img>    	<p>');
t.p( widget.name);
t.p('</p>    </div><br/>  ');
}
velocityCount = 0;
return t.toString();
}
function v2js_toInettuts(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<div class="widget color-');
t.p( context.tabNum);
t.p('" id="yellow-widget');
t.p( context.tabNum);
t.p('">    <div style="cursor: move;" class="widget-head">        <a href="');
t.p('#" class="collapse">COLLAPSE</a><h3>');
t.p( context.title);
t.p('</h3><a href="');
t.p('#" class="remove">CLOSE</a><a href="');
t.p('#" class="edit">EDIT</a><a href="');
t.p('#" class="maximize">MAXIMIZE</a>    </div>    <div class="edit-box" style="display: none;">        <ul>            <li class="item">                <label>Change the title?</label>                <input value="');
t.p( context.title);
t.p('"/>            </li>        </ul>        <li class="item">            <label>Available colors:</label>            <ul class="colors"><li class="color-1"></li><li class="color-2"></li><li class="color-3"></li><li class="color-4"></li><li class="color-5"></li><li class="color-6"></li></ul>        </li>    </div>    <div class="widget-content no-copy" id="widget-content');
t.p( context.tabNum);
t.p('">        <p>            <div id="aaa');
t.p( context.tabNum);
t.p('" class="no-copy">            </div>        </p>        <div id="dialog');
t.p( context.tabNum);
t.p('">            <div id="modalaaa');
t.p( context.tabNum);
t.p('">            </div>        </div>        <div id="hasContent" custom:hascontent="false">        </div>    </div></div>');
return t.toString();
}
