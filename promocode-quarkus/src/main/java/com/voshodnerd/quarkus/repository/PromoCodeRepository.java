package com.voshodnerd.quarkus.repository;

import com.voshodnerd.quarkus.model.PromoCode;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PromoCodeRepository extends CrudRepository<PromoCode, Long> {

    @Transactional
    Optional<PromoCode> findFirstByIssuedBy(String phone);

    List<PromoCode> findAllByValueIn(List<String> list);

    @Transactional
    Optional<PromoCode> findFirstByStatus(PromoCode.PromoCodeStatus status);


}
