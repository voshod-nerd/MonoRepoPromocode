package com.voshodnerd.helidon.repository;

import com.voshodnerd.helidon.model.Campaign;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CampaignRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public Optional<Campaign> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Campaign.class, id));
    }

    @Transactional
    public Campaign save(Campaign campaign) {
        if (campaign.getId() == null) {
            this.entityManager.persist(campaign);
        } else {
           return this.entityManager.merge(campaign);
        }
        return campaign;
    }

    @Transactional
    public List<Campaign> findAll() {
        return entityManager.createQuery("SELECT c FROM Campaign c", Campaign.class).getResultList();
    }

}
