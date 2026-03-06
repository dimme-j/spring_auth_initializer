package com.spring.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.auth.entities.User;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query(value = "SELECT * FROM users u WHERE u.fullname =:username",nativeQuery=true)
	Optional<User> findByFullname(@Param("username")String username);

	@Query(value = "SELECT * FROM users u WHERE u.email = :username",nativeQuery=true)
	Optional<User> findByEmail(@Param("username")String username);

}
