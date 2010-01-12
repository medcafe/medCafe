/*
 * ShareThis Widget Version 4.2.0-rc1
 * 9/09/09 ShareThis.com 
 */
if(lang=='en')
{
	var langTextArray= new Array();
	langTextArray['btn_ok']="OK";
	langTextArray['email']="Email";
	langTextArray['msg_drag']="Drag this to your bookmarks bar.";
	langTextArray['msg_right_click']="Right-click on this and add it to your favorites.";
	langTextArray['msg_username']="Please enter your username.";
	langTextArray['msg_password']="Please enter your password.";
	langTextArray['using']="using";
	langTextArray['email_message']="Message:";
	langTextArray['msg_blogger_dtls']="Invalid Blogger Username or Password";
	langTextArray['msg_signon_blog']="You have successfully signed on. Please select a blog.";
	langTextArray['msg_cred_rem']="Remembering Credentials is for Signed in Users Only";
	langTextArray['msg_not_connect']="Unable to connect to your blog.";
	langTextArray['msg_error_post']="Error while trying to post to your blog.";
	langTextArray['msg_status_update']="Please enter a status update.";
	langTextArray['msg_twitter_long']="Twitter post too long. Please shorten to less than 140 characters.";
	langTextArray['msg_twitter_dtls']="Invalid Twitter Username or Password";
	langTextArray['msg_server_connect']="Unable to connect to ShareThis server.";
	langTextArray['msg_typepad_dtls']="Invalid Typepad Username or Password";
	langTextArray['msg_enter_url']="Please enter a url.";
	langTextArray['msg_check_userpass']="Please Check Username and Password.";
	langTextArray['msg_postfail']="Post Failed.";
	langTextArray['msg_invalid_userpass']="Invalid Username or password.";
	langTextArray['msg_seprated_comma']="Separated by commas";
	langTextArray['msg_shared_using']="Shared using ShareThis";
	langTextArray['msg_enter_email']="Please enter your valid E-mail address";
	langTextArray['msg_enter_recip']="Please enter a recipient";
	langTextArray['msg_phone_no']="Not a valid Phone Number";
	langTextArray['msg_aim_screen']="Not a valid AIM Screen Name";
	langTextArray['msg_email_invalid']="Not a valid E-mail address";
	langTextArray['msg_enter_valid_email']="Please enter an E-mail address.";
	langTextArray['msg_email_address']="Please enter a valid E-mail address.";
	langTextArray['msg_your_name']="Please enter your Name.";
	langTextArray['msg_enter_username']="Please enter a username.";
	langTextArray['msg_enter_apassword']="Please enter a password.";
	langTextArray['msg_password_len']="Password must be at least 6 characters.";
	langTextArray['msg_password_match']="Please confirm that your passwords match.";
	langTextArray['msg_dtls_inuse']="E-mail address or Username is already in use.";
	langTextArray['msg_reg_incomplete']="Could not complete registration.";
	langTextArray['msg_optional']="optional";
	langTextArray['msg_comments']="Comments";
	langTextArray['msg_item_saved']="This item was successfully saved to your ShareBox.";
	langTextArray['msg_not_save_sharebox']="Could not save to your ShareBox.";
	langTextArray['msg_creating_acc']="Creating your account";
	langTextArray['msg_your_email']="Your Email Address";
	langTextArray['msg_enter_recipto']="Please enter a recipient in the 'To' field";
	langTextArray['msg_less_recip']="Please enter less than 25 recipients";
	langTextArray['msg_not_sent']="Could not send your message.";
	langTextArray['msg_importing_cont']="Importing contacts";
	langTextArray['msg_succ_cont']="Success! #%num%# contacts imported."
	langTextArray['msg_not_retrive']="Could not retrieve your contacts.";
	langTextArray['msg_import_serv']="Import contacts from";
	langTextArray['msg_service']="We'll take you to #%name%# where you'll be asked to let ShareThis access your address book.";
	langTextArray['msg_fail_cont']="Contact Import Failed.";
	langTextArray['msg_timeout']="Authorization Timed Out.";
	langTextArray['acc_linking_info']="We have identified an account that already exists with the same email.  Would you like to link the accounts so that they share the same contacts and ShareBox?  If so, then verify the password for the existing account below.";
	langTextArray['yahoo_linking_info']="Would you like to link your Yahoo credentials to an existing ShareThis account so that your shares & contacts will be saved to the same place?  If so, then please provide the appropriate sign in credentials below.";
	langTextArray['msg_unable_signin']="Unable to Sign In";
	langTextArray['msg_yahoo_signin']="Sign In with Yahoo!";
	langTextArray['msg_yahoo']="We'll take you to Yahoo where you'll be asked to link your Yahoo account to ShareThis.";
	langTextArray['msg_feed']="Include shares in my Y! feed";
	langTextArray['msg_share_yahoo_updt']="Post shares to Yahoo! Updates";
	langTextArray['msg_import_yahoo']="Import my Yahoo! contacts";
	langTextArray['msg_signin_share']="Sign In with ShareThis";
	langTextArray['msg_waiting_auth']="Waiting for Authorization";
	langTextArray['username']="Username:";
	langTextArray['password']="Password:";
	langTextArray['msg_failed_signin']="Signin Failed.";
	langTextArray['msg_shared']="Your message was successfully shared!";
	langTextArray['msg_posting_share']="Posting your share to";
	langTextArray['msg_posting_blog']="Post this to your blog";
	langTextArray['msg_lovejournal']="Post this to Livejournal.";
	langTextArray['msg_post_typepad']="Post this to your Typepad blog.";
	langTextArray['msg_post_wordpress']="Post this to your WordPress blog.";
	langTextArray['msg_comment']="Comment:";
	langTextArray['msg_text']="Text";
	langTextArray['msg_must_register']="You must register to save items to a ShareBox.";
	langTextArray['msg_encoded']="Encoded URLs are not allowed";//need to confirm for translation
	langTextArray['msg_usrl_share']="Shar URLs are allowed"; //need to confirm for translation
	langTextArray['msg_goto']="Go To Page";//need to confirm for translation
	langTextArray['msg_incorrect_pass']="Email/Username or Password is incorrect. Please try again.";
	langTextArray['msg_server_connect_auth']="Unable to connect to ShareThis authentication server.";
	langTextArray['msg_logingin']="Logging in";
	langTextArray['post_twitter']="Post this to your Twitter status.";
	langTextArray['msg_nichname_inuse']='Nickname already in use.';
	langTextArray['msg_loading']='Loading...';
}

//widget-class.js
var Widget = new Class(
	{ Implements: Events,
	  shareables: [],
	  shareablesKey: "",
	  shareableIndex: 0,
	  authToken:"",
	  user: null,
	  maxContactSearchResults: 5,
	  maxSendMessageLength: 2000,
	  
	  initialize: function() {
		  this.user = new Widget.User(this);
		  window.contacts = this.contacts;
		  this.pages = this.initializePages(this.pages);
	  },
	  
	  setMaxContactSearchResults: function(max) {
		  this.maxContactSearchResults = max;
	  },

	  userIsSignedIn: function() {
		  return this.user.isSignedIn();
	  },

	  signIn: function() {
		  var err_set=false;
		  var err="";
		  if($('login_username').value===""){
			  err+=langTextArray['msg_username']+"\n";
			  err_set=true;
		  }
		  if($('login_password').value===""){
			  err+=langTextArray['msg_password'];
			  err_set=true;
		  }
		  if(!err_set){
			  this.user.signIn($('login_username').value,$('login_password').value);
		  }
		  else{
			  widget.validationFailed(err);
		  }
	  },
	  
	  signOut: function () {
		  forgetUser();
		  this.user.signOut();
	  },
	  limitCharacters: function(inputField, maxCharacterCount, counterSpan, event) {
		  var retval = true;
		  if(inputField.value.length >= maxCharacterCount) {
			  switch (event.key) {
			  case "up": 
			  case "down": 
			  case "left": 
			  case "right": 
			  case "del": 
			  case "delete": 
			  case "backspace": 
				  retval = true;
				  break;
			  default:
				  retval = false;
				  break;
			  }
		  }
		  else if (inputField.value.length >= maxCharacterCount - 10) {
			  counterSpan.style.color = "#FF0000";
		  }
		  else if (inputField.value.length >= maxCharacterCount - 20) {
			  counterSpan.style.color = "#5C0002";
		  }
		  else {
			  counterSpan.style.color = "#666666";
		  }
		  counterSpan.set('html', maxCharacterCount - inputField.value.length);
		  return retval;
	  },
	  
	  
	  postBlogger: function() {
		  var username=$('post_username').value;
		  var password=$('post_password').value;
		  if ($('post_remember_me').checked) {
			  var rememberme=1;
		  }
		  else {
			  var rememberme=0;
		  }
		  if ($('post_forget_me').value === 'true') {
			  var forgetme=1;
		  }
		  else {
			  var forgetme=0;
		  }
		  var err="";
		  var err_set=false;
		  if(!username&&!blogid){
			  err_set=true;
			  err+=langTextArray['msg_enter_username']+"\n";
		  }
		  if(!password&&!blogid){
			  err_set=true;	
			  err+=langTextArray['msg_enter_apassword']+"\n";
		  }
		  if(err_set){
			  widget.validationFailed(err);
		  }
		  else {
			  if (document.getElementById('post_select')) {
				  var blogid = $('post_select').get('value');
			  }
			  var data = "";
			  var tmpTitle=glo_title;
			  try{tmpTitle=decodeURIComponent(glo_title);}catch(err){}
			  var atag = "<a href="+getSharURL()+">"+tmpTitle+"</a>";
			  
			  if(glo_content!==""){
				  atag="";
			  }
			  atag=encodeURIComponent(atag);
			  if(!blogid) {
				  data="username="+username+"&password="+password+"&d="+glo_content+atag+"&draft="+glo_bloggerDraft
					  +"&t="+glo_title+"&return=json&rememberme="+rememberme+"&forgetme="+forgetme;
			  }
			  else{
				  data="username="+username+"&password="+password+"&d="+glo_content+atag+"&draft="+glo_bloggerDraft
					  +"&t="+glo_title+"&return=json&blogid="+blogid+"&rememberme="+rememberme+"&forgetme="+forgetme;
			  }
			  var request=new Request({
				  method: "post",
				  url: "/api/postBlogger_ws.php",
				  data: data,
				  onFailure: function(){logError("postblogger","Ajax Failure");},
				  onSuccess:this.postBlogger_onSuccess.bind(this)
			  });
			  this.fireEvent('postToServiceRequested', 'blogger');
			  request.send();
		  }
	  },
	  postBlogger_onSuccess: function(responseText, responseXML) {
		  try{var resp = JSON.decode(responseText);}
		  catch(err){logError("post blogger",responseText);}
		  if (resp.status.toLowerCase() == 'success') {
			  this.fireEvent('postToServiceSucceeded', 'blogger');
		  }
		  if(resp.status==="FAILURE"){
			  if(resp.errorMessage && resp.errorMessage.toLowerCase()==="auth_failed") {
				  this.fireEvent('postToServiceFailed', [
					  'blogger', 
					  langTextArray['msg_blogger_dtls']
				  ]);
			  } 
			  else if (resp.errorMessage && resp.errorMessage.toLowerCase()==="multiple") {
				  this.fireEvent('postToServiceNeedsMoreInfo', [
					  'blogger', 
					  langTextArray['msg_signon_blog'],
					  resp.data
				  ]);
			  }
			  else if (resp.statusMessage && resp.statusMessage.toLowerCase()==="missing_parameter_token") {
				  this.fireEvent('postToServiceFailed', [
					  'blogger', 
					  langTextArray['msg_cred_rem'],
					  resp.data
				  ]);
			  }
			  //MISSING_PARAMETER_TOKEN
			  else {
				  logError("get contacts",JSON.encode(resp));
				  this.fireEvent('postToServiceFailed', [
					  'blogger', 
					  langTextArray['msg_not_connect']
				  ]);
			  }
		  }
	  },


	  postLive_journal: function(){ 
		  var username=$('post_username').value;
		  var password=$('post_password').value;
		  var comment=$('post_message').value;
		  if(comment===langTextArray['msg_optional']){comment="";}
		  comment=encodeURIComponent(comment);
		  if ($('post_remember_me').checked) {
			  var rememberme=1;
		  }
		  else {
			  var rememberme=0;
		  }
		  if ($('post_forget_me').value === 'true') {
			  var forgetme=1;
		  }
		  else {
			  var forgetme=0;
		  }
		  var err="";
		  var err_set=false;
		  if(!username){
			  err_set=true;
			  err+=langTextArray['msg_enter_username']+"\n";
		  }
		  if(!password){
			  err_set=true;	
			  err+=langTextArray['msg_enter_apassword']+"\n";
		  }
		  var tmpTitle=glo_title;
		  try{tmpTitle=decodeURIComponent(glo_title);}catch(err){}
		  var atag="<a href="+getSharURL()+">"+tmpTitle+"</a>";
		  if(glo_content!==""){
			  atag="";
		  }
		  atag=encodeURIComponent(atag);
		  if(err_set){
			  widget.validationFailed(err);
		  }	
		  else{
			  var data="";
			  data="username="+username+"&password="+password+"&d="+comment+"<br/><br/>"+glo_content+atag
				  +"&t="+glo_title+"&return=json&rememberme="+rememberme+"&forgetme="+forgetme;
			  var request=new Request({
				  method: "post",
				  url: "/api/postLiveJournal_ws.php",
				  data: data,
				  onFailure: function(){logError("postLiveJournal","Ajax Failure");},
				  onSuccess:this.postLive_journal_onSuccess.bind(this)
			  });
			  this.fireEvent('postToServiceRequested', 'livejournal');
			  request.send();
		  }
	  },
	  postLive_journal_onSuccess: function(responseText, responseXML) {
		  try{var resp = JSON.decode(responseText);}
		  catch(err){logError("post_live_journal",responseText);}
		  if(resp.status.toLowerCase()==="success"){
			  this.fireEvent('postToServiceSucceeded', 'livejournal');
		  }
		  else {
			  logError("postLiveJournal",JSON.encode(resp));
			  this.fireEvent('postToServiceFailed', [
				  'livejournal', 
				  langTextArray['msg_error_post']
			  ]);
		  }
	  },

	  postTwitter: function() { 
		  var username=$('post_username').value;
		  var password=$('post_password').value;
		  var status=$('post_message').value;
		  if ($('post_remember_me').checked) {
			  var rememberme=1;
		  }
		  else {
			  var rememberme=0;
		  }
		  if ($('post_forget_me').value == 'true') {
			  var forgetme=1;
		  }
		  else {
			  var forgetme=0;
		  }
		  var err="";
		  var err_set=false;
		  if(!username){
			  err_set=true;
			  err+=langTextArray['msg_enter_username']+"\n";	
		  }
		  if(!password){
			  err_set=true;	
			  err+=langTextArray['msg_enter_apassword']+"\n";
		  }
		  if(!status){
			  err_set=true;	
			  err+=langTextArray['msg_status_update']+"\n";
		  }
		  if(status.length > 140){
			  err_set=true;	
			  err+=langTextArray['msg_twitter_long']+"\n";
		  }
		  if(err_set){
			  widget.validationFailed(err);
		  }
		  else{
			  var data="";
			  
			  data="username="+username+
				  "&password="+password+
				  "&status="+encodeURIComponent(status)+
				  "&rememberme="+rememberme+
				  "&forgetme="+forgetme+
				  "&url="+glo_url+
				  "&sessionID="+glo_sessionID+
				  "&return=json";
			  
			  var request=new Request({
				  method: "post",
				  url: "/api/postTwitter_ws.php",
				  data: data,
				  onFailure: function(){logError("postTwitter","Ajax Failure");},
				  onSuccess: this.postTwitter_onSuccess.bind(this)
			  });
			  this.fireEvent('postToServiceRequested', 'twitter');
			  request.send();
		  }
	  },	
	  postTwitter_onSuccess: function(responseText, responseXML) {
		  try{var resp = JSON.decode(responseText);}
		  catch(err){logError("postTwitter",responseText);}
		  if (resp.status && resp.status.toLowerCase() == 'success') {
			  this.fireEvent('postToServiceSucceeded', 'twitter.com');
		  }
		  else {
			  if (resp.errorMessage) {
				  if(resp.errorMessage.toLowerCase() == 'auth_failed') {
					  this.fireEvent('postToServiceFailed', [
						  'twitter', 
						  langTextArray['msg_twitter_dtls']
					  ]);
				  } else if (resp.statusMessage && (resp.statusMessage.toLowerCase()==="missing_parameter_token")) {
                          this.fireEvent('postToServiceFailed', [
                                          'twitter', 
                                          langTextArray['msg_cred_rem']
                          ]);
				  } else {
					  this.fireEvent('postToServiceFailed', [
						  'twitter',
						  resp.errorMessage
					  ]);
				  }
			  } else {
				  logError("postTwitter",JSON.encode(resp));
				  this.fireEvent('postToServiceFailed', [
					  'twitter',
					  langTextArray['msg_server_connect']
				  ]);
				  
			  }
		  }
	  },


	  postTypePad: function() { 
		  var username=$('post_username').value;
		  var password=$('post_password').value;
		  if ($('post_remember_me').checked) {
			  var rememberme=1;
		  }
		  else {
			  var rememberme=0;
		  }
		  if ($('post_forget_me').value == 'true') {
			  var forgetme=1;
		  }
		  else {
			  var forgetme=0;
		  }
		  var err="";
		  var err_set=false;
		  if(!username&&!blogid){
			  err_set=true;
			  err+=langTextArray['msg_enter_username']+"\n";	
		  }
		  if(!password&&!blogid){
			  err_set=true;	
			  err+=langTextArray['msg_enter_apassword']+"\n";
		  }
		  if(err_set){
			  widget.validationFailed(err);
		  }
		  else{
			  if(document.getElementById('post_select')){
				  var blogid = $('post_select').get('value');
			  }
			  var data="";
			  var tmpTitle=glo_title;
			  try{tmpTitle=decodeURIComponent(glo_title);}catch(err){}
			  var atag="<a href="+getSharURL()+">"+tmpTitle+"</a>";
			  if(glo_content!==""){
				  atag="";
			  }
			  atag=encodeURIComponent(atag);
			  
			  if(!blogid){
				  data="username="+username+"&password="+password+"&d="+glo_content+atag+"&draft="+glo_tpDraft
					  +"&t="+glo_title+"&return=json&rememberme="+rememberme+"&forgetme="+forgetme;
			  }
			  else{
				  data="blogid="+blogid+"&d="+glo_content+atag+"&draft="+glo_tpDraft+"&password="+password
					  +"&t="+glo_title+"&username="+username+"&return=json&rememberme="+rememberme+"&forgetme="+forgetme;
			  }

			  var request=new Request({
				  method: "post",
				  url: "/api/postTypePad_ws.php",
				  data: data,
				  onFailure: function(){logError("postTypePad","Ajax Failure");},
				  onSuccess: this.postTypePad_onSuccess.bind(this)
			  });
			  this.fireEvent('postToServiceRequested', 'typepad');
			  request.send();
		  }
	  },	

	  postTypePad_onSuccess: function(responseText, responseXML) {
		  try{var resp = JSON.decode(responseText);}
		  catch(err){logError("post typepad",responseText);}
		  if (resp.status.toLowerCase() == 'success') {
			  this.fireEvent('postToServiceSucceeded', 'typepad');
		  }
		  if (resp.status.toLowerCase() == 'failure') {
			  if (resp.errorMessage && resp.errorMessage.toLowerCase() == 'auth_failed') {
				  this.fireEvent('postToServiceFailed', [
					  'typepad', 
					  langTextArray['msg_typepad_dtls']
				  ]);
			  }
			  if (resp.errorMessage && resp.errorMessage.toLowerCase() == 'multiple') {
				  this.fireEvent('postToServiceNeedsMoreInfo', [
					  'typepad', 
					  langTextArray['msg_signon_blog'],
					  resp.data
				  ]);
			  }
			  else {
				  logError("postTypePad",JSON.encode(resp));
				  this.fireEvent('postToServiceFailed', [
					  'typepad',
					  langTextArray['msg_not_connect']
				  ]);
			  }
		  }
	  },
	  
	  postWordpress: function()
	  { 
		  var username=$('post_username').value;
		  var password=$('post_password').value;
		  var url=$('post_url').value;
		  if ($('post_remember_me').checked) {
			  var rememberme=1;
		  }
		  else {
			  var rememberme=0;
		  }
		  if ($('post_forget_me').value == 'true') {
			  var forgetme=1;
		  }
		  else {
			  var forgetme=0;
		  }
		  var err="";
		  var err_set=false;
		  if(!username)
		  {
			  err_set=true;
			  err+=langTextArray['msg_enter_username']+"\n";
		  }
		  if(!password){
			  err_set=true;	
			  err+=langTextArray['msg_enter_apassword']+"\n";
		  }
		  if(!url){
			  err_set=true;	
			  err+=langTextArray['msg_enter_url']+"\n";
		  }
		  var tmpTitle=glo_title;
		  try{tmpTitle=decodeURIComponent(glo_title);}catch(err){}
		  var atag="<a href="+getSharURL()+">"+tmpTitle+"</a>";
		  if(glo_content!==""){
			  atag="";
		  }
		  atag=encodeURIComponent(atag);
		  if(err_set){
			  this.validationFailed(err);
		  }
		  else{
			  var data="";
			  data="username="+username+"&password="+password+"&d="+glo_content+atag
				  +"&t="+glo_title+"&return=json&url="+url+"&rememberme="+rememberme+"&forgetme="+forgetme;

			  var request=new Request({
				  method: "post",
				  url: "/api/postWordPress_ws.php",
				  data: data,
				  onFailure: function(){logError("postWordpress","Ajax Failure");},
				  onSuccess:this.postWordpress_onSuccess.bind(this)
			  });
			  this.fireEvent('postToServiceRequested', 'wordpress');
			  request.send();
		  }
	  },

	  postWordpress_onSuccess: function(responseText, responseXML) {
		  try{var resp = JSON.decode(responseText);}
		  catch(err){logError("post wordpress",responseText);}
		  if(resp.status.toLowerCase()==="success"){
			  this.fireEvent('postToServiceSucceeded', 'wordpress');
		  }
		  else {
			  if (resp.errorMessage && resp.errorMessage.toLowerCase()==="connection_failed") {
				  this.fireEvent('postToServiceFailed', [
					  'wordpress',
					  langTextArray['msg_not_connect']
				  ]);
			  } 
			  else {
				  if(resp.errorMessage && resp.errorMessage.toLowerCase()=="auth_failed"){
					  this.fireEvent('postToServiceFailed', [
						  'wordpress',
						  langTextArray['msg_check_userpass']
					  ]);
				  }
				  else if(resp.statusMessage && (resp.statusMessage.toLowerCase()=="post_failed" 
												 || resp.statusMessage.toLowerCase() == 'post_wordpress_service_failed')){
					  this.fireEvent('postToServiceFailed', [
						  'wordpress',
						  langTextArray['msg_postfail']
					  ]);
				  }
				  else {
					  logError("postWordpress",JSON.encode(resp));
					  this.fireEvent('postToServiceFailed', [
						  'wordpress',
						  langTextArray['msg_not_connect']
					  ]);
				  }
			  }		
		  }		
	  },	
	  setImportContactService: function(serviceTag) {
		  this.currentImportContactService = this.contactSources[serviceTag];
		  this.fireEvent('importContactServiceChanged', serviceTag);
	  },
	  setLoginService: function(serviceTag) {
		  this.currentLoginService = this.loginSources[serviceTag];
		  this.fireEvent('loginServiceChanged', serviceTag);
	  },
	  
	  getQueryParam: function( name, url )
	  {
		  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
		  var regexS = "[\\?&]"+name+"=([^&#]*)";
		  var regex = new RegExp( regexS );
		  var results = regex.exec( url );
		  if( results == null ) {
			  return "";
		  } else {
			  return results[1];
		  }
	  },
	  
	  importContacts: function(service, username, password, contact_url, delt, thirdparty_token, silent) {		
		  Cookie.dispose('import', {domain: ".sharethis.com", path: '/'});
		  Cookie.dispose('import_delt', {domain: ".sharethis.com", path: '/'});
		  if(contact_url){
			  widget.popModalWorkingSheet();
		  }
		  var data = "provider=" + service.protocolName + "&username=" + username + "&password=" + password
			  + "&contact_url=" + contact_url
			  + "&base_url=http://" + window.location.hostname + '/'
			  + "&referer=share3x/import.php"
			  + "&delt=" + delt
			  + "&thirdparty_token=" + encodeURIComponent(thirdparty_token);
		  var request = new Request({
			  method: "post",
			  url: "/api/importContacts_ws.php",
			  data: data,
			  onFailure: function(){logError("import contacts","Ajax Failure");},
			  onSuccess: (function(responseText, responseXML) {
				  try{var resp = JSON.decode(responseText);}
				  catch(err){logError("import contacts",responseText);}
				  if (resp.status==="SUCCESS" && resp.data) {

					  // the api does not return the service on some contacts, apparently...
					  for (var i = 0; i < resp.data.contacts.length; i++) {
						  if (!('service' in resp.data.contacts[i])) {
							  var guess = Widget.Contact.guessServiceForAddress(resp.data.contacts[i].address, service.protocolName);
							  if (!guess) {
								  guess = service.protocolName;
							  }
							  resp.data.contacts[i].service = guess;
						  }
					  }
					  if (this.userIsSignedIn()) {
						  // wait to declare success until we successfully add the contacts to our account.
						  var successHandler = null;
						  var failureHandler = null;
						  
						  successHandler = (function() {
							  if(silent == 1) {
								  this.fireEvent('importContactsSucceededSilent', {
									  nContacts: resp.data.contacts.length,
									  rawData: resp.data.contacts
								  });
							  } else {
								  this.fireEvent('importContactsSucceeded', {
									  nContacts: resp.data.contacts.length,
									  rawData: resp.data.contacts
								  });
							  }
							  this.user.removeEvent('addContactsToAccountSucceeded', successHandler);
							  this.user.removeEvent('addContactsToAccountFailed', failureHandler);
						  }).bind(this);
						  this.user.addEvent('addContactsToAccountSucceeded', successHandler);
						  
						  failureHandler = (function() {
							  this.fireEvent('importContactsFailed');
							  this.user.removeEvent('addContactsToAccountSucceeded', successHandler);
							  this.user.removeEvent('addContactsToAccountFailed', failureHandler);
						  }).bind(this);
						  this.user.addEvent('addContactsToAccountFailed', failureHandler);

						  this.user.addContactsToAccount(resp.data.contacts);
					  }
					  else {
						  this.user.addContactsLocally(resp.data.contacts);
						  if(silent == 1) {
							  this.fireEvent('importContactsSucceededSilent', {
								  nContacts: resp.data.contacts.length,
								  rawData: resp.data.contacts
							  });
						  } else {
							  this.fireEvent('importContactsSucceeded', {
								  nContacts: resp.data.contacts.length,
								  rawData: resp.data.contacts
							  });
						  }
					  }
				  }
				  else if(resp.errorMessage=="LOGIN_FAILED :: Invalid credentials"){
					  widget.popModalWorkingSheet();
					  widget.pushModalErrorSheet(langTextArray['msg_invalid_userpass']);
				  }
				  else{
					  logError("import contacts",JSON.encode(resp));
					  this.fireEvent('importContactsFailed');
				  }
			  }).bind(this)
		  });
		  request.send();
		  this.fireEvent('importContactsRequested');
	  },
	  login: function(service, username, password, contact_url, delt) {	
		  if(service.protocolName=="sharethis"){
			  widget.signIn();
			  return true;
		  }
		  Cookie.dispose('import', {domain: ".sharethis.com", path: '/'});
		  Cookie.dispose('import_delt', {domain: ".sharethis.com", path: '/'});
		  if(contact_url){
			  widget.popModalWorkingSheet();
		  }
		  var data = "provider=" + service.protocolName + "&username=" + username + "&password=" + password
			  + "&contact_url=" + contact_url
			  + "&base_url=http://" + window.location.hostname + '/'
			  + "&referer=share3x/import.php"
			  + "&delt=" + delt;
		  var request = new Request({
			  method: "post",
			  url: "/api/importContacts_ws.php",
			  data: data,
			  onFailure: function(){logError("import contacts","Ajax Failure");},
			  onSuccess: (function(responseText, responseXML) {
				  try{var resp = JSON.decode(responseText);}
				  catch(err){logError("import contacts",responseText);}
				  if (resp.status==="SUCCESS" && resp.data) {

					  // the api does not return the service on some contacts, apparently...
					  for (var i = 0; i < resp.data.contacts.length; i++) {
						  if (!('service' in resp.data.contacts[i])) {
							  var guess = Widget.Contact.guessServiceForAddress(resp.data.contacts[i].address, service.protocolName);
							  if (!guess) {
								  guess = service.protocolName;
							  }
							  resp.data.contacts[i].service = guess;
						  }
					  }
					  if (this.userIsSignedIn()) {
						  // wait to declare success until we successfully add the contacts to our account.
						  var successHandler = null;
						  var failureHandler = null;
						  
						  successHandler = (function() {
							  this.fireEvent('importContactsSucceeded', {
								  nContacts: resp.data.contacts.length,
								  rawData: resp.data.contacts
							  });
							  this.user.removeEvent('addContactsToAccountSucceeded', successHandler);
							  this.user.removeEvent('addContactsToAccountFailed', failureHandler);
						  }).bind(this);
						  this.user.addEvent('addContactsToAccountSucceeded', successHandler);
						  
						  failureHandler = (function() {
							  this.fireEvent('importContactsFailed');
							  this.user.removeEvent('addContactsToAccountSucceeded', successHandler);
							  this.user.removeEvent('addContactsToAccountFailed', failureHandler);
						  }).bind(this);
						  this.user.addEvent('addContactsToAccountFailed', failureHandler);

						  this.user.addContactsToAccount(resp.data.contacts);
					  }
					  else {
						  this.user.addContactsLocally(resp.data.contacts);
						  this.fireEvent('importContactsSucceeded', {
							  nContacts: resp.data.contacts.length,
							  rawData: resp.data.contacts
						  });
					  }
				  }
				  else if(resp.errorMessage=="LOGIN_FAILED :: Invalid credentials"){
					  widget.popModalWorkingSheet();
					  widget.pushModalErrorSheet(langTextArray['msg_invalid_userpass']);
				  }
				  else{
					  logError("import contacts",JSON.encode(resp));
					  this.fireEvent('importContactsFailed');
				  }
			  }).bind(this)
		  });
		  request.send();
		  this.fireEvent('importContactsRequested');
	  },
	  
	  updatePreview: function(index) {
		  if (this.shareables[index].icon !== "") {
			  $("icon").set("html", "<img src=\"" + this.shareables[index].icon + "\" width=\"100\" />");
		  }
		  if (this.shareables[index].url !== "") {
			  $("title").set("html", "<a href=\"" + this.shareables[index].url + "\" target=\"_blank\">"
							 + this.shareables[index].title + "</a>");
		  } else {
			  $("title").set("html", this.shareables[index].title);
		  }
		  $("summary").set("html", this.shareables[index].summary);
		  $("content").set("html", this.shareables[index].content);
	  },
	  
	  saveToShareBox: function(tags, objects) {
		  
		  if(tags==langTextArray['msg_seprated_comma']){
			  tags="";
		  }
		  var destination = [{
			  type: 'sharethis.com',
			  address: (glo_destinationAddress ? glo_destinationAddress : '')
		  }];
		  if (typeof tags == 'string') {
			  tags = tags.replace(/\s*,\s*/g, ',').split(',').join(',');
		  }
		  else if (typeof tags == 'object' && 'length' in tags) {
			  tags = tags.join(',');
		  }
		  var tmpTitle=glo_title;
		  try{tmpTitle=decodeURIComponent(glo_title);}catch(err){}
		  if (!objects || !objects.length || objects.length == 0) {
			  objects = [{
				  'type': 'default',
				  url: getSharURL(),
				  title: tmpTitle,
				  thumbnail: glo_thumb,
				  embed: encodeURIComponent(Url.decode(glo_content)),
				  description: (glo_description ? glo_description : ''),
				  tags: (tags ? tags : '')
			  }];
		  }
		  var sender = (glo_userEmail && glo_userEmail.length ? encodeURIComponent(glo_userEmail) : '');
		  var request = new Request({
			  method: "post",
			  url: "/api/createDestination_ws.php",
			  data: {
				  publisher: glo_publisher,
				  objects: encodeURIComponent(JSON.encode(objects)),
				  destinations: encodeURIComponent(JSON.encode(destination)),
				  sessionID: glo_sessionID,
				  destinationType: 'Social',
				  'return': 'JSON',
				  sender: sender
			  },
			  onFailure: function(){logError("save to sharebox","Ajax Failure");},
			  onSuccess: (function(responseText, responseXML) {
				  try{var response = JSON.decode(responseText);}
				  catch(err){logError("save to sharebox",responseText);}
				  if (response.status) {
					  switch (response.status) {
					  case 'SUCCESS':
						  widget.fireEvent('saveToShareBoxSucceeded');
						  break;
					  case 'FAILURE':
						  widget.fireEvent('saveToShareBoxFailed');
						  logError("save to sharebox",response);
						  break;
					  default:
						  break;
					  }
				  }
				  else {
					  logError("save to sharebox",JSON.encode(response));
					  widget.fireEvent('saveToShareBoxFailed');
				  }
			  }).bind(this)
		  });
		  request.send();
	  },
	  
	  extractDomainFromURL: function(url, keepWWW /* = false */) {
		  var domain = url.replace(/(\w+):\/\/([^\/:]+)(:\d*)?([^# ]*)/, '$2');
		  if (!keepWWW && domain.toLowerCase().indexOf('www.') == 0) {
			  domain = domain.substring(4);
		  }
		  return domain;
	  },
	  
	  setCookie: function(name, value) {
		  var current_date = new Date;
		  var exp_y = current_date.getFullYear();
		  var exp_m = current_date.getMonth() + 2;
		  var exp_d = current_date.getDate();
		  var cookie_string = name + "=" + escape(value);
		  if (exp_y) {
			  var expires = new Date (exp_y,exp_m,exp_d);
			  cookie_string += "; expires=" + expires.toGMTString();
		  }
		  cookie_string += "; domain=" + escape (".sharethis.com")+";path=/";
		  document.cookie = cookie_string;
	  },
	  getCookie: function(cookie_name) {
		  var results = document.cookie.match('(^|;) ?' + cookie_name + '=([^;]*)(;|$)');
		  if (results) {
			  return (unescape(results[2]));
		  } else {
			  return false;
		  }
	  },
	  
	  deleteCookie: function(name) {
		  var path="/";
		  var domain=".sharethis.com";
		  document.cookie = name + "=" +( ( path ) ? ";path=" + path : "") 
			  + ( ( domain ) ? ";domain=" + domain : "" ) +";expires=Thu, 01-Jan-1970 00:00:01 GMT";
	  },
	  
	  validationFailed: function(message) {
		  widget.pushModalErrorSheet(message.replace(/\n/g, '<br/>'));
	  },
	  
	  truncateText: function(text, maxWidth, charWidth) {
		  var charWidth = charWidth || 8;
		  var ellipsisWidth = charWidth * 2.5; // gnarly, dude. gnarly.
		  var pixelsOver = ((text.length * charWidth) - (maxWidth - ellipsisWidth));
		  if (pixelsOver > 0) {
			  return text.substring(0, text.length - Math.ceil(pixelsOver / charWidth)) + '&hellip;';
		  }
		  return text;
	  },

	  /**
	   * Call to invoke f after delay, and also once the event handling stack has collapsed back to the
	   * main event loop. If deferWhile is called again with the same tag argument, before delay 
	   * has passed, the timer is reset.
	   * 
	   * Most useful to defer firing a single event once a cluster of other things has happened.
	   * 
	   * @param string tag A unique identifier for this timer/cluster of calls.
	   * @param function f The function to invoke.
	   * @param [delay] The delay to put on the timer. 10 msecs if not passed.
	   */
	  _deferTimers: {},
	  deferWhile: function(tag, f, delay) {
		  if (this._deferTimers[tag] >= 0) {
			  clearTimeout(this._deferTimers[tag]);
		  }
		  this._deferTimers[tag] = setTimeout((function() {
			  this._deferTimers[tag] = -1;
			  f();
		  }).bind(this), (delay || 10));

	  },
	  
	  _frozenFocusHandler: function(event) {
		  if (event.target.hasClass('frozen')) {
			  event.target.blur();
		  }
	  },
	  freezeTextInput: function(inputElement) {
		  inputElement.disabled = true;
		  inputElement.addClass('frozen');
		  inputElement.addEvent('focus', this._frozenFocusHandler);
		  inputElement.blur();
	  },
	  unfreezeTextInput: function(inputElement) {
		  inputElement.disabled = false;
		  inputElement.removeClass('frozen');
		  inputElement.removeEvent('focus', this._frozenFocusHandler);
	  },
	  
	  _profilers: [],
	  pushProfiler: function(message) {
		  this._profilers.push({ start: $time(), message: message });
	  },
	  popProfiler: function() {
		  if (Browser.Engine.trident) {
			  var p = this._profilers.pop();
			  var s = ''; for(var i = 0; i < this._profilers.length; i++) s+= '   ';
			  alert(s + p.message + ' [' + ($time() - p.start) + ']');
		  }
		  else {
			  var p = this._profilers.pop();
			  var s = ''; for(var i = 0; i < this._profilers.length; i++) s+= '   ';
		  }
	  }
	  
	});


//widget.js

if (!window.console || !console.firebug) {
	var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml", "group", "groupEnd",
				 "time", "timeEnd", "count", "trace", "profile", "profileEnd"];
	window.console = {};
	for (var i = 0; i < names.length; ++i) window.console[names[i]] = function() {};
}


var glo_tabs='web,post,email';
var glo_send_services="email,myspace,aim,sms";
var glo_tabArray=[];
glo_tabArray=glo_tabs.split(",");
var glo_charset='utf-8';
var glo_services="";
var glo_exclusive_services=false;
var glo_default_services='myspace,digg,sms,windows_live,delicious,stumbleupon,reddit,google_bmarks,linkedin,bebo,ybuzz,blogger,yahoo_bmarks,mixx,technorati,friendfeed,propeller,wordpress,newsvine,xanga,blinklist,twine,twackle,diigo,fark,faves,mister_wong,current,livejournal,kirtsy,slashdot,oknotizie,care2,aim,meneame,simpy,blogmarks,n4g,bus_exchange,funp,sphinn,fresqui,dealsplus,typepad,yigg';
var glo_top_services = 'email,facebook,twitter,sharebox';
var glo_send_servicesArray = [];
var glo_default_swArray=[];
glo_default_swArray = glo_default_services.split(',');
var glo_style='default';
var glo_publisher="";
var glo_headerbg='#999999';  //header bg
var glo_headerTitle = '';
var glo_headerfg = '#ffffff';
var glo_inactivebg='#D2D2D2'; //inactive tab bg
var glo_inactivefg='#424242';  //inactive text
var glo_linkfg='#222'; //link color
var glo_embeds=false;
var glo_popup=false;
var glo_offsetLeft=0;
var glo_offsetTop=0;
var glo_isLoggedIn=false;
var glo_authToken="";
var glo_userName="";
var glo_userEmail="";
var glo_facebook="";
var glo_aim="";
var glo_greyBoxNum=0;
var glo_contProto=[];
var glo_contName=[];
var glo_contAdd=[];
var glo_contRes=[];
var glo_contResNum=0;
var glo_swArray=[];
var glo_swArray_more=[];
var glo_msgArray=[];
var	glo_hostname="";
var	glo_location="";
var	glo_url="";
var	glo_title="";
var glo_type="default";
var glo_content="";
var glo_summary="";
var glo_icon="";
var glo_category="";
var glo_updated="";
var glo_published="";
var glo_author="";
var glo_totalRegSW=0;
var glo_tpDraft=1;
var glo_bloggerDraft=1;
var glo_guid="";
var glo_guid_index=null;
var glo_title_array=[];
var glo_summary_array=[];
var glo_content_array=[];
var glo_url_array=[];
var glo_icon_array=[];
var glo_category_array=[];
var glo_updated_array=[];
var glo_published_array=[];
var glo_author_array=[];
var glo_type_array=[];
var glo_tags_array=[];
var glo_thumb_array=[];
var glo_description_array=[];
var glo_last_search="";
var glo_thumb="";
var glo_tags="";
var glo_description="";
var glo_num_diggs=0;
var glo_digg_comments=0;
var glo_pageOptions=false;
var glo_swDiv="";
var glo_oldQS="";
var glo_destinations="";
var glo_page_url="";
var glo_page_title="";
var glo_jsonStr="";
var glo_jsonArray=[];
var glo_destinationAddress="";
var glo_destinationType="Social";
var glo_options_popup=false;
var displayNum=20;
var glo_last_url="";
var glo_last_url2="";
var glo_initRun=false;
var domReady=false;
var bufferArgs=[];
var bufferValue=[];
var bufferRunArgs=[];
var glo_hash_flag = false;
var glo_sessionID=null;
var glo_fpc=null;
var glo_thumbImageTag="";
var glo_browser="";
var glo_toolbar=false;
var glo_tracking="";
var glo_googleProfileId="";
var googlePubTracker="";
var glo_omnitureURL="";
var glo_sharURL="";
var import_cookie="";
var signin_cookie="";
var glo_thirdparty_flag = "";
var glo_thirdparty_origin = "";
var glo_thirdparty_signin = "";
var glo_thirdparty_import_contacts = "";
var glo_thirdparty_email = "";
var glo_thirdparty_oauth_token = "";
var glo_thirdparty_existing_email = "";
var glo_thirdparty_service_feed = "";
var import_cookie_tid;
var signin_cookie_tid;
var import_cookie_cycles = 0;
var signin_cookie_cycles = 0;
var glo_ads=false;
var glo_adtag_header="";
var glo_adtag_footer="";
var glo_page="";
var glo_post_page=[];
var glo_credentials = [];
var glo_pUrl="";
var glo_widget_info = [];

function css_browser_selector(u){var ua = u.toLowerCase(),is=function(t){return ua.indexOf(t)>-1;},g='gecko',w='webkit',s='safari',h=document.getElementsByTagName('html')[0],b=[(!(/opera|webtv/i.test(ua))&&/msie\s(\d)/.test(ua))?('ie ie'+RegExp.$1):is('firefox/2')?g+' ff2':is('firefox/3')?g+' ff3':is('gecko/')?g:/opera(\s|\/)(\d+)/.test(ua)?'opera opera'+RegExp.$2:is('konqueror')?'konqueror':is('chrome')?w+' '+s+' chrome':is('applewebkit/')?w+' '+s+(/version\/(\d+)/.test(ua)?' '+s+RegExp.$1:''):is('mozilla/')?g:'',is('j2me')?'mobile':is('iphone')?'iphone':is('ipod')?'ipod':is('mac')?'mac':is('darwin')?'mac':is('webtv')?'webtv':is('win')?'win':is('freebsd')?'freebsd':(is('x11')||is('linux'))?'linux':'','js']; c = b.join(' '); h.className += ' '+c; return c;}
var glo_browser=css_browser_selector(navigator.userAgent);

var tstArray=[]; //test array from frag object;

//test frag object
function fragObj(inFrag,query){
	this.frag=inFrag;
	this.qs=query;
}

function readyTest(){
	for(var i=0;i<tstArray.length;i++){
		var tmp=tstArray[i].frag+" = \n"+tstArray[i].qs;
		alert(tmp);
	}		
}

function setGlobals(strArg,value) {
	var answer="";
	if(value===0){answer="No";}
	if(value===1){answer="Yes";}
	try{value=decodeURIComponent(value);}catch(err){}
	try{value=decodeURIComponent(value);}catch(err){}
	//console.log(strArg+":"+value);
	switch(strArg) {
	case "tabs":
		glo_tabs=value;
		glo_tabArray = value.split(',');
		tab_servicesChanged();
		break;    
	case "charset":
		glo_charset=value;
		break;
	case "services":
		glo_servces=value;
		glo_swArray = value.split(',');
		break;  
	case "exclusive_services":
		glo_exclusive_services=value;
		break;  
	case "style":
		glo_style=value;
		break;  
	case "publisher":
		glo_publisher=value;
		break;  
	case "headerbg":
		glo_headerbg=value;
		$$('.header-color').setStyle('background-color',value);
		break;  
	case "inactivebg":
		glo_inactivebg=value;
		$$('.nav-inactive-color').setStyle('background-color',value);
		break; 
	case "inactivefg":
		glo_inactivefg=value;
		$$('.nav-inactive-color').setStyle('color',value);
		break;  
	case "linkfg":
		glo_linkfg=value;
		setTimeout("widget.fireEvent('linkForegroundColorChanged', glo_linkfg)",10);
		break;  
	case "embeds":
		glo_embeds=value;
		break;  
	case "popup":
		glo_popup=value;
		break;  
	case "offsetLeft":
		glo_offsetLeft=value;
		break;  
	case "offsetTop":
		glo_offsetTop=value;
		break;
	case "auth":
		glo_authToken=value;
		break;
	case "userEmail":
		glo_userEmail=value;
		$('textAuthInfoEmail').set('html',value);
		break;
	case "userName":
		glo_userName=value;
		break;
	case "facebook":
		glo_facebook=value;
		break;
	case "aim":
		glo_aim=value;
		break;
	case "contProto":
		glo_contProto.push(value);
		break;
	case "contName":
		glo_contName.push(value);
		break;	
	case "contAdd":
		glo_contAdd.push(value);
		break;
	case "contName":
		glo_contRes.push(value);
		break;	
	case "contResNum":
		glo_contResNum=value;
		break;
	case "loggedIn":
		glo_isLoggedIn=value;
		if(glo_isLoggedIn===true){
			$('boxToYourAddr').addClass('hidden');
			$$('.guest').addClass('hidden');
		}
		else{
			$('boxToYourAddr').removeClass('hidden');
			$$('.guest').removeClass('hidden');
		}
		break;
	case "hostname":
		glo_hostname=encodeURIComponent(value);
		break;	
	case "location":
		glo_location=encodeURIComponent(value);
		break;
	case "url":
		if(value!==glo_url && value!==undefined){
			if(typeof(value)!=="string"){
				try{glo_url=value.href;}catch(err){}
			}
			else{
				glo_url=value;
			}
			glo_thumbImageTag='http://sharethis.com/share/thumb?url='+glo_url;
			$('previewUrl').set('text', widget.extractDomainFromURL(glo_url));
			getDiggs(glo_url);
			//createSharURL(glo_url, true);
		}
		break;
	case "title":
		glo_title=value;
		if(glo_title=="" || glo_title=="undefined"){
			glo_title=decodeURIComponent(glo_url);
		}
		try{value=decodeURIComponent(value)}catch(err){}
		if(value.length>80){
			value=value.slice(0,77);
			value+="...";
		}
		
		$('previewTitle').set('text',value);
		widget.fireEvent('shareableTitleChanged', glo_title);
		break;	
	case "type":
		glo_type=value;
		break;	
	case "icon":
		glo_icon=value;
		if(value.length>5){
			glo_thumbImageTag=value;
		}
		break;			
	case "category":
		glo_category=value;
		break;
	case "updated":
		glo_updated=value;
		break;
	case "published":
		glo_published=value;
		break;	
	case "author":
		glo_author=value;
		break;
	case "summary":
		glo_summary=value;
		break;	
	case "content":
		glo_content=encodeURIComponent(value);
		break;
	case "glo_tpDraft":
		glo_tpDraft=value;
		break;
	case "glo_bloggerDraft":
		glo_bloggerDraft=value;
		break;		
	case "guid":
		if(glo_guid==="" && value!==null && value!=="null"){
			glo_guid=value;
			getObjects();
		}
		break;
	case "guid_index":
		glo_guid_index=value;
		setValues();

		break;
	case "glo_title_array":
		glo_title_array.push(value);
		break;	
	case "glo_type_array":
		glo_type_array.push(value);
		break;
	case "glo_summary_array":
		glo_summary_array.push(value);
		break;
	case "glo_content_array":
		glo_content_array.push(value);
		break;
	case "glo_url_array":
		glo_url_array.push(value);
		break;
	case "glo_icon_array":
		glo_icon_array.push(value);
		break;
	case "glo_category_array":
		glo_category_array.push(value);
		break;
	case "glo_updated_array":
		glo_updated_array.push(value);
		break;
	case "glo_published_array":
		glo_published_array.push(value);
		break;
	case "glo_author_array":
		glo_author_array.push(value);
		break;
	case "glo_thumb":
		glo_thumb=value;
		break;	
	case "glo_tags":
		glo_tags=value;
		break;	
	case "glo_description":
		glo_description=value;
		glo_description=encodeURIComponent(glo_description);
		widget.fireEvent('shareableDescriptionChanged', glo_description && glo_description != 'undefined' ? glo_description : '');
		break;	
	case "glo_thumb_array":
		glo_thumb_array.push(value);
		break;	
	case "glo_tags_array":
		glo_tags_array.push(value);
		break;	
	case "glo_description_array":
		glo_description_array.push(value);
		break;
	case "pageTitle":
		glo_page_title=value;
		break;
	case "pageURL":
		glo_page_url=value;
		break;
	case "hash_flag":
		if( value == "true" ){
			glo_hash_flag = true;
		} else {
			glo_hash_flag = false;
		}
		break;
	case "sessionID":
		glo_sessionID=value;
		break;
	case "fpc":
		glo_fpc=value;
		break;	
	case 'headerTitle':
		glo_headerTitle = value;
		widget.fireEvent('headerTitleChanged', value);
		break;
	case 'headerfg':
		glo_headerfg = value;
		widget.fireEvent('headerFGColorChanged', value);
		break;
	case 'post_services':
		setGlobals("services",value);
		break;			
	case 'send_services':
		glo_send_servicesArray=value;
		send_servicesChanged(value);
		break;
	case 'glo_toolbar':
		glo_toolbar=value;
		$("close_button").set("style","display:inline");
		break;
	case 'tracking':
		glo_tracking = value;
		break;
	case "googleProfileId":
		glo_googleProfileId=value;
		break;
	case "omnitureURL":
		glo_omnitureURL=value;
		break;
	case "ads":
		if (value == "true") {
			glo_ads = true;
		} else {
			glo_ads = false;
		}
		break;
	case "adtag_header":
		glo_adtag_header=value;
		if (value != "") {
			$('header_ad').setStyle('height', '40px');
			$('header_ad').removeClass("hidden");
		}
		break;
	case "adtag_footer":
		glo_adtag_footer=value;
		if (value != "") {
			$('footer_ad').removeClass("hidden");
		}
		break;
	case "page":
		glo_page=value;
		if (glo_page == "send" || glo_page.match('post|')) {
			if (glo_page == "post|twitter") {
				createSharURL(glo_url, true);
				//createSharURL(glo_url, false);
				widget.fireEvent('twitterClicked', getSharURL());
			}
			widget.showPage(glo_page);
		} else if (glo_page == "import" ) {
			widget.showPage('import');
			event.stop();
		} else if (glo_page == "account_linking" ) {
			widget.showPage('account_linking');
			event.stop();
		} else {
			widget.showPage('home');
		}
		break;
	case "pUrl":
		glo_pUrl=value;
		//glo_url=value;
		break;
	default: 
		// do nothing
		break;
	}
}


function addToOptions(a){
	var temp=[];
	temp=a.split("=");	
	temp[0]=decodeURIComponent(temp[0]);
	temp[1]=decodeURIComponent(temp[1]);
	try{
		temp[0]=decodeURIComponent(temp[0]);
		temp[1]=decodeURIComponent(temp[1]);
	}
	catch(err){
		//noop
	}
	tstArray.push(new fragObj(temp[0],temp[1]));
	setGlobals(temp[0],temp[1]);
}

function addToOptionsBuffer(a){
	var temp=[];
	temp=a.split("=");	
	temp[0]=decodeURIComponent(temp[0]);
	temp[1]=decodeURIComponent(temp[1]);
	try{
		temp[0]=decodeURIComponent(temp[0]);
		temp[1]=decodeURIComponent(temp[1]);
	}
	catch(err){
		//noop
	}
	tstArray.push(new fragObj(temp[0],temp[1]));
	bufferArgs.push(temp[0]);
	bufferValue.push(temp[1]);
}

function checkBufferArg(testStr){
	var returnVal=false;
	for(var i=0;i<bufferRunArgs.length;i++){
		if(bufferRunArgs[i]==testStr){
			returnVal=true;
		}
	}
	return returnVal;
}

function processBuffer(){
	bufferArgs.reverse();
	bufferValue.reverse();		
	for(var i=0;i<bufferArgs.length;i++){
		if( checkBufferArg(bufferArgs[i])===false ){
			bufferRunArgs.push(bufferArgs[i]);
			setGlobals(bufferArgs[i],bufferValue[i]);
		}
	}
	createSwList();
	switch (glo_tracking) {
	case "google":
	case "omniture":
		var request=new Request({
			method: "post",
			url: "/api/getPublisherDomains_ws.php",
			data: "publisher="+glo_publisher+"&return=json",
			onFailure: function(){logError("glo_tracking","Ajax Failure");},
			onSuccess: function(responseText,responseXML){
				try{var response = JSON.decode(responseText);}
				catch(err){logError("glo_tracking",responseText);}
				if (response.status === "SUCCESS") {
					var domainString = document.referrer;
					var domainPattern = new RegExp("^(http|https)://([^/]*)");
					var domainParsed = domainString.match(domainPattern);
					var domain = domainParsed[2];
					for (var i = 0; i < response.data.domain.length; i++) {
						if (domain == response.data.domain[i].name) {
							if (response.data.domain[i].google_analytics != "" && glo_tracking == "google") {
								setGlobals("googleProfileId", response.data.domain[i].google_analytics);
								var gaScript = new Element('script')
									.set('src', 'http://www.google-analytics.com/ga.js')
									.set('type', 'text/javascript')
									.addEvent('load', function(){
										googlePubTracker = _gat._getTracker(glo_googleProfileId);
									})
									.inject($('container'), 'after');
							}
							if (response.data.domain[i].omniture != "" && glo_tracking == "omniture") {
								setGlobals("omnitureURL", response.data.domain[i].omniture);
							}
						}
					}
				}else{
					logError("glo_tracking",JSON.encode(response));
				}
			}
		});
		request.send();
		break;
	}
	if (glo_ads==true) {
		var request=new Request({
			method: "post",
			url: "/api/getPublisherDomains_ws.php",
			data: "publisher="+glo_publisher+"&return=json",
			onFailure: function(){logError("add Request","Ajax Failure");},
			onSuccess: function(responseText,responseXML){
				try{var response = JSON.decode(responseText);}
				catch(err){logError("add request",responseText);}
				if (response.status === "SUCCESS") {
					var domainString = document.referrer;
					var domainPattern = new RegExp("^(http|https)://([^/]*)");
					var domainParsed = domainString.match(domainPattern);
					var domain = domainParsed[2];
					for (var i = 0; i < response.data.domain.length; i++) {
						if (domain == response.data.domain[i].name) {
							setGlobals("adtag_header", response.data.domain[i].adtag_widgetheader);
							setGlobals("adtag_footer", response.data.domain[i].adtag_widgetfooter);
						}
					}
				}else{logError("add Request","Ajax Failure");}
			}
		});
		request.send();
	}
}

function send_servicesChanged(services) {
	var svc_arr=[];
	svc_arr=services.split(",");
	var email=false;
	var aim=false;
	var sms=false;
	for(var i=0;i<svc_arr.length;i++)
	{
		if(svc_arr[i]=="email"){
			email=true;
		}
		else if(svc_arr[i]=="aim"){
			aim=true;
		}else if(svc_arr[i]=="sms"){
			sms=true;
		}
	}
	
	if( email == false || aim == false || sms == false ) {
		var newServices = [];
		$each(glo_default_swArray, function(name, i) {
			if( name == 'aim' && aim == false ) {
			} else if( name == 'sms' && sms == false ) {
			} else {
				newServices.push(name);
			}
		});
		glo_default_swArray = newServices;
		
		if( email == false ) {
			glo_top_services = 'facebook,twitter,sharebox';
		}

		createSwList();

	}

	return "services_changed";
}

function tab_servicesChanged() {
	var email = false;
	var sms = false;
	var aim = false;

	for(var i=0;i<glo_tabArray.length;i++)
	{
		if(glo_tabArray[i]=="email"){
			email=true;
		}
	}
	
	if( email == false ) {
		var newServices = [];
		$each(glo_default_swArray, function(name, i) {
			if( name == 'aim' && aim == false ) {
			} else if( name == 'sms' && sms == false ) {
			} else {
				newServices.push(name);
			}
		});
		glo_default_swArray = newServices;
		
		glo_top_services = 'facebook,twitter,sharebox';

		createSwList();

	}

	return "services_changed";
}

// frag pump start	
function FragmentPump(){
	//Extends: Events,
	var fragTimer="";
	this.initialize= function() {
		FragmentPump.fragTimer=setInterval("fragmentPump.checkFragment()",5);
	};
	this.startint=function(){
		setInterval(this.checkFragment.bind(this), 250);
	};
	this.checkFragment=function() {
		var hash = document.location.hash.substring(1);
		if (hash.length > 0 && hash!==glo_oldQS) {
			var args = hash.split("/");
			glo_oldQS=hash;
			var cmd = args.shift();
			cmd="fragmentPump."+cmd;
			//			console.log(args);
			var temp="";
			for(var i=0;i<args.length;i++){
				temp=temp+'"'+args[i]+'"';
				if(i<(args.length-1)){
					temp=temp+",";
				}					
			}
			var evStr=cmd+"("+temp+")";
			//				console.log(evStr);
			var tempFucn = new Function(evStr);
			tempFucn();
		}
	};
	this.init=function(){
		if(glo_initRun===false){
			glo_initRun=true;
			for(var i=0;i<arguments.length;i++) {
				var num=i+1;
				//console.log(arguments[i]);
				if(arguments[i]!="" && arguments[i]!=" "){addToOptionsBuffer(arguments[i]);}
			}
			glo_pageOptions=true;
			if(domReady===true){
				processBuffer();
			}
			glo_initRun=true;
		}
	};
	this.test=function() {
		readyTest();
	};
	this.data=function() {
		for (var i=0; i < arguments.length; i++) {
			addToOptions2(arguments[i]);
		}
	};
	this.show=function() {
		gaLog("Widget", "widget_open"); 
		if(glo_initRun==false){
			return false;
		}
		for(var i=0; i < arguments.length; i++) {
			addToOptions(arguments[i]);
		}
	};
	this.popup=function(){
		//console.log("popup");
		clearInterval(fragmentPump.fragTimer);
		clearInterval(FragmentPump.fragTimer);
		//fragmentPump.startint();
		glo_options_popup=true;  
		displayNum=24;
		for(var i=0;i<arguments.length;i++) {
			var num=i+1;
			//console.log(arguments[i]);
			addToOptionsBuffer(arguments[i]);
		}
		if(domReady===true){
			processBuffer();
		}
		glo_initRun=true;
	};
	this.widget=function() {
		if (arguments.length) {
			var kvPairs = arguments[0].split('=');
			for (var i = 0; i < kvPairs.length; i += 2) {
				switch (kvPairs[i]) {
				case 'screen':
					widget.showPage(kvPairs[i + 1]);
					$('header_ad').empty();
					$('footer_ad_body').empty();
					break;
				}
			}
		}
	};
	this.initialize();
};
var fragmentPump = new FragmentPump();

function addToOptions2(a){
	var temp=[];
	temp=a.split("=");	
	temp[0]=decodeURIComponent(temp[0]);
	try{
		temp[0]=decodeURIComponent(temp[0]);
		temp[1]=decodeURIComponent(temp[1]);
	}
	catch(err){
		//noop
	}
	if(temp[0]=="pageHost"){
		setGlobals("hostname",temp[1]);
	}
	else if(temp[0]=="pagePath"){
		setGlobals("location",temp[1]);
	}
	tstArray.push(new fragObj(temp[0],temp[1]));
	if(temp[1]=="done"){
		if(glo_initRun===false){document.location.hash=glo_initFrag;}
		//clearInterval(fragmentPump.fragTimer);
		//clearInterval(FragmentPump.fragTimer); 
		//fragmentPump.startint();         
		glo_jsonStr=glo_jsonArray.join('');
		glo_jsonArray=[];
		glo_title_array=[];
		glo_type_array=[];
		glo_summary_array=[];
		glo_content_array=[];
		glo_url_array=[];
		glo_icon_array=[];
		glo_category_array=[];
		glo_updated_array=[];
		glo_published_array=[];
		glo_author_array=[];
		glo_thumb_array=[];
		
		processFrag();
		
	}
	else if(temp[0]=="jsonData"){
		glo_jsonArray.push(temp[1]);
	}
}

function forgetUser(){
	setGlobals("loggedIn",false);
	$('boxToYourAddr').removeClass('hidden');
	setGlobals("auth","");
	setGlobals("userName","");
	setGlobals("userEmail","");
	setGlobals("facebook","");
	setGlobals("aim","");
	setGlobals("contProto",[]);
	setGlobals("contName",[]);
	setGlobals("contAdd",[]);
	setGlobals("contResNum",0);
	emptyInputs();
	widget.deleteCookie("ShareUT");
	glo_contProto=[];
	glo_contName=[];
	glo_contAdd=[];
	glo_contRes=[];
	glo_contResNum=0;
	//delete auth cookie
	//setGlobals to default	
}
function emptyInputs(){
	$$('input[type=text]').set('value','');
	$$('input[type=password]').set('value','');
	$$('input[type=password]').set('value','');
	//blank out tp select box
	$$('textarea').set('value','');
	$('post_select_container').set('html',"");
}

//Google Analytics tracking wrapper
function gaLog(category, action, label, value) {
	if( typeof(pageTracker) != "undefined" ) {
		pageTracker._trackEvent(category, action, label, value);
	}
}

//Remove hashtracking from url
function cleanURL(url) {
	var url_hash = window.location.hash;
	var hash_regexp = new RegExp("STS=", "i");
	var match_results = url_hash.match(hash_regexp);  // elements 1,3
	if( match_results != null ) {
		var url_arr = url.split('#');
		return url_arr[0];
	} else {
		return url;
	}
}

//creates a social web log event
function logSW(network) {
	var source = "";
	if (glo_toolbar != false) {
		source = "toolbar";
	} else if (glo_page != "home" && glo_page != "") {
		source = "chicklet";
	} else {
		source = "button";
	}
	var url2 = "http://l.sharethis.com/log?event=click"
		+ "&source=" + source
		+ "&publisher=" + encodeURIComponent(glo_publisher)
		+ "&hostname=" + encodeURIComponent(glo_hostname)
		+ "&location=" + encodeURIComponent(glo_location)
		+ "&destinations=" + network
		+ "&ts" + (new Date()).getTime()
		+ "&title=" + glo_title
		+ "&url=" + cleanURL(glo_url)
		+ "&sessionID="+glo_sessionID
		+ "&fpc="+glo_fpc;
		//+ "&sharURL="+glo_sharURL;
	
	var logger2 = new Image(1,1);
	logger2.src = url2;
	logger2.onload = function(){return;};
	
	if (glo_tracking == "google" && glo_googleProfileId != "") {
		url = "/ShareThis/social/" + network + "/" + glo_title;
		if (googlePubTracker != "") {
			googlePubTracker._trackPageview(url);
		}
	}
	
	if (glo_tracking == "omniture" && glo_omnitureURL != "") {
		url = glo_omnitureURL + "?sms_st=social&sms_ss=" + network + "&sms_sa=" + encodeURIComponent(glo_title);
		tracker = new Element("iframe")
			.set('src', url)
			.set('style', 'position:absolute;top:0;left:0;width:0;height:0;visibility:hidden')
			.inject($('container'), 'after');
	}
	
	glo_destinationType="Social";
	sendDestination(network); //for doing createDestination
}

function postYahooUpdate( title, link ) {
	if( widget.user.thirdparty_token != '' && widget.user.yahoo_feed != '') {
		var request = new Request({
			method: "post",
			url: "/api/postYahooUpdate.php",
			asynch: true,
			data: {
				yahoo_token: widget.user.thirdparty_token,
				title: title,
				link: link,
				description: langTextArray['msg_shared_using'],
				'return': 'JSON'
			},
			onFailure: function(){logError("post yahoo update","Ajax Failure");},
			onSuccess: (function(responseText, responseXML) {
				try{var response = JSON.decode(responseText);}
				catch(err){logError("post yahoo update",responseText);}
				if (response.status) {
					switch (response.status) {
					case 'SUCCESS':
						break;
					case 'FAILURE':
						logError("post yahoo update",response);
					default:
						break;
					}
				}
				else {
					logError("poost yahoo update",JSON.encode(response));
				}
			}).bind(this)
		});
		request.send();
	}
}

function logEvent(destination1,eventType) {
	var source = "";
	if (glo_toolbar != false) {
		source = "toolbar";
	} else if (glo_page != "home" && glo_page != "") {
		source = "chicklet";
	} else {
		source = "button";
	}
	var url2 = "http://l.sharethis.com/log?event="+eventType;
	url2+= "&source=" + source;
	url2+= "&publisher="+ encodeURIComponent(glo_publisher);
	url2+= "&hostname="+ encodeURIComponent(glo_hostname);
	url2+= "&location="+ encodeURIComponent(glo_location);
	url2+= "&destinations="+destination1;
	url2+= "&ts" + (new Date()).getTime();
	url2+= "&title="+encodeURIComponent(glo_title);
	url2+= "&url="+encodeURIComponent(cleanURL(glo_url));
	url2+= "&sessionID="+glo_sessionID;
	url2+= "&fpc="+glo_fpc;
	
	var logger2 = new Image(1,1);
	logger2.src = url2;
	logger2.onload = function(){return;};
	
	if (glo_tracking == "google" && glo_googleProfileId != "") {
		destinations = destination1.split(",");
		for (var i=0; i<destinations.length; i++) {
			url = "/ShareThis/" + eventType + "/" + destinations[i] + "/" + glo_title;
			if (googlePubTracker != "") {
				googlePubTracker._trackPageview(url);
			}
		}
	}
	
	if (glo_tracking == "omniture" && glo_omnitureURL != "") {
		destinations = destination1.split(",");
		for (var i=0; i<destinations.length; i++) {
			url = glo_omnitureURL + "?sms_st=" + eventType + "&sms_ss=" + destinations[i] + "&sms_sa=" + encodeURIComponent(glo_title);
			tracker = new Element("iframe")
				.set('src', url)
				.set('style', 'position:absolute;top:0;left:0;width:0;height:0;visibility:hidden')
				.inject($('container'), 'after');
		}
	}
	
	if(eventType=="post"){
		glo_destinationType="Post";
		sendDestination(destination1); //for doing createDestination
	}
}

function genTopServices() {

	var top_elm = new Element('div', { id: 'top_services' } );

	var serviceNames = glo_top_services.split(",");
    $each(serviceNames, function(provider) {
		var link = widget.getServiceLink(provider);
		link.addClass('top_service');
		link.inject(top_elm);
    });
	
	top_elm.replaces( $('top_services') );
}

function createSwList() {

	genTopServices();

	var carouselItems = [];
	var defaultServicesCSV = glo_default_services;
	var defaultServices = glo_default_swArray;
	
	var userPrefs = widget.user.getShareServiceHistory().map(function(item) { return item.service; });
	var publisherPrefs = glo_swArray;

	if (!userPrefs.length && !publisherPrefs.length) {
		setGlobals("services", defaultServicesCSV);
	}
	
	// switch for using user prefs (disabled in initial v3 release):
	var respectUserPrefs = true;
	
	if( glo_exclusive_services ) {
		$each(defaultServices, function(name, i) {
			var link = widget.getServiceLink(defaultServices[i]);
			var publisherIndex = publisherPrefs.indexOf(defaultServices[i]);
			if(publisherIndex > -1) {
				var userIndex = userPrefs.indexOf(defaultServices[i]);
				carouselItems.push({
					getContent: function() { return link; },
					hasPublisherPref: (publisherIndex > -1),
					hasUserPref: (respectUserPrefs ? (userIndex > -1) : false),
					stIndex: i,
					publisherIndex: publisherIndex,
					userIndex: userIndex,
					tag: defaultServices[i]
				});
			}
		});
		widget.carousel.createPaginator();
	} else {
		$each(defaultServices, function(name, i) {
			var link = widget.getServiceLink(defaultServices[i]);
			var publisherIndex = publisherPrefs.indexOf(defaultServices[i]);
			var userIndex = userPrefs.indexOf(defaultServices[i]);
			carouselItems.push({
				getContent: function() { return link; },
				hasPublisherPref: (publisherIndex > -1),
				hasUserPref: (respectUserPrefs ? (userIndex > -1) : false),
				stIndex: i,
				publisherIndex: publisherIndex,
				userIndex: userIndex,
				tag: defaultServices[i]
			});
		});
	}
	widget.carousel.setDataSource(carouselItems);
	if(glo_pageOptions==true){
		setTimeout("widget.fireEvent('linkForegroundColorChanged', glo_linkfg)",10);
	}
}


function popupOpen(blah){
	var source="";
	var anchors = blah.getElementsByTagName("a");
	for (var i=0; i<anchors.length; i++) {
		var anchor = anchors[i];
		source=anchor.href;
	}
	window.open(source,"stpopup","width=970,height=700,location=1,toolbar=1,scrollbars=1,menubar=1,resizable=1"); 
	return false;
}
function popupOpen2(blah){
	var source=blah.href;
	try{
		window.open(source,"stpopup","width=970,height=700,location=1,toolbar=1,scrollbars=1,menubar=1,resizable=1");
	}catch(err){}
	
	return false;
}

function getObjects(){
	var request=new Request({
		method: "post",
		url: "/api/getCache_ws.php",
		data: "key="+glo_guid+"&return=json",
		onFailure: function(){logError("getObjects","Ajax Failure");},
		onSuccess:getObjects_onSuccess
	});
	request.send();
}

function getObjects_onSuccess(responseText,responseXML){
	try{var response = JSON.decode(responseText);}
	catch(err){logError("getObjects",responseText);}

	if (response.status == "SUCCESS") {
		var newJsonData=Url.decode(response.data);
		var newResp=JSON.decode(newJsonData);

		for(i=0;i<newResp.length;i++){
			setGlobals("glo_title_array",newResp[i].title);
			setGlobals("glo_type_array",newResp[i].type);
			setGlobals("glo_summary_array",newResp[i].summary);
			setGlobals("glo_content_array",newResp[i].content);
			setGlobals("glo_url_array",newResp[i].url);
			setGlobals("glo_icon_array",newResp[i].icon);
			setGlobals("glo_category_array",newResp[i].category);
			setGlobals("glo_updated_array",newResp[i].updated);
			setGlobals("glo_published_array",newResp[i].published);
			setGlobals("glo_author_array",newResp[i].author);
			setGlobals("glo_thumb_array",newResp[i].icon);
			if(newResp[i].tags){setGlobals("glo_tags_array",newResp[i].tags);}
			if(newResp[i].description){setGlobals("glo_description_array",newResp[i].description);}
		}
		setValues();
		
		if (glo_page == "post|twitter") {
			createSharURL(glo_url, true);
			//createSharURL(glo_url, false);
			widget.fireEvent('twitterClicked', getSharURL());
		}
		
	}else{logError("getObjects","Ajax Failure");}
}

function processFrag(){
	
	if(glo_browser.test("ff")==false){
		try{glo_jsonStr=decodeURIComponent(glo_jsonStr);}catch(err){}
	}

	var tmp=glo_jsonStr;
	var newResp=[];
	try{newResp=eval(tmp);}catch(err){tmp=decodeURIComponent(tmp);newResp=eval(tmp);}
	//		console.log(newResp);

	for(var i=0;i<newResp.length;i++){
		setGlobals("glo_title_array",newResp[i].title);
		setGlobals("glo_type_array",newResp[i].type);
		setGlobals("glo_summary_array",newResp[i].summary);
		setGlobals("glo_content_array",newResp[i].content);
		setGlobals("glo_url_array",newResp[i].url);
		setGlobals("glo_icon_array",newResp[i].icon);
		setGlobals("glo_category_array",newResp[i].category);
		setGlobals("glo_updated_array",newResp[i].updated);
		setGlobals("glo_published_array",newResp[i].published);
		setGlobals("glo_author_array",newResp[i].author);
		setGlobals("glo_thumb_array",newResp[i].icon);
		if(newResp[i].tags){setGlobals("glo_tags_array",newResp[i].tags);}
		if(newResp[i].description){setGlobals("glo_description_array",newResp[i].description);}
	}
	setValues();
}

var Url = {
	encode : function (string) {
		return escape(this._utf8_encode(string));
	},
	decode : function (string) {
		return this._utf8_decode(unescape(string));
	},
	_utf8_encode : function (string) {
		string = string.replace(/\r\n/g,"\n");
		var utftext = "";
		for (var n = 0; n < string.length; n++) {
			var c = string.charCodeAt(n);
			if (c < 128) {
				utftext += String.fromCharCode(c);
			}
			else if((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			}
			else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}
		}
		return utftext;
	},
	_utf8_decode : function (utftext) {
		var string = "";
		var i = 0;
		var c = c1 = c2 = 0;

		while ( i < utftext.length ) {
			c = utftext.charCodeAt(i);
			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			}
			else if((c > 191) && (c < 224)) {
				c2 = utftext.charCodeAt(i+1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			}
			else {
				c2 = utftext.charCodeAt(i+1);
				c3 = utftext.charCodeAt(i+2);
				string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}
		}
		return string;
	}
}

function setValues(){
	if(glo_guid_index!==null && glo_url_array.length>0){
		setGlobals("url",glo_url_array[glo_guid_index]);
		setGlobals("title",glo_title_array[glo_guid_index]);
		setGlobals("type",glo_type_array[glo_guid_index]);
		setGlobals("summary",glo_summary_array[glo_guid_index]);
		setGlobals("content",glo_content_array[glo_guid_index]);			
		setGlobals("icon",glo_icon_array[glo_guid_index]);
		setGlobals("category",glo_category_array[glo_guid_index]);
		setGlobals("updated",glo_updated_array[glo_guid_index]);
		setGlobals("published",glo_published_array[glo_guid_index]);
		setGlobals("author",glo_author_array[glo_guid_index]);
		setGlobals("glo_tags",glo_tags_array[glo_guid_index]);
		setGlobals("glo_thumb",glo_thumb_array[glo_guid_index]);
		setGlobals("glo_description",glo_description_array[glo_guid_index]);
		createSwList();
		widget.fireEvent('shareableValuesUpdated');
		if (glo_ads == true) {
			var adtag_timestamp = (new Date()).getTime();
			if (glo_adtag_header != "") {
				$("header_ad").set("html", glo_adtag_header.replace(/\[timestamp\]/g, adtag_timestamp));
			}
			if (glo_adtag_footer != "") {
				$("footer_ad_body").set("html", glo_adtag_footer.replace(/\[timestamp\]/g, adtag_timestamp));
			}
		}
	}
}

function searchName(searchStr){
	var results = [];
	var reg = new RegExp("^" + searchStr);
	for(var i = 0; i < widget.user.contacts.length && results.length < widget.maxContactSearchResults; i++) {
		var tmp_name = widget.user.contacts[i].name.toLowerCase();
		if (reg.test(tmp_name)) {
			results.push(i);
		}
	}
	return results;
}

function searchName2(searchStr){
	var results = [];
	var reg = new RegExp("^" + " " + searchStr);
	for(var i = 0; i < widget.user.contacts.length && results.length < widget.maxContactSearchResults; i++){
		var tmp_name = widget.user.contacts[i].name.toLowerCase();
		if(reg.test(tmp_name)){
			results.push(i);
		}
	}
	return results;
}
function searchName3(searchStr){
	var results = [];
	if(searchStr.search(/ /)>=0) {
		var tmp_name1="";
		var tmp_name2="";
		var tmp_name=searchStr.toLowerCase();
		tmp_name=tmp_name.split(" ");
		if(tmp_name.length>1){
			tmp_name1=tmp_name[0];
			tmp_name2=tmp_name[1];
		}
		var reg = new RegExp("^" + tmp_name1);
		var reg2 = new RegExp(" " + tmp_name2, "gi");
		for (var i = 0; i < widget.user.contacts.length && results.length < widget.maxContactSearchResults; i++) {
			tmp_name = widget.user.contacts[i].name.toLowerCase();
			if (reg.test(tmp_name) && reg2.test(tmp_name)) {
				results.push(i);
			}
		}
	}
	return results;
}

// searches for last name
function searchName4(searchStr) {
	var results = [];
	var tmp_name1="";
	var tmp_name2="";
	searchStr = searchStr.toLowerCase();

	for (var i = 0; i < widget.user.contacts.length && results.length < widget.maxContactSearchResults; i++) {	
		tmp_name=widget.user.contacts[i].name.toLowerCase();
		var reg = new RegExp(" " + searchStr, "gi");
		if (reg.test(tmp_name)) {
			results.push(i);
		}
	}
	return results;
}

// do searches for dave doster
function searchName5(searchStr){
	var results = [];
	var tmp_name1 = "";
	var tmp_name2 = "";
	var tmp_name = searchStr.toLowerCase();
	if(tmp_name.length>1){
		tmp_name1=tmp_name[0];
		tmp_name2=tmp_name.substring(1);
	}
	var reg = new RegExp("^" + tmp_name1);
	var reg2 = new RegExp(" " + tmp_name2, "gi");

	for (var i = 0; i < widget.user.contacts.length && results.length < widget.maxContactSearchResults; i++) {
		tmp_name = widget.user.contacts[i].name.toLowerCase();
		if (reg.test(tmp_name) && reg2.test(tmp_name)) {
			results.push(i);
		}
	}
	return results;
}


function searchAdd(searchStr){
	var isNum=false;
	
	if(searchStr>=0){
		var a="\\+?1?"+searchStr+"[0-9]*";
		var reg = new RegExp(a);
		isNum=true;
	}
	else if(searchStr[0]=="@") {
		var reg = new RegExp(searchStr,"gi");
	}
	else{
		var reg = new RegExp("^"+searchStr);

	}
	var i=0;

	var results = [];

	for(var i = 0; i < widget.user.contacts.length && results.length < widget.maxContactSearchResults;i++){
		var tmp_add = widget.user.contacts[i].address.toLowerCase();
		if(reg.test(tmp_add)){
			results.push(i);
		}
	}	
	return results;
}

function txtGreyBox(id,newAdd){
	var type=glo_contProto[id];
	var name=glo_contName[id];
	var disp_address=glo_contAdd[id]; 
	var address=glo_contAdd[id];
	var divid="greyBox_"+id;
	var addId="addBox_"+id;
	var parentID="paren_"+divid;
	var rtnStr="";	
	var dispType="";

	if(type==="EMAIL"){
		type="E-mail";
	}
	if(type==="AIM"){
		type="AIM";
	}
	if(type==="SMS"){
		type="SMS";
	}

	if(glo_greyBoxNum%2===0){
		rtnStr+='<div id="'+parentID+'" class="boxRecipientsItem even">';
	}
	else{
		rtnStr+='<div id="'+parentID+'" class="boxRecipientsItem odd">';
	}
	rtnStr+='<span class="txtRecipientsItemDisplay">'+name+'</span>';	
	rtnStr+='			<span class="txtRecipientsItemUsing">'+langTextArray['using']+ ' '+type+'</span>';
	if(newAdd===true && glo_isLoggedIn===true){
		rtnStr+='	<div id="'+addId+'" class="boxRecipientsItemAdd" onclick="showAddAnother(this);"/></div>';
	}
	rtnStr+='			<div id="'+divid+'" class="boxRecipientsItemDelete" onclick="delGreyItem(this);"/></div>';
	rtnStr+='</div>';
	hideSuggest();
	return rtnStr;
}

function delGreyItem(blah){
	var d = document.getElementById('boxAddressList');
	var rm=document.getElementById("paren_"+blah.id);
	var id=blah.id;
	id=id.replace(/greyBox_/,"");
	removeFromQueue(id);
	d.removeChild(rm);
	if(glo_msgArray.length==0){$('boxAddressList').addClass('hidden');}
}

function hideSuggest(){
	$('boxToMessage').setStyle('z-index','10');
	$('boxToYourAddr').setStyle('z-index','10');
	$('txtTo').set('value','');
	glo_selected_item=0;	
	$$('.activeItem').removeClass('activeItem');
}

function showSuggest(){
	$('boxToMessage').setStyle('z-index','1');
	$('boxToYourAddr').setStyle('z-index','1');
}

function hoverSelection(blah){
	var tempId = blah.id;
	$$('.activeItem').removeClass('activeItem');
	$(tempId).addClass('activeItem');
}

function addToSenders(blah){
	var id=blah.id;
	id=id.replace(/uniq_/,"");
	addToMsg(id);
	var outHTML="";
	glo_greyBoxNum=$$('div[id^=paren_greyBox_]').length;
	outHTML+=txtGreyBox(id);	
	$('boxAddressList').set('html',$('boxAddressList').get('html')+outHTML);
	$('boxAddressList').setStyle('display','block');
	hideSuggest();
	$('txtTo').value="";
}

function addToSenders2(id){
	addToMsg(id);
	var outHTML="";
	glo_greyBoxNum=$$('div[id^=paren_greyBox_]').length;
	outHTML+=txtGreyBox(id,true);	
	$('boxAddressList').set('html',$('boxAddressList').get('html')+outHTML);
	$('boxAddressList').setStyle('display','block');
	hideSuggest();
	$('txtTo').value="";
}

function clearMsgQueue() {
	glo_msgArray.empty();
}
function addToMsg(contact){
	glo_msgArray.push(contact);
}
function removeFromQueue(contact) {
	glo_msgArray.erase(contact);
}
function processSendQueue() {
	var recipients = [];
	var destinations = [];
	glo_msgArray.each(function(contact) {
		if (contact.service == 'sms') {
			var number = contact.address.replace(/[^\d]/g,"");
			if (number[0] != '1') {
				number = '1' + number;
			}
			contact.address = number;
		}
		recipients.push({
			type: contact.service,
			name: contact.name,
			address: contact.address
		});
		destinations.push(contact.service);
	});
	
	glo_destinations = destinations.join(',').toLowerCase();
	recipients = JSON.encode(recipients);
	createMessage(recipients);
}
function createMessage(recipients){	
	glo_msgArray=[];
	var err_isTrue=false;
	if(glo_isLoggedIn===false){
		var tmpEml=$('txtYourAddr').value;
		if(isEmail(tmpEml)){
			glo_userEmail=tmpEml;
		}
		else{
			alert(langTextArray['msg_enter_email']);
			err_isTrue=true;
		}
	}
	if (!recipients.length) {
		alert(langTextArray['msg_enter_recip']);
		err_isTrue=true;
	}
	var sender=glo_userEmail;
	var tmpTitle=glo_title;
	try{tmpTitle=decodeURIComponent(glo_title);}catch(err){}
	var subject=tmpTitle;
	if(glo_url=="" || glo_url=="undefined" || glo_url==undefined){
		glo_url=glo_pUrl;
	}
	if(subject=="" || subject=="undefined"){
		subject=decodeURIComponent(glo_url);
	}
	glo_type="";
	var comment=$('txtMessage').value;
	var publisher=glo_publisher;
	glo_content=Url.decode(glo_content);
	glo_content=encodeURIComponent(glo_content);
	if(!glo_description || glo_description==undefined || glo_description=="undefined"){glo_description="";}
	if(!glo_summary || glo_summary==undefined || glo_summary=="undefined"){glo_summary="";}
	if(!glo_tags || glo_tags=="undefined"){glo_tags="";}
	var objects=[];
	objects[0]={type:glo_type, url:glo_url, title:glo_title, thumbnail:glo_thumb,
				embed:glo_content, description:glo_summary, tags:glo_tags };
	objects=JSON.encode(objects);
	var captcha_challange=null;
	var captcha_response=null;
	try{
		captcha_challange=Recaptcha.get_challenge();
		captcha_response=Recaptcha.get_response();
	}catch(err){}
	var	data= "sender="+encodeURIComponent(sender)+"&subject="+encodeURIComponent(subject)
		+"&comment="+encodeURIComponent(comment)+"&publisher="+publisher+"&objects="+encodeURIComponent(objects)
		+"&recipients="+encodeURIComponent(recipients)+"&sessionID="+glo_sessionID+"&return=JSON"
	    +"&captcha_challenge=" + encodeURIComponent(captcha_challange)
		+"&captcha_response=" + encodeURIComponent(captcha_response);
	if( glo_widget_info ) {
		data = data + "&session_token=" + glo_widget_info.session_token;
	}

	if(!err_isTrue){
		var request=new Request({
			method: "post",
			url: "/api/createMessage_ws.php",
			data: data,
			onFailure: function(){logError("createMessage","Ajax Failure");},
			onSuccess:createMessage_onSuccess
		});
		request.send();			
		widget.fireEvent('createMessageRequested');
		postYahooUpdate( glo_title, getSharURL() );
	}

}

function createMessage_onSuccess(responseText, responseXML) {
	logEvent(glo_destinations,"share");	
	try{var resp = JSON.decode(responseText);}
	catch(err){logError("createMessage",responseText);}
	if(resp.status==="SUCCESS"){
		emptyInputs();
		glo_msgArray=[];
		if( resp.data.require_captcha ) {
			glo_widget_info.require_captcha = resp.data.require_captcha;
		}
		widget.fireEvent('createMessageSucceeded');
	}
	else {
		logError("createMessage",JSON.encode(resp));
		widget.fireEvent('createMessageFailed', resp.statusMessage);
	}
}


var glo_selected_item=0;

function selectActive(elem){
	$$('.activeItem').removeClass('activeItem');
	var a=document.getElementById(elem);
	a.addClass("activeItem");
}


function determineType(str,blur){
	if(str!==""){
		var type="";
		var idToAdd=0;
		var selectedType=$('txtUsing').value;
		
		if(selectedType==="ALL"){
			if(isEmail(str)){type="EMAIL";}
			else if(isAIM(str)){type="AIM";}
			else if(isPhone(str)){type="SMS";}
		}
		else if(selectedType==="SMS"){
			if(isPhone(str)){
				type="SMS";
			}
			else{
				if(!blur){alert(langTextArray['msg_phone_no']);}
				type="";
				document.getElementById("txtTo").value="";			
			}
		}
		else if(selectedType==="EMAIL"){
			if(isEmail(str)){
				type="EMAIL";
			}
			else{	
				type="";	
				if(!blur){alert(langTextArray['msg_email_invalid']);	}	
			}
		}

		if( document.getElementById("txtTo").value!=="" && (type=="EMAIL" || type=="AIM" || type=="SMS")
			&& type!=="undefined" &&type!==undefined ) {
			setGlobals("contProto",type);
			setGlobals("contName",str);
			setGlobals("contAdd",str);
			var id=glo_contAdd.length-1;
			return id;
		}
		else{
			return false;
		}
		if(!blur){document.getElementById("txtTo").focus();}
	}
}
function isEmail(str) {
	if (str.match(/^[_a-zA-Z0-9-]+(\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.(([0-9]{1,3})|([a-zA-Z]{2,3})|(aero|coop|info|museum|name))$/)) {
		return true;
	} else {
		return false;
	}
}
function isPhone(str) {
	str = str.replace(/[^\d]/g, "");
	if (str.length === 10 || str.length === 11) {
		if (str.match(/^1?[2-9]\d{9}$/)) {
			return true;
		} else {
			return false;
		}
	} else {
		return false;
	}
}

function register(){
	var eml=$('registerEmailInput').value;
	var nme=$('registerNameInput').value;
	var nick=$('registerNickInput').value;
	var pass1=$('registerPasswordInput').value;
	var pass2=$('registerPassword2Input').value;
	var err = "";
	if(eml==""){
		err+=langTextArray['msg_enter_valid_email']+"<br/>";
	}
	if(!isEmail(eml)){
		err+=langTextArray['msg_email_address']+"<br/>"; 
	}
	if(nme==""){
		err+=langTextArray['msg_your_name']+"<br/>";
	}
	if (!nick.length) {
		err+=langTextArray['msg_enter_username']+"<br/>";
	}
	if(pass1==""){
		err+=langTextArray['msg_enter_apassword']+"<br/>";
	}
	else if(pass1.length<6){
		err+=langTextArray['msg_password_len']+"<br/>";
	}
	if(pass1!==pass2){
		err+=langTextArray['msg_password_match']+"<br/>"; 
	}
	if (err.length) {
		widget.validationFailed(err.replace(/<br\/>/gi, "\n"));
	}
	else {
		var data="email="+eml+"&password="+pass1+"&name="+nme+"&nickname="+nick;
		var request=new Request({
			method: "post",
			url: "/api/createUser_ws.php",
			data: data,
			onFailure: function(){logError("register","Ajax Failure");},
			onSuccess:register_OnSuccess.bind(this)
		});
		widget.fireEvent('registerUserRequested');
		request.send();
	}					
}

function register_OnSuccess(responseText, responseXML){
	try{var resp = JSON.decode(responseText);}
	catch(err){logError("register",responseText);}
	if(resp.status==="SUCCESS"){
		widget.user.acquireAuth(resp.data.token);
		$$('.working').addClass('hidden');
		$$('.success').removeClass('hidden');
		$('registerSubmit').removeClass('hidden');
		widget.fireEvent('registerUserSucceeded', widget.user);
		emptyInputs();
	}
	else if (resp.errorMessage==="Nickname already in use.") {
		widget.fireEvent('registerUserFailed', langTextArray['msg_nichname_inuse']);
	}
	else if(resp.statusMessage==="USER_ALREADY_EXISTS"){
		widget.fireEvent('registerUserFailed', langTextArray['msg_dtls_inuse']);
	}
	else {
		logError("register","Ajax Failure");
		widget.fireEvent('registerUserFailed', langTextArray['msg_reg_incomplete']);
	}
}

function getDiggs(url){
	if(url!=="" && url!==" " && url!==glo_last_url && url!=="undefined" && url!==undefined){
		var data="url="+encodeURIComponent(url) + "&fpc=" + glo_fpc;
		console.log(data);
		var request=new Request({
			method: "post",
			url: "/api/initWidget_ws.php",
			data: data,
			onFailure: function(){logError("get diggs","Ajax Failure");},
			onSuccess:getDiggs_onSuccess
		});
		glo_last_url=url;
		request.send();
	}
}

function getDiggs_onSuccess(responseText, responseXML){
	try{var resp = JSON.decode(responseText);}
	catch(err){logError("get diggs",responseText);}
	glo_widget_info = resp.data;
	glo_num_diggs = widget.nDiggs = resp.data.diggs;
	glo_digg_comments = widget.nDiggComments = resp.data.comments;
	var diggElement=$('post_digg_link');
	diggElement.set('title', widget.nDiggs + ' Diggs, ' + widget.nDiggComments + langTextArray['msg_comments']);
	diggElement.set('text', 'Digg (' + widget.nDiggs + ')');
}

function addHashTracking(url) {
	if( glo_hash_flag && (!url.match('#') && glo_sessionID != null) ) {
		var hash_arr = glo_sessionID.split('.');
		var sts_hash = parseFloat(hash_arr[0]).toString(36) + 
			'.' + parseFloat(hash_arr[1]).toString(36);
		return url + '#STS=' + sts_hash + '&shr=1';
	} else {
		return url;
	}
}

function createSharURL(url, sync){
	if(url!=="" && url!==" " && url!==glo_last_url2 && url!==undefined && url!=="undefined"){
    	var data="url="+encodeURIComponent(addHashTracking(url))+"&sessionID="+glo_sessionID+"&fpc="+glo_fpc;
        var request=new Request({
            method: "post",
            url: "/api/createSharURL_ws.php",
            data: data,
            onFailure: function(){logError("createSharURL","Ajax Failure");},
            onSuccess:createSharURL_onSuccess
        });
        if (sync) {
            request.options.async = false;
        }
        glo_last_url2=url;
        $('post_message').disabled=true;
        $('post_message').value=langTextArray['msg_loading'];
        request.send();
    }
}

function createSharURL_onSuccess(responseText, responseXML){
    try {
        var resp=JSON.decode(responseText);
        var sharURL=resp.data.sharURL;
    }
    catch(err){
        logError("createSharURL",responseText);
        var sharURL=glo_url;
    }
    glo_sharURL = sharURL;
    $('post_message').disabled=false;
    $('post_message').value=glo_title + ' - ' + glo_sharURL;
    widget.fireEvent('sharURLAcquired');
}

function getSharURL() {
    if (!glo_sharURL || glo_sharURL==undefined || glo_sharURL=="undefined") {
    	return glo_url;
    } else {
        return glo_sharURL;
    }
}

var closetimeout;
function suggestAutoClose(){
	closetimeout = setTimeout("hideSuggest()",750);	
}

function suggestCancelClose() {
	if(closetimeout)clearTimeout(closetimeout);
}

function sendDestination(destination1){
	var destAddress=glo_destinationAddress;
	glo_type="default";
	glo_content=Url.decode(glo_content);
	glo_content=encodeURIComponent(glo_content);
	if(glo_url=="" || glo_url=="undefined" || glo_url==undefined){
		glo_url=glo_pUrl;
	}
	if(!glo_description || glo_description==undefined || glo_description=="undefined"){glo_description="";}
	if(!glo_tags || glo_tags=="undefined"){glo_tags="";}
	var tmpTitle=glo_title;
	try{tmpTitle=decodeURIComponent(glo_title);}catch(err){}
	var objects="";
	var destination="";
	
	if (destination1 == "twitter.com") {
		objects=[ {type:glo_type, url:glo_url, sharURL:getSharURL(), title:tmpTitle, thumbnail:glo_thumb,
				embed:glo_content, description:glo_description, tags:glo_tags}];		
	} else {
		objects=[ {type:glo_type, url:glo_url, title:tmpTitle, thumbnail:glo_thumb,
				embed:glo_content, description:glo_description, tags:glo_tags}];
	}
	
	objects=JSON.encode(objects);
	destination=[{type:destination1 , address:destAddress}];
	destination=JSON.encode(destination);
	var eml="";
	if(glo_userEmail==""){
		eml="";
	}
	else{
		eml="&sender="+encodeURIComponent(glo_userEmail);
	}
	var data="publisher="+glo_publisher+"&objects="+encodeURIComponent(objects)
		+"&destinations="+encodeURIComponent(destination)+"&destinationType="+glo_destinationType
		+"&sessionID="+glo_sessionID+"&return=json"+eml;
	var url = "/api/createDestination_ws.php?"+data;

	var logger = new Image(1,1);
	logger.src = url;
	logger.onload = function(){return;};
	
	postYahooUpdate( tmpTitle, getSharURL() );
	
}

function populateSavedCredentials(service) {
	if (typeof(glo_credentials) == "undefined" || glo_credentials.length < 1) return;
	for (var i = 0; i <= glo_credentials.length; i++) {
		if (glo_credentials[i] && glo_credentials[i].service == service) {
			try{
				$('post_remember_me').checked = true;
				if( !$('post_url').hasClass('hidden') ) {
					widget.freezeTextInput($('post_url'));
					$('post_url').value = glo_credentials[i].url;
				}
				widget.freezeTextInput($('post_username'));
				widget.freezeTextInput($('post_password'));
				$('post_username').value = glo_credentials[i].username;
				$('post_password').value = glo_credentials[i].password;
			}catch(err){
			}
		}
	}
}

function logError(event,error){
	var url = "http://l.sharethis.com/error?event="+event
		+ "&publisher=" + encodeURIComponent(glo_publisher)
		+ "&ts" + (new Date()).getTime()
		+ "&title=" + encodeURIComponent(glo_title)
		+ "&url=" + encodeURIComponent(cleanURL(glo_url))
		+ "&pUrl="+encodeURIComponent(glo_pUrl)
		+ "&error="+encodeURIComponent(error)
		+ "&sessionID="+glo_sessionID
		+ "&fpc="+glo_fpc;
		//+ "&sharURL="+glo_sharURL;
	
	var logger = new Image(1,1);
	logger.src = url;
	logger.onload = function(){return;};
	
}

function odjs(scriptSrc,callBack){
	this.head=document.getElementsByTagName('head')[0];
	this.scriptSrc=scriptSrc;
	this.script=document.createElement('script');
	this.script.setAttribute('type', 'text/javascript');
	this.script.setAttribute('src', this.scriptSrc);
	this.script.onload=callBack;
	this.script.onreadystatechange=function(){
		if(this.readyState=='loaded'){
			callBack();
		}
	}
	this.head.appendChild(this.script);
}



Widget.Page = new Class(
	{ Implements: Events,
	  id: '',
	  pages: {},
	  widget: null,
	  domContainer: null,
	  shown: false,
	  firstShow: true,
	  initialize: function(widget, props) {
		  this.widget = widget;
		  if (props) {
			  for (var p in props) {
				  if (typeof props[p] != 'function') {
					  this[p] = props[p];
				  }
			  }
		  }
		  if (this.id.length) {
			  this.domContainer = $(this.id);
		  }
		  if (this.pages) {
			  this.pages = widget.initializePages(this.pages);
		  }
	  },
	  onShow: function() {
		  this.shown = true;
		  if (this.firstShow) {
			  this.firstShow = false;
		  }
	  },
	  onHide: function() {
		  this.shown = false;
	  },
	  isShown: function() {
		  return this.shown;
	  },
	  onReady: function() {
	  },
	  /**
	   * Binds a keyup event handler to text and password inputs in this page. 
	   * Handler calls submitForm. Override submitForm to do what you need.
	   */
	  bindReturnKeyToSubmission: function() {
		  this.domContainer.getElements('input[type=text], input[type=password]').each((function(input) {
			  // @todo: validation shouldn't really happen in the postXXX method
			  input.addEvent('keyup', (function(event) {
				  if (event.key == 'enter' && !widget.showingAlert) {
					  this.submitForm();
				  }
			  }).bind(this));
		  }).bind(this));
	  },
	  post_bindReturnKeyToSubmission: function(page) {
		  $('post_template').getElements('input[type=text], input[type=password]').each((function(input) {
			  input.removeEvents('keyup');
			  // @todo: validation shouldn't really happen in the postXXX method
			  input.addEvent('keyup', (function(event) {
				  if (event.key == 'enter' && !widget.showingAlert) {
					  widget.pages.post.pages[page].submitForm();
				  }
			  }).bind(this));
		  }).bind(this));
	  },
	  submitForm: function() {
	  },
	  bindHelpText: function(input, text) {
		  // note: for some unknown reason, initial value does
		  // not take when bound onReady ... perhaps because it's
		  // not visible yet? at any rate, bind on the first onShow and
		  // it seems to work.
		  var textColor = input.getStyle('color');
		  if (!input.get('value').length) {
			  input.set('value', text);
			  input.setStyle('color', '#888');
		  }
		  input.addEvent('blur', (function() {
			  if (!this.get('value').length) {
				  this.set('value', text);
				  this.setStyle('color', '#888');
			  }
		  }).bind(input));
		  input.addEvent('focus', (function() {
			  if (this.get('value') == text) {
				  this.set('value', '');
				  this.setStyle('color', textColor);
			  }
		  }).bind(input));
	  }
	});

Widget.implement({
	// these are bare objects which will be dynamically turned into subclasses of Widget.Page 
	// later in initializePages. pretty silly but oh well.
	pages: {
		home: {
			id: 'home_page',
			onShow: function() {
				this.parent();
			},
			onHide: function() {
				this.parent();
			},
			onReady: function() {
				$('twitter_update_status').addEvent('click', function(event) {
					widget.showPage('post|twitter');
					event.stop();
				});
				widget.user.addEvent('infoChanged', (function() {
					// rebuild the carousel based on user prefs, if any
					// @todo: re-factor the function.
					createSwList();
				}).bind(this));
				
				widget.carousel = this.carousel = new Widget.Carousel(
					this.domContainer.getElement('.carousel'),
					(!glo_tabArray.contains('email') ? Widget.Carousel.initialState_more : Widget.Carousel.initialState_less)
				);

				this.parent();
			}
		},
		sharebox: {
			id: 'sharebox_page',
			onReady: function() {
				$('sharebox_submit').addEvent('click',function(event) {
					gaLog("Sharebox", "save_click", $('sharebox_tags').get('value')); 
					widget.saveToShareBox($('sharebox_tags').get('value'));
					event.stop();
				});
				//this.bindHelpText($('sharebox_tags'), 'Separated by commas');	// see note in bindHelpText
				if (glo_tags_array && glo_tags_array.length) {
					$('sharebox_tags').set('value', glo_tags_array.join(','));
				}
				widget.addEvent('shareableURLChanged', (function(url) {
					if( url == '' ) {
						url = glo_url;
					}
					var domain = widget.extractDomainFromURL(url);
					$('sharebox_previewUrl').set('text', domain);
					if (url.length) {
    					$('sharebox_previewThumb').removeClass('hidden');
						$('sharebox_previewThumb').set('src', glo_thumbImageTag);
					}
					else {
						$('sharebox_previewThumb').addClass('hidden');
					}
					$('sharebox_preview').removeClass('hidden');
				}).bind(this));
				widget.addEvent('shareableTitleChanged', (function(title) {
					try{title=decodeURIComponent(title)}catch(err){}
					$('sharebox_previewTitle').set('text',title);
				}).bind(this));
				widget.addEvent('shareableDescriptionChanged', (function(description) {
					$('sharebox_previewDescription').set('text', description);
				}).bind(this));
				this.parent();
				
				widget.addEvent('saveToShareBoxSucceeded', function() {
					widget.showPage('done');
					$('done_message').set('text', langTextArray['msg_item_saved']);
				});
				widget.addEvent('saveToShareBoxFailed', function() {
					widget.pushModalErrorSheet(langTextArray['msg_not_save_sharebox']);
				});
			},
			onShow: function() {
				if (this.firstShow) {
					// see note in bindHelpText
					this.bindHelpText($('sharebox_tags'), langTextArray['msg_seprated_comma']);
				}
				widget.user.addEvent('signedOut', this.runAway.bind(this));
				this.parent();
			},
			onHide: function() {
				widget.user.removeEvent('signedOut', this.runAway.bind(this));
			},
			runAway: function() {
				if (glo_page == "send" || glo_page == "post|twitter") {
					widget.showPage(glo_page);
				} else {
					widget.showPage('home');
				}
			}
		},
		register: {
			id: 'register_page',
			onShow: function() {
				this.parent();
			},
			onReady: function() {
				$('registerSubmit').addEvent('click', (function() {
					this.submitForm();
				}).bind(this));
				widget.addEvent('registerUserRequested', function() {
					widget.pushModalWorkingSheet(langTextArray['msg_creating_acc']+'&hellip;');
				});
				widget.addEvent('registerUserSucceeded', function() {
					widget.popModalWorkingSheet();
					setTimeout(function() {
						if (glo_page == "send" || glo_page == "post|twitter") {
							widget.showPage(glo_page);
						} else {
							widget.showPage('home');
						}
					}, 10);
				});
				widget.addEvent('registerUserFailed', function(message) {
					widget.popModalWorkingSheet();
					widget.pushModalErrorSheet(message);
				});
				this.bindReturnKeyToSubmission();
				this.parent();
			},
			submitForm: function() {
				register();
			}
		},
		send: {
			id: 'send_page',
			toField: null,
			_resizeShortcutsOnShow: false,
			buildShortcutList: function(recipients) {
				$('recent_recipients').getChildren().each(function(child) { 
					if (child.nodeName.toLowerCase() != 'h4') { child.destroy(); } 
				});
				for (var i = 0; i < 3 && i < recipients.length; i++) {
					var e = new Element('span', { 'class': 'shortcut' });
					var a = new Element('a', { href: '#', title: recipients[i].address + ' (' + recipients[i].service + ')' });
					a.set('html', recipients[i].name);
					a.addEvent('click', (function() {
						gaLog("Send", "recents_click", i+1, i+1); 
						var enclosedRecipient = recipients[i];	// peel off a copy for the closure
						return (function(event) {
							var existingContact = widget.user.searchContactsExact(
								enclosedRecipient.service.toLowerCase(),
								enclosedRecipient.address
							);
							if (existingContact) {
								existingContact.select();
							}
							else {
								var newContacts = widget.user.addContactsLocally([enclosedRecipient]);
								setTimeout(function() {
									newContacts[0].select();
								}, 1);
							}
							event.stop();
						});
					})());
					$('recent_recipients').grab(e.grab(a));
				}
				if (this.isShown()) {
					this.resizeShortcuts();
				}
				else if (recipients.length) {
					this._resizeShortcutsOnShow = true;
				}
				else {
					$('recent_recipients').addClass('hidden');
				}
			},
			hideShortcutList: function() {
				$('recent_recipients').getChildren().each(function(child) { 
					if (child.nodeName.toLowerCase() != 'h4') { child.destroy(); } 
				});
				$('recent_recipients').addClass('hidden');
			},
			resizeShortcuts: function() {
				$('recent_recipients').removeClass('hidden');
				$('recent_recipients').getChildren().getLast().addClass('last');
				var anchors = [];
				var total = 0;
				$('recent_recipients').getChildren().each(function(item) {
					var anchor = item.getElement('a');
					var width = item.getSize().x;
					if (anchor) {
						anchors.push({ anchor: anchor, width: width });
					}
					total += width;
				});
				while (total > 295) {
					anchors.sort(function(a, b) { return b.width - a.width; });
					var str = anchors[0].anchor.get('html');
					anchors[0].anchor.set('html', str.substring(0, str.length - 3) + '&hellip;');
					anchors[0].width = anchors[0].anchor.getSize().x;
					total = 0;
					$('recent_recipients').getChildren().each(function(item){
						total += item.getSize().x;
					});
				}
			},
			updateCharacterCounter: function() {
				var maxCharacterCount = widget.maxSendMessageLength;
				if ( this.hasTwitterRecipients() ) {
					maxCharacterCount = 140;
					$('boxToYourAddrInfo').style.display = 'block';
				}
				$('spanMessageCounter').set('html', maxCharacterCount - $('txtMessage').value.length);
			},
			hasTwitterRecipients: function() {
				var retval = false;
				var recipients = widget.user.getSelectedContacts();
				recipients.each(function(contact) {
					if ( (contact.service && contact.service.toLowerCase() == 'twitter')
						 ||
						 (contact.type && contact.type.toLowerCase() == 'twitter')
					   ) {
						retval = true;
					}
				});
				return retval;
			},
			onShow: function() {
				if (this.firstShow) {
					// see note in bindHelpText
					this.bindHelpText($('txtYourAddr'), langTextArray['msg_your_email']);
				}
				this.updateCharacterCounter();
				$('privacyLink').removeClass('hidden');
				this.parent();
				this.toField.onPageShown();
				if (this._resizeShortcutsOnShow) {
					this.resizeShortcuts();
					this._resizeShortcutsOnShow = false;
				}
				
				if( glo_widget_info.require_captcha ) {
					if(typeof(Recaptcha)=="undefined"){
						odjs("http://api.recaptcha.net/js/recaptcha_ajax.js",function(){Recaptcha.create("6Leh9AIAAAAAAHYpIPrfzeRFMrHC2QysVPB93Qhh", "captcha");});
					}else{
						  Recaptcha.create("6Leh9AIAAAAAAHYpIPrfzeRFMrHC2QysVPB93Qhh","captcha");
					}

					gaLog("Send", "captcha_shown", "require_captcha flag set"); 
				}
			},
			onHide: function() {
				this.parent();
				$('privacyLink').addClass('hidden');
				widget.user.contactServiceFilters = [];
				this.toField.onPageHidden();
			},
			onReady: function() {
				this.toField = new Widget.ToField(widget, $('send_to_field'));
				$('previewCloseLink').addEvent('click',function(event){
					gaLog("Send", "preview_close_click"); 
					$('preview').addClass('hidden');
					$('linkPreview').removeClass('hidden');
					$('previewCloseLink').addClass('hidden');
					event.stop();
				});
				$('linkPreview').addEvent('click',function(event){
					gaLog("Send", "preview_open_click"); 
					$('preview').removeClass('hidden');
					$('previewThumb').set("html","<img width='100' src='"+glo_thumbImageTag+"'/>");
					$('linkPreview').addClass('hidden');
					$('previewCloseLink').removeClass('hidden');
					event.stop();
				});
				$('btnShareSend').addEvent('click', (function(){
					gaLog("Send", "send_btn_click"); 
					var recipients = widget.user.getSelectedContacts();
					
					if($('contact_search_field')){
						if($('contact_search_field').value.length>0){
							widget.pages.send.toField._selectHighlightedResult();
							widget.pages.send.toField.hideSearchResults();
						}
					}					
					if(recipients.length<1){
						alert(langTextArray['msg_enter_recipto']);
					}
					else if(recipients.length<25){
						recipients.each(function(contact) {
							addToMsg(contact);
						});
						processSendQueue();
					}else{
						alert(langTextArray['msg_less_recip']);
					}
				}).bind(this));
				$('txtMessage').addEvent('keypress',(function(event){
					event = new Event(event);
					var retval = true;
					var inputField = $('txtMessage');
					var maxCharacterCount = widget.maxSendMessageLength;
					if ( glo_sharURL && this.hasTwitterRecipients() ) {
						maxCharacterCount = 140;
					}
					var counterSpan = $('spanMessageCounter');
					retval = widget.limitCharacters(inputField, maxCharacterCount, counterSpan, event);
					return retval;
				}).bind(this));
				$('txtMessage').addEvent('keyup',(function(){
					this.updateCharacterCounter();
				}).bind(this));
				widget.user.addEvent('signedIn', function() {
					$('from_box').addClass('hidden');
				});
				widget.user.addEvent('signedOut', function() {
					$('from_box').removeClass('hidden');
				});
				widget.addEvent('createMessageRequested', (function() {
					widget.pushModalWorkingSheet();
					if( glo_widget_info.require_captcha ) {
						Recaptcha.destroy();
					}
				}).bind(this));
				widget.addEvent('createMessageSucceeded', (function() {
					widget.popModalWorkingSheet();
					widget.showPage('done');
					widget.user.getUserAccountInfo();	// to update share history, etc...
				}).bind(this));
				widget.addEvent('createMessageFailed', (function() {
					widget.popModalWorkingSheet();
					widget.pushModalErrorSheet(langTextArray['msg_not_sent']);
					if( glo_widget_info.require_captcha ) {
						if(typeof(Recaptcha)=="undefined"){
							odjs("http://api.recaptcha.net/js/recaptcha_ajax.js",function(){Recaptcha.create("6Leh9AIAAAAAAHYpIPrfzeRFMrHC2QysVPB93Qhh","captcha", {callback: Recaptcha.focus_response_field});});
						}else{
							Recaptcha.create("6Leh9AIAAAAAAHYpIPrfzeRFMrHC2QysVPB93Qhh","captcha", {callback: Recaptcha.focus_response_field});
						}
						
						gaLog("Send", "captcha_shown", "message failed"); 
					}
				}).bind(this));

				widget.user.addEvent('recipientHistoryChanged', (function(recipientsContainer) {
					widget.deferWhile('recipientHistoryIsChanging', (function() {
						if (widget.user.isSignedIn() && glo_tabArray.contains('email')) {
							this.buildShortcutList(recipientsContainer.recipients);
						}
					}).bind(this));
				}).bind(this));
				
				widget.user.addEvent('signedOut', (function() {
					this.hideShortcutList();
				}).bind(this));

				this.parent();
			}
		},
		'import': {
			id: 'import_page',
			nImportsOnShow: 0,
			onReady: function() {
				$('import_list').getElements('li').each(function(item, index) {
					item.getElement('a').addEvents({
						click: function(event) {					
							widget.setImportContactService(item.getChildren()[0].get('class'));
							event.stop();
						}
					});
				});
				
				$('import_contacts_submit').addEvent('click', (function(event) {
					gaLog("Import", "import_btn_click", widget.currentImportContactService.protocolName); 
					Cookie.dispose('import', {domain: ".sharethis.com", path: '/'});
					Cookie.dispose('import_delt', {domain: ".sharethis.com", path: '/'});
					Cookie.dispose('signin', {domain: ".sharethis.com", path: '/'});
					Cookie.dispose('oauth_token', {domain: ".sharethis.com", path: '/'});
					var service = widget.currentImportContactService;
					if( service.protocolName == 'aol' || service.protocolName == 'gmail') {
						var username = $('import_contacts_username').get('value');
						var password = $('import_contacts_password').get('value');
						this.submitForm();
					} else if( service.protocolName == 'yahoo' ) {
						var service_feed = $('input_service_checkbox').checked ? 1 : '';
						var do_signin = ( $('input_signin_checkbox') && $('input_signin_checkbox').checked ) ? 1 : 0;

						widget.pushModalWorkingSheet(langTextArray['msg_waiting_auth']+'&hellip;');
						window.open('/auth.php?provider=yahoo' + '&yahoo_feed=' + service_feed
									+ '&import_contacts=1',
									'3rd_party_signin','scrollbars=yes,directories=no,menubar=yes,toolbar=yes,height=500,width=700');
						widget.pages.login.pollSigninCookie(1, do_signin, 'import');
					} else {
						//this.fireEvent('importContactsRequested');
						widget.pushModalWorkingSheet(langTextArray['msg_waiting_auth']+'&hellip;');
						window.open('/share3x/import.php?provider=' + service.protocolName,'import_contacts','scrollbars=yes,directories=no,menubar=yes,toolbar=yes,height=600,width=900');
						this.pollImportCookie();
					}
					event.stop();
				}).bind(this));
				
				widget.addEvent('importContactsRequested', function() {
					widget.pushModalWorkingSheet(langTextArray['msg_importing_cont']+'&hellip;');
				});
				widget.addEvent('importContactsSucceeded', function(contactInfo) {
					widget.popModalWorkingSheet();
					$('send_title').set('html', langTextArray['email']);
					widget.showPage('send');					
					var contImp=langTextArray['msg_succ_cont'].replace(/#%num%#/, contactInfo.nContacts);
					widget.displayNotification(contImp);
					emptyInputs();
				});
				widget.addEvent('importContactsSucceededSilent', function(contactInfo) {
					widget.popModalWorkingSheet();
					var contImp1=langTextArray['msg_succ_cont'].replace(/#%num%#/, contactInfo.nContacts);
					widget.displayNotification(contImp1);
					emptyInputs();
				});
				widget.addEvent('importContactsFailed', function() {
					widget.popModalWorkingSheet();
					widget.pushModalErrorSheet(langTextArray['msg_not_retrive']);
				});
				widget.addEvent('importContactServiceChanged', (function(serviceTag) {
					if (this.isShown()) {
						var service = widget.contactSources[serviceTag];
						$('regAuth').setStyle('display', 'none');
						$('oauthImport').setStyle('display', 'block');
						$('import_details').empty();
						$('import_with').set('text', langTextArray['msg_import_serv'] + ' ' +  widget.contactSources[serviceTag].title);
						switch(serviceTag) {
						case 'gmail':
							$('regAuth').setStyle('display', 'block');
							$('oauthImport').setStyle('display', 'none');
							//$('oauthImport').getElement('label').set('text','We\'ll take you to Gmail where you\'ll be asked to let ShareThis access your address book.');
							break;
						case 'yahoo':
							var textMsg=langTextArray['msg_service'].replace(/#%name%#/, 'Yahoo');
							$('oauthImport').getElement('label').set('text',textMsg);
							if(glo_thirdparty_flag != 'yahoo') {
							    var signin_element = new Element('div', { 'id': 'import_signin_box' });
							    signin_element.grab( new Element('input', { 'type': 'checkbox',
																			'name': 'input_signin_checkbox',
																			'id': 'input_signin_checkbox',
																			'checked': 'yes' }) );
							    signin_element.appendText(langTextArray['msg_yahoo_signin']);
							    $('import_details').grab(signin_element);
							}
							var feed_checkbox = new Element('div', { 'id': 'import_service_box' });
							feed_checkbox.grab( new Element('input', { 'type': 'checkbox',
																	   'name': 'input_service_checkbox',
																	   'id': 'input_service_checkbox',
																	   'checked': 'yes' }) );
							feed_checkbox.appendText(langTextArray['msg_share_yahoo_updt']);
							$('import_details').grab(feed_checkbox);


							break;
						case 'msn':
							var textMsg=langTextArray['msg_service'].replace(/#%name%#/, 'MSN/Hotmail');
							$('oauthImport').getElement('label').set('text',textMsg);
							break;
						case 'aol':
							$('regAuth').setStyle('display', 'block');
							$('oauthImport').setStyle('display', 'none');
							break;
						}
						var importBox = this.domContainer.getElement('.mbox');//$$('.mbox')[0];
						var item = null;
						$('import_list').getElements('li').each(function(i) {
							if (i.getElement('a').hasClass(serviceTag)) {
								item = i;
							}
						});
						if (item) {
							this.pointImportPointerAt(item);
						}
					}
				}).bind(this));
				this.bindReturnKeyToSubmission();
				this.parent();
			},
			
			pollImportCookie: function() {
				import_cookie_tid = setInterval('import_cookie = Cookie.read("import");'
												+ 'if(import_cookie == -1) { clearInterval(import_cookie_tid);'
												+ 		'widget.popModalWorkingSheet();'
												+       'widget.pushModalErrorSheet("Contact Import Failed.");'
												+		'import_cookie_cycles = 0;'
												+       'Cookie.dispose("import", {domain: ".sharethis.com", path: "/"});'
												+		'Cookie.dispose("import_delt", {domain: ".sharethis.com", path: "/"});'
												+ '} else if(import_cookie) { clearInterval(import_cookie_tid);'
												+		'import_cookie_cycles = 0;'	
												+ 		'contact_url = import_cookie;'
												+		'delt = Cookie.read("import_delt");'
												+		'Cookie.dispose("import", {domain: ".sharethis.com", path: "/"});'
												+		'Cookie.dispose("import_delt", {domain: ".sharethis.com", path: "/"});'
												+       'widget.importContacts( widget.currentImportContactService, "", "", escape(contact_url), delt, "", "" );'
												+ '} else if(import_cookie_cycles++ > 120) { clearInterval(import_cookie_tid);'
												+ 		'widget.popModalWorkingSheet();'
												+       'widget.pushModalErrorSheet("'+langTextArray['msg_timeout']+'");'
												+		'import_cookie_cycles = 0;'
												+ '}'
												, 1000);
			},

			submitForm: function() {
				var service = widget.currentImportContactService;
				var username = $('import_contacts_username').get('value');
				var password = $('import_contacts_password').get('value');
				widget.importContacts(widget.currentImportContactService, username, password, '', '', '', '');				
			},
			onShow: function() {
				setTimeout(function() {widget.setImportContactService('gmail')}, 1);
				this.nImportsOnShow = widget.user.contacts.length;
				if (this.firstShow) {
					setTimeout(function() {widget.setImportContactService('gmail')}, 1);
				}
				$('privacyLink_import').removeClass('hidden');
				this.parent();
			},
			onHide: function() {
				$('privacyLink_import').addClass('hidden');
				this.parent();
			},
			pointImportPointerAt: function(pointToItem) {
				var y = pointToItem.getPosition(pointToItem.getParent()).y;
				var pointer = $$('.mboxpoint')[0];
				var importBox = $$('.mbox')[0];
				pointer.set('tween', { duration: 0 });
				pointer.tween('top', y + (pointToItem.getSize().y / 2) - (pointer.getSize().y / 2) + 5);	// random extra 5 pixels. *shrug*

				$('import_list').getElements('li').each(function(selectedItem) {
					if (selectedItem !== pointToItem) {
						selectedItem.removeClass('selected');
					}
					else {
						selectedItem.addClass('selected');
					}
				});
			}
		},
		'account_linking': {
			id: 'account_linking_page',
			onReady: function() {
				$('account_linking_details').grab( new Element('label', { 'class': 'login_label', 'text': langTextArray['username'] }) );
				$('account_linking_details').grab( new Element('input', { 'id': "account_linking_username",
																		  'type': 'text',
																		  'class': 'text',
																		  'value': '' }) );
				$('account_linking_details').grab( new Element('label', { 'class': 'login_label', 'text': langTextArray['password'] }) );
				$('account_linking_details').grab( new Element('input', { 'id': "account_linking_password",
																		  'type': 'password',
																		  'class': 'text',
																		  'value': '' }) );
				
				$('account_linking_skip').addEvent('click', (function(event) {
					gaLog("Account_Link", "account_link_skip_btn_click"); 
					widget.pages.login.processAccountLinking(true);
				}).bind(this));

				$('account_linking_submit').addEvent('click', (function(event) {
					gaLog("Account_Link", "account_link_submit_btn_click"); 
					widget.pages.login.processAccountLinking(false);
				}).bind(this));

			},
			onShow: function() {
				if(glo_thirdparty_existing_email != '') {
					$('account_linking_info').set('html', langTextArray['acc_linking_info']);
					$('account_linking_username').set('text', glo_thirdparty_existing_email);
				} else {
					$('account_linking_info').set('html', langTextArray['yahoo_linking_info']);
				}

			}
		},
		'login': {
			id: 'login_page',
			onReady: function() {
				$each(widget.loginSources, function(item, index){
					var login_item = new Element('li').grab(
						new Element('a', { 'href': 'javascript:void(0)',
										   'class': item.protocolName,
										   'html': item.title,
										   'events': { 
											   'click': function(event) {
												   widget.setLoginService(item.protocolName);
												   event.stop();
											   }
										   }
										 }));
					$('login_list').grab(login_item);
				});
				
				$('login_submit').addEvent('click', (function(event) {
					gaLog("Login", "login_btn_click", widget.currentLoginService.protocolName); 
					widget.pages.login.processLogin();
				}).bind(this));
				
				
				widget.addEvent('loginRequested', function() {
				});

				widget.addEvent('loginSucceeded', function(contactInfo) {
					/*widget.popModalWorkingSheet();
					  $('send_title').set('html', 'Email');
					  widget.showPage('send');
					  widget.displayNotification('Success! ' + contactInfo.nContacts + ' contacts imported.');
					  emptyInputs();*/
				});

				widget.addEvent('loginFailed', function() {
					widget.popModalWorkingSheet();
					widget.pushModalErrorSheet(langTextArray['msg_unable_signin']);
				})
				
				widget.addEvent('loginServiceChanged', (function(serviceTag) {
					var service = widget.loginSources[serviceTag];
					$('login_details').empty();
					$('createAccount').setStyle('display', 'none');

					switch(serviceTag) {
					case 'yahoo':
						$('login_details').grab( new Element('div', { 'id': 'login_with', 'text': langTextArray['msg_yahoo_signin'] }), 'top' );
						$('login_details').grab( new Element('label', 
															 { 'id': 'oauth_label',
															   'text': langTextArray['msg_yahoo'] }) );

						var feed_checkbox = new Element('div', { 'id': 'login_service_box' });
						feed_checkbox.grab( new Element('input', { 'type': 'checkbox',
																   'name': 'input_service_checkbox',
																   'id': 'input_service_checkbox',
																   'checked': 'yes' }) );
						feed_checkbox.appendText(langTextArray['msg_share_yahoo_updt']);
						$('login_details').grab(feed_checkbox);

						var contacts = new Element('div', { 'id': 'login_contacts_box' });
						contacts.grab( new Element('input', { 'type': 'checkbox',
															  'name': 'input_contacts_checkbox',
															  'id': 'input_contacts_checkbox',
															  'checked': 'yes' }) );
						contacts.appendText(langTextArray['msg_import_yahoo']);
						$('login_details').grab(contacts);
						break;
					case 'sharethis':
						$('login_details').grab( new Element('div', { 'id': 'login_with', 'text': langTextArray['msg_signin_share'] }), 'top' );
						$('login_details').grab( new Element('label', { 'class': 'login_label', 'text': langTextArray['username'] }) );
						$('login_details').grab( new Element('input', { 'id': "login_username",
																		'type': 'text',
																		'class': 'text',
																		'value': '' }) );
						$('login_details').grab( new Element('label', { 'class': 'login_label', 'text': langTextArray['password'] }) );
						$('login_details').grab( new Element('input', { 'id': "login_password",
																		'type': 'password',
																		'class': 'text',
																		'value': '' }) );

						$('createAccount').setStyle('display', 'block');
						break;	
					}
					this.bindReturnKeyToSubmission();
					var loginBox = this.domContainer.getElement('.mbox2');//$$('.mbox')[0];
					var item = null;
					$('login_list').getElements('li').each(function(i) {
						if (i.getElement('a').hasClass(serviceTag)) {
							item = i;
						}
					});
					if (item) {
						this.pointImportPointerAt(item);
					}
				}).bind(this));
				this.bindReturnKeyToSubmission();
				this.parent();
			},

			processLogin: function(){
				Cookie.dispose("signin", {domain: ".sharethis.com", path: "/"});
				Cookie.dispose("signin_email", {domain: ".sharethis.com", path: "/"});
				Cookie.dispose("signin_existing_email", {domain: ".sharethis.com", path: "/"});
				Cookie.dispose("oauth_token", {domain: ".sharethis.com", path: "/"});

				var service = widget.currentLoginService;
				if( service.protocolName == 'sharethis') {
					var username = $('login_username').get('value');
					var password = $('login_password').get('value');
					this.submitForm();
				} else {
					var service_feed = $('input_service_checkbox').checked ? 1 : '';
					var import_contacts = $('input_contacts_checkbox').checked ? 1 : 0;
					//this.fireEvent('importContactsRequested');
					widget.pushModalWorkingSheet(langTextArray['msg_waiting_auth']+'&hellip;');
					glo_thirdparty_service_feed = service_feed;
					window.open('/auth.php?provider=' + service.protocolName + '&yahoo_feed=' + service_feed
								+ '&import_contacts=' + import_contacts,
								'3rd_party_signin','scrollbars=yes,directories=no,menubar=yes,toolbar=yes,height=500,width=700');
					this.pollSigninCookie(import_contacts, 1, 'login');
				}
				return true;
			},				
			
			processAccountLinking: function(skip_linking) {
				var username = $('account_linking_username').get('value');
				var password = $('account_linking_password').get('value');

				widget.pushModalWorkingSheet(langTextArray['msg_waiting_auth']+'&hellip;');
				
				var link_string = '';
				if(skip_linking == true) {
					link_string = '&link_accounts=0';
				} else {
					link_string = '&link_accounts=1'
						+ '&username=' + username
						+ '&password=' + password;
				}

				window.open('/auth.php?provider=yahoo' + '&yahoo_feed=' + glo_thirdparty_service_feed
							+ '&import_contacts=' + glo_thirdparty_import_contacts
							+ '&close=1' 
							+ '&link_step=1' 
							+ '&email_address=' + glo_thirdparty_email_address 
							+ '&auth_token=' + encodeURIComponent(glo_thirdparty_oauth_token)
							+ link_string
							,'3rd_party_signin','scrollbars=yes,directories=no,menubar=yes,toolbar=yes,height=500,width=700');
				this.pollSigninCookie(glo_thirdparty_import_contacts, glo_thirdparty_signin, glo_thirdparty_origin);
			},

			processLogin_callback: function(origin, import_contacts, signin) {
				glo_thirdparty_origin = origin;
				glo_thirdparty_import_contacts = import_contacts;
				glo_thirdparty_signin = signin;

				var signin_email = Cookie.read("signin_email");
				glo_thirdparty_email_address = signin_email;
				var signin_existing_email = Cookie.read("signin_existing_email");
				var oauth_token = Cookie.read("oauth_token");
				glo_thirdparty_oauth_token = oauth_token;
				
				if( signin_email && signin_email != '' ) {
					widget.popModalWorkingSheet();
					glo_thirdparty_email = signin_email;
					if(signin_existing_email) {
						glo_thirdparty_existing_email = signin_existing_email;
					}
					widget.showPage('account_linking');
				} else {
					var signin_cookie = Cookie.read("signin");
					if(signin == 1) {
						widget.user.acquireAuth(signin_cookie);
					} else {
						widget.popModalWorkingSheet();
					}
					if(import_contacts == 1) {
						oauth_token = Cookie.read("oauth_token");
						if(origin == "login") {
							widget.importContacts( widget.currentLoginService, "", "", "", "", oauth_token, 1 );
						} else {
							widget.importContacts( widget.currentImportContactService, "", "", "", "", oauth_token, 0 );
						}
					}

				}


				Cookie.dispose("signin", {domain: ".sharethis.com", path: "/"});
				Cookie.dispose("signin_email", {domain: ".sharethis.com", path: "/"});
				Cookie.dispose("signin_existing_email", {domain: ".sharethis.com", path: "/"});
				Cookie.dispose("oauth_token", {domain: ".sharethis.com", path: "/"});
			},

			pollSigninCookie: function(import_contacts, signin, origin) {
				signin_cookie_tid = setInterval('signin_cookie = Cookie.read("signin");'
												+ 'if(signin_cookie == -1) { clearInterval(signin_cookie_tid);'
												+ 		'widget.popModalWorkingSheet();'
												+       'widget.pushModalErrorSheet("Signin Failed.");'
												+		'signin_cookie_cycles = 0;'
												+       'Cookie.dispose("signin", {domain: ".sharethis.com", path: "/"});'
												+ '} else if(signin_cookie) { clearInterval(signin_cookie_tid);'
												+		'signin_cookie_cycles = 0;'	
												+       'widget.pages.login.processLogin_callback("'+origin+'",'+import_contacts+','+signin+');'
												+ '} else if(signin_cookie_cycles++ > 120) { clearInterval(signin_cookie_tid);'
												+ 		'widget.popModalWorkingSheet();'
												+       'widget.pushModalErrorSheet("'+langTextArray['msg_timeout']+'");'
												+		'signin_cookie_cycles = 0;'
												+ '}'
												, 1000);
			},
			
			submitForm: function() {
				var service = widget.currentLoginService;
				var username = $('login_username').get('value');
				var password = $('login_password').get('value');
				widget.login(widget.currentLoginService, username, password, '', '');
				//widget.signIn();
			},
			onShow: function() {
				if (this.firstShow) {
					//setTimeout(function() {widget.setLoginService('yahoo')}, 1);
				}
				$('privacyLink').removeClass('hidden');
				$('footerReg').addClass('hidden');
				this.parent();
			},
			onHide: function() {
				$('privacyLink').addClass('hidden');
				$('footerReg').removeClass('hidden');
				this.parent();
			},
			pointImportPointerAt: function(pointToItem) {
				var y = pointToItem.getPosition(pointToItem.getParent()).y;
				var pointer = $$('.mboxpoint2')[0];
				var loginBox = $$('.mbox2')[0];
				pointer.set('tween', { duration: 0 });
				pointer.tween('top', y + (pointToItem.getSize().y / 2) - (pointer.getSize().y / 2) + 5);	// random extra 5 pixels. *shrug*

				$('login_list').getElements('li').each(function(selectedItem) {
					if (selectedItem !== pointToItem) {
						selectedItem.removeClass('selected');
					}
					else {
						selectedItem.addClass('selected');
					}
				});
			}
		},
		done: {
			id: 'done_page',
			onReady: function() {
				$('doneScreenOk').addEvent('click',function(event) {
					widget.user.deselectContacts();
					clearMsgQueue();
					if (glo_page == "send" || glo_page == "post|twitter") {
						widget.showPage(glo_page);
					} else {
						widget.showPage('home');
					}
					event.stop();
				});
				this.parent();
			},
			onHide: function() {
				$('done_message').set('text', langTextArray['msg_shared']);
			}
		},
		post: {
			id: 'post_page',
			onShow: function() {
				//				widget.addEvent('postToServiceNeedsMoreInfo', function(serviceTag, message) {
				//	widget.popModalWorkingSheet();
				//	widget.displayNotification(message);
				//	});
				
				$('privacyLink').removeClass('hidden');
			},
			onHide: function() {
				$('privacyLink').addClass('hidden');
				this.parent();
			},
			onReady: function() {
				widget.addEvent('postToServiceRequested', function(serviceTag) {
					widget.pushModalWorkingSheet(
						'<span class="' + serviceTag + '">'+langTextArray['msg_posting_share'] + ' '+ widget.services[serviceTag].title + '</span>'
					);
				});
				widget.addEvent('postToServiceSucceeded', function(serviceTag) {
					logEvent(serviceTag, 'post');
					widget.popModalWorkingSheet();
					widget.showPage('done');
				});
				widget.addEvent('postToServiceFailed', function(serviceTag, error) {
					widget.popModalWorkingSheet();
					widget.pushModalErrorSheet(error);
				});
				this.parent();
			},
			pages: {
				blogger: {
					id: 'post_template',
					desc: '',
					onReady: function() {
						this.parent();
					},
					onShow: function() {
						$('post_desc').set('html', langTextArray['msg_posting_blog']);
						widget.addEvent('postToServiceNeedsMoreInfo', function(serviceTag, message, data) {
							var blogname = [];
							var blogid = [];
							var blOptions = '';
							for (var i = 0; i < data.blogs.length; i++) {
								blogname.push(data.blogs[i].blogname);
								blogid.push(data.blogs[i].blogid);
							}
							blOptions = '<select id="post_select">';
							for (i = 0; i < blogname.length; i++) {
								blOptions += '<option value="' + blogid[i] + '">' + blogname[i] + '</option>';
							}
							blOptions += '</select>';
							this.freezeTextInput($('post_username'));
							this.freezeTextInput($('post_password'));
							$('post_publish_btn').removeClass('hidden');
							$('post_draft_btn').addClass('hidden');
							$('post_submit_btn').addClass('hidden');
							$('post_select_container').set('html', blOptions);
							$('post_select_box').removeClass('hidden');
							widget.popModalWorkingSheet();
							widget.displayNotification(message);
						});

						$('post_draft_btn').addEvent('click', function(){
							gaLog("Post", "draft_btn_click", "blogger"); 
							setGlobals("glo_bloggerDraft",0);
							widget.postBlogger();
						});
						$('post_submit_btn').addEvent('click', function(){
							gaLog("Post", "post_btn_click", "blogger"); 
							setGlobals("glo_bloggerDraft",1);
							widget.postBlogger(); 
						});
						$('post_publish_btn').addEvent('click', function(){
							gaLog("Post", "submit_btn_click", "blogger"); 
							widget.postBlogger(); 
						});
						this.post_bindReturnKeyToSubmission('blogger');
					},
					submitForm: function() {
						setGlobals("glo_bloggerDraft",1);
						widget.postBlogger();
					}
				},
				livejournal:{
					id: 'post_template',
					desc: '',
					onReady: function() {
					},
					onShow: function() {
						$('post_desc').set('html', langTextArray['msg_lovejournal']);
						$('post_message').value=langTextArray['msg_optional'];
						$('post_message').addEvent('focus',function(){
							if($('post_message').value===langTextArray['msg_optional']){$('post_message').value="";}
						});
						$('post_submit_btn').addEvent('click', function(){
							gaLog("Post", "post_btn_click", "livejournal"); 
							widget.postLive_journal(); 
						});
						this.post_bindReturnKeyToSubmission('livejournal');
						this.parent();
					},
					submitForm: function() {
						widget.postLive_journal();
					}
				},
				twitter: {
					id: 'post_template',
					desc: '',
					statusMessage: null,
					onReady: function() {
						widget.addEvent('twitterClicked', (function() {
							var tempTitle=glo_title;
							try{tempTitle=decodeURIComponent(glo_title);}catch(err){}
							this.statusMessage = tempTitle + ' - ' + getSharURL();
							var i=0;
							while ( this.statusMessage.length > 140 ) {
								this.statusMessage = glo_title.substr(0, glo_title.length - i++) + '... ' + getSharURL();
							}
							$('post_message').value = this.statusMessage;
							
							this.updateCharacterCounter();
						}).bind(this));
						this.parent();
					},
					onShow: function() {
						$('post_desc').set('html', langTextArray['post_twitter']);
						$('post_message').addEvent('keypress',(function(event){
							event = new Event(event);
							var retval = true;
							var inputField = $('post_message');
							var maxCharacterCount = 140;
							var counterSpan = $('post_character_counter');
							if( 'enter' === event.key && status.length <= 140) {
								widget.postTwitter();
							}
							else {
								retval = widget.limitCharacters(inputField, maxCharacterCount, counterSpan, event);
							}
							return retval;
						}).bind(this));
						$('post_message').addEvent('keyup',(function(){
							this.statusMessage = $('post_message').value;
							this.updateCharacterCounter();
						}).bind(this));
						$('post_submit_btn').addEvent('click', function(){
							gaLog("Post", "post_btn_click", "twitter"); 
							widget.postTwitter(); 
						});
						this.post_bindReturnKeyToSubmission('twitter');

						$('post_message').value = this.statusMessage;
						this.updateCharacterCounter();
						createSharURL(glo_url);
					},
					updateCharacterCounter: function() {
						$('post_character_counter').set('html', 140 - $('post_message').value.length);
					},
					submitForm: function() {
						widget.postTwitter();
					}
				},
				typepad: {
					id: 'post_template',
					desc: '',
					onReady: function() {
						this.parent();
					},
					onShow: function() {
						$('post_desc').set('html', langTextArray['msg_post_typepad']);
						widget.addEvent('postToServiceNeedsMoreInfo', function(serviceTag, message, data) {
							if (serviceTag == 'typepad') {
								var blogname = [];
								var blogid = [];
								var tpOptions = "";
								for (var i = 0; i < data.blogs.length; i++) {
									blogname.push(data.blogs[i].blogName);
									blogid.push(data.blogs[i].blogid);
								}
								tpOptions = "<select id='post_select'>";
								for (i = 0; i < blogname.length; i++) {
									tpOptions += '<option value="' + blogid[i] + '">' + blogname[i] + '</option>';
								}
								tpOptions += "</select>";
								this.freezeTextInput($('post_username'));
								this.freezeTextInput($('post_password'));
								$('post_publish_btn').removeClass('hidden');
								$('post_draft_btn').addClass('hidden');
								$('post_submit_btn').addClass('hidden');
								$('post_select_container').set('html', tpOptions);
								$('post_select_box').removeClass('hidden');
								widget.popModalWorkingSheet();
								widget.displayNotification(message);
							}
						});

						$('post_draft_btn').addEvent('click', function(){
							gaLog("Post", "draft_btn_click", "typepad"); 
							setGlobals("glo_tpDraft",0);
							widget.postTypePad();
						});
						$('post_submit_btn').addEvent('click', function(){
							gaLog("Post", "post_btn_click", "typepad"); 
							setGlobals("glo_tpDraft",1);
							widget.postTypePad(); 
						});
						$('post_publish_btn').addEvent('click', function(){
							gaLog("Post", "submit_btn_click", "typepad"); 
							widget.postTypePad(); 
						});
						this.post_bindReturnKeyToSubmission('typepad');
					},
					submitForm: function() {
						setGlobals("glo_tpDraft",0);
						widget.postTypePad();
					}
				},
				wordpress: {
					id: 'post_template',
					desc: '',
					onReady: function() {
						this.parent();
					},
					onShow: function() {
						$('post_desc').set('html', langTextArray['msg_post_wordpress']);
						$('post_submit_btn').addEvent('click', function() {
							gaLog("Post", "post_btn_click", "wordpress"); 
							widget.postWordpress(); 
						});
						this.post_bindReturnKeyToSubmission('wordpress');
					},
					submitForm: function() {
						widget.postWordpress();
					}
				}
			}
		}
	},

	pageHistory: [],
	_currentPage: null,
	
	showSharebox: function() {

	},
	
	/**
	 * @param 	string|array path: pipe-delimited path to the page to show using 
	 * 			property names in structure above. eg, 'post/wordpress' or just 'done'. 
	 * 			during internal recursion the argument is an array, so that'll work too.
	 * @param	[object obj]: only used during internal recursion.
	 */
	showPage: function(path, obj, isPost) {
		if( path == null) {
			return;
		}
		if (!obj && path != this.pageHistory.getLast()) { 
			this.pageHistory.push(path); 
		}
		path = (typeof path == 'string' ? path.split('|') : path);
		obj = (obj ? obj : widget);
		var page = path.shift();
		if(page == "null") {
			return;
		}
		if( isPost ) {
			this.fireEvent('pageShown', obj.pages[page]);
		} else {
			for (var name in obj.pages) {
				if (name == page) {
					$(obj.pages[name].id).removeClass('hidden');
					this.fireEvent('pageShown', obj.pages[name]);
					this._currentPage = obj.pages[name];
				}
				else {
					if (!$(obj.pages[name].id).hasClass('hidden')) {
						$(obj.pages[name].id).addClass('hidden');
						this.fireEvent('pageHidden', obj.pages[name]);
					}
				}
			}
		}
		if (path.length) {
			if(page == 'post'){
				this.initPost(path[0]);
				this.showPage(path, obj.pages[page], true);
			} else {
				this.showPage(path, obj.pages[page], false);
			}
		}
	},
	initPost: function(page) {
		glo_post_page = page;
		$('post_url').set('value', '');
		$('post_username').set('value', '');
		$('post_password').set('value', '');
		$('post_message').set('value', '');
		$('post_remember_me').checked = false;
		$('post_forget_me').checked = false;
		this.unfreezeTextInput($('post_url'));
		this.unfreezeTextInput($('post_username'));
		this.unfreezeTextInput($('post_password'));
		$('post_url').disabled = false;
		$('post_username').disabled = false;
		$('post_password').disabled = false;

		$('post_message').removeEvents('focus');
		$('post_message').removeEvents('keypress');
		$('post_message').removeEvents('keyup');
		widget.removeEvents('postToServiceNeedsMoreInfo');
		
		if(document.getElementById('post_select')){
			$('post_select_container').empty();
		}

		var postElements = new Array( "post_url_box", "post_message_box", "post_character_counter_div", "post_select_box", 
									  "post_draft_btn", "post_submit_btn", "post_publish_btn" );
		postElements.each( function(item) {
			if( !$(item).hasClass('hidden') ) {
				$(item).addClass('hidden');
			}
		});

		var postBtns = new Array( "post_submit_btn", "post_draft_btn", "post_submit_btn" );
		postBtns.each( function(item) {
			$(item).removeEvents('click');
		});

		$('post_title').set('html', widget.services[page].title);
		$('post_title').set('class', page);

		$('post_desc').set('html', widget.pages.post.pages[page].desc);

		switch(page) {
		case 'twitter':
			$('post_message_label').set('html', langTextArray['email_message']);
			$('post_character_counter_div').removeClass('hidden');
			$('post_message_box').removeClass('hidden');
			$('post_submit_btn').removeClass('hidden');
			break;
		case 'livejournal':
			$('post_message_label').set('html', langTextArray['msg_comment']);
			$('post_message_box').removeClass('hidden');
			$('post_submit_btn').removeClass('hidden');
			break;
		case 'blogger':
			$('post_draft_btn').removeClass('hidden');
			$('post_submit_btn').removeClass('hidden');
			break;
		case 'wordpress':
			$('post_url_box').removeClass('hidden');
			$('post_submit_btn').removeClass('hidden');
			break;
		case 'typepad':
			$('post_draft_btn').removeClass('hidden');
			$('post_submit_btn').removeClass('hidden');
			break;
		}

		populateSavedCredentials(page);
	},

	showPreviousPage: function() {
		if (this.pageHistory.length > 1) {
			this.pageHistory.pop();
			this.showPage(this.pageHistory.getLast());
		}
	},
	hideOverlays: function() {
		$$('.overlay').addClass('hidden');
	},
	initializePages: function(obj) {
		var result = {};
		for (var pageName in obj) {
			var pageClass = new Class($extend({ Extends: Widget.Page }, obj[pageName]));			// the cheese! it burns!
			result[pageName] = new pageClass(this);
			this.addEvent('pageShown', (function(enclosedPage) { return function(shownPage) {
				if (shownPage == enclosedPage) {
					shownPage.onShow();
				}
			}; })(result[pageName]), true);
			this.addEvent('pageHidden', (function(enclosedPage) { return function(hiddenPage) {
				if (hiddenPage == enclosedPage) {
					hiddenPage.onHide();
				}
			}; })(result[pageName]), true);
		}
		return result;
	},
	
	// recursively calls ready
	readyPages: function(obj) {
		for (var pageName in obj) {
			obj[pageName].onReady();
			if ('pages' in obj[pageName]) {
				this.readyPages(obj[pageName].pages);
			}
		}
	},
	
	getCurrentPage: function() {
		return this._currentPage;
	},
	getCurrentTopLevelPage: function() {
		for (var name in widget.pages) {
			if (widget.pages[name].isShown()) {
				return widget.pages[name];
			}
		}
		return null;
	},
	
	_lastFocus: null,
	captureLastFocus: function(target) {
		this._lastFocus = target;
	},
	getCurrentFocus: function() {
		return this._lastFocus;
	},
	
	_modalCovers: [],
	_workingCovers: [],
	displayNotification: function(text) {
		var elementToCover = widget.getCurrentTopLevelPage().domContainer;
		var cover = new Element('div', { 'class': 'notification_sheet collapsed' });
		cover.grab(new Element('div', { style: 'font-weight:bold; text-align:center; margin:5px auto; width:337x' }).set('html', text));
		elementToCover.grab(cover);
		setTimeout(function() {
			cover.get('tween').addEvent('complete', function() {
				// this callback is not happening, for some reason
				cover.destroy();
			});
			cover.set('tween', { duration: 'normal' });
			cover.tween('height', '0px');
			setTimeout(function() { cover.destroy(); }, 1000);	// wtf?
		}, 2500);
		cover.set('tween', { duration: 'short' });
		cover.tween('height', '30px');
	},
	pushModalSheet: function(message, type, callback) {
		var widgetContainer = $('container');
		var cover = new Element('div', { 'class': 'modal_sheet faded_out' });
		var smoke = new Element('div', { 'class': 'smoke'});
		var errorContainer = new Element('div', { 'class': 'modal_message_container'});
		var height = widgetContainer.getSize().y - (2 * parseInt(widgetContainer.getStyle('border-width')));
			/*
		if(glo_widget_info.require_captcha || widget.getCurrentPage().id === 'send_page') {
			height += 129;
		}
			*/
		cover.grab(smoke);
		cover.setStyles('height', height);
		smoke.setStyle('height', height);
		errorContainer.grab(new Element('div', { 'class': type }).set('html', message));
		//<div id="btnShareSend" class="button_new_green"><span>Send</span></div>
		//errorContainer.grab(new Element('input', { 'class': 'button', 'type': 'button', value:'OK' }).addEvent('click', function(event) {
		errorContainer.grab(new Element('div', { 'class': 'button_new', 'html': '<span>'+langTextArray['btn_ok']+'</span>' }).addEvent('click', function(event) {
			widget.popModalSheet();
			if (typeof callback == 'function') {
				callback();
			}
			event.stop();
		}));
		errorContainer.setStyles({
			height: height,
			top: '40px'
		});
		widgetContainer.grab(cover.grab(errorContainer));
		cover.set('tween', { duration: 'short' });
		var currentFocus = this.getCurrentFocus();
		if (currentFocus) {
			currentFocus.blur();
		}
		var keyHandler = function(event) {
			if (event.key == 'enter' || event.key == 'esc') {
				widget.popModalSheet();
			}
		};
		cover.get('tween').addEvent('complete', function() {
			document.addEvent('keyup', keyHandler);
		});
		this._modalCovers.push({ latentFocus: currentFocus, cover: cover, keyHandler: keyHandler });
		cover.fade('hide').fade('in');
	},
	popModalSheet: function() {
		if (this._modalCovers.length) {
			var obj = this._modalCovers.pop();
			document.removeEvent('keyup', obj.keyHandler);
			obj.cover.destroy();
			if (obj.latentFocus) {
				obj.latentFocus.focus();
			}
		}
	},
	pushModalErrorSheet: function(errorMessage, onOKClicked) {
		this.pushModalSheet(errorMessage, 'error', onOKClicked);
	},
	pushModalNotificationSheet: function(message, onOKClicked) {
		this.pushModalSheet(message, 'notification', onOKClicked);
	},
	popModalNotificationSheet: function() {
		this.popModalSheet();
	},
	popModalErrorSheet: function() {
		this.popModalSheet();
	},
	pushModalWorkingSheet: function(statusMessage) {
		var widgetContainer = $('container');
		var cover = new Element('div', { 'class': 'modal_sheet faded_out' });
		var smoke = new Element('div', { 'class': 'smoke'});
		var spinnerContainer = new Element('div', { 'class': 'spinner_container'});
		var height = widgetContainer.getSize().y - (2 * parseInt(widgetContainer.getStyle('border-width')));
			/*
		if(glo_widget_info.require_captcha || widget.getCurrentPage().id === 'send_page') {
			height += 129;
		}
			*/
		cover.grab(smoke);
		cover.setStyles('height', height);
		smoke.setStyle('height', height);
		spinnerContainer.grab(new Element('div', { 'class': 'working_text' }).set('html', statusMessage));
		spinnerContainer.grab(new Element('div', { 'class': 'loading32' }));
		spinnerContainer.setStyles({
			height: height
		});
		widgetContainer.grab(cover.grab(spinnerContainer));
		cover.set('tween', { duration: 'short' });
		cover.fade('hide').fade('in');
		this._workingCovers.push(cover);
	},
	popModalWorkingSheet: function() {
		if (this._workingCovers.length) {
			this._workingCovers.pop().destroy();
		}
	}
});

//services.js

Widget.implement({
	services: {
		aim: {
			title: 'AIM Share',
			submitUrl: 'http://connect.aim.com/share/?url={url}&title={title}',
			destination: 'aim.com'
		},
		sms: {
            title: 'Text',
			onClick: function(event) {
				$('send_title').set('html', langTextArray['msg_text']);
				widget.showPage('send');
				event.stop();
			},
            type: 'sms'
        },
		email: {
            title: 'Email',
			onClick: function(event) {
				//createSharURL(glo_url, true);
				$('send_title').set('html', langTextArray['email']);
				widget.showPage('send');
				event.stop();
			},
            type: 'email'
        },
        sharebox: {
            title: 'Save',
			onClick: function(event) { 
				if (widget.userIsSignedIn()) {
					widget.fireEvent('shareableURLChanged', glo_thumb);
					widget.showPage('sharebox');
				} else {
					widget.showPage('register');
					widget.displayNotification(langTextArray['msg_must_register']);
					var signedIn = leftPage = null;
					signedIn = function() {
						widget.fireEvent('shareableURLChanged', glo_thumb);
						widget.showPage('sharebox');
						widget.removeEvent('signInComplete', signedIn);
						widget.removeEvent('pageHidden', leftPage);
					};
					leftPage = function(page) {
						widget.removeEvent('signInComplete', signedIn);
						widget.removeEvent('pageHidden', leftPage);
					};
					widget.addEvent('signInComplete', signedIn);
					// if we leave the reg page without registering/logging in, forget that we were going to the sharebox
					widget.addEvent('pageHidden', leftPage);
				}
				event.stop();
			},
            type: 'sharebox'
        },
		bebo: {
			title: 'Bebo',
			submitUrl: 'http://www.bebo.com/c/share?Url={url}&Title={title}',
			destination: 'bebo.com'
		},
		blinklist: {
			title: 'Blinklist',
			submitUrl: 'http://blinklist.com/index.php?Action=Blink/addblink.php&Url={url}&Title={title}',
			destination: 'blinklist.com'
		},
		blogmarks: {
			title: 'Blogmarks',
			submitUrl: 'http://blogmarks.net/my/new.php?mini=1&url={url}&title={title}',
			destination: 'blogmarks.net'
		},
		blogger:  {
			title: 'Blogger',
			onClick: function(event) { widget.showPage('post|blogger'); event.stop(); },
			type: 'post'
		},
		bus_exchange: {
			title: 'Add to BX',
			submitUrl: 'http://bx.businessweek.com/api/add-article-to-bx.tn?url={url}',
			destination: 'bx.businessweek.com',
			aTitle:'Business Exchange'
		},
		care2: {
			title: 'Care2',
			submitUrl: 'http://www.care2.com/news/compose?share[link_url]={url}&share[title]={title}',
			destination: 'care2.com'
		},
		current: {
			title: 'Current',
			submitUrl: 'http://current.com/clipper.htm?url={url}&title={title}&src=st',
			destination: 'current.com'
		},
		dealsplus: {
			title: 'Dealspl.us',
			submitUrl: 'http://dealspl.us/add.php?ibm=1&url={url}',
			destination: 'dealspl.us'
		},
		delicious: {
			title: 'Delicious',
			submitUrl: 'http://delicious.com/save?url={url}&title={title}',
			destination: 'del.icio.us'		
		},
		digg: {
			title: 'Digg',
			submitUrl: 'http://digg.com/submit?phase=2&url={url}&title={title}',
			destination: 'digg.com'
		},
		diigo: {
			title: 'Diigo',
			submitUrl: 'http://secure.diigo.com/post?url={url}&title={title}',
			destination: 'secure.diigo.com'
		},
		facebook: {
			title: 'Facebook',
			submitUrl: 'http://www.facebook.com/sharer.php?u={url}&t={title}',
			destination: 'facebook.com'
		},
		fark: {
			title: 'Fark',
			submitUrl: 'http://cgi.fark.com/cgi/farkit.pl?u={url}&h={title}',
			destination: 'cgi.fark.com'
		},
		faves: {
			title: 'Faves',
			submitUrl: 'http://www.faves.com/Authoring.aspx?u={url}&t={title}',
			destination: 'faves.com'
		},
		fresqui: {
			title: 'Fresqui',
			submitUrl: 'http://fresqui.com/enviar?url={url}&title={title}',
			destination: 'ocio.fresqui.com'
		},
		friendfeed: {
			title: 'FriendFeed',
			submitUrl: 'http://friendfeed.com/share?url={url}&title={title}',
			destination: 'friendfeed.com'
		},
		funp: {
			title: 'Funp',
			submitUrl: 'http://funp.com/pages/submit/add.php?title={title}&url={url}&via=tools',
			destination: 'funp.com'
		},
		google_bmarks: {
			title: 'G Bookmarks',
			submitUrl: 'http://www.google.com/bookmarks/mark?op=edit&bkmk={url}&title={title}',
			destination: 'google.com'
		},
		hi5: {
			title: 'Hi5',
			onClick: function(event) { widget.showPage('post|hi5'); event.stop(); },
			type: 'post'
		},
		kirtsy: {
			title: 'Kirtsy',
			submitUrl: 'http://www.kirtsy.com/submit.php?url={url}',
			destination: 'kirtsy.com'
		},
		linkedin: {
			title: 'LinkedIn',
			submitUrl: 'http://www.linkedin.com/shareArticle?mini=true&url={url}&title={title}&summary=&source=',
			destination: 'linkedin.com'
		},
		livejournal: {
			title: 'LiveJournal',
			onClick: function(event) { widget.showPage('post|livejournal'); event.stop(); },
			type: 'post'
		},
		/*
		  magnolia: {
		  title: 'Magnolia',
		  submitUrl: 'http://ma.gnolia.com/bookmarklet/add?url={url}&title={title}',
		  destination: 'ma.gnolia.com'
		  },
		*/
		meneame: {
			title: 'Meneame',
			submitUrl: 'http://meneame.net/submit.php?url={url}',
			destination: 'meneame.net'
		},
		mister_wong: {
			title: 'Mr Wong',
			submitUrl: 'http://www.mister-wong.com/index.php?action=addurl&bm_url={url}&bm_description={title}',
			destination: 'mister-wong.com'
		},
		mixx: {
			title: 'Mixx',
			submitUrl: 'http://www.mixx.com/submit?page_url={url}',
			destination: 'mixx.com'
		},
		myspace: {
			title: 'MySpace',
			submitUrl: 'http://www.myspace.com/Modules/PostTo/Pages/?l=3&u={url}&t={title}&c={content}%3Cp%3EPowered+by+%3Ca+href%3D%22http%3A%2F%2Fsharethis.com%22%3EShareThis%3C%2Fa%3E%3C%2Fp%3E',
			destination: 'myspace.com'
		},
		n4g: {
			title: 'N4G',
			submitUrl: 'http://www.n4g.com/tips.aspx?url={url}&title={title}',
			destination: 'ng4.com'
		},
		newsvine: {
			title: 'Newsvine',
			submitUrl: 'http://www.newsvine.com/_tools/seed&save?popoff=0&u={url}&h={title}',
			destination: 'newsvine.com'
		},
		oknotizie: {
			title: 'Oknotizie',
			submitUrl: 'http://oknotizie.alice.it/post?url={url}&title={title}',
			destination: 'oknotizie.alice.it'
		},
		propeller: {
			title: 'Propeller',
			submitUrl: 'http://www.propeller.com/submit/?U={url}&T={title}',
			destination: 'propeller.com'
		},
		reddit: {
			title: 'Reddit',
			submitUrl: 'http://reddit.com/submit?url={url}&title={title}',
			destination: 'reddit.com'
		},
		slashdot: {
			title: 'Slashdot',
			submitUrl: 'http://slashdot.org/bookmark.pl?url={url}&title={title}',
			destination: 'slashdot.org'
		},
		simpy: {
			title: 'Simpy',
			submitUrl: 'http://www.simpy.com/simpy/LinkAdd.do?href={url}&title={title}',
			destination: 'simpy.com'
		},
		sphinn: {
			title: 'Sphinn',
			submitUrl: 'http://sphinn.com/index.php?c=post&m=submit&link={url}',
			destination: 'sphinn.com'
		},
		stumbleupon: {
			title: 'Stumbleupon',
			submitUrl: 'http://www.stumbleupon.com/submit?url={url}&title={title}',
			destination: 'stumbleupon.com'
		},
		technorati: {
			title: 'Technorati',
			submitUrl: 'http://www.technorati.com/faves?add={url}',
			destination: 'technorati.com',
			dontUseEncodedURL: 'Encoded URLs are not allowed'
		},
		twackle: {
			title: 'Twackle',
			submitUrl: 'http://www.twackle.com/chicklet?site={url}',
			destination: 'twackle.com'
		},
		twine: {
			title: 'Twine',
			submitUrl: 'http://www.twine.com/bookmark/basic?u={url}',
			destination: 'twine.com'
		},
		twitter: {
			title: 'Twitter',
			onClick: function(event) {
				page = widget.pages.home;
				createSharURL(glo_url, true);
				//createSharURL(glo_url, false);
				widget.fireEvent('twitterClicked', glo_url);
				widget.showPage('post|twitter');
				event.stop();
			},
			type: 'post',
			useSharURL: 'Shar URLs are allowed'
		},
		typepad:  {
			title: 'TypePad',
			onClick: function(event) { widget.showPage('post|typepad'); event.stop(); },
			type: 'post'
		},
		windows_live: {
			title: 'Live',
			submitUrl: 'https://favorites.live.com/quickadd.aspx?marklet=1&mkt=en-us&url={url}&title={title}&top=1',
			destination: 'favorites.live.com'
		},
		wordpress:  {
			title: 'WordPress',
			onClick: function(event) { widget.showPage('post|wordpress'); event.stop(); },
			type: 'post'
		},
		xanga: {
			title: 'Xanga',
			submitUrl: 'http://www.xanga.com/private/editorx.aspx?t={title}&u={url}&s={content}',
			destination: 'xanga.com'
		},
		yahoo_bmarks: {
			title: 'Y! Bookmarks',
			submitUrl: 'http://bookmarks.yahoo.com/toolbar/savebm?opener=tb&u={url}&t={title}',
			destination: 'bookmarks.yahoo.com'
		},
		ybuzz: {
			title: 'Buzz Up!',
			submitUrl: 'http://buzz.yahoo.com/buzz?targetUrl={url}&headline={title}&src=sharethis',
			destination: 'buzz.yahoo.com'
		},
		yigg: {
			title: 'Yigg',
			submitUrl: 'http://www.yigg.de/neu?exturl={url}&exttitle={title}',
			destination: 'yigg.de'
		}
	},
	contactSources: {
		aim: {
			title: 'AIM Share',
			protocolName: 'aim'
		},
		aol: {
			title: 'AOL',
			protocolName: 'aol'
		},
		gmail: {
			title: 'GMail',
			protocolName: 'gmail'
		},
		hotmail: {
			title: 'Hotmail',
			protocolName: 'hotmail'
		},
		msn: {
			title: 'MSN',
			protocolName: 'hotmail'
		},
		twitter: {
			title: 'Twitter',
			protocolName: 'twitter'
		},
		yahoo: {
			title: 'Yahoo!',
			protocolName: 'yahoo'
		},
		facebook: {
			title: 'Facebook',
			protocolName: 'facebook'
		}
	},
	loginSources: {
		yahoo: {
			title: 'Yahoo!',
			protocolName: 'yahoo'
		},
		sharethis: {
			title: 'ShareThis',
			protocolName: 'sharethis'
		}
	}
});

Widget.implement({
	getServiceType: function(serviceTag) {
		if ('type' in this.services[serviceTag]) {
			return this.services[serviceTag].type;
		}
		return 'web';
	},
	getServiceLink: function(serviceTag) {
		var service = this.services[serviceTag];
		if(!service){
			return;
		}
		var link = 'javascript:void(0)';
		var aTitle='';
		
		if(service.aTitle){
			aTitle=service.aTitle;
		}
		
		if ('submitUrl' in service && service.submitUrl.length) {
			var serviceURL = service.useSharURL ? getSharURL() : addHashTracking(glo_url);
			
			if (service.dontUseEncodedURL) {
				link = service.submitUrl.replace('{title}', glo_title).replace('{url}', serviceURL).replace('{content}', glo_content);				
			} else {
				link = service.submitUrl.replace('{title}', encodeURIComponent(glo_title)).replace('{url}', encodeURIComponent(serviceURL)).replace('{content}', glo_content);
			}
		}
		var a = new Element('a', {
			'class': serviceTag,
			rel: 'external',
			style: 'color:' + glo_linkfg + ';',
			target: (glo_options_popup ? '_top' : ('linkTarget' in service ? service.linkTarget : '_blank')),
			href: link,
			title:aTitle,
			id: 'post_'+serviceTag+'_link'
		});
		a.addEvent('click', (function(event) {
			gaLog("Home", "chicklet_click", serviceTag);
			if ('destination' in service) {
				logSW(service.destination);
				if (widget.user) {
					widget.user.logServiceUse(serviceTag);
				}
			}
			if ('onClick' in service) {
				service.onClick.bind(service)(event, link);
			}
			else{
				widget.showPage('done'); //show done screen on click of service
			}
			if (link != 'javascript:void(0)') {
				if (glo_options_popup == true) {
					popupOpen2(this);
					event.stop();
				}
			}
		}).bind(a));

		a.appendText(service.title);
		return a;
	},
	getDummyServiceLink: function() {
		var a = new Element('a', {
			href: 'javascript:void(0);'
		});
		return a.appendText(' ');
	}
});

//contact.js

Widget.Contact = new Class(
	{ Implements: Events,
	  service: '',
	  name: '',
	  address: '',
	  id: '',
	  selected: false,
	  added: false,
	  widget: null,
	  inventName: false,
	  _isAContact: true,	// crappy duck-typing ... mootools' class inheritance does not support instanceof
	  toString: function() {
		  // right now, supporting only sort by name and sort by service
		  var secondarySort = '';
		  if (widget.user.contactSortMode == 'name') {
			  secondarySort = this['service'];
		  }
		  else if (widget.user.contactSortMode == 'service') {
			  secondarySort = this['name'];
		  }
		  return (this[widget.user.contactSortMode] + ' - ' + secondarySort).toLowerCase();
	  },
	  initialize: function(widget, props) {
		  this.widget = widget;
		  for (var p in props) {
			  if (props[p]) {
				  this[p] = props[p];
			  }
		  }
	  },
	  select: function() {
		  this.selected = true;
		  this.fireEvent('selected', true);
	  },
	  deselect: function() {
		  this.selected = false;
		  this.fireEvent('selected', false);
	  },
	  toggleSelect: function(){
		  if(this.selected==false){
			  this.select();
		  }
		  else if(this.selected==true){
			  this.deselect();
		  }
	  },
	  
	  setName: function(text) {
		  this.name = text;
		  this.fireEvent('nameChanged', this.name);
	  },
	  setAddress: function(text) {
		  this.address = text;
		  this.fireEvent('addressChanged', this.address);
	  },
	  setService: function(text) {
		  this.service = text;
		  this.fireEvent('serviceChanged', this.service);
	  },
	  // compares address and service
	  isEqualTo: function(obj) {
		  if (obj.address && obj.service) {
			  return obj.address.toLowerCase() == this.address.toLowerCase() && obj.service == this.service;
		  }
		  return false;
	  }
	});

Widget.Contact.guessServiceForAddress = function(address, serviceSrcHint) {
	
	if (serviceSrcHint) {
		switch (serviceSrcHint) {
		case 'gmail':
			return 'email';
			break;
		case 'aim':
			return 'aim';
			break;
		case 'myspace':
			return 'myspace';
			break;
		case 'aol':
			return 'email';
			break;
		case 'msn':
			return 'email';
			break;
		case 'yahoo':
			//return serviceSrcHint;
			return 'email';
			break;
		case 'hotmail':
			//return 'msn';
			return 'email';
			break;
			break;
		}
	}

	// if no source hint
	if (!address.test(/[^\d\-\(\)]/)) {	// if there are only digits, dashes and parens
		return 'sms';
	}
	else if (address.indexOf('@') >= 0) {
		return 'email';
	}
	// ?
	return null;
}

Widget.Contact.generateNameFromAddress = function(address) {
	if (address.indexOf('@') != -1) {
		return address.substr(0, address.indexOf('@'));
	}
	else if (address.test(/[^\d\-\(\)]/)) {	// exclude if there are only digits, dashes and parens
		return address;
	}
	return '';
};

//carousel.js

Widget.Carousel = new Class(
	{ Implements: Events,
	  initialize: function(idOrElement, initialState) {
		  if ($type(idOrElement) == 'element') {
			  this.domContainer = $(idOrElement);
		  }
		  else if ($type(idOrElement) == 'string') {
			  this.domContainer = $(idOrElement);
		  }
		  var poppet = this;
		  $('fwd_arrow').addEvent('click', function(event) {
			  gaLog("Home", "carousel_fwd_arrow", "Carousel Forward Arrow"); 
			  if (poppet.getNumPages() > 1) {
				  poppet.advance();
			  }
			  event.stop();
		  });
		  $('back_arrow').addEvent('click', function(event) {
			  gaLog("Home", "carousel_back_arrow", "Carousel Backward Arrow"); 
			  if (poppet.getNumPages() > 1) {
				  poppet.rewind();
			  }
			  event.stop();
		  });

	  },
	  
	  autoSize: function(){
		  //console.log("autosize dummies are: "+this.totalDummies);
		  if(this.totalDummies>6){
			  this.nRows=2;
			  this.createPaginator();
			  this.domContainer.getElement('.view').setStyle('height', '44px');
		  }
	  },
	  
	  setDataSource: function(contents) {
		  this.data = contents;
		  this.page = 0;
		  this.paginatorExists=false;
		  // sort by user preference, then by publisher preference
		  this.data.sort(function(a, b) { 
			  if (a.hasUserPref && b.hasUserPref) {
				  return a.userIndex - b.userIndex;
			  }
			  if (a.hasUserPref) {
				  return -1;
			  }
			  if (b.hasUserPref) {
				  return 1;
			  }
			  if (a.hasPublisherPref && b.hasPublisherPref) {
				  return a.publisherIndex - b.publisherIndex;
			  }
			  if (a.hasPublisherPref) {
				  return -1;
			  }
			  if (b.hasPublisherPref) {
				  return 1;
			  }
			  return a.stIndex - b.stIndex;
		  });
		  this.render();
	  },
	  
	  setNumRows: function(rows) {
		  this.nRows = rows;
		  this.render();
	  },
	  
	  setNumColumns: function(cols) {
		  this.nCols = cols;
		  this.render();
	  },
	  
	  _buildPage: function(pageNum) {
		  //console.log("butilpage "+pageNum+1);
		  //console.log("build page");
		  if(this.paginatorExists==false){this.createPaginator()};
		  
		  this.autoSize();
		  //console.log(this.getVisibleData().length);
		  this.highlightNum(pageNum+1);
		  pageNum = this.getEffectivePageNum(pageNum);
		  var data = this.getVisibleData();
		  var groupDiv = new Element('div', { 'class': 'group' });
		  var itemsPerPage = (this.nRows * this.nCols);
		  for (var i = (pageNum * itemsPerPage); i < (pageNum * itemsPerPage) + itemsPerPage; i++) {
			  if (i < data.length) {
				  var element = data[i].getContent();
				  groupDiv.grab(element);
			  }
		  }
		  return groupDiv;
	  },
	  
	  getNumPages: function() {
		  var data = this.getVisibleData();
		  return Math.ceil(data.length / (this.nRows * this.nCols));
	  },
	  
	  getVisibleData: function() {
		  if (this.isShowingMore) {
			  return this.data;
		  }
		  else {
			  var data = [];
			  var itemsPerPage = (this.nRows * this.nCols);
			  var startsWithPref = (this.data.length && (this.data[0].hasPublisherPref ));
			  var dummiesDeployed = false;
			  this.totalDummies=0;
			  
			  for (var i = 0; i < this.data.length; i++) {
				  var previousHadPublisherPref = (i > 1) && (this.data[i - 1].hasPublisherPref);
				  var hasPref = this.data[i].hasPublisherPref || this.data[i].hasUserPref;
				  if (!this.isShowingMore && !dummiesDeployed && (i<itemsPerPage) && startsWithPref  && !hasPref) {
					  var j = i;
					  while(j < itemsPerPage) {
						  data.push({ 
							  isDummy: function() { return true; },
							  getContent: function() { return widget.getDummyServiceLink(); }
						  });
						  j++;
						  this.totalDummies++;
					  }
					  dummiesDeployed = true;
				  }
				  data.push(this.data[i]);
			  }

			  if( glo_exclusive_services && this.data.length < itemsPerPage ) {
				  var k = this.data.length;
				  while( k < itemsPerPage ) {
					  data.push({ 
						  isDummy: function() { return true; },
						  getContent: function() { return widget.getDummyServiceLink(); }
					  });
					  k++;
					  this.totalDummies++;
				  }
				  dummiesDeployed = true;
			  }

			  return data;
		  }
	  },
	  
	  getEffectivePageNum: function(pageNum) {
		  var data = this.getVisibleData();
		  var nPages = this.getNumPages();
		  if (pageNum < 0) {
			  pageNum = nPages + pageNum;
		  }
		  if (pageNum >= nPages) {
			  pageNum = pageNum % nPages;
		  }
		  return pageNum;
	  },
	  
	  render: function() {
		  if(this.paginatorExists==false){this.createPaginator()};
		  this.fireEvent('renderBegin');
		  var view = this.domContainer.getElement('div.view').getElement('div.groups');
		  $each(view.getChildren(), function(child) { 
			  child.dispose(); 
		  });
		  
		  view.grab(this._buildPage(this.page));
		  
		  this.fireEvent('renderComplete');
	  },
	  
	  createPaginator: function(){
		  var pages=this.getNumPages();
		  if( pages > 1 ) {
			  $('paginator').removeClass('hidden');
		  } else {
			  if( ! $('paginator').hasClass('hidden') ) {
				  $('paginator').addClass('hidden');
			  }
		  }
		  var div_size=pages*20 + 36;
		  div_size+="px";
		  $('paginator').setStyle('width', div_size);
		  var html= "";
		  for(var i=0;i<pages;i++){
			  var num=i+1;
			  html+='<div class="circles" onclick="gaLog(\'Home\', \'carousel_paging_dot\', \'Carousel Paging Dot\'); widget.carousel.goToPage('+num+');" title='+langTextArray['msg_goto']+' # '+num+'"></div> ';			
		  }
		  
		  $("circle_container").set('html',html);
		  this.highlightNum(this.page+1);
		  this.paginatorExists=true;
	  },
	  highlightNum: function(num){
		  var maxSize=this.getNumPages();
		  if(num>maxSize){
			  num=1;
		  }
		  if(num<1){
			  num=4;
		  }
		  var i=num-1;
		  if($('paginator')){
			  var a=$('circle_container').getChildren();
			  a.removeClass('circles-selected');
			  a.addClass('circles');
			  a[i].addClass('circles-selected');
			  a[i].removeClass('circles');
			  var pgInfo="("+num+"/"+this.getNumPages()+")";
		  }
	  },
	  goToPage: function(num){
		  this.page=num-1;
		  this.render();
		  this.highlightNum(num);
	  },
	  
	  advance: function() {
		  if (this.rotating) {
			  return;
		  }
		  this.rotating = true;
		  this.fireEvent('advanceBegin');
		  var groups = this.domContainer.getElement('div.view').getElement('div.groups');
		  var currentGroup = groups.getElement('div.group');
		  var nextGroup = this._buildPage(this.page + 1);
		  groups.grab(nextGroup);
		  
		  var deltaX = currentGroup.getSize().x;
		  var poppet = this;
		  groups.get('tween').removeEvents('complete').addEvent('complete', function() {
			  currentGroup.dispose();
			  groups.setStyle('left', 0);
			  poppet.page = poppet.getEffectivePageNum(poppet.page + 1);
			  poppet.fireEvent('advanceComplete');
			  poppet.rotating = false;
		  });
		  groups.tween('left', -deltaX);
	  },
	  
	  rewind: function() {
		  if (this.rotating) {
			  return;
		  }
		  this.rotating = true;
		  this.fireEvent('rewindBegin');
		  var groups = this.domContainer.getElement('div.view').getElement('div.groups');
		  var currentGroup = groups.getElement('div.group');
		  var prevGroup = this._buildPage(this.page - 1);

		  var deltaX = currentGroup.getSize().x;
		  groups.setStyle('left', -deltaX);
		  groups.grab(prevGroup, 'top');
		  
		  var poppet = this;
		  groups.get('tween').removeEvents('complete').addEvent('complete', function() {
			  currentGroup.dispose();
			  poppet.page = poppet.getEffectivePageNum(poppet.page - 1);
			  poppet.fireEvent('rewindComplete');
			  poppet.rotating = false;
		  });
		  groups.tween('left', 0);

	  },
	  
	  domContainer: null,
	  nRows: 4,
	  nCols: 3,
	  page: 0,
	  isShowingMore: false,
	  data: [],
	  rotating: false
	});
Widget.Carousel.initialState_more = 0;
Widget.Carousel.initialState_less = 1;

//user.js

Widget.User = new Class(
	{ Implements: Events,
	  email: '',
	  name: '',
	  authToken: '',
	  userID: '',
	  thirdparty_token: '',
	  shareHistory: {
		  services: {},
		  recipients: []
	  },
	  contacts: [],
	  contactsAD:[],
	  contactsEH:[],
	  contactsIL:[],
	  contactsMP:[],
	  contactsQT:[],
	  contactsUZ:[],
	  _selectedContacts: [],
	  contactSortMode: 'name',
	  contactServiceFilters: [],
	  widget: null,
	  credentials: [],
	  _deferringContactSelectionChangedTimer: -1,
	  
	  initialize: function(widget) {
		  this.widget = widget;
		  var history = widget.getCookie("ServiceHistory");
		  if (history) {
			  history = JSON.decode(history);
			  for (var name in history) {
				  this.setShareServiceHistory(history[name]);
			  }
		  }
	  },
	  setShareServiceHistory: function(item) {
		  this.shareHistory.services[item.service] = item;
	  },
	  getShareServiceHistory: function() {
		  var result = [];
		  for (var service in this.shareHistory.services) {
			  result.push($extend({}, this.shareHistory.services[service]));	// nb: peeling off a copy
		  }
		  result.sort(function(a, b) {
			  return b.count - a.count;
		  })
		  return result;
	  },
	  clearShareServiceHistory: function() {
		  this.shareHistory.services = {};
	  },
	  addRecentRecipient: function(data) {
		  // @todo: match with current contact list; use references to those. bleh.
		  this.shareHistory.recipients.push(data);
		  this.fireEvent('recipientHistoryChanged', { 'recipients': this.shareHistory.recipients });
	  },
	  getRecentRecipients: function() {
		  return this.shareHistory.recipients;
	  },
	  clearRecentRecipients: function() {
		  this.shareHistory.recipients = [];
	  },
	  isSignedIn: function() {
		  return this.authToken && this.authToken.length;
	  },
	  acquireAuth: function(authToken) {
		  if (this.authToken.length) {
			  this.signOut();
		  }
		  this.authToken = authToken;
		  widget.setCookie("ShareUT", this.authToken);
		  widget.setCookie('ServiceHistory', JSON.encode({}));
		  setGlobals("auth", authToken);
		  setGlobals("loggedIn", true);
		  this.fireEvent('signedIn');
		  this.getUserAccountInfo();
		  this.getContactsOnAccount();
	  },
	  signIn: function(username, password) {
		  var request = new Request({
			  method: "post",
			  url: "/api/getAuth_ws.php",
			  data: "username=" + username + "&password=" + password + "&return=json",
			  onFailure: function(){logError("signin","Ajax Failure");},
			  onSuccess: (function(responseText) {
				  try{var response = JSON.decode(responseText);}
				  catch(err){logError("signin",responseText);}
				  if (response.status === "SUCCESS") {
					  this.acquireAuth(response.data.token)
				  } 
				  else if (response.statusMessage === "INVALID_USERNAME_OR_PASSWORD") {
					  this.fireEvent('signInFailed', langTextArray['msg_incorrect_pass']);
				  }
				  else if (response.statusMessage === "DATABASE_FAILED") {
					  this.fireEvent('signInFailed', langTextArray['msg_server_connect_auth']);
					  logError("signin","unable to connect to db");
				  }
				  else {
					  this.fireEvent('signInFailed', langTextArray['msg_incorrect_pass']);
				  }
			  }).bind(this)
		  });
		  request.send();
		  this.fireEvent('signInRequested');
	  },
	  signOut: function() {
		  glo_credentials = [];
		  this.contacts = [];
		  this._selectedContacts = [];
		  this.clearShareServiceHistory();
		  this.clearRecentRecipients();
		  this.contactSortMode = 'name';
		  this.email = '';
		  this.name = '';
		  this.userID = '';
		  this.thirdparty_token = '';
		  this.fireEvent('contactsChanged');
		  this.fireEvent('contactSelectionChanged');
		  this.authToken = '';
		  glo_thirdparty_flag = '';
		  this.fireEvent('signedOut');
		  glo_isLoggedIn=false;
	  },
	  getUserAccountInfo: function() {
		  if (this.isSignedIn()) {
			  var request=new Request({
				  method: "post",
				  url: "/api/getUserInfo_ws.php",
				  data: "token=" + this.authToken + "&return=json",
				  onFailure: function(){logError("get user info","Ajax Failure");},
				  onSuccess: (function (responseText) {
					  try{var response = JSON.decode(responseText);}
					  catch(err){logError("get user info",responseText);}
					  if (response.status === "SUCCESS") {
						  setGlobals("userName", response.data.name);
						  setGlobals("userEmail", response.data.email);
						  this.name = response.data.name;
						  this.email = response.data.email;
						  this.userID = response.data.userID;
						  this.credentials = response.data.credentials;
						  this.thirdparty_token = response.data.thirdparty_token;
						  glo_thirdparty_flag = response.data.UserType;
						  
						  glo_credentials = this.credentials;
						  //populateSavedCredentials(this.credentials);
						  
						  if (response.data.socialShares) {
							  response.data.socialShares.each((function(serviceInfo) {
								  if (serviceInfo.name && serviceInfo.count) {
									  this.setShareServiceHistory({
										  service: serviceInfo.name.toLowerCase(),
										  count: parseInt(serviceInfo.count)
									  });
								  }
							  }).bind(this));
						  }
						  
						  if (response.data.recipients) {
							  response.data.recipients.each((function(recipient) {
								  this.addRecentRecipient(recipient);
							  }).bind(this));
						  }
						  this.fireEvent('infoChanged', this);
					  }
					  else {
						  logError("get user info",JSON.encode(response));
						  setTimeout((function() {
							  this.signOut();
						  }).bind(this), 1);
					  }
				  }).bind(this)
			  });
			  this.clearRecentRecipients();
			  this.clearShareServiceHistory();
			  request.send();
		  }

	  },
	  getContactsOnAccount: function() {
		  var request=new Request({
			  method: "post",
			  url: "/api/getContacts_ws.php",
			  data: "token=" + this.authToken + "&return=json",
			  onFailure: function(){logError("get contacts","Ajax Failure");},
			  onSuccess:(function(responseText,responseXML) {
				  //widget.pushProfiler('decoding response');
				  try{var response = JSON.decode(responseText);}
				  catch(err){logError("get contacts",responseText);}
				  //widget.popProfiler();
				  if (response.status === "SUCCESS" && response.data) {
					  var newContacts = response.data;
					  this.contacts = [];
					  this._selectedContacts = [];
					  this.addContactsLocally(newContacts);
					  this.fireEvent('getContactsSucceeded', newContacts);
				  } else {
					  logError("get contacts",JSON.encode(response));
					  this.fireEvent('getContactsFailed');
				  }
			  }).bind(this)
		  });
		  request.send();
		  this.fireEvent('getContactsRequested');
	  },
	  
	  addContactsToAccount: function(contacts) {
		  var c = encodeURIComponent(JSON.encode(contacts));
		  var request = new Request({
			  method: 'post',
			  url: '/api/addContacts_ws.php',
			  data: 'token=' + this.authToken + '&contacts=' + c + '&return=json',
			  onFailure: function(){logError("add contacts","Ajax Failure");},
			  onSuccess: (function(responseText) {
				  try{var response = JSON.decode(responseText);}
				  catch(err){logError("add contacts",responseText);}
				  if (response.status == 'SUCCESS') {
					  // wipe local contacts; retrieve from api
					  var f = null;
					  f = (function() {
						  this.fireEvent('addContactsToAccountSucceeded');
						  this.removeEvent('getContactsSucceeded', f);
					  }).bind(this);
					  this.addEvent('getContactsSucceeded', f);
					  this.getContactsOnAccount();
				  }
				  else {
					  logError("add contacts",JSON.encode(response));
					  this.fireEvent('addContactsToAccountFailed');
				  }
			  }).bind(this)
		  });
		  this.fireEvent('addContactToAccountRequested');
		  request.send();
	  },
	  
	  addContactsLocally: function(newContacts) {
		  var result = [];
		  for (var i = 0; i < newContacts.length; i++) {
			  var signedIn = this.isSignedIn();
			  var alreadyPresent = false;
			  if (!signedIn) {
				  // de-dupe added contacts when not signed in. 
				  // the API is the source of truth when signed in, and it will de-dupe for us.
				  for (var j = 0; j < this.contacts.length; j++) {
					  if (this.contacts[j].isEqualTo(newContacts[i])) {
						  alreadyPresent = true;
						  break;
					  }
				  }
			  }
			  if (!alreadyPresent || signedIn) {
				  var c = null;
				  if (newContacts[i]._isAContact) {	// ick.
					  c = newContacts[i];
					  var idd=(new Date()).getTime();
					  idd+=Math.random();
					  if(c.id==""){
						  c.id=idd;
					  }
				  }
				  else {
					  var props = newContacts[i];
					  var idd=(new Date()).getTime();
					  idd+=Math.random();
					  
					  c = new Widget.Contact(this, {
						  service: props.service ? props.service.toLowerCase() : 'email',	// ??
						  name: props.name ? props.name : '',
						  address: props.address ? props.address : '',
						  id: props.id ? props.id : idd
					  });
				  }
				  
				  // listen for selection events
				  c.addEvent('selected', (function (enclosedContact) { return (function(isSelected) {
					  if (isSelected) {
						  this._selectedContacts.include(enclosedContact);
						  if( (!glo_widget_info.session_token || this._selectedContacts.length > 5) && !glo_widget_info.require_captcha) {
							  glo_widget_info.require_captcha = 1;
							  if(typeof(Recaptcha)=="undefined"){
								odjs("http://api.recaptcha.net/js/recaptcha_ajax.js",function(){Recaptcha.create("6Leh9AIAAAAAAHYpIPrfzeRFMrHC2QysVPB93Qhh", "captcha");});
							  }else{
								  Recaptcha.create("6Leh9AIAAAAAAHYpIPrfzeRFMrHC2QysVPB93Qhh","captcha");
							  }

							  if(!glo_widget_info.session_token) {
								  gaLog("Send", "captcha_shown", "no session token"); 
							  }
							  if(this._selectedContacts.length > 5) {
								  gaLog("Send", "captcha_shown", "too many recipients"); 
							  }
						  }
					  }
					  else {
						  this._selectedContacts.erase(enclosedContact);
					  }
					  widget.deferWhile('contactSelectionIsChanging', (function() {
						  this.fireEvent('contactSelectionChanged');
					  }).bind(this));
				  }).bind(this); }).bind(this)(c));	// peel off a local contact reference for the closure
				  this.contacts.push(c);
				  result.push(c);
				  setGlobals("contProto", newContacts[i].service ? newContacts[i].service.toUpperCase() : 'email');
				  setGlobals("contName", newContacts[i].name);
				  setGlobals("contAdd", newContacts[i].address);
			  }
		  }
		  //this._sortContacts();
		  this.fireEvent('contactsChanged');
		  return result;
	  },
	  setContactSortMode: function(mode) {
		  this.contactSortMode = mode;
	  },
	  /**
	   * @todo: multiple linear searches = slow.
	   */
	  searchContacts: function(matchText) {
		  var results = [];
		  if (!matchText.length) {
			  return results;
		  }
		  var fs = [searchName, searchName2, searchName3, searchAdd, searchName4, searchName5];
		  if (typeof matchText == 'number') {
			  results.combine(searchAdd(matchText))
		  }
		  for (var i = 0; i < fs.length && results.length < widget.maxContactSearchResults; i++) {
			  results.combine(fs[i](matchText));
		  }
		  if (this.contactServiceFilters.length) {
			  results = results.filter((function(index) { 
				  return this.contactServiceFilters.indexOf(this.contacts[index].service) >= 0;
			  }).bind(this));
		  }
		  return results.map((function(index) { return this.contacts[index]; }).bind(this));
	  },

	  /**
	   * For now just does a linear search.
	   * @todo: could be improved with some work.
	   */
	  searchContactsExact: function(service, address) {
		  var nContacts = this.contacts.length;
		  for (var i = 0; i < nContacts; i++) {
			  if (this.contacts[i].service == service && this.contacts[i].address == address) {
				  return this.contacts[i];
			  }
		  }
		  return null;
	  },

	  getSelectedContacts: function() {
		  return this._selectedContacts;
	  },

	  deselectContacts: function() {
		  for (var i = 0; i < this._selectedContacts.length; i++) {
			  this._selectedContacts[i].deselect();
		  }
	  },
	  hasContactsOnService: function(serviceTag) {
		  for (var i = 0; i < this.contacts.length; i++) {
			  if (this.contacts[i].service == serviceTag) {
				  return true;
			  }
		  }
		  return false;
	  },
	  logServiceUse: function(serviceTag, nUses) {
		  nUses = (typeof nUses == 'undefined') ? 1 : nUses;
		  if (this.shareHistory.services[serviceTag]) {
			  this.shareHistory.services[serviceTag].count += nUses;
		  }
		  else {
			  this.shareHistory.services[serviceTag] = { service: serviceTag, count: nUses };
		  }
		  widget.setCookie('ServiceHistory', JSON.encode(this.shareHistory.services));
	  }
	});


//tofield.js

Widget.MirrorContact = new Class(
	{ Extends: Widget.Contact,
	  added: false,
	  respondToLiveUpdate: true,
	  isMirror: true,	// gah.
	  select: function() {
		  if (!this.added) {
			  this.respondToLiveUpdate = false;
			  this.widget.user.addContactsLocally([this]);
			  this.added = true;
		  }
		  this.parent();
	  },
	  toString: function() {
		  // ie doesn't like the parent() call ... copy-pasting from the superclass directly. bleh.
		  
		  //return this.parent();
		  
		  // right now, supporting only sort by name and sort by service
		  var secondarySort = '';
		  if (widget.user.contactSortMode == 'name') {
			  secondarySort = this['service'];
		  }
		  else if (widget.user.contactSortMode == 'service') {
			  secondarySort = this['name'];
		  }
		  return (this[widget.user.contactSortMode] + ' - ' + secondarySort).toLowerCase();
		  
	  }
	});


Widget.ToField = new Class(
	{ Implements: Events,
	  contacts: [],	// list of currently-displayed contacts
	  domContainer: null,
	  inputElement: null,
	  inputElementContainer: null,
	  widget: null,
	  
	  highlightedResult: null,
	  resultsElement: null,
	  mirrorInputResultItem: null,
	  mirrorContact: null,
	  searchResults: [],
	  searchText: '',
	  
	  selectedTokens: [],
	  
	  _windowKeyDownHandler: null,
	  _mouseOverSearchResultsList: false,
	  _searchResultsShown: false,
	  
	  initialize: function(widget, domContainer) {
		  this.widget = widget;
		  this.domContainer = domContainer;
		  widget.user.addEvent('contactSelectionChanged', this._handleWidgetSelectionChanged.bind(this));
		  widget.user.addEvent('contactsChanged', this._handleWidgetContactsChanged.bind(this));
		  
		  domContainer.addEvent('mousedown', this._handleMouseDown.bind(this));
		  domContainer.addEvent('mouseup', this._handleMouseUp.bind(this));
		  
		  // internal
		  this.addEvent('contactsChanged', this._render.bind(this), true);
		  this.addEvent('searchTextChanged', this._handleSearchTextChanged.bind(this), true);
		  this._getInputField();		// force construction asap
		  this._getMirrorContact();	// ditto
		  this._render();
		  //alert("end init");
	  },

	  /**
	   * Note: it's preferable to use widget.getSeelctedContacts(). It
	   * will return the same items for now but is conceptually lower-level.
	   * 
	   * @return array A list of the current contacts in the to field.
	   */
	  getRecipients: function () {
		  return $A(this.contacts);
	  },

	  showSearchResults: function() {
		  var list = this._getSearchResultsList();
		  list.tween('opacity', .9);
		  this._searchResultsShown = true;
	  },

	  hideSearchResults: function() {
		  var list = this._getSearchResultsList();
		  this.highlightedResult = null;
		  list.tween('opacity', 0);
		  this._searchResultsShown = false;
	  },
	  
	  setSearchText: function(text) {
		  this.searchText = text;
		  this.fireEvent('searchTextChanged', this.searchText);
	  },
	  
	  _handleSearchTextChanged: function(text) {
		  if (text.length) {
			  this._search(text);
		  }
		  else {
			  this.hideSearchResults();
		  }
	  },

	  _search: function(text) {
		  this.searchResults = widget.user.searchContacts(text);
		  
		  var list = this._getSearchResultsList();
		  list.getChildren().each((function(item) { this._disposeSearchResult(item); }).bind(this));
		  
		  this.searchResults = this.searchResults.filter(function(item) { return !item.selected; });
		  if (this.searchResults.length == 0) {
			  this.searchResults.unshift(this._getMirrorContact());
		  }
		  
		  list.setStyle('height', 'auto');
		  var toField = $('send_to_field');
		  var pageElement = widget.getCurrentPage().domContainer;
		  var toFieldBottom = toField.getCoordinates(pageElement).bottom + toField.getScroll().y;
		  var availableHeight = pageElement.getSize().y - toFieldBottom - 15;
		  for (var i = 0; i < this.searchResults.length; i++) {
			  list.grab(this._createSearchResult(this.searchResults[i]));
			  if (list.getSize().y > availableHeight) {
				  list.setStyle('height', availableHeight + 'px');
			  }
			  else {
				  list.setStyle('height', 'auto');
			  }
		  }
		  this.showSearchResults();
		  this._highlightResult(0);
	  },


	  _getMirrorContact: function() {
		  if (this.mirrorContact) {
			  return this.mirrorContact;
		  }
		  var mirrorContact = new Widget.MirrorContact(this.widget, {
			  service: 'email',
			  name: '',
			  address: '',
			  id: ''
		  });
		  this.mirrorContact = mirrorContact;
		  
		  var liveUpdateF = function(text) {
			  if (mirrorContact.respondToLiveUpdate) {
				  mirrorContact.setAddress(text);
				  mirrorContact.setName(text);
				  mirrorContact.setService(Widget.Contact.guessServiceForAddress(text) || 'email');
			  }
		  }
		  // first time it's selected we no longer care about it.
		  // (note that bind() changes arguments.callee within the function
		  // so we can't bind() here to remove ourselves from the event stack;
		  // just use a poppet.)
		  var toField = this;
		  mirrorContact.addEvent('selected', function(selected) {
			  var f = arguments.callee;
			  // mirrorContact's fireEvent() is currently iterating over its events.
			  // calling removeEvent in the middle is ill-advised.
			  setTimeout(function() {
				  mirrorContact.removeEvent('selected', f);
			  }, 1);
			  toField.removeEvent('searchTextChanged', liveUpdateF);
			  toField.mirrorContact = null;
		  });
		  this.addEvent('searchTextChanged', liveUpdateF);

		  return this.mirrorContact;
	  },
	  
	  /**
	   * @param int|element item
	   */
	  _highlightResult: function(item, obj, scrollBehavior) {
		  var list = this._getSearchResultsList();
		  if (typeof item == 'number') {
			  obj = this.searchResults[item] || this._getMirrorContact();
			  item = list.getElements('li')[item];
		  }
		  if (this.highlightedResult) {
			  this.highlightedResult.removeClass('highlight');
		  }
		  this.highlightedResult = item;
		  if (this.highlightedResult) {
			  this.highlightedResult.addClass('highlight');
			  if (scrollBehavior == undefined || scrollBehavior == Widget.ToField.scrollBehavior_scroll) {
				  var itemPos = this.highlightedResult.getPosition(list).y;
				  var itemHeight = this.highlightedResult.getSize().y;
				  var scrollPos = list.getScroll().y;
				  var listHeight = list.getSize().y;
				  itemPos += (Browser.Engine.trident ? scrollPos : 0);
				  if (itemPos < scrollPos) {
					  list.scrollTo(0, itemPos);
				  }
				  if (itemPos + itemHeight > scrollPos + listHeight) {
					  list.scrollTo(0, scrollPos + ((itemPos + itemHeight) - (scrollPos + listHeight)));
				  }
			  }
		  }
	  },
	  
	  _getHighlightedResultIndex: function() {
		  if (this.highlightedResult && this.highlightedResult._stContact) {
			  for (var i = 0; i < this.searchResults.length; i++) {
				  if (this.searchResults[i] == this.highlightedResult._stContact) {
					  return i;
				  }
			  }
		  }
		  return -1;
	  },
	  
	  _highlightPrevResult: function() {
		  var current = this._getHighlightedResultIndex();
		  if (current > 0) {
			  this._highlightResult(current - 1);
		  }
	  },
	  
	  _highlightNextResult: function() {
		  var current = this._getHighlightedResultIndex();
		  if (current >= 0 && current < this.searchResults.length - 1) {
			  this._highlightResult(current + 1);
		  }
	  },
	  
	  _disposeSearchResult: function(item) {
		  for (var name in item._stEventHandlers.self) {
			  item.removeEvent(name, item._stEventHandlers.self[name])
		  }
		  for (var name in item._stEventHandlers.contact) {
			  item._stContact.removeEvent(name, item._stEventHandlers.contact[name]);
		  }
		  item.destroy();
	  },
	  
	  _createSearchResult: function(contact) {
		  var item = new Element('li', { 'class': 'search_result', id:'search_result_' + contact.id });
		  var name = new Element('div', { 'class': (contact.service.length ? contact.service : 'email') });
		  var address = new Element('div', { 'class': 'address' });
		  address.set('text', '<' + contact.address + '>');
		  
		  var nameText = (contact.name.indexOf('@') == -1 ? contact.name : contact.name.substr(0, contact.name.indexOf('@')));
		  name.set('text', nameText);
		  contact.addEvent('addressChanged', function(newAddress) {
			  address.set('text', '<' + newAddress + '>');
		  });
		  contact.addEvent('nameChanged', function(newName) {
			  name.set('text', (newName.length ? newName : ''));
		  });
		  
		  item._stEventHandlers = {
			  self: {
				  mouseenter: (function(event) {
					  this._highlightResult(item, contact, Widget.ToField.scrollBehavior_dontScroll);
				  }).bind(this),
				  mouseleave: (function(event) {
					  setTimeout((function() {
						  if (this.highlightedResult == item) {
							  item.removeClass('highlight');
							  this.highlightedResult = null;
						  }
					  }).bind(this), 100);
				  }).bind(this),
				  click: (function(event) {
					  contact.select();
					  this.hideSearchResults();
				  }).bind(this)
			  },
			  contact: {
				  serviceChanged: function(serviceName) {
					  name.set('class', serviceName);
				  }
			  }
		  };
		  
		  item._stContact = contact;
		  
		  item.grab(name)
			  .grab(address)
			  .addEvent('mouseenter', item._stEventHandlers.self.mouseenter)
			  .addEvent('mouseleave', item._stEventHandlers.self.mouseleave)
			  .addEvent('click', item._stEventHandlers.self.click);

		  contact.addEvent('serviceChanged', item._stEventHandlers.contact.serviceChanged);
		  
		  if (contact.isMirror) {
			  item.addClass('mirror');
		  }
		  
		  return item;
	  },

	  _selectHighlightedResult: function() {
		  if (this.highlightedResult && this.highlightedResult._stContact) {
			  if (this.highlightedResult._stContact.address.length) {
				  this.highlightedResult._stContact.select();
				  this.hideSearchResults();
			  }
		  }
		  //this.setSearchText('');
		  setTimeout((function() {
			  this._insertInputField();
		  }).bind(this), 10);
		  
	  },

	  _getSearchResultsList: function() {
		  if (this.resultsElement) {
			  return this.resultsElement;
		  }
		  this.resultsElement = new Element('ul', { 'id':'search_results', 'class': 'search_results' });
		  this.domContainer.getParent().grab(this.resultsElement, 'bottom');
		  this.resultsElement.set('tween', { 'duration': 250 });

		  // this hackery is for ie, which removes focus from the 
		  // input box when you click on a scroll bar.
		  this.resultsElement.addEvent('mouseenter', (function(event) {
			  this._mouseOverSearchResultsList = true;
		  }).bind(this));
		  /*		this.resultsElement.addEvent('mouseleave', (function(event) {
					this._mouseOverSearchResultsList = false;
					if (Browser.Engine.trident) {
					setTimeout((function() {
					this.inputElement.focus();
					}).bind(this), 10);
					}
					}).bind(this));*/
		  // note: there doesn't appear to be a way to detect a click on the scroll bar itself.
		  // soo... we're kind of screwed here. using the scroll bar without generating a mouseleave event
		  // will keep the input blurred, so keyboard commands won't work. ie is teh awesomeness.
		  return this.resultsElement;
	  },

	  _getInputField: function() {
		  if (this.inputElementContainer) {
			  return this.inputElementContainer;
		  }
		  this.inputElementContainer = new Element('div', { 'id':'contact_search_field_container' });
		  this.inputElement = new Element('input', { 'type': 'text', 'id':'contact_search_field', 'class': 'contact_search_field' });
		  this.inputElement.addEvent('keydown', (function(event) {
			  switch (event.key) {
			  case 'up':
				  widget.pages.send.toField._highlightPrevResult();
				  event.stop();
				  break;
			  case 'down':
				  widget.pages.send.toField._highlightNextResult();
				  event.stop();
				  break;
			  case 'enter':
				  widget.pages.send.toField._selectHighlightedResult();
				  event.stop();
				  break;
			  case 'esc':
				  this.hideSearchResults();
				  event.stop();
				  break;
			  }
			  switch (event.code) {
			  case 188: 	// comma
				  widget.pages.send.toField._selectHighlightedResult();
				  event.stop();
				  break;
			  }
		  }).bind(this));
		  this.inputElement.addEvent('keyup', (function(event) {
			  var value = this.inputElement.get('value');
			  if (this.searchText != value) {
				  this.setSearchText(value);
			  }
		  }).bind(this));
		  this.addEvent('searchTextChanged', (function(text) {
			  this.set('value', text);
		  }).bind(this.inputElement));
		  
		  this.inputElement.addEvent('blur', (function() {
			  if($('contact_search_field').value.length){
				  widget.pages.send.toField._selectHighlightedResult();
				  widget.pages.send.toField.hideSearchResults();
			  }
		  }),this);
		  
		  this.inputElement.addEvent('focus', (function() {
			  //this._deselectTokens();
			  //	console.log("in focus");
			  if (widget.pages.send.toField.searchText.length) {
				  widget.pages.send.toField.showSearchResults();
			  }
		  }),this);
		  this.inputElementContainer.grab(this.inputElement);
		  return this.inputElement;
	  },
	  _mouseInResultsListScrollBar: function(event) {
		  var coords = this._getSearchResultsList().getCoordinates();
		  return false;
		  return (
			  event.page.x >= coords.left && 
				  event.page.x <= coords.right && 
				  event.page.y >= coords.top && 
				  event.page.y <= coords.bottom
		  );
	  },
	  _deleteSelectedTokens: function() {
		  for(var i = 0; i < this.selectedTokens.length; i++) {
			  this.selectedTokens[i].contact.deselect();
		  }
		  this.selectedTokens.empty();
	  },
	  
	  _handleMouseDown: function(event) {
		  if (event.target == this.domContainer) {
			  this._insertInputField();
			  event.stop();
		  }
	  },
	  
	  _insertInputField: function() {
		  var f = this._getInputField();
		  //this.domContainer.grab(f, 'bottom');
		  this.domContainer.appendChild(f);
		  this.domContainer.scrollTo(0, this.domContainer.getScrollSize().y);
		  //alert("input field set");
		  var objId=widget.pages.send.toField.inputElement.id;
		  
		  setTimeout((function() {
			  var id=$("contact_search_field_container").firstChild.id;
			  var tmpObj=document.getElementById(id);
			  if(id=="contact_search_field" && tmpObj && glo_browser.test("ie")==false){
				  tmpObj.select();
			  }
			  //alert("input element focus");
		  }), 500);
	  },
	  
	  _handleMouseUp: function(event) {
	  },
	  _handleWidgetContactsChanged: function() {
		  // it's the widget's responsibility to merge new contacts on login with
		  // current local contacts.
		  this._handleWidgetSelectionChanged();
		  this._render();
		  //	alert("ContactsChanged");
	  },
	  _handleWidgetSelectionChanged: function() {
		  var selectedContacts = widget.user.getSelectedContacts();
		  this.contacts = [];
		  for (var i = 0; i < selectedContacts.length; i++) {
			  this.contacts.push(selectedContacts[i]);
		  }
		  this.fireEvent('contactsChanged');
		  
		  this.setSearchText('');
		  widget.pages.send.updateCharacterCounter();
	  },
	  /*
		_deselectTokens: function() {
		// deselecting directly from selectedTokens will invalidate the iteration
		var localTokens = [].combine(this.selectedTokens);
		for (var i = 0; i < localTokens.length; i++) {
		localTokens[i].select(false);
		}
		},
	  */
	  _clearList: function() {
		  this.domContainer.getChildren().each(function(element) {
			  if (element.hasClass('token')) {
				  element.destroy();
			  }
			  else {
				  element.dispose();
			  }
		  });
	  },

	  _createToken: function(contact, method) {
		  var retVal='<a class="token" href="javascript:void(0);" >';
		  retVal+='<span title= "' + contact.name + '<' + contact.address + '>' + '" class="' + (contact.service.length ? contact.service : 'email') + '">';
		  retVal+='<img class="token_x" id="token_img" src="">' + widget.truncateText(contact.name, 130);
		  retVal+='</span></span></a>';
		  return retVal;
	  },
	  
	  _attachTokenBehavior: function(element, contact) {
		  $extend(element, {
			  contact: contact,
			  pageCoordinateIsOverX: function(pageX) {
				  var xLeft = this.getSize().x - 20;
				  var elementXPos = this.getPosition().x;
				  return (pageX - elementXPos > xLeft);
			  }
		  });
		  element.addEvent('click', (function(event) {
			  if (element.pageCoordinateIsOverX(event.page.x) && element.contact) {
				  element.contact.deselect();
			  }
		  }).bind(this));
		  
		  var mouseMoveHandler = (function(event) {
			  if (element.pageCoordinateIsOverX(event.page.x)) {
				  //element.getElementById('token_img').addClass('token_hover');
			  }
			  else {
				  //element.getElementById('token_img').removeClass('token_hover');
			  }
		  }).bind(this);
		  element.addEvent('mousemove', mouseMoveHandler);
		  element.onselectstart = function() { return false; };
		  element.unselectable = "on";
		  element.style.MozUserSelect = "none";
	  },
	  
	  _getContentsHeight: function() {
		  var h = 0;
		  var lastLineY = 0;
		  this.domContainer.getChildren().each(function(child) {
			  if (child.getPosition().y != lastLineY) {
				  h += child.getSize().y;
			  }
			  lastLineY = child.getPosition().y;
		  });
		  return h;
	  },

	  _render: function() {
		  this._clearList();
		  var html = '';
		  for (var i = 0; i < this.contacts.length; i++) {
			  html += this._createToken(this.contacts[i], Widget.ToField.createToken_asHTML);
		  }
		  this.domContainer.set('html', html);
		  this.domContainer.getElements('.token').each((function(element, index) {
			  this._attachTokenBehavior(element, this.contacts[index]);
		  }).bind(this));
		  var h = this._getContentsHeight();
		  if (h * 2 < 14) {
			  this.domContainer.setStyles({
				  height: '14px',
				  overflow:'hidden'
			  });
		  }
		  else if (h * 2 > 70) {
			  this.domContainer.setStyles({
				  height: '70px',
				  overflow:'auto'
			  });
		  }
		  else {
			  this.domContainer.setStyles({
				  height: h * 2 + 'px',
				  overflow:'hidden'
			  });
		  }
		  //		alert("end render");
	  },
	  
	  onPageShown: function() {
		  setTimeout((function() {
			  this._insertInputField();
		  }).bind(this), 100);
		  
		  this._render();
		  //	alert("after on page shown");
	  },
	  
	  onPageHidden: function() {
		  document.removeEvent('keydown', this._windowKeyDownHandler);
	  }
	});

Widget.ToField.createToken_asNode = 0;
Widget.ToField.createToken_asHTML = 1;
Widget.ToField.scrollBehavior_dontScroll = 0;
Widget.ToField.scrollBehavior_scroll = 1;

//domready.js

window.addEvent('domready', function() {

	var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");

	var headID = document.getElementsByTagName("head")[0];         
	var newScript = document.createElement('script');
	newScript.type = 'text/javascript';
	newScript.src = gaJsHost + "google-analytics.com/ga.js";
	headID.appendChild(newScript);

	var html_str =  'try{' + 'var pageTracker = _gat._getTracker("UA-1645146-9");'
		+ 'pageTracker._trackPageview();' + '} catch(err) {}';
	var newScript = document.createElement('script');
	newScript.type = 'text/javascript';
	newScript.text = html_str;
	headID.appendChild(newScript);
	
	domReady=true;
	if(glo_options_popup===true && glo_toolbar==false)
	{
		$("close_button").set("style","display:none");
	}

	widget = new Widget();
	widget.readyPages(widget.pages);
	
	if(glo_initRun===true){
		processBuffer();
	}
	else{
		createSwList();
	}

	var a=[];
	a=$$('input');
	for(i=0;i<a.length;i++){
		a[i].setAttribute("autocomplete", "off"); 
	}
	//handleExternalLinks();
	emptyInputs();

	
	$('linkSignIn').addEvent('click', function(){
		gaLog("Footer", "signin_link", "Sign In Link"); 
		widget.showPage('login');
		widget.setLoginService('yahoo');
	});
	$('sharethis_login').addEvent('click', function(){
		gaLog("Footer", "signin_link", "ShareThis"); 
		widget.showPage('login');
		widget.setLoginService('sharethis');
	});
	$('yahoo_login').addEvent('click', function(){
		gaLog("Footer", "signin_link", "Yahoo"); 
		widget.showPage('login');
		widget.setLoginService('yahoo');
	});

	
	$('linkSignOut').addEvent('click', function(){
		gaLog("Footer", "signout_link", "Sign Out Link"); 
		if (glo_page == "send" || glo_page == "post|twitter") {
			widget.showPage(glo_page);
		} else {
			widget.showPage('home');
		}
		widget.signOut();
	});

	widget.user.addEvent('signedOut', function() {
		$('colophon').removeClass('signed_in');
		//		$('greeting').set('text', 'Hello.');
	});
	widget.user.addEvent('signInRequested', function() {
		widget.pushModalWorkingSheet(langTextArray['msg_logingin']+'&hellip;');
	});
	widget.user.addEvent('signInFailed', function(errorMessage) {
		widget.popModalWorkingSheet();
		widget.pushModalErrorSheet(errorMessage);
	});
	widget.user.addEvent('signedIn', function() {
		widget.popModalWorkingSheet();
		$('colophon').addClass('signed_in');
			//		widget.showPreviousPage();
		widget.showPage('home');
		widget.fireEvent('signInComplete');
	});
	
	
	widget.addEvent('headerTitleChanged', function(newTitle) {
		$('header_title').set('text', newTitle);
		if (!newTitle.length) {
			$('header_title').addClass('hidden');
		}
		else {
			if (glo_ads == false) {
				$('header_title').removeClass('hidden');
			}
		}
	});
	widget.addEvent('headerFGColorChanged', function(newColor) {
		$('header_title').setStyle('color', newColor);
	});

	if (glo_headerTitle) {
		widget.fireEvent('headerTitleChanged', glo_headerTitle);
		widget.fireEvent('headerFGColorChanged', glo_headerfg);
	}

	$('createAccount').addEvent('click', function(event) {
		gaLog("SignIn", "create_account_btn_click"); 
		widget.showPage('register');
		event.stop();
	});
	widget.addEvent('linkForegroundColorChanged', function(color) {
		for (var p in widget.pages) {
			$(widget.pages[p].id).getElements('a').each(function(anchor){ anchor.setStyle('color', color); });
		}
	});
	
	var authCookie = widget.getCookie("ShareUT");
	if (authCookie) {
		widget.user.acquireAuth(authCookie);
	}

	Cookie.dispose('import', {domain: ".sharethis.com", path: '/'});
	Cookie.dispose('import_delt', {domain: ".sharethis.com", path: '/'});
	
	$$('input').each(function(input) {
		input.addEvent('focus', function() {
			widget.captureLastFocus(input);
		});
	});
	
	$('post_remember_me').addEvent('click', function() {
		var postFields = new Array( 'post_url', 'post_username', 'post_password' );
		if ($('post_remember_me').checked == false) {
			for (var i = 0; i <= glo_credentials.length; i++) {
				if (glo_credentials[i] && glo_credentials[i].service == glo_post_page) {
					glo_credentials.splice(i,1);
				}
			}
			gaLog("Post", "post_remember_me_click", "forget_me"); 
			$('post_forget_me').value = 'true';
			postFields.each( function(item) {
				widget.unfreezeTextInput($(item));
				$(item).value = '';
			});
			widget.initPost(glo_post_page);
			widget.pages.post.pages[glo_post_page].onShow();
		} else {
			var new_cred = new Object();
			new_cred.service = glo_post_page
			new_cred.url = $('post_url').value;
			new_cred.username = $('post_username').value
			new_cred.password = $('post_password').value
			glo_credentials.push(new_cred);
			gaLog("Post", "post_remember_me_click", "remember_me"); 
			postFields.each( function(item) {
				widget.freezeTextInput($(item));
			});
		}
	});

	if (glo_page && (glo_page == "send" || glo_page.match('post|')) ) {
		widget.showPage(glo_page);
	} else {
		widget.showPage('home');
	}

	//replace all IDs with language stinggs if language is not english
	if(lang!='en')
	{
	$('top_services_title').set('html', langTextArray['most_popular_serv']);
	$('twitter_update_status').set('html', langTextArray['update_status']);
	$('twitter_direct_message').set('html', langTextArray['direct_msg']);
	$('msgstr_to_friend').set('html', langTextArray['to_friend']);
	$('import_contacts_link').set('html', langTextArray['import_contact']);
	$('msgstr_recent').set('html', langTextArray['recent']);
	$('msgstr_to_id').set('html', langTextArray['email_to']);
	$('msgstr_from_id').set('html', langTextArray['email_from']);
	$('boxToYourAddrInfo').set('html', "("+langTextArray['email_associated']+")");
	$('msgstr_email').set('html', langTextArray['email_message']);
	$('msgstr_character_left').set('html', langTextArray['character_left']);
	$('msgstr_btn_cancel').set('html', langTextArray['btn_cancel']);
	$('msgstr_btn_prev').set('html', langTextArray['btn_preview']);
	$('msgstr_btn_closepre').set('html', langTextArray['btn_close_preview']);
	$('msgstr_btn_send').set('html', langTextArray['btn_send']);
	$('privacyLink_text').set('html', langTextArray['privacy_policy']);
	$('msgstr_imp_contacts').set('html', langTextArray['email_import_contacts']);
	$('msgstr_imp_share').set('html', langTextArray['email_contacts_access']);
	$('msgstr_email_uname').set('html', langTextArray['username']);
	$('msgstr_email_pass').set('html', langTextArray['password']);
	$('msgstr_imp_cancel').set('html', langTextArray['btn_cancel']);
	$('msgstr_btn_import').set('html', langTextArray['import']);
	$('msgstr_reg_share').set('html', langTextArray['register_sharethis']);
	$('msgstr_save_contact').set('html', langTextArray['save_contacts_sharebox']);
	$('msgstr_full_name').set('html', langTextArray['full_name']);
	$('msgstr_sh_email').set('html', langTextArray['email']+":");
	$('msgstr_sh_uname').set('html', langTextArray['username']);
	$('msgstr_sh_pass').set('html', langTextArray['password']);
	$('msgstr_pass_again').set('html', langTextArray['password_again']);

	var regLink=langTextArray['reg_link'].replace(/#%termsSpanStarts%#/,'<span class="link" onclick="window.open(\'http://sharethis.com/terms/\',\'terms\',\'resizable=1,location=1,status=1,scrollbars=1,toolbar=1\'); return false;">');
		regLink=regLink.replace(/#%termsSpanEnd%#/,'</span>');
		regLink=regLink.replace(/#%privacySpanStarts%#/, '<span class="link" onclick="window.open(\'http://sharethis.com/privacy/\',\'privacy\',\'resizable=1,location=1,status=1,scrollbars=1,toolbar=1\'); return false;">');
		regLink=regLink.replace(/#%privacySpanEnds%#/,'</span>');

	$('registerLegal').set('html', regLink);
	$('msgstr_reg_cancel').set('html', langTextArray['btn_cancel']);
	$('msgstr_reg_register').set('html', langTextArray['register']);
	$('registerOK').set('value', langTextArray['btn_ok']);
	$('msgstr_create_acc').set('html', langTextArray['creating_acc']);
	$('registerSuccess').set('html', langTextArray['msg_acc_created']);
	$('msgstr_save_to_sharebox').set('html', langTextArray['save_to_sharebox']);
	$('msgstr_stuff_sharebox').set('html', langTextArray['stuff_sharebox']);
	$('msgstr_tags').set('html', langTextArray['tags']);
	$('msgstr_share_cancel').set('html', langTextArray['btn_cancel']);
	$('msgstr_share_save').set('html', langTextArray['btn_save']);
	$('post_username_label').set('html', langTextArray['username']);
	$('post_url_label').set('html', langTextArray['blog_url']);
	$('post_password_label').set('html', langTextArray['password']);
	$('post_message_label').set('html', langTextArray['email_message']);
	$('msgstr_post_char_cnt').set('html', langTextArray['character_left']);
	$('msgstr_select_blog').set('html', langTextArray['blog_select']);
	$('msgstr_remember_me').set('html', langTextArray['remember_me']);
	$('msgstr_post_btn_cancel').set('html', langTextArray['btn_cancel']);
	$('msgstr_post_btn_save_draft').set('html', langTextArray['btn_save_draft']);
	$('msgstr_post_btn_post').set('html', langTextArray['btn_post']);
	$('msgstr_post_btn_submit').set('html', langTextArray['btn_submit']);
	$('done_message').set('html', langTextArray['msg_shared']);
	$('msgstr_msg_sharing').set('html', langTextArray['msg_sharing']);
	$('msgstr_what_next').set('html', langTextArray['what_next']);
	$('msgstr_addto_toolbar').set('html', langTextArray['add_to_toolbar']);

	var addToolbar=langTextArray['sharethis_link'].replace(/#%anchorShareStart%#/,"<a class=\"bookmark\" onclick=\"javascript:alert(langTextArray['msg_right_click']); return false;\" href=\"javascript:var%20e=document.createElement('script');e.setAttribute('language','javascript');e.setAttribute('src','http://sharethis.com/share/load');document.body.appendChild(e);void(0);\">");
	addToolbar=addToolbar.replace(/#%anchorShareEnd%#/,"</a>");
	$('doneRegIE').set('html', addToolbar);

	var dragLink=langTextArray['drag_link'].replace(/#%anchorStart2%#/, "<a class=\"bookmark\" onclick=\"javascript:alert(langTextArray['msg_drag']); return false;\" href=\"javascript:var%20e=document.createElement('script');e.setAttribute('language','javascript');e.setAttribute('src','http://sharethis.com/share/load');document.body.appendChild(e);void(0);\">");
	dragLink=dragLink.replace(/#%anchorEnd2%#/,"</a>");
	$('doneRegOther').set('html', dragLink);

	$('msgstr_regsiter_now').set('html', langTextArray['register_now']);
	$('msgstr_instantly_share').set('html', langTextArray['instantly_share']);
	$('msgstr_get_button').set('html', langTextArray['get_button']);
	$('msgstr_put_sharethis').set('html', langTextArray['put_sharethis']);
	$('msgstr_share_again').set('html', langTextArray['btn_share_again']);
	$('msgstr_signin_acc').set('html', langTextArray['sign_in']);
	$('msgstr_signin_account').set('html', langTextArray['sign_in_account']);
	$('msgstr_btnlogin_can').set('html', langTextArray['btn_cancel']);
	$('msgstr_login_signin').set('html', langTextArray['sign_in']);
	$('msgstr_create_accnt').set('html', langTextArray['create_account']);
	$('msgstr_succ_signin').set('html', langTextArray['succ_signin']);
	$('msgstr_label_type').set('html', langTextArray['label_type']);
	$('msgstr_btn_skip').set('html', langTextArray['btn_skip']);
	$('msgstr_account_signin').set('html', langTextArray['sign_in']);
	$('footer_ad_header').set('html', langTextArray['advertisement']);
	$('msgstr_link_privacy').set('html', langTextArray['privacy_policy']);
	$('poweredByLink').set('html', langTextArray['powered_by']);
	$('ff_plugin_link').set('html', langTextArray['download_sharethis']);
	$('linkSignIn').set('html', langTextArray['sign_in']+':');
	$('myAccLink').set('html', langTextArray['my_account']);
	$('linkSignOut').set('html', langTextArray['sign_out']);
	$('close_widget').set('html', langTextArray['close']);

	}//Eoif for english language

});

