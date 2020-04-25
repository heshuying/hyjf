<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="着落页配置" />
	
	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">着落页配置</h1>
		<span class="mainDescription">着落页配置的说明。</span>
	</tiles:putAttribute>
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
	<div class="container-fluid container-fullw bg-white">
		<div class="tabbable">
			<ul id="mainTabs" class="nav nav-tabs nav-justified">
				<li class="s-ellipsis active">
					<a href="#tab_jbxx_1" data-toggle="tab"><i class="fa fa-edit"></i> 页面参数</a>
				</li>
				<c:if test="${1 ne whereaboutsPageForm.style}">
					<li class="imageConfig">
						<a href="#tab_xmms_2" data-toggle="tab"><i class="fa fa-cubes"></i> 图片配置</a>
					</li>
				</c:if>
			</ul>
			<form id="downloadForm" action="" method="post" target="_blank">
			</form>
			<form id="mainForm" action="insertAction" method="post"  role="form">
				<!-- 图片 -->
				<input type="hidden" name="imageJson1" id="imageJson1" value="" />
				<!-- 图片 -->
				<input type="hidden" name="imageJson2" id="imageJson2" value="" />
				<!-- 图片 -->
				<input type="hidden" name="imageJson3" id="imageJson3" value="" />
				<!-- 图片 -->
				<input type="hidden" name="id" id="id" value="${whereaboutsPageForm.id}" />
			<div class="tab-content">
				<!-- Start:页面参数 -->
				<div class="tab-pane fade in active" id="tab_jbxx_1">
					<hr>
					<div class="row">
						<!-- 表单左侧 -->
						<div class="col-xs-12 col-md-5">
							<div class="user-left">
								
								<div class="form-group">
									<label class="control-label">
										页面title <span class="symbol required"></span>
									</label>
									<input type="text" name="title" id="title" class="form-control input-sm" 
									value='<c:out value="${whereaboutsPageForm.title}"></c:out>'
										maxlength="20" datatype="*1-20" nullmsg="未填写页面title"  errormsg="页面title长度不能超过20位字符"/>
									<hyjf:validmessage key="title"></hyjf:validmessage>
								</div>
								
								<div class="form-group">
									<label class="control-label">
										utm_id 
									</label>
									<input type="text" id="utmId" name="utmId" class="form-control input-sm" 
									value='<c:out value="${whereaboutsPageForm.utmId}"></c:out>' datatype="n" ignore="ignore" ajaxurl="checkUtmId"  />
									<hyjf:validmessage  key="utmId" label="项目编号"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										选择样式 <span class="symbol required"></span>
									</label>
										<select name=style id="style" class="form-control underline form-select2">
<!-- 											<option value=""></option> -->
											<c:forEach items="${pageStyles }" var="pageStyle" begin="0" step="1" varStatus="st">
												<option value="${pageStyle.nameCd }"
													<c:if test="${pageStyle.nameCd eq whereaboutsPageForm.style}">selected="selected"</c:if>>
													<c:out value="${pageStyle.name }"></c:out></option>
											</c:forEach>
										</select>
									<hyjf:validmessage key="title"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										推荐人用户名
									</label>
									<input type="text" id="referrerName" name="referrerName" class="form-control input-sm" 
									value='<c:out value="${whereaboutsPageForm.referrerName}"></c:out>' datatype="*" ignore="ignore" ajaxurl="checkReferrer"  />
									<hyjf:validmessage key="referrerName" label="项目编号"></hyjf:validmessage>
								</div>	
								<div class="form-group" id="tongyong" <c:if test="${1 eq whereaboutsPageForm.style}">hidden="hidden" ignore="ignore"</c:if>>
								<div class="form-group">
									<label class="control-label">
										按钮名称 <span class="symbol required"></span>
									</label>
									<input type="text" name="topButton" id="topButton" class="form-control input-sm" value='<c:out 
									value="${whereaboutsPageForm.topButton}"></c:out>' 
										  maxlength="50" datatype="*1-50" <c:if test="${1 eq whereaboutsPageForm.style}">ignore="ignore"</c:if>nullmsg="未填写按钮名称" errormsg="按钮名称长度不能超过50位字符"/>
									<hyjf:validmessage key="topButton"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										注册成功跳转地址 <span class="symbol required"></span>
									</label>
									<input type="text" name="jumpPath" id="jumpPath" class="form-control input-sm" value='<c:out 
									value="${whereaboutsPageForm.jumpPath}"></c:out>'
										maxlength="100" datatype="*1-100" <c:if test="${1 eq whereaboutsPageForm.style}">ignore="ignore"</c:if> nullmsg="未填写注册成功跳转地址"  errormsg="注册成功跳转地址长度不能超过20位字符"/>
									<hyjf:validmessage key="jumpPath"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										下载按钮 <span class="symbol required"></span>
									</label>
									
										<c:if test="${ ( whereaboutsPageForm.id == null) }">
											<input type="radio" value="0" name="bottomButtonStatus" checked>
											<label >启用</label>
										</c:if>
										<c:if test="${ ( whereaboutsPageForm.id != null) }">
											<input type="radio" value="0" name="bottomButtonStatus"  
											<c:if test="${whereaboutsPageForm.bottomButtonStatus eq '0'}">checked</c:if>>
											<label >启用</label>
										</c:if>
										
										<input type="radio" value="1" name="bottomButtonStatus"  
										<c:if test="${whereaboutsPageForm.bottomButtonStatus eq '1'}">checked</c:if>>
										<label >停用</label>
									<hyjf:validmessage key="bottomButtonStatus"></hyjf:validmessage>
								</div>
								<div class="form-group changeInput">
									<label class="control-label">
										下载按钮名称 <span class="symbol required"></span>
									</label>
									<input type="text" name="bottomButton" id="bottomButton" class="form-control input-sm changeInput" 
									value='<c:out value="${whereaboutsPageForm.bottomButton}"></c:out>'
										maxlength="20" datatype="*1-20" <c:if test="${1 eq whereaboutsPageForm.style}">ignore="ignore"</c:if> nullmsg="未填写下载按钮名称"  errormsg="下载按钮名称长度不能超过20位字符"/>
									<hyjf:validmessage key="bottomButton"></hyjf:validmessage>
								</div>
								<div class="form-group changeInput" id = "address">
									<label class="control-label">
										下载地址 <span class="symbol required"></span>
									</label>
									<c:if test="${whereaboutsPageForm.downloadPath != null}">
										<input type="text" name="downloadPath" id="downloadPath" class="form-control input-sm changeInput" 
										value='<c:out value="${whereaboutsPageForm.downloadPath}"></c:out>' 
											maxlength="100" datatype="*1-100" nullmsg="下载地址目不能为空"  errormsg="下载地址长度不能超过20位字符"/>
									</c:if>
									<c:if test="${whereaboutsPageForm.downloadPath == null}">
										<input type="text" name="downloadPath" id="downloadPath" class="form-control input-sm changeInput" 
										value='http://a.app.qq.com/o/simple.jsp?pkgname=com.huiyingdai.apptest' 
											maxlength="100" datatype="*1-100" nullmsg="下载地址目不能为空"  errormsg="下载地址长度不能超过20位字符"/>
									</c:if>
									<hyjf:validmessage key="downloadPath"></hyjf:validmessage>
								</div>
								</div>
								<div class="form-group">
									<label class="control-label">描述 <span class="symbol required"></span></label><textarea rows="4" placeholder="备注" id="describe" name="describe" class="form-control" 
									maxlength="500" datatype="*" nullmsg="请填写说明信息" errormsg="说明信息最长为500字符" style="resize:none;" ><c:out value="${whereaboutsPageForm.describe}"></c:out></textarea>
									<hyjf:validmessage key="describe"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										状态 <span class="symbol required"></span>
									</label>
									<c:if test="${empty whereaboutsPageForm.id}">
										<input type="radio" value="1" name="statusOn" checked >
										<label >启用</label>
									</c:if>
									<c:if test="${!empty whereaboutsPageForm.id}">
										<input type="radio" value="1" name="statusOn"  
											<c:if test="${whereaboutsPageForm.statusOn eq '1'}">checked</c:if>>
										<label >启用</label>
									</c:if>
									
									<input type="radio" value="0" name="statusOn"  
										<c:if test="${whereaboutsPageForm.statusOn eq '0'}">checked</c:if>>
									<label >停用</label>
								<hyjf:validmessage key="statusOn"></hyjf:validmessage>
							   </div>
								
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div>
								<span class="symbol required"></span>必须填写的项目
								<hr>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-5">
							<p>
								点击【提交保存】按钮，保存当前的填写的资料。
							</p>
						</div>
						<c:choose>
						   <c:when test="${1 eq whereaboutsPageForm.style}"> 
						         <div class="col-md-7">
									<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
										<i class="fa fa-check"></i>  提交保存</i>
									</a>
								</div>     
						   </c:when>
						   <c:otherwise>
							  <div class="col-md-7">
								<a id="nextStep" href="#tab_xmms_2" class="btn btn-primary btn-o btn-wide pull-right fn-Next">
									下一步 <i class="fa fa-arrow-circle-right"></i>
								</a>
								<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
									<i class="fa fa-check"></i>  提交保存</i>
								</a>
							</div>
						   </c:otherwise>
						</c:choose>
						
					</div>
				</div>
				<!-- End:基本信息 -- tab_xmms_2>
				
				
				<!-- Start:项目资料 -->
				<div class="tab-pane fade" id="tab_xmms_2">
					<hr>
					<div class="row">
						<div class="col-md-12">
							<!-- Start:Panel -->
							<div class="panel panel-white">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="fa fa-list-ul"></i> 位置1</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="rate" data-original-title="当前的记录数" data-toggle="tooltip" data-placement="top">
												<i class="fa fa-indent text-primary margin-right-10"></i>
												<span id="xmzxRowCounts1" class="value">
													<c:if test="${empty whereaboutsPageForm.whereaboutsPagePictures1}">1</c:if>
													<c:if test="${!empty whereaboutsPageForm.whereaboutsPagePictures1}"><c:out value="${fn:length ( whereaboutsPageForm.whereaboutsPagePictures1 ) }"></c:out></c:if>
												</span>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start:表头 -->
										<div class="table-heads">
											<table class="table table-striped table-bordered">
												<colgroup>
													<col width="15%"></col>
													<col width=""></col>
													<col width="15%"></col>
													<col width="93px"></col>
													<col width="5%"></col>
												</colgroup>
												<thead>
													<tr>
														<th class="center">展示顺序 <span class="symbol required"></span></th>
														<th class="center">资料名称 <span class="symbol required"></span></th>
														<th class="center">资料图片 <span class="symbol required"></span></th>
														<th class="center">图片预览</th>
														<th class="center">操作</th>
													</tr>
												</thead>
											</table>
										</div>
										<!-- End:表头 -->
										<!-- Start:列表 -->
										<div id="xmzxList1" class="table-body perfect-scrollbar" style="height: 380px;">
											<table class="table table-striped table-bordered table-hover">
												<colgroup>
													<col width="15%"></col>
													<col width=""></col>
													<col width="15%"></col>
													<col width="93px"></col>
													<col width="5%"></col>
												</colgroup>
												<tbody>
													<c:if test="${empty whereaboutsPageForm.whereaboutsPagePictures1}">
														<tr>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageSort" class="form-control input-sm" value="" maxlength="2" datatype="n1-2" ignore="ignore"/>
																</div>
															</td>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageName" class="form-control input-sm" value="" maxlength="255" datatype="*1-255" ignore="ignore"/>
																</div>
															</td>
															<td class=" center">
																<div class="row fileupload-buttonbar">
																	<div class="col-lg-12">
																		<span class="btn btn-success fileinput-button"> <i class="glyphicon glyphicon-plus"></i> <span>上传图片</span>
																			<input type="file" name="file"  class="fileupload1" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
																			<input type="hidden" name="imageRealName" value="" />
																			<input type="hidden" name="imagePath" value="" />
																		</span>
																	</div>
																</div>
															</td>
															<td class="center">
																<a class="thumbnails-wrap"
																		data-toggle="popover" data-placement="left" data-trigger="hover" data-html="true" data-container="body"
																		data-title="图片预览" data-content="暂无预览...">
																	<img />
																</a>
															</td>
															<td class="center">
																<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
															</td>
														</tr>
													</c:if>
													<c:forEach items="${whereaboutsPageForm.whereaboutsPagePictures1 }" var="record" begin="0" step="1" varStatus="status">
														<tr>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageSort" class="form-control input-sm" value="<c:out value="${record.imageSort }"></c:out>" maxlength="2" datatype="n1-2" ignore="ignore"/>
																	<hyjf:validmessage key="imageSort${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageName" class="form-control input-sm" value="<c:out value="${record.imageName }"></c:out>" maxlength="255" datatype="*1-255" ignore="ignore"/>
																	<hyjf:validmessage key="imageName${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class=" center">
																<div class="row fileupload-buttonbar">
																	<div class="col-lg-12">
																		<span class="btn btn-success fileinput-button"> <i class="glyphicon glyphicon-plus"></i> <span>上传图片</span>
																			<input type="file" name="file" class="fileupload1" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
																			<input type="hidden" name="imageRealName" value="<c:out value="${record.imageRealName }"></c:out>" />
																			<input type="hidden" name="imagePath" value="<c:out value="${record.imagePath }"></c:out>" />
																		</span>
																	</div>
																</div>
															</td>
															<td class="center">
																<a class="thumbnails-wrap"
																		data-toggle="popover" data-placement="left" data-trigger="hover" data-html="true" data-container="body"
																		data-title="图片预览" data-content="<img src='<c:out value="${record.imageSrc }"></c:out>' style='max-height:350px;'/>">
																	<img src="<c:out value="${record.imageSrc }"></c:out>" />
																</a>
																<hyjf:validmessage key="imageSrc${status.index}"></hyjf:validmessage>
															</td>
															<td class="center">
																<c:if test="${ status.index eq 0}">
																	<a id="fn-addRow1" class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																	<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>											
																<c:if test="${ status.index ne 0}">
																	<a class="btn btn-transparent btn-xs fn-removeRow" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>
															</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
										<!-- End:列表 -->
									</div>
								</div>
							</div>
							<!-- End:Panel -->
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<!-- Start:Panel -->
							<div class="panel panel-white">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="fa fa-list-ul"></i> 位置2</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="rate" data-original-title="当前的记录数" data-toggle="tooltip" data-placement="top">
												<i class="fa fa-indent text-primary margin-right-10"></i>
												<span id="xmzxRowCounts2" class="value">
													<c:if test="${empty whereaboutsPageForm.whereaboutsPagePictures2}">1</c:if>
													<c:if test="${!empty whereaboutsPageForm.whereaboutsPagePictures2}"><c:out value="${fn:length ( whereaboutsPageForm.whereaboutsPagePictures2 ) }"></c:out></c:if>
												</span>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start:表头 -->
										<div class="table-heads">
											<table class="table table-striped table-bordered">
												<colgroup>
													<col width="15%"></col>
													<col width=""></col>
													<col width="15%"></col>
													<col width="93px"></col>
													<col width="5%"></col>
												</colgroup>
												<thead>
													<tr>
														<th class="center">展示顺序 <span class="symbol required"></span></th>
														<th class="center">资料名称 <span class="symbol required"></span></th>
														<th class="center">资料图片 <span class="symbol required"></span></th>
														<th class="center">图片预览</th>
														<th class="center">操作</th>
													</tr>
												</thead>
											</table>
										</div>
										<!-- End:表头 -->
										<!-- Start:列表 -->
										<div id="xmzxList2" class="table-body perfect-scrollbar" style="height: 380px;">
											<table class="table table-striped table-bordered table-hover">
												<colgroup>
													<col width="15%"></col>
													<col width=""></col>
													<col width="15%"></col>
													<col width="93px"></col>
													<col width="5%"></col>
												</colgroup>
												<tbody>
													<c:if test="${empty whereaboutsPageForm.whereaboutsPagePictures2}">
														<tr>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageSort" class="form-control input-sm" value="" maxlength="2" datatype="n1-2" ignore="ignore"/>
																</div>
															</td>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageName" class="form-control input-sm" value="" maxlength="255" datatype="*1-255" ignore="ignore"/>
																</div>
															</td>
															<td class=" center">
																<div class="row fileupload-buttonbar">
																	<div class="col-lg-12">
																		<span class="btn btn-success fileinput-button"> <i class="glyphicon glyphicon-plus"></i> <span>上传图片</span>
																			<input type="file" name="file"   class="fileupload2" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
																			<input type="hidden" name="imageRealName" value="" />
																			<input type="hidden" name="imagePath" value="" />
																		</span>
																	</div>
																</div>
															</td>
															<td class="center">
																<a class="thumbnails-wrap"
																		data-toggle="popover" data-placement="left" data-trigger="hover" data-html="true" data-container="body"
																		data-title="图片预览" data-content="暂无预览...">
																	<img />
																</a>
															</td>
															<td class="center">
																<a id="fn-addRow2" class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
															</td>
														</tr>
													</c:if>
													<c:forEach items="${whereaboutsPageForm.whereaboutsPagePictures2 }" var="record" begin="0" step="1" varStatus="status">
														<tr>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageSort" class="form-control input-sm" value="<c:out value="${record.imageSort }"></c:out>" maxlength="2" datatype="n1-2" ignore="ignore"/>
																	<hyjf:validmessage key="imageSort${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageName" class="form-control input-sm" value="<c:out value="${record.imageName }"></c:out>" maxlength="255" datatype="*1-255" ignore="ignore"/>
																	<hyjf:validmessage key="imageName${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class=" center">
																<div class="row fileupload-buttonbar">
																	<div class="col-lg-12">
																		<span class="btn btn-success fileinput-button"> <i class="glyphicon glyphicon-plus"></i> <span>上传图片</span>
																			<input type="file" name="file" class="fileupload2" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
																			<input type="hidden" name="imageRealName" value="<c:out value="${record.imageRealName }"></c:out>" />
																			<input type="hidden" name="imagePath" value="<c:out value="${record.imagePath }"></c:out>" />
																		</span>
																	</div>
																</div>
															</td>
															<td class="center">
																<a class="thumbnails-wrap"
																		data-toggle="popover" data-placement="left" data-trigger="hover" data-html="true" data-container="body"
																		data-title="图片预览" data-content="<img src='<c:out value="${record.imageSrc }"></c:out>' style='max-height:350px;'/>">
																	<img src="<c:out value="${record.imageSrc }"></c:out>" />
																</a>
																<hyjf:validmessage key="imageSrc${status.index}"></hyjf:validmessage>
															</td>
															<td class="center">
																<c:if test="${ status.index eq 0}">
																	<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																	<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>											
																<c:if test="${ status.index ne 0}">
																	<a class="btn btn-transparent btn-xs fn-removeRow" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>
															</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
										<!-- End:列表 -->
									</div>
								</div>
							</div>
							<!-- End:Panel -->
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<!-- Start:Panel -->
							<div class="panel panel-white">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="fa fa-list-ul"></i> 位置3</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="rate" data-original-title="当前的记录数" data-toggle="tooltip" data-placement="top">
												<i class="fa fa-indent text-primary margin-right-10"></i>
												<span id="xmzxRowCounts3" class="value">
													<c:if test="${empty whereaboutsPageForm.whereaboutsPagePictures3}">1</c:if>
													<c:if test="${!empty whereaboutsPageForm.whereaboutsPagePictures3}"><c:out value="${fn:length ( whereaboutsPageForm.whereaboutsPagePictures3 ) }"></c:out></c:if>
												</span>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start:表头 -->
										<div class="table-heads">
											<table class="table table-striped table-bordered">
												<colgroup>
													<col width="15%"></col>
													<col width=""></col>
													<col width="15%"></col>
													<col width="93px"></col>
													<col width="5%"></col>
												</colgroup>
												<thead>
													<tr>
														<th class="center">展示顺序 <span class="symbol required"></span></th>
														<th class="center">资料名称 <span class="symbol required"></span></th>
														<th class="center">资料图片 <span class="symbol required"></span></th>
														<th class="center">图片预览</th>
														<th class="center">操作</th>
													</tr>
												</thead>
											</table>
										</div>
										<!-- End:表头 -->
										<!-- Start:列表 -->
										<div id="xmzxList3" class="table-body perfect-scrollbar" style="height: 380px;">
											<table class="table table-striped table-bordered table-hover">
												<colgroup>
													<col width="15%"></col>
													<col width=""></col>
													<col width="15%"></col>
													<col width="93px"></col>
													<col width="5%"></col>
												</colgroup>
												<tbody>
													<c:if test="${empty whereaboutsPageForm.whereaboutsPagePictures3}">
														<tr>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageSort" class="form-control input-sm" value="" maxlength="2" datatype="n1-2" ignore="ignore"/>
																</div>
															</td>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageName" class="form-control input-sm" value="" maxlength="255" datatype="*1-255" ignore="ignore"/>
																</div>
															</td>
															<td class=" center">
																<div class="row fileupload-buttonbar">
																	<div class="col-lg-12">
																		<span class="btn btn-success fileinput-button"> <i class="glyphicon glyphicon-plus"></i> <span>上传图片</span>
																			<input type="file" name="file"  class="fileupload3" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
																			<input type="hidden" name="imageRealName" value="" />
																			<input type="hidden" name="imagePath" value="" />
																		</span>
																	</div>
																</div>
															</td>
															<td class="center">
																<a class="thumbnails-wrap"
																		data-toggle="popover" data-placement="left" data-trigger="hover" data-html="true" data-container="body"
																		data-title="图片预览" data-content="暂无预览...">
																	<img />
																</a>
															</td>
															<td class="center">
																<a id="fn-addRow3" class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
															</td>
														</tr>
													</c:if>
													<c:forEach items="${whereaboutsPageForm.whereaboutsPagePictures3 }" var="record" begin="0" step="1" varStatus="status">
														<tr>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageSort" class="form-control input-sm" value="<c:out value="${record.imageSort }"></c:out>" maxlength="2" datatype="n1-2" ignore="ignore"/>
																	<hyjf:validmessage key="imageSort${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageName" class="form-control input-sm" value="<c:out value="${record.imageName }"></c:out>" maxlength="255" datatype="*1-255" ignore="ignore"/>
																	<hyjf:validmessage key="imageName${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class=" center">
																<div class="row fileupload-buttonbar">
																	<div class="col-lg-12">
																		<span class="btn btn-success fileinput-button"> <i class="glyphicon glyphicon-plus"></i> <span>上传图片</span>
																			<input type="file" name="file" class="fileupload3" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
																			
																			<input type="hidden" name="imageRealName" value="<c:out value="${record.imageRealName }"></c:out>" />
																			<input type="hidden" name="imagePath" value="<c:out value="${record.imagePath }"></c:out>" />
																		</span>
																	</div>
																</div>
															</td>
															<td class="center">
																<a class="thumbnails-wrap"
																		data-toggle="popover" data-placement="left" data-trigger="hover" data-html="true" data-container="body"
																		data-title="图片预览" data-content="<img src='<c:out value="${record.imageSrc }"></c:out>' style='max-height:350px;'/>">
																	<img src="<c:out value="${record.imageSrc }"></c:out>" />
																</a>
																<hyjf:validmessage key="imageSrc${status.index}"></hyjf:validmessage>
															</td>
															<td class="center">
																<c:if test="${ status.index eq 0}">
																	<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																	<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>											
																<c:if test="${ status.index ne 0}">
																	<a class="btn btn-transparent btn-xs fn-removeRow" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>
															</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
										<!-- End:列表 -->
									</div>
								</div>
							</div>
							<!-- End:Panel -->
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="margin-top-15">
								<span class="symbol required"></span>必须填写的项目
								<hr>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-5">
							<p>
								点击【提交保存】按钮，保存当前的填写的资料。
							</p>
						</div>
						<div class="col-md-7">
							<div class="btn-group pull-right">
								<a href="#tab_jbxx_1" class="btn btn-primary btn-o btn-wide fn-Next">
									<i class="fa fa-arrow-circle-left"></i> 上一步
								</a>
							</div>
							
							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存</i>
							</a>
						</div>
					</div>
				</div>
				<!-- End:项目资料 -->
				
			<!-- 列表的行模板 -->
			<table id="rowTemplts" type="text/x-tmpl" style="display:none">
				
				<!-- 图片信息的行模板 -->
				<tr>
					<td class="">
						<div class="form-group">
							<input type="text" name="imageSort" class="form-control input-sm" value="" maxlength="2" datatype="n1-2" ignore="ignore"/>
						</div>
					</td>
					<td class="">
						<div class="form-group">
							<input type="text" name="imageName" class="form-control input-sm" value="" maxlength="255" datatype="*1-255" ignore="ignore"/>
						</div>
					</td>
					<td class=" center">
						<div class="row fileupload-buttonbar">
							<div class="col-lg-12">
								<span class="btn btn-success fileinput-button"> <i class="glyphicon glyphicon-plus"></i> <span>上传图片</span>
									<input type="file" name="file" class="fileupload1" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
									<input type="hidden" name="imageRealName" value="" />
									<input type="hidden" name="imagePath" value="" />
								</span>
							</div>
						</div>
					</td>
					<td class="center">
						<a class="thumbnails-wrap"
								data-toggle="popover" data-placement="left" data-trigger="hover" data-html="true" data-container="body"
								data-title="图片预览" data-content="暂无预览...">
							<img />
						</a>
					</td>
					<td class="center">
						<a class="btn btn-transparent btn-xs fn-removeRow" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
					</td>
				</tr>
				<!-- 图片信息的行模板 -->
				<tr>
					<td class="">
						<div class="form-group">
							<input type="text" name="imageSort" class="form-control input-sm" value="" maxlength="2" datatype="n1-2" ignore="ignore"/>
						</div>
					</td>
					<td class="">
						<div class="form-group">
							<input type="text" name="imageName" class="form-control input-sm" value="" maxlength="255" datatype="*1-255" ignore="ignore"/>
						</div>
					</td>
					<td class=" center">
						<div class="row fileupload-buttonbar">
							<div class="col-lg-12">
								<span class="btn btn-success fileinput-button"> <i class="glyphicon glyphicon-plus"></i> <span>上传图片</span>
									<input type="file" name="file" class="fileupload2" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
									<input type="hidden" name="imageRealName" value="" />
									<input type="hidden" name="imagePath" value="" />
								</span>
							</div>
						</div>
					</td>
					<td class="center">
						<a class="thumbnails-wrap"
								data-toggle="popover" data-placement="left" data-trigger="hover" data-html="true" data-container="body"
								data-title="图片预览" data-content="暂无预览...">
							<img />
						</a>
					</td>
					<td class="center">
						<a class="btn btn-transparent btn-xs fn-removeRow" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
					</td>
				</tr>
				
				<!-- 图片信息的行模板 -->
				<tr>
					<td class="">
						<div class="form-group">
							<input type="text" name="imageSort" class="form-control input-sm" value="" maxlength="2" datatype="n1-2" ignore="ignore"/>
						</div>
					</td>
					<td class="">
						<div class="form-group">
							<input type="text" name="imageName" class="form-control input-sm" value="" maxlength="255" datatype="*1-255" ignore="ignore"/>
						</div>
					</td>
					<td class=" center">
						<div class="row fileupload-buttonbar">
							<div class="col-lg-12">
								<span class="btn btn-success fileinput-button"> <i class="glyphicon glyphicon-plus"></i> <span>上传图片</span>
									<input type="file" name="file" class="fileupload3" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
									<input type="hidden" name="imageRealName" value="" />
									<input type="hidden" name="imagePath" value="" />
								</span>
							</div>
						</div>
					</td>
					<td class="center">
						<a class="thumbnails-wrap"
								data-toggle="popover" data-placement="left" data-trigger="hover" data-html="true" data-container="body"
								data-title="图片预览" data-content="暂无预览...">
							<img />
						</a>
					</td>
					<td class="center">
						<a class="btn btn-transparent btn-xs fn-removeRow" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
					</td>
				</tr>
				
				
			</table>
			</form>
		</div>
	</div>
	

	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda-themeless.min.css" rel="stylesheet" media="screen">
	<style>
	.table-heads .table-striped {
		margin-bottom: 0;
	}
	.table-body {
		position: relative;
	}
	.table-body .vertical-align-top {
		vertical-align: top!important;
	}
	.table-body .table-striped {
		border-top: none;
		margin-bottom: 0;
	}
	.table-body .table-striped tr:first-child td {
		border-top: 0;
	}
	.table-body .form-group {
		margin-bottom: 0;
	}
	.thumbnails-wrap {
		background: #f5f5f5;
		border: 1px solid #ccc;
		padding: 3px;
		display: inline-block;
	}
	.thumbnails-wrap img {
		min-width: 35px;
		max-width: 70px;
		height: 22px;
	}
	.popover {
		max-width: 500px;
	}
	.popover img {
		max-width: 460px;
	}
	</style>
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/spin.min.js"></script>
	    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/tinymce/jquery.tinymce.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/region-select.js"></script>
		<script type="text/javascript" src="${themeRoot}/assets/js/common.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.ui.widget.js"></script>
		<!-- The Templates plugin is included to render the upload/download listings -->
<%-- 		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/tmpl.min.js"></script> --%>
		<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/load-image.all.min.js"></script>
		<!-- The Canvas to Blob plugin is included for image resizing functionality -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/canvas-to-blob.min.js"></script>
		<!-- blueimp Gallery script -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.blueimp-gallery.min.js"></script>
		<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.iframe-transport.js"></script>
		<!-- The basic File Upload plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload.js"></script>
		<!-- The File Upload processing plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-process.js"></script>
		<!-- The File Upload image preview & resize plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-image.js"></script>
		<!-- The File Upload audio preview plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-audio.js"></script>
		<!-- The File Upload video preview plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-video.js"></script>
		<!-- The File Upload validation plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-validate.js"></script>
		<!-- The File Upload user interface plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-ui.js"></script>

	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/whereaboutspage/whereaboutspageInit.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
