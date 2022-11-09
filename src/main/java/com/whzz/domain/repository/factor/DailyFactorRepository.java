package com.whzz.domain.repository.factor;

import com.whzz.domain.bo.DailyId;
import com.whzz.domain.bo.factor.DailyFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyFactorRepository extends JpaRepository<DailyFactor, DailyId> {
}
