package com.ssafy.fundyou1.member.repository;

import com.ssafy.fundyou1.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "select * from member where member_id = :memberId", nativeQuery = true)
    Optional<Member> findByMemberId(@Param("memberId") Long memberId);

    Optional<Member> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);


    // point 차감
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update member set point = point - :point where member_id = :memberId", nativeQuery = true)
    void minusPoint(@Param("memberId") Long memberId, @Param("point") int point);

    @Modifying
    @Query(value = "update member set point = point + :point where member_id = :member_id",nativeQuery = true)
    Integer chargePoint(@Param("point") Long point, @Param("member_id") Long member_id);

    @Transactional
    @Modifying
    @Query(value = "update member set member_status = false, deleted_at = :currentTime where member_id = :currentMemberId",nativeQuery = true)
    Integer withdrawMembership(@Param("currentMemberId") Long currentMemberId, @Param("currentTime") LocalDateTime currentTime);
}
