/* 对应JAVA的Controllor的@RequestMapping */
	var Action = {
		//保存并发布
		publishAction:"publishAction",
		//跳转到新增页面
		infoAction:"infoAction",
		//修改月请求
		modifyMonthAction:"modifyMonthAction",
		//修改季度请求
		modifyQuarterAction:"modifyQuarterAction",
		//修改半年
		modifyHalfAction:"modifyHalfAction",
		//修改年
		modifyYearAction:"modifyYearAction",
		uploadFileAction:"uploadFileAction"
	};
	var flag=true;
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {	
		//长度限制
		lengthLimitBlur :function(){
			var value=$(this).val();
			if (value != null&&value != ""){
					var len=value.length;
					if(len >12){
						var value=$(this).val("");
						$(this).attr("placeholder","长度不能超过12!");
						flag=false;
						return;
					}
				}
		},

		//只能输入字母
		engRestrict:function(){
			//验证英文标题
			var value=$(this).val();
			if (value != null&&value != ""){
				if(!value.match("^[a-zA-Z]+$")){
					var value=$(this).val("");
					$(this).attr("placeholder","只能输入英文字符!");
					flag=false;
					return;
					}
				}
		},
		//保留小数点后两位
		KeepTwoDecimalPlaces:function(){
			var value=$(this).val();
			if(value != null&&value!= ""){
			//[0-9]*[.]+[0-9]+
//				if(!(value.match("^[0-9]+[\.][0-9]+$")||value.match("^[0-9]+$"))){
//				if(!value.match("^[0-9]+[\.][0-9]+$|^[0-9]+$")){
				if(!value.match("^([0-9]+[\.][0-9]+|^[0-9]+)$")){
//				if(!value.match("^([0-9]+[\.][0-9]+)|([0-9]+)$")){
					var value=$(this).val("");
					$(this).attr("placeholder","只能输入正数!");
					flag=false;
					return;
				} 
				  var f = parseFloat(value); 
				  if (isNaN(f)) { 
					  flag=false;
						return;
				  } 
				  var f = Math.round(value*100)/100; 
				  var s = f.toString(); 
				  var rs = s.indexOf('.'); 
				  if (rs < 0) { 
					  rs = s.length; 
					  s += '.'; 
				  } 
				  while (s.length <= rs + 2) { 
					  s += '0'; 
				  } 

				var id = $(this).attr("id");
				$("#"+id).val(s);
			}
		},
		//只允许是正正数
		positiveNumber:function(){
			var value=$(this).val();
			if(value != null&&value != ""){
				if(!value.match("^[0-9][0-9]*$")){
					var value=$(this).val("");
					$(this).attr("placeholder","只能输入正整数！");
					flag=false;
				}
			}
		},
		//上传图片
		changepc:function(){
				var pcName = $("#fileupload").val();
				$("#imgurl").val(pcName);
	
			},
		andOption :function(val,txt){
			var opt=document.createElement("option");
			opt.text=txt;
			opt.value=val;
			child.options.add(opt);
		},
		twelveMonth :function(){
			var value=$(this).val();
			if(value != null&&value != ""){
				if(parseInt(value)>12||parseInt(value)<1){
					var value=$(this).val("");
					$(this).attr("placeholder","只能输入正确月份！");
					flag=false;
				}
			}
		},
		year :function(){
			var value=$(this).val();
			if(value != null&&value != ""){
				if(!value.match("^[1,2,3,4,5,6,7,8,9][0-9]{3}$")){
					var value=$(this).val("");
					$(this).attr("placeholder","只能输入年！");
					flag=false;
				}
			}
		},
		//select标签change事件
		_reportTypeChange:function(){
			if($("#operationReportType").val()==1){
				$("#monthDiv").show();
				$("#myiframe").html("<iframe  style='height:80%;width:100%;'  frameborder='0' name='myFrame' src='"+webPath+"/manager/content/operationreport/monthinit?month="+$("#month").val()+"&&year="+$("#year").val()+"'></iframe>");

			}else if($("#operationReportType").val()==5||$("#operationReportType").val()==6){
				$("#monthDiv").hide();
				$("#myiframe").html("<iframe style='height:80%;width:100%;' frameborder='0' name='myFrame' src='"+webPath+"/manager/content/operationreport/quarterinit?operationReportType="+$("#operationReportType").val()+"&&year="+$("#year").val()+"'></iframe>");
			}else if($("#operationReportType").val()==3){
				$("#monthDiv").hide();
				$("#myiframe").html("<iframe style='height:80%;width:100%;' frameborder='0' name='myFrame' src='"+webPath+"/manager/content/operationreport/halfyearinit?year="+$("#year").val()+"'></iframe>");
			}else{
				$("#monthDiv").hide();
				$("#myiframe").html("<iframe style='height:80%;width:100%;' frameborder='0' name='myFrame' src='"+webPath+"/manager/content/operationreport/yearinit?year="+$("#year").val()+"'></iframe>");
			}
			//判断是新增还是修改?
//			var operationReportId=$('#id').val();
//			var operationReportType = $("#operationReportType").find("option:selected").val();
//			if(operationReportId == null||operationReportId == ""){
//				if (operationReportType == "") {
//					return;
//				}
//				if(operationReportType == '1'){
//					$("#monthDiv").css("display","inline-block");
//				}
//				if(operationReportType != '1'){
//					$("#monthDiv").css("display","none");
//				}
//				$.ajax({
//					url : Action.infoAction,
//					type : "POST",
//					text : "",
//					async : true,
//					dataType : "json",
//					data :  {
//						operationReportType : operationReportType
//					},
//					success : function(data) {
//						$("#operationReportType").select2({
//							data: data,
//						  	width : 200,
//							placeholder : "全部",
//							allowClear : true,
//							language : "zh-CN"
//						});
//						$("#operationReportType").val(operationReportType);
//						Events.assetTypeChange();
//					},
//					error : function(err) {
//						Page.alert("","没有对应的运营报告类型!");
//					}
//				});
//			}else{
//				if(operationReportType !=1){
//					$("#monthDiv").css("disabled","disabled");
//				
//				}
//			}
		},
		//保存并发布
		publishSubmit: function(event) {
			var isRelease=1; 
			if(!flag){
				return;
			}
			
			$("#mainForm").submit();
		},
		//保存
		addSubmit: function(event) {
			if(!flag){
				return;
			}
			$('#fileupload').fileupload({
				url : "uploadFileAction",
				autoUpload : true,
				done : function(e, data) {
					var file = data.result[0];
					$(this).parents("div.picClass").find("div.purMargin input").val(file.imagePath);
					/*$("#image").val(file.imagePath);*/
				}
			});
			$("#mainForm").submit();
		},
		
		//添加活动
		 addActive:function(){
			
			var str='<div style="width: 1800px;height: 200px;">'
					+'<div style="float: left;margin: 50px 0px 0px 10px;">'
					+'<p><span style="font-family:\'应用字体 Regular\', \'应用字体\';margin: auto">活动标题：</span></p>'
					+'</div>'
					+'<div style="float: left;margin:50px 0px 0px 95px">'
					+'<input type="text" id="activtyName" name="activtyName" value="" class="form-control"></input>'
					+'</div>'
					+'<div style="float: left;margin:50px 0px 0px 60px">'
					+'<p><span style="font-family:\'应用字体 Regular\', \'应用字体\';">活动时间：</span></p>'
					+'</div>'
					+'<div style="float: left;margin:50px 0px 0px 30px">'
					+'<input style="width:180px;border:1px solid #c8c7cc" type="text" name="startTime" id="activtyStartTime" class="form-control underline" value="" />' 
					+'</div>'
					+'<div style="float: left;margin:60px 0px 0px 20px">'
					+'<p><span style="font-family:\'应用字体 Regular\', \'应用字体\';">至</span></p>'
					+'</div>'
					+'<div style="float: left;margin:50px 0px 0px 20px">'
					+'<input  style="width:180px;border:1px solid #c8c7cc" type="text" name="endTime" id="activtyEndTime" class="form-control underline" value="" />'
					+'</div>'
					+'<div style="float: left;width: 45%;" class="fileinput fileinput-new col-xs-6 picClass" data-provides="fileinput">'
					+'<div style="float: left;margin:60px 0px 0px 25px;width: 10%;">'
					+'<p><span style="font-family:\'应用字体 Regular\', \'应用字体\';">活动图片：</span></p>'
					+'</div>'
					+'<div style="float: left;margin:67px 0px 0px 10px;width:30%" class="fileinput-preview fileinput-exists thumbnail"></div>'
					+'<div style="display:none;width: 100%;" class="purMargin">'
					+'<input type="text" readonly="readonly" datatype="*" name="image" id="image" value="" placeholder="上传图片路径" />'
					+'</div>'
					+'<div style="float: left;margin:55px 0px 0px 18px;width: 10%;" class="user-edit-image-buttons">'
					+'<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fa-picture"></i> 上传图片</span><span class="fileinput-exists"><i class="fa fa-picture"></i></span><input type="file" name="file" id="fileupload" class="fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff"></span>'
					+'</a>'
					+'</div>'
					+'<hyjf:validmessage key="type" label="image"></hyjf:validmessage>'
					+'</div>'
					+'</div><br/><br/><br/><br/>'
					
					
					$("#insertDiv").append(str);
				           
		},

		//添加足迹
		 addFootprint:function(){
			var str ='<div style="margin: 0px 0px 0px 11px;" class="col-xs-10">'
					+'<div style="display:inline-block">'
					+'<p><span style="font-family:\'应用字体 Regular\', \'应用字体\';margin: auto">标题：</span></p>'
					+'</div>'
					+'<div style="display:inline-block;margin:8px 0px 0px 115px">'
					+'<input type="text" id="activtyName" name="activtyName" value="" class="form-control">'
					+'</input>'
					+'</div>'
					+'<div style="display:inline-block;margin:8px 0px 0px 60px">'
					+'<p><span style="font-family:\'应用字体 Regular\', \'应用字体\';">活动时间：</span></p>'
					+'</div>'
					+'<div style="width:15%;display:inline-block;margin:8px 0px -10px 28px;" class="input-group input-daterange datepicker">'
					+'<input style="width:180px;border:1px solid #c8c7cc" type="text" name="time" id="activtyTime" class="form-control underline" value=""/>'
					+'</div>'
					+'</div>'
					
					$("#insertFootActive").append(str);
			
		}
		

};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#ids"),
	// 画面布局
	doLayout: function() {
		$(".form-select2").select2({
			width : 200,
			placeholder : "月度运营报告",
			allowClear : true,
			language : "zh-CN"
		}),
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose: true,
			todayHighlight: true
		});
	},
		
	initEvents:function() {
			//select标签绑定change事件
			$("#operationReportType").change(Events._reportTypeChange),
			//twelveMonth
			$(".monthStyle").blur(Events.twelveMonth),
			//yearStyle
			$(".yearStyle").blur(Events.year),
			// 中文字段验证
			$("#cnName").blur(Events.lengthLimitBlur),
			// 英文字段验证 engRestrict_clean
			$("#enName").blur(Events.engRestrict),
			//保留两位小数
			$(".moneyStyle").blur(Events.KeepTwoDecimalPlaces),
			//只能输入整数
			$(".positiveNumber").blur(Events.positiveNumber),
//			$("#allAmount").blur(Events.KeepTwoDecimalPlaces(this)),
			//添加活动
			$("#addActive").click(Events.addActive),
			//添加足迹
			$("#addFootprint").click(Events.addFootprint),
			//图片改变
			$("#fileupload").change(Events.changepc),
	        //发布
	        $("#addSubmit").click(Events.addSubmit);
	        //发布并保存
	        $("#publishSubmit").click(Events.publishSubmit);
	        //图片选中上传
			$('.fileupload').fileupload({
				url : "uploadFileAction",
				autoUpload : true,
				done : function(e, data) {
					var file = data.result[0];
					$(this).parents("div.picClass").find("div.purMargin input").val(file.imagePath);
					/*$("#imgurl").val(file.imagePath);*/
				}
			});
	}

});




