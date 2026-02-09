package com.standard.service.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.standard.service.constant.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Hazelcast L2 Caching.
 */
@Configuration
public class HazelcastCacheConfig {

    @Bean
    public Config hazelcastConfig() {
        Config config = new Config();
        config.setInstanceName(AppConstants.HAZELCAST_INSTANCE);

        MapConfig mapConfig = new MapConfig();
        mapConfig.setName(AppConstants.STANDARD_CACHE);
        mapConfig.setTimeToLiveSeconds(3600);

        EvictionConfig evictionConfig = new EvictionConfig();
        evictionConfig.setEvictionPolicy(EvictionPolicy.LRU);
        evictionConfig.setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_PERCENTAGE);
        evictionConfig.setSize(10);

        mapConfig.setEvictionConfig(evictionConfig);
        config.addMapConfig(mapConfig);

        return config;
    }
}
