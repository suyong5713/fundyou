package com.ssafy.fundyou1.auth.controller;

import com.ssafy.fundyou1.auth.controller.dto.request.LoginRequest;
import com.ssafy.fundyou1.auth.controller.dto.request.ReissueRequest;
import com.ssafy.fundyou1.auth.domain.Authority;
import com.ssafy.fundyou1.auth.domain.KakaoSocialLoginResponse;
import com.ssafy.fundyou1.auth.service.AuthService;
import com.ssafy.fundyou1.global.dto.TokenDto;
import com.ssafy.fundyou1.global.dto.TokenRequestDto;
import com.ssafy.fundyou1.member.dto.request.MemberLoginRequestDto;
import com.ssafy.fundyou1.member.dto.request.MemberRequestDto;
import com.ssafy.fundyou1.member.dto.response.MemberResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Api(tags = {"로그인"})
public class AuthRestController {
    Logger logger = LoggerFactory.getLogger(getClass());
    private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/members/login")
    @ApiOperation(value = "일반 로그인", notes = "일반 로그인 API")
    @ApiResponses({
        @ApiResponse(code = 401, message = "UNAUTHORIZED\n일치하지 않는 비밀번호(M04)"),
        @ApiResponse(code = 404, message = "NOT FOUND\n존재하지 않는 로그인 아이디(M01)")
    })
    public ResponseEntity<TokenDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        return ResponseEntity.ok(authService.login(memberLoginRequestDto));
    }



    @PostMapping("members/social/kakao")
    @ApiOperation(value = "카카오 소셜 로그인", notes = "소셜 로그인 API")
    public ResponseEntity<TokenDto> kakaoLogin(@RequestBody String accessToken, HttpServletResponse response){
        // 카카오 로그인 서비스 호출. 카카오 API response return.
        KakaoSocialLoginResponse rEntity = authService.kakaoLoginService(accessToken);
        // response의 body에 회원정보가 있다.
//        authService.saveKaKaoUser(rEntity, response);

        String kakaoId = String.valueOf(rEntity.getId());

        String passwerd = "fundyou"+String.valueOf(rEntity.getId());

        // 회원가입
        authService.signup(MemberRequestDto.builder()
                .loginId(String.valueOf(rEntity.getId()))
                .username(rEntity.getProperties().getNickname())
                .profileImg(rEntity.getKakao_account().getProfile_image_url())
                .password("fundyou"+String.valueOf(rEntity.getId()))
                .build());
        logger.debug("여기는 signup (Controller)");
        //로그인 하고 토큰 발급받기
        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                        .loginId(kakaoId)
                        .password(passwerd)
                        .build();
        logger.debug("여기는 토큰발급받기 직전 (Controller)");
        return ResponseEntity.ok(authService.login(memberLoginRequestDto));
    }

    @PostMapping("/members/reissue")
    @ApiOperation(value = "토큰 재발급", notes = "토큰을 재발급하는 API")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

}
