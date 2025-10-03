package com.baklavatiramisu.learn.springjpa.status;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends CrudRepository<StatusEntity, Long> {
    @Query("SELECT s FROM Status s WHERE s.user.id = :userId AND s.deletedOn IS NULL")
    Iterable<StatusEntity> findAllStatusByUserId(@Param("userId") long userId);
}
