package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findByName(String name);
}
