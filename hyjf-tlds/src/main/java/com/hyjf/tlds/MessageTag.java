package com.hyjf.tlds;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.support.RequestContext;

import com.hyjf.common.http.HtmlUtil;

public class MessageTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private String message;

	@Override
	public int doStartTag() throws JspException {
		Object value = pageContext.getRequest().getAttribute(key);
		if (value == null || StringUtils.isBlank(value.toString())) {
			return EVAL_BODY_INCLUDE;
		}

		if (StringUtils.isBlank(message) && value != null) {
			message = value.toString();
		}

		if (StringUtils.isNotBlank(message)) {
			RequestContext requestContext = new RequestContext((HttpServletRequest)pageContext.getRequest());
			String tempMessage = requestContext.getMessage(message);
			if (StringUtils.isNotBlank(tempMessage)) {
				message = tempMessage;
			}
		}

		try {
			if (StringUtils.isNotBlank(message)) {
				JspWriter out = this.pageContext.getOut();
                StringBuffer sb = new StringBuffer();
                createErrorMsg(sb, message);
                out.print(sb.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		message = "";
		return EVAL_BODY_INCLUDE;
	}
    
    /**
     * 创建错误信息
     * @param sb
     * @param msg
     */
    private void createErrorMsg(StringBuffer sb, String msg) {
        sb.append("<script>");
        sb.append("$(document).ready(function(){Page.notice('");
        sb.append(HtmlUtil.escape(msg));
        sb.append("','', '");
        sb.append(key.contains("error") ? "error" : (key.contains("warn") ? "warning" : "success"));
        sb.append("')})</script>");
    }

	@Override
	public int doAfterBody() throws JspException {
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}
}
