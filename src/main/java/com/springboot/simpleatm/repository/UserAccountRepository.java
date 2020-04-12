package com.springboot.simpleatm.repository;

import com.springboot.simpleatm.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    UserAccount findByAccountNumber(String accountNumber);
}
