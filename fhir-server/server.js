var fs = require('fs');
var httpServer = require('http');
var path = require('path');
var connect = require('connect');
var mongoose = require('mongoose/');
var restify = require('restify');  

var config = require('./config');

var local_port = 8888;

//Hopefully this is never used in production, but (god forbid) you can change this.... walk with god.
var root_url = 'http://localhost:' + local_port;
var replace_url = "http://hl7connect.healthintersections.com.au/svc/fhir";

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
    entry : {}
});

// Use the schema to register a model
mongoose.model('Message', MessageSchema); 
var MessageMongooseModel = mongoose.model('Message'); // just to emphasize this isn't a Backbone Model

var format_fhir = function(entries) {
    //TODO put JSON definition of the document junk here
    var entry_array = new Array();
    for(i=0;i<entries.length;i++){
        entry_array.push(entries[i].entry);
    }
    var finished_doc = {
        'title' : "document",
        'entries' : entry_array
    };
    return finished_doc;
}

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
    query["entry.content.Observation.subject.reference.value"] = 'patient/@' + subject ;
  }
  if(performer){
    query["entry.content.Observation.performer.reference.value"] = 'practitioner/@' + performer ;
  }

  if(name){
    query['$or'] = [
	{ "entry.content.Observation.name.coding.code.value" : name  },
    	{ "entry.content.Observation.name.coding.display.value" : new RegExp( '.*' + name + '.*' ) }
    ];
  }

  MessageMongooseModel.find(query).execFind(function (arr,data) {
    res.send(format_fhir(data));
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
      query = { $or : [ {"entry.content.Practitioner.name.family.value": new RegExp('^' + search_name + '.*') }, {"message.content.Practitioner.name.given.value": new RegExp('^' + search_name + '.*' ) } ]  
      };
  }
  else{
    if(family_name && given_name){
      query = {  "entry.content.Practitioner.name.family.value": new RegExp('^' + family_name + '.*') , 
                  "entry.content.Practitioner.name.given.value": new RegExp('^' + given_name + '.*') 
      };
    }
    else if(family_name){
      query = {  "entry.content.Practitioner.name.family.value": new RegExp('^' + family_name + '.*') 
      };
    }
    else if(given_name){
      query = {  "entry.content.Practitioner.name.given.value": new RegExp('^' + given_name + '.*') 
      };
    }
    else {
      query = {};
    }
  }

  MessageMongooseModel.find(query).execFind(function (arr,data) {
    res.send(format_fhir(data));
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
      query = { $or : [ {"entry.content.Patient.name.family.value": new RegExp('^' + search_name + '.*') }, {"entry.content.Patient.name.given.value": new RegExp('^' + search_name + '.*' ) } ]  
      };
  }
  else{
    if(family_name && given_name){
      query = {  "entry.content.Patient.name.family.value": new RegExp('^' + family_name + '.*') , 
                  "entry.content.Patient.name.given.value": new RegExp('^' + given_name + '.*') 
      };
    }
    else if(family_name){
      query = {  "entry.content.Patient.name.family.value": new RegExp('^' + family_name + '.*') 
      };
    }
    else if(given_name){
      query = {  "entry.content.Patient.name.given.value": new RegExp('^' + given_name + '.*') 
      };
    }
    else {
      query = {};
    }
  }

  MessageMongooseModel.find(query).execFind(function (arr,data) {
    res.send(format_fhir(data));
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

    MessageMongooseModel.find({"entry.title": new RegExp('^Patient.*' + req.params.refid) }).execFind(function (arr,data) {
    res.send(format_fhir(data));
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

    MessageMongooseModel.find({"entry.title": new RegExp('^Practitioner.*' + req.params.refid) }).execFind(function (arr,data) {
        res.send(format_fhir(data));
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

  MessageMongooseModel.find({"entry.title": new RegExp('^Patient') }).execFind(function (arr,data) {
    res.send(format_fhir(data));
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

  MessageMongooseModel.find({"entry.title": new RegExp('^Practitioner') }).execFind(function (arr,data) {
    res.send(format_fhir(data));
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

  MessageMongooseModel.find({"entry.title": new RegExp('^Observation.*' + req.params.refid) }).execFind(function (arr,data) {
    res.send(format_fhir(data));
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

  MessageMongooseModel.find({"entry.title": /^Observation/}).execFind(function (arr,data) {
    res.send(format_fhir(data));
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
  var entry = req.body.replace(new RegExp(replace_url, 'g'), root_url); //TODO: lol!
  message.entry = JSON.parse(entry);
  message.save(function () {
    res.send(entry); //TODO, actual response code? How about validation?
  });
}

mongodbServer.listen(local_port, function() {
  
  var consoleMessage = '\n Simple Fhir server api: port ' + local_port;
      consoleMessage += '\n - /observation/history \n';
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


