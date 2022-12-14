package com.whzz.domain.repository;

import com.whzz.domain.bo.Daily;
import com.whzz.domain.bo.DailyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyRepository extends JpaRepository<Daily, DailyId>{
    List<Daily> findByCodeOrderByDate(String code);
    List<Daily> findByCodeAndDateLessThanOrderByDate(String code, LocalDate date);
    List<Daily> findByCodeAndDateGreaterThanEqualOrderByDate(String code, LocalDate date);
    @Transactional
    @Modifying
    @Query(value = "update Daily set limitUp = :limitUp, limitDown = :limitDown where code = :code and date = :date", nativeQuery = true)
    void updateLimit(String code, LocalDate date, float limitUp, float limitDown);
}
