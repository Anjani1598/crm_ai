package com.crm.service;

import com.crm.dto.CustomerInsightsDTO;

import java.util.Map;

public interface DashboardService {
    Map<String, Object> getTotalRevenue();
    Map<String, Object> getActiveDeals();
    Map<String, Object> getNewCustomers();
    Map<String, Object> getPendingTasks();
    CustomerInsightsDTO getCustomerInsights(Long customerId);
} 