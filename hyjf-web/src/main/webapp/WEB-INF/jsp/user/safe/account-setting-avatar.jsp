<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content">
        <div class="container">
            
            <!-- start 内容区域 --> 
            <div class="acc-set">
            	<h5>设置头像</h5>
            	<div class="acc-set-avatar">
            		<div class="acc-set-avatar-1">
            			<div class="acc-set-avatar-1-img inline-block">
            				<c:choose>
								 <c:when test="${iconUrl == null || iconUrl == ''}">
									<img src="${cdn}/img/default.png">
								 </c:when>
								 <c:otherwise>
									<img src="${iconUrl}"/>
								 </c:otherwise>
							</c:choose>
            				<p>当前头像</p>
            			</div>
            			<div class="acc-set-avatar-1-upload inline-block">
            				<p class="upload-p-1">请选择本地图片文件，格式仅限JPG、GIF、PNG，大小不超过5M。</p>
            				<span class="upload-error block hidden error"><i class="icon iconfont icon-jinzhi"></i>图片格式不正确！</span><br />
            				<label class="avatar-select-pic acc-set-btn" for="inputImage1">
            					<img src="${cdn}/dist/images/acc-set/acc-set-avatar-default@2x.png" width="18px"/>
            					选择图片文件
            					<input type="file" id="inputImage1" class="inputImage" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff"/>
            				</label>
            			</div>
            		</div>
            		<div class="acc-set-avatar-2 hide">
            			<div class="acc-set-avatar-1-img inline-block">
            				<div class="pic-container">
            					<img src="" id="image"/>
            				</div>
            				<p>可以用鼠标拖动调整位置和大小</p>
            			</div>
            			<div class="acc-set-avatar-1-upload inline-block">
            				<div class="img-preview preview-lg preview-avatar">
            					<img src=""/>
            				</div>
            				<p class="preview-text">预览头像</p>
            				<div class="avatar-select-pic" data-method="getCroppedCanvas">
            					保存
            				</div>
            				<label class="re-select" for="inputImage2">
            					重新选择图片
            					<input type="file" id="inputImage2" class="inputImage" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff"/>
            				</label>
            				<span class="upload-error block hidden error"><i class="icon iconfont icon-jinzhi"></i>图片格式不正确！</span><br />
            			</div>
            		</div>
            	</div>
            </div>
                      
            <!-- end 内容区域 -->   
                      
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>setActById("accountSet");</script>
	<script src="${cdn }/dist/js/lib/cropper.min.js"></script>
	<script src="${cdn }/dist/js/acc-set/acc-set.js?version=${version}"></script>
</body>
</html>