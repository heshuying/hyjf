package com.hyjf.admin.msgpush.template;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.MessagePushTemplate;

/**
 * 推送消息标签
 * 
 * @author lishenqiang
 *
 */
public class MessagePushTemplateBean extends MessagePushTemplate implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;

	/**
	 * 标签类型查询
	 */
	private Integer templateTagIdSrch;
	/**
	 * 标题查询
	 */
	private String templateTitleSrch;
	/**
	 * 消息编码查询
	 */
	private String templateCodeSrch;
	/**
	 * 状态查询
	 */
	private Integer templateStatusSrch;
	/**
	 * 指定的原生界面
	 */
	private String templateActionUrl1;
	/**
	 * 指定的原生界面
	 */
	private String templateActionUrl2;

	private String templateActionUrl3;

	private String ids;

	private List<MessagePushTemplate> recordList;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	public int getPaginatorPage() {
		if (paginatorPage == 0) {
			paginatorPage = 1;
		}
		return paginatorPage;
	}

	public Integer getTemplateTagIdSrch() {
		return templateTagIdSrch;
	}

	public void setTemplateTagIdSrch(Integer templateTagIdSrch) {
		this.templateTagIdSrch = templateTagIdSrch;
	}

	public String getTemplateTitleSrch() {
		return templateTitleSrch;
	}

	public void setTemplateTitleSrch(String templateTitleSrch) {
		this.templateTitleSrch = templateTitleSrch;
	}

	public String getTemplateCodeSrch() {
		return templateCodeSrch;
	}

	public void setTemplateCodeSrch(String templateCodeSrch) {
		this.templateCodeSrch = templateCodeSrch;
	}

	public Integer getTemplateStatusSrch() {
		return templateStatusSrch;
	}

	public void setTemplateStatusSrch(Integer templateStatusSrch) {
		this.templateStatusSrch = templateStatusSrch;
	}

	public String getTemplateActionUrl1() {
		return templateActionUrl1;
	}

	public void setTemplateActionUrl1(String templateActionUrl1) {
		this.templateActionUrl1 = templateActionUrl1;
	}

	public String getTemplateActionUrl2() {
		return templateActionUrl2;
	}

	public void setTemplateActionUrl2(String templateActionUrl2) {
		this.templateActionUrl2 = templateActionUrl2;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public List<MessagePushTemplate> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<MessagePushTemplate> recordList) {
		this.recordList = recordList;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}

	public String getTemplateActionUrl3() {
		return templateActionUrl3;
	}

	public void setTemplateActionUrl3(String templateActionUrl3) {
		this.templateActionUrl3 = templateActionUrl3;
	}
}
