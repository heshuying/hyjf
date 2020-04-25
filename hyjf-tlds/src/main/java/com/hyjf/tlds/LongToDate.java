package com.hyjf.tlds;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class LongToDate extends TagSupport {  
    private static final long serialVersionUID = -2312310581852395045L;  
    private String value;  
  
    @Override  
    public int doStartTag() throws JspException {  
        if(StringUtils.isEmpty(value)){
            return super.doStartTag();  
        }
        String vv = "" + value;  
        String s = "";
        if (vv.length() == 8) {
        	s = vv.substring(0, 4) + "-" + vv.substring(4, 6) + "-" + vv.substring(6, 8);
        } else if (vv.length() == 6) {
        	s = vv.substring(0, 2) + ":" + vv.substring(2, 4) + ":" + vv.substring(4, 6);
        } else if (vv.length() == 5) {
        	s = "0" + vv.substring(0, 1) + ":" + vv.substring(1, 3) + ":" + vv.substring(3, 5);
        }
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

}
