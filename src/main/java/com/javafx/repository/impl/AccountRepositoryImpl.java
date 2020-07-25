package com.javafx.repository.impl;

import com.javafx.entity.Account;
import com.javafx.repository.AccountRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepositoryImpl extends CrudRepositoryImpl<Account, Integer> implements AccountRepository {
}
