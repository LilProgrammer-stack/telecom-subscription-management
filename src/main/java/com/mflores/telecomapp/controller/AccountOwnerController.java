package com.mflores.telecomapp.controller;

import com.mflores.telecomapp.dto.AccountOwnerRequest;
import com.mflores.telecomapp.dto.AccountOwnerResponse;
import com.mflores.telecomapp.service.AccountOwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accountOwners")
public class AccountOwnerController {

    private final AccountOwnerService accountOwnerService;

    public AccountOwnerController(AccountOwnerService accountOwnerService) {
        this.accountOwnerService = accountOwnerService;
    }

    @PostMapping
    public ResponseEntity<AccountOwnerResponse> createAccountOwner(@RequestBody AccountOwnerRequest accountOwnerRequest){
        AccountOwnerResponse accountOwnerResponse= this.accountOwnerService.createAccountOwner(accountOwnerRequest);

        URI uriLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(accountOwnerResponse.getAccountOwnerId())
                .toUri();

        return ResponseEntity.created(uriLocation).body(accountOwnerResponse);
    }

    @GetMapping
    public ResponseEntity<List<AccountOwnerResponse>>  getAllAccountOwners(){
        List<AccountOwnerResponse> accountOwnerResponses = accountOwnerService.getAllAccountOwners();
        return ResponseEntity.ok(accountOwnerResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountOwnerResponse> getAccountOwnerById(@PathVariable Long id){
        AccountOwnerResponse accountOwnerResponse = accountOwnerService.getAccountOwnerById(id);
        return ResponseEntity.ok(accountOwnerResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountOwnerResponse> updateAccountOwnerById(@PathVariable Long id,@RequestBody AccountOwnerRequest accountOwnerRequest){
        AccountOwnerResponse accountOwnerResponse = accountOwnerService.updateAccountOwnerById(id, accountOwnerRequest);
        return ResponseEntity.ok(accountOwnerResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountOwnerById(@PathVariable Long id){
        accountOwnerService.deleteAccountOwnerById(id);
        return ResponseEntity.noContent().build();
    }
}
