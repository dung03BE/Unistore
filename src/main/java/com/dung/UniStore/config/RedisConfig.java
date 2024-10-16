    package com.dung.UniStore.config;


    import org.springframework.cache.CacheManager;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.data.redis.cache.RedisCacheConfiguration;
    import org.springframework.data.redis.cache.RedisCacheManager;
    import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
    import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
    import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
    import org.springframework.data.redis.core.RedisTemplate;
    import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
    import org.springframework.data.redis.serializer.GenericToStringSerializer;
    import org.springframework.data.redis.serializer.RedisSerializationContext;
    import org.springframework.data.redis.serializer.StringRedisSerializer;

    import java.time.Duration;
    import java.util.HashMap;
    import java.util.Map;

    @Configuration
    public class RedisConfig {
        @Bean
        public JedisConnectionFactory redisConnectionFactory() {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
            config.setPassword("123abc");
            JedisClientConfiguration clientConfiguration = JedisClientConfiguration.builder().build();
            return new JedisConnectionFactory(config, clientConfiguration);
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate() {
            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory());
            redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Object.class));
            return redisTemplate;
        }
        @Bean
        public CacheManager cacheManager(JedisConnectionFactory redisConnectionFactory) {
            RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(10))
                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

            Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
            cacheConfigurations.put("products", defaultConfig.entryTtl(Duration.ofMinutes(5)));
            cacheConfigurations.put("users", defaultConfig.entryTtl(Duration.ofHours(1)));

            // Thêm các cấu hình cache khác tại đây nếu cần

            return RedisCacheManager.builder(redisConnectionFactory)
                    .cacheDefaults(defaultConfig)
                    .withInitialCacheConfigurations(cacheConfigurations)
                    .build();
        }
    }