var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 事件动作处理 */
    Events = {
    };
// // --------------------------------------------------------------------------------------------------------------------------------
// /* 画面对象 */
$.extend(Page, {
    // 画面布局
    doLayout: function() {
        // 条件下拉框
        $(".form-select2").select2({
            placeholder: "请选择球队",
            allowClear: true,
            language: "zh-CN"
        })
    }
})

function submitButton(e) {
    Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
        if(isConfirm) {
            var index = $(e).attr("id").substring(6);
            var id=$("#id"+index).val();
            var matchType=$("#matchType"+index).val();
            var winTeamId=$("#statusSrch"+index).val();
            var matchResult=$("#statusSrch"+index+"  option:selected").text();
            if(winTeamId != null && winTeamId != ''){
            	$.ajax({
            		url : "updateWorldCupMatch" ,//url
            		type : "POST",
            		dataType: "json",//预期服务器返回的数据类型
            		data :  {"id":id,"matchType":matchType,"matchResult":matchResult,"winTeamId":winTeamId},
            		success : function(data) {
            			if (data.status=="000") {
            				//页面重现加载
            				window.location.reload();
            			}else{            				
            				alert("请选择获胜球队！");
            			}
            		}
            	});
            	
            }else{
            	alert("请选择获胜球队！");
            }
        }
    })
}