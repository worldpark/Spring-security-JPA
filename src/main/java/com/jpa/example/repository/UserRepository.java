package com.jpa.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jpa.example.entity.UserEntity;

@Repository("UserRepository")
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	@Query(value = "SELECT count(*) FROM usertable where id = :id", nativeQuery = true)
	int findbyId(@Param("id") String id);
	
	
	@Query(value = "SELECT * FROM usertable where id = :id", nativeQuery = true)
	UserEntity selectUserInfoOne(@Param("id") String id);
	
	@Query(value = "SELECT auth FROM usertable where id = :id", nativeQuery = true)
	List<String> selectUserAuthOne(@Param("id") String id);

	@Query(value = "SELECT * FROM usertable where id = :id", nativeQuery = true)
	Optional<UserEntity> findByUser(@Param("id") String id);
	

}
