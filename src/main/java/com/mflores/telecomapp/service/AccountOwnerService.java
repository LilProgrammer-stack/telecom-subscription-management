package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.AccountOwnerRequest;
import com.mflores.telecomapp.dto.AccountOwnerResponse;
import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountOwnerService {

    private final AccountOwnerRepository accountOwnerRepository;

    public AccountOwnerService(AccountOwnerRepository accountOwnerRepository) {
        this.accountOwnerRepository = accountOwnerRepository;
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

    public AccountOwnerResponse createAccountOwner(AccountOwnerRequest accountOwnerRequest){
        AccountOwner accountOwner = new AccountOwner(accountOwnerRequest.getFirstName(),
                accountOwnerRequest.getLastName(),
                accountOwnerRequest.getEmail(),
                accountOwnerRequest.getDateOfBirth());

        accountOwnerRepository.save(accountOwner);
        return convertToAccountOwnerResponse(accountOwner);
    }

    private AccountOwner getEntityAccountOwnerById(Long id){
        return accountOwnerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("The account owner id "+id+" was not found"));
    }

    public AccountOwnerResponse updateAccountOwnerById(Long id, AccountOwnerRequest ownerRequest){
        AccountOwner accountOwner = getEntityAccountOwnerById(id);
        accountOwner.setFirstName(ownerRequest.getFirstName());
        accountOwner.setLastName(ownerRequest.getLastName());
        accountOwner.setEmail(ownerRequest.getEmail());
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
                accountOwner.getLastName(), accountOwner.getEmail(), accountOwner.getDateOfBirth(),
                accountOwner.getCreatedAt());
    }

}
