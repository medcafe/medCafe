function v2js_listPatients(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
for (var i1=0;  i1<context.patients.length; i1++) {
var patient = context.patients[i1];
velocityCount = i1;
t.p('    <div value="');
t.p( patient);
t.p('">');
t.p( patient);
t.p('</div>');
}
velocityCount = 0;
return t.toString();
}
