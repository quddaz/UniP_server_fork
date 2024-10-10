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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/pm")
@Tag(name = "PM 리스트", description = "파티 멤버 관리 API")
public class PMListController {
    private final PMListService pmListService;

    @PostMapping("/{id}")
    @Operation(summary = "파티 가입", description = "주어진 ID의 파티에 가입합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "파티 가입 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<?> joinParty(@PathVariable Long id,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("User {} joined party {}", customUserDetails.getUsername(), id);
        pmListService.createJoinParty(PartyRole.USER, customUserDetails.getId(), id);
        return ResponseEntity.ok().body(ResponseDto.of("파티 가입 성공", id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "파티 탈퇴", description = "주어진 ID의 파티에서 탈퇴합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "파티 탈퇴 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<?> leavingParty(@PathVariable Long id,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("User {} left party {}", customUserDetails.getUsername(), id);
        pmListService.deletePartyMember(customUserDetails.getId(), id);
        return ResponseEntity.ok().body(ResponseDto.of("파티 탈퇴 성공", id));
    }
    @GetMapping("/{id}")
    @Operation(summary = "파티 가입자 조회", description = "주어진 ID의 파티 가입자를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "파티 가입자 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "404", description = "파티를 찾을 수 없음")
    })
    public ResponseEntity<?> getPMList(@PathVariable Long id) {
        return ResponseEntity.ok().body(ResponseDto.of("파티 가입자 조회 성공", pmListService.getMemberIdsByPartyId(id)));
    }
}
