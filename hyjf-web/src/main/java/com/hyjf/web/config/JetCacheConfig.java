package com.hyjf.web.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alicp.jetcache.anno.CacheConsts;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.alicp.jetcache.anno.support.GlobalCacheConfig;
import com.alicp.jetcache.anno.support.SpringConfigProvider;
import com.alicp.jetcache.embedded.EmbeddedCacheBuilder;
import com.alicp.jetcache.embedded.LinkedHashMapCacheBuilder;
import com.alicp.jetcache.redis.lettuce.RedisLettuceCacheBuilder;
import com.alicp.jetcache.support.FastjsonKeyConvertor;
import com.alicp.jetcache.support.JavaValueDecoder;
import com.alicp.jetcache.support.JavaValueEncoder;
import com.hyjf.common.util.PropUtils;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;

@Configuration
@EnableMethodCache(basePackages = "com.hyjf")
@EnableCreateCacheAnnotation
public class JetCacheConfig {
	
	@Bean
    public RedisClient redisClient(){
        
		RedisURI redisURI1 = RedisURI.builder().withHost(PropUtils.getRedisValue("redis.ip"))
				.withPort(Integer.valueOf(PropUtils.getRedisValue("redis.port")))
				.withPassword(PropUtils.getRedisValue("redis.pool.password")).build();
		RedisClient client = RedisClient.create(redisURI1);
        
        return client;
    }

    @Bean
    public SpringConfigProvider springConfigProvider() {
        return new SpringConfigProvider();
    }

    @Bean
    public GlobalCacheConfig config(SpringConfigProvider configProvider, RedisClient redisClient){
        Map localBuilders = new HashMap();
        EmbeddedCacheBuilder localBuilder = LinkedHashMapCacheBuilder
                .createLinkedHashMapCacheBuilder()
                .keyConvertor(FastjsonKeyConvertor.INSTANCE);
        localBuilders.put(CacheConsts.DEFAULT_AREA, localBuilder);

        Map remoteBuilders = new HashMap();
        RedisLettuceCacheBuilder remoteCacheBuilder = RedisLettuceCacheBuilder.createRedisLettuceCacheBuilder()
                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
                .valueEncoder(JavaValueEncoder.INSTANCE)
                .valueDecoder(JavaValueDecoder.INSTANCE)
                .redisClient(redisClient);
        remoteBuilders.put(CacheConsts.DEFAULT_AREA, remoteCacheBuilder);

        GlobalCacheConfig globalCacheConfig = new GlobalCacheConfig();
        globalCacheConfig.setConfigProvider(configProvider);
        globalCacheConfig.setLocalCacheBuilders(localBuilders);
        globalCacheConfig.setRemoteCacheBuilders(remoteBuilders);
        globalCacheConfig.setStatIntervalMinutes(15);
        globalCacheConfig.setAreaInCacheName(false);
        globalCacheConfig.setHiddenPackages(new String[] {"com.hyjf"});
       

        return globalCacheConfig;
    }
    
}