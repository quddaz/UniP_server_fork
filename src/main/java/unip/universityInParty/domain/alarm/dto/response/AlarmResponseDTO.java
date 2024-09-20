package unip.universityInParty.domain.alarm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmResponseDTO {
    private long id; // 알람 ID
    private Long receiverId; // 수신자 ID
    private Long senderId; // 발신자 ID
    private String senderName; // 발신자 이름
    private String senderProfileImage; // 발신자 프로필 이미지
    private String partyTitle; // 파티 제목
}
