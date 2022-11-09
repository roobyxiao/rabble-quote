package com.whzz.domain.repository.factor;

import com.whzz.domain.bo.DailyId;
import com.whzz.domain.bo.factor.ForwardFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForwardFactorRepository extends JpaRepository<ForwardFactor, DailyId> {
    List<ForwardFactor> findByCode(String code);
}
