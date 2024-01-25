package com.example.redisson.DeployCoupon;

import com.example.redisson.DeployCoupon.CouponService;
import com.example.redisson.Event.CoffeeCoupon;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest

public class CouponServiceTest {
    @Autowired
    private CouponService couponService;
    private String couponKey;
//    private CoffeeCoupon coupon;

    private class UsingCoupon_Lock implements Runnable{
    	private final String couponKey;
    	private final CountDownLatch countDownLatch;

    	public UsingCoupon_Lock(String couponKey, CountDownLatch countDownLatch){
        this.couponKey = couponKey;
        this.countDownLatch = countDownLatch;
        }

    	@Override
    	public void run(){
           couponService.decreaseCouponWithLock(this.couponKey);
           countDownLatch.countDown();
        }
    }
    
    private class UsingCoupon_withoutLock implements Runnable{
        private final String couponKey;
        private final CountDownLatch countDownLatch;
    		
        public UsingCoupon_withoutLock(String couponKey, CountDownLatch countDownLatch){
            this.couponKey = couponKey;
            this.countDownLatch = countDownLatch;
        }

    	@Override
    	public void run(){
            couponService.decreaseCouponWithoutLock(this.couponKey);
            countDownLatch.countDown();
        }
    }    
    
    @BeforeEach
    void CoffeeCoupon_Setup(){
        final String Menu = "(ICE)Americano";
        final String code = "code:0001";
        final int quantity = 10;
        final CoffeeCoupon coupon = new CoffeeCoupon(Menu, code, quantity);

        this.couponKey = couponService.CodeMaker(coupon.getMenu() ,coupon.getCoffee_code());
        couponService.setCouponQuantity(this.couponKey, quantity);
    }
    
    @Test
    void 커피쿠폰사용_분산락_적용_테스트() throws InterruptedException{
        final int numberOfThreads = 15;
        final CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);

        List<Thread> threadList = Stream
                .generate(() -> new Thread(new UsingCoupon_Lock(this.couponKey, countDownLatch)))
                .limit(numberOfThreads)
                .collect(Collectors.toList());

        threadList.forEach(Thread::start);
        countDownLatch.await();
    }  

    
    @Test
    void 커피쿠폰사용_분산락_미적용_테스트() throws InterruptedException{
        final int numberOfThreads = 15;
        final CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);

        List<Thread> threadList = Stream
                .generate(() -> new Thread(new UsingCoupon_withoutLock(this.couponKey, countDownLatch)))
                .limit(numberOfThreads)
                .collect(Collectors.toList());

        threadList.forEach(Thread::start);
    }
}
