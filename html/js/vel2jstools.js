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
        ep[0]=what;
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