package com.monprojet.backend.repository;
import com.monprojet.backend.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByUtilisateurId(Long userId);
}