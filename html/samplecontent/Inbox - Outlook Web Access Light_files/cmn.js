var AB_LKUP=1;var EDT_MSG=2;var EDT_CAL=3;var EDT_MTR=4;function isEdtMd(iMd){return iMd==EDT_MSG||iMd==EDT_CAL||iMd==EDT_MTR;}
function logOff(sCnr){nv("logoff.owa?canary="+urlEnc(sCnr));}
function onClkBrwsFldNv(){var o=getEm("selbrfld");var sHrf="?ae=Folder&t="+o.value+"&slUsng=1";nv(sHrf);return false;}
function isSfAscChr(iCh){return ((iCh>=0x30&&iCh<=0x39)||(iCh>=0x41&&iCh<=0x5A)||(iCh>=0x61&&iCh<=0x7A)||(iCh==0x21)||(iCh>=0x27&&iCh<=0x2A)||(iCh==0x2D)||(iCh==0x2E)||(iCh==0x5F)||(iCh==0x7E));}
function toUTF8(szInput){var wch,x,uch="",szRet="";if(szInput==null)return "";if(typeof (szInput)=="number")szInput=""+szInput;for(x=0;x<szInput.length;x++){wch=szInput.charCodeAt(x);if(isSfAscChr(wch)){szRet+=szInput.charAt(x);}else if(!(wch&0xFF80)){szRet+="%"+wch.toString(16);}else if(!(wch&0xF800)){uch="%"+(wch>>6|0xC0).toString(16)+"%"+(wch&0x3F|0x80).toString(16);szRet+=uch;}else {uch="%"+(wch>>12|0xE0).toString(16)+"%"+(((wch>>6)&0x3F)|0x80).toString(16)+"%"+(wch&0x3F|0x80).toString(16);szRet+=uch;}}return (szRet);}
function urlEnc(s){return toUTF8(s);}
var g_fRqtlck=false;function isRqtLck(){return g_fRqtlck;}
function lckRqt(){var f=g_fRqtlck;g_fRqtlck=true;return f;}
function nv(sHrf){if(!lckRqt()){document.location.href=sHrf;}return false;}
var NM_ML=0;var NM_CLD=1;var NM_CNT=2;function nvMdl(iM){var s="?ae=Folder&t=";switch(iM){case NM_ML:nv(s+"IPF.Note");break;case NM_CLD:nv(s+"IPF.Appointment");break;case NM_CNT:nv(s+"IPF.Contact");break;}}
function sbtFrm(o){if(!lckRqt()&&o["hidcanary"]){o.submit();}}
function getEm(sId){return document.getElementById(sId);}
function showHideEm(oEm2H,oEm2S){oEm2H.style.display="none";oEm2S.style.display="";}
function opnWin(url,iW,iH,sWN){if(!sWN)sWN=new String(Math.round(Math.random()*1000000));if(!iW)iW=700;if(!iH)iH=500;var sF="toolbar=0,location=0,directories=0,status=1,menubar=0,scrollbars=1,resizable=1,width="+iW+",height="+iH;return window.open(url,sWN,sF);}
function opnHlp(url){opnWin(url,a_iHW,a_iHH,a_sHWN).focus();}
function rcptType(iT){switch(iT){case 1:return "AD.RecipientType.User";case 2:return "ADDistList";case 3:return "IPM.Contact";case 4:return "IPM.DistList";}}
function getDtURL(iY,iM,iD){return "&yr="+iY+"&mn="+iM+"&dy="+iD;}
var g_dtLstKA=new Date();var g_iKA=g_dtLstKA.getTime();function sndKA(){var now=new Date();if(now.getTime()-g_dtLstKA.getTime()>a_iKAI){getEm("imgKA").src="keepalive.owa?m="+g_iKA;g_iKA=g_iKA+1;g_dtLstKA=new Date();return 1;}return 0;}
function getChkedCnt(oChk){if(!oChk)return 0;if(isNaN(oChk.length))return oChk.checked?1:0;var count=0;for(var i=0;i<oChk.length;i++){if(oChk[i].checked)count++;}return count;}
function isAllChked(oChk){if(!oChk)return false;if(isNaN(oChk.length))return oChk.checked;for(var i=0;i<oChk.length;i++){if(!oChk[i].checked)return false;}return true;}
function isAnyChked(oChk){if(!oChk)return false;if(isNaN(oChk.length))return oChk.checked;for(var i=0;i<oChk.length;i++){if(oChk[i].checked)return true;}return false;}
function chkAll(oChk,fChk){if(!oChk)return ;if(isNaN(oChk.length))oChk.checked=fChk;else for(var i=0;i<oChk.length;i++)oChk[i].checked=fChk;}
function stSelTdClss(oRow,fChk){var sBfr=fChk?"sc":"sr";var sAft=fChk?"sr":"sc";for(var i=0;i<oRow.childNodes.length;i++){if(oRow.childNodes[i].className.indexOf(sBfr)>=0){oRow.childNodes[i].className=oRow.childNodes[i].className.replace(sBfr,sAft);break;}}}
function lvChkAll(oChk,fChk){var oRow=null;if(oChk){if(isNaN(oChk.length)){oChk.checked=fChk;oRow=oChk.parentNode.parentNode;oRow.className=fChk?"sl":"";stSelTdClss(oRow,fChk);}else {var rowClass=fChk?"sl":"";var cellClass=fChk?"sr":"sc";var oRow=oChk[0].parentNode.parentNode;var iSlIdx=-1;for(var i=0;i<oRow.childNodes.length;i++){if(oRow.childNodes[i].className.indexOf("sr")>=0||oRow.childNodes[i].className.indexOf("sc")>=0){iSlIdx=i;break;}}for(var i=0;i<oChk.length;i++){oChk[i].checked=fChk;oRow=oChk[i].parentNode.parentNode;oRow.className=rowClass;if(iSlIdx>=0)oRow.childNodes[iSlIdx].className=(i==0)?(cellClass+" frst"):cellClass;}}}}
function getItmURL(sTp,sId,sSt){var s="?ae=";if(sTp.indexOf("IPM.Appointment")==0)s+="PreFormAction";else s+="Item";if(sSt&&sSt!="")s+="&s="+urlEnc(sSt);return (s+"&t="+urlEnc(sTp)+"&a=Open&id="+urlEnc(sId));}
function opnItm(sTp,sId,sSt){nv(getItmURL(sTp,sId,sSt));}
function opnEmbItem(sPr,sId,sAttId,fEmb,rgsIds){var s=sPr;s+="&id="+(fEmb?rgsIds[0]:sId);if(fEmb){for(var i=1;i<rgsIds.length;i++)s+="&attid"+(i-1)+"="+rgsIds[i];}var iA=fEmb?rgsIds.length:1;s+="&attid"+(iA-1)+"="+sAttId;s+="&attcnt="+iA;nv(s);}
function gtEmItmId(rgsIds){var s=rgsIds[0];for(var i=1;i<rgsIds.length;i++)s+="&attid"+(i-1)+"="+rgsIds[i];s+="&attcnt="+(rgsIds.length-1);return s;}
function onClkLvChk(oCb,oChk,oChkHd){var oRow=oCb.parentNode.parentNode;oRow.className=oCb.checked?"sl":"";stSelTdClss(oRow,oCb.checked);oChkHd.checked=oCb.checked?isAllChked(oChk):false;}
function getSelectedRcpt(oSel){var sRet="";var fNeedDelim=false;for(var i=0;i<oSel.length;i++){if(oSel[i].selected){if(fNeedDelim)sRet+="&";sRet+=oSel[i].value;fNeedDelim=true;}}return sRet;}
function genOwPrm(sId,sCk,sTp,sSt){var sP="";if(sId!="")sP+="&oId="+urlEnc(sId);if(sCk!="")sP+="&oCk="+urlEnc(sCk);if(sTp!="")sP+="&oT="+urlEnc(sTp);if(sSt!="")sP+="&oS="+urlEnc(sSt);return sP;}
function onFOSch(o){if(o.className!=""){o.value="";o.className="";}}
function hasSchStr(){var oSch=getEm("txtSch");return oSch.className==""&&oSch.value!="";}
function shSchBtn(){if(!hasSchStr())return ;var oSch=getEm("schBtn");if(oSch.style.display=="none")showHideEm(getEm("clrBtn"),oSch);}
function onChgSch(){shSchBtn();}
function gtUTF8EncSch(){var oSch=getEm("txtSch");return toUTF8(oSch.value);}
function isPpPk(){return (typeof (a_iCtx)!="undefined"&&isEdtMd(a_iCtx));}
function doDtyCk(){return (typeof (canLvWOSv)=="undefined"||canLvWOSv());}
function doSch(){if(typeof (doSch2)!="undefined")return doSch2();if(!hasSchStr()||(!isPpPk()&&!doDtyCk()))return ;var s=getEm("selSch").value;nv(s+"&sch="+gtUTF8EncSch());}
function onClkClrLnk(){return nv(getEm("clrBtn").href);}
function onClkOp(){return doDtyCk();}
function onClkLgf(sCnr){if(doDtyCk())logOff(sCnr);return false;}
function onClkAB(){if(typeof (onClkAB2)!="undefined")return onClkAB2();return (isPpPk()||doDtyCk());}
function onEOSch(e){if(e.keyCode==13){doSch();return false;}}
function onClkSch(){doSch();return false;}
var g_sLstSch;function ldLstSch(){if(hasSchStr())g_sLstSch=getEm("txtSch").value;}
function onKUSch(){if(getEm("txtSch").value!=g_sLstSch)shSchBtn();}
function onClkPN(iM){if(doDtyCk())nvMdl(iM);return false;}
var IMP_LW=0;var IMP_NM=1;var IMP_HI=2;function setImpPrmpt(){stFrm();var iImp=g_oFrm.hidmsgimp.value;setImpPrmptOneBtn("imphigh",(iImp==IMP_HI)?L_ImpHighSlt:L_ImpHigh);setImpPrmptOneBtn("implow",(iImp==IMP_LW)?L_ImpLowSlt:L_ImpLow);}
function setImpPrmptOneBtn(sId,s){var o=getEm(sId);var oLnk=o.firstChild;oLnk.title=s;var oImg=oLnk.firstChild;oImg.alt=s;}
function hasANR(){return (getEm("tblANR")!=null);}
