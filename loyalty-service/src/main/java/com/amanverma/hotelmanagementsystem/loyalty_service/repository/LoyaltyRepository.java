package com.amanverma.hotelmanagementsystem.loyalty_service.repository;

import com.amanverma.hotelmanagementsystem.loyalty_service.model.Loyalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoyaltyRepository extends JpaRepository<Loyalty, Long> {

    Optional<Loyalty> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}

