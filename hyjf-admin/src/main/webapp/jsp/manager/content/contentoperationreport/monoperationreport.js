$(function(){
    var validatError= $("#monthForm").Validform({
        tiptype:3,
        postonce:true,
        showAllError:true
    });

    $("#publishSubmit").ready(function () {
        var isRelease= $("#isRelease").val();
        if(isRelease == 1){
            $("#publishSubmitControl").hide();
        }else{
            $("#publishSubmitControl").show();
        }
    });

    //保留小数点后两位
    $(".moneyStyle").blur(function(){
        var value=$(this).val().trim();
        if(value != null&&value!= ""){
            var s = value.toString();
            var rs = s.indexOf('.');
            if (rs < 0) {
                rs = s.length;
                s += '.';
            }
            while (s.length <= rs + 2) {
                s += '0';
            }
            if(s.length>rs+2){
                s=s.substr(0,rs+3);
            }
            var id = $(this).attr("id");
            $("#"+id).val(s);
            flag=true;
        }
    });
    //对整数进行头尾去空
    $(".positiveNumber").blur(function(){
        var value=$(this).val();
        if(value != null&&value != ""){
            value= value.trim();
            $(this).val(value);
        }
    });
    $(".ageNumber").blur(function(){
        var value=$(this).val().trim();
        if(value != null&&value != ""){
            $(this).val(value);
        }
    });

    //图片改变
    $("#fileupload").change(function(){
        var pcName = $("#fileupload").val();
        $("#activtyPictureUrl").val(pcName);
    });
    //保留小数点后两位
    //图片改变
    $("#fileupload").change(function(){
        var pcName = $("#fileupload").val();
        $("#activtyPictureUrl").val(pcName);
    });

    //添加活动
    $(".addActive").click(function(){
        var len=$(".wonderfulActivity").length;
        var str='<div id="insertDiv2" class="wonderfulActivity" style="min-width: 1000px;min-height: 0px;;display:inline-block;">'
            +'<input type="hidden"  id="wonderfulActivities'+len+'" name="wonderfulActivities['+len+'].activtyType" value="2"/>'
            +'<input type="hidden" name="wonderfulActivities['+len+'].id" id="wonderfulActivitieId'+len+'" />'
            +'<div style="min-width: 1400px;min-height: 80px;" >'
            +'<div style="float: left;margin:30px 0px 10px 10px;">'
            +'<p><span style="font-family:\'应用字体 Regular\', \'应用字体\';margin: auto">活动标题：</span></p>'
            +'</div>'

            +'<div style="float: left;margin:30px 0px 0px 20px;">'
            +'<input type="text"  class="wonderfulActivitiesactivtyName"  name="wonderfulActivities['+len+'].activtyName" value="" class="form-control"   />'
            +'<span class="Validform_checktip"></span>'
            +'</div>'

            +'<div style="float: left;margin:30px 0px 0px 35px">'
            +'<p><span style="font-family:\'应用字体 Regular\', \'应用字体\';">活动时间：</span></p>'
            +'</div>'

            +'<div style="float: left;margin:30px 0px 0px 30px;">'
            +'<input style="width:180px;border:1px solid #c8c7cc" readonly="readonly" type="text" name="wonderfulActivities['+len+'].startTime" onclick="WdatePicker();" class="form-control underline datepicker"/>'
            +'<span class="Validform_checktip"></span>'
            +'</div>'

            +'<div style="float: left;margin:36px 0px 0px 20px">'
            +'<p><span style="font-family:\'应用字体 Regular\', \'应用字体\';">至</span></p>'
            +'</div>'

            +'<div style="float: left;margin:30px 0px 0px 30px">'
            +'<input  style="width:180px;border:1px solid #c8c7cc" readonly="readonly" type="text" name="wonderfulActivities['+len+'].endTime" onclick="WdatePicker();" class="form-control underline datepicker" />'
            +'<span class="Validform_checktip"></span>'
            +'</div>'

            +'<div style="float: left;width: 20%;" class="fileinput fileinput-new col-xs-6 picClass" data-provides="fileinput">'
            +'<div style="float: left;margin:35px 0px 0px 45px;">'
            +'<p><span style="font-family:\'应用字体 Regular\',\'应用字体\';">活动图片：</span></p>'
            +'</div>'

            +'<div style="float: left;margin:35px 0px 0px 10px;" height="20" width="20" class="fileinput-preview fileinput-exists thumbnail myimg"/>'

            +'<input type="hidden" readonly="readonly" name="wonderfulActivities['+len+'].activtyPictureUrl" class="activtyPictureUrl'+len+' activtyPictureUrl"  value="" />'
            +'<div style="float: left;margin:-38px 0px 0px 180px;" class="user-edit-image-buttons">'
            +'<span class="btn btn-azure btn-file" ><input type="file" name="file" value="上传" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff" onclick="fileUpload('+len+')" class="fileupload"></span>'
            +'</div>'
            +'</div>'
            +'<div style="float: right;margin:-31px 604px 0px 250px" class="deleteWonderfulActivity">'
            +'<input type="button" onclick="deleteWonderfulActivity_Button('+len+');" value="删除本条"/>'
            +'</div>'
            +'</div>'
        $("#insertDiv").append(str);

    });

    //添加足迹
    $(".addFootprint").click(function(){
        var len=$(".wonderActivity").length;
        var str ='<div style="margin: 0px 0px 0px -16px;height: 80px;" class="wonderActivity col-xs-10">'
            +'<input type="hidden" id="wonderActivities'+len+'" name="footprints['+len+'].activtyType" value="3"/>'
            +'<input type="hidden" name="footprints['+len+'].id" id="footprintId'+len+'"  />'
            +'<div style="float:left;margin:1px -30px -20px 23px">'
            +'<p><span style="font-family:\'应用字体 Regular\', \'应用字体\';margin: auto">标题：</span></p>'
            +'</div>'
            +'<div style="float:left;margin:-3px 0px 0px 75px">'
            +'<input type="text" name="footprints['+len+'].activtyName" value="" class="form-control" />'
            +'<span class="Validform_checktip"></span>'
            +'</div>'
            +'<div style="float:left;margin:1px 0px 0px 28px">'
            +'<p><span style="font-family:\'应用字体 Regular\', \'应用字体\';">活动时间：</span></p>'
            +'</div>'
            +'<div style="width:15%;float:left;margin:-5px 0px -10px 25px;" class="input-group input-daterange datepicker ">'
            +'<input style="width:180px;border:1px solid #c8c7cc" readonly="readonly" type="text" name="footprints['+len+'].time" onclick="WdatePicker();" value="" />'
            +'<span class="Validform_checktip"></span>'
            +'</div>'
            +'<div style="float: right;margin:-1px 253px 0px 0px" class="deleteWonderfulActivity">'
            +'<input type="button" onclick="deleteWonderActivity_Button('+len+');" value="删除本条"/>'
            +'</div>'
            +'</div>'
        $("#insertFootActive").append(str);
    });
    //自定义校验
    function coustomValidform(){
        var flag = true;
        $(".wonderfulActivitiesActivtyName").each(function (e) {
            if($(this).val()!=""){
                if($(this).parent().nextAll().find(".activtyPictureUrl").val()==""){
                    alert("请上传活动图片！");
                    flag =  false;
                }
            }

        });
        return flag;
    };
    //保存并发布
    $("#publishSubmit").click(function(){
        $("#monthForm").action="monthOperationReportAction";
        $("#isRelease").val(1);
        $("#monthInput").val(parent.document.getElementById("month").value);
        $("#yearInput").val(parent.document.getElementById("year").value);
        var monthInput=$("#monthInput").val();
        var yearInput=$("#yearInput").val();
        if(monthInput == ""||yearInput == ""){
            $(".monyear_error").html("您的年或月为空，请输入！");
        }else if(coustomValidform()){
            $("#monthForm").submit();
        }
    });
    //保存   "monthOperationReportAction"
    $("#addSubmit").click(function(){
        $("#monthForm").action="monthOperationReportAction";
        $("#monthInput").val(parent.document.getElementById("month").value);
        $("#yearInput").val(parent.document.getElementById("year").value);
        var monthInput=$("#monthInput").val();
        var yearInput=$("#yearInput").val();
        if(monthInput == ""||yearInput == ""){
            $(".monyear_error").html("您的年或月为空，请输入！");
        }else if(coustomValidform()){
            $("#monthForm").submit();
        }
    });
    //预览addPreview
    $("#addPreview").click(function(){
        $("#monthInput").val(parent.document.getElementById("month").value);
        $("#yearInput").val(parent.document.getElementById("year").value);
        var flag= validatError.check();
        var monthInput=$("#monthInput").val();
        var yearInput=$("#yearInput").val();
        if(monthInput == ""||yearInput == ""){
            $(".monyear_error").html("您的年或月为空，请输入！");
        }else if(coustomValidform()&&flag) {
            // 新开页面
            var newTab=window.open('about:blank');
            $.ajax({
                url : webPath+"/manager/content/operationreport/previewAction" ,//url
                type : "POST",
                dataType: "json",//预期服务器返回的数据类型
                data :  $('#monthForm').serialize(),
                success : function(data) {
                    if (data!=null||data!="") {
                        var res=eval(data);
                        var operationReportId= res.operationId;
                        var monthlyOperationReportId=res.monthlyOperationReportId;
                        var userOperationReportId=res.userOperationReportId;
                        var tenthOperationReportId=res.tenthOperationReportId;
                        var footprintIdList=res.footprintList;
                        var wonderfulActivitieList=res.wonderfulActivitieIdList;
                        if($("#operationReportId").val() != null||$("#operationReportId").val() != ""){
                            $("#operationReportId").val(operationReportId);
                            $("#monthId").val(monthlyOperationReportId);
                            $("#userId").val(userOperationReportId);
                            $("#tenthId").val(tenthOperationReportId);
                        }
                        //跳转到新开页面
                        newTab.location.href=res.path;
                    }
                }
            });
        }

    });
    //图片选中上传
    $('.fileupload').fileupload({
        url : "uploadFileAction",
        autoUpload : true,
        done : function(e, data) {
            var file = data.result[0];
            $(this).parent().parent().prev().val(file.imagePath);
        }
    });

    //日历选择器
    $('.datepicker').click(function(){
        WdatePicker();
    });

});
//精彩活动的删除按钮
function deleteWonderfulActivity_Button(len){
    var len=len;
    $("#wonderfulActivities"+len).parent().remove();
}
//足迹的删除按钮
function deleteWonderActivity_Button(len){
    var len=len;
    //alert($("#wonderActivities"+len));
    $("#wonderActivities"+len).parent().remove();
}


function fileUpload(len) {
    $(".fileupload").fileupload({
        url : "uploadFileAction",
        autoUpload : true,
        done : function(e, data) {
            var file = data.result[0];
            $(this).parent().parent().prev().val(file.imagePath);
        }
    });
}