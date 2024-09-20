package unip.universityInParty.domain.refresh.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unip.universityInParty.domain.refresh.entity.Refresh;
import unip.universityInParty.domain.refresh.repository.RefreshRepository;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshService {
    private final RefreshRepository refreshRepository;
    private final long refreshTokenExpireTime = 86400L; // 1일

    @Transactional
    public void addRefresh(String username, String token) {
        Refresh refresh = Refresh.builder()
            .username(username)
            .token(token)
            .expiration(refreshTokenExpireTime) // Redis의 TTL(Time-to-live) 기능 사용
            .build();
        refreshRepository.save(refresh);
    }

    @Transactional(readOnly = true)
    public Boolean existsByToken(String token) {
        return refreshRepository.existsByToken(token);
    }

    @Transactional(readOnly = true)
    public Boolean existsByUsername(String username) {
        return refreshRepository.existsByUsername(username);
    }

    @Transactional
    public void deleteByToken(String token) {
        refreshRepository.deleteByToken(token);
    }

    @Transactional
    public void deleteByUsername(String username) {
        refreshRepository.deleteByUsername(username);
    }


}