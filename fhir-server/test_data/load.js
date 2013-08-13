var http = require('http');
require('./'+ process.argv[2]);

for(i=0;i<list.entry.length;i++){
    if( list.entry[i] != undefined ){
        json_string = JSON.stringify(list.entry[i]);
        console.log("************* entry " + i + " ***************");
        console.log(JSON.stringify(json_string));


var headers = {
  'Content-Type': 'application/json',
  'Content-Length': json_string.length
};

var options = {
  host: 'localhost',
  port: 8888,
  path: '/entries',
  method: 'POST',
  headers: headers
};

// Setup the request.  The options parameter is
// the object we defined above.
var req = http.request(options, function(res) {
  res.setEncoding('utf-8');

  var responseString = '';

  res.on('data', function(data) {
    responseString += data;
  });

  res.on('end', function() {
    console.log("server response: " + responseString);
  });
});

req.on('error', function(e) {
  console.log("Error");
});

req.write(json_string);
req.end();


    }
}

