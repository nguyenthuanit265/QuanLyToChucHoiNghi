package com.javafx.repository.impl;

import com.javafx.config.HibernateUtil;
import com.javafx.entity.Account;
import com.javafx.repository.AccountRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.hibernate.query.Query;

import java.util.List;

@Repository
public class AccountRepositoryImpl extends CrudRepositoryImpl<Account, Integer> implements AccountRepository {
    @Override
    public Account findByEmail(String email) {
        String hql = "FROM accounts WHERE email = :email";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction trans = session.beginTransaction();
            Query<Account> query = session.createQuery(hql, Account.class);
            query.setParameter("email", email);
            Account account = query.getSingleResult();
            trans.commit();
            return account;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

}
