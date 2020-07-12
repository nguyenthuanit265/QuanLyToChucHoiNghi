package com.javafx.repository.impl;

import com.javafx.entity.Role;
import com.javafx.repository.TestRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public class TestRepositoryImpl implements TestRepository {
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<Role> findAll() {
//        		Session session = sessionFactory.openSession();
//		List<Role> roles = null;
//		try {
//			Query<Role> query = session.createQuery("from roles", Role.class);
//			roles = query.getResultList();
//
//		} catch (HibernateException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			session.close();
//		}
//
//		return roles;

        Session session = sessionFactory.getCurrentSession();
        List<Role> roles = null;
        try {
            Query<Role> query = session.createQuery("from roles", Role.class);
            roles = query.getResultList();

        } catch (HibernateException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return roles;
    }

    public void SaveOrUpdate(Role role) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.saveOrUpdate(role);

            transaction.commit();
        } catch (HibernateException e) {
            // TODO: handle exception
            transaction.rollback();
        } finally {
            session.close();
        }

//        Session session = sessionFactory.getCurrentSession();
//
//        try {
//
//            session.saveOrUpdate(role);
//
//        } catch (HibernateException e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }

    }
}
