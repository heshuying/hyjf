/**
 * Description:汇添金计划安全保障
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.mybatis.model.customize.web.htj;

import java.io.Serializable;

public class DebtPlanQuestionCustomize implements Serializable {

	/** 序列化id */
	private static final long serialVersionUID = -1272179572549203942L;
	/* 问题配置 */
	private String question;
	
	/**构造方法*/
	public DebtPlanQuestionCustomize() {
		super();
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

}
