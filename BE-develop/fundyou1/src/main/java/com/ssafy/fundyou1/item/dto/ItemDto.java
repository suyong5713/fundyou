package com.ssafy.fundyou1.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.fundyou1.cart.entity.CartItem;
import com.ssafy.fundyou1.fund.entity.FundingItem;
import com.ssafy.fundyou1.item.entity.Item;
import com.ssafy.fundyou1.like.entity.LikeItem;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ItemDto {

    private Long id;

    private int price;

    private String image;

    private String descriptionImg;

    private String title;

    private String isAr;

    private String description;

    private int sellingCount;

    private String brand;

    @JsonProperty("category_id")
    private Long categoryId;

    public ItemDto(Long id, int price, String image, String descriptionImg, String title, String isAr, List<Object> description, int sellingCount, String brand, Long id1, int sellingCount1) {
    }

    public static ItemDto createItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getPrice(),
                item.getImage(),
                item.getDescriptionImg(),
                item.getTitle(),
                item.getIsAr(),
                Collections.singletonList(item.getDescription()),
                item.getSellingCount(),
                item.getBrand(),
                item.getCategory().getId(),
                item.getSellingCount()
        );
    }


    public void addsellingCount(int quantity) {
        this.sellingCount += quantity;
    }

    List<LikeItem> likeItems = new ArrayList<>();

    List<CartItem> cartItems = new ArrayList<>();

    List<FundingItem> fundingItems = new ArrayList<>();
}