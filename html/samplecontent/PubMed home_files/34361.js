Portal.Portlet.Entrez_Messages = Portal.Portlet.extend({

	init: function(path, name, notifier) {
		this.base(path, name, notifier);
		
		this.setMsgAreaClassName();
	},
	
	listen: {
	   /* messages from message bus*/
		
		'AddUserMessage' : function(sMessage, oData, sSrc) {
		    // create new message node
		    var msgnode = document.createElement('li');
		    if (oData.type != ''){
		        msgnode.className = oData.type; 
		    }
		    if (oData.name != ''){
		        msgnode.id = oData.name; 
		    }
		    msgnode.innerHTML = oData.msg;
		    
		    // add new node as first message in message block (not ads that look like messages)
		    var parent = document.getElementById('msgportlet');
		    if (parent){
    		    var oldnode = document.getElementById(oData.name);
    		    if (oldnode){
    		        parent.removeChild(oldnode);
    		    }
    		    var firstchild = parent.firstChild;
    	        if (firstchild){
                    parent.insertBefore(msgnode, firstchild);
                }
                else{
                    parent.appendChild(msgnode);
                }
                this.setMsgAreaClassName('true');
            }
		},
		
		'RemoveUserMessage' : function(sMessage, oData, sSrc) {
		    var msgnode = document.getElementById(oData.name);
		    if (msgnode){
		        var parent = document.getElementById('msgportlet'); 
		        if (parent){
    		        parent.removeChild(msgnode);
    		        this.setMsgAreaClassName();
    		    }
		    }
		}
	}, // end listen
	
	'setMsgAreaClassName' : function(hasMsg){
        var msgarea = document.getElementById('messagearea');
	    if (msgarea){
	        var msgclass = "empty";
	        
    	    // if a message was added, hasMsg is set to true at call time to avoid checks. 
    	    // by default, hasMsg is false.
    	    if (hasMsg == 'true'){
    	        msgclass = "messagearea";
    	    }
    	    else if (msgarea.getElementsByTagName('li').length > 0){
                msgclass = "messagearea"; 
        	}
        	
            msgarea.className = msgclass;
        }
	} // end setMsgAreaClassName
});
		
		
Portal.Portlet.Entrez_SearchBar = Portal.Portlet.extend ({
  
	init: function (path, name, notifier) 
	{
		console.info ("Created SearchBar"); 
		this.base (path, name, notifier);
		
		var AutoCompSelectFnc = function(){ 
            Portal.$send('AutoCompSelect'); 
        } 
        jQuery("#search_term").bind("ncbiautocompleteenter", AutoCompSelectFnc ).bind("ncbiautocompleteoptionclick", AutoCompSelectFnc ); 
		
	},

	send: {
		"Cmd": null,
		"Term": null,
		"AutoCompSelect": null
	},
	
	listen: {
		// messages
		
		'AppendTerm': function(sMessage, oData, sSrc) {
		    this.ProcessAppendTerm(sMessage, oData, sSrc);
		},
		
		// to allow any other portlet to clear term if needed  
		'ClearSearchBarTerm': function(sMessage, oData, sSrc) {
			this.setValue("Term", '');
		},
		
		// request current search bar term to be broadcast  
		'SendSearchBarTerm': function(sMessage, oData, sSrc) {
			this.send.Term({'term' : this.getValue("Term")});
		},
		
		'AutoCompleteControl': function(sMessage, oData, sSrc) {
		    this.ChangeAutoCompleteState(sMessage, oData, sSrc);
        },
        
        'AutoCompSelect': function(sMessage, oData, sSrc) {
		    this.AutoCompleteOptionSelected();
        },
        
		// Browser events
		
		"SearchResourceList<change>": function(e, target, name) {
		    this.ResourceSelected(e, target, name);
		},
		
		"Term<keypress>": function(e, target, name) {
			var event = e || utils.fixEvent (window.event);
			if ((event.keyCode || event.which) == 13) 
			{
			    // Emulate Search button click
			    this.ProcessTermKeyPress(event, e, target, name);
			}
		},
		
		// Cmd is set to Go, so ResultsView of other database can choose component based 
		// on value of Cmd. The existing search term is also passed down.
		"Search<click>": function(e, target, name) {
		    this.ProcessSearchClick (e, target, name);
		},
		
		"Preview<click>": function(e, target, name) {
            this.ProcessPreviewClick(e, target, name);
		},
		
		// On Clear button click, set focus to search box and clear the term
		"Clear<click>": function (e, target, name) {
		    this.ProcessClearClick(e, target, name);
		},
		
		// On Advanced Search click, append term
		"Advanced<click>": function (e, target, name) {
		    this.ProcessAdvancedClick(e, target, name);
		},
		
		"CreateRssFeed<click>": function (e, target, name) {
		    this.ProcessCreateRssFeed(e, target, name);
		}
		
	}, //end listen
	
	"ProcessAppendTerm" : function(sMessage, oData, sSrc){
	    var newTerm = this.getValue("Term");
	    if (newTerm != '' && oData.op != ''){
	        newTerm = '(' + newTerm + ') ' + oData.op + ' ';
	    }
	    newTerm += oData.term;
	    this.setValue("Term", newTerm); 
	    
	    var oTerm = this.getInput("Term");
        if (oTerm) {
           oTerm.focus();
        }
	},
	
	"ResourceSelected" : function(e, target, name){
	    /*
	    if (this.getValue("SearchResourceList") == 'customize'){
            window.location = "/sites/myncbi/searchbar/" + db;
        }
        else
        */
        
	    // turn autocomplete off or on if database is changed in selector.
	    if (this.getValue("Term:suggest") == 'true'){
		    // change to if the current database has a dictionary 
		    this.EnableDisableAutocomplete(target.options[target.selectedIndex].getAttribute('resource'));
        }
       
	},
	
	"ProcessTermKeyPress" : function(event, e, target, name){
	    event.returnValue = false;
		if (event.stopPropagation != undefined)
              event.stopPropagation ();   
		if (event.preventDefault != undefined)
              event.preventDefault ();
              
		this.ProcessSearchClick (e, target, name);
		return false;
	},
	
	"ProcessSearchClick" : function(e, target, name){
	    var resource = this.getValue("SearchResourceList");
	    var term = this.getValue("Term");
	    var db = this.getValue("CurrDb");
	    
	    if (resource == db){
	        this.send.Cmd({
				'cmd' : 'Go'
			});
		   	this.send.Term({
				'term' : this.getValue("Term")
			});
			Portal.requestSubmit(); 
	    } 
	    else {
	        window.location = resource 
	            + (term != '' ? (resource.match(/\?/) ? "&term=" : "?term=") + encodeURIComponent(term) : "");
	    }
	},
	
	"ProcessPreviewClick" : function(e, target, name){
        this.send.Cmd({
			'cmd' : 'Preview'
		});
	   	this.send.Term({
			'term' : this.getValue("Term")
		});
		Portal.requestSubmit(); 
	},
	
	"ProcessClearClick" : function(e, target, name){
        this.setValue ("Term", "");
        var term = this.getInput ("Term");
        if (term) 
            term.focus (); 
	},
	
	"ProcessAdvancedClick" : function(e, target, name){
	   	window.location = target.href
	   	    + (this.getValue("Term") != '' ? "?term=" + encodeURIComponent(this.getValue("Term")) : "");
	},
	
	"ProcessCreateRssFeed" : function(e, target, name){
	   	// do xml http to create the feed using portal code, then update the screen with link to feed
	   	var site = document.forms[0]['p$st'].value;
	   	var portletPath = this.getPortletPath(); 
	   	var args = {
            "QueryKey": target.getAttribute('qk'),
            "Db": this.getValue("CurrDb"),
            "RssFeedName": this.getValue("FeedName"),
            "RssFeedLimit": this.getValue("FeedLimit"),
            "HID": target.getAttribute('hid')
        };
        try{
            var resp = xmlHttpCall(site, portletPath, "CreateRssFeed", args, this.receiveRss, {}, this);
        }
        catch (err){
            alert ('Could not create RSS feed.');
        }
	},
	
	/* this function created to be able to create a hack to overcome shortcoming in current portal framework
	Because of using portlet inheritence, the action defined in the base portlet cannot be found.
	The hack is to allow derived portlets to hard-code the path to themselves by overriding this function.
	This hack can be removed after new implementation of portal is in place which will view objects as a flat model.
	*/ 
	"getPortletPath" : function(){
	    return this.realname;
	},
	
	receiveRss: function(responseObject, userArgs) {
	    try{
    	    //Handle timeouts 
    	    if(responseObject.status == 408){
    	        //display an error indicating a server timeout
    	        alert('RSS feed creation timed out.');
    	    }
    	    
    	    // deserialize the string with the JSON object 
    	    var response = '(' + responseObject.responseText + ')'; 
    	    var JSONobject = eval(response);
    	    // display link to feed
    	    document.getElementById('rss_menu').innerHTML = JSONobject.Output;
        }
        catch(e){
              alert('RSS unavailable.');
        }
    },
    
    'ChangeAutoCompleteState': function(sMessage, oData, sSrc){
        this.setValue("Term:suggest", 'false');
        var site = document.forms[0]['p$st'].value;
        var resp = xmlHttpCall(site, this.getPortletPath(), "AutoCompleteControl", {"ShowAutoComplete": 'false'}, this.receiveAutoComp, {}, this);
    },        
        
    'receiveAutoComp': function(responseObject, userArgs) {
    },
    
    'AutoCompleteOptionSelected': function(){
        /*if (this.getInput("AutoSuggestUsed")){
            this.setValue("AutoSuggestUsed", 'true');
        }*/
	    this.ProcessSearchClick ();
	},
	
	'EnableDisableAutocomplete': function(resource){
	    var site = document.forms[0]['p$st'].value;
        var resp2 = xmlHttpCall(site, this.getPortletPath(), "SetAutoCompleteDictionary", {"Db": resource}, this.receiveDictionary, {}, this);
	},
	
	'receiveDictionary': function(responseObject, userArgs){ 
        try {
            // deserialize the string with the JSON object
            var response = '(' + responseObject.responseText + ')';
            var JSONobject = eval(response);
            
            var dict = JSONobject.Dictionary || "";
            
            // turn autocomplete off or on if database is changed in selector.
            if(dict != ''){
               jQuery("#search_term").ncbiautocomplete("option","isEnabled",true).ncbiautocomplete("option","dictionary",dict);
            }
            else{
               jQuery("#search_term").ncbiautocomplete("turnOff",true);    
            }
        }
        catch (e){
        
        }
    }
	
});

function EntrezSearchBarAutoComplCtrl(){
    Portal.$send('AutoCompleteControl');
}
Portal.Portlet.Pubmed_SearchBar = Portal.Portlet.Entrez_SearchBar.extend ({
  
	init: function (path, name, notifier) {
		this.base (path, name, notifier);
	},
	
	/* ######### this is a hack. See detailed comment on same function in base */
	"getPortletPath" : function(){
	    return (this.realname + ".Entrez_SearchBar");
	}
});


Portal.Portlet.DbConnector = Portal.Portlet.extend({

	init: function(path, name, notifier) {
		var oThis = this;
		console.info("Created DbConnector");
		this.base(path, name, notifier);
		
		// reset Db value to original value on page load. Since LastDb is the same value as Db on page load and LastDb is not changed on
		// the client, this value can be used to reset Db. This is a fix for back button use.
		if (this.getValue("Db") != this.getValue("LastDb")){
		    this.setValue("Db", this.getValue("LastDb"));
		}
     
		// the SelectedIdList and id count from previous iteration (use a different attribute from IdsFromResult to prevent back button issues)
		Portal.Portlet.DbConnector.originalIdList = this.getValue("LastIdsFromResult");
		console.info("originalIdList " + Portal.Portlet.DbConnector.originalIdList);
		// if there is an IdList from last iteration set the count
		if (Portal.Portlet.DbConnector.originalIdList != ''){
			Portal.Portlet.DbConnector.originalCount = Portal.Portlet.DbConnector.originalIdList.split(/,/).length;
		}

		notifier.setListener(this, 'HistoryCmd', 
        	function(oListener, custom_data, sMessage, oNotifierObj) {
           		var sbTabCmd = $N(oThis.path + '.TabCmd');
           		sbTabCmd[0].value = custom_data.tab;
        	}
    		, null);
    
	},

	send: {
   		'SelectedItemCountChanged': null,
   		'newUidSelectionList': null,
   		'SavedSelectedItemCount': null
	},

	listen: {
	
		//message from Display bar on Presentation change 
		'PresentationChange' : function(sMessage, oData, sSrc){
			
			// set link information only if it exists
			if (oData.dbfrom){
				console.info("Inside PresentationChange in DbConnector: " + oData.readablename);
				this.setValue("Db", oData.dbto);
				this.setValue("LinkSrcDb", oData.dbfrom);
				this.setValue("LinkName", oData.linkname);
				this.setValue("LinkReadableName", oData.readablename);
			}
			//document.forms[0].submit();
		},
		
		// various commands associated with clicking different form control elements
		'Cmd' : function(sMessage, oData, sSrc){
			console.info("Inside Cmd in DbConnector: " + oData.cmd);
			this.setValue("Cmd", oData.cmd);
			
			// back button fix, clear TabCmd
			if (oData.cmd == 'Go' || oData.cmd == 'PageChanged' || oData.cmd == 'FilterChanged' || 
			oData.cmd == 'DisplayChanged' || oData.cmd == 'HistorySearch' || oData.cmd == 'Text' || 
			oData.cmd == 'File' || oData.cmd == 'Printer' || oData.cmd == 'Order' || 
			oData.cmd == 'Add to Clipboard' || oData.cmd == 'Remove from Clipboard' || 
			oData.cmd.toLowerCase().match('details')){
				this.setValue("TabCmd", '');
				console.info("Inside Cmd in DbConnector, reset TabCmd: " + this.getValue('TabCmd'));
			}

		},
		
		
		// the term to be shown in the search bar, and used from searching
		'Term' : function(sMessage, oData, sSrc){
			console.info("Inside Term in DbConnector: " + oData.term);
			this.setValue("Term", oData.term);
		},
		
		
		// to indicate the Command Tab to be in
		'TabCmd' : function(sMessage, oData, sSrc){
			console.info("Inside TABCMD in DbConnector: " + oData.tab);
			this.setValue("TabCmd", oData.tab);
			console.info("DbConnector TabCmd: " + this.getValue("TabCmd"));
		},
		
		
		// message sent from SearchBar when db is changed while in a Command Tab
		'DbChanged' : function(sMessage, oData, sSrc){
			console.info("Inside DbChanged in DbConnector");
			this.setValue("Db", oData.db);
		},
		
		// Handles item select/deselect events
		// Argument is { 'id': item-id, 'selected': true or false }
		'ItemSelectionChanged' : function(sMessage, oData, oSrc) {
			var sSelection = this.getValue("IdsFromResult");
			var bAlreadySelected = (new RegExp("\\b" + oData.id + "\\b").exec(sSelection) != null);
	       	var count =0;
	       	
			if (oData.selected && !bAlreadySelected) {
				sSelection += ((sSelection > "") ? "," : "") + oData.id;
			   	this.setValue("IdsFromResult", sSelection);
			   	if (sSelection.length > 0){
			   		count = sSelection.split(',').length;
			   	}
			   	this.send.SelectedItemCountChanged({'count': count});
			   	this.send.newUidSelectionList({'list': sSelection});
		   	} else if (!oData.selected && bAlreadySelected) {
				sSelection = sSelection.replace(new RegExp("^"+oData.id+"\\b,?|,?\\b"+oData.id+"\\b"), '');
		   	   	this.setValue("IdsFromResult", sSelection);
				console.info("Message ItemSelectionChanged - IdsFromResult after change:  " + this.getValue("IdsFromResult"));
			   	if (sSelection.length > 0){
			   		count = sSelection.split(',').length;
			   	}
				console.info("Message ItemSelectionChanged - IdsFromResult length:  " + count);   
				this.send.SelectedItemCountChanged({'count': count});
			   	this.send.newUidSelectionList({'list': sSelection});
		   	}
		},
				
		// FIXME: This is the "old message" that is being phased out.
		// when result citations are selected, the list of selected ids are intercepted here,
		// and notification sent that selected item count has changed.
		'newSelection' : function(sMessage, oData, sSrc){
		
			// Check if we already have such IDs in the list
			var newList = new Array();
			var haveNow = new Array();
			if(Portal.Portlet.DbConnector.originalIdList){
				haveNow = Portal.Portlet.DbConnector.originalIdList.split(',');
				newList = haveNow;
			}
			
			var cameNew = new Array();
			if (oData.selectionList.length > 0) {
				cameNew = oData.selectionList;
			}
			
			if (cameNew.length > 0) {
				for(var ind=0;ind<cameNew.length;ind++) {
					var found = 0;
					for(var i=0;i<haveNow.length;i++) {
						if (cameNew[ind] == haveNow[i]) {
							found = 1;
							break;
						}
					}
						//Add this ID if it is not in the list
					if (found == 0) {
						newList.push(cameNew[ind]);
					}
				}
			}
			else {
				newList = haveNow;
			}

				// if there was an IdList from last iteration add new values to old
			var count = 0;
			if ((newList.length > 0) && (newList[0].length > 0)){
				count = newList.length;
			}
			
			console.info("id count = " + count);
			this.setValue("IdsFromResult", newList.join(","));
			
			this.send.SelectedItemCountChanged({'count': count});
			this.send.newUidSelectionList({'list': newList.join(",")});
		},


		// empty local idlist when list was being collected for other purposes.
		//used by Mesh and Journals (empty UidList should not be distributed, otherwise Journals breaks)
		// now used by all reports for remove from clipboard function.
		'ClearIdList' : function(sMessage, oData, sSrc){
			this.setValue("IdsFromResult", '');
			this.send.SelectedItemCountChanged({'count': '0'});
			this.send.newUidSelectionList({'list': ''});
		}, 


		// back button fix: when search backend click go or hot enter on term field,
		//it also sends db. this db should be same as dbconnector's db
		'SearchBarSearch' : function(sMessage, oData, sSrc){
			if (this.getValue("Db") != oData.db){
				this.setValue("Db", oData.db);
			}
		},
		
		// back button fix: whrn links is selected from DisplayBar,
		//ResultsSearchController sends the LastQueryKey from the results on the page
		// (should not be needed by Entrez 3 code)
		'LastQueryKey' : function(sMessage, oData, sSrc){
			if (this.getInput("LastQueryKey")){
				this.setValue("LastQueryKey", oData.qk);
			}
		},
		
		'QueryKey' : function(sMessage, oData, sSrc){
			if (this.getInput("QueryKey")){
				this.setValue("QueryKey", oData.qk);
			}
		},
		
		
		//ResultsSearchController asks for the initial item count in case of send to file 
		'needSavedSelectedItemCount' : function(sMessage, oData, sSrc){
			var count = 0;
			if(this.getInput("IdsFromResult")){
				if (this.getValue("IdsFromResult").length > 0){
					count = this.getValue("IdsFromResult").split(',').length;
				}
				console.info("sending SavedSelectedItemCount from IdsFromResult: " + count);
			}
			else{
				count = Portal.Portlet.DbConnector.originalCount;
				console.info("sending SavedSelectedItemCount from OriginalCount: " + count);
			}
			this.send.SavedSelectedItemCount({'count': count});
		},
		
		// Force form submit, optionally passing db, term and cmd parameters
		'ForceSubmit': function (sMessage, oData, sSrc)
		{
		    if (oData.db)
    			this.setValue("Db", oData.db);
		    if (oData.cmd)
    			this.setValue("Cmd", oData.cmd);
		    if (oData.term)
    			this.setValue("Term", oData.term);
    		Portal.requestSubmit ();
		},
		
		'LinkName': function (sMessage, oData, sSrc){
		    this.setValue("LinkName", oData.linkname);
		}
		
	}, //listen
	
	/* other portlet functions */
	
	// DisplayBar in new design wants selected item count
	'SelectedItemCount': function(){
	    var count = 0;
		if(this.getInput("IdsFromResult")){
			if (this.getValue("IdsFromResult") != ''){
				count = this.getValue("IdsFromResult").split(',').length;
			}
		}
		else{
			count = Portal.Portlet.DbConnector.originalCount;
		}
		return count;
	}
		
},
{
	originalIdList: '',
	originalCount: 0
});

function getEntrezSelectedItemCount() {
    return $PN('DbConnector').SelectedItemCount();
}
