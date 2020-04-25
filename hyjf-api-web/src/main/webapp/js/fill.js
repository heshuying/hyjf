;~function($) {
	// jQuery扩展
	$.extend({
		isString: function(v) {
			return $.type(v) === "string"
		},
		isDate: function(v) {
			return $.type(v) === "date"
		},
		// SID counter
		sid: (function(id) {
			return id = new Date().getTime() & 0xffff, function() {
				return id++
			}
		})(),
		// 在目标字符串左边用指定的字符将字符串垫补到指定的长度。
		fmtPadleft: function(str, len, symbol) {
			return $.isNumeric(str) && (str += ""),
				$.isString(str) ? ((len = ($.isNumeric(len) ? len : 2) - str.length) > 0 ?
					new Array(++len).join(symbol || "0") + str : str) : "";
		},
		// 数字千分位
		fmtThousand: function(number) {
			return number && (
				number < 1000 ? number : (
						number + "").split("").reverse().join("")
							.match(/(^\d+\.)?\d{1,3}[+-]?/g).join().split("").reverse().join(""));
		},
		// 格式化日期对象
		fmtDate: function(arg, pattern, fn) {
			return fn = $.fmtPadleft,
				(pattern || "yyyy-MM-dd hh:mm:ss")
					.replace(/yyyy/gi,	arg.getFullYear())
					.replace(/MM/g , fn(arg.getMonth() + 1 ))
					.replace(/dd/gi, fn(arg.getDate()))
					.replace(/hh/gi, fn(arg.getHours() ))
					.replace(/mm/g , fn(arg.getMinutes()))
					.replace(/ss/g , fn(arg.getSeconds()))
					.replace(/SSS/g, fn(arg.getMilliseconds(), 3));
		},
		// doT调用接口
		tmpl: function(template) {
			return window.doT &&
				doT.template(template).apply(null, $.makeArray(arguments).slice(1));
		},
		// 通过ajax请求结果进行模板填充
		fillTmplByAjax: function(action, params, panel, tmpl, fillParams, fn) {
			action && $.get(action, params, function(rs) {
				rs.status == 0 && (
					tmpl ?
						$(panel).doT(tmpl, $.extend(rs || {}, fillParams)) :
						panel = panel && $(panel).html(rs || ""),
					fn && fn.call(panel, rs));
			}, "json").error(function(e) {
				window.console && window.console.info("fill error!");
			}) 
		}
	}).fn.extend({
		doT: function(tmpl) {
			return tmpl = $(tmpl).html(),
				$(this).html($.tmpl.apply(null, [tmpl].concat($.makeArray(arguments).slice(1))));
		}
	});

}(jQuery);
