package com.hyjf.tlds;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.codehaus.plexus.util.StringUtils;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.AdminSystem;

/**
 * 
 * 类描述：菜单标签
 * 
 * @version 1.0
 */
public class MainMenuTag extends TagSupport {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * URL前缀
     */
    private String urlPrefix;

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
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
    @SuppressWarnings("unchecked")
    public int doEndTag() throws JspTagException {
        try {
            JspWriter out = this.pageContext.getOut();
            StringBuffer sb = new StringBuffer("");

            String fid = String.valueOf(SessionUtils.getSession(CustomConstants.FUNCTION_ID));
            List<AdminSystem> menuInfo = (List<AdminSystem>) SessionUtils.getSession(CustomConstants.MAIN_MENU_TREE);

            if (Validator.isNull(fid) && menuInfo != null && menuInfo.size() > 0) {
                // 默认第一个菜单被选中
                fid = menuInfo.get(0).getMenuUuid();
            } // Endif
            this.createMenu(sb, menuInfo, fid, true);
            out.print(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    /**
     * 创建菜单
     * 
     * @return
     */
    private void createMenu(StringBuffer sb, List<AdminSystem> list, String funId, boolean root) {
        if (list != null && list.size() > 0) {
            if (root) {
                // 导航菜单标题栏
                sb.append("<ul class=\"main-navigation-menu\">");
            } else {
                sb.append("<ul class=\"sub-menu\">");
            } // Endif

            boolean hasChild;
            for (AdminSystem entity : list) {
                // 是否有子菜单
                hasChild = entity.getMenuTreeClild() != null && !entity.getMenuTreeClild().isEmpty();

                sb.append("<li");
                if (entity.getMenuUuid().equals(funId)) {
                    sb.append(" class=\"active\"");
                } // Endif
                sb.append("><a href=\"");
                if (StringUtils.isNotEmpty(entity.getMenuUrl())) {
                    // 菜单项的URL
                    sb.append(this.urlPrefix).append(entity.getMenuUrl()).append("?fid=").append(entity.getMenuUuid());
                } else {
                    sb.append("javascript:void(0)");
                } // Endif
                sb.append("\">");
                // 菜单项的图标
                if (root) {
                    sb.append("<div class=\"item-content\"><div class=\"item-media\"><i class=\"").append(entity.getMenuIcon()).append("\"></i></div><div class=\"item-inner\"><span class=\"title\">")
                            .append(entity.getMenuName()).append("</span>");
                    if (hasChild) {
                        sb.append("<i class=\"icon-arrow\"></i>");
                    } // Endif
                    sb.append("</div></div>");
                } else {
                    sb.append("<span class=\"title\">").append(entity.getMenuName()).append("</span>");
                    if (hasChild) {
                        sb.append("<i class=\"icon-arrow\"></i>");
                    } // Endif
                } // Endif
                sb.append("</a>");

                if (hasChild) {
                    // 有子菜单
                    this.createMenu(sb, entity.getMenuTreeClild(), funId, false);
                } // Endif

                sb.append("</li>");

            } // Endfor
            sb.append("</ul>");
        }

    }

}
