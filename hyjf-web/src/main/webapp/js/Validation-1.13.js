/*! jQuery Validation Plugin - v1.13.1 - 10/14/2014
 * http://jqueryvalidation.org/
 * Copyright (c) 2014 Jörn Zaefferer; Licensed MIT */
!function(a){"function"==typeof define&&define.amd?define(["jquery"],a):a(jQuery)}(function(a){a.extend(a.fn,{validate:function(b){if(!this.length)return void(b&&b.debug&&window.console&&console.warn("Nothing selected, can't validate, returning nothing."));var c=a.data(this[0],"validator");return c?c:(this.attr("novalidate","novalidate"),c=new a.validator(b,this[0]),a.data(this[0],"validator",c),c.settings.onsubmit&&(this.validateDelegate(":submit","click",function(b){c.settings.submitHandler&&(c.submitButton=b.target),a(b.target).hasClass("cancel")&&(c.cancelSubmit=!0),void 0!==a(b.target).attr("formnovalidate")&&(c.cancelSubmit=!0)}),this.submit(function(b){function d(){var d;return c.settings.submitHandler?(c.submitButton&&(d=a("<input type='hidden'/>").attr("name",c.submitButton.name).val(a(c.submitButton).val()).appendTo(c.currentForm)),c.settings.submitHandler.call(c,c.currentForm,b),c.submitButton&&d.remove(),!1):!0}return c.settings.debug&&b.preventDefault(),c.cancelSubmit?(c.cancelSubmit=!1,d()):c.form()?c.pendingRequest?(c.formSubmitted=!0,!1):d():(c.focusInvalid(),!1)})),c)},valid:function(){var b,c;return a(this[0]).is("form")?b=this.validate().form():(b=!0,c=a(this[0].form).validate(),this.each(function(){b=c.element(this)&&b})),b},removeAttrs:function(b){var c={},d=this;return a.each(b.split(/\s/),function(a,b){c[b]=d.attr(b),d.removeAttr(b)}),c},rules:function(b,c){var d,e,f,g,h,i,j=this[0];if(b)switch(d=a.data(j.form,"validator").settings,e=d.rules,f=a.validator.staticRules(j),b){case"add":a.extend(f,a.validator.normalizeRule(c)),delete f.messages,e[j.name]=f,c.messages&&(d.messages[j.name]=a.extend(d.messages[j.name],c.messages));break;case"remove":return c?(i={},a.each(c.split(/\s/),function(b,c){i[c]=f[c],delete f[c],"required"===c&&a(j).removeAttr("aria-required")}),i):(delete e[j.name],f)}return g=a.validator.normalizeRules(a.extend({},a.validator.classRules(j),a.validator.attributeRules(j),a.validator.dataRules(j),a.validator.staticRules(j)),j),g.required&&(h=g.required,delete g.required,g=a.extend({required:h},g),a(j).attr("aria-required","true")),g.remote&&(h=g.remote,delete g.remote,g=a.extend(g,{remote:h})),g}}),a.extend(a.expr[":"],{blank:function(b){return!a.trim(""+a(b).val())},filled:function(b){return!!a.trim(""+a(b).val())},unchecked:function(b){return!a(b).prop("checked")}}),a.validator=function(b,c){this.settings=a.extend(!0,{},a.validator.defaults,b),this.currentForm=c,this.init()},a.validator.format=function(b,c){return 1===arguments.length?function(){var c=a.makeArray(arguments);return c.unshift(b),a.validator.format.apply(this,c)}:(arguments.length>2&&c.constructor!==Array&&(c=a.makeArray(arguments).slice(1)),c.constructor!==Array&&(c=[c]),a.each(c,function(a,c){b=b.replace(new RegExp("\\{"+a+"\\}","g"),function(){return c})}),b)},a.extend(a.validator,{defaults:{messages:{},groups:{},rules:{},errorClass:"error",validClass:"valid",errorElement:"label",focusInvalid:!0,errorContainer:a([]),errorLabelContainer:a([]),onsubmit:!0,ignore:":hidden",ignoreTitle:!1,onfocusin:function(a){this.lastActive=a,this.settings.focusCleanup&&!this.blockFocusCleanup&&(this.settings.unhighlight&&this.settings.unhighlight.call(this,a,this.settings.errorClass,this.settings.validClass),this.hideThese(this.errorsFor(a)))},onfocusout:function(a){this.checkable(a)||!(a.name in this.submitted)&&this.optional(a)||this.element(a)},onkeyup:function(a,b){(9!==b.which||""!==this.elementValue(a))&&(a.name in this.submitted||a===this.lastElement)&&this.element(a)},onclick:function(a){a.name in this.submitted?this.element(a):a.parentNode.name in this.submitted&&this.element(a.parentNode)},highlight:function(b,c,d){"radio"===b.type?this.findByName(b.name).addClass(c).removeClass(d):a(b).addClass(c).removeClass(d)},unhighlight:function(b,c,d){"radio"===b.type?this.findByName(b.name).removeClass(c).addClass(d):a(b).removeClass(c).addClass(d)}},setDefaults:function(b){a.extend(a.validator.defaults,b)},messages:{required:"This field is required.",remote:"Please fix this field.",email:"请输入有效的Email地址.",url:"Please enter a valid URL.",date:"Please enter a valid date.",dateISO:"Please enter a valid date ( ISO ).",number:"Please enter a valid number.",digits:"Please enter only digits.",creditcard:"Please enter a valid credit card number.",equalTo:"Please enter the same value again.",maxlength:a.validator.format("Please enter no more than {0} characters."),minlength:a.validator.format("请输入不少于 {0} 位的字符."),rangelength:a.validator.format("Please enter a value between {0} and {1} characters long."),range:a.validator.format("Please enter a value between {0} and {1}."),max:a.validator.format("Please enter a value less than or equal to {0}."),min:a.validator.format("Please enter a value greater than or equal to {0}.")},autoCreateRanges:!1,prototype:{init:function(){function b(b){var c=a.data(this[0].form,"validator"),d="on"+b.type.replace(/^validate/,""),e=c.settings;e[d]&&!this.is(e.ignore)&&e[d].call(c,this[0],b)}this.labelContainer=a(this.settings.errorLabelContainer),this.errorContext=this.labelContainer.length&&this.labelContainer||a(this.currentForm),this.containers=a(this.settings.errorContainer).add(this.settings.errorLabelContainer),this.submitted={},this.valueCache={},this.pendingRequest=0,this.pending={},this.invalid={},this.reset();var c,d=this.groups={};a.each(this.settings.groups,function(b,c){"string"==typeof c&&(c=c.split(/\s/)),a.each(c,function(a,c){d[c]=b})}),c=this.settings.rules,a.each(c,function(b,d){c[b]=a.validator.normalizeRule(d)}),a(this.currentForm).validateDelegate(":text, [type='password'], [type='file'], select, textarea, [type='number'], [type='search'] ,[type='tel'], [type='url'], [type='email'], [type='datetime'], [type='date'], [type='month'], [type='week'], [type='time'], [type='datetime-local'], [type='range'], [type='color'], [type='radio'], [type='checkbox']","focusin focusout keyup",b).validateDelegate("select, option, [type='radio'], [type='checkbox']","click",b),this.settings.invalidHandler&&a(this.currentForm).bind("invalid-form.validate",this.settings.invalidHandler),a(this.currentForm).find("[required], [data-rule-required], .required").attr("aria-required","true")},form:function(){return this.checkForm(),a.extend(this.submitted,this.errorMap),this.invalid=a.extend({},this.errorMap),this.valid()||a(this.currentForm).triggerHandler("invalid-form",[this]),this.showErrors(),this.valid()},checkForm:function(){this.prepareForm();for(var a=0,b=this.currentElements=this.elements();b[a];a++)this.check(b[a]);return this.valid()},element:function(b){var c=this.clean(b),d=this.validationTargetFor(c),e=!0;return this.lastElement=d,void 0===d?delete this.invalid[c.name]:(this.prepareElement(d),this.currentElements=a(d),e=this.check(d)!==!1,e?delete this.invalid[d.name]:this.invalid[d.name]=!0),a(b).attr("aria-invalid",!e),this.numberOfInvalids()||(this.toHide=this.toHide.add(this.containers)),this.showErrors(),e},showErrors:function(b){if(b){a.extend(this.errorMap,b),this.errorList=[];for(var c in b)this.errorList.push({message:b[c],element:this.findByName(c)[0]});this.successList=a.grep(this.successList,function(a){return!(a.name in b)})}this.settings.showErrors?this.settings.showErrors.call(this,this.errorMap,this.errorList):this.defaultShowErrors()},resetForm:function(){a.fn.resetForm&&a(this.currentForm).resetForm(),this.submitted={},this.lastElement=null,this.prepareForm(),this.hideErrors(),this.elements().removeClass(this.settings.errorClass).removeData("previousValue").removeAttr("aria-invalid")},numberOfInvalids:function(){return this.objectLength(this.invalid)},objectLength:function(a){var b,c=0;for(b in a)c++;return c},hideErrors:function(){this.hideThese(this.toHide)},hideThese:function(a){a.not(this.containers).text(""),this.addWrapper(a).hide()},valid:function(){return 0===this.size()},size:function(){return this.errorList.length},focusInvalid:function(){if(this.settings.focusInvalid)try{a(this.findLastActive()||this.errorList.length&&this.errorList[0].element||[]).filter(":visible").focus().trigger("focusin")}catch(b){}},findLastActive:function(){var b=this.lastActive;return b&&1===a.grep(this.errorList,function(a){return a.element.name===b.name}).length&&b},elements:function(){var b=this,c={};return a(this.currentForm).find("input, select, textarea").not(":submit, :reset, :image, [disabled]").not(this.settings.ignore).filter(function(){return!this.name&&b.settings.debug&&window.console&&console.error("%o has no name assigned",this),this.name in c||!b.objectLength(a(this).rules())?!1:(c[this.name]=!0,!0)})},clean:function(b){return a(b)[0]},errors:function(){var b=this.settings.errorClass.split(" ").join(".");return a(this.settings.errorElement+"."+b,this.errorContext)},reset:function(){this.successList=[],this.errorList=[],this.errorMap={},this.toShow=a([]),this.toHide=a([]),this.currentElements=a([])},prepareForm:function(){this.reset(),this.toHide=this.errors().add(this.containers)},prepareElement:function(a){this.reset(),this.toHide=this.errorsFor(a)},elementValue:function(b){var c,d=a(b),e=b.type;return"radio"===e||"checkbox"===e?a("input[name='"+b.name+"']:checked").val():"number"===e&&"undefined"!=typeof b.validity?b.validity.badInput?!1:d.val():(c=d.val(),"string"==typeof c?c.replace(/\r/g,""):c)},check:function(b){b=this.validationTargetFor(this.clean(b));var c,d,e,f=a(b).rules(),g=a.map(f,function(a,b){return b}).length,h=!1,i=this.elementValue(b);for(d in f){e={method:d,parameters:f[d]};try{if(c=a.validator.methods[d].call(this,i,b,e.parameters),"dependency-mismatch"===c&&1===g){h=!0;continue}if(h=!1,"pending"===c)return void(this.toHide=this.toHide.not(this.errorsFor(b)));if(!c)return this.formatAndAdd(b,e),!1}catch(j){throw this.settings.debug&&window.console&&console.log("Exception occurred when checking element "+b.id+", check the '"+e.method+"' method.",j),j}}if(!h)return this.objectLength(f)&&this.successList.push(b),!0},customDataMessage:function(b,c){return a(b).data("msg"+c.charAt(0).toUpperCase()+c.substring(1).toLowerCase())||a(b).data("msg")},customMessage:function(a,b){var c=this.settings.messages[a];return c&&(c.constructor===String?c:c[b])},findDefined:function(){for(var a=0;a<arguments.length;a++)if(void 0!==arguments[a])return arguments[a];return void 0},defaultMessage:function(b,c){return this.findDefined(this.customMessage(b.name,c),this.customDataMessage(b,c),!this.settings.ignoreTitle&&b.title||void 0,a.validator.messages[c],"<strong>Warning: No message defined for "+b.name+"</strong>")},formatAndAdd:function(b,c){var d=this.defaultMessage(b,c.method),e=/\$?\{(\d+)\}/g;"function"==typeof d?d=d.call(this,c.parameters,b):e.test(d)&&(d=a.validator.format(d.replace(e,"{$1}"),c.parameters)),this.errorList.push({message:d,element:b,method:c.method}),this.errorMap[b.name]=d,this.submitted[b.name]=d},addWrapper:function(a){return this.settings.wrapper&&(a=a.add(a.parent(this.settings.wrapper))),a},defaultShowErrors:function(){var a,b,c;for(a=0;this.errorList[a];a++)c=this.errorList[a],this.settings.highlight&&this.settings.highlight.call(this,c.element,this.settings.errorClass,this.settings.validClass),this.showLabel(c.element,c.message);if(this.errorList.length&&(this.toShow=this.toShow.add(this.containers)),this.settings.success)for(a=0;this.successList[a];a++)this.showLabel(this.successList[a]);if(this.settings.unhighlight)for(a=0,b=this.validElements();b[a];a++)this.settings.unhighlight.call(this,b[a],this.settings.errorClass,this.settings.validClass);this.toHide=this.toHide.not(this.toShow),this.hideErrors(),this.addWrapper(this.toShow).show()},validElements:function(){return this.currentElements.not(this.invalidElements())},invalidElements:function(){return a(this.errorList).map(function(){return this.element})},showLabel:function(b,c){var d,e,f,g=this.errorsFor(b),h=this.idOrName(b),i=a(b).attr("aria-describedby");g.length?(g.removeClass(this.settings.validClass).addClass(this.settings.errorClass),g.html(c)):(g=a("<"+this.settings.errorElement+">").attr("id",h+"-error").addClass(this.settings.errorClass).html(c||""),d=g,this.settings.wrapper&&(d=g.hide().show().wrap("<"+this.settings.wrapper+"/>").parent()),this.labelContainer.length?this.labelContainer.append(d):this.settings.errorPlacement?this.settings.errorPlacement(d,a(b)):d.insertAfter(b),g.is("label")?g.attr("for",h):0===g.parents("label[for='"+h+"']").length&&(f=g.attr("id"),i?i.match(new RegExp("\b"+f+"\b"))||(i+=" "+f):i=f,a(b).attr("aria-describedby",i),e=this.groups[b.name],e&&a.each(this.groups,function(b,c){c===e&&a("[name='"+b+"']",this.currentForm).attr("aria-describedby",g.attr("id"))}))),!c&&this.settings.success&&(g.text(""),"string"==typeof this.settings.success?g.addClass(this.settings.success):this.settings.success(g,b)),this.toShow=this.toShow.add(g)},errorsFor:function(b){var c=this.idOrName(b),d=a(b).attr("aria-describedby"),e="label[for='"+c+"'], label[for='"+c+"'] *";return d&&(e=e+", #"+d.replace(/\s+/g,", #")),this.errors().filter(e)},idOrName:function(a){return this.groups[a.name]||(this.checkable(a)?a.name:a.id||a.name)},validationTargetFor:function(a){return this.checkable(a)&&(a=this.findByName(a.name).not(this.settings.ignore)[0]),a},checkable:function(a){return/radio|checkbox/i.test(a.type)},findByName:function(b){return a(this.currentForm).find("[name='"+b+"']")},getLength:function(b,c){switch(c.nodeName.toLowerCase()){case"select":return a("option:selected",c).length;case"input":if(this.checkable(c))return this.findByName(c.name).filter(":checked").length}return b.length},depend:function(a,b){return this.dependTypes[typeof a]?this.dependTypes[typeof a](a,b):!0},dependTypes:{"boolean":function(a){return a},string:function(b,c){return!!a(b,c.form).length},"function":function(a,b){return a(b)}},optional:function(b){var c=this.elementValue(b);return!a.validator.methods.required.call(this,c,b)&&"dependency-mismatch"},startRequest:function(a){this.pending[a.name]||(this.pendingRequest++,this.pending[a.name]=!0)},stopRequest:function(b,c){this.pendingRequest--,this.pendingRequest<0&&(this.pendingRequest=0),delete this.pending[b.name],c&&0===this.pendingRequest&&this.formSubmitted&&this.form()?(a(this.currentForm).submit(),this.formSubmitted=!1):!c&&0===this.pendingRequest&&this.formSubmitted&&(a(this.currentForm).triggerHandler("invalid-form",[this]),this.formSubmitted=!1)},previousValue:function(b){return a.data(b,"previousValue")||a.data(b,"previousValue",{old:null,valid:!0,message:this.defaultMessage(b,"remote")})}},classRuleSettings:{required:{required:!0},email:{email:!0},url:{url:!0},date:{date:!0},dateISO:{dateISO:!0},number:{number:!0},digits:{digits:!0},creditcard:{creditcard:!0}},addClassRules:function(b,c){b.constructor===String?this.classRuleSettings[b]=c:a.extend(this.classRuleSettings,b)},classRules:function(b){var c={},d=a(b).attr("class");return d&&a.each(d.split(" "),function(){this in a.validator.classRuleSettings&&a.extend(c,a.validator.classRuleSettings[this])}),c},attributeRules:function(b){var c,d,e={},f=a(b),g=b.getAttribute("type");for(c in a.validator.methods)"required"===c?(d=b.getAttribute(c),""===d&&(d=!0),d=!!d):d=f.attr(c),/min|max/.test(c)&&(null===g||/number|range|text/.test(g))&&(d=Number(d)),d||0===d?e[c]=d:g===c&&"range"!==g&&(e[c]=!0);return e.maxlength&&/-1|2147483647|524288/.test(e.maxlength)&&delete e.maxlength,e},dataRules:function(b){var c,d,e={},f=a(b);for(c in a.validator.methods)d=f.data("rule"+c.charAt(0).toUpperCase()+c.substring(1).toLowerCase()),void 0!==d&&(e[c]=d);return e},staticRules:function(b){var c={},d=a.data(b.form,"validator");return d.settings.rules&&(c=a.validator.normalizeRule(d.settings.rules[b.name])||{}),c},normalizeRules:function(b,c){return a.each(b,function(d,e){if(e===!1)return void delete b[d];if(e.param||e.depends){var f=!0;switch(typeof e.depends){case"string":f=!!a(e.depends,c.form).length;break;case"function":f=e.depends.call(c,c)}f?b[d]=void 0!==e.param?e.param:!0:delete b[d]}}),a.each(b,function(d,e){b[d]=a.isFunction(e)?e(c):e}),a.each(["minlength","maxlength"],function(){b[this]&&(b[this]=Number(b[this]))}),a.each(["rangelength","range"],function(){var c;b[this]&&(a.isArray(b[this])?b[this]=[Number(b[this][0]),Number(b[this][1])]:"string"==typeof b[this]&&(c=b[this].replace(/[\[\]]/g,"").split(/[\s,]+/),b[this]=[Number(c[0]),Number(c[1])]))}),a.validator.autoCreateRanges&&(b.min&&b.max&&(b.range=[b.min,b.max],delete b.min,delete b.max),b.minlength&&b.maxlength&&(b.rangelength=[b.minlength,b.maxlength],delete b.minlength,delete b.maxlength)),b},normalizeRule:function(b){if("string"==typeof b){var c={};a.each(b.split(/\s/),function(){c[this]=!0}),b=c}return b},addMethod:function(b,c,d){a.validator.methods[b]=c,a.validator.messages[b]=void 0!==d?d:a.validator.messages[b],c.length<3&&a.validator.addClassRules(b,a.validator.normalizeRule(b))},methods:{required:function(b,c,d){if(!this.depend(d,c))return"dependency-mismatch";if("select"===c.nodeName.toLowerCase()){var e=a(c).val();return e&&e.length>0}return this.checkable(c)?this.getLength(b,c)>0:a.trim(b).length>0},email:function(a,b){return this.optional(b)||/^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test(a)},url:function(a,b){return this.optional(b)||/^(https?|s?ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(a)},date:function(a,b){return this.optional(b)||!/Invalid|NaN/.test(new Date(a).toString())},dateISO:function(a,b){return this.optional(b)||/^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$/.test(a)},number:function(a,b){return this.optional(b)||/^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(a)},digits:function(a,b){return this.optional(b)||/^\d+$/.test(a)},creditcard:function(a,b){if(this.optional(b))return"dependency-mismatch";if(/[^0-9 \-]+/.test(a))return!1;var c,d,e=0,f=0,g=!1;if(a=a.replace(/\D/g,""),a.length<13||a.length>19)return!1;for(c=a.length-1;c>=0;c--)d=a.charAt(c),f=parseInt(d,10),g&&(f*=2)>9&&(f-=9),e+=f,g=!g;return e%10===0},minlength:function(b,c,d){var e=a.isArray(b)?b.length:this.getLength(a.trim(b),c);return this.optional(c)||e>=d},maxlength:function(b,c,d){var e=a.isArray(b)?b.length:this.getLength(a.trim(b),c);return this.optional(c)||d>=e},rangelength:function(b,c,d){var e=a.isArray(b)?b.length:this.getLength(a.trim(b),c);return this.optional(c)||e>=d[0]&&e<=d[1]},min:function(a,b,c){return this.optional(b)||a>=c},max:function(a,b,c){return this.optional(b)||c>=a},range:function(a,b,c){return this.optional(b)||a>=c[0]&&a<=c[1]},equalTo:function(b,c,d){var e=a(d);return this.settings.onfocusout&&e.unbind(".validate-equalTo").bind("blur.validate-equalTo",function(){a(c).valid()}),b===e.val()},remote:function(b,c,d){if(this.optional(c))return"dependency-mismatch";var e,f,g=this.previousValue(c);return this.settings.messages[c.name]||(this.settings.messages[c.name]={}),g.originalMessage=this.settings.messages[c.name].remote,this.settings.messages[c.name].remote=g.message,d="string"==typeof d&&{url:d}||d,g.old===b?g.valid:(g.old=b,e=this,this.startRequest(c),f={},f[c.name]=b,a.ajax(a.extend(!0,{url:d,mode:"abort",port:"validate"+c.name,dataType:"json",data:f,context:e.currentForm,success:function(d){var f,h,i,j=d===!0||"true"===d;e.settings.messages[c.name].remote=g.originalMessage,j?(i=e.formSubmitted,e.prepareElement(c),e.formSubmitted=i,e.successList.push(c),delete e.invalid[c.name],e.showErrors()):(f={},h=d||e.defaultMessage(c,"remote"),f[c.name]=g.message=a.isFunction(h)?h(b):h,e.invalid[c.name]=!0,e.showErrors(f)),g.valid=j,e.stopRequest(c,j)}},d)),"pending")}}}),a.format=function(){throw"$.format has been deprecated. Please use $.validator.format instead."};var b,c={};a.ajaxPrefilter?a.ajaxPrefilter(function(a,b,d){var e=a.port;"abort"===a.mode&&(c[e]&&c[e].abort(),c[e]=d)}):(b=a.ajax,a.ajax=function(d){var e=("mode"in d?d:a.ajaxSettings).mode,f=("port"in d?d:a.ajaxSettings).port;return"abort"===e?(c[f]&&c[f].abort(),c[f]=b.apply(this,arguments),c[f]):b.apply(this,arguments)}),a.extend(a.fn,{validateDelegate:function(b,c,d){return this.bind(c,function(c){var e=a(c.target);return e.is(b)?d.apply(e,arguments):void 0})}})});

/*
** Unobtrusive validation support library for jQuery and jQuery Validate
** Copyright (C) Microsoft Corporation. All rights reserved.
*/
(function(a){var d=a.validator,b,e="unobtrusiveValidation";function c(a,b,c){a.rules[b]=c;if(a.message)a.messages[b]=a.message}function j(a){return a.replace(/^\s+|\s+$/g,"").split(/\s*,\s*/g)}function f(a){return a.replace(/([!"#$%&'()*+,./:;<=>?@\[\\\]^`{|}~])/g,"\\$1")}function h(a){return a.substr(0,a.lastIndexOf(".")+1)}function g(a,b){if(a.indexOf("*.")===0)a=a.replace("*.",b);return a}function m(c,e){var b=a(this).find("[data-valmsg-for='"+f(e[0].name)+"']"),d=b.attr("data-valmsg-replace"),g=d?a.parseJSON(d)!==false:null;b.removeClass("field-validation-valid").addClass("field-validation-error");c.data("unobtrusiveContainer",b);if(g){b.empty();c.removeClass("input-validation-error").appendTo(b)}else c.hide()}function l(e,d){var c=a(this).find("[data-valmsg-summary=true]"),b=c.find("ul");if(b&&b.length&&d.errorList.length){b.empty();c.addClass("validation-summary-errors").removeClass("validation-summary-valid");a.each(d.errorList,function(){a("<li />").html(this.message).appendTo(b)})}}function k(d){var b=d.data("unobtrusiveContainer"),c=b.attr("data-valmsg-replace"),e=c?a.parseJSON(c):null;if(b){b.addClass("field-validation-valid").removeClass("field-validation-error");d.removeData("unobtrusiveContainer");e&&b.empty()}}function n(){var b=a(this);b.data("validator").resetForm();b.find(".validation-summary-errors").addClass("validation-summary-valid").removeClass("validation-summary-errors");b.find(".field-validation-error").addClass("field-validation-valid").removeClass("field-validation-error").removeData("unobtrusiveContainer").find(">*").removeData("unobtrusiveContainer")}function i(c){var b=a(c),d=b.data(e),f=a.proxy(n,c);if(!d){d={options:{errorClass:"input-validation-error",errorElement:"span",errorPlacement:a.proxy(m,c),invalidHandler:a.proxy(l,c),messages:{},rules:{},success:a.proxy(k,c)},attachValidation:function(){b.unbind("reset."+e,f).bind("reset."+e,f).validate(this.options)},validate:function(){b.validate();return b.valid()}};b.data(e,d)}return d}d.unobtrusive={adapters:[],parseElement:function(b,h){var d=a(b),f=d.parents("form")[0],c,e,g;if(!f)return;c=i(f);c.options.rules[b.name]=e={};c.options.messages[b.name]=g={};a.each(this.adapters,function(){var c="data-val-"+this.name,i=d.attr(c),h={};if(i!==undefined){c+="-";a.each(this.params,function(){h[this]=d.attr(c+this)});this.adapt({element:b,form:f,message:i,params:h,rules:e,messages:g})}});a.extend(e,{__dummy__:true});!h&&c.attachValidation()},parse:function(b){var c=a(b).parents("form").andSelf().add(a(b).find("form")).filter("form");a(b).find(":input").filter("[data-val=true]").each(function(){d.unobtrusive.parseElement(this,true)});c.each(function(){var a=i(this);a&&a.attachValidation()})}};b=d.unobtrusive.adapters;b.add=function(c,a,b){if(!b){b=a;a=[]}this.push({name:c,params:a,adapt:b});return this};b.addBool=function(a,b){return this.add(a,function(d){c(d,b||a,true)})};b.addMinMax=function(e,g,f,a,d,b){return this.add(e,[d||"min",b||"max"],function(b){var e=b.params.min,d=b.params.max;if(e&&d)c(b,a,[e,d]);else if(e)c(b,g,e);else d&&c(b,f,d)})};b.addSingleVal=function(a,b,d){return this.add(a,[b||"val"],function(e){c(e,d||a,e.params[b])})};d.addMethod("__dummy__",function(){return true});d.addMethod("regex",function(b,c,d){var a;if(this.optional(c))return true;a=(new RegExp(d)).exec(b);return a&&a.index===0&&a[0].length===b.length});d.addMethod("nonalphamin",function(c,d,b){var a;if(b){a=c.match(/\W/g);a=a&&a.length>=b}return a});if(d.methods.extension){b.addSingleVal("accept","mimtype");b.addSingleVal("extension","extension")}else b.addSingleVal("extension","extension","accept");b.addSingleVal("regex","pattern");b.addBool("creditcard").addBool("date").addBool("digits").addBool("email").addBool("number").addBool("url");b.addMinMax("length","minlength","maxlength","rangelength").addMinMax("range","min","max","range");b.addMinMax("minlength","minlength").addMinMax("maxlength","minlength","maxlength");b.add("equalto",["other"],function(b){var i=h(b.element.name),j=b.params.other,d=g(j,i),e=a(b.form).find(":input").filter("[name='"+f(d)+"']")[0];c(b,"equalTo",e)});b.add("required",function(a){(a.element.tagName.toUpperCase()!=="INPUT"||a.element.type.toUpperCase()!=="CHECKBOX")&&c(a,"required",true)});b.add("remote",["url","type","additionalfields"],function(b){var d={url:b.params.url,type:b.params.type||"GET",data:{}},e=h(b.element.name);a.each(j(b.params.additionalfields||b.element.name),function(i,h){var c=g(h,e);d.data[c]=function(){return a(b.form).find(":input").filter("[name='"+f(c)+"']").val()}});c(b,"remote",d)});b.add("password",["min","nonalphamin","regex"],function(a){a.params.min&&c(a,"minlength",a.params.min);a.params.nonalphamin&&c(a,"nonalphamin",a.params.nonalphamin);a.params.regex&&c(a,"regex",a.params.regex)});a(function(){d.unobtrusive.parse(document)})})(jQuery);

/*!
 * baguetteBox.js
 * @author  feimosi
 * @version 1.1.1
 * @url https://github.com/feimosi/baguetteBox.js
 */
var baguetteBox=function(){function t(t,n){L.transforms=f(),L.svg=p(),e(),D=document.querySelectorAll(t),[].forEach.call(D,function(t){var e=t.getElementsByTagName("a");e=[].filter.call(e,function(t){return j.test(t.href)});var o=S.length;S.push(e),S[o].options=n,[].forEach.call(S[o],function(t,e){h(t,"click",function(t){t.preventDefault?t.preventDefault():t.returnValue=!1,i(o),a(e)})})})}function e(){return(b=v("picBox"))?(k=v("baguetteBox-slider"),w=v("previous-button"),C=v("next-button"),void(T=v("close-button"))):(b=y("div"),b.id="picBox",document.getElementsByTagName("body")[0].appendChild(b),k=y("div"),k.id="baguetteBox-slider",b.appendChild(k),w=y("button"),w.id="previous-button",w.innerHTML=L.svg?E:"&lt;",b.appendChild(w),C=y("button"),C.id="next-button",C.innerHTML=L.svg?x:"&gt;",b.appendChild(C),T=y("button"),T.id="close-button",T.innerHTML=L.svg?B:"X",b.appendChild(T),w.className=C.className=T.className="baguetteBox-button",void n())}function n(){h(b,"click",function(t){t.target&&"IMG"!==t.target.nodeName&&"FIGCAPTION"!==t.target.nodeName&&s()}),h(w,"click",function(t){t.stopPropagation?t.stopPropagation():t.cancelBubble=!0,c()}),h(C,"click",function(t){t.stopPropagation?t.stopPropagation():t.cancelBubble=!0,u()}),h(T,"click",function(t){t.stopPropagation?t.stopPropagation():t.cancelBubble=!0,s()}),h(b,"touchstart",function(t){N=t.changedTouches[0].pageX}),h(b,"touchmove",function(t){H||(t.preventDefault?t.preventDefault():t.returnValue=!1,touch=t.touches[0]||t.changedTouches[0],touch.pageX-N>40?(H=!0,c()):touch.pageX-N<-40&&(H=!0,u()))}),h(b,"touchend",function(){H=!1}),h(document,"keydown",function(t){switch(t.keyCode){case 37:c();break;case 39:u();break;case 27:s()}})}function i(t){if(A!==t){for(A=t,o(S[t].options);k.firstChild;)k.removeChild(k.firstChild);X.length=0;for(var e,n=0;n<S[t].length;n++)e=y("div"),e.className="full-image",e.id="baguette-img-"+n,X.push(e),k.appendChild(X[n])}}function o(t){t||(t={});for(var e in P)I[e]=P[e],"undefined"!=typeof t[e]&&(I[e]=t[e]);k.style.transition=k.style.webkitTransition="fadeIn"===I.animation?"opacity .4s ease":"slideIn"===I.animation?"":"none","auto"===I.buttons&&("ontouchstart"in window||1===S[A].length)&&(I.buttons=!1),w.style.display=C.style.display=I.buttons?"":"none"}function a(t){"block"!==b.style.display&&(M=t,r(M,function(){g(M),m(M)}),d(),b.style.display="block",setTimeout(function(){b.className="visible"},50))}function s(){"none"!==b.style.display&&(b.className="",setTimeout(function(){b.style.display="none"},500))}function r(t,e){var n=X[t];if("undefined"!=typeof n){if(n.getElementsByTagName("img")[0])return void(e&&e());imageElement=S[A][t],imageCaption=imageElement.getAttribute("data-caption")||imageElement.title,imageSrc=l(imageElement);var i=y("figure"),o=y("img"),a=y("figcaption");n.appendChild(i),i.innerHTML='<div class="spinner"><div class="double-bounce1"></div><div class="double-bounce2"></div></div>',o.onload=function(){var n=document.querySelector("#baguette-img-"+t+" .spinner");i.removeChild(n),!I.async&&e&&e()},o.setAttribute("src",imageSrc),i.appendChild(o),I.captions&&imageCaption&&(a.innerHTML=imageCaption,i.appendChild(a)),I.async&&e&&e()}}function l(t){var e=imageElement.href;if(t.dataset){var n=[];for(var i in t.dataset)"at-"!==i.substring(0,3)||isNaN(i.substring(3))||(n[i.replace("at-","")]=t.dataset[i]);keys=Object.keys(n).sort(function(t,e){return parseInt(t)<parseInt(e)?-1:1});for(var o=window.innerWidth*window.devicePixelRatio,a=0;a<keys.length-1&&keys[a]<o;)a++;e=n[keys[a]]||e}return e}function u(){M<=X.length-2?(M++,d(),g(M)):I.animation&&(k.className="bounce-from-right",setTimeout(function(){k.className=""},400))}function c(){M>=1?(M--,d(),m(M)):I.animation&&(k.className="bounce-from-left",setTimeout(function(){k.className=""},400))}function d(){var t=100*-M+"%";"fadeIn"===I.animation?(k.style.opacity=0,setTimeout(function(){L.transforms?k.style.transform=k.style.webkitTransform="translate3d("+t+",0,0)":k.style.left=t,k.style.opacity=1},400)):L.transforms?k.style.transform=k.style.webkitTransform="translate3d("+t+",0,0)":k.style.left=t}function f(){var t=y("div");return"undefined"!=typeof t.style.perspective||"undefined"!=typeof t.style.webkitPerspective}function p(){var t=y("div");return t.innerHTML="<svg/>","http://www.w3.org/2000/svg"==(t.firstChild&&t.firstChild.namespaceURI)}function g(t){t-M>=I.preload||r(t+1,function(){g(t+1)})}function m(t){M-t>=I.preload||r(t-1,function(){m(t-1)})}function h(t,e,n){t.addEventListener?t.addEventListener(e,n,!1):t.attachEvent("on"+e,n)}function v(t){return document.getElementById(t)}function y(t){return document.createElement(t)}var b,k,w,C,T,N,E='<svg width="44" height="60"><polyline points="30 10 10 30 30 50" stroke="rgba(255,255,255,0.5)" stroke-width="2"stroke-linecap="butt" fill="none" stroke-linejoin="round"/></svg>',x='<svg width="44" height="60"><polyline points="14 10 34 30 14 50" stroke="rgba(255,255,255,0.5)" stroke-width="2"stroke-linecap="butt" fill="none" stroke-linejoin="round"/></svg>',B='<svg width="30" height="30"><g stroke="rgb(160, 160, 160)" stroke-width="2"><line x1="5" y1="5" x2="25" y2="25"/><line x1="5" y1="25" x2="25" y2="5"/></g></svg>',I={},P={captions:!0,buttons:"auto",async:!1,preload:2,animation:"slideIn"},L={},M=0,A=-1,H=!1,j=/.+\.(gif|jpe?g|png|webp)/i,D=[],S=[],X=[];return[].forEach||(Array.prototype.forEach=function(t,e){for(var n=0;n<this.length;n++)t.call(e,this[n],n,this)}),[].filter||(Array.prototype.filter=function(t,e,n,i,o){for(n=this,i=[],o=0;o<n.length;o++)t.call(e,n[o],o,n)&&i.push(n[o]);return i}),{run:t}}();


//*! Lazy Load 1.9.5 - MIT license - Copyright 2010-2015 Mika Tuupola */
!function(a,b,c,d){var e=a(b);a.fn.lazyload=function(f){function g(){var b=0;i.each(function(){var c=a(this);if(!j.skip_invisible||c.is(":visible"))if(a.abovethetop(this,j)||a.leftofbegin(this,j));else if(a.belowthefold(this,j)||a.rightoffold(this,j)){if(++b>j.failure_limit)return!1}else c.trigger("appear"),b=0})}var h,i=this,j={threshold:0,failure_limit:0,event:"scroll",effect:"show",container:b,data_attribute:"original",skip_invisible:!1,appear:null,load:null,placeholder:"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC"};return f&&(d!==f.failurelimit&&(f.failure_limit=f.failurelimit,delete f.failurelimit),d!==f.effectspeed&&(f.effect_speed=f.effectspeed,delete f.effectspeed),a.extend(j,f)),h=j.container===d||j.container===b?e:a(j.container),0===j.event.indexOf("scroll")&&h.bind(j.event,function(){return g()}),this.each(function(){var b=this,c=a(b);b.loaded=!1,(c.attr("src")===d||c.attr("src")===!1)&&c.is("img")&&c.attr("src",j.placeholder),c.one("appear",function(){if(!this.loaded){if(j.appear){var d=i.length;j.appear.call(b,d,j)}a("<img />").bind("load",function(){var d=c.attr("data-"+j.data_attribute);c.hide(),c.is("img")?c.attr("src",d):c.css("background-image","url('"+d+"')"),c[j.effect](j.effect_speed),b.loaded=!0;var e=a.grep(i,function(a){return!a.loaded});if(i=a(e),j.load){var f=i.length;j.load.call(b,f,j)}}).attr("src",c.attr("data-"+j.data_attribute))}}),0!==j.event.indexOf("scroll")&&c.bind(j.event,function(){b.loaded||c.trigger("appear")})}),e.bind("resize",function(){g()}),/(?:iphone|ipod|ipad).*os 5/gi.test(navigator.appVersion)&&e.bind("pageshow",function(b){b.originalEvent&&b.originalEvent.persisted&&i.each(function(){a(this).trigger("appear")})}),a(c).ready(function(){g()}),this},a.belowthefold=function(c,f){var g;return g=f.container===d||f.container===b?(b.innerHeight?b.innerHeight:e.height())+e.scrollTop():a(f.container).offset().top+a(f.container).height(),g<=a(c).offset().top-f.threshold},a.rightoffold=function(c,f){var g;return g=f.container===d||f.container===b?e.width()+e.scrollLeft():a(f.container).offset().left+a(f.container).width(),g<=a(c).offset().left-f.threshold},a.abovethetop=function(c,f){var g;return g=f.container===d||f.container===b?e.scrollTop():a(f.container).offset().top,g>=a(c).offset().top+f.threshold+a(c).height()},a.leftofbegin=function(c,f){var g;return g=f.container===d||f.container===b?e.scrollLeft():a(f.container).offset().left,g>=a(c).offset().left+f.threshold+a(c).width()},a.inviewport=function(b,c){return!(a.rightoffold(b,c)||a.leftofbegin(b,c)||a.belowthefold(b,c)||a.abovethetop(b,c))},a.extend(a.expr[":"],{"below-the-fold":function(b){return a.belowthefold(b,{threshold:0})},"above-the-top":function(b){return!a.belowthefold(b,{threshold:0})},"right-of-screen":function(b){return a.rightoffold(b,{threshold:0})},"left-of-screen":function(b){return!a.rightoffold(b,{threshold:0})},"in-viewport":function(b){return a.inviewport(b,{threshold:0})},"above-the-fold":function(b){return!a.belowthefold(b,{threshold:0})},"right-of-fold":function(b){return a.rightoffold(b,{threshold:0})},"left-of-fold":function(b){return!a.rightoffold(b,{threshold:0})}})}(jQuery,window,document);

$("img.lazyload").lazyload({effect:"fadeIn"})

/*weixinPop*/  
var weixinHtml = '<div class="weixinPop"><div class="mask"></div><div class="inner"><a href="javascript:;" onclick="checkclose()" class="weixin-close"><i class="iconfont">&#xe61e;</i></a><h3 class="tit">汇盈金服订阅号!</h3><img src="/themes/default/images/weixin-code.png"><p>获取汇盈金服最新资讯</p></div></div>'
$('.weixin').on('click', function () {$('body').addClass('weixin-show');$('body').append(weixinHtml);});function checkclose(){$('body').removeClass('weixin-show');$('.weixinPop').remove();}

/*scrollTop*/
var toolbarHtml = '<!--toolbar start--><div class="r-toolbar"><ul><!--<li><div class="r-hover"><img width="105" height="105" alt="关注汇盈金服微信" src="/themes/default/images/weixin-code.png"><p>汇盈金服</p></div><i class="iconfont"></i></li><li><div class="r-hover"><img width="105" height="105" alt="下载汇盈金服APP" src="/themes/default/images/mobile/1121549398.png"><p>汇盈金服APP下载</p></div><i class="iconfont"></i></li>--><li><a title="在线客服" target="_blank" href="http://b.qq.com/webc.htm?new=0&sid=4000655000&eid=218808P8z8p8p8z8K8p80&o=www.huiyingdai.com&q=7"><!--<div class="r-hover"><div class="item-tip">在线客服<br>9:00~21:00</div></div>--><i class="iconfont"></i><span class="qq_word">在线客服</span></a></li><li class="rollbar"><a href="javascript:(scrollTo());"><div class="r-hover"><p>返回顶部</p></div><i class="iconfont"></i></a></li></ul></div><!--toolbar end-->'
$('body').append(toolbarHtml)
var scroller=$(".rollbar");$(window).scroll(function(){document.documentElement.scrollTop+document.body.scrollTop>200?scroller.fadeIn():scroller.fadeOut()});function scrollTo(name,add,speed){if(!speed){speed=300}if(!name){$("html,body").animate({scrollTop:0},speed)}else{if($(name).length>0){$("html,body").animate({scrollTop:$(name).offset().top+(add||0)},speed)}}};

/*onload_banner*/
$(function() {
	$(".onload_banner").show();
	$(".onload_banner").animate({
		top: '0'
	}, 500).animate({
		top: "0px"
	}, 1600);
	setTimeout('$(".onload_banner").fadeOut();', 6000);//十秒后隐藏
	$('.onload_closed').click(function() {
		$('.onload_banner').fadeOut()
	})
});	

/*Slider*/
/*eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('3 p=2(){$(C.q).v();3 b=0,c=$(".6-s").t(),d,g=2(){3 a=2(a){$(".6-r a").R("w").7(a).Y("w")};n{5:2(b,c){X(3 d=$(".6-r"),e=0;e<b;e++)d.G("<8><a></a></8>");d.O("8","9",2(){3 b=$(V).W();a(b);c&&c(b)})},E:a}}(),h=2(){b=0==b?c.o-1:b-1;e(b)},f=2(){b=b==c.o-1?0:b+1;e(b)},e=2(a){b=a;c.P(":Q").F();c.7(a).i();g.E(a);13((a=c.7(a))&&"u"!=a.j("k-x")){3 d=a.j("k-I");a.j("k-x","u");a.J("a").K("L-M","N(\'"+d+"\')")}};n{5:2(){$(C.q).v();g.5(c.t().y(),2(a){z(d);e(a)});0<c.y()&&(e(0),d=A(f,B));$(".S-T").U(2(){$(".4-l,.4-m").i("D");z(d)},2(){$(".4-l,.4-m").F("D");d=A(f,B)});$(".4-l").9(2(){h()});$(".4-m").9(2(){f()});c.Z(":10").11();$(".6-s").i()},12:f,H:h}}();p.5();',62,66,'||function|var|arrow|init|slider|eq|li|click|||||||||fadeIn|attr|data|left|right|return|length|Slider|body|lights|imgs|children|true|width|active|ok|size|clearInterval|setInterval|5E3|document|fast|light|fadeOut|append|previous|img|find|css|background|image|url|delegate|filter|visible|removeClass|new|banner|hover|this|index|for|addClass|not|first|hide|next|if'.split('|'),0,{}))*/

/*filter-other*/
$('.filter-other').on('click', function() {
	$(".pro-filter").toggleClass("open")
});

/* 数字滚动*/
eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('i 8=h(c,d,e,f){i a=y;r(a.5=c,a.g=a.5.D("4"),a.2=[],x(a.g))n!1;a.7="";a.l=0;a.k=0;a.m="";p(c=a.g.q().6-1;0<=c;c--)a.m=a.g.q().t(c),a.2.u(a.m);a.4=h(){r(a.7="",9<a.k)n A(a.l),a.5.v("4-w",!0),H 0;p(i b=a.2.6-1;0<=b;b--)a.2[b]=z(a.2[b]),a.2[b]=9<a.2[b]+1?0:a.2[b]+1,a.7+=(d||"")+a.2[b]+(e||""),f&&0==b%3&&0!=b&&(a.7+=f);a.5.s();a.5.s(a.7);a.k++;a.l=B(a.4,C)}};o=h(){i c=$(".E"),d=$(".F"),e=$(".G");0<c.6&&(j 8(c,"","",",")).4();0<d.6&&(j 8(d,"","",",")).4();0<e.6&&(j 8(e,"","",",")).4()};$(h(){o()});',44,44,'||arr||spark|obj|length|frag|numberSpark||||||||no|function|var|new|flag|intv|sn|return|init|for|toString|if|html|charAt|push|attr|done|isNaN|this|Number|clearTimeout|setTimeout|50|data|per|riskm|income|void'.split('|'),0,{}))
		
/*进度滚动 */
eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('6.A=2(){$.m=2(a,b){a=a||J,2(){3 c=1,d=6.E(2(){c>=a&&D(d),b(c),c++},5)}()};3 g={q:2(){3 a=8;$(6).z("y",2(){a.l()})},l:2(){3 a=8,b=$(8).x(),c=$(".w-s-K").p("r[7-9=\'0\']"),d=c.t;u(d>0)v(3 e=0;d>e;e++){3 f=$(c[e]);f.4("7-9")-0||b>f.j().o+f.B()||b+$(6).C()<f.j().o||a.n(f)}},n:2(a){3 b=a.p(".h"),c=b.4("7-F")-0,d=G;a.4("7-9","1");3 e=1,f=H.I(d/c);$.m(d,2(a){e>=c||a>=d?b.4("i","h k"+c):a%f==0&&(b.4("i","h k"+e),e++)})}};$(2(){g.q()})};',47,47,'||function|var|attr||window|data|this|flag||||||||notesite|class|offset|lv|processHandler|customAnimate|animateProcess|top|find|init|tr|loan|length|if|for|new|scrollTop|scroll|bind|onload|outerHeight|height|clearInterval|setInterval|process|99|Math|floor|100|list'.split('|'),0,{}));

/*help*/
$(function() {
	$(".faq-info").hide();
	$(".help-invest-h5").click(function() {
		$(this).next(".faq-info").slideToggle().siblings(".faq-info:visible").slideUp("slow")
	})
})

/*tab*/
var TabBlock={s:{animLen:200},init:function(){TabBlock.bindUIActions();TabBlock.hideInactive()},bindUIActions:function(){$('.tabs-nav').on('click','.tabs-li',function(){TabBlock.switchTab($(this))})},hideInactive:function(){var $tabBlocks=$('.istabs');$tabBlocks.each(function(i){var $tabBlock=$($tabBlocks[i]),$panes=$tabBlock.find('.tab-panel'),$activeTab=$tabBlock.find('.tabs-li.active');$panes.hide();$($panes[$activeTab.index()]).show()})},switchTab:function($tab){var $context=$tab.closest('.istabs');if(!$tab.hasClass('active')){$tab.siblings().removeClass('active');$tab.addClass('active');TabBlock.showPane($tab.index(),$context)}},showPane:function(i,$context){var $panes=$context.find('.tab-panel');$panes.slideUp(TabBlock.s.animLen);$($panes[i]).slideDown(TabBlock.s.animLen)}};$(function(){TabBlock.init()});

/*!
 * 登陆弹窗
 * ====================================================
*/
//有验证码
//var signHtml='<!-- start: signPop --><div class="sign"><div class="sign-mask"></div><div class="container"><a aria-hidden="true" data-dismiss="sign" class="close-link signclose-loader" href="javascript:;"><i class="iconfont">&#xe61e;</i></a><div class="sign-tips"><p class="alert-error"></p></div><form action="/site/login_ajax" method="get" class="signForm" id="loginForm" novalidate><h3><small class="signup">切换注册</small>登录</h3><div class="input-group"><span class="input-tip">用户名</span><input data-val="true" placeholder="用户名/手机号/邮箱" data-left="355" id="username" name="username" tabindex="1" type="text" value="" data-val-regex="用户名错误。" data-val-required="请填写用户名。" data-val-remote="用户不存在!" data-val-remote-additionalfields="*.username" data-val-remote-type="post" data-val-remote-url="/site/ajaxuserlogin.html"><span class="help-block"><span class="field-validation-valid" data-valmsg-for="username" data-valmsg-replace="true"></span></span></div><div class="input-group"><span class="input-tip">密码</span><input autocomplete="off" placeholder="密码" data-left="355" data-val="true" data-val-length="登录密码必须由6-32位字符组成。" data-val-length-max="32" data-val-length-min="6" data-val-required="请填写登录密码。" id="password" name="password" tabindex="2" type="password"><span class="help-block"><span class="field-validation-valid" data-valmsg-for="password" data-valmsg-replace="true"></span></span></div><div class="input-group small"><div class="input-group-addon"><img title="点击刷新" src="/vcode/123/45.html" id="CaptchaImage"></div><span class="input-tip">验证码</span><input data-left="320" data-val="true" placeholder="验证码" id="CaptchaInputText" name="CaptchaInputText" type="text" value="" autocomplete="off" tabindex="3" class="valid" data-val-length="必须为4位" minlength="4" maxlength="4" data-val-length-max="4" data-val-required="验证码不可为空。" data-val-remote="验证码错误!" data-val-remote-additionalfields="*.CaptchaInputText" data-val-remote-type="post" data-val-remote-url="/site/ajaxcaptcha.html"><span class="help-block"><span class="field-validation-valid" data-valmsg-for="CaptchaInputText" data-valmsg-replace="true"></span></span></div><button type="submit" id="act_login" class="login-btn" tabindex="4"><input type="hidden" name="redirect" value="" id="redirect">立即登录</button><p class="login-other"><a class="fr" href="/site/findPwd">忘记密码？</a></p></form><form novalidate class="regForm" method="get" action="/site/register" id="regForm"><h3><small class="login">切换登录</small>注册</h3><div class="input-group"><span class="input-tip">用户名</span><input type="text" class="username" placeholder="用户名" data-left="480" tabindex="1" id="UserName" name="UserName" autocomplete="off" data-val="true" data-val-regex="昵称只允许字母、数字、下划线、横线组成，首位只能为字母，且至少需要 6 个字符。" data-val-regex-pattern="^[a-zA-z][a-zA-Z0-9_]{5,19}$" data-val-remote="用户名已存在。" data-val-remote-additionalfields="*.UserName" data-val-remote-url="/site/ajaxusersign.html" data-val-remote-type="post" data-val-required="请填写昵称。" /><span class="help-block"><span class="field-validation-valid" data-valmsg-for="UserName" data-valmsg-replace="true"></span></span></div><div class="input-group"><span class="input-tip">密码</span><input type="password" class="password" data-left="480" placeholder="密码" tabindex="2" id="LoginPass" name="LoginPass" autocomplete="off" data-val="true" data-val-regex="密码只能为 6 - 32 位数字，字母及常用符号组成。" data-val-regex-pattern="^[A-Za-z0-9\^$\.\+\*_@!#%&amp;~=-]{6,32}$" data-val-required="请填写登录密码。"/><span class="help-block"><span class="field-validation-valid" data-valmsg-for="LoginPass" data-valmsg-replace="true"></span></span></div><div class="input-group small"><div class="input-group-addon"><img title="点击刷新" src="/vcode/123/45.html" id="CaptchaImage1"></div><span class="input-tip">验证码</span><input data-left="320" data-val="true" placeholder="验证码" id="CaptchaInputText" name="CaptchaInputText" type="text" minlength="4" maxlength="4"  value="" autocomplete="off" tabindex="3" class="valid" data-val-length="必须为4位" data-val-length-max="4" data-val-required="验证码不可为空。" data-val-remote="验证码错误!" data-val-remote-additionalfields="*.CaptchaInputText" data-val-remote-type="post" data-val-remote-url="/site/ajaxcaptcha.html"><span class="help-block"><span class="field-validation-valid" data-valmsg-for="CaptchaInputText" data-valmsg-replace="true"></span></span></div><div class="input-group small"><div class="input-group-addon"><div class="msgs" id="msgs">发送验证码</div></div><span class="input-tip">手机号码</span><input data-left="320" data-val="true" id="Mobile" name="mobile" value="" type="text" tabindex="4" autocomplete="off" placeholder="请输入手机号" data-val-required="请填写手机号码。" data-val-remote="手机已经被注册。" maxlength="11" minlength="11" data-val-remote-additionalfields="*.mobile" data-val-remote-type="post" data-val-remote-url="/site/ajaxmobilesign.html"><span class="help-block"><span class="field-validation-valid" data-valmsg-for="mobile" data-valmsg-replace="true"></span></span></div><div class="input-group"><span class="input-tip">手机验证码</span><input data-left="480" placeholder="请输入手机验证码" maxlength="6" minlength="6" class="valid" data-val="true" id="MobileCode" name="mobileCode" value="" type="text" autocomplete="off" tabindex="5"  data-val-required="请填写手机验证码。"><span class="help-block"><span class="field-validation-valid" data-valmsg-for="mobileCode" data-valmsg-replace="true"></span></span></div><a class="coupon-code-bar show" href="javascript:;"></a><div class="input-group coupon-code-wrap" style="display:none"><span class="input-tip">推荐人</span><input data-left="480" placeholder="推荐人手机号或邀请码" class="valid" data-val="true" id="RecommendCode" name="recommendCode" value="" type="text" autocomplete="off" tabindex="6" data-val="true" data-val-regex="邀请码必须数字/手机号必须是11位数字" data-val-regex-pattern="((^[0-9]*$))"  maxlength="11" data-val-remote="邀请码/手机号无效"  data-val-remote-additionalfields="*.RecommendCode" data-val-remote-url="/site/ajaxrecommend.html" data-val-remote-type="post" ><span class="help-block"><span class="field-validation-valid" data-valmsg-for="recommendCode" data-valmsg-replace="true"></span></span></div><div class="register-btn-wrap"><button class="register-btn" id="act_register" type="submit" tabindex="7">同意用户协议并注册 </button><div class="contract-container"><p>点击注册即同意汇盈金服</p><a href="/article/agreement.html">用户协议</a><div class="contract-arrow"><em>◆</em><i>◆</i></div></div></div></form></div></div><!-- end: signPop -->'
/*
//无验证码
var signHtml='<!-- start: signPop --><div class="sign"><div class="sign-mask"></div><div class="container"><a aria-hidden="true" data-dismiss="sign" class="close-link signclose-loader" href="javascript:;"><i class="iconfont">&#xe61e;</i></a><div class="sign-tips"><p class="alert-error"></p></div><form action="/site/login_ajax" method="get" class="signForm" id="loginForm" novalidate><h3><small class="signup">切换注册</small>登录</h3><div class="input-group"><span class="input-tip">用户名</span><input data-val="true" placeholder="用户名/手机号/邮箱" data-left="355" id="username" name="username" tabindex="1" type="text" value="" data-val-regex="用户名错误。" data-val-required="请填写用户名。" data-val-remote="用户不存在!" data-val-remote-additionalfields="*.username" data-val-remote-type="post" data-val-remote-url="/site/ajaxuserlogin.html"><span class="help-block"><span class="field-validation-valid" data-valmsg-for="username" data-valmsg-replace="true"></span></span></div><div class="input-group"><span class="input-tip">密码</span><input autocomplete="off" placeholder="密码" data-left="355" data-val="true" data-val-length="登录密码必须由6-32位字符组成。" data-val-length-max="32" data-val-length-min="6" data-val-required="请填写登录密码。" id="password" name="password" tabindex="2" type="password"><span class="help-block"><span class="field-validation-valid" data-valmsg-for="password" data-valmsg-replace="true"></span></span></div><div class="input-group small"><div class="input-group-addon"><img title="点击刷新" src="/vcode/123/45.html" id="CaptchaImage"></div><span class="input-tip">验证码</span><input data-left="320" data-val="true" placeholder="验证码" id="CaptchaInputText" name="CaptchaInputText" type="text" value="" autocomplete="off" tabindex="3" class="valid" data-val-length="必须为4位" minlength="4" maxlength="4" data-val-length-max="4" data-val-required="验证码不可为空。" data-val-remote="验证码错误!" data-val-remote-additionalfields="*.CaptchaInputText" data-val-remote-type="post" data-val-remote-url="/site/ajaxcaptcha.html"><span class="help-block"><span class="field-validation-valid" data-valmsg-for="CaptchaInputText" data-valmsg-replace="true"></span></span></div><button type="submit" id="act_login" class="login-btn" tabindex="4"><input type="hidden" name="redirect" value="" id="redirect">立即登录</button><p class="login-other"><a class="fr" href="/site/findPwd">忘记密码？</a></p></form><form novalidate class="regForm" method="get" action="/site/register" id="regForm"><h3><small class="login">切换登录</small>注册</h3><div class="input-group"><span class="input-tip">用户名</span><input type="text" class="username" placeholder="用户名" data-left="480" tabindex="1" id="UserName" name="UserName" autocomplete="off" data-val="true" data-val-regex="昵称只允许字母、数字、下划线、横线组成，首位只能为字母，且至少需要 6 个字符。" data-val-regex-pattern="^[a-zA-z][a-zA-Z0-9_]{5,19}$" data-val-remote="用户名已存在。" data-val-remote-additionalfields="*.UserName" data-val-remote-url="/site/ajaxusersign.html" data-val-remote-type="post" data-val-required="请填写昵称。" /><span class="help-block"><span class="field-validation-valid" data-valmsg-for="UserName" data-valmsg-replace="true"></span></span></div><div class="input-group"><span class="input-tip">密码</span><input type="password" class="password" data-left="480" placeholder="密码" tabindex="2" id="LoginPass" name="LoginPass" autocomplete="off" data-val="true" data-val-regex="密码只能为 6 - 32 位数字，字母及常用符号组成。" data-val-regex-pattern="^[A-Za-z0-9\^$\.\+\*_@!#%&amp;~=-]{6,32}$" data-val-required="请填写登录密码。"/><span class="help-block"><span class="field-validation-valid" data-valmsg-for="LoginPass" data-valmsg-replace="true"></span></span></div><div class="input-group small"><div class="input-group-addon"><img title="点击刷新" src="/vcode/123/45.html" id="CaptchaImage1"></div><span class="input-tip">验证码</span><input data-left="320" data-val="true" placeholder="验证码" id="CaptchaInputText" name="CaptchaInputText" type="text" minlength="4" maxlength="4"  value="" autocomplete="off" tabindex="3" class="valid" data-val-length="必须为4位" data-val-length-max="4" data-val-required="验证码不可为空。" data-val-remote="验证码错误!" data-val-remote-additionalfields="*.CaptchaInputText" data-val-remote-type="post" data-val-remote-url="/site/ajaxcaptcha.html"><span class="help-block"><span class="field-validation-valid" data-valmsg-for="CaptchaInputText" data-valmsg-replace="true"></span></span></div><a class="coupon-code-bar show" href="javascript:;"></a><div class="input-group coupon-code-wrap" style="display:none"><span class="input-tip">推荐人</span><input data-left="480" placeholder="推荐人手机号或邀请码" class="valid" data-val="true" id="RecommendCode" name="recommendCode" value="" type="text" autocomplete="off" tabindex="6" data-val="true" data-val-regex="邀请码必须数字/手机号必须是11位数字" data-val-regex-pattern="((^[0-9]*$))"  maxlength="11" data-val-remote="邀请码/手机号无效"  data-val-remote-additionalfields="*.RecommendCode" data-val-remote-url="/site/ajaxrecommend.html" data-val-remote-type="post" ><span class="help-block"><span class="field-validation-valid" data-valmsg-for="recommendCode" data-valmsg-replace="true"></span></span></div><div class="register-btn-wrap"><button class="register-btn" id="act_register" type="submit" tabindex="7">同意用户协议并注册 </button><div class="contract-container"><p>点击注册即同意汇盈金服</p><a href="/article/agreement.html">用户协议</a><div class="contract-arrow"><em>◆</em><i>◆</i></div></div></div></form></div></div><!-- end: signPop -->'


$("body").append( signHtml )
$("#redirect").val(window.location.href);

$('.btn-login,.login,.btn-stock').on('click', function(){
	$('body').addClass('sign-show')
    $('.sign .signForm').show().find('input:first').focus()
	$("#CaptchaImage").attr('src','/vcode/123/45.html?'+Math.random()); //加载验证码
    $('.sign .regForm').hide()
	$('.sign').removeClass('max'),	
	$('.sign').removeClass('mxa')	
})

$('.signup,.btn-signup').on('click', function(){
	$('body').addClass('sign-show')
	$('.sign').addClass('max mxa')
    $('.sign .regForm').show().find('input:first').focus()
	$("#CaptchaImage1").attr('src','/vcode/123/45.html?'+Math.random()); //加载验证码
    $('.sign .signForm').hide()
	$('.coupon-code-wrap').show()
	$('.regForm').show().find('a.show').addClass('max mxa')
})
*/
$('.coupon-code-bar').on('click', function(){
	$(".sign").toggleClass("mxa");
})

$('.signclose-loader').on('click', function(){
	$('body').removeClass('sign-show')
});

var abc = new function () {
	this.showTip = function(info){
		$(".sign-tips").show().animate({height: 29}, 220).find('p.alert-error').html(info);
		$(".loan-tip").show().animate({height: 29}, 220).find('p.alert-error').html(info);
		setTimeout("clean()",3000);
		//resizeIframe();
	};
}

function clean() {
	$(".sign-tips").hide('slow');
	$(".loan-tip").hide('slow');
}

//刷新页面
function refreshPage() {
	window.location.reload()
}
//跳转页面
function jumpPage(url) {
	window.location.href = url;
}
$.validator.setDefaults({
	submitHandler: function(form) {
		//根据表单method判断是否直接提交
		if (form.method == "get") {
			//一般网页ajax表单提交
			$.ajax({
				cache: true,
				type: "POST",
				url: form.action,//提交地址为form action值
				dataType: "json",
				data: $('#' + form.id).serialize(),
				success: function(data) {
					if(data.status != null && data.status != ""){
						/*php调用*/
						if (data.status == 1) {
							if (data.location != '') {
								window.location.href = data.location
							} else {
								window.location.href = '/'
							}
						} else if (data.status == 3) {
							abc.showTip(data.info);
							setTimeout('refreshPage()', 4000)
						} else if (data.status == 4) {
							abc.showTip(data.info);
							setTimeout(function(){jumpPage(data.location)}, 1500)
						}else {
							abc.showTip(data.info)
						}
					}else if(data.resultFlag != null && data.resultFlag != ""){
						/*java调用*/
						/*2016-01-04 php-->java修改调用了java的后台*/
						if("1" == data.resultFlag){
							var json = data.msg;
							var str = ""; 
							for (var one in json) 
							{ 
								str += json[one] + " ";
							} 
							abc.showTip(str);
							
						}else if("0" == data.resultFlag){
							if (data.url != '') {
								/*window.location.href = data.url;*/
								abc.showTip("提交成功");
								$('input[type="text"]').val("");
								$('select').val("");
							} else {
								window.location.href = '/';
							}
						}else{
							abc.showTip("请检查自己的网络");
						}
					}
				}
			})
		} else {
			form.submit()//直接提交
		}
	}
});

/*reg*/
$(function(){
// input tips
$('input').focus(function() {
	$(this).prev('.input-tip').animate({
		left: $(this).attr('data-left') - 125 + 'px'
	}, 350)
}).blur(function() {
	if ($(this).val() && $(this).val().length > 0) return;
	$(this).prev('.input-tip').animate({
		left: $(this).attr('data-left') + 'px'
	}, 500)
});

// set recommend code
$("#CaptchaImage,#CaptchaImage1").click(function() {
	$("#CaptchaImage,#CaptchaImage1").attr({
		'src': 'http://' + window.location.host + '/vcode/123/45?' + Math.random(),
		'title': '点击刷新'
	})
});

// coupon code slide
$('.coupon-code-bar').click(function() {
	$(this).toggleClass('show').next('.coupon-code-wrap').toggle('slideDown')
});

// register contract
(function($) {
	var t = 0,
		st;
	$('.register-btn-wrap').mouseover(function() {
		clearTimeout(st);
		$(this).find('.contract-container').css({
			display: 'block'
		})
	}).mouseout(function(e) {
		var target = e.target || window.event.srcElement,
			that = this;
		if (target.tagName !== 'BUTTON') return;
		st = setTimeout(function() {
			if (t == 0) {
				$(that).find('.contract-container').css({
					display: 'none'
				})
			}
			clearTimeout(st)
		}, 1000)
	});
	$('.contract-container').mouseover(function() {
		t = 1
	}).mouseout(function() {
		$(this).css({
			display: 'none'
		});
		t = 0
	})
})($);

// get mobile code
$('.msgs').click(function() {
	var _self = $(this);
	_self.prop("disabled", true).addClass('disable');
	$.ajax({
		url: '/site/sendsmscode',
		type: 'POST',
		data: {
			phone: $("#Mobile").val()
		},
		dataType: 'json',
		success: function(data) {
			var validStatus = 1
			if (data.errno != 0) {
				abc.showTip(data.error);
				_self.text("发送验证码").prop("disabled", false).removeClass('disable');
				return false
			}
			if (validStatus === 1) {
				var i = 120;
				var t = null;

				function timer() {
					if (i <= 0) {
						_self.text("重新获取验证码").prop("disabled", false).removeClass('disable');
						return
					}
					_self.text(i + " 秒后重新获取") 
					i--;
					t = setTimeout(timer, 1000)
				}
				timer()
			}
		}
	})
});

// get mobile code
$('.loanmsgs').click(function() {
	var _self = $(this);
	_self.prop("disabled", true).addClass('disable');
	$.ajax({
		url: '/site/sendloansms',
		type: 'POST',
		data: {
			phone: $("#Mobile").val()
		},
		dataType: 'json',
		success: function(data) {
			var validStatus = 1
			if (data.errno != 0) {
				abc.showTip(data.error);
				_self.text("发送验证码").prop("disabled", false).removeClass('disable');
				return false
			}
			if (validStatus === 1) {
				var i = 120;
				var t = null;

				function timer() {
					if (i <= 0) {
						_self.text("重新获取验证码").prop("disabled", false).removeClass('disable');
						return
					}
					_self.text(i + " 秒后重新获取") 
					i--;
					t = setTimeout(timer, 1000)
				}
				timer()
			}
		}
	})
});

function checkVerifyCode() {
	var tip = $("#tip");
	var vCode = document.getElementById("verifycode");
	if (vCode == null || vCode.value.length < 4) {
		tip.html("请输入验证码");
		tipShow();
		return false
	} else {
		tipHide()
	}
	return true
}
});

/*partners*/
var tabul = $(".tabul");
var tabula = $(".tabul").find("a");
var tabsDiv = $(".ipartner");
tabul.find("a").click(function(){
	tabul.find("a").removeClass("active");
	$(this).addClass("active");
	tabsDiv.removeClass("show");
	tabsDiv.eq($(".tabul").find("a").index($(this))).addClass("show");
})
var index_=window.location.hash.replace(/#/g,"");
tabul.find("a").removeClass("active");
tabul.find("a").eq(index_).addClass("active");
tabsDiv.removeClass("show");
tabsDiv.eq(index_).addClass("show");

/*jobs*/
$(function(){
	$(".ifaq dd").hide();
	$(".ifaq dt").click(function(){ 
		$(this).next("dd").slideToggle("slow").siblings("dd:visible").slideUp("slow");
	});
})

/*auto setting*/
$(".switch").click(function() {
	$(this).toggleClass("switch-off", 1000);
	$.ajax({
		url: '/user/account_profile',
		type: 'POST',
		data: {
			borrow_sms: $("#borrow_sms").val()
		},
		dataType: 'json',
		success: function(data) {
			$("#borrow_sms").val(data)
		}
	});
	if ($("#borrow_sms").val() == 1) {
		$('.switch-label').html('关闭')
	} else {
		$('.switch-label').html('开启')
	}
	return false
});
/*用户通知配置*/
$(".set_notice").change(function(){
	$.ajax({
		url: '/user/set_notice',
		type: 'POST',
		data: {
			action: $(this).attr("act"),
			value: $(this).val()
		},
		dataType: 'json',
		success: function(data){
			if(data.code == 1){
				if($(this).val()==1){
					//$(this).val()=0;
					$(this).val(0);
				}else{
					//$(this).val()==1;
					$(this).val(1);
				}
			}
		}
	});
})

$(function(){
//利率排序
$("#apr").click(function() {
	var upclass = $(this).attr("class");
	if (upclass == 'down') {
		$(this).attr("class", "up");
		var url_u = $("#apr_u").val();
		document.location.href = url_u
	} else {
		$(this).attr("class", "down");
		var url_d = $("#apr_d").val();
		document.location.href = url_d
	}
});
//期限排序
$("#timelimit").click(function() {
	var upclass = $(this).attr("class");
	if (upclass == 'down') {
		$(this).attr("class", "up");
		var url_up = $("#tlimit_u").val();
		document.location.href = url_up
	} else {
		$(this).attr("class", "up");
		var url_dp = $("#tlimit_d").val();
		document.location.href = url_dp
	}
});
});
$(function(){
//详情弹窗
$('.allbtn').click(function() {
	$('body').addClass('states-show')
}) 
$('.close').click(function() {
	$('body').removeClass('states-show')
})
})

//cnzz
var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan style='display:none' id='cnzz_stat_icon_1000171048'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s22.cnzz.com/z_stat.php%3Fid%3D1000171048' type='text/javascript'%3E%3C/script%3E"));
var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/hm.js?70d2e6c2cf42c804e34504e3dea1f168' type='text/javascript'%3E%3C/script%3E"));

//六月活动
/*var irice = '<div class="irice"><div class="small"></div><div class="big"></div>'
$('body').append(irice);
$('.irice').on('click', function () {
  $('.irice').toggleClass("open");
  });*/