package com.example.redisson.DeployCoupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {
    private final RedissonClient redissonClient;
    private final int EMPTY = 0;

    public String CodeMaker(String menu, String code){
    	//return "Coupon:" + code + menu;
		return menu;
    }

    public void setCouponQuantity(String key, int quantity){
    	redissonClient.getBucket(key).set(quantity);
    }

    public int availableCoupons(String key){
		return (int) redissonClient.getBucket(key).get();
    }
    
    public void decreaseCouponWithLock(final String key){
        final String keyName = key + "_withLock";
        final RLock lock = redissonClient.getLock(keyName);
        final String threadName = Thread.currentThread().getName();

        try {
            if (!lock.tryLock(1, 3, TimeUnit.SECONDS)){
                return;
            }

            final int quantity = availableCoupons(key);
            if (quantity <= EMPTY){
                log.info("쿠폰 미보유자: {} - 사용 가능한 커피쿠폰은 모두 소진 (수량 : {}개)", threadName, quantity);
                return;
            }

            log.info("쿠폰 보유자: {} - 현재 잔여 커피쿠폰{} 수량 : {}개", threadName, keyName, quantity);
            setCouponQuantity(key, quantity - 1);
        	}
        catch (InterruptedException e){
            e.printStackTrace();
			} 
        finally {
            if (lock != null && lock.isLocked()){
                lock.unlock();
			}
        }
    }

    public void decreaseCouponWithoutLock(final String key) 
    	{
        final String keyName = key + "_withoutLock";
        final String threadName = Thread.currentThread().getName();
        final int quantity = availableCoupons(key);

        if (quantity <= EMPTY) 
        		{
            log.info("threadName : {} / 사용 가능한 커피쿠폰은 모두 소진 (수량 : {}개)", threadName, quantity);
            return;
        		}

        log.info("threadName : {} / 현재 잔여 커피쿠폰{} 수량 : {}개", threadName, keyName, quantity);
        setCouponQuantity(key, quantity - 1);
    	}




}
