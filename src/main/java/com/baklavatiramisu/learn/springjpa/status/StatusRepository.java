package com.baklavatiramisu.learn.springjpa.status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends CrudRepository<StatusEntity, Long>, PagingAndSortingRepository<StatusEntity, Long> {
    @Query("SELECT s FROM Status s WHERE s.user.id = :userId AND s.deletedOn IS NULL")
    Page<StatusEntity> findAllStatusByUserId(@Param("userId") long userId, Pageable pageable);

    @Query("SELECT s FROM Status s WHERE s.user.id = :userId AND s.deletedOn IS NULL AND status LIKE :searchQuery")
    Page<StatusEntity> findAllStatusByUserIdWithSearch(@Param("userId") long userId, @Param("searchQuery") String searchQuery, Pageable pageable);
}
