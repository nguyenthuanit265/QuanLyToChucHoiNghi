package com.javafx.repository;

import com.javafx.entity.Role;

import java.util.List;

public interface TestRepository {
    List<Role> findAll();

    public void SaveOrUpdate(Role role);
}
