package com.yazilimmotoru.simple_banking.repository;

import com.yazilimmotoru.simple_banking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
}

