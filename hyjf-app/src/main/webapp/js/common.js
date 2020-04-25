// --------------------------------------------------------------------------------------------------------------------------------
var jumpcommond = document.getElementById("jumpcommend")==null? "":document.getElementById("jumpcommend").value;
var versionStr =  document.getElementById("version")==null? "":document.getElementById("version").value;
//获取URL参数
function GetQueryString(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
