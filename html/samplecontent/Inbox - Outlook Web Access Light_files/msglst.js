var g_oFrm=document.forms["frm"];window.onload=function (){var rgIds=new Array("Importance","MailIcon","HasAttachment");for(var i=0;i<rgIds.length;i++){var o=getEm("lnkCol"+rgIds[i]).firstChild;o.title=o.alt;}shOofDlg;if(a_fIsSch)ldLstSch();}
function stFrm(){g_oFrm=document.forms["frm"];}
function onClkPNTgl(iSt){stFrm();g_oFrm.action=gtFrmActn();g_oFrm.hidpnst.value=iSt;sbtFrm(g_oFrm);return false;}
function onClkBrwsFld(){stFrm();g_oFrm.action=gtFrmActn();g_oFrm.hidactbrfld.value=1;sbtFrm(g_oFrm);return false;}
function onClkTb(sCmd){switch(sCmd){case "checkmessages":chkMsgs();break;case "delete":msgLstDel(sCmd);break;case "emptyfolder":emptyFldr(sCmd);break;case "junk":submtCmd(sCmd,L_PrJkNoItm);break;case "markread":submtCmd(sCmd,L_PrRdNoItm);break;case "markunread":submtCmd(sCmd,L_PrUnrdNoItm);break;case "move":mvSelMsg();break;case "newmsg":stFrm();nv("?ae=Item&t=IPM.Note&a=New");break;case "notjunk":submtCmd(sCmd,L_PrNtJkNoItm);break;}return false;}
function onPwdNtf(sCmd){switch(sCmd){case "yes":nv("?ae=Options&t=ChangePassword");break;case "no":chkMsgs();break;}return false;}
function gtPrfx(){var sP="?ae="+a_sAe;if(a_sT!="")sP=sP+"&t="+a_sT;sP=sP+"&id="+a_sFldId+"&slUsng="+a_iSlUsng;if(a_fIsSch)sP=sP+"&sch="+urlEnc(g_sLstSch)+"&scp="+a_iScp;return sP;}
function gtFrmActn(){if(a_sPg!="")return gtPrfx()+"&pg="+a_sPg;return gtPrfx();}
function onClkPg(iPg){stFrm();if(a_sFldId!="")nv(gtPrfx()+"&pg="+iPg);return false;}
function onClkSrt(iCId,iSO){stFrm();g_oFrm.action=gtFrmActn();g_oFrm.hidcmdpst.value="s";g_oFrm.hidcid.value=iCId;g_oFrm.hidso.value=iSO;sbtFrm(g_oFrm);return false;}
function onClkSlAll(){stFrm();lvChkAll(g_oFrm.chkmsg,g_oFrm.chkhd.checked);}
function onClkChkBx(oCb){stFrm();onClkLvChk(oCb,g_oFrm.chkmsg,g_oFrm.chkhd);}
function chkMsgs(){stFrm();nv(gtFrmActn());}
function msgLstDel(sCmd){stFrm();g_oFrm.action=gtFrmActn();var fSl=isAnyChked(g_oFrm.chkmsg);if(!fSl){alert(L_PrDelNoItm);}else {if(a_iIsDlFld==1)if(!confirm(L_PrDelPrp))return ;g_oFrm.hidcmdpst.value=sCmd;sbtFrm(g_oFrm);}}
function mvSelMsg(){stFrm();if(!isAnyChked(g_oFrm.chkmsg)){alert(L_PrMvNoItm);}else {if(""==a_sT)a_sT="IPF.Note";g_oFrm.action="?ae=Dialog&t="+urlEnc(a_sT)+"&a=Move&fid="+a_sFldId;sbtFrm(g_oFrm);}}
function emptyFldr(sCmd){if(!confirm(a_sEmptyMsg))return ;stFrm();g_oFrm.action=gtFrmActn();g_oFrm.hidcmdpst.value=sCmd;sbtFrm(g_oFrm);}
function submtCmd(sCmd,sMsg){stFrm();if(sMsg!=null&&!isAnyChked(g_oFrm.chkmsg)){alert(sMsg);}else {g_oFrm.action=gtFrmActn();g_oFrm.hidcmdpst.value=sCmd;sbtFrm(g_oFrm);}}
function onClkRdMsg(oLnk,sT,iI,fC){stFrm();var msgId=0;if(g_oFrm.chkmsg){if(isNaN(g_oFrm.chkmsg.length))msgId=g_oFrm.chkmsg.value;else msgId=g_oFrm.chkmsg[iI].value;msgId=urlEnc(msgId);if((msgId!=0)&&(sT!="")){var sHref="";if(a_fIsJnkFld)sHref="?ae=Item&id="+msgId;else if(sT.indexOf("IPM.Appointment")==0)sHref="?ae=PreFormAction&a=Open&t="+sT+"&id="+msgId;else if(fC==1)sHref="?ae=Item&t="+sT+"&a=Open&s=Draft&id="+msgId;else sHref="?ae=Item&t="+sT+"&id="+msgId;if(a_fWP)oLnk.target="_blank";oLnk.href=sHref;}}}
function onClkAddEml(){submtCmd("addjnkeml",null);return false;}
function onClkHdOof(){submtCmd("hideoof",null);return false;}
function shOofDlg(){if(!a_fWP&&a_fShOofDlg&&confirm(L_OofOn))submtCmd("turnoffoof",null);}
function onClkFM(){nv("?ae=Dialog&t=FolderManagement&m="+a_iNM);return false;}
