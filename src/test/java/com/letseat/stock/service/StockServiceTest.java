package com.letseat.stock.service;

import com.letseat.stock.domain.Stock;
import com.letseat.stock.facade.OptimisticLockStockFacade;
import com.letseat.stock.repository.StockRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private OptimisticLockStockFacade optimisticLockStockFacade;

    @Autowired
    private StockRepository stockRepository;

    @AfterEach
    public void after(){
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("동시에 100개 재고감소 로직 요청")
    void 동시에_100개의_요청() throws InterruptedException {
        //given
        Stock stock = stockRepository.save(new Stock(1L, 100L));
        int threadCount = 100;
        // ExecutorService는 비동기로 실행하는 작업을 단순화하여 사용할 수 있게하는 자바 api
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // CountDownLatch는 다른 쓰레드에서 수행중인 작업이 완료될때까지 대기할 수 있도록 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(()->{
                try {
                    optimisticLockStockFacade.decrease(stock.getId(),1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        //when
        //then
        Stock savedStock = stockRepository.findById(stock.getId()).orElseThrow();
        assertThat(savedStock.getQuantity()).isEqualTo(0);
    }

    @Test
    @DisplayName("재고가 없을시 재고감소 로직 요청시 예외가 일어난다.")
    void decreaseStockZero() {
        //given
        Stock stock = stockRepository.save(new Stock(1L, 100L));
        //when
        //then
        assertThatThrownBy(() -> optimisticLockStockFacade.decrease(stock.getId(), 101L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("재고 수량이 부족합니다.");
    }

    @Test
    @DisplayName("재고가 있을시 재고감소 로직 요청시 재고가 감소하는지 확인한다.")
    void decreaseStockDecrease99() throws InterruptedException {
        //given
        Stock stock = stockRepository.save(new Stock(1L, 100L));
        //when
        optimisticLockStockFacade.decrease(stock.getId(), 99L);
        Stock savedStock = stockRepository.findById(stock.getId()).orElseThrow();
        //then
        assertThat(savedStock.getQuantity()).isEqualTo(1L);

    }
}