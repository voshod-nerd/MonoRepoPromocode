package com.voshodnerd.promocode.issue.repository;

import com.voshodnerd.promocode.issue.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
    Optional<PromoCode> findFirstByStatus(PromoCode.PromoCodeStatus status);
}
