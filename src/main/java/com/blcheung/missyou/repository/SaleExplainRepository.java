package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.SaleExplain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleExplainRepository extends JpaRepository<SaleExplain, Long> {}
