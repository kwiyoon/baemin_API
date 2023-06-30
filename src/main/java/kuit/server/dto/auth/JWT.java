package kuit.server.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JWT {
    private String accessToken;
    private String refreshToken;
}
