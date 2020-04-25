package com.hyjf.app.corner;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.MobileCode;
import com.hyjf.mybatis.model.auto.UserCorner;
import com.hyjf.mybatis.model.auto.Version;

public interface AppCommonService extends BaseService {
	
	  /**
	   * 获取最新版本号
	   * @param versionInfo
	   * @return
	   */
      public Version getNewVersionByType(Integer type);
      /**
       * 存储设备唯一标识
       * @param mobileCodeInfo
       */
      public void insertMobileCode(MobileCode mobileCodeInfo);
      
      /**
       * 通过userid 获取设备唯一标识码
       * @param userid
       * @return
       */
      public MobileCode getMobileCodeByUserId(Integer userid);
      /**
       * 修改用户 设备唯一标识码
       * @param mobileCode
       */
      public void updateMobileCode(MobileCode mobileCode);
      /**
       * 存储用户角标数
       * @param tokenCornerInfo
       */
      public void insertUserCorner(UserCorner userCornerInfo);
      /**
       * 修改用户角标数
       * @param tokenCornerInfo
       */
      public void updateUserCorner(UserCorner userCornerInfo);
      /**
       * 根据用户id 获取用户角标数据
       * @param userid
       * @return
       */
      public UserCorner getUserCornerBySign(String sign);
      /**
       * 获取强制更新版本号
       * @param versionInfo
       * @return
       */
      public Version getVersionByType(Integer type, Integer isupdate, String versionStr);
      
}
