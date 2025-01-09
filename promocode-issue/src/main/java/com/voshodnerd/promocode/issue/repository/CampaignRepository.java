package com.voshodnerd.promocode.issue.repository;

import com.voshodnerd.promocode.issue.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}
