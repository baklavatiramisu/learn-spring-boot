package com.baklavatiramisu.learn.springjpa.user;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    @Query("SELECT u FROM User u WHERE id = :id AND deletedOn IS NULL")
    Optional<UserEntity> findExistingUserById(@Param("id") long id);
}
