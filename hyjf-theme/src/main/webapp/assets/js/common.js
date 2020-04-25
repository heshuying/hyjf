/**!
 * FileName: common.js
 * 
 * Copyright (c) 2015-2016 Benben, http://www.gogtz.com
 * 
 * Tested in Chrome10+ & Safari4+.
 * Any browser strangeness, please report, E-mail: jsrookie@qq.com.
 * 
 * Date: Tue Nov 17 10:34:31 2015 +800
 */

;~function(win, dmt, $, udf) {
	
var
// Constant_String
HI_STR_EMPTY	 = "",
HI_STR_DELIMITER = "@@",

// Constant_RegExp
HI_REG_BITLEN	= /[^\x00-\xff]/g,

// Constant_Other
HI_TRUE		= !0,
HI_FALSE	= !1,
HI_NULL		= null;


// 解析参数字符串
function parseParams(param, rs) {
	return rs = {},
		$.isString(param) && param.replace(/([^=?]+)=([^&]*)&?/g, function(a, b, c) {
			c = decodeURIComponent(c),
			a = rs[b],
			b in rs ? ($.isArray(a) || (
				rs[b] = [a]), rs[b].push(c)) : rs[b] = c;
		}), rs;
}

// 函数拦截器
// Note: 为函数Fn创建拦截函数后，执行函数Fn，如果返回值为(-224)，说明此前的调用已被拦截
function interceptor(fn, flag, scope) {
	var t = this;
	return  $.isFunction(fn) ? function() {
		return scope = scope || t, flag ? fn.call(scope,
			t.apply(scope, arguments)) : fn.apply(scope,
				arguments) === HI_FALSE ? -0xe0 : t.apply(scope, arguments);
	} : t;
}


/* jQuery extensions */
$.extend($.extend($, {
	hippo: "2.0.150519b",
	isString: function(v) {
		return $.type(v) === "string";
	},
	isDate: function(v) {
		return $.type(v) === "date";
	},
	// SID counter
	sid: (function(id) {
		return id = new Date().getTime() & 0xffff, function() {
			return id++;
		};
	})(),
	// Function interceptor
	interceptor: interceptor,
	// Timer function
	timer: function(fn, delay) {
		return delay = setTimeout(fn, delay), function() {
			return delay = clearTimeout(delay), this;
		}
	},
	// Listener function
	listener: function(check, fn, interval, handleObj) {
		return handleObj = {
				stop: function() { }
			},
			~function() {
				check() ? fn() :
					handleObj.stop = $.timer(arguments.callee, interval);
			}(), handleObj;
	},
	// 为目标函数创建一个循环函数
	loop: function(fn, interval, step, delay, handleObj) {
		// call handler
		return $.isFunction(fn) && (
			handleObj = {
				stop: function() { }
			},
			delay = typeof delay == "boolean" ||
				typeof(delay = arguments[arguments.length - 1]) == "boolean" ? delay : HI_FALSE,
			// loop functions
			function() {
				var t = this, args = arguments,
					ti = $.isNumeric(interval) ? interval : 160,
					st = $.isNumeric(step) ? step : 20,
					flag = delay;
				~function() {
					!flag && fn.apply(t, args) === HI_FALSE || (
						handleObj.stop = apply.timer(arguments.callee, ti),
						st &&  ti > st && (ti -= st)),
						flag = HI_FALSE;
				}()
				return handleObj;
			});
	},
	// 检查浏览器是否支持HTML5
	checkHTML5: function() {
		return win.Worker !== udf;
	},
	// 满屏显示
	fullScreen: function(full, de, obj) {
		if(arguments.length) {
			try {
				de = dmt.documentElement,
				obj = full ?
					de.requestFullScreen || de.webkitRequestFullScreen || de.mozRequestFullScreen || de.msRequestFullScreen :
					dmt.exitFullscreen || dmt.mozCancelFullScreen || dmt.webkitCancelFullScreen || dmt.msExitFullscreen,
				obj != udf && obj ? obj.call(full ? de : dmt) :
					win.ActiveXObject != udf && (
						obj = new ActiveXObject("WScript.Shell"), obj && obj.SendKeys(full ? "{F11}" : "{ESC}"));
			} catch(e) {
			}// Endtry
		} else {
			return screen.height === $(win).height();
		}// Endif
	},
	// 取得指定浏览器当前URL中指定参数名称的所有值
	getParams: function(name, rs) {
		return rs = parseParams($.isString(rs) ?
			rs : (rs || win).location.search), name ? rs[name] : rs;
	},
	// 返回字符串的单节长度
	bytelen: function(str) {
		return $.isString(str) ? str
			.replace(HI_REG_BITLEN, HI_STR_DELIMITER).length : 0;
	},
	// 在目标字符串左边用指定的字符将字符串垫补到指定的长度。
	padLeft: function(str, len, symbol) {
		return $.isNumeric(str) && (str += HI_STR_EMPTY),
			$.isString(str) ? ((len = ($.isNumeric(len) ? len : 2) - str.length) > 0 ?
				new Array(++len).join(symbol || "0") + str : str) :
				HI_STR_EMPTY;
	},
	fmtThousand: function(number) {
		return number && (
			number < 1000 ? number : (
					number + "").split("").reverse().join("")
						.match(/(^\d+\.)?\d{1,3}[+-]?/g).join().split("").reverse().join(""));
	},
	// 格式化日期对象
	dateFormat: function(arg, pattern, fn) {
		return fn = $.padLeft,
			(pattern || "yyyy-MM-dd hh:mm:ss")
				.replace(/yyyy/gi,	arg.getFullYear())
				.replace(/MM/g , fn(arg.getMonth() + 1 ))
				.replace(/dd/gi, fn(arg.getDate()))
				.replace(/hh/gi, fn(arg.getHours() ))
				.replace(/mm/g , fn(arg.getMinutes()))
				.replace(/ss/g , fn(arg.getSeconds()))
				.replace(/SSS/g, fn(arg.getMilliseconds(), 3));
	},
	// JSON to String
	strify: win.JSON && JSON.stringify || function(arg, rs, item) {
		rs = ["{"];
		for(var i in arg) {
			(item = arg[i]) == null || (rs.push('"', i, '":'),
					$.isArray(item) ? rs.push("[", item.join('","'), "]") :
					$.isNumeric(item) ? rs.push(item) :
					$.isString(item) && rs.push('"', item, '"'),
				rs.push(","));
		}//Endfor
		return rs.length > 1 && rs.pop(), rs.push("}"), rs.join('');
	},
	// Convert UTF-8 character
	toUTF8: function(str) {
		var out = "",
			len = str.length,
			fcc = String.fromCharCode,
			i, c;
		for (i = 0; i < len; i++) {
			c = str.charCodeAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				out += str.charAt(i);
			} else if (c > 0x07FF) {
				out += fcc(0xE0 | ((c >> 12) & 0x0F)),
				out += fcc(0x80 | ((c >> 6) & 0x3F)),
				out += fcc(0x80 | ((c >> 0) & 0x3F));
			} else {
				out += fcc(0xC0 | ((c >> 6) & 0x1F)),
				out += fcc(0x80 | ((c >> 0) & 0x3F));
			}
		}
		return out;
	},
	// 返回目标元素是否存在于浮动的元素中
	inPosi: function(el) {
		return $(el).offsetParent()[0] !== dmt.documentElement;
	},
	// 返回目标元素是否为焦点元素
	isFocus: function(el) {
		return dmt.activeElement === $(el)[0];
	}
}).fn, {
	// 旋转Dom元素
	transform: function(fn, angle) {
		
	}
})
	
	
}(window, document, jQuery)
