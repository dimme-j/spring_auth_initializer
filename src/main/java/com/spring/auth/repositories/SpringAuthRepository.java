package com.spring.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.auth.entities.Signupresponse;
@Repository
public interface SpringAuthRepository extends JpaRepository<Signupresponse, Integer> {
	
	@Query(value="""
			select Count(*)>0 from signup where username =:username
			""",nativeQuery=true)
	Boolean getByUsername(@Param("username") String username);

}
