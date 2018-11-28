package com.yb.fish.lock;

import com.yb.fish.appcontext.SpringContextUtil;
import com.yb.fish.constant.FishContants;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@DependsOn("springContextUtil")
public class RedissionLock implements InterfaceRedisLock {
    Redisson redisson = null;

    @PostConstruct
    public void init() {
//   注入依赖项目的redis数据源，依赖配置文件，和redisFactory都可以
//        JedisConnectionFactory redisConnection = SpringContextUtil.getBean(JedisConnectionFactory.class);
//        if (null == redisConnection) {
//            new RuntimeException("redisConnection error...");
//        }
//        redisson = this.getRedisson(redisConnection);
    }

//    private Redisson getRedisson(JedisConnectionFactory redisConnection) {
//        Config config = new Config();
//        StringBuilder sblHostAndPort = new StringBuilder();
//        boolean isCluster = redisConnection.isUseCluster();
//        boolean usePool = redisConnection.getUsePool();
//        boolean isSharding = redisConnection.isUseSharding();
//        //cluster
//        if (isCluster && usePool) {
//            Set<HostAndPort> hostAndPorts = redisConnection.getHostAndPorts();
//            for (HostAndPort hostAndPort : hostAndPorts) {
//                String hostAndPortStr = FishContants.REDIS_PROTAL + hostAndPort.getHost() + FishContants.COLON + hostAndPort.getPort();
//                sblHostAndPort = sblHostAndPort.length() > FishContants.ZERO ? sblHostAndPort.append(FishContants.COMMA).append(FishContants.REDIS_PROTAL).append(hostAndPortStr) : sblHostAndPort.append(hostAndPortStr);
//            }
//            config.useClusterServers().setScanInterval(FishContants.TWO_THOUSAND).addNodeAddress(sblHostAndPort.toString().split(FishContants.COMMA));
//            return (Redisson) Redisson.create(config);
//            //shard
//        } else if (isSharding && usePool) {
//            List<JedisShardInfo> jedisShardInfos = redisConnection.getShards();
//            for (JedisShardInfo jedisShardInfo : jedisShardInfos) {
//                String hostAndPortStr = FishContants.REDIS_PROTAL + jedisShardInfo.getHost() + FishContants.COLON + jedisShardInfo.getPort();
//                sblHostAndPort = sblHostAndPort.length() > FishContants.ZERO ? sblHostAndPort.append(FishContants.COMMA).append(hostAndPortStr) : sblHostAndPort.append(hostAndPortStr);
//            }
//            config.useClusterServers().setScanInterval(FishContants.TWO_THOUSAND).addNodeAddress(sblHostAndPort.toString().split(FishContants.COMMA));
//            return (Redisson) Redisson.create(config);
//            //single
//        } else if (usePool) {
//            sblHostAndPort.append(FishContants.REDIS_PROTAL)
//                    .append(redisConnection.getHostName())
//                    .append(FishContants.COLON)
//                    .append(redisConnection.getPort());
//            System.out.println(sblHostAndPort.toString());
//            config.useSingleServer().setAddress(sblHostAndPort.toString());
//            return (Redisson) Redisson.create(config);
//        }
//        return (Redisson) Redisson.create();
//    }

    /**
     * @param waitTime  尝试加锁最多等待时间
     * @param leaseTime 上锁以后解锁时间
     */
    @Override
    public boolean lock(String bussinessKey, int waitTime, int leaseTime) throws InterruptedException {
        RLock lock = redisson.getLock(bussinessKey);
        return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
    }

    /**
     * @param waitTime 上锁以后解锁时间(避免产生死锁)
     */
    @Override
    public boolean lock(String bussinessKey, int waitTime) throws InterruptedException {
        RLock lock = redisson.getLock(bussinessKey);
        return lock.tryLock(waitTime, TimeUnit.SECONDS);
    }

    @Override
    public void lock(String bussinessKey) {
        RLock lock = redisson.getLock(bussinessKey);
        lock.lock();
    }

    @Override
    public void unLock(String bussinessKey) {
        RLock lock = redisson.getLock(bussinessKey);
        lock.unlock();
    }
}
