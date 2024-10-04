package unip.universityInParty.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import unip.universityInParty.domain.party.dto.request.PartyDto;
import unip.universityInParty.domain.party.dto.response.PartyDetailDto;
import unip.universityInParty.domain.party.entity.Party;
import unip.universityInParty.domain.party.service.PartyService;
import unip.universityInParty.domain.pmList.entity.Enum.PartyRole;
import unip.universityInParty.domain.pmList.service.PMListService;
import unip.universityInParty.global.baseResponse.ResponseDto;
import unip.universityInParty.global.security.custom.CustomUserDetails;

@RestController
@RequestMapping("/party")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "파티", description = "파티 관련 API")
public class PartyController {
    private final PartyService partyService;
    private final PMListService pmListService;

    @GetMapping("/{id}")
    @Operation(summary = "파티 상세 조회", description = "주어진 ID에 해당하는 파티의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "파티 상세 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyDetailDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "파티를 찾을 수 없음")
    })
    public ResponseEntity<?> getPartyById(@PathVariable Long id) {
        PartyDetailDto partyDetailDto = partyService.getPartyDetailById(id);
        return ResponseEntity.ok().body(ResponseDto.of("파티 상세 조회 성공", partyDetailDto));
    }

    @PostMapping()
    @Operation(summary = "파티 생성", description = "새로운 파티를 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "파티 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ResponseEntity<?> createParty(@Valid @RequestBody PartyDto partyDto,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Party party = partyService.create(partyDto, customUserDetails.getUsername(), partyDto.courses());
        pmListService.createJoinParty(PartyRole.MASTER, customUserDetails.getUsername(), party.getId());
        return ResponseEntity.ok().body(ResponseDto.of("파티 생성 성공", party.getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "파티 삭제", description = "주어진 ID에 해당하는 파티를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "파티 제거 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "404", description = "파티를 찾을 수 없음")
    })
    public ResponseEntity<?> deleteParty(@PathVariable Long id,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        partyService.delete(id, customUserDetails.getUsername());
        return ResponseEntity.ok().body(ResponseDto.of("파티 제거 성공", null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "파티 업데이트", description = "주어진 ID에 해당하는 파티를 업데이트합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "파티 업데이트 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "404", description = "파티를 찾을 수 없음")
    })
    public ResponseEntity<?> updateParty(@PathVariable Long id,
                                         @Valid @RequestBody PartyDto partyDto,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        partyService.update(id, partyDto, customUserDetails.getUsername(), partyDto.courses());
        return ResponseEntity.ok().body(ResponseDto.of("파티 업데이트 성공", null));
    }
}
