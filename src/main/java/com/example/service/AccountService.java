package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public int validateAccount(Account a){
        if (a.getPassword().length() < 4){
            return 0;
        }
        for (Account ac : accountRepository.findAll()){
            if (ac.getUsername().equals(a.getUsername())){
                return -1;
            }
        }
        accountRepository.save(a);
        return 1;
    }

    public Account accountExists(Account a){
        for (Account ac : accountRepository.findAll()){
            if (ac.getUsername().equals(a.getUsername()) && ac.getPassword().equals(a.getPassword())){
                return ac;
            }
        }
        return null;
    }

    public Account accountExistsById(int id){
        for (Account ac : accountRepository.findAll()){
            if (ac.getAccount_id().equals(id)){
                return ac;
            }
        }
        return null;
    }
}
