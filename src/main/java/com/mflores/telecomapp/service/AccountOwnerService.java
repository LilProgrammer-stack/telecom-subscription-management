package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.AccountOwnerRequest;
import com.mflores.telecomapp.dto.AccountOwnerResponse;
import com.mflores.telecomapp.dto.CreateCustomerRegistrationRequest;
import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.model.UserAccount;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import com.mflores.telecomapp.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AccountOwnerService {

    private final AccountOwnerRepository accountOwnerRepository;
    private final UserAccountRepository userAccountRepository;

    public AccountOwnerService(AccountOwnerRepository accountOwnerRepository,
                               UserAccountRepository userAccountRepository) {
        this.accountOwnerRepository = accountOwnerRepository;
        this.userAccountRepository = userAccountRepository;
    }


    public List<AccountOwnerResponse> getAllAccountOwners(){
        return this.accountOwnerRepository.findAll()
                .stream()
                .map(this::convertToAccountOwnerResponse)
                .toList();
    }

    public AccountOwnerResponse getAccountOwnerById(Long id){
        AccountOwner accountOwner = getEntityAccountOwnerById(id);
        return convertToAccountOwnerResponse(accountOwner);
    }

    public AccountOwner createAccountOwner(CreateCustomerRegistrationRequest createCustomerRegistrationRequest,
                                                   UserAccount userAccount){
        AccountOwner accountOwner = new AccountOwner(createCustomerRegistrationRequest.getFirstName(),
                createCustomerRegistrationRequest.getLastName(),
                userAccount,
                createCustomerRegistrationRequest.getDateOfBirth(),
                OffsetDateTime.now());

        return accountOwnerRepository.save(accountOwner);
    }

    private AccountOwner getEntityAccountOwnerById(Long id){
        return accountOwnerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("The account owner id "+id+" was not found"));
    }

    public AccountOwnerResponse updateAccountOwnerById(Long id, AccountOwnerRequest ownerRequest){
        AccountOwner accountOwner = getEntityAccountOwnerById(id);
        accountOwner.setFirstName(ownerRequest.getFirstName());
        accountOwner.setLastName(ownerRequest.getLastName());
        accountOwner.setDateOfBirth(ownerRequest.getDateOfBirth());
        accountOwnerRepository.save(accountOwner);

        return convertToAccountOwnerResponse(accountOwner);
    }

    public void deleteAccountOwnerById(Long id){
        AccountOwner accountOwner = getEntityAccountOwnerById(id);
        accountOwnerRepository.delete(accountOwner);
    }

    private AccountOwnerResponse convertToAccountOwnerResponse(AccountOwner accountOwner){
        return new AccountOwnerResponse(accountOwner.getAccountOwnerId(), accountOwner.getFirstName(),
                accountOwner.getLastName(), accountOwner.getDateOfBirth(),
                accountOwner.getCreatedAt(),
                accountOwner.getUserAccount().getEmail());
    }

}
