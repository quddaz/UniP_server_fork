package unip.universityInParty.refresh;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import unip.universityInParty.domain.member.entity.Enum.Role;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.domain.refresh.entity.Refresh;
import unip.universityInParty.domain.refresh.repository.RefreshRepository;
import unip.universityInParty.domain.refresh.service.RefreshService;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.global.security.jwt.JwtUtil;
import unip.universityInParty.global.util.CookieStore;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshServiceTest {
    @InjectMocks
    private RefreshService refreshService;

    @Mock
    private RefreshRepository refreshRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CookieStore cookieStore;

    @Mock
    private HttpServletResponse response;

    private final String username = "testUser";
    private final String refreshToken = "validRefreshToken";

    @Test
    public void testAddRefresh() {
        refreshService.addRefresh(username, refreshToken);
        verify(refreshRepository, times(1)).save(any(Refresh.class));
    }
    @Test
    public void testExistsByUsername() {
        when(refreshRepository.existsByUsername(username)).thenReturn(true);
        boolean exists = refreshService.existsByUsername(username);
        assertTrue(exists);
        verify(refreshRepository, times(1)).existsByUsername(username);
    }
    @Test
    public void testDeleteByUsername() {
        refreshService.deleteByUsername(username);
        verify(refreshRepository, times(1)).deleteByUsername(username);
    }
    @Test
    public void testRefreshAccessToken() {
        // given
        Member member = new Member();
        member.setUsername(username);
        member.setRole(Role.valueOf("ROLE_USER"));
        member.setAuth(true);

        when(jwtUtil.isExpired(refreshToken)).thenReturn(false);
        when(jwtUtil.getCategory(refreshToken)).thenReturn("refresh");
        when(jwtUtil.getUsername(refreshToken)).thenReturn(username);
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));
        when(jwtUtil.createAccessJwt(eq(username), anyString(), anyString(), anyBoolean())).thenReturn("newAccessToken");
        when(jwtUtil.createRefreshJwt(eq(username), anyString(), anyString(), anyBoolean())).thenReturn("newRefreshToken");
        when(cookieStore.createCookie("newRefreshToken")).thenReturn(new Cookie("refreshToken", "newRefreshToken"));

        // when
        refreshService.refreshAccessToken(refreshToken, response);

        // then
        verify(response).addCookie(any(Cookie.class));
        verify(response).setHeader("access", "newAccessToken");
    }
    @Test
    public void testRefreshAccessToken_WhenTokenExpired() {
        when(jwtUtil.isExpired(refreshToken)).thenReturn(true);

        assertThrows(CustomException.class, () -> {
            refreshService.refreshAccessToken(refreshToken, response);
        });
    }
}
