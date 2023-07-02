package kuit.server.controller;

import kuit.server.common.argument_resolver.PreAuthorize;
import kuit.server.common.exception.UserException;
import kuit.server.common.exception.jwt.unauthorized.JwtInvalidTokenException;
import kuit.server.common.response.BaseResponse;
import kuit.server.dto.auth.JWT;
import kuit.server.dto.auth.LoginRequest;
import kuit.server.dto.auth.LoginResponse;
import kuit.server.dto.auth.RefreshRequest;
import kuit.server.service.AuthService;
import kuit.server.util.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static kuit.server.common.response.status.BaseExceptionResponseStatus.INVALID_TOKEN;
import static kuit.server.common.response.status.BaseExceptionResponseStatus.INVALID_USER_VALUE;
import static kuit.server.util.BindingResultUtils.getErrorMessages;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@Validated @RequestBody LoginRequest authRequest, BindingResult bindingResult) {
        log.info("[AuthController.login]");
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        return new BaseResponse<>(authService.login(authRequest));
    }

    /**
     * 권한이 필요한 작업 시 클라이언트에서 Body에 access Token을 담아 Request
     * 1.   access token 유효
     *      바로 인가
     * 2.   access token 만료 -> 서버에서 오류 코드를 보냄
     *      오류 코드를 받은 클라이언트는 다시 access Token & refresh Token을 담아 /auth/refresh Requst
     * 2-1. refresh token 유효
     *      access Token & refresh Token 재발급
     * 2-2. refresh token 만료 -> 만료, 유효하지 않음 등의 오류 코드를 보냄.
     *      오류 코드를 받은 클라이언트 측에서 로그인 창으로 redirect 구현
     */
    @PostMapping("/refresh")
    public BaseResponse<JWT> refresh(@Validated @RequestBody RefreshRequest refreshRequest, BindingResult bindingResult) {
        log.info("[AuthController.refresh]");

        if(bindingResult.hasErrors()){
            // 로그인 창으로 redirect
            throw new JwtInvalidTokenException(INVALID_TOKEN);
        }

        return new BaseResponse<>(authService.refresh(refreshRequest));
    }




    /**
     * 인가(JWT 검증) 테스트
     */
    @GetMapping("/test")
    public BaseResponse<String> checkAuthorization(@PreAuthorize long userId) {
        log.info("[AuthController.checkAuthorization]");
        return new BaseResponse<>("userId=" + userId);
    }

}