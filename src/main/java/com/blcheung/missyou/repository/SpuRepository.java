package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.Spu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpuRepository extends JpaRepository<Spu, Long> {

    Spu findOneById(Long id);
}
