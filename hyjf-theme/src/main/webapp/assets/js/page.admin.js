/**!
 * FileName: page.js
 *
 * All Pages
 * Copyright (c) 2012-2015 Benben, http://www.gogtz.com
 *
 * Tested in Chrome10+ & Safari4+.
 * Any browser strangeness, please report, E-mail: jsrookie@qq.com.
 *
 * Date: Fri Mar 2 10:34:31 2015 +800
 */

// 兼容window.console

INST_CODE_HYJF		= "10000000";

console = window.console || {},
$.each([
	'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
	'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
	'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
	'timeline', 'timelineEnd', 'timeStamp', 'trace', 'warn'
], function(idx, method) {
	console[method] = console[method] || function() { }
});


;~function(win, dmt, $, root, wrap, _MediaQuery, _Events, swal, udf) {
	'use strict'
	//----------------------------------------------------------------------------------------------------------------------------
	/* 功能函数 */
	function isSmallDevice() {							// 小屏幕设备检测
		return $(win).width() < _MediaQuery.desktop
	}
	
	function isMobile() {								// 移动设备检测
		return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)
	}

	//----------------------------------------------------------------------------------------------------------------------------
	/* 画面设置 */
	function settingHandler() {
		var settingKey = "gration-page-setting",
			app = $("#app"),
			defaults = {
				"app-navbar-fixed": true, "app-footer-fixed": false, "app-sidebar-fixed": true,
				"app-sidebar-closed": false
			},
			setting, tmp;
		// 读取Cookie
		$.cookie && (setting = (tmp = $.cookie(settingKey)) ? $.parseJSON(tmp) : defaults),
		
		$.each(setting, function(name, value) {
			app[value ? "addClass" : "removeClass"](name),
			// 事件绑定
			$("#" + name.slice(4)).prop("checked", value).change(function() {
				tmp = setting[name] = $(this).is(":checked"),
				app[tmp ? "addClass" : "removeClass"](name),
				$.cookie(settingKey, JSON.stringify(setting))
			})
		})
	}
	
	/* 画面Toggle按钮触发 */
	function initToggleClass(toggleEl) {
		toggleEl = $("*[data-toggle-class]").each(function() {
			var t = $(this),
				tCls = t.attr("data-toggle-class"),
				tmp = t.attr("data-toggle-target"),
				tEl = tmp !== udf ? $(tmp) : t,
				oEl, fn;
			// 事件绑定
			t.click(function(event, type) {
				type = t.attr("data-toggle-type"),
				tEl[type === "on" ? "addClass" : type === "off" ? "removeClass" : "toggleClass"](tCls),
				event.preventDefault(),
				tmp = t.attr("data-toggle-click-outside"),
				tmp && (oEl = $(tmp),
					fn || $(document).on("mousedown touchstart", fn = function(event, el) {
						// checks if descendants of $box was clicked
						oEl.has(el = event.target)[0] ||
						//checks if the $box itself was clicked
						oEl.is(el) || (
							toggleEl.is(el) || !tEl.hasClass(tCls) || tEl.removeClass(tCls),
							// 释放触发后的关闭监听事件
							fn = !$(dmt).off("mousedown touchstart", fn))
					}))
			})
		})
	}
	
	/* 功能菜单项 */
	function initNavMenu(sidebar, tmp) {
		sidebar = $("#sidebar"),
		// 事件绑定
		$("#sidebar a").click(function(event, t) {
			// Click Action
			return t = $(this),
				!(isSidebarClosed() && !isSmallDevice() && !t.closest("ul").hasClass("sub-menu")) && (
					// Collapse Other submenu
					t.closest("ul").find(".open:not(.active)")
						.children("ul").not(t.next()).slideUp(200).parent(".open").removeClass("open"),
					// Expand the menu panel
					tmp = t.next(),
					tmp.is("ul") && t.parent().toggleClass("open") && (
						tmp.slideToggle(200, function() {
							$(win).trigger("resize")
						}),
						event.stopPropagation(),
						event.preventDefault())), 1
		})[isTouch() ? "click" : "mouseenter"](function(t, menuTitle, offset, top, ul) {
			// Hover Action - IN
			return (!isSidebarClosed() || isSmallDevice()) || (
				t = $(this),
				// No submenu Item of non-focus
				!t.parent().hasClass("hover") && !t.closest("ul").hasClass("sub-menu") && (
					wrapLeave(),
					// set status
					t.parent().addClass("hover"),
					// menu tile
					menuTitle = t.find(".item-inner").clone(),
					t.parent().hasClass("active") && menuTitle.addClass("active"),
					// show menu panel
					offset = sidebar.position().top,
					top = t.parent().position().top,
					menuTitle.css({
						position: isSidebarFixed() ? "fixed" : "absolute",
						height: t.outerHeight(),
						top: isSidebarFixed() ? top + offset : top
					}).appendTo(wrap),
					// sub-menu
					tmp = t.next(), tmp.is("ul") && (
						// Copy the sub-menu and show
						ul = tmp.clone(true).appendTo(wrap).css({
							top: menuTitle.position().top + t.outerHeight(),
							position: isSidebarFixed() ? "fixed" : "absolute",
							bottom: top + t.outerHeight() + offset + tmp.height() > $(win).height() &&
								isSidebarFixed() ? 0 : "auto"
						}),
						wrap.children().first().scroll(function() {
							isSidebarFixed() && wrapLeave()
						}),
						setTimeout(function() {
							wrap.is(':empty') || $(dmt).on('click tap', wrapLeave)
						}, 300)		)), 1)
		}),
		
		wrap.mouseleave(function() {
			// Hover Action - OUT
			$(document).off("click tap", wrapLeave),
			$(".hover", wrap).removeClass("hover"),
			$("> .item-inner, > ul", wrap).remove();
		})
	}
	
	function isTouch() {							// Touch事件检测
		return root.hasClass("touch");
	}
	function isSidebarClosed() {					// 功能菜单关闭检测
		return $(".app-sidebar-closed")[0];
	}
	function isSidebarFixed() {						// 功能菜单悬浮固定检测
		return $('.app-sidebar-fixed')[0];
	}
	function wrapLeave() {							// 关闭功能菜单项
		wrap.trigger("mouseleave");
	}
	
	/* Navbar collapse */
	var nbEvtReady;
	function navbarHandler(clpBtn, bar) {
		clpBtn = $('#menu-toggler'),
		bar = $(".navbar-collapse > .nav")
			.css("height", isSmallDevice() ? $(win).innerHeight() - $("header").outerHeight() : "auto"),
		nbEvtReady || $(dmt).on("mousedown touchstart", nbEvtReady = toggleNavbar)
		function toggleNavbar(event) {
			//checks if descendants of $box was clicked
			!bar.has(event.target)[0] &&
			//checks if the $box itself was clicked
			!bar.is(event.target) &&
					bar.parent().hasClass("collapse in") && clpBtn.trigger("click")
		}
	}
	
	/* Search form */
	function searchHandler() {
		var elem = $(".search-form"),
			searchForm = elem.children("form"),
			formWrap = elem.parent();
		
		$(".s-open").click(function(event) {
			searchForm.prependTo(wrap),
			event.preventDefault(),
			$(document).on("mousedown touchstart", closeForm);
		}),
		$(".s-remove").click(function(event) {
			searchForm.appendTo(elem),
			event.preventDefault();
		});
		function closeForm(event, el) {
			!searchForm.is(el = event.target) && !searchForm.has(el)[0] && (
				$(".s-remove").trigger('click'),
				$(dmt).off("mousedown touchstart", closeForm));
		}
	}
	
	/* 页面部件 */
	function initPageWidget(tmp) {
		// tooltips
		$('[data-toggle="tooltip"]').tooltip(),
		// popovers
		$('[data-toggle="popover"]').popover(),
		// perfect scrollbar
		tmp = $(".perfect-scrollbar"),
		isMobile() || tmp[0] && tmp.perfectScrollbar({
				suppressScrollX: true
			}).mousemove(function() {
				$(this).perfectScrollbar('update')
			}),
		// Switchery
		$(".js-switch").each(function() {
			new Switchery(this)
		}),
		// SelectFx
		$("select.cs-select").each(function() {
			new SelectFx(this)
		})
	}
	
	/* 页面部件事件 */
	function initWidgetEvent(tmp) {
		// function to allow a button or a link to open a tab
		/*tmp = $(".show-tab"),
		tmp[0] && tmp.click(function(event, tabToShow) {
			event.preventDefault(),
			$(tabToShow = $(this).attr("href"))[0] && $('a[href="' + tabToShow + '"]').tab("show")
		}),*/
		// function to enable panel scroll with perfectScrollbar
		tmp = $(".panel-scroll"),
		isMobile() || tmp[0] && tmp.perfectScrollbar({
				suppressScrollX: true
			})
		// function to activate the panel tools
		// panel close
		$("body").on("click", ".panel-close", function(event) {
			var panel = $(this).closest('.panel'),
				col = panel.parent();
			
			panel.fadeOut(300, function() {
				$(this).remove(),
				col.is('[class*="col-"]') && !col.children('*')[0] && col.remove();
			}),
			event.preventDefault()
		}),
		// panel refresh
		/*$("body").on("click", ".panel-refresh", function(event) {
			var t = $(this), 
				panel = t.parents(".panel").eq(0), 
				csspinnerClass = "csspinner";
			
			panel.addClass(csspinnerClass + " " + (t.data("spinner") || "load1")),
			setTimeout(function() {
				panel.removeClass(csspinnerClass)
			}, 5000),
			event.preventDefault()
		}),*/
		// panel collapse
		$("body").on("click", ".panel-collapse", function(event, panel) {
			collapse.preventDefault(),
			panel = $(this).closest(".panel"),
			panel.children(".panel-body").slideToggle(200, function() {
				panel.toggleClass("collapses")
			})
		}),
		// function to activate the Go-Top button
		$('.go-top').click(function(event) {
            $("html, body").animate({ scrollTop: 0 }, "slow"),
            event.preventDefault()
        }),
        // 锁屏事件
        $(document).keydown(function(event) {
    		if(lockLayer.is(":visible")) {
    			unlockAct.apply(lockPWD[0], arguments);
    		} else {
    			// Ctrl+L
    			return !event.ctrlKey || event.keyCode != 76 || !lockLayer.fadeIn(function() {
    					lockPWD.focus();
    				})
    		}// Endif
    	}),
    	$("#lockscreenBtn").click(function() {
        	return $(this).closest("li.open").removeClass("open"),
	        	!lockLayer.fadeIn(function() {
					lockPWD.focus();
				})
        }),
        $("#unlockBtn").click(function(event) {
        	event.keyCode = 13,
        	unlockAct.apply(lockPWD[0], arguments)
        }),
        lockPWD.keyup(unlockAct)
	}
	
	// 锁屏遮蔽层
	var lockLayer = $(".lockscreen"),
		lockPWD	  = $(".unlockPWD");
	
	// 解锁动作事件
	function unlockAct(event, pwd) {
		// ajax解锁
		event.keyCode == 13 && (
			pwd = $(this),
			pwd.val() && lockLayer.fadeOut(function() {
				pwd.val("")
			}));
	}
	
	
	
	//----------------------------------------------------------------------------------------------------------------------------
	/* 画面对象 */
	win.Page = {
		debug: true,
		// 线程集合
		handles: {},
		// 画面调试
		out: function() {
			Page.debug && console.debug.apply(console, arguments);
		},
		// 页面主表单
		form: $("#mainForm"),
		// 提交默认表单
		submit: function(action, msg, form, target) {
			form = form && $(form) || Page.form,
			form[0] && (
					Page.coverLayer(msg || "正在操作数据，请稍候..."),
					target && form.attr("target", target),
					action && form.attr("action", action), form.submit());
		},
		// 画面默认的Alert消息
		alert: (parent.Page || {}).alert || function() {
			swal = win.swal,
			(swal || alert).apply(null, swal ? arguments : [arguments[1]]);
		},
		// 画面默认的Confirm确认
		confirm: (parent.Page || {}).confirm || function(title, text, type, options, callback, tmp) {
			callback = $.isFunction(tmp = callback || options || type) ? tmp : function() {},
			options = $.isPlainObject(tmp = options || type) ? tmp : {
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				showCancelButton: true
			},
			type = type && type.charAt ? type : "warning",
			swal = win.swal,
			swal ? swal($.extend({
					title: title || "", text: text, type: type
				}, options), callback) : callback(confirm(text))
		},
		// 画面通知信息
		notice: (parent.Page || {}).notice || $.extend(function(msg, title, type, options) {
				win.toastr && (
					options && $.extend(toastr.options, options),
					toastr[type || "info"](msg, title))
			}, {
				clear: function() {
					win.toastr && toastr.clear()
				}
			}),
		// 显示隐藏画面遮蔽层
		coverLayer: (parent.Page || {}).coverLayer || function(msg) {
			// 画面遮蔽层
			win.s_layer || (win.s_info = $(".s-cover-info div",
				win.s_layer = $('<div class=s-cl-wrap><div class=s-cover-layer/><div class=s-cover-info><div/></div></div>')
					.hide().appendTo("body").mousedown(function() { return false }))),
				msg ? (
					win.s_info.text(msg || "\u8bf7\u7a0d\u540e..."), win.s_layer.show()) :
					win.s_layer.hide();
		},
		// 画面布局
		_doLayout: function() {
			// 设置面板
			settingHandler(),
			// 页面部件
			initPageWidget(),
			// 菜单项父级的active状态设置
			$("#sidebar .active").parents("ul.sub-menu").show()
					.parent().addClass("open").first().addClass("active"),
			// 页面布局初始化
			this.doLayout && this.doLayout(),
			// 页面布局尺寸调整
			_Events.resizeAct(),
			// 兼容IE8-中文本域的placeholder功能
			$.fn.placeholder && $('input, textarea').placeholder()
		},
		// 初始化画面事件处理
		_initEvents: function() {
			// 画面调整事件
			$(window).resize(function(handles) {
				handles = Page.handles,
				handles.resize && clearTimeout(handles.resize),
				handles.resize = setTimeout(function() {
					navbarHandler(),
					_Events.resizeAct();
				}, 100);
			}),
			// 按钮触发事件
			initToggleClass(),
			// 功能菜单事件
			initNavMenu(),
			// 检索栏
			searchHandler(),
			// 页面部件事件
			initWidgetEvent(),
			// 页面事件动作初始化
			this.initEvents && this.initEvents()
		},
		// 手动调用页面初始化接口(Page.auto == false)
		init: function() {
			return Page._doLayout(), Page._initEvents(), this
		}
	}
	Page.coverLayer("正在加载页面，请稍候..."),

	// 入口函数
	$(function() {
		Page.auto !== false && (
			Page.init().ready && Page.ready(), Page.coverLayer());
	})
	
}(window, document, jQuery, jQuery("html"), jQuery(".app-aside"), {
	// 设备屏幕标准尺寸
	desktopXL: 1200,
	desktop: 992,
	tablet: 768,
	mobile: 480
}, {
	// 画面尺寸调整动作事件
	resizeAct: function(event) {
		// 页面事件动作初始化
		window.Events && Events.resizeAct && Events.resizeAct.apply(this, arguments);
	}
})

/*清空表单内容的所有内容开始 */

$(".fn-ClearForm").click(function(){
//	$(":text,.form-select2", "#searchable-panel").val("").filter(".form-select2").trigger('change');
	$(":text,.form-select2","#searchable-panel-clone").val("").filter(".form-select2").trigger('change');
})

/*清空表单内容的所有内容结束*/
//多选框全选状态设置
$(".checkbox input:checkbox").change(function(){
	var ipt = $(this);
	var group = ipt.parent().parent();
	var all = group.find(".checkbox input:checkbox[value=all]");
	var other = group.find(".checkbox input:checkbox[value!=all]");
	if(other.not(":checked").length>0){
		all.prop("checked",false);
	}else if(other.not(":checked").length == 0){
		all.prop("checked",true);
	}
})
