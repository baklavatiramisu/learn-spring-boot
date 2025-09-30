package com.baklavatiramisu.learn.springjpa.repository;

import com.baklavatiramisu.learn.springjpa.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
}
