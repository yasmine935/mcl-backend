// ArticleAttenteRepository.java
package com.monprojet.backend.repository;
import com.monprojet.backend.model.ArticleAttente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleAttenteRepository extends JpaRepository<ArticleAttente, Long> {
    List<ArticleAttente> findByUtilisateurId(Long utilisateurId);
}
