package com.hyjf.batch.onetime.cleanrediskey;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hyjf.common.cache.RedisUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 清除redis永久key
 * @author xiaojohn
 *
 */
public class CleanRedisKeyTask {
	
	Logger _log = LoggerFactory.getLogger(CleanRedisKeyTask.class);
	
	/** 运行状态 */
	private static int isRun = 0;
	

	public static JedisPool pool = RedisUtils.getPool();
	
	private static String godsign = "c916f191-f7c2-4e43-9a7d-0494173f31b97oMb316";
	private static String appkeytempPath = "D:\\tmpp\\apprediskey.txt";

	/**
	 * 调用任务实际方法接口
	 */
	public void run() {
		process();
	}

	/**
	 * 清除redis永久key
	 *
	 * @return
	 */
	private boolean process() {
		if (isRun == 0) {
			_log.info("清除redis永久key开始... ");
			
			isRun = 1;
			try {

//				scanALlKey();
//				
//				
//				expireAppKey();
				
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
			
			_log.info("清除redis永久key 结束... ");
			
		}else{
			
			_log.info("清除redis永久key  正在运行... ");
		}
		
		return false;
	}
	
	/**
	 * 扫描所有app key，写到文件中
	 * @param key
	 * @param value
	 */
	private void scanALlKey(){/*

		Jedis jedis = pool.getResource();
		File appfilePath= new File(appkeytempPath);
		
		// 写入文件
		try {
			List<String> keys = new ArrayList<String>();
			FileUtils.writeLines(appfilePath, keys);
			_log.info("清除临时文件成功  "+appkeytempPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		String cursor = ScanParams.SCAN_POINTER_START;
		int total = 0;
		int apptotal = 0;
		ScanParams sp = new ScanParams();
		sp.count(100);
		
		do {
			ScanResult<String> sResult = jedis.scan(cursor,sp);
			
			cursor = sResult.getStringCursor();
			List<String> keys = sResult.getResult();
			List<String> appkeys = new ArrayList<String>();
			
			for (String key : keys) {
//				_log.info(key); 32+4+3
				total = total+1;
				if (key.length() == 43 && StringUtils.contains(key, "-")) {
					apptotal = apptotal+1;
					appkeys.add(key);
					
//					//TODO:delte
//					System.out.println(key+"    "+cursor);
				}
			}
			
			// 写入文件
			try {
				FileUtils.writeLines(appfilePath, appkeys, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} while (!"0".equals(cursor));

		_log.info(total+" keys "+apptotal);
		jedis.close();
		
	*/}
	
	/**
	 * 批量设置有效期
	 * @param key
	 * @param value
	 */
	private void expireAppKey(){

		Jedis jedis = pool.getResource();
		File appfilePath= new File(appkeytempPath);
		int apptotal = 0;
		
		try {

			_log.info("开始遍历所有APPKEY  "+appkeytempPath);
			LineIterator ll = FileUtils.lineIterator(appfilePath);
			
			while (ll.hasNext()) {
				String key = (String) ll.next();
				long ttl = jedis.ttl(key);
				if(ttl == -1 && !godsign.equals(key)) {
					apptotal = apptotal+1;
					long result = jedis.expire(key, 24*60*60);
//					System.out.println(key+"   "+jedis.get(key));
				}
			}
			
			_log.info("processed keys "+apptotal);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
