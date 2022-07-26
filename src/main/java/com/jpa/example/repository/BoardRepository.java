package com.jpa.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jpa.example.entity.BoardEntity;

@Repository("BoardRepository")
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
	
	@Query(value="SELECT count(*) FROM boardtable", nativeQuery = true)
	public int getBoardListCnt();
	
	@Query(value = "SELECT * from boardtable order by bid desc limit :startlist, :listsize", nativeQuery = true)
	public List<BoardEntity> getBoardList(@Param(value = "startlist") int startlist, @Param(value = "listsize") int listsize);

	@Query(value = "SELECT * from boardtable where bid = :bid", nativeQuery = true)
	public BoardEntity read(@Param(value="bid") int bid);
	
	@Query(value = "SELECT bid from boardtable where userid = :userid order by bid  desc limit 1", nativeQuery = true)
	public Long getBid(@Param(value="userid") String userid);
}
