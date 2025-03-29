package com.voshodnerd.quarkus.service;

import com.voshodnerd.quarkus.exception.NoCampaignException;
import com.voshodnerd.quarkus.model.Campaign;
import com.voshodnerd.quarkus.model.PromoCode;
import com.voshodnerd.quarkus.model.dto.CampaignRecord;
import com.voshodnerd.quarkus.repository.CampaignRepository;
import com.voshodnerd.quarkus.repository.PromoCodeRepository;
import com.voshodnerd.quarkus.utils.CsvReader;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.ListUtils;
import org.codejargon.fluentjdbc.api.FluentJdbc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class PromoCodeService {

    @Inject
    private final FluentJdbc fluentJdbc;

    private static final Logger LOGGER = Logger.getLogger(PromoCodeService.class.getName());

    private final CampaignRepository campaignRepository;
    private final PromoCodeRepository promoCodeRepository;

    @Inject
    public PromoCodeService(CampaignRepository campaignRepository,
                            PromoCodeRepository promoCodeRepository,
                            FluentJdbc fluentJdbc) {
        this.fluentJdbc = fluentJdbc;
        this.campaignRepository = campaignRepository;
        this.promoCodeRepository = promoCodeRepository;
    }

    @Transactional
    public Optional<PromoCode> issuePromoCode(String phoneNumber) {
        var existingPromoCode = promoCodeRepository.findFirstByIssuedBy(phoneNumber);
        if (existingPromoCode.isEmpty()) {
            var promoCode = promoCodeRepository.findFirstByStatus(PromoCode.PromoCodeStatus.ACTIVE);
            if (promoCode.isEmpty()) return Optional.empty();
            var promoCodeEntity = promoCode.get();
            promoCodeEntity.setStatus(PromoCode.PromoCodeStatus.USED);
            promoCodeEntity.setIssuedBy(phoneNumber);
            promoCodeRepository.save(promoCodeEntity);
            return promoCode;
        }
        return existingPromoCode;
    }

    @Transactional
    public List<CampaignRecord> getCampaigns() {
        return StreamSupport.stream(campaignRepository.findAll().spliterator(), false)
                .map(el -> new CampaignRecord(el.getId(),
                        el.getName(),
                        el.getDescription(),
                        el.getStartDate(),
                        el.getEndDate()))
                .toList();
    }

    @Transactional
    public Campaign createCampaign(CampaignRecord campaign) {
        Campaign campaignEntity = new Campaign();
        campaignEntity.setName(campaign.name());
        campaignEntity.setDescription(campaign.description());
        campaignEntity.setStartDate(campaign.startDate());
        campaignEntity.setEndDate(campaign.endDate());
        return campaignRepository.save(campaignEntity);
    }

    @TransactionConfiguration(timeout = 300 )
    @Transactional
    public boolean uploadPromoCodeFile(InputStream stream, Long campaignId) throws NoCampaignException {
        // Parse the CSV file
        var uploadList = new ArrayList<String>();
        try {
            uploadList = (ArrayList<String>) CsvReader.parseCsv(stream);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to parse CSV file");
        }
        if (uploadList.isEmpty()) return false;

        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(NoCampaignException::new);
        var batchSize = 1000;
        var currentlyUploaded = 0;
        var listOfList = ListUtils.partition(uploadList, batchSize);
        for (var list : listOfList) {
            var promoCodeListInDB = promoCodeRepository.
                    findAllByValueIn(list);
            currentlyUploaded= currentlyUploaded + list.size();
            LOGGER.log(Level.INFO, ": Promo code list in DB" + promoCodeListInDB.size());
            LOGGER.log(Level.INFO, ": Promo code list in upload" + list.size());
            LOGGER.log(Level.INFO, ": Уже загружено " + currentlyUploaded);
            if (promoCodeListInDB.isEmpty()) {
                insertNewPromoCode(list, campaignId);
                continue;
            }
            var db = promoCodeListInDB.stream()
                    .map(PromoCode::getValue).toList();
            var uniquePromoCodeList = list.stream()
                    .filter(el -> !db.contains(el))
                    .toList();
            insertNewPromoCode(uniquePromoCodeList, campaignId);

            promoCodeListInDB.forEach(el -> {
                el.setIssuedBy(null);
                el.setStatus(PromoCode.PromoCodeStatus.ACTIVE);
            });
            promoCodeRepository.saveAll(promoCodeListInDB);

        }
        return true;
    }


    private void insertNewPromoCode(List<String> list, Long campaign) {
        String sql = "INSERT INTO promo_code (value,campaign_id,status) VALUES (?, ?, ?)";
        Stream<List<?>> params = list.stream()
                .map( el -> List.of(el,campaign,PromoCode.PromoCodeStatus.ACTIVE));
        fluentJdbc.query().batch(sql)
                .params(params)
                .run();
    }
}
