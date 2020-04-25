var strFullPath = window.document.location.href;
var strPath = window.document.location.pathname;
var pos = strFullPath.indexOf(strPath);
var prePath = prePath = window.document.location.protocol+"//"+window.document.location.host;
var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
var webPath = prePath;
var webPath = prePath + postPath;


