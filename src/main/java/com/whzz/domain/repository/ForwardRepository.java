package com.whzz.domain.repository;

import com.whzz.domain.bo.Forward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForwardRepository extends JpaRepository<Forward, Long> {
    List<Forward> findByCodeOrderByDateDesc(String code);
}
