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
function v2js_listPatientResults(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
t.p('<div class="repository-content">');
if (context.result) {
var oparen = "(";
var cparen = ")";
var space = " ";
t.p('	');
for (var i2=0;  i2<context.result.length; i2++) {
var res = context.result[i2];
velocityCount = i2;
t.p('		<b>Result Record</b><br/>			');
if (res.dateTime) {
t.p('			');
for (var i4=0;  i4<res.dateTime.length; i4++) {
var dateType = res.dateTime[i4];
velocityCount = i4;
t.p('				');
if (dateType.type) {
t.p('					');
t.p( dateType.type.text);
t.p(':				');
}
else {
t.p('					Time:				');
}
t.p('				');
if (dateType.exactDateTime) {
t.p('					');
t.p( dateType.exactDateTime);
t.p('				');
}
else {
t.p('					');
if (dateType.approximateDateTime) {
var codedDesc = dateType.approximateDateTime;
t.p('						');
if (codedDesc.text) {
t.p('							');
t.p( codedDesc.text);
t.p('						');
}
t.p('						');
if (codedDesc.objectAttribute) {
t.p('							');
for (var i7=0;  i7<codedDesc.objectAttribute.length; i7++) {
var objAtt = codedDesc.objectAttribute[i7];
velocityCount = i7;
t.p('								');
t.p( objAtt.attribute);
t.p(':								');
for (var i8=0;  i8<objAtt.attributeValue.length; i8++) {
var attVal = objAtt.attributeValue[i8];
velocityCount = i8;
t.p('									');
t.p( space);
t.p( attVal.value);
t.p( space);
t.p('									');
if (attVal.code) {
t.p('										');
for (var i10=0;  i10<attVal.code.length; i10++) {
var code = attVal.code[i10];
velocityCount = i10;
t.p('											<br/> 											Attribute Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>										');
}
velocityCount = i8;
t.p('									');
}
t.p('								');
}
velocityCount = i7;
t.p('									');
}
velocityCount = i4;
t.p('							');
if (codedDesc.code) {
t.p('								');
for (var i8=0;  i8<context.objAtt.code.length; i8++) {
var code = context.objAtt.code[i8];
velocityCount = i8;
t.p('									Coded Description Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>								');
}
velocityCount = i4;
t.p('							');
}
t.p('						');
}
t.p('					');
}
else {
t.p('						');
if (dateType.age) {
var measurement = dateType.age;
t.p('							');
t.p( measurement.value);
t.p('							');
if (measurement.units) {
t.p('								');
t.p( space);
t.p('								');
if (measurement.units.unit) {
t.p('									');
t.p( measurement.units.unit);
t.p('								');
}
t.p('								');
if (measurement.units.code) {
t.p('									');
t.p( space);
t.p('									');
for (var i8=0;  i8<measurement.units.code.length; i8++) {
var code = measurement.units.code[i8];
velocityCount = i8;
t.p('										Unit Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>									');
}
velocityCount = i4;
t.p('								');
}
t.p('							');
}
t.p('						');
}
else {
t.p('							');
if (dateType.dateTimeRange) {
t.p('								');
if (dateType.dateTimeRange.beginRange) {
t.p('									');
t.p( space);
t.p('Range Begin:');
t.p( space);
t.p('									');
if (dateType.dateTimeRange.beginRange.exactDateTime) {
t.p('										');
t.p( dateType.dateTimeRange.beginRange.exactDateTime);
t.p('<br clear="all"/>									');
}
else {
t.p('										');
if (dateType.dateTimeRange.beginRange.approximateDateTime) {
var codedDesc = dateType.dateTimeRange.beginRange.approximateDateTime;
t.p('											');
if (codedDesc.text) {
t.p('												');
t.p( codedDesc.text);
t.p('											');
}
t.p('											');
if (codedDesc.objectAttribute) {
t.p('												');
for (var i9=0;  i9<codedDesc.objectAttribute.length; i9++) {
var objAtt = codedDesc.objectAttribute[i9];
velocityCount = i9;
t.p('													');
t.p( objAtt.attribute);
t.p(':													');
for (var i10=0;  i10<objAtt.attributeValue.length; i10++) {
var attVal = objAtt.attributeValue[i10];
velocityCount = i10;
t.p('														');
t.p( space);
t.p( attVal.value);
t.p( space);
t.p('														');
if (attVal.code) {
t.p('															');
for (var i12=0;  i12<attVal.code.length; i12++) {
var code = attVal.code[i12];
velocityCount = i12;
t.p('																<br/> 																Attribute Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>															');
}
velocityCount = i10;
t.p('														');
}
t.p('													');
}
velocityCount = i9;
t.p('																		');
}
velocityCount = i4;
t.p('												');
if (codedDesc.code) {
t.p('													');
for (var i10=0;  i10<context.objAtt.code.length; i10++) {
var code = context.objAtt.code[i10];
velocityCount = i10;
t.p('														Coded Description Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>													');
}
velocityCount = i4;
t.p('												');
}
t.p('											');
}
t.p('										');
}
else {
t.p('											');
if (dateType.dateTimeRange.beginRange.age) {
var measurement = dateType.dateTimeRange.beginRange.age;
t.p('												');
t.p( measurement.value);
t.p('												');
if (measurement.units) {
t.p('													');
t.p( space);
t.p('													');
if (measurement.units.unit) {
t.p('														');
t.p( measurement.units.unit);
t.p('													');
}
t.p('													');
if (measurement.units.code) {
t.p('														');
t.p( space);
t.p('														');
for (var i10=0;  i10<measurement.units.code.length; i10++) {
var code = measurement.units.code[i10];
velocityCount = i10;
t.p('															Unit Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>														');
}
velocityCount = i4;
t.p('													');
}
t.p('												');
}
t.p('											');
}
t.p('										');
}
t.p('									');
}
t.p('								');
}
t.p('								');
if (dateType.dateTimeRange.endRange) {
t.p('									');
t.p( space);
t.p('Range End:');
t.p( space);
t.p('																		');
if (dateType.dateTimeRange.endRange.exactDateTime) {
t.p('										');
t.p( dateType.dateTimeRange.endRange.exactDateTime);
t.p('<br clear="all"/>									');
}
else {
t.p('										');
if (dateType.dateTimeRange.endRange.approximateDateTime) {
var codedDesc = dateType.dateTimeRange.endRange.approximateDateTime;
t.p('											');
if (codedDesc.text) {
t.p('												');
t.p( codedDesc.text);
t.p('											');
}
t.p('											');
if (codedDesc.objectAttribute) {
t.p('												');
for (var i9=0;  i9<codedDesc.objectAttribute.length; i9++) {
var objAtt = codedDesc.objectAttribute[i9];
velocityCount = i9;
t.p('													');
t.p( objAtt.attribute);
t.p(':													');
for (var i10=0;  i10<objAtt.attributeValue.length; i10++) {
var attVal = objAtt.attributeValue[i10];
velocityCount = i10;
t.p('														');
t.p( space);
t.p( attVal.value);
t.p( space);
t.p('														');
if (attVal.code) {
t.p('															');
for (var i12=0;  i12<attVal.code.length; i12++) {
var code = attVal.code[i12];
velocityCount = i12;
t.p('																<br/> 																Attribute Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>															');
}
velocityCount = i10;
t.p('														');
}
t.p('													');
}
velocityCount = i9;
t.p('																		');
}
velocityCount = i4;
t.p('												');
if (codedDesc.code) {
t.p('													');
for (var i10=0;  i10<context.objAtt.code.length; i10++) {
var code = context.objAtt.code[i10];
velocityCount = i10;
t.p('														Coded Description Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>													');
}
velocityCount = i4;
t.p('												');
}
t.p('											');
}
t.p('										');
}
else {
t.p('											');
if (dateType.dateTimeRange.endRange.age) {
var measurement = dateType.dateTimeRange.endRange.age;
t.p('												');
t.p( measurement.value);
t.p('												');
if (measurement.units) {
t.p('													');
t.p( space);
t.p('													');
if (measurement.units.unit) {
t.p('														');
t.p( measurement.units.unit);
t.p('													');
}
t.p('													');
if (measurement.units.code) {
t.p('														');
t.p( space);
t.p('														');
for (var i10=0;  i10<measurement.units.code.length; i10++) {
var code = measurement.units.code[i10];
velocityCount = i10;
t.p('															Unit Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>														');
}
velocityCount = i4;
t.p('													');
}
t.p('												');
}
t.p('											');
}
t.p('										');
}
t.p('									');
}
t.p('								');
}
t.p('																							');
}
t.p('						');
}
t.p('					');
}
t.p('				');
}
t.p('			');
}
velocityCount = i2;
t.p('				');
}
else {
t.p('			<b>No dates associated with this test</b>		');
}
t.p('		<br clear="all"/>		');
if (res.source) {
t.p('			');
for (var i4=0;  i4<res.source.length; i4++) {
var source = res.source[i4];
velocityCount = i4;
t.p('				');
if (source.actor) {
t.p('					');
for (var i6=0;  i6<source.actor.length; i6++) {
var actor = source.actor[i6];
velocityCount = i6;
t.p('						');
if (actor.actorID) {
t.p('							Actor ID: ');
t.p( actor.actorID);
t.p('						');
}
t.p('						');
if (actor.actorRole) {
t.p('							');
for (var i8=0;  i8<actor.actorRole.length; i8++) {
var role = actor.actorRole[i8];
velocityCount = i8;
t.p('								( ');
t.p( role.text);
t.p(' )							');
}
velocityCount = i6;
t.p('						');
}
t.p('						<br clear="all"/>					');
}
velocityCount = i4;
t.p('				');
}
t.p('			');
}
velocityCount = i2;
t.p('		');
}
t.p('		');
if (res.ccrDataObjectID) {
t.p('			CCR DataObjectID: ');
t.p( res.ccrDataObjectID);
t.p('<br/>		');
}
t.p('		');
if (res.test) {
t.p('		Tests:			<div style="padding-left: 2em">			');
for (var i4=0;  i4<res.test.length; i4++) {
var testInst = res.test[i4];
velocityCount = i4;
t.p('				');
if (testInst.description.text) {
t.p('					<b>');
t.p( testInst.description.text);
t.p('</b><br/>				');
}
t.p('				');
for (var i5=0;  i5<testInst.description.code.length; i5++) {
var code = testInst.description.code[i5];
velocityCount = i5;
t.p('					Test Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>				');
}
velocityCount = i4;
t.p('				Results:  ');
t.p( testInst.testResult.value);
t.p('				');
if (testInst.testResult.units) {
t.p('					');
t.p( space);
t.p('					');
if (testInst.testResult.units.unit) {
t.p('						');
t.p( testInst.testResult.units.unit);
t.p('					');
}
t.p('					');
if (testInst.testResult.units.code) {
t.p('						');
for (var i7=0;  i7<testInst.testResult.units.code.length; i7++) {
var code = testInst.testResult.units.code[i7];
velocityCount = i7;
t.p('							Unit Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )						');
}
velocityCount = i4;
t.p('					');
}
t.p('				');
}
t.p('				<br/>				');
for (var i5=0;  i5<testInst.source.length; i5++) {
var source = testInst.source[i5];
velocityCount = i5;
t.p('					');
if (source.actor) {
t.p('						');
for (var i7=0;  i7<source.actor.length; i7++) {
var actor = source.actor[i7];
velocityCount = i7;
t.p('							');
if (actor.actorID) {
t.p('								Actor ID: ');
t.p( actor.actorID);
t.p('							');
}
t.p('							');
if (actor.actorRole) {
t.p('								');
for (var i9=0;  i9<actor.actorRole.length; i9++) {
var role = actor.actorRole[i9];
velocityCount = i9;
t.p('									( ');
t.p( role.text);
t.p(' )								');
}
velocityCount = i7;
t.p('							');
}
t.p('							<br clear="all"/>						');
}
velocityCount = i5;
t.p('					');
}
t.p('				');
}
velocityCount = i4;
t.p('				');
if (testInst.dateTime) {
t.p('					');
for (var i6=0;  i6<testInst.dateTime.length; i6++) {
var dateType = testInst.dateTime[i6];
velocityCount = i6;
t.p('						');
if (dateType.type) {
t.p('							');
t.p( dateType.type.text);
t.p(':						');
}
else {
t.p('							Time:						');
}
t.p('						');
if (dateType.exactDateTime) {
t.p('							');
t.p( dateType.exactDateTime);
t.p('						');
}
else {
if (dateType.approximateDateTime) {
var codedDesc = dateType.approximateDateTime;
t.p('							');
if (codedDesc.text) {
t.p('								');
t.p( codedDesc.text);
t.p('							');
}
t.p('							');
if (codedDesc.objectAttribute) {
t.p('								');
for (var i9=0;  i9<codedDesc.objectAttribute.length; i9++) {
var objAtt = codedDesc.objectAttribute[i9];
velocityCount = i9;
t.p('									');
t.p( objAtt.attribute);
t.p(':									');
for (var i10=0;  i10<objAtt.attributeValue.length; i10++) {
var attVal = objAtt.attributeValue[i10];
velocityCount = i10;
t.p('										');
t.p( space);
t.p( attVal.value);
t.p( space);
t.p('										');
if (attVal.code) {
t.p('											');
for (var i12=0;  i12<attVal.code.length; i12++) {
var code = attVal.code[i12];
velocityCount = i12;
t.p('												<br/> 												Attribute Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>											');
}
velocityCount = i10;
t.p('										');
}
t.p('									');
}
velocityCount = i9;
t.p('								');
}
velocityCount = i6;
t.p('								');
if (codedDesc.code) {
t.p('									');
for (var i10=0;  i10<context.objAtt.code.length; i10++) {
var code = context.objAtt.code[i10];
velocityCount = i10;
t.p('										Coded Description Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>									');
}
velocityCount = i6;
t.p('								');
}
t.p('							');
}
t.p('						');
}
else {
if (dateType.age) {
var measurement = dateType.age;
t.p('							');
t.p( measurement.value);
t.p('							');
if (measurement.units) {
t.p('								');
t.p( space);
t.p('								');
if (measurement.units.unit) {
t.p('									');
t.p( measurement.units.unit);
t.p('								');
}
t.p('								');
if (measurement.units.code) {
t.p('									');
t.p( space);
t.p('									');
for (var i10=0;  i10<measurement.units.code.length; i10++) {
var code = measurement.units.code[i10];
velocityCount = i10;
t.p('										Unit Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>									');
}
velocityCount = i6;
t.p('								');
}
t.p('							');
}
t.p('						');
}
else {
if (dateType.dateTimeRange) {
t.p('							');
if (dateType.dateTimeRange.beginRange) {
t.p('								');
t.p( space);
t.p('Range Begin:');
t.p( space);
t.p('								');
if (dateType.dateTimeRange.beginRange.exactDateTime) {
t.p('									');
t.p( dateType.dateTimeRange.beginRange.exactDateTime);
t.p('<br clear="all"/>								');
}
else {
if (dateType.dateTimeRange.beginRange.approximateDateTime) {
var codedDesc = dateType.dateTimeRange.beginRange.approximateDateTime;
t.p('									');
if (codedDesc.text) {
t.p('										');
t.p( codedDesc.text);
t.p('									');
}
t.p('									');
if (codedDesc.objectAttribute) {
t.p('										');
for (var i11=0;  i11<codedDesc.objectAttribute.length; i11++) {
var objAtt = codedDesc.objectAttribute[i11];
velocityCount = i11;
t.p('											');
t.p( objAtt.attribute);
t.p(':											');
for (var i12=0;  i12<objAtt.attributeValue.length; i12++) {
var attVal = objAtt.attributeValue[i12];
velocityCount = i12;
t.p('												');
t.p( space);
t.p( attVal.value);
t.p( space);
t.p('												');
if (attVal.code) {
t.p('													');
for (var i14=0;  i14<attVal.code.length; i14++) {
var code = attVal.code[i14];
velocityCount = i14;
t.p('														<br/> 														Attribute Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>													');
}
velocityCount = i12;
t.p('												');
}
t.p('											');
}
velocityCount = i11;
t.p('										');
}
velocityCount = i6;
t.p('										');
if (codedDesc.code) {
t.p('											');
for (var i12=0;  i12<context.objAtt.code.length; i12++) {
var code = context.objAtt.code[i12];
velocityCount = i12;
t.p('												Coded Description Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>											');
}
velocityCount = i6;
t.p('										');
}
t.p('									');
}
t.p('								');
}
else {
if (dateType.dateTimeRange.beginRange.age) {
var measurement = dateType.dateTimeRange.beginRange.age;
t.p('									');
t.p( measurement.value);
t.p('									');
if (measurement.units) {
t.p('										');
t.p( space);
t.p('										');
if (measurement.units.unit) {
t.p('											');
t.p( measurement.units.unit);
t.p('										');
}
t.p('										');
if (measurement.units.code) {
t.p('											');
t.p( space);
t.p('											');
for (var i12=0;  i12<measurement.units.code.length; i12++) {
var code = measurement.units.code[i12];
velocityCount = i12;
t.p('												Unit Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>											');
}
velocityCount = i6;
t.p('										');
}
t.p('									');
}
t.p('								');
}
}
}
t.p('							');
}
t.p('							');
if (dateType.dateTimeRange.endRange) {
t.p('								');
t.p( space);
t.p('Range End:');
t.p( space);
t.p('								');
if (dateType.dateTimeRange.endRange.exactDateTime) {
t.p('									');
t.p( dateType.dateTimeRange.endRange.exactDateTime);
t.p('<br clear="all"/>								');
}
else {
if (dateType.dateTimeRange.endRange.approximateDateTime) {
var codedDesc = dateType.dateTimeRange.endRange.approximateDateTime;
t.p('									');
if (codedDesc.text) {
t.p('										');
t.p( codedDesc.text);
t.p('									');
}
t.p('									');
if (codedDesc.objectAttribute) {
t.p('										');
for (var i11=0;  i11<codedDesc.objectAttribute.length; i11++) {
var objAtt = codedDesc.objectAttribute[i11];
velocityCount = i11;
t.p('											');
t.p( objAtt.attribute);
t.p(':											');
for (var i12=0;  i12<objAtt.attributeValue.length; i12++) {
var attVal = objAtt.attributeValue[i12];
velocityCount = i12;
t.p('												');
t.p( space);
t.p( attVal.value);
t.p( space);
t.p('												');
if (attVal.code) {
t.p('													');
for (var i14=0;  i14<attVal.code.length; i14++) {
var code = attVal.code[i14];
velocityCount = i14;
t.p('														<br/> 														Attribute Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>													');
}
velocityCount = i12;
t.p('												');
}
t.p('											');
}
velocityCount = i11;
t.p('										');
}
velocityCount = i6;
t.p('										');
if (codedDesc.code) {
t.p('											');
for (var i12=0;  i12<context.objAtt.code.length; i12++) {
var code = context.objAtt.code[i12];
velocityCount = i12;
t.p('												Coded Description Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>											');
}
velocityCount = i6;
t.p('										');
}
t.p('									');
}
t.p('								');
}
else {
if (dateType.dateTimeRange.endRange.age) {
var measurement = dateType.dateTimeRange.endRange.age;
t.p('									');
t.p( measurement.value);
t.p('									');
if (measurement.units) {
t.p('										');
t.p( space);
t.p('										');
if (measurement.units.unit) {
t.p('											');
t.p( measurement.units.unit);
t.p('										');
}
t.p('										');
if (measurement.units.code) {
t.p('											');
t.p( space);
t.p('											');
for (var i12=0;  i12<measurement.units.code.length; i12++) {
var code = measurement.units.code[i12];
velocityCount = i12;
t.p('												Unit Code: ');
t.p( code.value);
t.p(' ( ');
t.p( code.codingSystem);
t.p(' of ');
t.p( code.version);
t.p(' )<br/>												');
}
velocityCount = i6;
t.p('										');
}
t.p('									');
}
t.p('								');
}
}
}
t.p('							');
}
t.p('						');
}
}
}
}
t.p('					');
}
velocityCount = i4;
t.p('					<br clear="all"/>				');
}
t.p('				');
if (testInst.normalResult) {
t.p('					');
if (testInst.normalResult.normal) {
t.p('						');
for (var i7=0;  i7<testInst.normalResult.normal.length; i7++) {
var norm = testInst.normalResult.normal[i7];
velocityCount = i7;
t.p('							');
if (norm.description) {
t.p('								');
if (norm.description.text) {
t.p('									<i>Normal Values: ');
t.p( norm.description.text);
t.p('</i><br/>								');
}
t.p('							');
}
t.p('						');
}
velocityCount = i4;
t.p('					');
}
t.p('				');
}
t.p('				');
if (testInst.ccrDataObjectID) {
t.p('					CCR DataObjectID: ');
t.p( testInst.ccrDataObjectID);
t.p('<br/>				');
}
t.p('			');
}
velocityCount = i2;
t.p('			</div>		');
}
else {
t.p('			<b>No test code provided</b><br/>		');
}
t.p('		<hr/>	');
}

}
else {
t.p('	<b>No test results provided</b>');
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
t.p('\')">Show alerts</a>    <a href="');
t.p('#" onclick="showResults(\'');
t.p( patient.id);
t.p('\')">Show results</a></div>');
}
velocityCount = 0;
return t.toString();
}
