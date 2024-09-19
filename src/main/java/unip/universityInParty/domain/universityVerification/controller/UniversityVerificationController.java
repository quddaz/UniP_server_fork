package unip.universityInParty.domain.universityVerification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import unip.universityInParty.domain.universityVerification.entity.UniversityVerification;
import unip.universityInParty.domain.universityVerification.service.UniversityVerificationService;
import unip.universityInParty.global.baseResponse.ResponseDto;
import unip.universityInParty.global.security.custom.CustomUserDetails;

@RequiredArgsConstructor
@RestController
@RequestMapping("/univer")
public class UniversityVerificationController {
    private final UniversityVerificationService universityVerificationService;
    @GetMapping()
    public ResponseEntity<?> createVerification(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        universityVerificationService.create(customUserDetails.getId());
        return ResponseEntity.ok().body(ResponseDto.of(
            "인증 코드 생성 성공",
            null
        ));
    }
    @GetMapping("/re")
    public ResponseEntity<?> reVerification(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        universityVerificationService.reRequest(customUserDetails.getId());
        return ResponseEntity.ok().body(ResponseDto.of(
            "인증 코드 재생성 성공",
            null
        ));
    }
    @PostMapping()
    public ResponseEntity<?> verification(@RequestBody String code,
        @AuthenticationPrincipal CustomUserDetails customUserDetails){
        universityVerificationService.verificationRequest(code,customUserDetails.getId());
        return ResponseEntity.ok().body(ResponseDto.of(
            "인증 성공",
            null
        ));
    }
}
