package com.example.redisson.DeployCoupon;

import com.example.redisson.DeployCoupon.CouponService;
import com.example.redisson.Event.CoffeeCoupon;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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
    private Exception exception;
//    private CoffeeCoupon coupon;
    
    @BeforeEach
    void CoffeeCoupon_Setup() {
        final String Menu = "(ICE)Americano";
        final String code = "code:0001";
        final int quantity = 100;
        final CoffeeCoupon coupon = new CoffeeCoupon(Menu, code, quantity);

        this.couponKey = couponService.CodeMaker(coupon.getMenu() ,coupon.getCoffee_code());
        couponService.setCouponQuantity(this.couponKey, quantity);
    	}
 
       
    @Test
    void 커피쿠폰사용_분산락_적용_테스트() throws InterruptedException {
        final int numberOfThreads = 115;
        final CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);

        List<Thread> threadList = Stream
                .generate(() -> new Thread(new UsingCoupon_Lock(this.couponKey, countDownLatch)))
                .limit(numberOfThreads).collect(Collectors.toList());

        threadList.forEach(Thread::start);
        
        threadList.forEach(a -> {
            try {
                a.join();
        
		    } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        
        assertThat(exception.getClass()).isEqualTo(IllegalMonitorStateException.class);
    }  
    

    private class UsingCoupon_Lock implements Runnable {
        private final String couponKey;
        private final CountDownLatch countDownLatch;

        public UsingCoupon_Lock(String couponKey, CountDownLatch countDownLatch) {
            this.couponKey = couponKey;
            this.countDownLatch = countDownLatch;
        }

    	@Override
        public void run() {
            try {
    		    couponService.decreaseCouponWithLock(this.couponKey);
    	        countDownLatch.countDown();
    	        countDownLatch.await(); 		    
    	    
            } catch (Exception ex) {
                exception = ex;
            }
        }
    }

    @Test
    void 커피쿠폰사용_분산락_미적용_테스트() throws InterruptedException {
        final int numberOfThreads = 115;
        final CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);

        List<Thread> threadList = Stream
                .generate(() -> new Thread(new UsingCoupon_withoutLock(this.couponKey, countDownLatch)))
                .limit(numberOfThreads).collect(Collectors.toList());

        threadList.forEach(Thread::start);
        
        threadList.forEach(a -> {
            try {
                a.join();
            
			} catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        
        assertThat(exception.getClass()).isEqualTo(IllegalMonitorStateException.class);
    }
    
    private class UsingCoupon_withoutLock implements Runnable {
        private final String couponKey;
        private final CountDownLatch countDownLatch;
    		
        public UsingCoupon_withoutLock(String couponKey, CountDownLatch countDownLatch) {
            this.couponKey = couponKey;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                couponService.decreaseCouponWithoutLock(this.couponKey);
                countDownLatch.countDown();	
            
			} catch (Exception ex) {
                exception = ex;
            }
        }
    }    
}

