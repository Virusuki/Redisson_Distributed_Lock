package com.example.redisson.DeployCoupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {
    private final RedissonClient redissonClient;
    private final int Complete = 0;
//    RBucket <Object> bucket;
    
    public String CodeMaker(String menu, String code) {
      return menu;
    }

    public void setCouponQuantity(String key, int quantity) {
    	redissonClient.getBucket(key).set(quantity);
    }

    public int availableCoupons(String key) {
      return (int) redissonClient.getBucket(key).get();
    }
    
    public void decreaseCouponWithLock(final String key) {
        boolean usingLock; 
    	long waitTime = 1L;
    	long leaseTime = 3L;
        final String keyName = key + "_withLock";
        final RLock lock = redissonClient.getLock(keyName);
        final String threadName = Thread.currentThread().getName();

        try {
            usingLock = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
            if (!usingLock) {
                return;
            }
			
            final int quantity = availableCoupons(key);
            if (quantity <= Complete) {
            	  log.info("{} - 사용 가능한 커피쿠폰은 모두 소진 (수량 : {}개)", threadName, quantity);
            	  return;
            }
            
            log.info("쿠폰 발급 중: {} - 현재 잔여 커피쿠폰{} 수량 : {}개", threadName, keyName, quantity);
            setCouponQuantity(key, quantity - 1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();       
        } finally {
            if (lock.isLocked()) {
                 lock.unlock();
            }
        }
    }
    
    public void decreaseCouponWithoutLock(final String key) {
        final String keyName = key + "_withoutLock";
        final String threadName = Thread.currentThread().getName();
        final int quantity = availableCoupons(key);

        if (quantity <= Complete) {
            log.info("{} / 사용 가능한 커피쿠폰은 모두 소진 (수량 : {}개)", threadName, quantity);
            return;
        }

        log.info("쿠폰 발급 중: {} / 현재 잔여 커피쿠폰{} 수량 : {}개", threadName, keyName, quantity);
        setCouponQuantity(key, quantity - 1);
    }
}
