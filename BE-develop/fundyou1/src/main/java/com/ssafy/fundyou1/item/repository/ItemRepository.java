package com.ssafy.fundyou1.item.repository;

import com.ssafy.fundyou1.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // 아이템 전체 조회
    @Override
    ArrayList<Item> findAll();

    // 카테고리별 아이템 불러오기
    @Query(value =
            "SELECT * " +
            "FROM item " +
            "WHERE item.category_id = :categoryId",
            nativeQuery = true)
    List<Item> findAllByCategoryId(@Param("categoryId") Long categoryId);


    // 랜덤 5개 상품 추출
    @Query(value = "SELECT * FROM item order by RAND() limit 5", nativeQuery = true)
    List<Item> findRandomItemById();


    // 조건에 맞는 상위 5개 상품 추출
    @Query(value =
            "SELECT * " +
                    "FROM item " +
                    "WHERE item.category_id = :categoryId " +
                    "AND item.price BETWEEN :minPrice AND :maxPrice " +
                    "ORDER BY item.selling_count DESC LIMIT 6",
            nativeQuery = true)
    List<Item> findTopItem(@Param("categoryId") Long categoryId, @Param("minPrice") Long minimumPrice,@Param("maxPrice") Long maxPrice);

    boolean existsByTitleAndBrand(String title, String brand);

    Item findByTitle(String title);
}
