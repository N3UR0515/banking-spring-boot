package com.banking.bankingapp.bankaccount;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BankingAccount implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private float balance;

    public BankingAccount(float balanceToSet, String passwordToSet, Integer idToSet){
        balance = balanceToSet;
        password = passwordToSet;
        id = idToSet;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    private void validateAmount(float amount) throws Exception
    {
        if(amount <= 0)
        {
            throw new Exception("Invalid amount provided");
        }
    }

    public void deposit(float amount) throws Exception {
        validateAmount(amount);
        balance += amount;
    }

    public void withdraw(float amount) throws Exception {
        validateAmount(amount);
        if(amount > balance)
            throw new Exception("Insufficient funds");

        balance -= amount;
    }

    public void printBalance()
    {
        System.out.printf("Current Balance is %.2f%n", balance);
    }

    public void transfer(BankingAccount anotherAccount, float amount) throws Exception {
        if(Objects.equals(this.id, anotherAccount.getId()))
            throw new Exception("Money can't be transferred to the same account");

        this.withdraw(amount);
        anotherAccount.deposit(amount);
    }
}
