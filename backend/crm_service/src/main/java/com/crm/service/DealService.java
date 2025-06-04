package com.crm.service;

import com.crm.dto.DealDTO;
import com.crm.dto.DealFilterDTO;
import org.springframework.data.domain.Page;
import java.util.List;

public interface DealService {
    DealDTO createDeal(DealDTO dealDTO);
    List<DealDTO> getDealsByContactId(Long contactId);
    Page<DealDTO> getFilteredDeals(DealFilterDTO filterDTO);
    DealDTO getDealById(Long dealId);
} 