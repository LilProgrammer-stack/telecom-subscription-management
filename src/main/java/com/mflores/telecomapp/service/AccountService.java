package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.AccountResponse;
import com.mflores.telecomapp.dto.CreateAccountRequest;
import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import com.mflores.telecomapp.repository.AccountRepository;

public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountOwnerRepository accountOwnerRepository;

    public AccountService(AccountRepository accountRepository, AccountOwnerRepository accountOwnerRepository) {
        this.accountRepository = accountRepository;
        this.accountOwnerRepository = accountOwnerRepository;
    }

    public AccountResponse createAccount(CreateAccountRequest accountRequest, AccountOwner accountOwner){

    return null;
    }
}
