package unip.universityInParty.domain.party.entity.type;

import lombok.Getter;

@Getter
public enum PartyType {
    RESTAURANT("식사"),
    BAR("술집"),
    COMPREHENSIVE("종합");
    private final String description;

    PartyType(String description) {
        this.description = description;
    }

}