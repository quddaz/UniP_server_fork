package unip.universityInParty.refresh;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import unip.universityInParty.domain.party.entity.Party;
import unip.universityInParty.domain.party.repository.PartyRepository;
import unip.universityInParty.domain.pmList.entity.Enum.PartyRole;
import unip.universityInParty.domain.pmList.service.PMListService;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class PartyServiceConcurrencyTest {

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PMListService pmListService;

    /*
    @Test
    void testConcurrencyOnJoinParty() throws InterruptedException {
        // Thread 배열 생성
        int threadCount = 4;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            long id = i + 1;
            threads[i] = new Thread(() -> {
                try {
                    pmListService.createJoinParty(PartyRole.USER, id, 1L);
                } catch (Exception e) {
                    System.err.println("Failed to join party: " + e.getMessage());
                }
            });
        }

        // When
        for (Thread thread : threads) {
            thread.start(); // 스레드 시작
        }

        for (Thread thread : threads) {
            thread.join(); // 모든 스레드가 종료될 때까지 대기
        }

        // Then
        Party updatedParty = partyRepository.findById(1L)
            .orElseThrow(() -> new IllegalStateException("Party not found"));
        System.out.println("People Count in Party: " + updatedParty.getPeopleCount());

        // Assertions
        assertThat(updatedParty.getPeopleCount()).isEqualTo(threadCount);
    }
     */
}
