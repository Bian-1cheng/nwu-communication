package com.bian.nwucommunication.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Service
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;


    /** * 以count为步长查找符合pattern条件的keys * * @param redisTemplate 指定redis * @param pattern 匹配条件 * @param count 一次在count条记录中match符合pattern条件的记录。若count<=0,使用1000 * @return Set<String> 若limit<= 0,返回所有;否则返回查找结果 */
    public Set<String> scanKeys(RedisTemplate<String, Object> redisTemplate, String pattern, int count) {

//        log.info("pattern:{}, count:{}", pattern, count);
        return redisTemplate.execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
                Set<String> tmpKeys = new HashSet<>();
                ScanOptions options;
                if (count <= 0) {
                    options = ScanOptions.scanOptions().match(pattern+"*").count(1000).build();
                } else {
                    options = ScanOptions.scanOptions().match(pattern+"*").count(count).build();
                }
                // 迭代一直查找，直到找到redis中所有满足条件的key为止(cursor变为0为止)
                Cursor<byte[]> cursor = connection.scan(options);
                while (cursor.hasNext()) {
                    tmpKeys.add(new String(cursor.next()));
                }
                cursor.close();
                return tmpKeys;
            }
        });
    }

}
