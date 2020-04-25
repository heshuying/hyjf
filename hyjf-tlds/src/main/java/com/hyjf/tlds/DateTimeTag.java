package com.hyjf.tlds;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;

public class DateTimeTag extends TagSupport {
	private static final long serialVersionUID = -2312310581852395045L;
	private String value;
	private String pattern;

	@Override
	public int doStartTag() throws JspException {
	    if(StringUtils.isEmpty(value)){
            return super.doStartTag();  
        }
		String vv = "" + value;
		long time = Long.valueOf(vv) * 1000;
		Date date = new Date(time);
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!StringUtils.isEmpty(pattern)) {
			dateformat = new SimpleDateFormat(pattern);
		}
		String s = dateformat.format(date);
		try {
			pageContext.getOut().write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}

	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}

	public void setValue(String value) {
		this.value = value;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}