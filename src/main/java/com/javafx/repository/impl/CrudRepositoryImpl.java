package com.javafx.repository.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.javafx.config.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.javafx.repository.CrudRepository;

@Repository
@Transactional(rollbackFor = Exception.class)
public abstract class CrudRepositoryImpl<T, K extends Serializable>
        implements CrudRepository<T, K> {

    @Autowired
    protected SessionFactory sessionFactory;

    protected Class<? extends T> clazz;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CrudRepositoryImpl() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        clazz = (Class) paramType.getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction trans = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = (CriteriaQuery<T>) builder.createQuery(clazz);
            Root<T> root = (Root<T>) query.from(clazz);
            query.select(root);
            Query<T> q = session.createQuery(query);
            List<T> list = q.getResultList();
            trans.commit();

            return list;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public T findById(K id) {
//        Session session = sessionFactory.getCurrentSession();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {

            // Truy vấn dữ liệu
            T t = session.find(clazz, id);
            trans.commit();

            return t;

        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {

        }

        return null;
    }

    public List<T> findByIdActive(K id) {
//        Session session = sessionFactory.getCurrentSession();
        List<T> objects = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();
        try {

            // Truy vấn dữ liệu
//            T t = session.find(clazz, id);
            Query query = session.createQuery("from " + clazz.getName() + " where is_delete=false and id =" + id);
            objects = query.list();
//            objects = (List<T>) session.createQuery("SELECT a FROM " + clazz.getName() + " a where a.is_delete = false", clazz).getResultList();
            trans.commit();

            return objects;

        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }

        return objects;
    }

    public void save(T entity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction trans = session.beginTransaction();

        try {

            // Truy vấn dữ liệu
            session.saveOrUpdate(entity);
            trans.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void removeById(K id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();
        try {
            // Truy vấn dữ liệu
            T entity = findById(id);
            session.remove(entity);
            trans.commit();

        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    public List<T> findAllActive() {
        List<T> objects = null;

        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction trans = session.beginTransaction();
//            Query query = session.createQuery("from " + clazz.getName() + " where is_delete=false");
//            objects = query.list();
            objects = (List<T>) session.createQuery("SELECT a FROM " + clazz.getName() + " a where a.isDelete = false", clazz).getResultList();

            trans.commit();

            return objects;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {

//            session.close();
        }
        return objects;
    }
}