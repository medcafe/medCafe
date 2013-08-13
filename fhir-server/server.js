
//http server
var fs = require('fs');
var httpServer = require('http');
var path = require('path');
var connect = require('connect');
//mongo server
var mongoose = require('mongoose/');
var restify = require('restify');  

var config = require('./config');

// localhost

var mongodbPort = 8888;

var sendHTML = function( filePath, contentType, response ){

  console.log('sendHTML: ' + filePath) ;

  path.exists(filePath, function( exists ) {
     
        if (exists) {
            fs.readFile(filePath, function(error, content) {
                if (error) {
                    response.writeHead(500);
                    response.end();
                }
                else {
                    response.writeHead(200, { 'Content-Type': contentType });
                    response.end(content, 'utf-8');
                }
            });
        }
        else {
            response.writeHead(404);
            response.end();
        }
    });
}

var getFilePath = function(url) {

  var filePath = './app' + url;
  if (url == '/' ) filePath = './app/index.html';

  console.log("url: " + url)

  return filePath;
}

var getContentType = function(filePath) {
   
   var extname = path.extname(filePath);
   var contentType = 'text/html';
    
    switch (extname) {
        case '.js':
            contentType = 'text/javascript';
            break;
        case '.css':
            contentType = 'text/css';
            break;
    }

    return contentType;
}

var onHtmlRequestHandler = function(request, response) {

  console.log('onHtmlRequestHandler... request.url: ' + request.url) ;

  /*
   when this is live, nodjitsu only listens on 1 port(80) so the httpServer will hear it first but
   we need to pass the request to the mongodbServer
   */
  if ( process.env.PORT && url === '/messages') {
    
    // pass the request to mongodbServer
   

    return; 
  } 

  var filePath = getFilePath(request.url);
  var contentType = getContentType(filePath);

  console.log('onHtmlRequestHandler... getting: ' + filePath) ;

  sendHTML(filePath, contentType, response); 

}

//httpServer.createServer(onHtmlRequestHandler).listen(httpPort); 

/// MONGODB - saves data in the database and posts data to the browser

var mongoURI = ( process.env.PORT ) ? config.creds.mongoose_auth_jitsu : config.creds.mongoose_auth_local;

db = mongoose.connect(mongoURI),
Schema = mongoose.Schema;  

var mongodbServer = restify.createServer({
    formatters: {
        'application/json': function(req, res, body){
            if(req.params.callback){
                var callbackFunctionName = req.params.callback.replace(/[^A-Za-z0-9_\.]/g, '');
                return callbackFunctionName + "(" + JSON.stringify(body) + ");";
            } else {
                return JSON.stringify(body);
            }
        },
        'text/html': function(req, res, body){
            return body;
        }
    }
});

mongodbServer.use(restify.bodyParser());

// Create a schema for our data
var MessageSchema = new Schema({
    message: {}
});

// Use the schema to register a model
mongoose.model('Message', MessageSchema); 
var MessageMongooseModel = mongoose.model('Message'); // just to emphasize this isn't a Backbone Model


/*

this approach was recommended to remove the CORS restrictions instead of adding them to each request
but its not working right now?! Something is wrong with adding it to mongodbServer

// Enable CORS
mongodbServer.all( '/*', function( req, res, next ) {
  res.header( 'Access-Control-Allow-Origin', '*' );
  res.header( 'Access-Control-Allow-Method', 'POST, GET, PUT, DELETE, OPTIONS' );
  res.header( 'Access-Control-Allow-Headers', 'Origin, X-Requested-With, X-File-Name, Content-Type, Cache-Control' );
  if( 'OPTIONS' == req.method ) {
  res.send( 203, 'OK' );
  }
  next();
});


*/

var searchObservationParams = function(req, res, next) {
  // Resitify currently has a bug which doesn't allow you to set default headers
  // This headers comply with CORS and allow us to mongodbServer our response to any origin
  res.header( 'Access-Control-Allow-Origin', '*' );
  res.header( 'Access-Control-Allow-Method', 'POST, GET, PUT, DELETE, OPTIONS' );
  res.header( 'Access-Control-Allow-Headers', 'Origin, X-Requested-With, X-File-Name, Content-Type, Cache-Control' );
  
  if( 'OPTIONS' == req.method ) {
    res.send( 203, 'OK' );
  }

  var url =require('url');
  var url_parts = url.parse(req.url, true);

  var name       = url_parts.query.name;
  var subject    = url_parts.query.subject;
  var performer  = url_parts.query.performer;

  query = {};
 
  if(subject){
    query["message.content.Observation.subject.reference.value"] = 'patient/@' + subject ;
  }
  if(performer){
    query["message.content.Observation.performer.reference.value"] = 'practitioner/@' + performer ;
  }

  if(name){
    query['$or'] = [
	{ "message.content.Observation.name.coding.code.value" : name  },
    	{ "message.content.Observation.name.coding.display.value" : new RegExp( '.*' + name + '.*' ) }
    ];
  }

  MessageMongooseModel.find(query).execFind(function (arr,data) {
    res.send(data);
  });
 
}




var searchPractitionerName = function(req, res, next) {
  // Resitify currently has a bug which doesn't allow you to set default headers
  // This headers comply with CORS and allow us to mongodbServer our response to any origin
  res.header( 'Access-Control-Allow-Origin', '*' );
  res.header( 'Access-Control-Allow-Method', 'POST, GET, PUT, DELETE, OPTIONS' );
  res.header( 'Access-Control-Allow-Headers', 'Origin, X-Requested-With, X-File-Name, Content-Type, Cache-Control' );
  
  if( 'OPTIONS' == req.method ) {
    res.send( 203, 'OK' );
  }

  var url =require('url');
  var url_parts = url.parse(req.url, true);
  var search_name = url_parts.query.name;
  var family_name = url_parts.query.family;
  var given_name  = url_parts.query.given;

  if(search_name){
      console.log("search name : " + search_name);
      query = { $or : [ {"message.content.Practitioner.name.family.value": new RegExp('^' + search_name + '.*') }, {"message.content.Practitioner.name.given.value": new RegExp('^' + search_name + '.*' ) } ]  
      };
  }
  else{
    if(family_name && given_name){
      query = {  "message.content.Practitioner.name.family.value": new RegExp('^' + family_name + '.*') , 
                  "message.content.Practitioner.name.given.value": new RegExp('^' + given_name + '.*') 
      };
    }
    else if(family_name){
      query = {  "message.content.Practitioner.name.family.value": new RegExp('^' + family_name + '.*') 
      };
    }
    else if(given_name){
      query = {  "message.content.Practitioner.name.given.value": new RegExp('^' + given_name + '.*') 
      };
    }
    else {
      query = {};
    }
  }

  MessageMongooseModel.find(query).execFind(function (arr,data) {
    res.send(data);
  });
 
}



var searchPatientName = function(req, res, next) {
  // Resitify currently has a bug which doesn't allow you to set default headers
  // This headers comply with CORS and allow us to mongodbServer our response to any origin
  res.header( 'Access-Control-Allow-Origin', '*' );
  res.header( 'Access-Control-Allow-Method', 'POST, GET, PUT, DELETE, OPTIONS' );
  res.header( 'Access-Control-Allow-Headers', 'Origin, X-Requested-With, X-File-Name, Content-Type, Cache-Control' );
  
  if( 'OPTIONS' == req.method ) {
    res.send( 203, 'OK' );
  }

  var url =require('url');
  var url_parts = url.parse(req.url, true);
  var search_name = url_parts.query.name;
  var family_name = url_parts.query.family;
  var given_name  = url_parts.query.given;

  if(search_name){
      console.log("search name : " + search_name);
      query = { $or : [ {"message.content.Patient.name.family.value": new RegExp('^' + search_name + '.*') }, {"message.content.Patient.name.given.value": new RegExp('^' + search_name + '.*' ) } ]  
      };
  }
  else{
    if(family_name && given_name){
      query = {  "message.content.Patient.name.family.value": new RegExp('^' + family_name + '.*') , 
                  "message.content.Patient.name.given.value": new RegExp('^' + given_name + '.*') 
      };
    }
    else if(family_name){
      query = {  "message.content.Patient.name.family.value": new RegExp('^' + family_name + '.*') 
      };
    }
    else if(given_name){
      query = {  "message.content.Patient.name.given.value": new RegExp('^' + given_name + '.*') 
      };
    }
    else {
      query = {};
    }
  }

  MessageMongooseModel.find(query).execFind(function (arr,data) {
    res.send(data);
  });
 
}

var searchPatients = function(req, res, next) {
  // Resitify currently has a bug which doesn't allow you to set default headers
  // This headers comply with CORS and allow us to mongodbServer our response to any origin
  res.header( 'Access-Control-Allow-Origin', '*' );
  res.header( 'Access-Control-Allow-Method', 'POST, GET, PUT, DELETE, OPTIONS' );
  res.header( 'Access-Control-Allow-Headers', 'Origin, X-Requested-With, X-File-Name, Content-Type, Cache-Control' );
  
  if( 'OPTIONS' == req.method ) {
    res.send( 203, 'OK' );
  }
  
  console.log("mongodbServer searchSubjects: " + req.params.id);

    MessageMongooseModel.find({"message.title": new RegExp('^Patient.*' + req.params.refid) }).execFind(function (arr,data) {
    res.send(data);
  });
}

var searchPractitioners = function(req, res, next) {
  // Resitify currently has a bug which doesn't allow you to set default headers
  // This headers comply with CORS and allow us to mongodbServer our response to any origin
  res.header( 'Access-Control-Allow-Origin', '*' );
  res.header( 'Access-Control-Allow-Method', 'POST, GET, PUT, DELETE, OPTIONS' );
  res.header( 'Access-Control-Allow-Headers', 'Origin, X-Requested-With, X-File-Name, Content-Type, Cache-Control' );
  
  if( 'OPTIONS' == req.method ) {
    res.send( 203, 'OK' );
  }
  
  console.log("mongodbServer searchProviders: " + req.params.refid);

  //MessageMongooseModel.find({ "message.content.Observation.performer.reference.value" : "practitioner/@" + req.params.refid}).execFind(function (arr,data) {
  
    MessageMongooseModel.find({"message.title": new RegExp('^Practitioner.*' + req.params.refid) }).execFind(function (arr,data) {
        res.send(data);
    });
}

// This function is responsible for returning all entries for the Message model
var getPatients = function(req, res, next) {
  // Resitify currently has a bug which doesn't allow you to set default headers
  // This headers comply with CORS and allow us to mongodbServer our response to any origin
  res.header( 'Access-Control-Allow-Origin', '*' );
  res.header( 'Access-Control-Allow-Method', 'POST, GET, PUT, DELETE, OPTIONS' );
  res.header( 'Access-Control-Allow-Headers', 'Origin, X-Requested-With, X-File-Name, Content-Type, Cache-Control' );
  
  if( 'OPTIONS' == req.method ) {
    res.send( 203, 'OK' );
  }
  
  console.log("mongodbServer getPatient");

  MessageMongooseModel.find({"message.title": new RegExp('^Patient') }).execFind(function (arr,data) {
    res.send(data);
  });
}

// This function is responsible for returning all entries for the Message model
var getPractitioners = function(req, res, next) {
  // Resitify currently has a bug which doesn't allow you to set default headers
  // This headers comply with CORS and allow us to mongodbServer our response to any origin
  res.header( 'Access-Control-Allow-Origin', '*' );
  res.header( 'Access-Control-Allow-Method', 'POST, GET, PUT, DELETE, OPTIONS' );
  res.header( 'Access-Control-Allow-Headers', 'Origin, X-Requested-With, X-File-Name, Content-Type, Cache-Control' );
  
  if( 'OPTIONS' == req.method ) {
    res.send( 203, 'OK' );
  }
  
  console.log("mongodbServer getPractitioner");

  MessageMongooseModel.find({"message.title": new RegExp('^Practitioner') }).execFind(function (arr,data) {
    res.send(data);
  });
}


// This function is responsible for returning all entries for the Message model
var searchObservations = function(req, res, next) {
  // Resitify currently has a bug which doesn't allow you to set default headers
  // This headers comply with CORS and allow us to mongodbServer our response to any origin
  res.header( 'Access-Control-Allow-Origin', '*' );
  res.header( 'Access-Control-Allow-Method', 'POST, GET, PUT, DELETE, OPTIONS' );
  res.header( 'Access-Control-Allow-Headers', 'Origin, X-Requested-With, X-File-Name, Content-Type, Cache-Control' );
  
  if( 'OPTIONS' == req.method ) {
    res.send( 203, 'OK' );
  }
  
  console.log("mongodbServer searchObs: " + req.params.refid);

  MessageMongooseModel.find({"message.title": new RegExp('^Observation.*' + req.params.refid) }).execFind(function (arr,data) {
    res.send(data);
  });
}


// This function is responsible for returning all entries for the Message model
var getObservations = function(req, res, next) {
  // Resitify currently has a bug which doesn't allow you to set default headers
  // This headers comply with CORS and allow us to mongodbServer our response to any origin
  res.header( 'Access-Control-Allow-Origin', '*' );
  res.header( 'Access-Control-Allow-Method', 'POST, GET, PUT, DELETE, OPTIONS' );
  res.header( 'Access-Control-Allow-Headers', 'Origin, X-Requested-With, X-File-Name, Content-Type, Cache-Control' );
  
  if( 'OPTIONS' == req.method ) {
    res.send( 203, 'OK' );
  }
  
  console.log("mongodbServer getMessages");

  MessageMongooseModel.find({"message.title": /^Observation/}).execFind(function (arr,data) {
    res.send(data);
  });
}

var postMessage = function(req, res, next) {
  res.header( 'Access-Control-Allow-Origin', '*' );
  res.header( 'Access-Control-Allow-Method', 'POST, GET, PUT, DELETE, OPTIONS' );
  res.header( 'Access-Control-Allow-Headers', 'Origin, X-Requested-With, X-File-Name, Content-Type, Cache-Control' );
  
  if( 'OPTIONS' == req.method ) {
    res.send( 203, 'OK' );
  }
  
  // Create a new message model, fill it up and save it to Mongodb
  var message = new MessageMongooseModel(); 
  
  console.log("mongodbServer postMessage: " + req.body);

  message.message = JSON.parse(req.body);
  message.save(function () {
    res.send(req.body);
  });
}

mongodbServer.listen(mongodbPort, function() {
  
  var consoleMessage = '\n Fhir server api:\n';
      consoleMessage += ' - /observation/history \n';
      consoleMessage += ' - /observation/search \n';
      consoleMessage += ' - /observation/:refid \n';

      consoleMessage += ' - /patient/history \n';
      consoleMessage += ' - /patient/search \n';
      consoleMessage += ' - /patient/:refid \n';

      consoleMessage += ' - /practitioner/history \n';
      consoleMessage += ' - /practitioner/search \n';
      consoleMessage += ' - /practitioner/:refid \n';
 
      consoleMessage += '++++++++++++++++++++++++++++++++++++++++++ \n\n'  
 
  console.log(consoleMessage, mongodbServer.name, mongodbServer.url);

});


/////   All records
mongodbServer.get('/observation/history', getObservations);
mongodbServer.get('/patient/history', getPatients);
mongodbServer.get('/practitioner/history', getPractitioners);

/////   Write new records...
mongodbServer.post('/observation', postMessage);
mongodbServer.post('/patient', postMessage);
mongodbServer.post('/practitioner', postMessage);

/////   Searches!
mongodbServer.get('/patient/search', searchPatientName);
mongodbServer.get('/practitioner/search', searchPractitionerName);
mongodbServer.get('/observation/search', searchObservationParams);

/////   finding specific records
mongodbServer.get('/observation/:refid', searchObservations);
mongodbServer.get('/patient/:refid', searchPatients);
mongodbServer.get('/practitioner/:refid', searchPractitioners);


