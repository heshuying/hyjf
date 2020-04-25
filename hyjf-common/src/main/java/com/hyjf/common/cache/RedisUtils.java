package com.hyjf.common.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

/**
 * Redis操作接口
 *
 * @author 加内特
 */
public class RedisUtils {
    private static Logger _log = LoggerFactory.getLogger(RedisUtils.class);

    private static JedisPool pool = null;

    private static ThreadLocal<JedisPool> poolThreadLocal = new ThreadLocal<JedisPool>();

//    public static final int signExpireTime = 86400;

    public static final int signExpireTime = 1209600;

    /**
     * 构建redis连接池
     */
    public static JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxActive(Integer.valueOf(PropUtils.getRedisValue("redis.pool.maxActive")));
            config.setMaxIdle(Integer.valueOf(PropUtils.getRedisValue("redis.pool.maxIdle")));
            config.setMaxWait(Long.valueOf(PropUtils.getRedisValue("redis.pool.maxWait")));
            config.setTestOnBorrow(Boolean.valueOf(PropUtils.getRedisValue("redis.pool.testOnBorrow")));
            config.setTestOnReturn(Boolean.valueOf(PropUtils.getRedisValue("redis.pool.testOnReturn")));
            // 测试环境
            // pool = new JedisPool(config, bundle.getString("redis.ip"),
            // Integer.valueOf(bundle.getString("redis.port")));
            // 线上环境
            pool =
                    new JedisPool(config, PropUtils.getRedisValue("redis.ip"), Integer.valueOf(PropUtils
                            .getRedisValue("redis.port")), 100000, PropUtils.getRedisValue("redis.pool.password"));
        }
        return pool;
    }

    public static JedisPool getConnection() {
        // ②如果poolThreadLocal没有本线程对应的JedisPool创建一个新的JedisPool，将其保存到线程本地变量中。
        if (poolThreadLocal.get() == null) {
            pool = RedisUtils.getPool();
            poolThreadLocal.set(pool);
            return pool;
        } else {
            return poolThreadLocal.get();// ③直接返回线程本地变量
        }
    }

    /**
     * 返还到连接池
     * 
     * @param pool
     * @param redis
     */
	public static void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            pool.returnResource(redis);
        }
    }

    /**
     * 累加值
     * 
     * @param key
     * @return
     */
    public static Long incr(String key) {
        Long value = null;

        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.incr(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return value;
    }

    /**
     * 递减值
     *
     * @param key
     * @return
     */
    public static Long decr(String key) {
        Long value = null;

        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.decr(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return value;
    }

    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public static String get(String key) {
        String value = null;

        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return value;
    }

    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public static byte[] get(byte[] key) {
        byte[] value = null;

        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return value;
    }

    /**
     * 删除数据
     * 
     * @param key
     * @return
     */
    public static Long del(String key) {
        Long value = null;

        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return value;
    }

    /**
     * 删除数据
     * 
     * @param key
     * @return
     */
    public static Long del(byte[] key) {
        Long value = null;

        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return value;
    }

    /**
     * 判断是否存在
     * 
     * @param key
     * @return
     */
    public static Boolean exists(String key) {
        Boolean value = null;

        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return value;
    }

    /**
     * 赋值数据
     * 
     * @param key
     * @param value
     * @param expireSeconds(过期时间，秒)
     * @return value
     */
    public static Long set(String key, String value, int expireSeconds) {
        Long result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            jedis.set(key, value);
            result = jedis.expire(key, expireSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return result;
    }

    /**
     * 设置过期时间
     * 
     * @param key
     * @param expireSeconds(过期时间，秒)
     * @return value
     */
    public static Long expire(String key, int expireSeconds) {
        Long result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.expire(key, expireSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return result;
    }
    
    /**
     * 赋值数据
     * 
     * @param key
     * @return
     */
    public static String set(byte[] key, byte[] value) {
        String result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return result;
    }

    /**
     * 赋值数据
     * 
     * @param key
     * @return
     */
    public static String set(String key, String value) {
        String result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return result;
    }

    /**
     * 赋值数据
     * 
     * @param key
     * @return
     */
    public static Long sadd(String key, String value) {
        Long result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.sadd(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return result;
    }
    
    /**
     * 删除数据
     * 
     * @param key
     * @return
     */
    public static Long srem(String key, String value) {
        Long result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.srem(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return result;
    }

    /**
     * 判断set中是否有值
     * 
     * @param key
     * @return
     */
    public static Boolean sismember(String key, String member) {
        Boolean result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.sismember(key, member);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return result;
    }

    /**
     * 
     * redis链表左侧头部压栈
     * @author renxingchen
     * @param key
     * @param values
     * @return
     */
    public static Long lpush(String key, String... values) {
        Long result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.rpush(key, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }

    /**
     * 
     * redis链表右侧尾部出栈，入临时队列，如果没有值则阻塞
     * @author renxingchen
     * @return
     */
    public static String brpoplpush(String source, String destination, int timeout) {
        String result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.brpoplpush(source, destination, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }

    /**
     * 
     * redis链表右侧尾部出栈
     * @author renxingchen
     * @param key
     * @return
     */
    public static String rpop(String key) {
        String result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.rpop(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }

    /**
     * 
     * 移除特定元素
     * @author renxingchen
     * @param key
     * @param count
     * @param value
     * @return
     */
    public static Long lrem(String key, long count, String value) {
        Long result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.lrem(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }

    /**
     * 
     * 返回列表长度
     * @author renxingchen
     * @param key
     * @return
     */
    public static Long llen(String key) {
        Long result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.llen(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }

    /**
     * 可以在并发中使用的不可重复设置锁
     * @param rediskey
     * @param value
     * @param seconds
     * @return
     */
	public static boolean tranactionSet(String rediskey,String value,int seconds) {
		
		String redisResult = RedisUtils.get(rediskey);
        Jedis jedis = null;
		
        if(StringUtils.isEmpty(redisResult)){
    		try {
                pool = getPool();
                jedis = pool.getResource();
        		
        		jedis.watch(rediskey);
        		
        		redisResult= jedis.get(rediskey);
        		if(StringUtils.isNotEmpty(redisResult)){
        			return false;
        		}
        		
                Transaction tx = jedis.multi();
                
        		tx.setex(rediskey ,seconds ,value);
                List<Object> result = tx.exec();
                if (result == null || result.isEmpty()) {
                    jedis.unwatch();
                    return false;
                }else{
                	return true;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                // 释放redis对象
                pool.returnBrokenResource(jedis);
                // 返还到连接池
                returnResource(pool, jedis);
            }
        }else{
        	return false;
        }
	}
	
	public static boolean tranactionSet(String rediskey,int seconds) {
		String value = GetDate.getCalendar().getTimeInMillis() + GetCode.getRandomCode(5);
		return tranactionSet(rediskey, value, seconds);
	}
	
	public static boolean tranactionSet(String rediskey) {
		String value = GetDate.getCalendar().getTimeInMillis() + GetCode.getRandomCode(5);

		return tranactionSet(rediskey, value, 30);
	}
	
    /**
     * 
     * redis链表左侧头部压栈
     * @param key
     * @param values
     * @return
     */
    public static Long leftpush(String key, String... values) {
        Long result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.lpush(key, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }

    /**
     * 
     * redis链表右侧头部压栈
     * @param key
     * @param values
     * @return
     */
    public static Long rightpush(String key, String... values) {
        Long result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.rpush(key, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }

    /**
     * 
     * redis链表左侧尾部出栈
     * @param key
     * @return
     */
    public static String lpop(String key) {
        String result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.lpop(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }

    /**
     * @author fp
     * 获取当天剩余多少秒
     * @return
     */
    public static int getRemainMiao(){
        Calendar curDate = Calendar.getInstance();
        Calendar tommorowDate = new GregorianCalendar(curDate
                .get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate
                .get(Calendar.DATE) + 1, 0, 0, 0);
        return (int)(tommorowDate.getTimeInMillis() - curDate .getTimeInMillis()) / 1000;
    }

    /**
     *判断是否是否恶意请求
     * @param key
     * @param count 次数
     * @param seconds 秒数
     * @return
     */
    public static boolean isMaliciousRequest(String key,int count,int seconds){

        boolean result = false;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            long loginCount = jedis.incr(key);
            if(loginCount == 1){
                jedis.expire(key,seconds);//第一次将过期时间为seconds秒
            }
            if(loginCount > count){//如果1秒之内登录次数大于5就就将这个IP加入黑名单
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }

    /**
     *
     * redis获取key的剩余时间
     * @param key
     * @return
     */
    public static long ttl(String key) {
        long result = -1L;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.ttl(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }

    // add 汇计划三期 汇计划自动出借(分散出借) liubin 20180515 start
    /**
     * Redis中From队列中的成员移动到To队列中（队头→队尾）
     * @param queueNameFrom
     * @param queueNameTo
     * @return moveCount
     */
    public static long lpoprpush(String queueNameFrom, String queueNameTo) {
        String queueValue;
        long moveCount = 0;//移动成员数
        // from队列 → to队列
        while(true){
            queueValue = RedisUtils.lpop(queueNameFrom);//队头取出成员
            if (queueValue == null) {
                break;//退出循环
            }
            RedisUtils.rightpush(queueNameTo, queueValue);//队尾压入成员
            moveCount += 1;//移动成员数+1
            // 队列中无From成员时
            if (moveCount == 1) {
                _log.info("l[" + queueNameFrom + "]→[" + queueNameTo + "]r" + " : ");
            }
            _log.info("   (" + moveCount + ") " + queueValue);
        }
        return moveCount;
    }
    // add 汇计划三期 汇计划自动出借(分散出借) liubin 20180515 end

    /**
     * 获取数据
     * @param key
     */
    public static <T> T getObj(String key,Class<T> clazz) {
        String value = null;

        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.get(key);
            return JSON.parseObject(value, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            // 释放
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return null;
    }

    /**
     * 赋值数据
     * @param key
     * @param value
     * @return
     */
    public static String setObj(String key, Object value) {
        String result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            result = jedis.set(key, JSONObject.toJSONString(value));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            // 释放
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return result;
    }



    /**
     * 并发情况下保证设置一个值
     * @param key
     * @param value
     */
    public static void redisAdd(String key,String value){

        Jedis jedis = pool.getResource();

        while ("OK".equals(jedis.watch(key))) {
            List<Object> results = null;

            String balance = jedis.get(key);
            BigDecimal bal = new BigDecimal(0);
            if (balance != null) {
                bal =  new BigDecimal(balance);
            }
            BigDecimal val =  new BigDecimal(value);

            Transaction tx = jedis.multi();
            String valbeset = bal.add(val).toString();
            tx.set(key, valbeset);
            results = tx.exec();
            if (results == null || results.isEmpty()) {
                jedis.unwatch();
            } else {
                String ret = (String) results.get(0);
                if (ret != null && ret.equals("OK")) {
                    // 成功后
                    break;
                } else {
                    jedis.unwatch();
                }
            }
        }
    }

    /**
     * 并发情况下保证设置一个值
     * @param key
     * @param value
     */
    public static boolean redisSubstrack(String key,String value){

        Jedis jedis = pool.getResource();
        boolean result = false;

        while ("OK".equals(jedis.watch(key))) {
            List<Object> results = null;

            String balance = jedis.get(key);
            BigDecimal bal = new BigDecimal(balance);
            BigDecimal val = new BigDecimal(value);

            if(val.compareTo(bal)>0){
                return false;
            }

            Transaction tx = jedis.multi();
            String valbeset = bal.subtract(val).toString();
            tx.set(key, valbeset);
            results = tx.exec();
            if (results == null || results.isEmpty()) {
                jedis.unwatch();
            } else {
                String ret = (String) results.get(0);
                if (ret != null && ret.equals("OK")) {
                    // 成功后
                    result = true;
                    break;
                } else {
                    jedis.unwatch();
                }
            }
        }
        return result;
    }

    /**
     * 锁竞争 等待
     * @param key
     * @param token
     * @param waitTimeout 超时毫秒
     * @return
     */
    public static boolean lockWithTimeout(String key, String token, long waitTimeout) {
        JedisPool conn = null;
        Jedis jedis = null;

        long startTime = System.currentTimeMillis();
        long now = 0L;
        boolean isLocked = false;
        try {
            conn = getPool();
            jedis = conn.getResource();

            isLocked = dolock( key, token);
            if (isLocked) {
                return true;
            }

            while (!isLocked) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    return false;
                }
                isLocked = dolock( key, token);
                if (isLocked) {
                    now = System.currentTimeMillis();
                    return true;
                }

                now = System.currentTimeMillis();
                if (now - startTime > waitTimeout) {
                    //time out
                    return false;
                }
            }
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }
        return false;
    }

    private static boolean dolock(String key, String token) {
        boolean acquired = setnx(key, token, 30);

        return acquired;
    }

    /**
     * 删除数据
     *
     * @param key
     * @param token
     * @return
     */
    public static Long release(String key,String token) {
        Long value = null;

        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            if (token.equals(jedis.get(key))) {
                value = jedis.del(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }

        return value;
    }

    /**
     * 分布式锁(比前面那个 可以在并发中使用的不可重复设置锁 性能不知道好多少，
     * PS：前面那个还有BUG，千万不要用，因为 unwatch()是取消所有key的监视 )
     * @param rediskey
     * @param value
     * @param seconds
     * @return
     */
    public static boolean setnx(String rediskey,String value,int seconds) {

        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();

            //判断设置锁的值是否成功 1=成功，0=失败
            if(jedis.setnx(rediskey,value) == 1){
                jedis.expire(rediskey,seconds);
                return true;
            }else{
                return false;
            }

        } catch (Exception e) {
            if (value.equals(jedis.get(rediskey))) {
                jedis.del(rediskey);
            }
            e.printStackTrace();
            return false;
        } finally {
            // 释放redis对象
            pool.returnBrokenResource(jedis);
            // 返还到连接池
            returnResource(pool, jedis);
        }
    }

    public static String getToken() {
        return UUID.randomUUID().toString();
    }
}
