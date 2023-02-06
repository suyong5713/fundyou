package com.ssafy.fundyou1.cart.dto;

import com.ssafy.fundyou1.item.entity.Item;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDto {

    @ApiModelProperty(name = "장바구니 목록 아이디", example = "1,2")
    Long cartItemId;
    @ApiModelProperty(name = "아이템 아이디", example = "1,2")
    Long itemId;
    @ApiModelProperty(name = "아이템 가격", example = "10000")
    int price;
    @ApiModelProperty(name = "아이템 이미지", example = "ssafy/img/thumbnail.jpg")
    String image;
    @ApiModelProperty(name = "아이템 타이틀", example = "쇼파")
    String title;
    @ApiModelProperty(name = "AR가능여부", example = "N / Y")
    String isAr;


    @ApiModelProperty(name = "아이템 개수", example = "1,2,3")
    int count;


    public CartItemResponseDto(Item item, Long cartItemId, int count){
        this.cartItemId = cartItemId;
        this.itemId  = item.getItemId();
        this.price = item.getPrice();
        this.image = item.getImage();
        this.title = item.getTitle();
        this.isAr = item.getIsAr();
        this.count = count;
    }

}