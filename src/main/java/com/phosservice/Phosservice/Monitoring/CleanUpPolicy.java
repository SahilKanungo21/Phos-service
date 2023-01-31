package com.phosservice.Phosservice.Monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CleanUpPolicy {

    @Autowired
    private HealthStatus healthStatus;

}
