package com.whzz.web;

import com.whzz.applicationsrevice.factor.FactorService;
import com.whzz.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping(value = "/factor")
@AllArgsConstructor
public class FactorController {
    private final FactorService factorService;

    @RequestMapping(value = "/all")
    public void updateAll() {
        factorService.calculateFactors(Constants.START_DATE);
    }

    @RequestMapping(value = "/daily")
    public void updateDaily() {
        factorService.calculateFactors(LocalDate.now());
    }
}
