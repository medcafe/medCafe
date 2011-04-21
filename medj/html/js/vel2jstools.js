function StringCat()
{
    var sp;
    var ep;
    var l=0;

    this.p=function(what)
    {
        if(typeof(sp)=='undefined')
        {
            ep=new Array();
            sp=ep;
        }
        else
        {
            var oep=ep;
            ep=new Array();
            oep[1]=ep;
        }
        if (typeof what == "object")
        {
        	  ep[0] = printJsonObject(what, 0);
        }
        else
        {
        	 ep[0]=what;
 		  }
        ++l;
    }

    this.toString=function()
    {
        if(l==0)return ;

        while(l>1)
        {
            var ptr=sp;
            var nsp=new Array();
            var nep=nsp;
            var nl=0;

            while(typeof(ptr)!='undefined')
            {
                if(typeof(nep[0])=='undefined')
                {
                    nep[0]=ptr[0];
                    ++nl;
                }
                else
                {
                    if(typeof(ptr[0])!='undefined')nep[0]+=ptr[0];
                    nep[1]=new Array();
                    nep=nep[1];
                }
                ptr=ptr[1];
            }
            sp=nsp;
            ep=nep;
            l=nl;
        }
        return sp[0];
    }
}
function printJsonObject(jsonObject, nestingLevel)
{
	var i;
	var returnString = "";

	for ( i in jsonObject)
	{
			var number = parseInt(i, 10);
			if (isNaN(number))
				number = i;
			else
				number++;
	//	alert(i + ":  " +JSON.stringify(jsonObject[i]));
		returnString = returnString + "<div style=\"text-indent: " + nestingLevel + "em\">";
		if (typeof jsonObject[i] == "object")
		{		
			returnString = returnString + number + ": </div>" + printJsonObject(jsonObject[i], nestingLevel+1);
		}
		else
		{
			returnString = returnString + number + ": " + jsonObject[i] + " </div>";
		}
	}
	return returnString;
}
