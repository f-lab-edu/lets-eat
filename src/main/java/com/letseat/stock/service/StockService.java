package com.letseat.stock.service;

import com.letseat.stock.domain.Stock;
import com.letseat.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long id,Long quantity){
        Stock stock = stockRepository.findById(id).orElseThrow(()->new RuntimeException("등록된 상품을 찾을수 없습니다."));
        stock.decrease(quantity);
    }
}
