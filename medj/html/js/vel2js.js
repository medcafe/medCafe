function v2js_listPatientAlerts(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<div class="repository-content">    ');
if (context.alert) {
var oparen = "(";
var cparen = ")";
t.p('        ');
for (var i2=0;  i2<context.alert.length; i2++) {
var item = context.alert[i2];
velocityCount = i2;
t.p('            ');
if (item.type) {
t.p('                <b>');
t.p( item.type.text);
t.p('</b><br/>            ');
}
t.p('            ');
if (item.status) {
t.p('                ');
t.p( item.status.text);
t.p(' <br/>            ');
}
t.p('            ');
if (item.dateTime) {
t.p('                ');
for (var i4=0;  i4<item.dateTime.length; i4++) {
var dateType = item.dateTime[i4];
velocityCount = i4;
t.p('                    ');
if (dateType.exactDateTime) {
t.p('                        ');
if (dateType.type) {
t.p('                            ');
t.p( dateType.type.text);
t.p(':                        ');
}
else {
t.p('                            Time:                        ');
}
t.p('                        ');
t.p( dateType.exactDateTime);
t.p('<br clear="all"/>                    ');
}
t.p('                ');
}
velocityCount = i2;
t.p('            ');
}
else {
t.p('                <b>No dates associated with this alert</b>            ');
}
t.p('            ');
if (item.source) {
t.p('                ');
for (var i4=0;  i4<item.source.length; i4++) {
var source = item.source[i4];
velocityCount = i4;
t.p('                    ');
if (source.actor) {
t.p('                        ');
for (var i6=0;  i6<source.actor.length; i6++) {
var actor = source.actor[i6];
velocityCount = i6;
t.p('                            ');
if (actor.actorID) {
t.p('                                Actor ID: ');
t.p( actor.actorID);
t.p('                            ');
}
t.p('                            ');
if (actor.actorRole) {
t.p('                                ');
for (var i8=0;  i8<actor.actorRole.length; i8++) {
var role = actor.actorRole[i8];
velocityCount = i8;
t.p('                                    ( ');
t.p( role.text);
t.p(' )                                ');
}
velocityCount = i6;
t.p('                            ');
}
t.p('                            <br clear="all"/>                        ');
}
velocityCount = i4;
t.p('                    ');
}
t.p('                ');
}
velocityCount = i2;
t.p('            ');
}
t.p('            <hr/>        ');
}

t.p('    ');
}
else {
t.p('        <b>No alerts provided</b>    ');
}
t.p('</div>');
return t.toString();
}
function v2js_listPatientMeds(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<div class="repository-content">    ');
if (context.medication) {
var oparen = "(";
var cparen = ")";
t.p('        ');
for (var i2=0;  i2<context.medication.length; i2++) {
var med = context.medication[i2];
velocityCount = i2;
t.p('            ');
if (med.product) {
t.p('                ');
for (var i4=0;  i4<med.product.length; i4++) {
var product = med.product[i4];
velocityCount = i4;
t.p('                    ');
if (product.productName.text) {
t.p('                        <b>');
t.p( product.productName.text);
t.p('</b><br/>                    ');
}
t.p('                    ');
for (var i5=0;  i5<product.productName.code.length; i5++) {
var code = product.productName.code[i5];
velocityCount = i5;
t.p('                        Medicine Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>                    ');
}
velocityCount = i4;
t.p('                ');
}
velocityCount = i2;
t.p('            ');
}
else {
t.p('                <b>No medication code provided</b><br/>            ');
}
t.p('            ');
if (med.dateTime) {
t.p('                ');
for (var i4=0;  i4<med.dateTime.length; i4++) {
var dateType = med.dateTime[i4];
velocityCount = i4;
t.p('                    ');
if (dateType.exactDateTime) {
t.p('                        ');
if (dateType.type) {
t.p('                            ');
t.p( dateType.type.text);
t.p(':                        ');
}
else {
t.p('                            Time:                        ');
}
t.p('                        ');
t.p( dateType.exactDateTime);
t.p('<br clear="all"/>                    ');
}
t.p('                ');
}
velocityCount = i2;
t.p('            ');
}
else {
t.p('                <b>No dates associated with this medication</b>            ');
}
t.p('            ');
if (med.source) {
t.p('                ');
for (var i4=0;  i4<med.source.length; i4++) {
var source = med.source[i4];
velocityCount = i4;
t.p('                    ');
if (source.actor) {
t.p('                        ');
for (var i6=0;  i6<source.actor.length; i6++) {
var actor = source.actor[i6];
velocityCount = i6;
t.p('                            ');
if (actor.actorID) {
t.p('                                Actor ID: ');
t.p( actor.actorID);
t.p('                            ');
}
t.p('                            ');
if (actor.actorRole) {
t.p('                                ');
for (var i8=0;  i8<actor.actorRole.length; i8++) {
var role = actor.actorRole[i8];
velocityCount = i8;
t.p('                                    ( ');
t.p( role.text);
t.p(' )                                ');
}
velocityCount = i6;
t.p('                            ');
}
t.p('                            <br clear="all"/>                        ');
}
velocityCount = i4;
t.p('                    ');
}
t.p('                ');
}
velocityCount = i2;
t.p('            ');
}
t.p('            ');
if (med.ccrDataObjectID) {
t.p('                CCR DataObjectID: ');
t.p( med.ccrDataObjectID);
t.p('<br/>            ');
}
t.p('            ');
if (med.patientInstructions) {
t.p('                Patient Instruction:<ul>                ');
for (var i4=0;  i4<med.patientInstructions.instruction.length; i4++) {
var instruction = med.patientInstructions.instruction[i4];
velocityCount = i4;
t.p('                    ');
if (instruction.text) {
t.p('                        <li><i>');
t.p( instruction.text);
t.p('<i></li>                    ');
}
t.p('                ');
}
velocityCount = i2;
t.p('                </ul>            ');
}
t.p('            <hr/>        ');
}

t.p('    ');
}
else {
t.p('        <b>No medications provided</b>    ');
}
t.p('</div>');
return t.toString();
}
function v2js_listPatients(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
for (var i1=0;  i1<context.patients.length; i1++) {
var patient = context.patients[i1];
velocityCount = i1;
t.p('    <div id="patient_');
t.p( patient.id);
t.p('"><a href="ccr/patients/');
t.p( patient.id);
t.p('">');
t.p( patient.id);
t.p('</a> <a href="');
t.p('#" onclick="showMeds(\'');
t.p( patient.id);
t.p('\')">Show medications</a> <a href="');
t.p('#" onclick="showAlerts(\'');
t.p( patient.id);
t.p('\')">Show alerts</a></div>');
}
velocityCount = 0;
return t.toString();
}
