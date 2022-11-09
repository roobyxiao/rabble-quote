package com.whzz.domain.repository;

import com.whzz.domain.bo.Forward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ForwardRepository extends JpaRepository<Forward, Long> {
    List<Forward> findByCodeOrderByDate(String code);
    List<Forward> findByCodeOrderByDateDesc(String code);
    List<Forward> findByCodeAndDateLessThanOrderByDate(String code, LocalDate date);
    List<Forward> findByCodeAndDateGreaterThanEqualOrderByDate(String code, LocalDate date);
}
