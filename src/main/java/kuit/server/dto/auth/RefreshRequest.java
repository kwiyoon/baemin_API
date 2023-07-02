package kuit.server.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshRequest {
    private long userId;
    private String accessToken;
    private String refreshToken;
}
