package com.blcheung.cappuccino.service;

import com.blcheung.cappuccino.model.Activity;
import com.blcheung.cappuccino.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    public Activity getActivityByName(String name) {
        return this.activityRepository.findByName(name);
    }


}
