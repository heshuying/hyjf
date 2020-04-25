package com.hyjf.tlds;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class DateSetTag extends TagSupport {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -3024423180500528013L;

    private String value;

    private String var;

    @Override
    public int doStartTag() throws JspException {
        String vv = "" + value;
        long time = Long.valueOf(vv) * 1000;
        Date date = new Date(time);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String s = dateformat.format(date);
        try {
            date = dateformat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pageContext.setAttribute(var, date.getTime());
        return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setVar(String var) {
        this.var = var;
    }

}
