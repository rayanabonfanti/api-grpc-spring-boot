package api.grpc.spring.boot.iam.utils;

import api.grpc.spring.boot.iam.domain.orm.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisCache {

    private final RedisTemplate<String, User> redisTemplate;

    public RedisCache(RedisTemplate<String, User> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheUser(String login, User user) {
        ValueOperations<String, User> ops = redisTemplate.opsForValue();
        ops.set(login, user);
    }

    public User getCachedUser(String login) {
        ValueOperations<String, User> ops = redisTemplate.opsForValue();
        return ops.get(login);
    }
}

