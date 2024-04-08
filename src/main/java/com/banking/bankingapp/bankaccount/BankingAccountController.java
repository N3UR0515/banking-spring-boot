package com.banking.bankingapp.bankaccount;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@RestController
@ResponseBody
@RequestMapping("/api/account/")
public class BankingAccountController {
    protected static final Logger logger = LogManager.getLogger();

    @Autowired
    private BankingAccountRepository bankingAccountRepository;

    private void checkId(int expected) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BankingAccount principal = (BankingAccount) authentication.getPrincipal();
        if(expected != principal.getId())
        {
            throw new Exception();
        }
    }

    @PostMapping(path="/add/")
    public ResponseEntity<?> addNewAccount(@RequestBody Map<String, Object> requestBody)
    {
        BankingAccount bankingAccount = new BankingAccount();
        try {
            String password = requestBody.get("password").toString();
            float balance = Float.parseFloat(requestBody.get("balance").toString());
            bankingAccount.setBalance(balance);
            bankingAccount.setPassword(password);
            bankingAccountRepository.save(bankingAccount);
        }catch (Exception e){
            logger.error(e);
            return ResponseEntity.status(400).body("Could not add");
        }
        logger.info("New banking account added");
        return ResponseEntity.status(HttpStatus.CREATED).body(bankingAccount);
    }

    @DeleteMapping(path="/{id}/delete/")
    public ResponseEntity<?> deleteAccount(@PathVariable int id) {
        Optional<BankingAccount> bankingAccount = bankingAccountRepository.findById(id);
        try{
            checkId(id);
            bankingAccountRepository.delete(bankingAccount.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(bankingAccount);
        } catch (Exception e){
            return ResponseEntity.status(404).body("The account was not found");
        }
    }

    @GetMapping(path="/all/")
    public ResponseEntity<Iterable<BankingAccount>> getAllAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(bankingAccountRepository.findAll());
    }

    @PostMapping(path="/{id}/withdraw/")
    public ResponseEntity<?> withdraw(@PathVariable int id, @RequestBody float amount){
        Optional<BankingAccount> bankingAccount = bankingAccountRepository.findById(id);
        try{
            checkId(id);
            bankingAccount.get().withdraw(amount);
            bankingAccountRepository.save(bankingAccount.get());
        } catch (Exception e)
        {
            logger.error(e);
            return ResponseEntity.status(400).body("Withdrawal failed");
        }

        return ResponseEntity.status(HttpStatus.OK).body(bankingAccount.get());
    }

    @PostMapping(path="/{id}/deposit/")
    public ResponseEntity<?> deposit(@PathVariable int id, @RequestBody float amount){
        Optional<BankingAccount> bankingAccount = bankingAccountRepository.findById(id);
        try{
            checkId(id);
            bankingAccount.get().deposit(amount);
            bankingAccountRepository.save(bankingAccount.get());
        }catch (Exception e)
        {
            logger.error(e);
            return ResponseEntity.status(400).body("Deposit failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(bankingAccount.get());
    }

    @PostMapping(path="{id}/transfer/")
    public ResponseEntity<?> transfer(@PathVariable int id, @RequestBody Map<String, Object> requestBody){
        Optional<BankingAccount> bankingAccount = bankingAccountRepository.findById(id);
        try{
            checkId(id);
            int anotherId = (int) requestBody.get("anotherId");
            float amount = Float.parseFloat(requestBody.get("amount").toString());
            Optional<BankingAccount> anotherBankingAccount = bankingAccountRepository.findById(anotherId);
            bankingAccount.get().transfer(anotherBankingAccount.get(), amount);
            bankingAccountRepository.save(bankingAccount.get());
            bankingAccountRepository.save(anotherBankingAccount.get());
        }catch (Exception e)
        {
            logger.error(e);
            return ResponseEntity.status(400).body("Transfer failed");
        }

        return ResponseEntity.status(HttpStatus.OK).body(bankingAccount.get());
    }

    @GetMapping(path="{id}/display/")
    public ResponseEntity<?> display(@PathVariable int id){
        Optional<BankingAccount> bankingAccount = bankingAccountRepository.findById(id);
        float balance;
        try{
            checkId(id);
            balance = bankingAccount.get().getBalance();
        }catch(java.util.NoSuchElementException e){
            logger.error(e);
            return ResponseEntity.status(404).body("Element was not found");
        }catch (Exception e){
            logger.error(e);
            return ResponseEntity.status(400).body("There was an error");
        }

        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }
}
