package com.monprojet.backend.repository;

import com.monprojet.backend.dto.TacheResume;
import com.monprojet.backend.model.Tache;
import com.monprojet.backend.model.Utilisateur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import java.util.List;

/**
 * Fragment de TacheRepository (suffixe "Impl" détecté par Spring Data).
 * SELECT explicite colonne par colonne : "fichiers" n'apparaît pas dans le SQL,
 * la base ne le lit donc pas du tout.
 */
public class TacheResumeRepositoryImpl implements TacheResumeRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<TacheResume> findResumes(Specification<Tache> spec, Pageable pageable) {
        TypedQuery<TacheResume> query = requete(spec, pageable.getSort());
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList(), pageable, compter(spec));
    }

    @Override
    public List<TacheResume> findResumes(Specification<Tache> spec, Sort sort) {
        return requete(spec, sort).getResultList();
    }

    private TypedQuery<TacheResume> requete(Specification<Tache> spec, Sort sort) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TacheResume> cq = cb.createQuery(TacheResume.class);
        Root<Tache> root = cq.from(Tache.class);
        Join<Tache, Utilisateur> utilisateur = root.join("utilisateur", JoinType.LEFT);
        cq.select(cb.construct(TacheResume.class,
                root.get("id"), root.get("titre"), root.get("description"),
                root.get("priorite"), root.get("statut"), root.get("avancement"),
                root.get("dateEcheance"), root.get("dateCreation"), root.get("client"),
                root.get("clientFinal"), root.get("adresse"), root.get("chiffreAffaire"),
                root.get("numDevis"), root.get("caDevis"), root.get("assignes"),
                root.get("etapes"), utilisateur));
        Predicate filtre = spec == null ? null : spec.toPredicate(root, cq, cb);
        if (filtre != null) cq.where(filtre);
        if (sort != null && sort.isSorted()) cq.orderBy(QueryUtils.toOrders(sort, root, cb));
        return em.createQuery(cq);
    }

    private long compter(Specification<Tache> spec) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Tache> root = cq.from(Tache.class);
        cq.select(cb.count(root));
        Predicate filtre = spec == null ? null : spec.toPredicate(root, cq, cb);
        if (filtre != null) cq.where(filtre);
        return em.createQuery(cq).getSingleResult();
    }
}
