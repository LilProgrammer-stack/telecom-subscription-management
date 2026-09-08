package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.AccountResponse;
import com.mflores.telecomapp.dto.CreateAccountRequest;
import com.mflores.telecomapp.model.Account;
import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.model.AccountStatus;
import com.mflores.telecomapp.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    public AccountService(AccountRepository accountRepository, AccountNumberGenerator accountNumberGenerator) {
        this.accountRepository = accountRepository;
        this.accountNumberGenerator = accountNumberGenerator;
    }

    public AccountResponse createAccount(CreateAccountRequest accountRequest, AccountOwner accountOwner){

        Account account = new Account(
                accountNumberGenerator.generate(),
                accountRequest.getBillingLanguage(),
                AccountStatus.ACTIVE,
                accountOwner
        );

        Account savedAccount = accountRepository.save(account);

        return convertToAccountResponse(savedAccount);
    }

    private AccountResponse convertToAccountResponse(Account savedAccount){

        return new AccountResponse(savedAccount.getAccountId(),
                savedAccount.getAccountNumber(),
                savedAccount.getAccountStatus(),
                savedAccount.getBillingLanguage(),
                savedAccount.getCreationDate());
    }
}
