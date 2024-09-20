package unip.universityInParty.domain.party.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import unip.universityInParty.domain.course.dto.CourseDto;
import unip.universityInParty.domain.party.dto.request.PartyDto;
import unip.universityInParty.domain.party.dto.response.PartyDetailDto;
import unip.universityInParty.domain.party.service.PartyService;
import unip.universityInParty.global.baseResponse.ResponseDto;
import unip.universityInParty.global.security.custom.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/party")
@RequiredArgsConstructor
public class PartyController {
    private final PartyService partyService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPartyById(@PathVariable Long id){
        PartyDetailDto partyDetailDto = partyService.getPartyDetailById(id);
        return ResponseEntity.ok().body(ResponseDto.of("파티 상세 조회 성공", partyDetailDto));
    }

    @PostMapping()
    public ResponseEntity<?> createParty(@Valid @RequestPart("party") PartyDto partyDto,
                                         @Valid @RequestPart("course") List<CourseDto> courseDtos,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails){
        partyService.create(partyDto, customUserDetails.getUsername(), courseDtos);
        return ResponseEntity.ok().body(ResponseDto.of("파티 생성 성공", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParty(@PathVariable Long id,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails){
        partyService.delete(id, customUserDetails.getUsername());
        return ResponseEntity.ok().body(ResponseDto.of("파티 제거 성공", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateParty(@PathVariable Long id,
                                         @Valid @RequestPart("party") PartyDto partyDto,
                                         @Valid @RequestPart("course") List<CourseDto> courseDtos,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails){
        partyService.update(id, partyDto, customUserDetails.getUsername(), courseDtos);
        return ResponseEntity.ok().body(ResponseDto.of("파티 업데이트 성공", null));
    }

}
