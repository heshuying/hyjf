var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 翻页功能
	adviceSearch : function(page) {
		$("#page").val(page);
		$("#myForm").submit();
	},
	
	// 翻页功能
	infoDate : function(id) {
		$("#id").val(id);
		$("#myForm").submit();
	}
},

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
Page = {
};

// 入口函数
~function() {
}();
