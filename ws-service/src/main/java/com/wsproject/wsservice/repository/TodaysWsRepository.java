package com.wsproject.wsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.wsservice.domain.TodaysWs;

public interface TodaysWsRepository extends JpaRepository<TodaysWs, Long> {
	
//	Optional<TodaysWs> findByUserIdx(Long userIdx);
	
//	@Query(value = 	"SELECT new com.wsproject.wsservice.dto.TodaysWsResponseDto("
//					+ "t.id, "
//					+ "t.userIdx, "
//					+ "CASE WHEN a IS NOT NULL THEN a.content ELSE p.content END, "
//					+ "CASE WHEN a IS NOT NULL THEN a.author ELSE p.author END, "
//					+ "CASE WHEN a IS NOT NULL THEN a.type ELSE p.type END, "
//					+ "CASE WHEN a IS NOT NULL THEN a.id ELSE p.id END, "
//					+ "CASE WHEN a IS NOT NULL THEN false ELSE true END, "
//					+ "CASE WHEN a IS NOT NULL THEN (CASE WHEN l IS NOT NULL THEN true ELSE false END) ELSE p.liked END, "
//					+ "t.createdDate, t.modifiedDate) " + 
//					"FROM TodaysWs t " + 
//					"LEFT JOIN t.wsAdmin a " + 
//					"LEFT JOIN t.wsPrivate p " + 
//					"LEFT JOIN a.likes l " + 
//					"ON l.userIdx = :userIdx " + 
//					"WHERE t.userIdx = :userIdx")
//	Optional<TodaysWsResponseDto> findWithLikeByUserIdx(@Param("userIdx") Long userIdx);
}
