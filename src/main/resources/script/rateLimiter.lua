-- KEYS[1] 是计数key，比如 "ip:127.0.0.1:requests"
-- KEYS[2] 是过期时间key，实际上在这个例子中我们不需要它，但为了与RedisTemplate的API保持一致，可以留空
-- ARGV[1] 是请求的限制次数，比如 100
-- ARGV[2] 是过期时间（秒），比如 60

local limit = tonumber(ARGV[1])
local expiry = tonumber(ARGV[2])
local counter = redis.call('INCR', KEYS[1])

if counter > limit then
    -- 如果超过限制，可以记录一个事件或返回true表示被限制
    -- 这里我们直接返回true
    return true
else
    -- 重置过期时间，确保key在expiry秒后过期
    redis.call('EXPIRE', KEYS[1], expiry)
    -- 没有超过限制，返回false或null表示请求被允许
    return false
end