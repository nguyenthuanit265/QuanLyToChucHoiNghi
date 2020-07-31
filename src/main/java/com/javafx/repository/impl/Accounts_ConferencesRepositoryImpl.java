package com.javafx.repository.impl;

import com.javafx.entity.Account;
import com.javafx.entity.Accounts_Conferences;
import com.javafx.repository.AccountRepository;
import com.javafx.repository.Accounts_ConferencesRepository;
import org.springframework.stereotype.Repository;

@Repository
public class Accounts_ConferencesRepositoryImpl extends CrudRepositoryImpl<Accounts_Conferences, Integer> implements Accounts_ConferencesRepository {
}
