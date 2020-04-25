package com.hyjf.app.corner;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.MobileCode;
import com.hyjf.mybatis.model.auto.MobileCodeExample;
import com.hyjf.mybatis.model.auto.UserCorner;
import com.hyjf.mybatis.model.auto.UserCornerExample;
import com.hyjf.mybatis.model.auto.Version;
import com.hyjf.mybatis.model.auto.VersionExample;

@Service
public class AppCommonServiceImpl extends BaseServiceImpl implements AppCommonService {



	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param mobileCodeInfo
	 * @author Michael
	 */
		
	@Override
	public void insertMobileCode(MobileCode mobileCodeInfo) {
		this.mobileCodeMapper.insertSelective(mobileCodeInfo);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param userid
	 * @return
	 * @author Michael
	 */
		
	@Override
	public MobileCode getMobileCodeByUserId(Integer userid) {
		MobileCodeExample mobileCodeExample = new MobileCodeExample();
		MobileCodeExample.Criteria cri = mobileCodeExample.createCriteria();
		if(userid != null){
			cri.andUserIdEqualTo(userid);
		}
		List<MobileCode> list = mobileCodeMapper.selectByExample(mobileCodeExample);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null ;
	}


	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param mobileCode
	 * @author Michael
	 */
		
	@Override
	public void updateMobileCode(MobileCode mobileCode) {
		this.mobileCodeMapper.updateByPrimaryKeySelective(mobileCode);
	}


	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param type
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Version getNewVersionByType(Integer type) {
		VersionExample example = new VersionExample();
		VersionExample.Criteria cri = example.createCriteria();
		cri.andTypeEqualTo(type);
		example.setOrderByClause(" id desc ");
		List<Version> list = versionMapper.selectByExample(example);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null ;
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param userCornerInfo
	 * @author Michael
	 */
		
	@Override
	public void insertUserCorner(UserCorner userCornerInfo) {
		userCornerMapper.insertSelective(userCornerInfo);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param userCornerInfo
	 * @author Michael
	 */
		
	@Override
	public void updateUserCorner(UserCorner userCornerInfo) {
		userCornerMapper.updateByPrimaryKey(userCornerInfo);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param userid
	 * @return
	 * @author Michael
	 */
		
	@Override
	public UserCorner getUserCornerBySign(String sign) {
		UserCornerExample userCornerExample = new UserCornerExample();
		UserCornerExample.Criteria cri = userCornerExample.createCriteria();
		cri.andSignEqualTo(sign);
		List<UserCorner> list = userCornerMapper.selectByExample(userCornerExample);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null ;
	}
	/**
     * 获取强制更新版本号
     * @param versionInfo
     * @return
     */
    @Override
    public Version getVersionByType(Integer type, Integer isupdate, String versionStr) {
        VersionExample example = new VersionExample();
        VersionExample.Criteria cri = example.createCriteria();
        cri.andTypeEqualTo(type);
        if(isupdate!=null){
            cri.andIsupdateEqualTo(isupdate);
        }
        
        if(versionStr!=null){
            cri.andVersionEqualTo(versionStr);
        }
        example.setOrderByClause(" id desc ");
        List<Version> list = versionMapper.selectByExample(example);
        if(list != null && list.size()>0){
            return list.get(0);
        }
        return null ;
    }
	


	
}
