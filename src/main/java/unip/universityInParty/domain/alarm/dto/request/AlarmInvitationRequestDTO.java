package unip.universityInParty.domain.alarm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmInvitationRequestDTO {
    private Long party;
    private Long receiver;
}
