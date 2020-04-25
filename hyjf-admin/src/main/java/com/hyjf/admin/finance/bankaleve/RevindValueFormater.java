package com.hyjf.admin.finance.bankaleve;

/**
 * 撤消、冲正
 * Created by cuigq on 2018/1/23.
 */
public class RevindValueFormater implements IValueFormater {

    public String format(Object object) {
        if(object instanceof Integer){
            Integer revind = (Integer) object;
            return revind == 1 ? "已撤销/冲正" : "";
        }
        return "";
    }
}
