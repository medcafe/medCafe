function v2js_hellojson(context) { 
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
