package app.services;

import app.model.dto.ForexDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.math3.analysis.function.Exp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RedisCacheService {

    private static final String ForexKeyPrefix = "forex#";

    private int ExpiryTime;

    private Jedis jedis;

    private ObjectMapper mapper;

    private final Environment env;

    @Autowired
    public RedisCacheService(Environment env) {
        this.env = env;

        try {
            jedis = new Jedis(env.getProperty("redis.hostname"), Integer.parseInt(env.getProperty("redis.port")));
            ExpiryTime = Integer.parseInt(env.getProperty("cacheexpirytime"));
            mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        catch (Exception e) {
            System.err.println("Redis is not running or cannot connect to Redis. Working without cache..");
            jedis = null;
        }
    }

    public void saveForex (String id, ForexDTO forex) {
        if (jedis == null) {
            return;
        }

        String forexContent;
        try {
            forexContent = mapper.writeValueAsString(forex);
            jedis.set(ForexKeyPrefix + id, forexContent);
            jedis.expire(ForexKeyPrefix + id, ExpiryTime);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
        }
        catch (Exception e) {
            return;
        }
    }

    public ForexDTO getForex (String id) {
        if (jedis == null) {
            return null;
        }

        ForexDTO forex = null;
        String forexContent = null;
        try {
            forexContent = jedis.get(ForexKeyPrefix + id);
        }
        catch (Exception e) {
            return null;
        }

        if (forexContent != null && !forexContent.equals("")) {
            try {
                forex = mapper.readValue(forexContent, ForexDTO.class);
            } catch (JsonProcessingException e) {
                System.err.println(e.getMessage());
            }
        }

        return forex;
    }

    public void deleteForex (String id) {
        if (jedis == null) {
            return;
        }

        jedis.del(ForexKeyPrefix + id);
    }
}
