package com.monprojet.backend.repository;

import com.monprojet.backend.model.OptionTache;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OptionTacheRepository extends JpaRepository<OptionTache, Long> {
    List<OptionTache> findByCategorieId(Long categorieId);
}
