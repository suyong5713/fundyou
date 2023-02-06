package com.ssafy.fundyou1.fund.repository;

import com.ssafy.fundyou1.fund.entity.FundingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FundingItemRepository extends JpaRepository<FundingItem, Long> {

    FundingItem findByFundingIdAndItemId(Long fundingId, Long itemId);


    List<FundingItem> findByFundingId(Long fundingId);
}
