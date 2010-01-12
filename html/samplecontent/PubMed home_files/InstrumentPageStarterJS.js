var s_account = "nihncbiprod";

(function(){
        
    if (document.cookie.length>0){
        var c_start=document.cookie.indexOf("NCBISTATLOC=");
        if(c_start!=-1){ 
            c_start = c_start + 12; 
            var c_end = document.cookie.indexOf(";",c_start);
            if (c_end==-1) c_end = document.cookie.length;
            var ckVal = document.cookie.substring(c_start,c_end);
            if(ckVal.length>0){
                s_account=ckVal;
            }
        } 
    }
    
})();



/* SiteCatalyst code version: H.19.3.
Copyright 1997-2009 Omniture, Inc. More info available at
http://www.omniture.com */
/************************ ADDITIONAL FEATURES ************************
     Plugins
*/


var s=s_gi(s_account)
/************************** CONFIG SECTION **************************/
/* You may add or alter any code config here. */
/* Specify the life time of the cookie in seconds, or */
/* set to "Session" to turn off persistent cookies.   */
s.cookieLifetime="Session"
/* Conversion Config */
s.currencyCode="USD"
/* Link Tracking Config */
s.trackDownloadLinks=true
s.trackExternalLinks=true
s.trackInlineStats=true
s.linkDownloadFileTypes="exe,zip,wav,mp3,mov,mpg,avi,wmv,pdf,doc,docx,xls,xlsx,ppt,pptx"
s.linkInternalFilters="javascript:,ncbi.nlm.nih.gov"
s.linkLeaveQueryString=false
s.linkTrackVars="None"
s.linkTrackEvents="None"
/* Plugin Config */
s.usePlugins=true

/* Page Name Plugin Config */
s.siteID=""            // leftmost value in pagename
s.defaultPage=""       // filename to add when none exists
s.queryVarsList=""     // query parameters to keep
s.pathExcludeDelim=";" // portion of the path to exclude
s.pathConcatDelim=""   // page name component separator
s.pathExcludeList=""   // elements to exclude from the path

function s_doPlugins(s) {
	/* Add calls to plugins here */
	
	/*Populate Page Name*/
	if(!s.pageName && !s.pageType)
		s.pageName=s.getPageName();
	
	/*Track External Campaign*/
	if(!s.campaign)
		s.campaign=s.getQueryParam('psrc');
		s.campaign=s.getValOnce(s.campaign,'s_campaign',0);
	
	/*Page View*/
	s.events=s.apl(s.events,'event2',',',2)
	
	/****************Internal Search****************/
	/*Copy Internal Search Term*/
	if(s.prop3) s.prop3=s.prop3.toLowerCase()
	if(s.prop3){
	    s.eVar3=s.prop3

	/* Set de-duped onsite search event */
	var t_search=s.getValOnce(s.eVar3,'ev3',0)
	if(t_search)
		s.events=s.apl(s.events,'event1',',',1)
	}
	
	/*Copy Search Type*/
	if(s.prop4 && !s.eVar4) s.eVar4 = s.prop4 //Search Type
	
	/***************Entrez Limit**********************/
	/*Copy Limit Criteria Set (Entrez limit) */
	if(s.prop17) s.prop17=s.prop17.toLowerCase()
	if(s.prop17){
	    s.eVar17=s.prop17

	/* Set de-duped onsite search event */
	var t_search=s.getValOnce(s.eVar17,'ev17',0)
	if(t_search)
		s.events=s.apl(s.events,'event7',',',1)
	}
	
	/*Copy Limit Criteria Set Values */
	if(s.prop19) s.prop19=s.prop19.toLowerCase()
	if(s.prop19){
	    s.eVar19=s.prop19

	/* Set de-duped onsite search event */
	var t_search=s.getValOnce(s.eVar19,'ev19',0)
	if(t_search)
		s.events=s.apl(s.events,'event7',',',1)
	}
	
	/******************Advanced Search***************/
	/*Copy Advanced Search Criteria*/
	if(s.prop18) s.prop18=s.prop18.toLowerCase()
	if(s.prop18){
	    s.eVar18=s.prop18

	/* Set de-duped onsite search event */
	var t_search=s.getValOnce(s.eVar18,'ev18',0)
	if(t_search)
		s.events=s.apl(s.events,'event8',',',1)
	}
	
	/*Copy Advanced Search Criteria Values*/
	if(s.prop20) s.prop20=s.prop20.toLowerCase()
	if(s.prop20){
	    s.eVar20=s.prop20

	/* Set de-duped onsite search event */
	var t_search=s.getValOnce(s.eVar20,'ev20',0)
	if(t_search)
		s.events=s.apl(s.events,'event8',',',1)
	}
	
	/***************Entrez Filter Applied**************/
	/*Copy Entrez Filter Applies*/
	if(s.prop38) s.prop38=s.prop38.toLowerCase()
	if(s.prop38){
	    s.eVar38=s.prop38

	/* Set de-duped onsite search event */
	var t_search=s.getValOnce(s.eVar38,'ev38',0)
	if(t_search)
		s.events=s.apl(s.events,'event9',',',1)
	}
	
	/*Time Parting*/
	s.prop34=s.getTimeParting('h','-5','2009'); // Set hour
	s.prop35=s.getTimeParting('d','-5','2009'); // Set day
	s.prop36=s.getTimeParting('w','-5','2009'); // Set Weekend / Weekday
	s.eVar34=s.prop34;
	s.eVar35=s.prop35;
	s.eVar36=s.prop36;
	
	/*Google Scholars tracking*/
	if(document.referrer.indexOf('scholar.google.com') > -1){
		s.eVar32='scholar.google.com';
		s.prop32=s.getAndPersistValue(s.eVar32,'s_dr_persist',0);
		s.prop33=s.prop32+':'+s.pageName;
	}
	
	/*Copy props to eVars*/
	if(s.prop1 && !s.eVar1) s.eVar1 = s.prop1; //Page Hit ID
	if(s.prop2 && !s.eVar2) s.eVar2 = s.prop2; //Database
	if(s.prop5 && !s.eVar5) s.eVar5 = s.prop5; //Search Display(Report Name)
	if(s.prop6 && !s.eVar6) s.eVar6 = s.prop6; //SEarch Show (Page Size)
	if(s.prop7 && !s.eVar7) s.eVar7 = s.prop7; //Search Sort By (Sort Order)
	if(s.prop8 && !s.eVar8) s.eVar8 = s.prop8; //Current Page (Search Page)
	if(s.prop11 && !s.eVar11) s.eVar11 = s.prop11; //Link Name
	if(s.prop12 && !s.eVar12) s.eVar12 = s.prop12; //Database Source
	if(s.prop13 && !s.eVar13) s.eVar13 = s.prop13; //Link Id (TI)
	if(s.prop14 && !s.eVar14) s.eVar14 = s.prop14; //Link Name (TN)
	if(s.prop15 && !s.eVar15) s.eVar15 = s.prop15; //Link Pos (TS)
	if(s.prop16 && !s.eVar16) s.eVar16 = s.prop16; //Entrez Operation
	if(s.prop21 && !s.eVar21) s.eVar21 = s.prop21; //Page Design ID
	if(s.prop22 && !s.eVar22) s.eVar22 = s.prop22; //Session ID
	if(s.prop23 && !s.eVar23) s.eVar23 = s.prop23; //Site Section 2
	if(s.prop24 && !s.eVar24) s.eVar24 = s.prop24; //Sub Section 3
	if(s.prop25 && !s.eVar25) s.eVar25 = s.prop25; //Sub Section 3
	if(s.prop26 && !s.eVar26) s.eVar26 = s.prop26; //Content ID
	if(s.channel && !s.eVar27) s.eVar27 = s.channel; //Site Section
	if(s.pageName && !s.eVar41) s.eVar41 = s.pageName; //Page Name

}
s.doPlugins=s_doPlugins
/************************** PLUGINS SECTION *************************/
/* You may insert any plugins you wish to use here.                 */


/* WARNING: Changing any of the below variables will cause drastic
changes to how your visitor data is collected.  Changes should only be
made when instructed to do so by your account manager.*/
s.visitorNamespace="nih"
s.trackingServer="a.ncbi.nlm.nih.gov"
s.dc=122

/*
 * Plugin: getAndPersistValue 0.3 - get a value on every page
 */
s.getAndPersistValue=new Function("v","c","e",""
+"var s=this,a=new Date;e=e?e:0;a.setTime(a.getTime()+e*86400000);if("
+"v)s.c_w(c,v,e?a:0);return s.c_r(c);");

/*
 * Plugin: getPageName v2.1 - parse URL and return
 */
s.getPageName=new Function("u",""
+"var s=this,v=u?u:''+s.wd.location,x=v.indexOf(':'),y=v.indexOf('/',"
+"x+4),z=v.indexOf('?'),c=s.pathConcatDelim,e=s.pathExcludeDelim,g=s."
+"queryVarsList,d=s.siteID,n=d?d:'',q=z<0?'':v.substring(z+1),p=v.sub"
+"string(y+1,q?z:v.length);z=p.indexOf('#');p=z<0?p:s.fl(p,z);x=e?p.i"
+"ndexOf(e):-1;p=x<0?p:s.fl(p,x);p+=!p||p.charAt(p.length-1)=='/'?s.d"
+"efaultPage:'';y=c?c:'/';while(p){x=p.indexOf('/');x=x<0?p.length:x;"
+"z=s.fl(p,x);if(!s.pt(s.pathExcludeList,',','p_c',z))n+=n?y+z:z;p=p."
+"substring(x+1)}y=c?c:'?';while(g){x=g.indexOf(',');x=x<0?g.length:x"
+";z=s.fl(g,x);z=s.pt(q,'&','p_c',z);if(z){n+=n?y+z:z;y=c?c:'&'}g=g.s"
+"ubstring(x+1)}return n");

/*
 * Utility Function: p_c
 */
s.p_c=new Function("v","c",""
+"var x=v.indexOf('=');return c.toLowerCase()==v.substring(0,x<0?v.le"
+"ngth:x).toLowerCase()?v:0");


/*
 * Utility Function: split v1.5 - split a string (JS 1.0 compatible)
 */
s.split=new Function("l","d",""
+"var i,x=0,a=new Array;while(l){i=l.indexOf(d);i=i>-1?i:l.length;a[x"
+"++]=l.substring(0,i);l=l.substring(i+d.length);}return a");

/*
 * Plugin: getQueryParam 2.3
 */
s.getQueryParam=new Function("p","d","u",""
+"var s=this,v='',i,t;d=d?d:'';u=u?u:(s.pageURL?s.pageURL:s.wd.locati"
+"on);if(u=='f')u=s.gtfs().location;while(p){i=p.indexOf(',');i=i<0?p"
+".length:i;t=s.p_gpv(p.substring(0,i),u+'');if(t){t=t.indexOf('#')>-"
+"1?t.substring(0,t.indexOf('#')):t;}if(t)v+=v?d+t:t;p=p.substring(i="
+"=p.length?i:i+1)}return v");
s.p_gpv=new Function("k","u",""
+"var s=this,v='',i=u.indexOf('?'),q;if(k&&i>-1){q=u.substring(i+1);v"
+"=s.pt(q,'&','p_gvf',k)}return v");
s.p_gvf=new Function("t","k",""
+"if(t){var s=this,i=t.indexOf('='),p=i<0?t:t.substring(0,i),v=i<0?'T"
+"rue':t.substring(i+1);if(p.toLowerCase()==k.toLowerCase())return s."
+"epa(v)}return ''");

/*
* Plugin Utility: apl v1.1
*/
s.apl=new Function("L","v","d","u",""
+"var s=this,m=0;if(!L)L='';if(u){var i,n,a=s.split(L,d);for(i=0;i<a."
+"length;i++){n=a[i];m=m||(u==1?(n==v):(n.toLowerCase()==v.toLowerCas"
+"e()));}}if(!m)L=L?L+d+v:v;return L");

 /* Plugin: getValOnce 0.2 - get a value once per session or number of days
 */
s.getValOnce=new Function("v","c","e",""
+"var s=this,k=s.c_r(c),a=new Date;e=e?e:0;if(v){a.setTime(a.getTime("
+")+e*86400000);s.c_w(c,v,e?a:0);}return v==k?'':v");


/*
 * Function - read combined cookies v 0.2
 */
s.c_rr=s.c_r;
s.c_r=new Function("k",""
+"var s=this,d=new Date,v=s.c_rr(k),c=s.c_rr('s_pers'),i,m,e;if(v)ret"
+"urn v;k=s.ape(k);i=c.indexOf(' '+k+'=');c=i<0?s.c_rr('s_sess'):c;i="
+"c.indexOf(' '+k+'=');m=i<0?i:c.indexOf('|',i);e=i<0?i:c.indexOf(';'"
+",i);m=m>0?m:e;v=i<0?'':s.epa(c.substring(i+2+k.length,m<0?c.length:"
+"m));if(m>0&&m!=e)if(parseInt(c.substring(m+1,e<0?c.length:e))<d.get"
+"Time()){d.setTime(d.getTime()-60000);s.c_w(s.epa(k),'',d);v='';}ret"
+"urn v;");
/*
 * Function - write combined cookies v 0.2
 */
s.c_wr=s.c_w;
s.c_w=new Function("k","v","e",""
+"var s=this,d=new Date,ht=0,pn='s_pers',sn='s_sess',pc=0,sc=0,pv,sv,"
+"c,i,t;d.setTime(d.getTime()-60000);if(s.c_rr(k)) s.c_wr(k,'',d);k=s"
+".ape(k);pv=s.c_rr(pn);i=pv.indexOf(' '+k+'=');if(i>-1){pv=pv.substr"
+"ing(0,i)+pv.substring(pv.indexOf(';',i)+1);pc=1;}sv=s.c_rr(sn);i=sv"
+".indexOf(' '+k+'=');if(i>-1){sv=sv.substring(0,i)+sv.substring(sv.i"
+"ndexOf(';',i)+1);sc=1;}d=new Date;if(e){if(e.getTime()>d.getTime())"
+"{pv+=' '+k+'='+s.ape(v)+'|'+e.getTime()+';';pc=1;}}else{sv+=' '+k+'"
+"='+s.ape(v)+';';sc=1;}if(sc) s.c_wr(sn,sv,0);if(pc){t=pv;while(t&&t"
+".indexOf(';')!=-1){var t1=parseInt(t.substring(t.indexOf('|')+1,t.i"
+"ndexOf(';')));t=t.substring(t.indexOf(';')+1);ht=ht<t1?t1:ht;}d.set"
+"Time(ht);s.c_wr(pn,pv,d);}return v==s.c_r(s.epa(k));");


/*
 * Plugin: getTimeParting 1.4 - Set timeparting values based on time zone (15 min)
 */

s.getTimeParting=new Function("t","z","y",""
+"dc=new Date('1/1/2000');var f=15;var ne=8;if(dc.getDay()!=6||"
+"dc.getMonth()!=0){return'Data Not Available'}else{;z=parseInt(z);"
+"if(y=='2009'){f=8;ne=1};gmar=new Date('3/1/'+y);dsts=f-gmar.getDay("
+");gnov=new Date('11/1/'+y);dste=ne-gnov.getDay();spr=new Date('3/'"
+"+dsts+'/'+y);fl=new Date('11/'+dste+'/'+y);cd=new Date();"
+"if(cd>spr&&cd<fl){z=z+1}else{z=z};utc=cd.getTime()+(cd.getTimezoneO"
+"ffset()*60000);tz=new Date(utc + (3600000*z));thisy=tz.getFullYear("
+");var days=['Sunday','Monday','Tuesday','Wednesday','Thursday','Fr"
+"iday','Saturday'];if(thisy!=y){return'Data Not Available'}else{;thi"
+"sh=tz.getHours();thismin=tz.getMinutes();thisd=tz.getDay();var dow="
+"days[thisd];var ap='AM';var dt='Weekday';var mint='00';if(thismin>1"
+"5&&thismin<30){mint='15'}if(thismin>30&&thismin<45){mint='30'}if(th"
+"ismin>45&&thismin<60){mint='45'}"
+"if(thish>=12){ap='PM';thish=thish-12};if (thish==0){th"
+"ish=12};if(thisd==6||thisd==0){dt='Weekend'};var timestring=thish+'"
+":'+mint+ap;var daystring=dow;var endstring=dt;if(t=='h'){return tim"
+"estring}if(t=='d'){return daystring};if(t=='w'){return en"
+"dstring}}};"
);

/************* DO NOT ALTER ANYTHING BELOW THIS LINE ! **************/
var s_code='',s_objectID;function s_gi(un,pg,ss){var c="=fun^I(~){`Ls=^Z~$w ~.substring(~.indexOf(~;@t~`c@t~=new Fun^I(~.toLowerCase()~};s.~.length~s_c_il['+s@4n+']~=new Object~`aMigrationServer~.toU"
+"pperCase~){@t~`V$x=^O=s.`X`q=s.`X^c=`I^zobjectID=s.ppu=$8=$8v1=$8v2=$8v3=~','~s.wd~t^S~')q='~var ~s.pt(~=new Array~ookieDomainPeriods~.location~^KingServer~dynamicAccount~s.apv~='+#D~BufferedReques"
+"ts~);s.~)@tx^w!Object$pObject.prototype$pObject.prototype[x])~link~s.m_~Element~visitor~referrer~else ~.get#9()~}c#B(e){~.lastIndexOf(~.protocol~=new Date~=''~;@d^ss[k],255)}~javaEnabled~conne^I^c~"
+"^zc_i~:'')~onclick~}@t~Name~ternalFilters~javascript~s.dl~@9s.b.addBehavior(\"# default# ~for(~=parseFloat(~'+tm.get~typeof(v)==\"~window~cookie~s.rep(~s.vl_g~tfs~s.un~&&s.~o^zoid~browser~.parent~d"
+"ocument~colorDepth~String~while(~.host~s.maxDelay~r=s.m(f)?s[f](~s.sq~parseInt(~ction~t=s.ot(o)~track~nload~j='1.~#NURL~s.eo~lugins~'){q='~dynamicVariablePrefix~=='~set#9out(~Sampling~s.rc[un]~Even"
+"t~;i++)~');~this~resolution~}else{~Type~s.c_r(~s.c_w(~s.eh~s.isie~s.vl_l~s.vl_t~Secure~Height~t,h#Wt?t~tcf~isopera~ismac~escape(~.href~screen.~s.fl(~s=s_gi(~Version~harCode~&&(~_'+~variableProvider"
+"~.s_~f',~){s.~)?'Y':'N'~:'';h=h?h~._i~e&&l!='SESSION'~s_sv(v,n[k],i)}~name~home#N~;try{~s.ssl~s.oun~s.rl[u~Width~o.type~\"m_\"+n~Lifetime~s.gg('objectID~sEnabled~.mrq($run+'\"~ExternalLinks~charSet"
+"~onerror~currencyCode~.src~disable~etYear(~MigrationKey~&&!~Opera~'s_~Math.~s.fsg~s.$x~s.ns6~InlineStats~&&l!='NONE'~Track~'0123456789~s[k]=~'+n+'~loadModule~+\"_c\"]~s.epa(~t.m_nl~m._d~n=s.oid(o)~"
+",'sqs',q);~LeaveQuery~(''+~')>=~'=')~){n=~\",''),~&&t!='~if(~vo)~s.sampled~=s.oh(o);~+(y<1900?~n]=~true~sess~campaign~lif~ in ~'http~,100)~s.co(~ffset~s.pe~'&pe~m._l~s.c_d~s.brl~s.nrs~s.gv(~s[mn]~s"
+".qav~,'vo~s.pl~=(apn~Listener~\"s_gs(\")~vo._t~b.attach~2o7.net'~d.create~=s.n.app~n){~t&&~)+'/~s()+'~){p=~():''~a):f(~'+n;~+1))~a['!'+t]~){v=s.n.~channel~.target~x.split~o.value~[\"s_\"+g~s_si(t)~"
+"')dc='1~\".tl(\")~etscape~s_')t=t~omePage~s.d.get~')<~||!~'||~\"'+~[b](e);~\"){n[k]~a+1,b):~m[t+1](~return~lnk~mobile~height~events~random~code~wd.~=un~un,~,pev~'MSIE ~rs,~Time~floor(~atch~s.num(~s"
+".ape(~s.pg~m._e~s.c_gd~,'lt~.inner~transa~;s.gl(~idt='+~',s.bc~page~Group,~.fromC~sByTag~?'&~+';'~&&o~1);~}}}}~){t=~[t]=~[n];~>=5)~[t](~,1)!='~!a[t])~~s._c=@Uc';`I=`z`5!`I`m$R`I`ml`N;`I`mn=0;}s@4l="
+"`I`ml;s@4n=`I`mn;s@4l[s@4@ys;`I`mn++;s.m`0m){`2@nm)`4'{$o0`9fl`0x,l){`2x?@nx)`30,l):x`9co`0o`F!o)`2o;`Ln`C,x;`vx$3o)@tx`4'select$o0&&x`4'filter$o0)n[x]=o[x];`2n`9num`0x){x`i+x;`v`Lp=0;p<x`A;p++)@t("
+"@c')`4x`3p,p$Z<0)`20;`21`9rep=s_r;s.spf`0t,a){a[a`A]=t;`20`9sp`0x,d`1,a`N`5$e)a=$e(d);`c`Mx,d,'sp@0a);`2a`9ape`0x`1,h=@cABCDEF',i,c=s.@L,n,l,e,y`i;c=c?c`E$W`5x){x`i+x`5c^SAUTO'^w'').c^vAt){`vi=0;i<"
+"x`A^X{c=x`3i,i+#Un=x.c^vAt(i)`5n>127){l=0;e`i;^Cn||l<4){e=h`3n%16,n%16+1)+e;n=(n-n%16)/16;l++}y+='%u'+e}`6c^S+')y+='%2B';`cy+=^pc)}x=y^bx=x?^1^p''+x),'+`H%2B'):x`5x&&c^5em==1&&x`4'%u$o0&&x`4'%U$o0)"
+"{i=x`4'%^Y^Ci>=0){i++`5h`38)`4x`3i,i+1)`E())>=0)`2x`30,i)+'u00'+x`3i);i=x`4'%',i)#V`2x`9epa`0x`1;`2x?un^p^1''+x,'+`H ')):x`9pt`0x,d,f,a`1,t=x,z=0,y,r;^Ct){y=t`4d);y=y<0?t`A:y;t=t`30,y);^Ft,$Xt,a)`5"
+"r)`2r;z+=y+d`A;t=x`3z,x`A);t=z<x`A?t:''}`2''`9isf`0t,a){`Lc=a`4':')`5c>=0)a=a`30,c)`5t`30,2)^S$l`32);`2(t!`i&&t==a)`9fsf`0t,a`1`5`Ma,`H,'is@0t))@W+=(@W!`i?`H`n+t;`20`9fs`0x,f`1;@W`i;`Mx,`H,'fs@0f);"
+"`2@W`9si`0wd`1,c`i+s_gi,a=c`4\"{\"),b=c`f\"}\"),m;c=s_fe(a>0&&b>0?c`3$u0)`5wd&&#3^9&&c){#3^T'fun^I s_sv(o,n,k){`Lv=o[k],i`5v`F`ystring\"||`ynumber\")n[k]=v;`cif (`yarray$t`N;`vi=0;i<v`A^X@6`cif (`y"
+"object$t`C;`vi$3v)@6}}fun^I $h{`Lwd=`z,s,i,j,c,a,b;wd^zgi`7\"un\",\"pg\",\"ss\",$rc+'\");#3^t$r@B+'\");s=#3s;s.sa($r^4+'\"`V^3=wd;`M^2,\",\",\"vo1\",t`G\\'\\'`5t.m_l&&@i)`vi=0;i<@i`A^X{n=@i[i]`5$Rm"
+"=t#Yc=t[@F]`5m&&c){c=\"\"+c`5c`4\"fun^I\")>=0){a=c`4\"{\");b=c`f\"}\");c=a>0&&b>0?c`3$u0;s[@F@g=c`5#F)s.@f(n)`5s[n])`vj=0;j<$A`A;j++)s_sv(m,s[n],$A[j])#V}`Le,o,t@9o=`z.opener`5o#T^zgi#Wo^zgi($r^4+'"
+"\")`5t)$h}`e}',1)}`9c_d`i;#Gf`0t,a`1`5!#Ct))`21;`20`9c_gd`0`1,d=`I`P^D@7,n=s.fpC`O,p`5!n)n=s.c`O`5d@S$B@qn?^Hn):2;n=n>2?n:2;p=d`f'.')`5p>=0){^Cp>=0&&n>1$Vd`f'.',p-#Un--}$B=p>0&&`Md,'.`Hc_gd@00)?d`3"
+"p):d}}`2$B`9c_r`0k`1;k=#Dk);`Lc=' '+s.d.^0,i=c`4' '+k+@p,e=i<0?i:c`4';',i),v=i<0?'':@hc`3i+2+k`A,e<0?c`A:e));`2v!='[[B]]'?v:''`9c_w`0k,v,e`1,d=#G(),l=s.^0@G,t;v`i+v;l=l?@nl)`E$W`5@5@a#W(v!`i?^Hl?l:"
+"0):-60)`5t){e`h;e.set#9(e`d+(t*1000))}`pk@a@1d.^0=k+'`Tv!`i?v:'[[B]]')+'; path=/;'+(@5?' expires='+e.toGMT^B()#S`n+(d?' domain='+d#S`n;`2^dk)==v}`20`9eh`0o,e,r,f`1,b='s^xe+'^xs@4n,n=-1,l,i,x`5!^fl)"
+"^fl`N;l=^fl;`vi=0;i<l`A&&n<0;i++`Fl[i].o==o&&l[i].e==e)n=i`pn<0@qi;l[n]`C}x=l#Yx.o=o;x.e=e;f=r?x.b:f`5r||f){x.b=r?0:o[e];x.o[e]=f`px.b){x.o[b]=x.b;`2b}`20`9cet`0f,a,t,o,b`1,r,^m`5`S>=5^w!s.^n||`S>="
+"7)){^m`7's`Hf`Ha`Ht`H`Le,r@9^F$Xa)`er=s.m(t)?s#ae):t(e)}`2r^Yr=^m(s,f,a,t)^b@ts.^o^5u`4#74@o0)r=s.m(b)?s[b](a):b(a);else{^f(`I,'@M',0,o);^F$Xa`Veh(`I,'@M',1)}}`2r`9g^3et`0e`1;`2s.^3`9g^3oe`7'e`H`Ls"
+"=`B,c;^f(`z,\"@M\",1`Ve^3=1;c=s.t()`5c)s.d.write(c`Ve^3=0;`2@z'`Vg^3fb`0a){`2`z`9g^3f`0w`1,p=w^8,l=w`P;s.^3=w`5p&&p`P!=l&&p`P^D==l^D@1^3=p;`2s.g^3f(s.^3)}`2s.^3`9g^3`0`1`5!s.^3@1^3=`I`5!s.e^3)s.^3="
+"s.cet('g^3@0s.^3,'g^3et',s.g^3oe,'g^3fb')}`2s.^3`9mrq`0u`1,l=@C],n,r;@C]=0`5l)`vn=0;n<l`A;n++){r=l#Ys.mr(0,0,r.r,0,r.t,r.u)}`9br`0id,rs`1`5s.@P`U$p^e@Ubr',rs))$C=rs`9flush`U`0`1;s.fbr(0)`9fbr`0id`1"
+",br=^d@Ubr')`5!br)br=$C`5br`F!s.@P`U)^e@Ubr`H'`Vmr(0,0,br)}$C=0`9mr`0$0,q,#8id,ta,u`1,dc=s.dc,t1=s.`Q,t2=s.`Q^j,tb=s.`QBase,p='.sc',ns=s.`a`qspace,un=u?u:(ns?ns:s.fun),unc=^1#5'_`H-'),r`C,l,imn=@Ui"
+"^x(un),im,b,e`5!rs`Ft1`Ft2^5ssl)t1=t2^b@t!ns)ns#4c`5!tb)tb='$O`5dc)dc=@ndc)`8;`cdc='d1'`5tb^S$O`Fdc^Sd1$i12';`6dc^Sd2$i22';p`i}t1=ns+'.'+dc+'.'+p+tb}rs=$4'+(@A?'s'`n+'://'+t1+'/b/ss/'+^4+'/'+(s.$y?"
+"'5.1':'1'$TH.19.3/'+$0+'?AQB=1&ndh=1'+(q?q`n+'&AQE=1'`5^g@Ss.^o`F`S>5.5)rs=^s#84095);`crs=^s#82047)`pid@1br(id,rs);$w}`ps.d.images&&`S>=3^w!s.^n||`S>=7)^w@Y<0||`S>=6.1)`F!s.rc)s.rc`C`5!^V){^V=1`5!s"
+".rl)s.rl`C;@Cn]`N;^T'@t`z`ml)`z.`B@J)',750)^bl=@Cn]`5l){r.t=ta;r.u#4;r.r=rs;l[l`A]=r;`2''}imn+='^x^V;^V++}im=`I[imn]`5!im)im=`I[im@ynew Image;im^zl=0;im.o^L`7'e`H^Z^zl=1;`Lwd=`z,s`5wd`ml){s=#3`B;s@"
+"J`Vnrs--`5!$D)`Ym(\"rr\")}')`5!$D@1nrs=1;`Ym('rs')}`c$D++;im@O=rs`5rs`4$9=@o0^w!ta||ta^S_self$qta^S_top$q(`I.@7&&ta==`I.@7))){b=e`h;^C!im^zl&&e`d-b`d<500)e`h}`2''}`2'<im'+'g sr'+'c=$rrs+'\" width=1"
+" $z=1 border=0 alt=\"\">'`9gg`0v`1`5!`I['s^xv])`I['s^xv]`i;`2`I['s^xv]`9glf`0t,a`Ft`30,2)^S$l`32);`Ls=^Z,v=s.gg(t)`5v)s#Xv`9gl`0v`1`5#E)`Mv,`H,'gl@00)`9gv`0v`1;`2s['vpm^xv]?s['vpv^xv]:(s[v]?s[v]`n`"
+"9havf`0t,a`1,b=t`30,4),x=t`34),n=^Hx),k='g^xt,m='vpm^xt,q=t,v=s.`X@bVa#8e=s.`X@b^Ws,mn;@d$Et)`5s[k]`F$8||@X||^O`F$8){mn=$8`30,1)`E()+$8`31)`5$F){v=$F.^KVars;e=$F.^K^Ws}}v=v?v+`H+^h+`H+^h2:''`5v@S`M"
+"v,`H,'is@0t))s[k]`i`5`J#0'&&e)@ds.fs(s[k],e)}s[m]=0`5`J^R`KD';`6`J`aID`Kvid';`6`J^N^Qg'`j`6`J`b^Qr'`j`6`Jvmk$q`J`a@R`Kvmt';`6`J`D^Qvmf'`5@A^5`D^j)s[k]`i}`6`J`D^j^Qvmf'`5!@A^5`D)s[k]`i}`6`J@L^Qce'`5"
+"s[k]`E()^SAUTO')@d'ISO8859-1';`6s.em==2)@d'UTF-8'}`6`J`a`qspace`Kns';`6`Jc`O`Kcdp';`6`J^0@G`Kcl';`6`J^y`Kvvp';`6`J@N`Kcc';`6`J$c`Kch';`6`J#J^IID`Kxact';`6`J$1`Kv0';`6`J^a`Ks';`6`J^A`Kc';`6`J`s^u`Kj"
+"';`6`J`k`Kv';`6`J^0@I`Kk';`6`J^7@D`Kbw';`6`J^7^k`Kbh';`6`J`l`Kct';`6`J@8`Khp';`6`Jp^P`Kp';`6#Cx)`Fb^Sprop`Kc$Y`6b^SeVar`Kv$Y`6b^Slist`Kl$Y`6b^Shier^Qh'+n`j`ps[k]@s`X`q'@s`X^c')$G+='&'+q+'`Ts[k]);}`"
+"2''`9hav`0`1;$G`i;`M^i,`H,'hav@00);`2$G`9lnf`0^l`8@3`8:'';`Lte=t`4@p`5$Ste>0&&h`4t`3te$Z>=0)`2t`30,te);`2''`9ln`0h`1,n=s.`X`qs`5n)`2`Mn,`H,'ln@0h);`2''`9ltdf`0^l`8@3`8:'';`Lqi=h`4'?^Yh=qi>=0?h`30,q"
+"i):h`5$Sh`3h`A-(t`A$Z^S.'+t)`21;`20`9ltef`0^l`8@3`8:''`5$Sh`4t)>=0)`21;`20`9lt`0h`1,lft=s.`XDow^LFile^cs,lef=s.`XEx`r,$2=s.`XIn`r;$2=$2?$2:`I`P^D@7;h=h`8`5s.^KDow^LLinks&&lf$S`Mlft,`H#Hd@0h))`2'd'`"
+"5s.^K@K&&h`30#b# '^wlef||$2)^w!lef||`Mlef,`H#He@0h))^w!$2$p`M$2,`H#He@0h)))`2'e';`2''`9lc`7'e`H`Ls=`B,b=^f(^Z,\"`o\"`V$x=$6^Z`Vt(`V$x=0`5b)`2^Z$s`2@z'`Vbc`7'e`H`Ls=`B,f,^m`5s.d^5d.all^5d.all.cppXYc"
+"tnr)$w;^O=e@O`Z?e@O`Z:e$d;^m`7\"s\",\"`Le@9@t^O^w^O.tag`q||^O^8`Z||^O^8Node))s.t()`e}\");^m(s`Veo=0'`Voh`0o`1,l=`I`P,h=o^q?o^q:'',i,j,k,p;i=h`4':^Yj=h`4'?^Yk=h`4'/')`5h^wi<0||(j>=0&&i>j)||(k>=0&&i>"
+"k))$Vo`g#T`g`A>1?o`g:(l`g?l`g`n;i=l.path@7`f'/^Yh=(p?p+'//'`n+(o^D?o^D:(l^D?l^D`n)+(h`30#b/'?l.path@7`30,i<0?0:i$T'`n+h}`2h`9ot`0o){`Lt=o.tag`q;t=$St`E?t`E$W`5`JSHAPE')t`i`5t`F`JINPUT'&&@E&&@E`E)t="
+"@E`E();`6!$So^q)t='A';}`2t`9oid`0o`1,^J,p,c,n`i,x=0`5t@S^6$Vo`g;c=o.`o`5o^q^w`JA$q`JAREA')^w!c$pp||p`8`4'`s$o0))n@w`6c@q^1s.rep(^1s.rep@nc,\"\\r@r\"\\n@r\"\\t@r' `H^Yx=2}`6$f^w`JINPUT$q`JSUBMIT')@q"
+"$f;x=3}`6o@O&&`JIMAGE')n=o@O`5$R^6=^sn$5;^6t=x}}`2^6`9rqf`0t,un`1,e=t`4@p,u=e>=0?`H+t`30,e)+`H:'';`2u&&u`4`H+un+`H)>=0?@ht`3e$Z:''`9rq`0un`1,c#4`4`H),v=^d@Usq'),q`i`5c<0)`2`Mv,'&`Hrq@0un);`2`M#5`H,"
+"'rq',0)`9sqp`0t,a`1,e=t`4@p,q=e<0?'':@ht`3e+1)`Vsqq[q]`i`5e>=0)`Mt`30,e),`H@l`20`9sqs`0#5q`1;^Gu[u@yq;`20`9sq`0q`1,k=@Usq',v=^dk),x,c=0;^Gq`C;^Gu`C;^Gq[q]`i;`Mv,'&`Hsqp',0);`M^4,`H@lv`i;`vx$3^Gu`W)"
+"^Gq[^Gu[x]]+=(^Gq[^Gu[x]]?`H`n+x;`vx$3^Gq`W^5sqq[x]^wx==q||c<2)){v+=(v#R'`n+^Gq[x]+'`Tx);c++}`2^ek,v,0)`9wdl`7'e`H`Ls=`B,r=@z,b=^f(`I,\"o^L\"),i,o,oc`5b)r=^Z$s`vi=0;i<s.d.`Xs`A^X{o=s.d.`Xs[i];oc=o."
+"`o?\"\"+o.`o:\"\"`5(oc`4$L<0||oc`4\"^zoc(\")>=0)#Tc`4$j<0)^f(o,\"`o\",0,s.lc);}`2r^Y`Is`0`1`5`S>3^w!^g$ps.^o||`S#Z`Fs.b^5$N^W)s.$N^W('`o#M);`6s.b^5b.add^W$K)s.b.add^W$K('click#M,false);`c^f(`I,'o^L"
+"',0,`Il)}`9vs`0x`1,v=s.`a^U,g=s.`a^U#Ok=@Uvsn^x^4+(g?'^xg`n,n=^dk),e`h,y=e.g@Q);e.s@Qy+10@x1900:0))`5v){v*=100`5!n`F!^ek,x,e))`20;n=x`pn%10000>v)`20}`21`9dyasmf`0t,m`F$Sm&&m`4t)>=0)`21;`20`9dyasf`0"
+"t,m`1,i=t?t`4@p:-1,n,x`5i>=0&&m){`Ln=t`30,i),x=t`3i+1)`5`Mx,`H,'dyasm@0m))`2n}`20`9uns`0`1,x=s.`RSele^I,l=s.`RList,m=s.`RM#B,n,i;^4=^4`8`5x&&l`F!m)m=`I`P^D`5!m.toLowerCase)m`i+m;l=l`8;m=m`8;n=`Ml,'"
+";`Hdyas@0m)`5n)^4=n}i=^4`4`H`Vfun=i<0?^4:^4`30,i)`9sa`0un`1;^4#4`5!@B)@B#4;`6(`H+@B+`H)`4un)<0)@B+=`H+un;^4s()`9m_i`0n,a`1,m,f=n`30,1),r,l,i`5!`Yl)`Yl`C`5!`Ynl)`Ynl`N;m=`Yl[n]`5!a&&m&&#F@Sm@4)`Ya(n"
+")`5!m){m`C,m._c=@Um';m@4n=`I`mn;m@4l=s@4l;m@4l[m@4@ym;`I`mn++;m.s=s;m._n=n;$A`N('_c`H_in`H_il`H_i`H_e`H_d`H_dl`Hs`Hn`H_r`H_g`H_g1`H_t`H_t1`H_x`H_x1`H_rs`H_rr`H_l'`Vm_l[@ym;`Ynl[`Ynl`A]=n}`6m._r@Sm."
+"_m){r=m._r;r._m=m;l=$A;`vi=0;i<l`A^X@tm[l[i]])r[l[i]]=m[l[i]];r@4l[r@4@yr;m=`Yl[@yr`pf==f`E())s[@ym;`2m`9m_a`7'n`Hg`H@t!g)g=@F;`Ls=`B,c=s[g@g,m,x,f=0`5!c)c=`I$g@g`5c&&s_d)s[g]`7\"s\",s_ft(s_d(c)));"
+"x=s[g]`5!x)x=s[g]=`I$g];m=`Yi(n,1)`5x){m@4=f=1`5(\"\"+x)`4\"fun^I\")>=0)x(s);`c`Ym(\"x\",n,x)}m=`Yi(n,1)`5@jl)@jl=@j=0;`tt();`2f'`Vm_m`0t,n,d#W'^xt;`Ls=^Z,i,x,m,f='^xt`5`Yl&&`Ynl)`vi=0;i<`Ynl`A^X{x"
+"=`Ynl[i]`5!n||x==$Rm=`Yi(x)`5m[t]`F`J_d')`21`5d)m#ad);`cm#a)`pm[t+1]@Sm[f]`Fd)$vd);`c$v)}m[f]=1}}`20`9@f`0n,u,d,l`1,m,i=n`4':'),g=i<0?@F:n`3i+1),o=0,f,c=s.h?s.h:s.b,^m`5i>=0)n=n`30,i);m=`Yi(n)`5(l$"
+"p`Ya(n,g))&&u^5d&&c^5$P`Z`Fd){@j=1;@jl=1`p@A)u=^1u,$4:`Hhttps:^Yf`7'e`H`B.m_a(\"@e\",$rg+'\")^Y^m`7's`Hf`Hu`Hc`H`Le,o=0@9o=s.$P`Z(\"script\")`5o){@E=\"text/`s\"`5f)o.o^L=f;o@O=u;c.appendChild(o)}`e"
+"o=0}`2o^Yo=^m(s,f,u,c)}`cm=`Yi(n);#F=1;`2m`9vo1`0t,a`Fa[t]||$a)^Z#Xa[t]`9vo2`0t,a`F#c{a#X^Z[t]`5#c$a=1}`9dlt`7'`Ls=`B,d`h,i,vo,f=0`5`tl)`vi=0;i<`tl`A^X{vo=`tl[i]`5vo`F!`Ym(\"d\")||d`d-$M>=^E){`tl[i"
+"]=0;s.t(@u}`cf=1}`p`ti)clear#9out(`ti`Vdli=0`5f`F!`ti)`ti=^T`tt,^E)}`c`tl=0'`Vdl`0vo`1,d`h`5!@uvo`C;`M^2,`H$H2',@u;$M=d`d`5!`tl)`tl`N;`tl[`tl`A]=vo`5!^E)^E=250;`tt()`9t`0vo,id`1,trk=1,tm`h,sed=Math"
+"&&@V#1?@V#A@V#1()*10000000000000):tm`d,$0='s'+@V#Atm`d/10800000)%10+sed,y=tm.g@Q),vt=tm.getDate($T`xMonth($T'@xy+1900:y)+' `xHour$U:`xMinute$U:`xSecond$U `xDay()+' `x#9zoneO$7(),^m,^3=s.g^3(),ta`i,"
+"q`i,qs`i,#2`i,vb`C#K^2`Vuns()`5!s.td){`Ltl=^3`P,a,o,i,x`i,c`i,v`i,p`i,bw`i,bh`i,^M0',k=^e@Ucc`H@z',0@2,hp`i,ct`i,pn=0,ps`5^B&&^B.prototype){^M1'`5j.m#B){^M2'`5tm.setUTCDate){^M3'`5^g^5^o&&`S#Z^M4'`"
+"5pn.toPrecisio$R^M5';a`N`5a.forEach){^M6';i=0;o`C;^m`7'o`H`Le,i=0@9i=new Iterator(o)`e}`2i^Yi=^m(o)`5i&&i.next)^M7'#V`p`S>=4)x=^rwidth+'x'+^r$z`5s.isns||s.^n`F`S>=3$b`k(@2`5`S>=4){c=^rpixelDepth;bw"
+"=`I#I@D;bh=`I#I^k}}$I=s.n.p^P}`6^g`F`S>=4$b`k(@2;c=^r^A`5`S#Z{bw=s.d.^9`Z.o$7@D;bh=s.d.^9`Z.o$7^k`5!s.^o^5b){^m`7's`Htl`H`Le,hp=0`uh$m\");hp=s.b.isH$m(tl)?\"Y\":\"N\"`e}`2hp^Yhp=^m(s,tl);^m`7's`H`L"
+"e,ct=0`uclientCaps\");ct=s.b.`l`e}`2ct^Yct=^m(s)}}}`cr`i`p$I)^Cpn<$I`A&&pn<30){ps=^s$I[pn].@7$5#S`5p`4ps)<0)p+=ps;pn++}s.^a=x;s.^A=c;s.`s^u=j;s.`k=v;s.^0@I=k;s.^7@D=bw;s.^7^k=bh;s.`l=ct;s.@8=hp;s.p"
+"^P=p;s.td=1`p@u{`M^2,`H$H2',vb);`M^2,`H$H1',@u`ps.useP^P)s.doP^P(s);`Ll=`I`P,r=^3.^9.`b`5!s.^N)s.^N=l^q?l^q:l`5!s.`b@Ss._1_`b@1`b=r;s._1_`b=1}`Ym('g')`5(vo&&$M)$p`Ym('d')`F@X||^O){`Lo=^O?^O:@X`5!o)"
+"`2'';`Lp=$E'#N`q'),w=1,^J,@k,x=^6t,h,l,i,oc`5^O#T==^O){^Co@Sn@sBODY'){o=o^8`Z?o^8`Z:o^8Node`5!o)`2'';^J;@k;x=^6t}oc=o.`o?''+o.`o:''`5(oc`4$L>=0#Tc`4\"^zoc(\")<0)||oc`4$j>=0)`2''}ta=n?o$d:1;h@wi=h`4"
+"'?^Yh=s.`X@m^B||i<0?h:h`30,i);l=s.`X`q?s.`X`q:s.ln(h);t=s.`X^c?s.`X^c`8:s.lt(h)`5t^wh||l))q+=$9=$x^x(`Jd$q`Je'?#Dt):'o')+(h?$9v1`Th)`n+(l?$9v2`Tl)`n;`ctrk=0`5s.^K@Z`F!p$V$E'^N^Yw=0}^J;i=o.sourceInd"
+"ex`5@H')@q@H^Yx=1;i=1`pp&&n&&t)qs='&pid`T^sp,255))+(w#Rp#Lw`n+'&oid`T^sn$5)+(x#Ro#Lx`n+'&ot`Tt)+(i#Roi='+i`n}`p!trk@Sqs)`2'';@v=s.vs(sed)`5trk`F@v)#2=s.mr($0,(vt#Rt`Tvt)`n+s.hav()+q+(qs?qs:s.rq(^4)"
+"),0,id,ta);qs`i;`Ym('t')`5s.p_r)s.p_r(`V`b`i}^G(qs);^b`t(@u;`p@u`M^2,`H$H1',vb`G''`5#E)`I^z$x=`I^zeo=`I^z`X`q=`I^z`X^c`i`5!id@Ss.tc@1tc=1;s.flush`U()}`2#2`9tl`0o,t,n,vo`1;@X=$6o`V`X^c=t;s.`X`q=n;s."
+"t(@u}`5pg){`I^zco`0o){`L^t\"_\",1,#U`2$6o)`9wd^zgs`0u$R`L^t#51,#U`2s.t()`9wd^zdc`0u$R`L^t#5#U`2s.t()}}@A=(`I`P`g`8`4$4s@o0`Vd=^9;s.b=s.d.body`5$n`Z#Q`q@1h=$n`Z#Q`q('HEAD')`5s.h)s.h=s.h[0]}s.n=navig"
+"ator;s.u=s.n.userAgent;@Y=s.u`4'N$k6/^Y`Lapn$Q`q,v$Q^u,ie=v`4#7'),o=s.u`4'@T '),i`5v`4'@T@o0||o>0)apn='@T';^g$J^SMicrosoft Internet Explorer'`Visns$J^SN$k'`V^n$J^S@T'`V^o=(s.u`4'Mac@o0)`5o>0)`S`ws."
+"u`3o+6));`6ie>0){`S=^Hi=v`3ie+5))`5`S>3)`S`wi)}`6@Y>0)`S`ws.u`3@Y+10));`c`S`wv`Vem=0`5^B#P^v){i=^p^B#P^v(256))`E(`Vem=(i^S%C4%80'?2:(i^S%U0100'?1:0))}s.sa(un`Vvl_l='^R,`aID,vmk,`a@R,`D,`D^j,ppu,@L,"
+"`a`qspace,c`O,^0@G,#N`q,^N,`b,@N';^i=^h+',^y,$c,server,#N^c,#J^IID,purchaseID,$1,state,zip,#0,products,`X`q,`X^c';`v`Ln=1;n<51;n++)^i+=',prop@e,eVar@e,hier@e,list$Y^h2=',tnt,pe#61#62#63,^a,^A,`s^u,"
+"`k,^0@I,^7@D,^7^k,`l,@8,p^P';^i+=^h2;^2=^i+',`Q,`Q^j,`QBase,fpC`O,@P`U,$y,`a^U,`a^U#O`RSele^I,`RList,`RM#B,^KDow^LLinks,^K@K,^K@Z,`X@m^B,`XDow^LFile^cs,`XEx`r,`XIn`r,`X@bVa#8`X@b^Ws,`X`qs,$x,eo,_1_"
+"`b';#E=pg#K^2)`5!ss)`Is()",
w=window,l=w.s_c_il,n=navigator,u=n.userAgent,v=n.appVersion,e=v.indexOf('MSIE '),m=u.indexOf('Netscape6/'),a,i,s;if(un){un=un.toLowerCase();if(l)for(i=0;i<l.length;i++){s=l[i];if(s._c=='s_c'){if(s.oun==un)return s;else if(s.fs&&s.sa&&s.fs(s.oun,un)){s.sa(un);return s}}}}
w.s_r=new Function("x","o","n","var i=x.indexOf(o);if(i>=0&&x.split)x=(x.split(o)).join(n);else while(i>=0){x=x.substring(0,i)+n+x.substring(i+o.length);i=x.indexOf(o)}return x");
w.s_d=new Function("x","var t='`^@$#',l='0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz',d,n=0,b,k,w,i=x.lastIndexOf('~~');if(i>0){d=x.substring(0,i);x=x.substring(i+2);while(d){w=d;i"
+"=d.indexOf('~');if(i>0){w=d.substring(0,i);d=d.substring(i+1)}else d='';b=(n-n%62)/62;k=n-b*62;k=t.substring(b,b+1)+l.substring(k,k+1);x=s_r(x,k,w);n++}for(i=0;i<5;i++){w=t.substring(i,i+1);x=s_r(x"
+",w+' ',w)}}return x");
w.s_fe=new Function("c","return s_r(s_r(s_r(c,'\\\\','\\\\\\\\'),'\"','\\\\\"'),\"\\n\",\"\\\\n\")");
w.s_fa=new Function("f","var s=f.indexOf('(')+1,e=f.indexOf(')'),a='',c;while(s>=0&&s<e){c=f.substring(s,s+1);if(c==',')a+='\",\"';else if((\"\\n\\r\\t \").indexOf(c)<0)a+=c;s++}return a?'\"'+a+'\"':"
+"a");
w.s_ft=new Function("c","c+='';var s,e,o,a,d,q,f,h,x;s=c.indexOf('=function(');while(s>=0){s++;d=1;q='';x=0;f=c.substring(s);a=s_fa(f);e=o=c.indexOf('{',s);e++;while(d>0){h=c.substring(e,e+1);if(q){i"
+"f(h==q&&!x)q='';if(h=='\\\\')x=x?0:1;else x=0}else{if(h=='\"'||h==\"'\")q=h;if(h=='{')d++;if(h=='}')d--}if(d>0)e++}c=c.substring(0,s)+'new Function('+(a?a+',':'')+'\"'+s_fe(c.substring(o+1,e))+'\")"
+"'+c.substring(e+1);s=c.indexOf('=function(')}return c;");
c=s_d(c);if(e>0){a=parseInt(i=v.substring(e+5));if(a>3)a=parseFloat(i)}else if(m>0)a=parseFloat(u.substring(m+10));else a=parseFloat(v);if(a>=5&&v.indexOf('Opera')<0&&u.indexOf('Opera')<0){w.s_c=new Function("un","pg","ss","var s=this;"+c);return new s_c(un,pg,ss)}else s=new Function("un","pg","ss","var s=new Object;"+s_ft(c)+";return s");return s(un,pg,ss)}



if(typeof ncbi=="undefined"){ncbi={}}ncbi.sg=function(){};ncbi.sg.getInstance=function(){if(!ncbi.sg._instance){ncbi.sg._instance=new ncbi.sg()}return ncbi.sg._instance};ncbi.sg._instance=null;ncbi.sg.omnDictMap={pagename:["pageName"],ncbi_app:["channel"],server:["server"],ncbi_pagetype:["pageType"],ncbi_phid:["prop1"],ncbi_db:["prop2","prop4"],ncbi_term:["prop3"],ncbi_type:["prop4"],ncbi_report:["prop5"],ncbi_pagesize:["prop6"],ncbi_sortorder:["prop7"],ncbi_pageno:["prop8"],formtype:["prop9"],ncbi_resultcount:["prop10"],ncbi_linkname:["prop11"],ncbi_dbfrom:["prop12"],linkid:["prop13"],linkname:["prop14"],linksid:["prop15"],ncbi_op:["prop16"],ncbi_limits:["prop17"],ncbi_limitvalues:["prop19"],ncbi_pdid:["prop21"],ncbi_sessionid:["prop22"],sitesect2:["prop23"],subsect3:["prop24"],subsect4:["prop25"],ncbi_pcid:["prop26"],jserror:["prop27"],jserrortype:["prop28"],ncbi_servererror:["prop29"],ncbi_servererrortype:["prop30"],jsloadtime:["prop31"],jsevent:["prop37"],ncbi_filter:["prop38"],adid:["prop39"],adtargetid:["prop40"],heir1:["heir1"],ncbi_reload:["prop41"],ncbi_opsource:["prop42"],prev_phid:["prop43"]};ncbi.sg.appLogIgnore=["ncbi_sessionid","ncbi_clickdisabled"];ncbi.sg.prototype={init:function(){this.isDebugMode=((this.getCookie("debugger").length>0)&&console&&console.info);this.isProcessRunning=true;this._setUpOmnValues();this._setUpPathParts();this._setUpCustomProps();this.send("init")},_vals:{},_applogVals:{},_cachedVals:{},_hasInitRun:false,_pathParts:{part1:"",part2:"",part3:"",part4:""},_setUpOmnValues:function(){var C=document.getElementsByTagName("meta");for(var A=0;A<C.length;A++){var B=C[A].name;if(B.indexOf("ncbi_")===0){var D=C[A].content;this.addEntry(B,D)}}},_setUpSubsetOmnValues:function(A){for(var B=0;B<A.length;B++){var C=this._cachedVals[A[B]];if(C){this.addEntry(A[B],C.value)}}},addEntry:function(B,C){if(C===undefined||C.length===0){return }else{if(C.length>100){C=C.substr(0,100)}}var A=ncbi.sg.omnDictMap[B];if(A){this._vals[B]={sProp:A,value:C}}else{this._applogVals[B]={sProp:B,value:C}}this._cachedVals[B]={sProp:(A||B),value:C}},removeAllEntries:function(){this._vals={};this._applogVals={};for(var A in this._pathParts){this._pathParts[A]=""}},_setUpCustomProps:function(){var C=this._pathParts.part1;var A=(this._pathParts.part2.length>0)?":"+this._pathParts.part2:"";var F=(this._pathParts.part3.length>0)?":"+this._pathParts.part3:"";var D=(this._pathParts.part4.length>0)?":"+this._pathParts.part4:"";var B={pagename:C+A+F,server:window.location.hostname,sitesect2:C+A,subsect3:C+A+F,subsect4:C+A+F+D,heir1:(C+A+F+D).replace(/:/g,"|")};for(var E in B){this.addEntry(E,B[E])}this._sessionIdCheck();this._staticPageCheck();this._reloadCheck();this._prevHitCheck();this._configCheck()},_staticPageCheck:function(){if(!(this._vals.ncbi_app&&this._vals.ncbi_app.value.length>0)){this.addEntry("ncbi_app","static")}if(!(this._vals.ncbi_pdid&&this._vals.ncbi_pdid.value.length>0)){var A=(document.title||"unknown").replace(/\s+/g,"");this.addEntry("ncbi_pdid",A)}},_sessionIdCheck:function(){if(!(this._vals.ncbi_sessionid&&this._vals.ncbi_sessionid.value.length>0)){var A="";if(A.length===0){var B=this.getCookie("WebCubbyUser")||this.getCookie("WebEnv");if(B.length>0){B=unescape(B).split("@");if(B.length>1){A=B[B.length-1]}}}if(A.length===0){A="NOSESSIONID"}this.addEntry("ncbi_sessionid",A)}},_reloadCheck:function(){var A=this.getCookie("ncbi_reload");var B="null";if(A.length>0){A=parseInt(A,10)+1;B=(A>1)?"true":"false";this.setCookie("ncbi_reload",A)}this.addEntry("ncbi_reload",B)},_configCheck:function(){var B=window.innerWidth!==null?window.innerWidth:document.documentElement&&document.documentElement.clientWidth?document.documentElement.clientWidth:document.body!==null?document.body.clientWidth:"NA";var A=window.innerHeight!==null?window.innerHeight:document.documentElement&&document.documentElement.clientHeight?document.documentElement.clientHeight:document.body!==null?document.body.clientHeight:"NA";this.addEntry("browserwidth",B);this.addEntry("browserheight",A);this.addEntry("screenwidth",screen.width);this.addEntry("screenheight",screen.height);this.addEntry("screenavailwidth",screen.availWidth);this.addEntry("screenavailheight",screen.availHeight);if(screen.colorDepth){this.addEntry("colorDepth",screen.colorDepth)}else{if(screen.pixelDepth){this.addEntry("colorDepth",screen.pixelDepth)}}},_createPHID:function(){var B=this._vals.ncbi_sessionid;var A=B.value;var F=A.substr(0,15)+"9"+new Date().getTime().toString();var E=32-F.length;var C=A.length;F+=A.substr(C-E,C);var D={value:F};this.addEntry("ncbi_phid",F);return D},currentPageHitId:null,_prevHitCheck:function(){var D=this.getCookie("ncbi_prevPHID");var C=this._vals.ncbi_phid;if(D.length>0){this.addEntry("prev_phid",D)}if(!C||!C.value||C.value.length===0){C=this._createPHID()}this.currentPageHitId=C.value;var B=this;if(ncbi.sg._hasFocus){B.setCookie("ncbi_prevPHID",C.value)}var A=window.onfocus;window.onfocus=function(E){if(B.getCookie("ncbi_prevPHID")!==C.value){B.setCookie("ncbi_prevPHID",C.value)}if(typeof A==="function"){A(E)}}},_setUpPathParts:function(){var A=this._vals.ncbi_app;var D=this._vals.ncbi_db;var B=this._vals.ncbi_pdid;var C=this._vals.ncbi_pcid;this._pathParts.part1=(A!==undefined)?A.value:"";this._pathParts.part2=(D!==undefined)?D.value:"";this._pathParts.part3=(B!==undefined)?B.value:"";this._pathParts.part4=(C!==undefined)?C.value:""},send:function(A,E){var L=[];if(A==="init"){L.push("jsevent=render")}for(var B in this._vals){if(ncbi.sg.appLogIgnore.indexOf(B)!==-1){continue}var H=this._vals[B];var D=this._vals[B].value;var I=this._vals[B].sProp;for(var J=0;J<I.length;J++){s[I[J]]=D}L.push(B+"="+encodeURIComponent(D))}for(B in this._applogVals){if(ncbi.sg.appLogIgnore.indexOf(B)!==-1){continue}var N=this._applogVals[B];L.push(N.sProp+"="+encodeURIComponent(N.value))}var G=this._areOmnEventsSG();L.push("wasOmnPinged="+G);if(!this._hasInitRun){var F=unescape(this.getCookie("s_sess"));F=F.split("%26");for(var J=0;J<F.length;J++){B=F[J].split("%3D");if(B[0]==="pid"||B[0]==="oid"){F[J]="omn_"+F[J];L.push(unescape(F[J]))}}var M=document.referrer;if(M.length>0){L.push("ncbi_referrer="+encodeURIComponent(M))}}var K=L.join("&");if(this.isDebugMode){console.info("sg Data:\n\n"+L.join("\n"))}if(s_account!=="none"){if(G){this.sendOmn(A)}this.sendAl(K,E,true);this._hasInitRun=true;var C=this;setTimeout(function(){C.isProcessRunning=false;C.runSGProcess()},300)}},sendOmn:function(B){if(this.isDebugMode){console.info("Setting Omn")}if(B==="init"){var A=s.t();if(A){document.write(A)}}else{if(this._areEventsSG()){s.tl()}}},sendAl:function(G,D,F){if(this.isDebugMode){console.info("al Sent: \n"+G)}var E=this;var C=(window.location.port.length>0)?":"+window.location.port:"";var A=window.location.protocol+"//"+window.location.hostname+C+"/stat?"+G;if(document.all&&!F){var B=new Image(1,1);B.src=A}else{this.makeAjaxCall(A,function(){if(E.isDebugMode){console.info("al: done")}if(typeof D==="function"){D()}},F)}},_processingQueue:[],isProcessRunning:false,addSGProcess:function(A){this._processingQueue.push(A)},getSGProcess:function(A){return this._processingQueue.shift(A)},runSGProcess:function(){if(this.isProcessRunning||this._processingQueue.length===0||!this._hasInitRun){return false}this.isProcessRunning=true;this.removeAllEntries();var C=this.getSGProcess();s=s_gi(s_account);s.linkTrackVars="prop13,prop14,prop15";s.linkTrackEvents="None";this._setUpSubsetOmnValues(C.metadata);if(this.isDebugMode){console.info("Now sg "+C.eventName+" to the server")}this.addEntry("jsevent",C.eventName);for(var B in C.props){this.addEntry(B,C.props[B])}this.send(C.eventName,C.callbackFnc);var A=this;setTimeout(function(){A.isProcessRunning=false;A.runSGProcess()},300)},noteEventData:function(D,C,A,B){if(this.isDebugMode){console.info("Adding "+D+" to the queue")}this.addSGProcess({eventName:D,props:C,metadata:A,callback:B});this.runSGProcess()},_isSG:null,_areEventsSG:function(){if(this._isSG===null){var E=null;var D=document.getElementsByTagName("meta");for(var B=0;B<D.length;B++){var C=D[B].name;if(C==="ncbi_stat"){E=D[B].content;break}}if(E==="true"||this.getCookie("NCBISTATON")==="true"){this._isSG=true}else{if(E==="false"){this._isSG=false}else{if(!isNaN(E)){var A=this.getCookie("NCBISTATG");if(A.length>0){this._isSG=(A==="true")?true:false}else{var G=parseFloat(E);var F=Math.floor(Math.random()*100);this._isSG=(F<=G);this.setCookie("NCBISTATG",this._isSG)}}else{this._isSG=false}}}}return this._isSG},_isOmnSG:null,_areOmnEventsSG:function(){if(this._isOmnSG===null){var E=null;var D=document.getElementsByTagName("meta");for(var B=0;B<D.length;B++){var C=D[B].name;if(C==="ncbi_hitstat"){E=D[B].content;break}}if(E==="true"||this.getCookie("NCBISTATOMNON")==="true"){this._isOmnSG=true;this.setCookie("NCBISTATOMNON",this._isOmnSG)}else{if(E==="false"){this._isOmnSG=false;this.setCookie("NCBISTATOMNON",this._isOmnSG)}else{if(!isNaN(E)){var A=this.getCookie("NCBISTATOMNON");if(A.length>0){this._isOmnSG=(A==="true")?true:false}else{var G=parseFloat(E);var F=Math.floor(Math.random()*100);this._isOmnSG=(F<=G);this.setCookie("NCBISTATOMNON",this._isOmnSG)}}else{var A=this.getCookie("NCBISTATOMNON");if(A.length>0){this._isOmnSG=(A==="true")?true:false}else{this._isOmnSG=false}}}}}return this._isOmnSG},setCookie:function(B,C,A){var D=new Date();if(A!==null){D.setDate(D.getDate()+A)}document.cookie=B+"="+escape(C)+((A===null)?"":"; expires="+D.toGMTString())+"; domain="+escape(".nih.gov")+"; path=/"},getCookie:function(B){if(document.cookie.length>0){var C=document.cookie.indexOf(B+"=");if(C!=-1){C=C+B.length+1;var A=document.cookie.indexOf(";",C);if(A==-1){A=document.cookie.length}return unescape(document.cookie.substring(C,A))}}return""},getTransport:function(){var A=null;if(window.XMLHttpRequest){try{A=new XMLHttpRequest();this.getTransport=function(){return new XMLHttpRequest()}}catch(D){A=null}}if(window.ActiveXObject&&A===null){try{A=new ActiveXObject("Msxml2.XMLHTTP");this.getTransport=function(){return new ActiveXObject("Msxml2.XMLHTTP")}}catch(C){try{A=new ActiveXObject("Microsoft.XMLHTTP");this.getTransport=function(){return new ActiveXObject("Microsoft.XMLHTTP")}}catch(B){A=false}}}if(A===null){this.getTransport=function(){return null}}return this.getTransport()},makeAjaxCall:function(A,D,B){var C=this.getTransport();C.open("GET",A,B);if(B){C.onreadystatechange=function(){if(C.readyState===4){D(C)}}}C.send(null);if(!B){if(typeof D==="function"){D(C)}}}};ncbi.sg.hasNotedErrorEvent=false;(function(){var P=window.onerror;window.onerror=function(R,Q,S){if(this.isDebugMode&&console.warn){console.warn("sg error to server to be investigated: \n"+R+"\n"+Q+"\n"+S)}if(ncbi.sg.hasNotedErrorEvent){if(this.isDebugMode){console.info("Already found one error for this page, not sending another error report")}return }ncbi.sg.getInstance().noteEventData("jserror",{jserror:S+": "+R,jserrortype:Q},["ncbi_sessionid","ncbi_phid"]);ncbi.sg.hasNotedErrorEvent=true;if(typeof P==="function"){return P(R,Q,S)}};var L=window.onunload;window.onunload=function(Q){H();if(typeof L==="function"){L(Q)}};var E=[];var O=[];var G=[];function A(S){var T=E.length;var Q=G.length;var R=T>0?E[T-1]:Q>0?G[Q-1]:null;if(R!==null){if(S.elem==R.elem){if((R.floodTstamp&&S.floodTstamp-R.tstamp<=400)||(S.tstamp-R.tstamp<=400)){R.floodTstamp=new Date().getTime();return false}}R.floodTstamp=new Date().getTime()}E.push(S);return true}function H(){ncbi.sg.getInstance().setCookie("ncbi_prevPHID",ncbi.sg.getInstance().currentPageHitId);var Q=ncbi.sg.clickTimers;while(Q>0){var R=Q.shift();if(R){window.clearTimeout(R)}}if(E.length>0){while(E.length>0){N(E.pop(),["ncbi_pingaction=unload"])}}else{ncbi.sg.ping({jsevent:"unload",ncbi_pingaction:"unload"},false)}}function I(Q){if(!Q){return }if(Q.onclick){Q.onclick_=Q.onclick}if(Q.oncontextmenu){Q.oncontextmenu_=Q.oncontextmenu}}function D(R){for(var Q=E.length-1;Q>=-1;Q--){if(E[Q]==R){E.slice(Q,1);break}}N(R,["ncbi_pingaction=timer"])}function N(f,b){if(O.indexOf(f.tstamp)!==-1){return }O.push(f.tstamp);G.push(f);var h=f.link;var Y=f.evt;var U=h.id||"";var V=h.name||"";var X=h.sid||"";var S=h.href||"";var Q=h.innerText||h.textContent||"";if(Q.length>50){Q=Q.substr(0,50)}var Z=h.getAttribute("ref")||h.ref||"";var c=h.className.split(/\s/g).join(",")||"";var d=ncbi.sg.getInstance().currentPageHitId||"";var j=[];if(U.length>0){j.push("link_id="+encodeURIComponent(U))}if(V.length>0){j.push("link_name="+encodeURIComponent(V))}if(X.length>0){j.push("link_sid="+encodeURIComponent(X))}if(S.length>0){j.push("link_href="+encodeURIComponent(S))}if(Q.length>0){j.push("link_text="+encodeURIComponent(Q))}if(c.length>0){j.push("link_class="+encodeURIComponent(c))}if(Y.link_coor_x.toString().length>0){j.push("link_coor_x="+Y.link_coor_x.toString())}if(Y.link_coor_y.toString().length>0){j.push("link_coor_y="+Y.link_coor_y.toString())}if(Y.link_scroll_x.toString().length>0){j.push("link_scroll_x="+Y.link_scroll_x.toString())}if(Y.link_scroll_y.toString().length>0){j.push("link_scroll_y="+Y.link_scroll_y.toString())}j.push("jsevent=click");if(Z.length>0){j.push(Z)}if(typeof jQuery!=="undefined"){var T=jQuery(h).attr("sg");if(T){var e=T.split(/\}\s*,\s*\{/);for(var a=0;a<e.length;a++){var g=e[a].match(/name\s*:\s*'(.+)',\s*selector\s*:\s*'(.+)'/);if(g.length!==3){continue}var W="cust_"+g[1];var R=jQuery(g[2]).val();j.push(W+"="+encodeURIComponent(R))}}}if(b&&b.length>0){while(b.length>0){j.push(b.shift())}}ncbi.sg.ping(j,false)}ncbi.sg.clickTimers=[];function M(R,S,X){function Q(g,a,e,Y){ncbi.sg.getInstance().setCookie("ncbi_prevPHID",ncbi.sg.getInstance().currentPageHitId);var d={};if(g.clientX||g.clientY){d.link_coor_x=g.clientX+document.body.scrollLeft;d.link_coor_y=g.clientY+document.body.scrollTop}else{d.link_coor_x=g.pageX;d.link_coor_y=g.pageY}d.link_scroll_x=document.body.scrollLeft||0;d.link_scroll_y=document.body.scrollTop||0;if(d.link_scroll_x===0&&d.link_scroll_y===0){if(window.pageXOffset||window.pageYOffset){d.link_scroll_x=window.pageXOffset;d.link_scroll_y=window.pageYOffset}else{if(document.body.parentElement){d.link_scroll_x=document.body.parentElement.scrollLeft||0;d.link_scroll_y=document.body.parentElement.scrollTop||0}}}var f={evt:d,link:a,tstamp:new Date().getTime(),floodTstamp:new Date().getTime()};var c=A(f);if(c){ncbi.sg.clickTimers.push(window.setTimeout(function(){D(f)},500))}var b=null;function Z(h){var m=function(i){if(typeof i.preventDefault==="undefined"){i.preventDefault=function(){this.returnValue=false}}if(typeof i.stopPropagation==="undefined"){i.stopPropagation=function(){this.cancelBubble=true}}return i};var j=m(g);var l=[j];for(var k=0;k<e.length;k++){l.push(e[k])}b=a[h].apply(a,l)}if(Y==="click"&&typeof a.onclick_==="function"){Z("onclick_")}else{if(Y==="contextmenu"&&typeof a.oncontextmenu_==="function"){Z("oncontextmenu_")}}if(b!==null||b!==undefined){return b}}for(var U=0;U<20&&S<X;U++){var W=R[S];if(W&&!W._sg){W._sg=true;I(W);W.onclick=function(a){var Y=a||window.event;var Z=null;Z=Q(Y,this,arguments,"click");if(Z!==null||Z!==undefined){return Z}};W.oncontextmenu=function(a){var Y=a||window.event;var Z=null;Z=Q(Y,this,arguments,"contextmenu");if(Z!==null||Z!==undefined){return Z}}}S++}if(S<X){var V=function(){M(R,S,X)};var T=window.setTimeout(V,0)}}if(typeof ncbi.sg.isClickEnabled==="undefined"){var J=document.getElementsByTagName("meta");var F=J.length-1;var K=true;while(F>=0){if(J[F].name.toLowerCase()==="ncbi_clickdisabled"){K=J[F].content.toLowerCase()==="false";break}F--}ncbi.sg.isClickEnabled=K}function B(){if(ncbi.sg.isClickEnabled){var T=document.links;M(T,0,T.length);var W=document.getElementsByTagName("button");M(W,0,W.length);var R=document.getElementsByTagName("input");var Q=[];var S=R.length-1;while(S>=0){var V=R[S];var U=V.type;if(U==="button"||U==="submit"||U==="reset"){Q.push(V)}S--}M(Q,0,Q.length)}}B();ncbi.sg.scanLinks=function(Q){if(Q){if(typeof Q==="object"&&!(Q instanceof Array)){Q=[Q]}M(Q,0,Q.length)}else{B()}};ncbi.sg.ping=function(T,S){if(typeof T==="undefined"||T===null){return }if(typeof S==="undefined"){S=true}var R=[];if(typeof T==="object"&&!(T instanceof Array)){for(var U in T){R.push(U+"="+encodeURIComponent(T[U]))}}else{if(typeof T==="string"){R.push(T)}else{R=T}}var Q=ncbi.sg.getInstance().currentPageHitId||"";if(Q.length>0){R.push("ncbi_phid="+encodeURIComponent(Q))}if(typeof ncbi.sg.loadTime!=="undefined"){R.push("ncbi_timesinceload="+(new Date()-ncbi.sg.loadTime))}ncbi.sg.getInstance().sendAl(R.join("&"),null,S)};ncbi.sg.loadTime=new Date();if(typeof ncbi_startTime!=="undefined"){var C=ncbi.sg.loadTime-ncbi_startTime;ncbi.sg.getInstance().noteEventData("loadtime",{jsloadtime:C},["ncbi_sessionid","ncbi_phid"])}})();if(!Array.prototype.indexOf){Array.prototype.indexOf=function(B){var A=this.length>>>0;var C=Number(arguments[1])||0;C=(C<0)?Math.ceil(C):Math.floor(C);if(C<0){C+=A}for(;C<A;C++){if(C in this&&this[C]===B){return C}}return -1}};
// This code creates window.console if it doesn't exist.
// It also creates stub functions for those functions that are missing in window.console.
// (Safari implements some but not all of the firebug window.console methods--this implements the rest.)
(function() {
    var names = [ "log", "debug", "info", "warn", "error", "assert", "dir", "dirxml", "group",
                  "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd" ];

    if (typeof(console) === 'undefined' || typeof console === "function" ) {
      //"typeof function" is needed see PP-769 
      console = {};
    }

    for (var i = 0; i < names.length; ++i) {
       if (typeof(console[names[i]]) === 'undefined') {
          console[names[i]] = function() { return false; };
       }
    }
    ncbi.sg.getInstance().init();                          
})();
