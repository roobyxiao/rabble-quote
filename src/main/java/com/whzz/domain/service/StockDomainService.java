package com.whzz.domain.service;

import com.whzz.domain.bo.Stock;
import com.whzz.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockDomainService {
    private final StockRepository repository;
    private Predicate<Stock> isSHzb = stock -> stock.getCode().startsWith("sh.");
    private Predicate<Stock> isSZzb = stock -> stock.getCode().startsWith("sz.0");
    public void saveAll(List<Stock> stocks) {
        repository.saveAll(stocks);
    }

    public List<Stock> findAll() {
        return repository.findAll();
    }
    public boolean stockExists(String code) {
        return repository.existsById(code);
    }

    public List<Stock> getListedStocks() {
        return repository.findByActive(true);
    }

    //获取沪深主板的股票
    public List<Stock> getHszb() {
        var stocks = repository.findAll();
        return stocks.stream().filter(isSHzb.or(isSZzb)).collect(Collectors.toList());
    }
}
