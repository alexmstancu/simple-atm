package com.springboot.simpleatm.repository;

import com.springboot.simpleatm.model.UserAccount;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer>, JpaSpecificationExecutor<UserAccount> {
    static Specification<UserAccount> filterByAccountNumber(String accountNumber) {
        return (root, cq, cb) -> cb.equal(root.get("accountNumber"), accountNumber);
    }
}
