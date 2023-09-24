package com.demo.demandfarm.repository;

import com.demo.demandfarm.entity.GoTFamTreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoTFamTreeRepository extends JpaRepository<GoTFamTreeEntity, UUID> {

    @Transactional(readOnly = true)
    @Query(value="SELECT DISTINCT(gfte.data->>'houseName') as houseName FROM gotfam_tree_entity gfte ORDER BY houseName",nativeQuery = true)
    List<String> findDistinctHouseNames();

    @Transactional(readOnly = true)
    @Query(nativeQuery = true,
    value = "SELECT * FROM gotfam_tree_entity WHERE data->>'houseName' = :houseName")
    List<GoTFamTreeEntity> findMembersByHouseName(@Param("houseName") final String houseName);

    @Transactional(readOnly = true)
    @Query(nativeQuery = true,
            value = "SELECT * FROM gotfam_tree_entity WHERE data->>'characterName' = :characterName AND data->>'houseName' = :houseName")
    Optional<GoTFamTreeEntity> findMembersByCharacterNameAndHouseName(@Param("characterName") final String characterName,@Param("houseName") final String houseName);

}
