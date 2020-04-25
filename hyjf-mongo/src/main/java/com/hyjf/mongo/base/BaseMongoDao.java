package com.hyjf.mongo.base;

import com.hyjf.mongo.entity.BathUpdateOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * mongodb dao基类,可以继续扩展
 * @author xiaojohn
 *
 * @param <T>
 */
public abstract class BaseMongoDao<T> {

	@Autowired
	protected MongoTemplate mongoTemplate;
	
	/**
	 * 保存
	 * @param t
	 */
	public void insert(T t){
		this.mongoTemplate.insert(t);
	}
	
	public void insert(T t, String collectionName){
		this.mongoTemplate.insert(t, collectionName);
	}
	
	public void save(T t){
		this.mongoTemplate.save(t);
	}
	
	public void save(T t, String collectionName){
		this.mongoTemplate.save(t, collectionName);
	}
	
	public T findOne(Query query){
		return this.mongoTemplate.findOne(query, getEntityClass());
	}

	public List<T> find(Query query){
		return this.mongoTemplate.find(query, getEntityClass());
	}
	
	public T findOne(Query query, String collectionName){
		return this.mongoTemplate.findOne(query, getEntityClass(), collectionName);
	}
	
	public void update(Query query, Update update){
		this.mongoTemplate.upsert(query, update, getEntityClass());
	}

	/**
	 * 未查询到数据不创建新数据、批量更新（上面封装的更新、查询不到数据会插入一条、慎用）
	 * @Authod liushouyi
	 * @param query
	 * @param update
	 */
	public void updateAll(Query query, Update update){
		this.mongoTemplate.updateMulti(query, update, getEntityClass());
	}
	
	public void findAndModify(Query query, Update update, String collectionName){
		this.mongoTemplate.findAndModify(query, update, getEntityClass(), collectionName);
	}
	
	public void setMongoTemplate(MongoTemplate mongoTemplate){
		this.mongoTemplate = mongoTemplate;
	}
	
	protected abstract Class<T> getEntityClass();
	public void delete(T t){
		this.mongoTemplate.remove(t);
	}
	public void deleteBatch(List list){
		for(int i=0;i<list.size();i++){
			this.mongoTemplate.remove(list.get(i));
		}
	}

	/**
	 * 批量修改
	 * @param collName
	 * @param options
	 * @param ordered
	 * @return
	 */
	protected int doBathUpdate(String collName,
									List<BathUpdateOptions> options, boolean ordered) {

		DBCollection dbCollection = mongoTemplate.createCollection(getEntityClass());
		DBObject command = new BasicDBObject();
		command.put("update", collName);
		List<BasicDBObject> updateList = new ArrayList<BasicDBObject>();
		for (BathUpdateOptions option : options) {
			BasicDBObject update = new BasicDBObject();
			update.put("q", option.getQuery().getQueryObject());
			update.put("u", option.getUpdate().getUpdateObject());
			update.put("upsert", option.isUpsert());
			update.put("multi", option.isMulti());
			updateList.add(update);
		}
		command.put("updates", updateList);
		command.put("ordered", ordered);
		CommandResult commandResult = dbCollection.getDB().command(command);
		return Integer.parseInt(commandResult.get("n").toString());
	}


}
