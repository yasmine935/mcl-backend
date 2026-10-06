package com.monprojet.backend.repository;

import com.monprojet.backend.dto.TacheResume;
import com.monprojet.backend.model.Tache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

/** Listes de projets sans la colonne "fichiers" (voir TacheResume). */
public interface TacheResumeRepository {
    Page<TacheResume> findResumes(Specification<Tache> spec, Pageable pageable);
    List<TacheResume> findResumes(Specification<Tache> spec, Sort sort);
}
