package com.hyjf.admin.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {

	/**
	 * 在需要生成token的controller类的方法上增加@Token(save=true)
	 * 
	 * @return
	 */
	boolean save() default false;

	/**
	 * 在需要生成token的controller类的方法上增加@Token(save=true)
	 * 
	 * @return
	 */
	boolean check() default false;

	/**
	 * 在需要检查重复提交后跳转的方法
	 * 
	 * @return
	 */
	String forward() default "/admin/login/init";
}