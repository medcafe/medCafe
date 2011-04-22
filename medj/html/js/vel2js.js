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
t.p('</a></div>');
}
velocityCount = 0;
return t.toString();
}
