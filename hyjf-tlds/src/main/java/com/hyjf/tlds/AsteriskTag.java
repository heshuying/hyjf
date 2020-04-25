package com.hyjf.tlds;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

import com.hyjf.common.util.AsteriskProcessUtil;

public class AsteriskTag extends TagSupport {
	private static final long serialVersionUID = -2312310581852395045L;
	private String value;
	
	private String permission;

	@Override
	public int doStartTag() throws JspException {
		if (StringUtils.isNotEmpty(value)) {
		    String result = "";
		    String phoneNumAsterisked = "";
	        if(StringUtils.isNotEmpty(value) && value.length() > 7){
	            phoneNumAsterisked = value.substring(0, 3) + AsteriskProcessUtil.getAsterisked(value.length() - 7) + value.substring(value.length() - 4);
	        }else {
	            phoneNumAsterisked = (value==null?"":value);
	        }
	        
	        boolean isHasPermission = SecurityUtils.getSubject().isPermitted(permission);
	        if(isHasPermission){
	            result = value;
	        }else{
	            result = phoneNumAsterisked;
	        }
			try {
				pageContext.getOut().write(result);
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

    public void setPermission(String permission) {
        this.permission = permission;
    }
}