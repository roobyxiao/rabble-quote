package com.whzz.domain.repository;

import com.whzz.domain.bo.DailyId;
import com.whzz.domain.bo.UpLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UpLimitRepository extends JpaRepository<UpLimit, DailyId> {
    List<UpLimit> findByCode(String code);
}
