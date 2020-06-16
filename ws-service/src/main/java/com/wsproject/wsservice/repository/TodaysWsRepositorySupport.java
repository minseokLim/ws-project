package com.wsproject.wsservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wsproject.wsservice.domain.TodaysWs;
import com.wsproject.wsservice.dto.QTodaysWsResponseDto;
import com.wsproject.wsservice.dto.TodaysWsResponseDto;

import static com.wsproject.wsservice.domain.QTodaysWs.todaysWs;
import static com.wsproject.wsservice.domain.QWsAdmin.wsAdmin;
import static com.wsproject.wsservice.domain.QWsPrivate.wsPrivate;
import static com.wsproject.wsservice.domain.QWsAdminLike.wsAdminLike;

@Repository
public class TodaysWsRepositorySupport extends QuerydslRepositorySupport {

	private JPAQueryFactory queryFactory;
	
	public TodaysWsRepositorySupport(JPAQueryFactory queryFactory) {
		super(TodaysWs.class);
		this.queryFactory = queryFactory;
	}
	
	public Optional<TodaysWsResponseDto> findWithLikeByUserIdx(Long userIdx) {
		
		return Optional.ofNullable(queryFactory
			.select(
				new QTodaysWsResponseDto(
					todaysWs.id,
					todaysWs.userIdx,
					new CaseBuilder().when(wsAdmin.isNotNull()).then(wsAdmin.content).otherwise(wsPrivate.content),
					new CaseBuilder().when(wsAdmin.isNotNull()).then(wsAdmin.author).otherwise(wsPrivate.author),
					new CaseBuilder().when(wsAdmin.isNotNull()).then(wsAdmin.type).otherwise(wsPrivate.type),
					new CaseBuilder().when(wsAdmin.isNotNull()).then(wsAdmin.id).otherwise(wsPrivate.id),
					new CaseBuilder().when(wsAdmin.isNotNull()).then(false).otherwise(true),
					new CaseBuilder().when(wsAdmin.isNotNull().and(wsAdminLike.isNotNull())).then(true)
									.when(wsAdmin.isNotNull().and(wsAdminLike.isNull())).then(false)
									.otherwise(wsPrivate.liked),
					todaysWs.createdDate,
					todaysWs.modifiedDate
				)
			)
			.from(todaysWs)
			.leftJoin(todaysWs.wsAdmin, wsAdmin)
			.leftJoin(todaysWs.wsPrivate, wsPrivate)
			.leftJoin(wsAdmin.likes, wsAdminLike)
			.on(wsAdminLike.userIdx.eq(userIdx))
			.where(todaysWs.userIdx.eq(userIdx))
			.fetchOne());
	}
}
