package com.whzz.domain.repository;

import com.whzz.domain.bo.Tick;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TickRepository extends MongoRepository<Tick, ObjectId> {
}
