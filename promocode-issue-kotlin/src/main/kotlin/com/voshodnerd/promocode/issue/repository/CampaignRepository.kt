package com.voshodnerd.promocode.issue.repository;

import com.voshodnerd.promocode.issue.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository;

@Repository
interface CampaignRepository : JpaRepository<Campaign, Long>
