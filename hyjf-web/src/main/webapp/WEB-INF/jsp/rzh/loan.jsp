<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta charset="utf-8" />
<title>融资汇 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="hjk-top">
		<h2>融资汇</h2>
		<div class="cut-line"></div>
		<div class="subtitle">资金一站到位，融资期限、融资形式任选</div>
	</div>
	<h3 class="hjk-form-title">请填写基本信息</h3>
	<section class="hjk-container">
	<form action="${ctx }/rzh/apply/apply.do" id="hjkForm" mehtod="post">
		<div class="new-form">
			<div class="new-form-item item-1 mr20">
				<label for="name" class="new-form-label iconfont iconfont-name"></label>
				<input type="text" name="name" class="new-form-input" maxlength="4"
					id="name" placeholder="您的姓名" datatype="s1-4" errormsg="姓名为1-4位汉字！"/>
			</div>
			<div class="new-form-item item-1">
				<label for="mobile" class="new-form-label iconfont iconfont-tel"></label>
				<input type="text" name="mobile" class="new-form-input" id="mobile"
					maxlength="11" placeholder="您的联系电话" 
					datatype="n11" errormsg="手机号不正确！"/>
			</div>
			<div class="new-form-item item-1 mr20">
				<label class="new-form-label iconfont iconfont-gender"></label>
				<h4 class="new-form-input iconfont iconfont-arrow">男</h4>
				<div class="new-form-hdselect select-1">
					<!-- for为需要存放值的隐藏input的id -->
					<ul for="sex" id="sex">
						<!-- value为选项的值 -->
						<li value="0">男</li>
						<li value="1">女</li>
					</ul>
				</div>
				<input type="hidden" name="sex" id="sex" value="0">
			</div>
			<div class="new-form-item item-1">
				<label for="price" class="new-form-label iconfont iconfont-money"></label>
				<input type="text" name="price" class="new-form-input" id="price"
					placeholder="请输入融资金额" maxlength="10" /> <span class="unit">万元</span>
			</div>
			<div class="clearfix"></div>
			<h3 class="hjk-form-title">请选择融资期限</h3>
			<div class="new-radio-group">
				<div class="radio-item checked" value="1">1个月</div>
				<div class="radio-item" value="2">2个月</div>
				<div class="radio-item" value="3">3个月</div>
				<div class="radio-item" value="6">6个月</div>
				<div class="radio-item" value="99">其它</div>
				<input type="hidden" name="approach" id="approach" value="1">
			</div>
			<div class="new-form-item item-2 mr20">
				<input type="text" name="use" class="new-form-input"
					id="use" placeholder="请输入融资用途" maxlength="30" />
			</div>
			<div class="new-form-item item-2">
				<input type="text" name="info" class="new-form-input" id="info"
					placeholder="请输入抵质押物" maxlength="30" />
			</div>
			<div class="clearfix"></div>
			<h3 class="hjk-form-title">请选择地址</h3>
			<div class="select-group address-group">
				<div class="new-form-item item-3 mr20">
					<h4 class="new-form-input iconfont iconfont-arrow">省份</h4>
					<div class="new-form-hdselect select-2">
						<ul for="province" id="ForProvince">
							<li value="请选择">请选择</li>
						</ul>
					</div>
					<input type="hidden" name="province" id="province">
				</div>
				<div class="new-form-item item-3 mr20">
					<h4 class="new-form-input iconfont iconfont-arrow">市</h4>
					<div class="new-form-hdselect select-2">
						<ul for="city" id="ForCity">
							<li value="请选择">请选择</li>
						</ul>
					</div>
					<input type="hidden" name="city" id="city">
				</div>
				<div class="new-form-item item-3">
					<h4 class="new-form-input iconfont iconfont-arrow">区</h4>
					<div class="new-form-hdselect select-2">
						<ul for="area" id="ForCounty">
							<li value="请选择">请选择</li>
						</ul>
					</div>
					<input type="hidden" name="area" id="area" value="请选择">
				</div>
			</div>
			<div class="clearfix"></div>
			<div class="more-new-form-open">
				<div class="closed">
					<span>填写更多企业信息</span>
				</div>
				<div class="opened">
					<span class="line"></span> <span class="txt">填写更多企业信息</span> <span
						class="line"></span>
				</div>
			</div>
			<div class="more-new-form">
				<div class="new-form-item item-2 mr20">
					<input type="text" name="gname" class="new-form-input"
						id="gname" placeholder="请输入企业名称" maxlength="30" />
				</div>
				<div class="new-form-item item-2">
					<input type="text" name="gdress" class="new-form-input" 
						id="gdress" placeholder="请输入经营地址" maxlength="30" />
				</div>
				<div class="new-form-item item-2 mr20">
					<input type="text" name="gpro" class="new-form-input" 
						id="gpro" placeholder="请输入主营业务" maxlength="30" />
				</div>
				<div class="new-form-item item-2">
					<input type="text" name="gplay" class="new-form-input" id="gplay"
						placeholder="请输入所属行业" maxlength="30" />
				</div>
				<div class="new-form-item item-3 mr20">
					<input type="text" name="gget" class="new-form-input" id="gget"
						placeholder="请输入年利润额" maxlength="10" /> <span class="unit">元</span>
				</div>
				<div class="new-form-item item-3 mr20">
					<input type="text" name="gmoney" class="new-form-input"
						id="gmoney" placeholder="请输入年营业额" /> <span class="unit">元</span>
				</div>
				<div class="new-form-item item-3">
					<input type="text" name="gpay" class="new-form-input" id="gpay"
						placeholder="请输入银行贷款" /> <span class="unit">元</span>
				</div>
				<h3 class="hjk-form-title">请选择成立时间</h3>
				<div class="select-group address-group">
					<div class="new-form-item item-3 mr20">
						<h4 class="new-form-input iconfont iconfont-arrow">年</h4>
						<div class="new-form-hdselect select-2">
							<!-- for为需要存放值的隐藏input的id -->
							<ul for="year" id="ForYear">
								<!-- value为选项的值 -->
							</ul>
						</div>
						<input type="hidden" name="year" id="year">
					</div>
					<div class="new-form-item item-3 mr20">
						<h4 class="new-form-input iconfont iconfont-arrow">月</h4>
						<div class="new-form-hdselect select-2">
							<!-- for为需要存放值的隐藏input的id -->
							<ul for="month" id="ForMonth">
								<!-- value为选项的值 -->
								<li value="">月</li>
							</ul>
						</div>
						<input type="hidden" name="month" id="month">
					</div>
					<div class="new-form-item item-3">
						<h4 class="new-form-input iconfont iconfont-arrow">日</h4>
						<div class="new-form-hdselect select-2">
							<!-- for为需要存放值的隐藏input的id -->
							<ul for="date" id="ForDate">
								<!-- value为选项的值 -->
								<li value="">日</li>
							</ul>
						</div>
						<input type="hidden" name="date" id="date">
					</div>
				</div>
			</div>
			<div class="clearfix"></div>
			<a class="btn new-form-btn fn-Confirm" id="formSubmit">提 交</a>
		</div>
	</form>
	</section>
	<section class="hjk-bg">
	<ul class="hjk-bg-inner">
		<li class="dq">
			<div class="title">地区</div>
			<p>为让自身的安全保障承诺具备可行性，更好地控制资金风险，我们目前只受理北京、上海、内蒙、山东地区的融资申请，后期将根据业务发展情况，在其他城市开发融资服务，请关注公告。</p>
		</li>
		<li class="sh">
			<div class="title">审核</div>
			<p>由于审核部门每天要对大量的融资信息做审核工作，因此我们仅对通过初审的融资申请进行电话回访，并告知接下来的融资流程。</p>
		</li>
		<li class="sy">
			<div class="title">收益</div>
			<p>汇盈金服只为投资人与融资方搭建提供信息服务的平台，本身并不提供任何资金，所有资金均来自汇盈金服的注册投资用户。</p>
		</li>
	</ul>
	</section>
	<script>setActById('hdRZH');</script>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript" src="${ctx}/js/jquery.validate.js"
		charset="utf-8"></script>
	<script type="text/javascript"
		src="${ctx}/js/jquery.placeholder.min.js" charset="utf-8"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.metadata.js"
		charset="utf-8"></script>
	<script type="text/javascript" src="${ctx}/js/cities.js"
		charset="utf-8"></script>
	<script type="text/javascript" src="${ctx}/js/customform.js"
		charset="utf-8"></script>
	<script type="text/javascript" src="${ctx}/js/messages_cn.js"
		charset="utf-8"></script>
	<script type="text/javascript" src="${ctx}/js/over.js" charset="utf-8"></script>

	<%-- <script type="text/javascript" src="${ctx}/js/rzh/loan.js" charset="utf-8"></script> --%>
	<script type="text/javascript">
	function checkTime(i) {
		if (i < 10) {
			i = "0" + i;
		}
		return i;
	}
	// 下拉菜单非默认--请选择  省市区
	jQuery.validator.addMethod("emptySelect", function(value, element) {
		return $.trim(value) != "" && $.trim(value) != "请选择";
	}, "请选择");
	
	jQuery.validator.addMethod("cnname", function(value, element) {
		var reg = /^[\u4e00-\u9fa5]+$/;
		return reg.test(value);
	}, "请选择");
	
	var validate = $("#hjkForm").validate({
		"rules" : {
			"name" : {
				"required":true,
				"cnname":true
			},
			"mobile" : {
				"required" : true,
				"isMobile" : true
			},
			"sex" : "required",
			"price" : {
				"number" : true,
				"required" : true
			},
			"approach" : "required",
			"use" : "required",
			"info" : "required",
			"province" : "emptySelect",
			"city" : "emptySelect",
			"area" : "emptySelect",
			"gmoney" : "number",
			"gget" : "number",
			"gpay" : "number"
		},
		"messages" : {
			"name" : {
				"required":"这是必填字段",
				"cnname":"只能输入汉字"
			},
			"mobile" : {
				"required" : "这是必填字段",
				"isMobile" : "请输入正确的电话"
			},
			"sex" : "请选择性别",
			"price" : {
				"number" : "请输入正确的金额",
				"required" : "这是必填字段"
			},
			"approach" : "这是必填字段",
			"use" : "这是必填字段",
			"info" : "这是必填字段",
			"province" : "请选择省份",
			"city" : "请选择城市",
			"area" : "请选择区县",
			"gmoney" : "请输入正确的金额",
			"gget" : "请输入正确的金额",
			"gpay" : "请输入正确的金额"
		},
		"ignore" : ".ignore",
		errorPlacement : function(error, element) {
			error.insertAfter(element);
		},
		highlight : function(element, errorClass) { //针对验证的表单设置高亮
			$(element).parent().addClass(errorClass);
		},
		submitHandler : function(form) {
			form.submit();
		}

	});

	customForm.init();
	customForm.citySelection(cities);

	//提交
	$("#formSubmit").click(function() {
		$("#hjkForm").submit();
	});

	$("input.new-form-input").keyup(function() {
		if ($("#hjkForm").validate().element(this)) {
			$(this).parent().removeClass("error");
		}
	})

	$("input:hidden").change(function() {
		if ($("#hjkForm").validate().element(this)) {
			$(this).parent().removeClass("error");
		}
	})
	
	$("#price,#mobile,#gmoney,#gget,#gpay").keyup(function(){
		var val = $(this).val();
		var reg = /[^\d]/g;
		$(this).val(val.replace(reg,""));
	});
	</script>
</body>

</html>