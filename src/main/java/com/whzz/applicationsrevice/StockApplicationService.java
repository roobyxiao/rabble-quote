package com.whzz.applicationsrevice;

import com.whzz.domain.bo.Stock;
import com.whzz.domain.service.StockDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StockApplicationService {
    private final StockDomainService domainService;

    public List<Stock> findAll() {
        return domainService.findAll();
    }
    public void saveAll(List<Stock> stocks) {
        domainService.saveAll(stocks);
    }

    public boolean stockExists(String code) {
        return domainService.stockExists(code);
    }

    public List<Stock> getListedStocks() {
        return domainService.getListedStocks();
    }

}
