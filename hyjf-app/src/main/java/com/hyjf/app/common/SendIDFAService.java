package com.hyjf.app.common;

import java.util.List;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.I4;
import com.hyjf.mybatis.model.auto.Idfa;
import com.hyjf.mybatis.model.auto.MobileCode;
import com.hyjf.mybatis.model.auto.Version;

public interface SendIDFAService extends BaseService {
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
       * 查询IDFA
       * @param mobileCode
       */
      public List<Idfa> selectIDFA(String idfa);
      
      /**
       * 更新IDFA
       * @param mobileCode
       */
      public Integer updateIDFA(Idfa idfa);
      
      /**
       * 插入IDFA
       * @param mobileCode
       */
      public Integer insertIDFA(Idfa idfa);
      
      /**
       * 删除IDFA
       * @param mobileCode
       */
      public Integer deleteIDFA(Idfa idfa);
      
      /**
       * 删除一年以上的数据
       * @param mobileCode
       */
      public Integer deleteIDFAByOneYear(Integer timeInt);
      

      /**
       * 查询I4
       * @param mobileCode
       */
      public List<I4> selectI4List(String idfa);
      
}
