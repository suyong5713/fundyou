package com.ssafy.fundyou1.cart.api;

import com.ssafy.fundyou1.cart.dto.CartItemResponseDto;
import com.ssafy.fundyou1.cart.dto.CartRequestDto;
import com.ssafy.fundyou1.cart.entity.Cart;
import com.ssafy.fundyou1.cart.repository.CartRepository;
import com.ssafy.fundyou1.cart.service.CartService;
import com.ssafy.fundyou1.global.dto.BaseResponseBody;
import com.ssafy.fundyou1.global.security.SecurityUtil;
import com.ssafy.fundyou1.member.dto.response.MemberResponseDto;
import com.ssafy.fundyou1.member.entity.Member;
import com.ssafy.fundyou1.member.repository.MemberRepository;
import com.ssafy.fundyou1.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api")
@Api(tags = {"장바구니"})
public class CartRestController {

    @Autowired
    CartService cartService;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CartRepository cartRepository;

    // 장바구니에 아이템을 추가
    @PostMapping(value = "/cart")
    public @ResponseBody
    ResponseEntity  addCartItem(@RequestBody @Valid CartRequestDto cartRequestDto){

        MemberResponseDto responseDto= memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));

        Long memberId = responseDto.getId();

        Long itemId = cartRequestDto.getItemId();

        Cart cart = cartService.findOneCartItem(memberId, itemId);
        // 장바구니에 동일한 아이템이 있으면 장바구니 아이템 개수만 업데이트
        if (cart != null) {
            int addcount = cartService.updateAddCartItem(cartRequestDto, memberId);
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success", addcount ));
        } else {
            // 동일한 아이템이 없으면 아이템 추가해주기
            Long cartId = cartService.addCart(cartRequestDto);
            return new ResponseEntity<Long>(cartId, HttpStatus.OK);

        }

    }

    // 회원의 장바구니 아이템 조회
    @GetMapping("/cart/list")
    @ApiOperation(value = "장바구니 아이템 조회", notes = "회원의 장바구니 목록을 반환한다.")
    public ResponseEntity<List<CartItemResponseDto>> getAllCartItems() {

        List<CartItemResponseDto> cartList = cartService.findCartItemsByCartId(SecurityUtil.getCurrentMemberId());

        return ResponseEntity.status(HttpStatus.OK).body(cartList);
    }

    // 회원의 장바구니 아이템 삭제. itemId를 전달받는다.
    @DeleteMapping("/cartItem/{itemid}")
    @ApiOperation(value = "장바구니 아이템 삭제", notes = "<strong>장바구니 목록 id를 받아</strong> 장바구니 목록에서 아이템을 삭제한다.")
    public ResponseEntity deleteCartItem(@PathVariable Long itemid){

        MemberResponseDto meDto = memberService.getMyInfo();

        Member member = memberRepository.findByUsername(meDto.getUsername());

        Long memberId = member.getId();

        try {
            // 삭제 했으면 현재 남아 있는 장바구니 내역을 반환한다.
            List<CartItemResponseDto> cartItemResponseDtos = cartService.deleteByCartItemId(itemid);
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success", cartItemResponseDtos ));
        } catch (Exception e){
            return ResponseEntity.status(403).body(BaseResponseBody.of(403, "fail", null));
        }

    }

    // 장바구니 내부에서 아이템 개수 수정 (아이템 추가)
    @PutMapping("/cart")
    @ApiOperation(value = "장바구니 아이템 개수 추가", notes = "<strong>장바구니 목록 아이템 id를 받아</strong> 장바구니 목록에서 아이템을 개수 추가한다.")
    public ResponseEntity updateCartItem(@RequestBody CartRequestDto cartRequestDto) {

        MemberResponseDto meDto = memberService.getMyInfo();

        Member member = memberRepository.findByUsername(meDto.getUsername());

        Long memberId = member.getId();

        try {
            // 장바구니 내부에서 아이템 개수 추가
            int addcount = cartService.updateAddCartItem(cartRequestDto, memberId);
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success", addcount ));
        } catch (Exception e){
            return ResponseEntity.status(403).body(BaseResponseBody.of(403, "fail", null));
        }

    }


}
