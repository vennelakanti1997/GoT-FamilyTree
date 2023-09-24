package com.demo.demandfarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FamilyTreeNode {
    @JsonProperty("name")
    private String characterName;
    @JsonProperty("attributes")
    private Map<String, String> additionalDetails;
    private List<FamilyTreeNode> children;

    public FamilyTreeNode(final String characterName) {
        this.characterName = characterName;
        this.children = new ArrayList<>();
        this.additionalDetails = new HashMap<>();
    }

    public void addChild(final FamilyTreeNode familyTreeNode){
        children.add(familyTreeNode);
    }

    public void addAdditionalDetails(final String key, final String value){
        additionalDetails.put(key, value);
    }
}
