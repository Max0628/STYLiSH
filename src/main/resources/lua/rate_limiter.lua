-- Get arguments
local now = tonumber(ARGV[1])    -- Current timestamp in milliseconds
local window = tonumber(ARGV[2]) -- Time window in milliseconds
local maxReq = tonumber(ARGV[3]) -- Maximum number of requests allowed in the time window
local key = KEYS[1]              -- Redis key for the rate limiter

-- Remove outdated entries
redis.call("ZREMRANGEBYSCORE", key, 0, now - window) -- Remove entries older than the time window

-- Count current requests
local count = redis.call("ZCARD", key) -- Count the number of requests in the current time window
if count >= maxReq then -- If the count exceeds the maximum allowed requests
    return 0 -- Rate limit exceeded, return 0
end

-- Add current timestamp
redis.call("ZADD", key, now, now) -- Add the current timestamp to the sorted set

-- Set expiration
redis.call("EXPIRE", key, math.ceil(window / 1000)) -- Set the key to expire after the time window

return 1
