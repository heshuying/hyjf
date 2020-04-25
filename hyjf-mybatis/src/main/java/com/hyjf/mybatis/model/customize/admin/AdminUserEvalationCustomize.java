
/**
 * Description:用户列表前端显示所用po
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
    
package com.hyjf.mybatis.model.customize.admin;

/**
 * @author 王坤
 */

public class AdminUserEvalationCustomize {
    
    //问题
    public String question;
    //答案
    public String answer;
    //分数
    public String score;
    
    /**
     * 构造方法不带参数
     */
        
    public AdminUserEvalationCustomize() {
        super();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

   
}

    