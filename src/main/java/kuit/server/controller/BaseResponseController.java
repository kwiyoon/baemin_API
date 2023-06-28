package kuit.server.controller;

import kuit.server.common.response.BaseErrorResponse;
import kuit.server.common.response.BaseResponse;
import kuit.server.temp.UserData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kuit.server.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;
import static kuit.server.common.response.status.BaseExceptionResponseStatus.INVALID_TOKEN;

@RestController
public class BaseResponseController {

    // 성공 메시지 확인
    @RequestMapping("/base-response")
    public BaseResponse<UserData> checkBaseResponse(){
        UserData absd = new UserData("absd", 23);
        return new BaseResponse<>(absd);
    }

    // 에러 메시지 확인
    @RequestMapping("/base-error-response")
    public BaseErrorResponse checkBaseErrorResponse(){
        return new BaseErrorResponse(INVALID_TOKEN);
    }
}
