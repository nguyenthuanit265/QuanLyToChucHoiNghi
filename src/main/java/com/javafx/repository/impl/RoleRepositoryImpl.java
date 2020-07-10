package com.javafx.repository.impl;

import com.javafx.entity.Role;
import com.javafx.repository.RoleRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl extends CrudRepositoryImpl<Role, Integer> implements RoleRepository {

}
