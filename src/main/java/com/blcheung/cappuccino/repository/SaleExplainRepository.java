package com.blcheung.cappuccino.repository;

import com.blcheung.cappuccino.model.SaleExplain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleExplainRepository extends JpaRepository<SaleExplain, Long> {}
