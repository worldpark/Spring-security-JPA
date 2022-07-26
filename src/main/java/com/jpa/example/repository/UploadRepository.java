package com.jpa.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jpa.example.entity.UploadEntity;

@Repository("UploadRepository")
public interface UploadRepository extends JpaRepository<UploadEntity, Long> {
	
	@Query(value = "SELECT * from uploadtable where bid = :bid", nativeQuery = true)
	public List<UploadEntity> fileread(@Param(value="bid") int bid);
	
	@Query(value = "SELECT * from uploadtable where fid = :fid", nativeQuery = true)
	public UploadEntity fileinfo(@Param(value="fid") int fid);
	
	@Query(value = "DELETE from uploadtable where bid = :bid", nativeQuery = true)
	@Modifying
	@Transactional
	public void deletebid(@Param(value="bid") Long bid);
}
