package com.demo.demandfarm.repository;

import com.demo.demandfarm.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, UUID> {

    @Transactional(readOnly = true)
    @Query("SELECT id FROM Favorites WHERE goTFamTreeId=:characterId")
    Optional<UUID> findIdByCharacterId(@Param("characterId") final UUID characterId);
}
