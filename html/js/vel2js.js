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
function v2js_listImages(context) { 
var t = new StringCat();
var velocityCount = 0;
if (context.velocityCount) velocityCount=context.velocityCount;
for (var i1=0;  i1<context.images.length; i1++) {
var image = context.images[i1];
velocityCount = i1;
t.p('    <cover>    <img><![CDATA[');
t.p( image.source);
t.p(']]></img>    <desc><![CDATA[');
t.p( image.id);
t.p(']]></desc>    <param><![CDATA[');
t.p( image.param);
t.p(']]></param>    </cover>');
}
velocityCount = 0;
return t.toString();
}
