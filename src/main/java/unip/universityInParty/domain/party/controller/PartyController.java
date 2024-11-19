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
import unip.universityInParty.domain.oauth.dto.AuthMember;
import unip.universityInParty.domain.party.dto.request.PartyDto;
import unip.universityInParty.domain.party.dto.request.PartyGptDto;
import unip.universityInParty.domain.party.dto.request.PartyGptPrompt;
import unip.universityInParty.domain.party.dto.response.PartyDetailDto;
import unip.universityInParty.domain.party.dto.response.PartyMyDto;
import unip.universityInParty.domain.party.dto.response.PartyResponseDto;
import unip.universityInParty.domain.party.entity.Party;
import unip.universityInParty.domain.party.entity.type.PartyType;
import unip.universityInParty.domain.party.service.ChatGptService;
import unip.universityInParty.domain.party.service.PartyService;
import unip.universityInParty.domain.pmList.entity.Enum.PartyRole;
import unip.universityInParty.domain.pmList.service.PMListService;
import unip.universityInParty.global.baseResponse.ResponseDto;

import java.util.List;

@RestController
@RequestMapping("/party")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "파티", description = "파티 관련 API")
public class PartyController {
    private final PartyService partyService;
    private final PMListService pmListService;
    private final ChatGptService chatGptService;

    @GetMapping("/{id}")
    @Operation(summary = "파티 상세 조회", description = "주어진 ID에 해당하는 파티의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "파티 상세 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyDetailDto.class))),
    })
    public ResponseEntity<?> getPartyById(@PathVariable Long id) {
        PartyDetailDto partyDetailDto = partyService.getPartyDetailById(id);
        return ResponseEntity.ok().body(ResponseDto.of("파티 상세 조회 성공", partyDetailDto));
    }

    @PostMapping()
    @Operation(summary = "파티 생성", description = "새로운 파티를 생성합니다.")
    public ResponseEntity<?> createParty(@Valid @RequestBody PartyDto partyDto,
                                         @AuthenticationPrincipal AuthMember authMember) {
        Party party = partyService.create(partyDto, authMember.getId());
        pmListService.createJoinParty(PartyRole.MASTER, authMember.getId(), party.getId());
        return ResponseEntity.ok().body(ResponseDto.of("파티 생성 성공", party.getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "파티 삭제", description = "주어진 ID에 해당하는 파티를 삭제합니다.")
    public ResponseEntity<?> deleteParty(@PathVariable Long id,
                                         @AuthenticationPrincipal AuthMember authMember) {
        partyService.delete(id, authMember.getId());
        return ResponseEntity.ok().body(ResponseDto.ok());
    }

    @PutMapping("/{id}")
    @Operation(summary = "파티 업데이트", description = "주어진 ID에 해당하는 파티를 업데이트합니다.")
    public ResponseEntity<?> updateParty(@PathVariable Long id,
                                         @Valid @RequestBody PartyDto partyDto,
                                         @AuthenticationPrincipal AuthMember authMember) {
        partyService.update(id, partyDto, authMember.getId(), partyDto.courses());
        return ResponseEntity.ok().body(ResponseDto.ok());
    }

    @GetMapping
    @Operation(summary = "파티 전체 조회", description = "파티 전체 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "파티 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyDetailDto.class)))
    public ResponseEntity<?> getParty(@RequestParam(required = false) PartyType partyType) {
        List<PartyResponseDto> parties = partyService.getPartyMainPage(partyType);
        return ResponseEntity.ok().body(ResponseDto.of("파티 전체 조회 성공", parties));
    }

    @GetMapping("/nooffset")
    @Operation(summary = "파티 전체 성능 개선 페이징 조회", description = "파티 전체 정보를 조회합니다.")
    public ResponseEntity<?> getPartyPage2(@RequestParam(required = false) PartyType partyType, @RequestParam(required = false) int lastId) {
        List<PartyResponseDto> parties = partyService.getPartyPage(partyType, lastId, 5);
        return ResponseEntity.ok().body(ResponseDto.of("파티 전체 조회 성공", parties));
    }

    @GetMapping("/my")
    @Operation(summary = "자신의 파티 조회", description = "친구를 파티 초대하기 위한 API")
    @ApiResponse(responseCode = "200", description = "파티 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyMyDto.class)))
    public ResponseEntity<?> getMyParty(@AuthenticationPrincipal AuthMember authMember) {
        List<PartyMyDto> parties = partyService.getMyParty(authMember.getId());
        return ResponseEntity.ok().body(ResponseDto.of("자신의 파티 조회 성공", parties));
    }

    @PostMapping(value = "/gpt")
    @Operation(summary = "gpt 응답 생성", description = "주어진 질문 기반 gpt 응답 생성")
    @ApiResponse(responseCode = "200", description = "응답 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyGptDto.class)))
    public ResponseEntity<?> getGpt(@RequestBody PartyGptPrompt prompt) {
        return ResponseEntity.ok().body(ResponseDto.of("gpt 루트 생성 성공", chatGptService.getChatResponse(prompt.prompt())));
    }

    @PostMapping(value = "/gpt/create")
    @Operation(summary = "gpt 기반 파티 생성", description = "gpt 응답으로 파티를 생성")
    @ApiResponse(responseCode = "200", description = "gpt 기반 파티 생성", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyGptDto.class)))
    public ResponseEntity<?> createGpt(@RequestBody PartyGptDto partyGptDto,
                                       @AuthenticationPrincipal AuthMember authMember) {
        Party party = partyService.create(PartyDto.toPartyDto(partyGptDto), authMember.getId());
        pmListService.createJoinParty(PartyRole.MASTER, authMember.getId(), party.getId());
        return ResponseEntity.ok().body(ResponseDto.of("gpt 기반 파티 생성 성공", party.getId()));
    }
}

