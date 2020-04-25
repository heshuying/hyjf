<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	
	<body>
		<jsp:include page="/header.jsp"></jsp:include>
		<jsp:include page="/subMenu-aboutUS.jsp"></jsp:include>
		<article class="main-content">
	        <div class="container">
	            <section class="content">
	                <div class="main-title">
	                    联系我们
	                </div>
	                <div class="contact-content">
				        <div class="contact-cell-top contact-cell">
				    	     <div class="contact-cell-left">
				    	   	   <p class="name"><i class="iconfont icon-shape3"></i>客户服务(9:00-18:00)</p>
				    	   	   <p class="callme">电话：400-900-7878</p>
				    	   	   <p class="address">邮箱：VIP@hyjf.com</p>
				    	    </div> 
				    	    <div class="cooperation contact-cell-left">
				    	   	   <p class="name"><i class="iconfont icon-shape3"></i>商务合作&媒体采访</p>
				    	   	   <p>邮箱：yaozhen@hyjf.com</p>
				    	    </div>
				    	    <div class="clearboth"></div>
						</div>
						<div class="contact-cell">
				    	    <div class="contact-cell-left">
					    	    <p class="name"><i class="iconfont icon-shape3"></i>北京</p>
				    	   	    <p class="callme">电话：010-57569768</p>
				    	   	    <p class="address"><span>地址：</span><span  class="local">北京市中关村西区丹棱街6号丹棱soho1105室</span></p>
				    	    </div> 
				    	    <div class="contact-cell-right">
				    	    	<div style="width:400px;height:145px;border:#ccc solid 1px;font-size:12px" id="map_beijing"></div>
				    	    </div>
				    	    <div class="clearboth"></div>
						</div>
						<div class="contact-cell">
				    	    <div class="contact-cell-left">
					    	    <p class="name"><i class="iconfont icon-shape3"></i>上海</p>
				    	   	    <p class="callme">电话：021-23570077</p>
				    	   	    <p class="address"><span>地址：</span><span class="local">上海市普陀区丹巴路99号C9座</span></p>
				    	    </div> 
				    	    <div class="contact-cell-right">
				    	    	<div style="width:400px;height:145px;border:#ccc solid 1px;font-size:12px" id="map_shanghai"></div>
				    	    </div>
				    	    <div class="clearboth"></div>
						</div>
						<div class="contact-cell">
				    	    <div class="contact-cell-left">
					    	    <p class="name"><i class="iconfont icon-shape3"></i>青岛</p>
				    	   	    <p class="callme">电话：0532-68895739</p>
				    	   	    <p class="address"><span>地址：</span><span  class="local">青岛市市南区香港中路7号亚麦国际中心19楼 </span></p>
				    	    </div> 
				    	    <div class="contact-cell-right">
				    	    	<div style="width:400px;height:145px;border:#ccc solid 1px;font-size:12px" id="map_qingdao"></div>
				    	    </div>
				    	    <div class="clearboth"></div>
						</div>
				    </div>
	            </section>
	        </div>
	    </article>
		<jsp:include page="/footer.jsp"></jsp:include>
	</body>
	<script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=ekFyUKQ9evXiTXWAgh6vqewS0NZRLW2l"></script>
	<script type="text/javascript">
    //创建和初始化地图函数：
    function initMapQD(){
    	createMapQD();//创建地图
      setMapEventQD();//设置地图事件
      addMapOverlayQD();//向地图添加覆盖物
    }
    function createMapQD(){ 
      map = new BMap.Map("map_qingdao"); 
      map.centerAndZoom(new BMap.Point(120.383599,36.070442),15);
    }
    function setMapEventQD(){
      map.enableScrollWheelZoom();
      map.enableKeyboard();
      map.enableDragging();
      map.enableDoubleClickZoom()
    }
    function addClickHandlerQD(target,window){
      target.addEventListener("click",function(){
        target.openInfoWindow(window);
      });
    }
    function addMapOverlayQD(){
      var markers = [
        {content:"市南区香港中路7号亚麦国际中心19楼",title:"青岛",imageOffset: {width:-46,height:-21},position:{lat:36.071609,lng:120.38288}}
      ];
      for(var index = 0; index < markers.length; index++ ){
        var point = new BMap.Point(markers[index].position.lng,markers[index].position.lat);
        var marker = new BMap.Marker(point,{icon:new BMap.Icon("http://api.map.baidu.com/lbsapi/createmap/images/icon.png",new BMap.Size(20,25),{
          imageOffset: new BMap.Size(markers[index].imageOffset.width,markers[index].imageOffset.height)
        })});
        var label = new BMap.Label(markers[index].title,{offset: new BMap.Size(25,5)});
        var opts = {
          width: 200,
          title: markers[index].title,
          enableMessage: false
        };
        var infoWindow = new BMap.InfoWindow(markers[index].content,opts);
        marker.setLabel(label);
        addClickHandlerQD(marker,infoWindow);
        map.addOverlay(marker);
      };
    }
      initMapQD();
      
  </script>
    <script type="text/javascript">
    //创建和初始化地图函数：
    function initMap(){
      createMapBJ();//创建地图
      setMapEventBJ();//设置地图事件
      addMapOverlayBJ();//向地图添加覆盖物
    }
    function createMapBJ(){ 
      map = new BMap.Map("map_beijing"); 
      map.centerAndZoom(new BMap.Point(116.319508,39.983914),15);
    }
    function setMapEventBJ(){
      map.enableScrollWheelZoom();
      map.enableKeyboard();
      map.enableDragging();
      map.enableDoubleClickZoom()
    }
    function addClickHandlerBJ(target,window){
      target.addEventListener("click",function(){
        target.openInfoWindow(window);
      });
    }
    function addMapOverlayBJ(){
      var markers = [
        {content:"中关村西区丹棱街6号丹棱soho1105室",title:"北京",imageOffset: {width:-46,height:-21},position:{lat:39.984509,lng:116.320065}}
      ];
      for(var index = 0; index < markers.length; index++ ){
        var point = new BMap.Point(markers[index].position.lng,markers[index].position.lat);
        var marker = new BMap.Marker(point,{icon:new BMap.Icon("http://api.map.baidu.com/lbsapi/createmap/images/icon.png",new BMap.Size(20,25),{
          imageOffset: new BMap.Size(markers[index].imageOffset.width,markers[index].imageOffset.height)
        })});
        var label = new BMap.Label(markers[index].title,{offset: new BMap.Size(25,5)});
        var opts = {
          width: 200,
          title: markers[index].title,
          enableMessage: false
        };
        var infoWindow = new BMap.InfoWindow(markers[index].content,opts);
        marker.setLabel(label);
        addClickHandlerBJ(marker,infoWindow);
        map.addOverlay(marker);
      };
    }
      initMap();
  </script>
  
    <script type="text/javascript">
    //创建和初始化地图函数：
    function initMapSH(){
      createMapSH();//创建地图
      setMapEventSH();//设置地图事件
      addMapOverlaySH();//向地图添加覆盖物
    }
    function createMapSH(){ 
      map = new BMap.Map("map_shanghai"); 
      map.centerAndZoom(new BMap.Point(121.386361,31.227279),15);
    }
    function setMapEventSH(){
      map.enableScrollWheelZoom();
      map.enableKeyboard();
      map.enableDragging();
      map.enableDoubleClickZoom()
    }
    function addClickHandlerSH(target,window){
      target.addEventListener("click",function(){
        target.openInfoWindow(window);
      });
    }
    function addMapOverlaySH(){
      var markers = [
        {content:"普陀区丹巴路99号C9座",title:"上海",imageOffset: {width:-46,height:-21},position:{lat:31.227279,lng:121.386361}}
      ];
      for(var index = 0; index < markers.length; index++ ){
        var point = new BMap.Point(markers[index].position.lng,markers[index].position.lat);
        var marker = new BMap.Marker(point,{icon:new BMap.Icon("http://api.map.baidu.com/lbsapi/createmap/images/icon.png",new BMap.Size(20,25),{
          imageOffset: new BMap.Size(markers[index].imageOffset.width,markers[index].imageOffset.height)
        })});
        var label = new BMap.Label(markers[index].title,{offset: new BMap.Size(25,5)});
        var opts = {
          width: 200,
          title: markers[index].title,
          enableMessage: false
        };
        var infoWindow = new BMap.InfoWindow(markers[index].content,opts);
        marker.setLabel(label);
        addClickHandlerSH(marker,infoWindow);
        map.addOverlay(marker);
      };
    }
      initMapSH();
  </script>
  <!-- 设置定位  -->
  <script>setActById("aboutContactus");</script>
  <!-- 导航栏定位  -->
  <script>setActById("indexMessage");</script>
</html>