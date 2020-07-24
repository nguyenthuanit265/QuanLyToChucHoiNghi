package com.javafx.repository.impl;

import com.javafx.entity.Conference;
import com.javafx.repository.ConferenceRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ConferenceRepositoryImpl extends CrudRepositoryImpl<Conference, Integer> implements ConferenceRepository {
}
