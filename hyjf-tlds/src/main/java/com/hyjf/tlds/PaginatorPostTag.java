package com.hyjf.tlds;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.codehaus.plexus.util.StringUtils;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetSessionOrRequestUtils;

/**
 * 
 * 类描述：分页栏标签
 * 
 * @version 1.0
 */
public class PaginatorPostTag extends TagSupport {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    // 页码栏ID
    private String id = Paginator.DEFAULT_PAGINATOR_BAR_ID;

    // 页码属性ID
    private String hidden;

    // 分页Form的Action
    private String action;

    // 分布属性集合
    private Paginator paginator;

    // 当前页数
    private String paginatorValue;

    public String getPaginatorValue() {
        return paginatorValue;
    }

    public void setPaginatorValue(String paginatorValue) {
        this.paginatorValue = paginatorValue;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    /**
     * 自定义标签开始
     */
    public int doStartTag() throws JspTagException {
        return EVAL_PAGE;
    }

    /**
     * 自定义标签结束
     */
    public int doEndTag() throws JspTagException {
        try {
            JspWriter out = this.pageContext.getOut();
            out.print(createPaginator());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    /**
     * 创建分页栏
     * 
     * @return
     */
    private String createPaginator() {
    	StringBuffer sb = new StringBuffer("");
    	if(paginator != null) {
            String paginatorValue = "1";
            String defaultPaginatorValue = this.paginatorValue;
            if (StringUtils.isNotEmpty(defaultPaginatorValue)) {
                defaultPaginatorValue = GetSessionOrRequestUtils.getRequest().getParameter(Paginator.DEFAULT_PAGINATOR_PAGE_NAME);
            }
            if (StringUtils.isNotEmpty(defaultPaginatorValue)) {
                paginatorValue = defaultPaginatorValue;
            }

            sb = new StringBuffer("<div class=\"pagination-info\">");
            // 当前页记录范围
            sb.append("<span class=\"hidden-xs\">显示第 ").append(paginator.getStartRow()).append(" 至 ").append(paginator.getEndRow()).append(" 项结果，共 ").append(paginator.getTotalCount()).append(" 项</span>");
            if (StringUtils.isEmpty(this.hidden)) {
                // 分页页码属性
                sb.append("<input type=\"hidden\" id=\"").append(Paginator.DEFAULT_PAGINATOR_PAGE_ID).append("\" name=\"").append(Paginator.DEFAULT_PAGINATOR_PAGE_NAME).append("\" value=\"")
                        .append(paginatorValue).append("\" />");
            }
            // 分布栏
            sb.append("<ul id=\"").append(this.id).append("\" class=\"pagination pagination-sm\">");

            // 首页
            this.addButton(sb, "首页", "first", 0, paginator.isFirstPage());
            // 上一页
            this.addButton(sb, "上一页", "prev", paginator.getPrePage(), paginator.isFirstPage());
            // 页码栏
            int page = paginator.getPage();
            Integer[] sliders = paginator.getSlider();
            for (int number : sliders) {
                this.addButton(sb, null, number == page ? "page active" : "page hidden-xs", number, false);
            }
            // 下一页
            this.addButton(sb, "下一页", "next", paginator.getNextPage(), paginator.isLastPage());
            // 末页
            this.addButton(sb, "末页", "last", paginator.getTotalPages(), paginator.isLastPage());
            sb.append("<li><input type=\"number\" placeholder=\"页码\" value=\"").append(page).append("\" class=\"hidden-xs\"/></li>");
            sb.append("</ul></div><script type=\"text/javascript\">");
            // javascript脚本
            sb.append("$(\"#").append(id).append(" [href]\").click(function() { return window.Page && Page.coverLayer('正在加载页面，请稍候...'),!$(\"#").append(this.hidden != null ? this.hidden : Paginator.DEFAULT_PAGINATOR_PAGE_ID)
                    .append("\").val($(this).attr(\"href\")).closest(\"form\").attr(\"method\", \"post\")");
            // 自定义分页Action
            if (StringUtils.isNotEmpty(action)) {
                sb.append(".attr(\"action\", \"").append(action).append("\")");
            } // Endif
            sb.append(".submit(); }),").append("$(\"#").append(id)
                    .append(" input\").change(function(){(!$.isNumeric(this.value)||this.value<1)&&(this.value=1)}).keydown(function(event) { return code=event.keyCode,code!=13?code==46||(code>7&&code<10)||(code>36&&code<41)||(code>47&&code<58)||(code>95&&code<106):$.isNumeric(this.value) && (window.Page && Page.coverLayer('正在加载页面，请稍候...'),$(\"#");
            sb.append(this.hidden != null ? this.hidden : Paginator.DEFAULT_PAGINATOR_PAGE_ID).append("\").val(this.value).closest(\"form\").attr(\"method\", \"post\")");
            // 自定义分页Action
            if (StringUtils.isNotEmpty(action)) {
                sb.append(".attr(\"action\", \"").append(action).append("\")");
            } // Endif
            sb.append(".submit()); }),").append("$(\".fn-Prev\").click(function() { return !$(\"#").append(this.id).append(" .prev a\").click(); }),")
                    .append("$(\".fn-Next\").click(function() { return !$(\"#").append(this.id).append(" .next a\").click(); })");
            sb.append("</script>");

    	}
    	return sb.toString();
    }

    /**
     * 添加分页按钮
     */
    private void addButton(StringBuffer sb, String text, String type, int number, boolean isDisable) {
        sb.append("<li class=\"");
        // 按钮样式
        if (StringUtils.isNotEmpty(type)) {
            sb.append(type);
        }
        // 是否禁用
        if (isDisable) {
            sb.append(" disabled");
        }
        sb.append("\"><a");
        // 页码
        if (!isDisable && !"page active".equals(type)) {
            sb.append(" href=\"").append(number).append("\"");
        }
        // 按钮名称
        sb.append(">").append(text == null ? number : text).append("</a></li>");
    }

}
