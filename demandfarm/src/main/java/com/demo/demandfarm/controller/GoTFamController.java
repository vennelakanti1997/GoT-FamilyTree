package com.demo.demandfarm.controller;

import com.demo.demandfarm.dto.CharacterDetails;
import com.demo.demandfarm.dto.FamilyDetails;
import com.demo.demandfarm.dto.FamilyTreeNode;
import com.demo.demandfarm.service.GoTFamService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/characters")
@CrossOrigin(value = {"*"})
public class GoTFamController {

    @Autowired
    private transient GoTFamService goTFamService;
    @GetMapping(value = "/houses")
    public ResponseEntity<List<String>> getDistictHouses(){
        return ResponseEntity.ok(goTFamService.getDistictHouses());
    }

    @GetMapping(value = "/familytree/{houseName}")

    public ResponseEntity<List<FamilyDetails>> getHouseMembers(@PathVariable(name = "houseName")
                                                         @NotBlank(message = "houseName is required")
                                                         @Pattern(regexp = "^[a-zA-Z]+$", message = "Invalid House name") final String houseName){

        return ResponseEntity.ok(goTFamService.getHouseMembers(houseName));

    }

    @GetMapping(value = "/familytreee/{houseName}")

    public ResponseEntity<FamilyTreeNode> getHouseMembersTree(@PathVariable(name = "houseName")
                                                               @NotBlank(message = "houseName is required")
                                                               @Pattern(regexp = "^[a-zA-Z]+$", message = "Invalid House name") final String houseName){

        return ResponseEntity.ok(goTFamService.getHouseMemberTree(houseName));

    }

    @GetMapping(value = "/{houseName}/{characterName}")
    public ResponseEntity<CharacterDetails> getDataByName(@PathVariable(name = "characterName")
                                                  @NotBlank(message = "characterName is required")
                                                  @Pattern(regexp = "^[a-zA-Z ]+$", message = "Invalid Character name") final String characterName,
                                                          @PathVariable(name = "houseName")
                                                          @NotBlank(message = "houseName is required")
                                                          @Pattern(regexp = "^[a-zA-Z]+$", message = "Invalid House name") final String houseName){
        return ResponseEntity.ok(goTFamService.getCharacterByName(characterName, houseName));

    }
    @PutMapping(value = "/{id}/favourite")
    public ResponseEntity<Map<String,UUID>> addFavorite(@PathVariable(name = "id") final UUID characterId){
        return ResponseEntity.ok(Map.of("favoriteId",goTFamService.addFavorite(characterId)));
    }
}
