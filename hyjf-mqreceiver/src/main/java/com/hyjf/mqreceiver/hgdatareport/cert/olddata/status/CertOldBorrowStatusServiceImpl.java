package com.hyjf.mqreceiver.hgdatareport.cert.olddata.status;

import com.hyjf.mongo.hgdatareport.dao.CertBorrowDao;
import com.hyjf.mongo.hgdatareport.entity.CertBorrowEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mybatis.mapper.auto.CertBorrowMapper;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.CertBorrow;
import com.hyjf.mybatis.model.auto.CertBorrowExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author nxl
 */

@Service
public class CertOldBorrowStatusServiceImpl extends BaseHgCertReportServiceImpl implements CertOldBorrowStatusService {

    @Autowired
    private CertBorrowDao certBorrowDao;
    @Autowired
    CertBorrowMapper certBorrowMapper;

    /**
     * 查找Mongon中ht_cert_borrow标的信息
     *
     * @return
     */
    @Override
    public List<CertBorrowEntity> getCertBorrowEntityList() {
        //查找用户信息报送完毕的数据
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("isUserInfo").is("1");
        criteria.and("isScatter").is("1");
        criteria.and("isStatus").is("0");
        query.addCriteria(criteria);
        List<CertBorrowEntity> certBorrowEntityList = certBorrowDao.find(query);
        return certBorrowEntityList;
    }
    //

    /**
     * 查找数据库里待上报散标状态的数据
     *
     * @return
     */
    @Override
    public List<CertBorrow> insertCertBorrowStatusList() {
        CertBorrowExample example = new CertBorrowExample();
        CertBorrowExample.Criteria cra = example.createCriteria();
        //1 代表已上报,0:未上报,99:上报错误
        // 查找用户信息已上传并且散标状态未上报的数据
        cra.andIsUserInfoEqualTo(1);
        //散标数据已上报
        cra.andIsScatterEqualTo(1);
        //散标状态未上报
        cra.andIsStatusEqualTo(0);
        example.setLimitStart(0);
        example.setLimitEnd(3000);
        List<CertBorrow> listStatus = certBorrowMapper.selectByExample(example);
        return listStatus;
    }

    //测试用,查找三千个标的编号

    /**
     * 以下为测试
     * @return
     */
    @Override
    public List<CertBorrow> getCertBorrowStatusList1(){
        BorrowExample example = new BorrowExample();
        BorrowExample.Criteria cra = example.createCriteria();
        example.setLimitStart(0);
        example.setLimitEnd(3000);
        List<Borrow> listBorrow = borrowMapper.selectByExample(example);
        List<CertBorrow> certBorrowList =convertBeanList(listBorrow,CertBorrow.class);

        return certBorrowList;
    }
    public static <S, T> List<T> convertBeanList(List<S> sources, Class<T> clazz) {
        return sources.stream().map(source -> convertBean(source, clazz)).collect(Collectors.toList());
    }
    public static <S, T> T convertBean(S s, Class<T> clazz) {
        if (s == null) {
            return null;
        }
        try {
            T t = clazz.newInstance();
            BeanUtils.copyProperties(s, t);
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("拷贝属性异常");
        }
    }
    //测试结束end
}
