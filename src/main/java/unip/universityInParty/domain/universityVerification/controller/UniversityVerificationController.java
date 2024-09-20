package unip.universityInParty.domain.universityVerification.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import unip.universityInParty.domain.universityVerification.dto.UvRequestDTO;
import unip.universityInParty.domain.universityVerification.dto.UvValidRequestDTO;
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
    public ResponseEntity<?> createVerification(@RequestBody @Valid UvRequestDTO uvRequestDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails){
        universityVerificationService.sendVerificationEmail(uvRequestDTO.getEmail());
        return ResponseEntity.ok().body(ResponseDto.of(
            "인증 코드 생성 성공",
            null
        ));
    }
    @GetMapping("/re")
    public ResponseEntity<?> reVerification(@RequestBody @Valid UvRequestDTO uvRequestDTO){
        universityVerificationService.reRequest(uvRequestDTO.getEmail());
        return ResponseEntity.ok().body(ResponseDto.of(
            "인증 코드 재생성 성공",
            null
        ));
    }
    @PostMapping()
    public ResponseEntity<?> verification(@RequestBody @Valid UvValidRequestDTO uvValidRequestDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails){
        universityVerificationService.verifyAuthCode(uvValidRequestDTO.getEmail(),uvValidRequestDTO.getAuthCode(), customUserDetails.getId());
        return ResponseEntity.ok().body(ResponseDto.of(
            "인증 성공",
            null
        ));
    }
}
