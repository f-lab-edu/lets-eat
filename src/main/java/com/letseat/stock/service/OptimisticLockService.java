package com.letseat.stock.service;

import com.letseat.stock.NotEnoughStock;
import com.letseat.stock.domain.Stock;
import com.letseat.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptimisticLockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long id,Long quantity){
        Stock stock = stockRepository.findByIdWithOptimisticLock(id);
        if (stock.getQuantity() - quantity < 0) {
            throw new NotEnoughStock("재고 수량이 부족합니다.");
        }
        stock.decrease(quantity);
    }
}
