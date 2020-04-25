if ($(".section-banner .swiper-container .swiper-wrapper .swiper-slide").length > 1) {
    var options = {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        effect: 'fade',
        autoplay: 5000,
        loop: true
    }
    if (!$.browser.msie) {
        $.extend(options, {
            paginationBulletRender: function(swiper, index, className) {
                return '<span class="' + className + '"><svg viewBox="0 0 100 100"><path d="M 50,50 m 0,-45 a 45,45 0 1 1 0,90 a 45,45 0 1 1 0,-90" stroke="#fff" stroke-width="12" fill-opacity="0" class="path" style="stroke-dasharray: 282.783, 282.783; stroke-dashoffset: 0;"></path></svg></span>';
            },
            onAutoplayStop: function(swiper) {
                $("body").append("<style>.swiper-pagination-bullet-active{background:#fff!important;}<//style>");
            }
        })
    }else{
        $("body").append("<style>.swiper-pagination-bullet-active{background:#fff!important;}<//style>");
    }
    setTimeout(function(){
    	var bannerswiper = new Swiper('.section-banner .swiper-container', options);
    },300)
}
$(".timeout").each(function() {
    var ele = $(this),
        s = parseInt(ele.data("end")) - parseInt(ele.data("start"));
    utils.timer(ele, s,function(){
        ele.parent().html('<div class="btn sm">出借</div>')
    });
});
var coo = EncodeUtf8(hyjfUsername);
var hyjfeyeIndex=document.cookie.indexOf('hyjfeye'+coo);
var loginFlag = $("#loginFlag").val();
if(hyjfeyeIndex != -1){
	var hyjfeye=utils.getCookie('hyjfeye'+coo);
}else if(loginFlag == '1'){
	utils.setCookie('hyjfeye'+coo,'true',365);
	var hyjfeye=true;
}
if(hyjfeye=="false"){
	$('.data-show .val').toggleClass('hide')
	if($('.desc .i-eye').hasClass('close')){
		$('.desc .i-eye').removeClass('close');
	}else{
		$('.desc .i-eye').addClass('close');
	}
}
$('.desc .i-eye').click(function(){
	$('.data-show .val').toggleClass('hide')
	if($(this).hasClass('close')){
		$(this).removeClass('close');
		utils.setCookie('hyjfeye'+coo,'true',365);
		
	}else{
		$(this).addClass('close');
		utils.setCookie('hyjfeye'+coo,'false',365);
	}
})

//字符转换为UTF-8编码
function EncodeUtf8(s1)
{
      var s = escape(s1);
      var sa = s.split("%");
      var retV ="";
      if(sa[0] != "")
      {
         retV = sa[0];
      }
      for(var i = 1; i < sa.length; i ++)
      {
           if(sa[i].substring(0,1) == "u")
           {
               retV += Hex2Utf8(Str2Hex(sa[i].substring(1,5)));

           }
           else retV += "%" + sa[i];
      }

      return retV;
}

function Hex2Utf8(s)
{
     var retS = "";
     var tempS = "";
     var ss = "";
     if(s.length == 16)
     {
         tempS = "1110" + s.substring(0, 4);
         tempS += "10" + s.substring(4, 10);
         tempS += "10" + s.substring(10,16);
         var sss = "0123456789ABCDEF";
         for(var i = 0; i < 3; i ++)
         {
            retS += "%";
            ss = tempS.substring(i * 8, (eval(i)+1)*8);



            retS += sss.charAt(Dig2Dec(ss.substring(0,4)));
            retS += sss.charAt(Dig2Dec(ss.substring(4,8)));
         }
         return retS;
     }
     return "";
}

function Str2Hex(s)
{
      var c = "";
      var n;
      var ss = "0123456789ABCDEF";
      var digS = "";
      for(var i = 0; i < s.length; i ++)
      {
         c = s.charAt(i);
         n = ss.indexOf(c);
         digS += Dec2Dig(eval(n));

      }
      //return value;
      return digS;
}

function Dec2Dig(n1)
{
      var s = "";
      var n2 = 0;
      for(var i = 0; i < 4; i++)
      {
         n2 = Math.pow(2,3 - i);
         if(n1 >= n2)
         {
            s += '1';
            n1 = n1 - n2;
          }
         else
          s += '0';

      }
      return s;

}

function Dig2Dec(s)
{
      var retV = 0;
      if(s.length == 4)
      {
          for(var i = 0; i < 4; i ++)
          {
              retV += eval(s.charAt(i)) * Math.pow(2, 3 - i);
          }
          return retV;
      }
      return -1;
}
