package com.ssafy.fundyou1.fund.repository;

import com.ssafy.fundyou1.fund.entity.FundingItem;
import com.ssafy.fundyou1.fund.entity.FundingItemMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundingItemMemberRepository extends JpaRepository<FundingItemMember, Long> {
    List<FundingItemMember> findAllByMemberId(Long id);


    @Query(value = "select sum(funding_item_price) from funding_item_member fim where fim.member_id = :memberId and (fim.funding_item_id in :fundingItemIdList) ",nativeQuery = true)
    int findAllByMemberIdAndFundingItemList(@Param("memberId") Long memberId, @Param("fundingItemIdList") List<Long> fundingItemIdList);

    List<FundingItemMember> findAllByFundingItemId(Long id);

    @Query(value = "select sum(funding_item_price) from funding_item_member fim where fim.member_id = :memberId and fim.funding_item_id = :fundingItemId ",nativeQuery = true)
    int findSumByMemberIdAndFundingItemId(@Param("memberId") Long memberId, @Param("fundingItemId") Long fundingItemId);
}
