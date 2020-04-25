package com.hyjf.tlds;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;

public class DateTimeFormatTag extends TagSupport {
	private static final long serialVersionUID = -2312310581852395045L;
	private String value;

	@Override
	public int doStartTag() throws JspException {
		if (StringUtils.isNotEmpty(value)) {
			String vv = "" + value;
			long time = Long.valueOf(vv) * 1000;
			Date date = new Date(time);
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String s = dateformat.format(date);
			try {
				pageContext.getOut().write(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
}