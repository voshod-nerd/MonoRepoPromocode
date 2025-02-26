package com.voshodnerd.helidon.repository;

import com.voshodnerd.helidon.model.PromoCode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PromoCodeRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void saveAll(List<PromoCode> promoCodes) {
        promoCodes.forEach(promoCode -> {
            if (promoCode.getId() == null) {
                this.entityManager.persist(promoCode);
            } else {
                this.entityManager.merge(promoCode);
            }
        });
    }

    @Transactional
    public Optional<PromoCode> findFirstByStatus(PromoCode.PromoCodeStatus status) {
        try {
            var result = (PromoCode) entityManager.createQuery("select d from PromoCode d where d.promoCodeStatus=:status")
                    .setParameter("status", status)
                    .getSingleResult();
            return Optional.ofNullable(result);
        } catch (NoResultException nre) {
            // Code for handling NoResultException
        } catch (NonUniqueResultException nure) {
            // Code for handling NonUniqueResultException
        }
        return Optional.ofNullable(null);
    }

    @Transactional
    public PromoCode save(PromoCode promoCode) {
        if (promoCode.getId() == null) {
            this.entityManager.persist(promoCode);
            return promoCode;
        } else {
            return this.entityManager.merge(promoCode);
        }
    }

}

