package com.project2.sec.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project2.sec.model.User;
import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<User, Integer>{
	
//	List<User> findByUsername(String username);
	
	@Query("SELECT u from User u where u.username = :username")
	User findByUsername(@Param("username") String username);
}
