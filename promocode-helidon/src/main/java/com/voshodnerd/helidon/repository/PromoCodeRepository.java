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
import java.util.logging.Logger;

@ApplicationScoped
public class PromoCodeRepository {

    private static final Logger LOGGER = Logger.getLogger(PromoCodeRepository.class.getName());

    @PersistenceContext
    EntityManager entityManager;




    @Transactional
    public void batchSave(List<PromoCode> promoCodes) {
        entityManager.persist(promoCodes);
    }




    @Transactional
    public void saveAll(List<PromoCode> promoCodes) {
        //entityManager.setFlushMode(FlushModeType.);
        //int count = 0;
        //int BATCH_SIZE = 100;
        for (PromoCode promoCode : promoCodes) {
            if (promoCode.getId() == null) {
                this.entityManager.persist(promoCode);
            } else {
                this.entityManager.merge(promoCode);
            }
            //count++;
            //if (count % BATCH_SIZE == 0) {
            //    LOGGER.info("Batch Save");
            //    this.entityManager.flush();
            //    this.entityManager.clear();
            //}
        }
        // Flush and clear any remaining entities
        //if (count % BATCH_SIZE != 0) {
        //    entityManager.flush();
        //   entityManager.clear();
        //}
    }

    @Transactional
    public Optional<PromoCode> findFirstByIssuedBy(String phone) {
        PromoCode result = null;
        try {
            result = (PromoCode) entityManager.createQuery("select d from PromoCode d where d.issuedBy=:phone ")
                    .setParameter("phone", phone)
                    .setMaxResults(1)
                    .getSingleResult();
            return Optional.ofNullable(result);
        } catch (NoResultException nre) {
            LOGGER.severe("Error: " + nre.getMessage());
            // Code for handling NoResultException
        } catch (NonUniqueResultException nure) {
            LOGGER.severe("Error: " + nure.getMessage());
            // Code for handling NonUniqueResultException
        } catch (Exception e) {
            LOGGER.severe("Error: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<PromoCode> findFirstByStatus(PromoCode.PromoCodeStatus status) {
        PromoCode result = null;
        try {
            result = (PromoCode) entityManager.createQuery("select d from PromoCode d where d.status=:status ")
                    .setParameter("status", status)
                    .setMaxResults(1)
                    .getSingleResult();
            return Optional.ofNullable(result);
        } catch (NoResultException nre) {
            LOGGER.severe("Error: " + nre.getMessage());
            // Code for handling NoResultException
        } catch (NonUniqueResultException nure) {
            LOGGER.severe("Error: " + nure.getMessage());
            // Code for handling NonUniqueResultException
        } catch (Exception e) {
            LOGGER.severe("Error: " + e.getMessage());
        }
        return Optional.empty();
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

