package kuit.server.dto.user;

import kuit.server.dto.auth.JWT;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUserResponse {

    private long userId;
    private JWT jwt;

}
