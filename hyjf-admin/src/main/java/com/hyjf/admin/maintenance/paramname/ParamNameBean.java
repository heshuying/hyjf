package com.hyjf.admin.maintenance.paramname;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class ParamNameBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 387630498860089653L;
	//查询条件
	private String nameClassSrch;//字典区分

	private String nameCdSrch;//字典编号

	private String nameSrch;//字典名称
	
	//显示字段
	private String nameClass;

	private String nameCd;

	private String name;

	private String other1;

	private String other2;

	private String other3;

	private String sort;

	private String delFlag;

	private String createtime;

	private String updatetime;

	private String createuser;

	private String updateuser;

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

	/**
	 * nameClass
	 * 
	 * @return the nameClass
	 */

	public String getNameClass() {
		return nameClass;
	}

	/**
	 * @param nameClass
	 *            the nameClass to set
	 */

	public void setNameClass(String nameClass) {
		this.nameClass = nameClass;
	}

	/**
	 * nameCd
	 * 
	 * @return the nameCd
	 */

	public String getNameCd() {
		return nameCd;
	}

	/**
	 * @param nameCd
	 *            the nameCd to set
	 */

	public void setNameCd(String nameCd) {
		this.nameCd = nameCd;
	}

	/**
	 * name
	 * 
	 * @return the name
	 */

	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * other1
	 * 
	 * @return the other1
	 */

	public String getOther1() {
		return other1;
	}

	/**
	 * @param other1
	 *            the other1 to set
	 */

	public void setOther1(String other1) {
		this.other1 = other1;
	}

	/**
	 * other2
	 * 
	 * @return the other2
	 */

	public String getOther2() {
		return other2;
	}

	/**
	 * @param other2
	 *            the other2 to set
	 */

	public void setOther2(String other2) {
		this.other2 = other2;
	}

	/**
	 * other3
	 * 
	 * @return the other3
	 */

	public String getOther3() {
		return other3;
	}

	/**
	 * @param other3
	 *            the other3 to set
	 */

	public void setOther3(String other3) {
		this.other3 = other3;
	}

	/**
	 * sort
	 * 
	 * @return the sort
	 */

	public String getSort() {
		return sort;
	}

	/**
	 * @param sort
	 *            the sort to set
	 */

	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * delFlag
	 * 
	 * @return the delFlag
	 */

	public String getDelFlag() {
		return delFlag;
	}

	/**
	 * @param delFlag
	 *            the delFlag to set
	 */

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	/**
	 * createtime
	 * 
	 * @return the createtime
	 */

	public String getCreatetime() {
		return createtime;
	}

	/**
	 * @param createtime
	 *            the createtime to set
	 */

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	/**
	 * updatetime
	 * 
	 * @return the updatetime
	 */

	public String getUpdatetime() {
		return updatetime;
	}

	/**
	 * @param updatetime
	 *            the updatetime to set
	 */

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	/**
	 * createuser
	 * 
	 * @return the createuser
	 */

	public String getCreateuser() {
		return createuser;
	}

	/**
	 * @param createuser
	 *            the createuser to set
	 */

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	/**
	 * updateuser
	 * 
	 * @return the updateuser
	 */

	public String getUpdateuser() {
		return updateuser;
	}

	/**
	 * @param updateuser
	 *            the updateuser to set
	 */

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}

	public String getNameClassSrch() {
		return nameClassSrch;
	}

	public void setNameClassSrch(String nameClassSrch) {
		this.nameClassSrch = nameClassSrch;
	}

	public String getNameCdSrch() {
		return nameCdSrch;
	}

	public void setNameCdSrch(String nameCdSrch) {
		this.nameCdSrch = nameCdSrch;
	}

	public String getNameSrch() {
		return nameSrch;
	}

	public void setNameSrch(String nameSrch) {
		this.nameSrch = nameSrch;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
