package com.crm.controller;

import com.crm.dto.CustomerInsightsDTO;
import com.crm.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/totalRevenue")
    public Map<String, Object> getTotalRevenue() {
        return dashboardService.getTotalRevenue();
    }

    @GetMapping("/activeDeals")
    public Map<String, Object> getActiveDeals() {
        return dashboardService.getActiveDeals();
    }

    @GetMapping("/newCustomers")
    public Map<String, Object> getNewCustomers() {
        return dashboardService.getNewCustomers();
    }

    @GetMapping("/pendingTasks")
    public Map<String, Object> getPendingTasks() {
        return dashboardService.getPendingTasks();
    }

    @GetMapping("/customer/insights/{customerId}")
    public CustomerInsightsDTO getCustomerInsights(@PathVariable Long customerId) {
        return dashboardService.getCustomerInsights(customerId);
    }
} 