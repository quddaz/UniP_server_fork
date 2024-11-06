package unip.universityInParty.domain.oauth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // JSON 응답 작성 메서드
    public static void writeJsonResponse(HttpServletResponse response, Map<String, String> data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = objectMapper.writeValueAsString(data);
        response.getWriter().write(jsonResponse);
    }

    // 토큰 맵 생성 메서드
    public static Map<String, String> createTokenMap(String accessToken, String refreshToken, boolean isAuth) {
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", "bearer " + accessToken);
        tokenMap.put("refreshToken", refreshToken);
        tokenMap.put("auth", String.valueOf(isAuth));
        return tokenMap;
    }
}
