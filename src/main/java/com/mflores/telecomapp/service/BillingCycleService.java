package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.BillingCycleResponse;
import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.model.Account;
import com.mflores.telecomapp.model.BillingCycle;
import com.mflores.telecomapp.repository.BillingCycleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BillingCycleService {

    private final BillingCycleRepository billingCycleRepository;

    public BillingCycleService(BillingCycleRepository billingCycleRepository) {
        this.billingCycleRepository = billingCycleRepository;
    }

    public BillingCycleResponse createBillingCycle(Account account){

        Optional<BillingCycle> latestCycleOpt = billingCycleRepository.findFirstByAccountOrderByCycleNumberDesc(account);

        LocalDate startDate;
        Integer nextCycleNumber;

        if (latestCycleOpt.isPresent()) {
            BillingCycle latestCycle = latestCycleOpt.get();

            startDate = latestCycle.getPeriodEndDate().plusDays(1);
            nextCycleNumber = latestCycle.getCycleNumber() + 1;
        }else {
            startDate = account.getCreationDate().toLocalDate();
            nextCycleNumber = 1;
        }

        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        BillingCycle billingCycle = new BillingCycle();
        billingCycle.setPeriodStartDate(startDate);
        billingCycle.setPeriodEndDate(endDate);
        billingCycle.setCycleNumber(nextCycleNumber);
        billingCycle.setAccount(account);

        BillingCycle cycle = billingCycleRepository.save(billingCycle);

        return convertIntoBillingCycleResponse(cycle);
    }

    private BillingCycleResponse convertIntoBillingCycleResponse(BillingCycle billingCycle){

        return new BillingCycleResponse(billingCycle.getBillingCycleId(),
                billingCycle.getCycleNumber(), billingCycle.getPeriodStartDate(),
                billingCycle.getPeriodEndDate(), billingCycle.getAccount());
    }
}
