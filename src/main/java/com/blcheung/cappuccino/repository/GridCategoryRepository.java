package com.blcheung.cappuccino.repository;

import com.blcheung.cappuccino.model.GridCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GridCategoryRepository extends JpaRepository<GridCategory, Long> {

}
