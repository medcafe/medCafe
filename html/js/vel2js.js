function v2js_listDates(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
for (var i1=0;  i1<context.years.length; i1++) {
var year = context.years[i1];
velocityCount = i1;
t.p('    <optgroup label="');
t.p( year.year);
t.p('">    ');
for (var i2=0;  i2<year.months.length; i2++) {
var month = year.months[i2];
velocityCount = i2;
t.p('        <option value="');
t.p( month);
t.p('/');
t.p( year.year);
t.p('">');
t.p( month);
t.p('/');
t.p( year.year);
t.p('</option>    ');
}
velocityCount = i1;
t.p('    </optgroup>');
}
velocityCount = 0;
return t.toString();
}
function v2js_listPatientTable(context) { 
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
function v2js_listPatientsBookmarksTable(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<table cellpadding="0" cellspacing="0" border="0" class="display" id="bookmarks1"><thead><tr><th></th></tr></thead><tbody>');
for (var i1=0;  i1<context.bookmarks.length; i1++) {
var bookmark = context.bookmarks[i1];
velocityCount = i1;
t.p('    <tr class="gradeX"><td name="name" value="');
t.p( bookmark.name);
t.p('">');
t.p( bookmark.name);
t.p('</td>    <td name="url" value="');
t.p( bookmark.url);
t.p('">');
t.p( bookmark.url);
t.p('</td>    <td name="description" value="');
t.p( bookmark.description);
t.p('">');
t.p( bookmark.description);
t.p('</td></tr>');
}
velocityCount = 0;
t.p('</tbody></table>');
return t.toString();
}
function v2js_listPatientsTable(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<table cellpadding="0" cellspacing="0" border="0" class="display" id="exampleOurVista"><thead><tr><th></th></tr></thead><tbody>');
for (var i1=0;  i1<context.patients.length; i1++) {
var patient = context.patients[i1];
velocityCount = i1;
t.p('    <tr class="gradeX"><td value="');
t.p( patient);
t.p('"><span class="summary"><a href="');
t.p('#" class="details">');
t.p( patient);
t.p('</a></span></td></tr>');
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
t.p( widget.clickURL);
t.p('" custom:type="');
t.p( widget.type);
t.p('"></img>    	<p>');
t.p( widget.name);
t.p('</p>    </div><br/>  ');
}
velocityCount = 0;
return t.toString();
}
