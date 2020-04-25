package com.hyjf.admin.manager.config.borrow.projecttype;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.BorrowProjectType;

/**
 * @package com.hyjf.admin.maintenance.BorrowType;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class ProjectTypeBean extends BorrowProjectType implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4520703937401940484L;

	private List<BorrowProjectType> recordList;

	private String modifyFlag;

	/**
	 * modifyFlag
	 * 
	 * @return the modifyFlag
	 */

	public String getModifyFlag() {
		return modifyFlag;
	}

	/**
	 * @param modifyFlag
	 *            the modifyFlag to set
	 */

	public void setModifyFlag(String modifyFlag) {
		this.modifyFlag = modifyFlag;
	}

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 回显数据
	 */
	private List<Map<String, Object>> forBack;

	public List<Map<String, Object>> getForBack() {
		return forBack;
	}

	/**
	 * 接收还款方式
	 */
	private String methodName;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setForBack(List<Map<String, Object>> forBack) {
		this.forBack = forBack;
	}

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

	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public List<BorrowProjectType> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<BorrowProjectType> recordList) {
		this.recordList = recordList;
	}

}
