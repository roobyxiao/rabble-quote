package com.whzz.domain.repository;

import com.whzz.domain.bo.Daily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyRepository extends JpaRepository<Daily, Long>{
    List<Daily> findByCode(String code);
    Optional<Daily> findByCodeAndDate(String code, LocalDate date);
    boolean existsByCodeAndDate(String code, LocalDate date);
    @Transactional
    @Modifying
    @Query(value = "update Daily set limitUp = :limitUp, limitDown = :limitDown where code = :code and date = :date", nativeQuery = true)
    void updateLimit(String code, LocalDate date, float limitUp, float limitDown);
}
