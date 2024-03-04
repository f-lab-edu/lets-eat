package com.letseat.stock.facade;

import com.letseat.stock.NotEnoughStock;
import com.letseat.stock.service.OptimisticLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private final OptimisticLockService optimisticLockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                optimisticLockService.decrease(id, quantity);
                break;
            } catch (NotEnoughStock e) {
                throw e;
            } catch (Exception e) {
                log.info("wait===================================");
                Thread.sleep(50);
            }
        }
    }
}
