package com.hyjf.tlds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.support.RequestContext;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.KeyValueBean;

public class ValidMessageTag extends TagSupport {
    private static final long serialVersionUID = 1L;
    private static final String PREFIX = "validateError-";

    private String key;
    private String message;
    private String label;
    private String params;

    @Override
    public int doStartTag() throws JspException {
        List<String> messageList = new ArrayList<String>();
        Object value = pageContext.getRequest().getAttribute(PREFIX + key);
        if (value == null) {
            return EVAL_BODY_INCLUDE;
        }

        if (StringUtils.isBlank(message) && value != null) {
            if (value instanceof List) {
                try {
                    @SuppressWarnings("unchecked")
                    List<KeyValueBean> list = (List<KeyValueBean>) value;
                    for (KeyValueBean kvb : list) {
                        messageList.add(kvb.getValue());
                    }
                } catch (Exception e) {
                    LogUtil.errorLog(ValidMessageTag.class.getName(), "doStartTag", e);
                }
            }
        } else {
            RequestContext requestContext = new RequestContext((HttpServletRequest) pageContext.getRequest());
            String tempMessage = requestContext.getMessage(message);
            if (StringUtils.isNotBlank(tempMessage)) {
                messageList.add(tempMessage);
            } else {
                messageList.add(message);
            }
        }

        try {
            if (messageList != null) {
                JspWriter out = this.pageContext.getOut();
                for (String msg : messageList) {
                    msg = replaceParam(msg);

                    StringBuffer sb = new StringBuffer();
                    createErrorMsg(sb, msg);
                    out.print(sb.toString());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_BODY_INCLUDE;
    }
    
    /**
     * 创建错误信息
     * @param sb
     * @param msg
     */
    private void createErrorMsg(StringBuffer sb, String msg) {
        sb.append("<span class=\"Validform_checktip Validform_wrong\" >");
        sb.append(msg);
        sb.append("</span>");
    }

    private String replaceParam(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            if (msg.contains("{label}")) {
                msg = msg.replace("{label}", label == null ? "" : label.trim());
            }
            if (msg.contains("{0}")) {
                if (StringUtils.isNotBlank(params)) {
                    String[] paramList = getParamList();
                    for (int i = 0; i < paramList.length; i++) {
                        msg = msg.replace("{" + i + "}", paramList[i] == null ? "" : paramList[i].trim());
                    }
                }
            }
        }
        return msg;
    }

    private String[] getParamList() {
        String[] paramList = null;
        if (StringUtils.isNotBlank(params)) {
            if (params.contains(",")) {
                paramList = params.split(",");
            } else {
                paramList = new String[] { params };
            }
        }

        return paramList;
    }

    @Override
    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

}
