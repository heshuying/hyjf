
var ChinaPnr = function(url) {
    //构造函数
    return ChinaPnr.prototype.init(url);
};

ChinaPnr.prototype = {
    //原型
    init:function(url){
        this.payurl = url;
        this.pageType = "1";
        return this;
    },
    // 接口URL
    payurl:"",
	// 汇付天下URL
	chinapnrurl:"",
    // 页面调用方式(1:后台获取html,2:直接提交form到汇付天下)
    pageType:"",
    // 调用接口(后台)
    callApiajax : function(param, callback) {
		$.ajax({
			url : this.payurl + "/callapiajax",
			type : "POST",
			async : true,
			data : JSON.stringify(param),
			dataType: "json",
			contentType : "application/json",
			success : function(result) {
				callback(result);
			},
			error : function(err) {
				callback("");
			}
		});
	},
	// 调用接口(页面)
	callApi: function(param,chinapnrurl, callback) {
		var form = $("<form target='_blank' style='margin:0;padding:0' method='post'></form>");
		
		for (key in param) {
			var input = "<input name='"+key+"' type='hidden' value='"+param[key]+"'>";
			form.append(input);
		}
		console.info(form.html());
		// 后台获取html
		if (this.pageType == "1") {
			form.attr("action", this.payurl + "/callapi");
			console.info( this.payurl + "/callapi");
			form.appendTo("body");
			form.submit();	
			
		// 直接提交form到汇付天下
		} else {
			form.attr("action", chinapnrurl);
			console.info(chinapnrurl);
			$.ajax({
				url : this.payurl + "/getChkValue",
				type : "POST",
				async : true,
				data : JSON.stringify(param),
				dataType: "json",
				contentType : "application/json",
				success : function(result) {
				
					if(result == "" || result == null || result.ChkValue == "") {
						callback("");
					}
					var input = "<input name='ChkValue' type='hidden' value='"+result.ChkValue+"'>";
					form.append(input);
					console.info(form.html());
					form.appendTo("body");
					form.submit();
				},
				error : function(err) {
					callback("");
				}
			});
		}
	}
}
