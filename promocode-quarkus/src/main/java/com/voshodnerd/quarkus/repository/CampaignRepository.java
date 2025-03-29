package com.voshodnerd.quarkus.repository;

import com.voshodnerd.quarkus.model.Campaign;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends CrudRepository<Campaign, Long> {

}

