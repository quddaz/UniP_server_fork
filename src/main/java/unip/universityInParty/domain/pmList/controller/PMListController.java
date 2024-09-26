package unip.universityInParty.domain.pmList.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import unip.universityInParty.domain.pmList.entity.Enum.PartyRole;
import unip.universityInParty.domain.pmList.service.PMListService;
import unip.universityInParty.global.baseResponse.ResponseDto;
import unip.universityInParty.global.security.custom.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/pm")
public class PMListController {
    private final PMListService pmListService;
    @PostMapping("/{id}")
    public ResponseEntity<?> joinParty(@PathVariable Long id,
                                    @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("User {} joined party {}", customUserDetails.getUsername(), id);
        pmListService.createJoinParty(PartyRole.USER, customUserDetails.getUsername(), id);
        return ResponseEntity.ok().body(ResponseDto.of("파티 가입 성공", id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> leavingParty(@PathVariable Long id,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("User {} left party {}", customUserDetails.getUsername(), id);
        pmListService.deletePartyMember(customUserDetails.getUsername(), id);
        return ResponseEntity.ok().body(ResponseDto.of("파티 탈퇴 성공", id));
    }
}
