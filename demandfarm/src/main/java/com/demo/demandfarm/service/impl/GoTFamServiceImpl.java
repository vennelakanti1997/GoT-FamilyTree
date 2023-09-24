package com.demo.demandfarm.service.impl;

import com.demo.demandfarm.dto.CharacterDetails;
import com.demo.demandfarm.dto.FamilyDetails;
import com.demo.demandfarm.dto.FamilyTreeNode;
import com.demo.demandfarm.dto.error.ErrorResponse;
import com.demo.demandfarm.dto.error.ErrorWrapper;
import com.demo.demandfarm.entity.Favorites;
import com.demo.demandfarm.entity.GoTFamTreeEntity;
import com.demo.demandfarm.error.BusinessException;
import com.demo.demandfarm.repository.FavoritesRepository;
import com.demo.demandfarm.repository.GoTFamTreeRepository;
import com.demo.demandfarm.service.GoTFamService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class GoTFamServiceImpl implements GoTFamService {

    @Autowired
    private transient GoTFamTreeRepository goTFamTreeRepository;

    @Autowired
    private transient FavoritesRepository favoritesRepository;
    @Override
    public List<String> getDistictHouses() {
        return goTFamTreeRepository.findDistinctHouseNames();
    }

    @Override
    public List<FamilyDetails> getHouseMembers(final String houseName) {
        log.info("Fetching house member details of {}", houseName);
        final List<GoTFamTreeEntity> characters =  goTFamTreeRepository.findMembersByHouseName(houseName);
        final List<FamilyDetails> familyDetails = new ArrayList<>();
        if(!characters.isEmpty()){
            characters.forEach(character->{
                final FamilyDetails familyDetail = new FamilyDetails(character);
                favoritesRepository.findIdByCharacterId(character.getId())
                        .ifPresent(familyDetail::setFavoriteId);
                familyDetails.add(familyDetail);
            });

        }
        return familyDetails;
    }

    @Override
    public FamilyTreeNode getHouseMemberTree(final String houseName) {
        final List<FamilyDetails> familyDetailsList = getHouseMembers(houseName);
        final Map<String, FamilyTreeNode> rootCharactersMap = new TreeMap<>();
        if (!familyDetailsList.isEmpty()){
            if (familyDetailsList.size()==1){
                final JsonNode data = familyDetailsList.get(0).getCharacterData().getData();
                final FamilyTreeNode character = new FamilyTreeNode(data.get("characterName").asText());
                character.addAdditionalDetails("favoriteId",familyDetailsList.get(0).getFavoriteId()==null?null:familyDetailsList.get(0).getFavoriteId().toString());
                character.addAdditionalDetails("id",familyDetailsList.get(0).getCharacterData().getId().toString());
                character.addAdditionalDetails("imageThumbNail",data.get("characterImageThumb")==null?null:data.get("characterImageThumb").asText());
                rootCharactersMap.put(data.get("characterName").asText(),character);

            } else{
                //Loop to get root nodes
                for (FamilyDetails familyDetail:familyDetailsList
                ) {
                    final JsonNode data = familyDetail.getCharacterData().getData();
                    if (!data.has("parents") && (data.has("parentOf")||data.has("siblings"))){
                        final FamilyTreeNode character = new FamilyTreeNode(data.get("characterName").asText());
                        character.addAdditionalDetails("favoriteId",familyDetail.getFavoriteId()==null?null:familyDetail.getFavoriteId().toString());
                        character.addAdditionalDetails("id",familyDetail.getCharacterData().getId().toString());
                        character.addAdditionalDetails("imageThumbNail",data.get("characterImageThumb")==null?null:data.get("characterImageThumb").asText());
                        rootCharactersMap.put(data.get("characterName").asText(),character);
                    }
                }

                //loop to add children nodes to the root nodes and add attributes
                for (FamilyDetails familyDetail:familyDetailsList
                ) {
                    final JsonNode data = familyDetail.getCharacterData().getData();
                    if (data.has("parents")){
                        final JsonNode parents = data.get("parents");
                        for (JsonNode parent:parents
                        ) {
                            if (rootCharactersMap.containsKey(parent.asText())){
                                final FamilyTreeNode child = new FamilyTreeNode(data.get("characterName").asText());
                                child.addAdditionalDetails("favoriteId",familyDetail.getFavoriteId() == null?null:familyDetail.getFavoriteId().toString());
                                child.addAdditionalDetails("id",familyDetail.getCharacterData().getId().toString());
                                child.addAdditionalDetails("imageThumbNail",parents.get("characterImageThumb")==null?null:parents.get("characterImageThumb").asText());
                                rootCharactersMap.get(parent.asText()).addChild(child);
                            }
                        }
                    }
                    if(data.has("marriedEngaged")){
                        final JsonNode spouses =data.get("marriedEngaged");
                        for (JsonNode spouse :spouses
                        ) {
                            if (rootCharactersMap.containsKey(spouse.asText())){
                                final FamilyTreeNode character = rootCharactersMap.get(spouse.asText());
                                character.addAdditionalDetails("spouse",data.get("characterName").asText());
                            }
                        }

                    }
                }
            }


        }
        final FamilyTreeNode houseFamily;
        if (rootCharactersMap.size()>1){
            houseFamily = new FamilyTreeNode(houseName);
            rootCharactersMap.forEach((key,value)->{
                houseFamily.addChild(value);
            });
            return houseFamily;
        }else{
            houseFamily= rootCharactersMap.values().stream().toList().get(0);
        }
        return houseFamily;
    }


    @Override
    public UUID addFavorite(final UUID characterId) {
        final UUID[] characterIdArray = new UUID[1];
        favoritesRepository.findIdByCharacterId(characterId)
                .ifPresentOrElse(id-> characterIdArray[0] = id,()-> characterIdArray[0] =favoritesRepository.save(new Favorites(characterId)).getId());

        return characterIdArray[0];
    }

    @Override
    public CharacterDetails getCharacterByName(final String characterName, final String houseName) {
        log.info("Fetching details of {}",characterName);
        final GoTFamTreeEntity entity = goTFamTreeRepository.findMembersByCharacterNameAndHouseName(characterName, houseName)
                .orElseThrow(()->new BusinessException(HttpStatus.BAD_REQUEST,
                new ErrorWrapper(List.of(new ErrorResponse(9008,String.format("No details found for %s in %s",characterName, houseName))))));
        return new CharacterDetails(entity.getId(),entity.getData());
    }
}
