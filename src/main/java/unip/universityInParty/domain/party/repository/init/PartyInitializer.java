package unip.universityInParty.domain.party.repository.init;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import unip.universityInParty.domain.member.entity.Enum.Role;
import unip.universityInParty.domain.member.entity.Enum.Status;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.domain.party.entity.Party;
import unip.universityInParty.domain.party.entity.type.PartyType;
import unip.universityInParty.domain.party.repository.PartyRepository;
import unip.universityInParty.global.util.DummyDataInit;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Order(2)
@DummyDataInit
public class PartyInitializer implements ApplicationRunner {


    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (partyRepository.count() > 0) {
            log.info("[User] 더미 데이터 존재");
        } else {
            List<Party> li = new ArrayList<>();
            Member member = memberRepository.findById(5L).get();
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = LocalDateTime.now().plusMonths(10);
            for(int i = 0; i < 5; i++){
                li.add(Party.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .partyType(PartyType.COMPREHENSIVE)
                    .partyLimit(10)
                    .peopleCount(0)
                    .startTime(start)
                    .endTime(end)
                    .member(member)
                    .isClosed(false)
                    .build());
            }

            partyRepository.saveAll(li);
        }
    }
}