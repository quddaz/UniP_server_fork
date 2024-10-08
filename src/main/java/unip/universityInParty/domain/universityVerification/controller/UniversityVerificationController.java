package unip.universityInParty.domain.universityVerification.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import unip.universityInParty.domain.universityVerification.dto.UvRequestDTO;
import unip.universityInParty.domain.universityVerification.dto.UvValidRequestDTO;
import unip.universityInParty.domain.universityVerification.service.UniversityVerificationService;
import unip.universityInParty.global.baseResponse.ResponseDto;
import unip.universityInParty.global.security.custom.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
@RequiredArgsConstructor
@RestController
@RequestMapping("/univer")
@Tag(name = "학교인증", description = "학교 인증 API")
public class UniversityVerificationController {

    private final UniversityVerificationService universityVerificationService;

    @Operation(summary = "인증 코드 생성", description = "이메일로 인증 코드를 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "인증 코드 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping()
    public ResponseEntity<?> createVerification(@RequestBody @Valid UvRequestDTO uvRequestDTO) {
        universityVerificationService.sendVerificationEmail(uvRequestDTO.email());
        return ResponseEntity.ok().body(ResponseDto.of(
            "인증 코드 생성 성공",
            null
        ));
    }

    @Operation(summary = "인증 코드 재생성", description = "이메일로 인증 코드를 재생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "인증 코드 재생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping("/re")
    public ResponseEntity<?> reVerification(@RequestBody @Valid UvRequestDTO uvRequestDTO) {
        universityVerificationService.reRequest(uvRequestDTO.email());
        return ResponseEntity.ok().body(ResponseDto.of(
            "인증 코드 재생성 성공",
            null
        ));
    }

    @Operation(summary = "인증 코드 검증", description = "전송된 인증 코드를 검증합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "인증 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/au")
    public ResponseEntity<?> verification(@RequestBody @Valid UvValidRequestDTO uvValidRequestDTO,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        universityVerificationService.verifyAuthCode(uvValidRequestDTO.email(), uvValidRequestDTO.authCode(), customUserDetails.getId());
        return ResponseEntity.ok().body(ResponseDto.of(
            "인증 성공",
            null
        ));
    }
}
