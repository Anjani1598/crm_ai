package com.crm.service.impl;

import com.crm.domain.repository.ContactRepository;
import com.crm.dto.CustomerInsightsDTO;
import com.crm.repository.TaskRepository;
import com.crm.repository.DealRepository;
import com.crm.service.DashboardService;
import com.crm.statemachine.DealState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private final ContactRepository contactRepository;
    private final TaskRepository taskRepository;
    private final DealRepository dealRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getTotalRevenue() {
        Map<String, Object> response = new HashMap<>();
        
        // Calculate total revenue from all won deals
        BigDecimal totalRevenue = dealRepository.findAll().stream()
                .filter(deal -> deal.getDealState() != null && 
                        deal.getDealState().getState() == DealState.CLOSED_WON)
                .map(deal -> deal.getAmount() != null ? deal.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate last month's revenue
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        LocalDateTime twoMonthsAgo = LocalDateTime.now().minusMonths(2);
        
        BigDecimal lastMonthRevenue = dealRepository.findAll().stream()
                .filter(deal -> deal.getDealState() != null && 
                        deal.getDealState().getState() == DealState.CLOSED_WON &&
                        deal.getDealState().getUpdatedAt() != null &&
                        deal.getDealState().getUpdatedAt().isAfter(oneMonthAgo) &&
                        deal.getDealState().getUpdatedAt().isBefore(LocalDateTime.now()))
                .map(deal -> deal.getAmount() != null ? deal.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate previous month's revenue for percentage change
        BigDecimal previousMonthRevenue = dealRepository.findAll().stream()
                .filter(deal -> deal.getDealState() != null && 
                        deal.getDealState().getState() == DealState.CLOSED_WON &&
                        deal.getDealState().getUpdatedAt() != null &&
                        deal.getDealState().getUpdatedAt().isAfter(twoMonthsAgo) &&
                        deal.getDealState().getUpdatedAt().isBefore(oneMonthAgo))
                .map(deal -> deal.getAmount() != null ? deal.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate percentage change
        String percentageChange = "0.00%";
        if (previousMonthRevenue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal change = lastMonthRevenue.subtract(previousMonthRevenue)
                    .divide(previousMonthRevenue, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
            percentageChange = String.format("%.2f%%", change);
        }
        
        response.put("totalRevenue", totalRevenue);
        response.put("lastMonthRevenue", lastMonthRevenue);
        response.put("percentageChange", percentageChange);
        response.put("currency", "USD");
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getActiveDeals() {
        Map<String, Object> response = new HashMap<>();
        
        // Get all deals and filter for active ones (not CLOSED_WON or CLOSED_LOST)
        var activeDeals = dealRepository.findAll().stream()
                .filter(deal -> deal.getDealState() != null && 
                        deal.getDealState().getState() != DealState.CLOSED_WON &&
                        deal.getDealState().getState() != DealState.CLOSED_LOST)
                .toList();
        
        // Calculate total value of active deals
        BigDecimal activeDealsValue = activeDeals.stream()
                .map(deal -> deal.getAmount() != null ? deal.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate deals by stage
        Map<DealState, Long> dealsByStage = activeDeals.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        deal -> deal.getDealState().getState(),
                        java.util.stream.Collectors.counting()
                ));
        
        response.put("activeDeals", activeDeals.size());
        response.put("activeDealsValue", activeDealsValue);
        response.put("dealsByStage", dealsByStage);
        response.put("currency", "USD");
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getNewCustomers() {
        Map<String, Object> response = new HashMap<>();
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        LocalDateTime sixtyDaysAgo = LocalDateTime.now().minusDays(60);
        
        long newCustomersCount = contactRepository.findAll().stream()
                .filter(contact -> contact.getCreatedAt() != null && 
                        contact.getCreatedAt().isAfter(thirtyDaysAgo))
                .count();
        
        // Calculate previous period for comparison
        long previousPeriodCount = contactRepository.findAll().stream()
                .filter(contact -> contact.getCreatedAt() != null && 
                        contact.getCreatedAt().isAfter(sixtyDaysAgo) &&
                        contact.getCreatedAt().isBefore(thirtyDaysAgo))
                .count();
        
        double percentageChange = previousPeriodCount > 0 ? 
                ((newCustomersCount - previousPeriodCount) / (double)previousPeriodCount) * 100 : 0;
        
        response.put("newCustomers", newCustomersCount);
        response.put("previousPeriod", previousPeriodCount);
        response.put("percentageChange", String.format("%.2f%%", percentageChange));
        response.put("period", "last30Days");
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getPendingTasks() {
        Map<String, Object> response = new HashMap<>();
        long pendingTasksCount = taskRepository.findAll().stream()
                .filter(task -> !task.isCompleted())
                .count();
        
        // Calculate overdue tasks
        long overdueTasksCount = taskRepository.findAll().stream()
                .filter(task -> !task.isCompleted() && 
                        task.getDueDate() != null && 
                        task.getDueDate().before(new java.util.Date()))
                .count();
        
        response.put("pendingTasks", pendingTasksCount);
        response.put("overdueTasks", overdueTasksCount);
        response.put("overduePercentage", pendingTasksCount > 0 ? 
                String.format("%.2f%%", (overdueTasksCount * 100.0 / pendingTasksCount)) : "0%");
        return response;
    }

    @Override
    public CustomerInsightsDTO getCustomerInsights(Long customerId) {
        return null;
    }
} 