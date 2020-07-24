package com.javafx.repository.impl;

import com.javafx.entity.Location;
import com.javafx.repository.LocationRepository;
import org.springframework.stereotype.Repository;

import java.util.Locale;

@Repository
public class LocationRepositoryImpl extends CrudRepositoryImpl<Location, Integer> implements LocationRepository {
}
