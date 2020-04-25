/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.dao;

import com.hyjf.mongo.base.BaseMongoDao;
import com.hyjf.mongo.entity.BathUpdateOptions;
import com.hyjf.mongo.hgdatareport.entity.CertBorrowEntity;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 国家互联网应急中心    CERT 标的信息上报
 * @author sss
 * @version BaseHgDataReportEntity, v0.1 2018/6/27 10:06
 */
@Repository
public class CertBorrowDao extends BaseMongoDao<CertBorrowEntity> {
    @Override
    protected Class<CertBorrowEntity> getEntityClass() {
        return CertBorrowEntity.class;
    }

    /**
     * 保存集合
     */
    public void saveAll(List<CertBorrowEntity> list) {
        mongoTemplate.insertAll(list);
    }

    /**
     * 上报的用户数量
     * @return
     */
    public Long getCertUserCount() {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("isUserInfo").is("0");
        query.addCriteria(criteria);
        return mongoTemplate.count(query,"ht_cert_borrow");
    }

    /**
     * 等待上报的标的的数量
     * @return
     */
    public Long getCertBorrowCount() {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("isUserInfo").is("1");
        criteria.and("isScatter").is("0");
        query.addCriteria(criteria);
        return mongoTemplate.count(query,"ht_cert_borrow");
    }

    /**
     * 查未上报的标的借款人
     * @return
     */
    public List<CertBorrowEntity> getCertBorrowUsers() {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("isUserInfo").is("0");
        query.addCriteria(criteria);
        query.limit(1000);
        query.with(new Sort(Sort.Direction.ASC, "borrowNid"));
        return mongoTemplate.find(query,getEntityClass());
    }

    /**
     * 查未上报的标的
     * @return
     */
    public List<CertBorrowEntity> getCertBorrow() {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("isUserInfo").is("1");
        criteria.and("isScatter").is("0");
        query.addCriteria(criteria);
        query.limit(1000);
        query.with(new Sort(Sort.Direction.ASC, "borrowNid"));
        return mongoTemplate.find(query,getEntityClass());
    }

    /**
     * 批量修改状态
     * @param param
     */
    public void updateAll(List<Map<String,String>> param) {
        List<BathUpdateOptions> result = new ArrayList<>();
        for (Map<String,String> item:param) {
            BathUpdateOptions options = new BathUpdateOptions();
            Query query = new Query();
            //查询条件
            query.addCriteria(Criteria.where("borrowNid").is(item.get("borrowNid")));
            options.setQuery(query);
            //mongodb 默认是false,只更新找到的第一条记录，如果这个参数为true,就把按条件查出来多条记录全部更新。
            options.setMulti(false);
            Update update = new Update();
            //更新内容
            update.set(item.get("key"), item.get("value"));
            options.setUpdate(update);
            result.add(options);
        }
        this.doBathUpdate("ht_cert_borrow",result,false);

    }
}
