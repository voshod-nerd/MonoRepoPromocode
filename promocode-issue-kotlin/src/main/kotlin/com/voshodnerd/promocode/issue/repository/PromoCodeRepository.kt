package com.voshodnerd.promocode.issue.repository;

import com.voshodnerd.promocode.issue.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface PromoCodeRepository : JpaRepository<PromoCode, Long> {
    fun findFirstByStatus(status: PromoCode.PromoCodeStatus): PromoCode
}
