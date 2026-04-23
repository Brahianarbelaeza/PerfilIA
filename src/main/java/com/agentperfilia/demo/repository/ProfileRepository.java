package com.agentperfilia.demo.repository;

import com.agentperfilia.demo.model.entity.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<Profile, String> {
}
