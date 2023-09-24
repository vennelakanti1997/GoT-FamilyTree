package com.demo.demandfarm.service;

import com.demo.demandfarm.dto.CharacterDetails;
import com.demo.demandfarm.dto.FamilyDetails;
import com.demo.demandfarm.dto.FamilyTreeNode;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.UUID;

public interface GoTFamService {

    List<String> getDistictHouses();

    /**
     *
     * @param houseName
     * @return
     */
    List<FamilyDetails> getHouseMembers(final String houseName);

    FamilyTreeNode getHouseMemberTree(final String houseName);
    UUID addFavorite(final UUID characterId);

    CharacterDetails getCharacterByName(final String characterName, final String houseName);
}
