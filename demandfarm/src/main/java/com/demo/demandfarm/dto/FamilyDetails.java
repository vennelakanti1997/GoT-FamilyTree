package com.demo.demandfarm.dto;

import com.demo.demandfarm.entity.GoTFamTreeEntity;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FamilyDetails{

    private GoTFamTreeEntity characterData;
    private UUID favoriteId;

    public FamilyDetails(GoTFamTreeEntity characterData) {
        this.characterData = characterData;
    }
}
