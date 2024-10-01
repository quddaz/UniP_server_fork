package unip.universityInParty.domain.alarm.dto.response;

import lombok.Builder;

@Builder
public record AlarmResponseDTO(
    long id,
    Long receiverId,
    Long senderId,
    String senderName,
    String senderProfileImage,
    String partyTitle) {
}