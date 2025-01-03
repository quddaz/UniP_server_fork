package unip.universityInParty.domain.member.repository.init;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.User;
import unip.universityInParty.domain.member.entity.Enum.Role;
import unip.universityInParty.domain.member.entity.Enum.Status;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.global.util.DummyDataInit;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Order(1)
@DummyDataInit
public class MemberInitializer implements ApplicationRunner {


    private final MemberRepository memberRepository;


    @Override
    public void run(ApplicationArguments args) {
        if (memberRepository.count() > 0) {
            log.info("[User] 더미 데이터 존재");
        } else {
            List<Member> li = new ArrayList<>();
            for(int i = 0; i < 5; i++){
                li.add(Member.builder()
                    .username("user" + i)
                    .name("name" + i)
                    .email("user" + i +"@gmail.com")
                    .profileImage("https://quddaztestbucket.s3.ap-northeast-2.amazonaws.com/default.png")
                    .role(Role.GUEST)
                    .point(0)
                    .auth(false)
                    .status(Status.BORED)
                    .build());
            }
            memberRepository.saveAll(li);
        }
    }
}