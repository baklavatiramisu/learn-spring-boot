package com.baklavatiramisu.learn.springjpa.repository;

import com.baklavatiramisu.learn.springjpa.dto.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM com.baklavatiramisu.learn.springjpa.dto.Post p WHERE p.id = :id AND p.user.id = :userId")
    public Optional<Post> findPostByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT p FROM com.baklavatiramisu.learn.springjpa.dto.Post p WHERE p.user.id = :userId")
    public List<Post> findAllPostsByUserId(@Param("userId") Long userId);
}
