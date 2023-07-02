package kuit.server.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshRequest {
    private long userId;
    private String AccessToken;
    private String RefreshToken;
}
