package com.voshodnerd.helidon.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "promo_code")
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    @Enumerated(EnumType.STRING)
    private PromoCodeStatus status;
    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    public String getValue() {
        return value;
    }

    public PromoCodeStatus getStatus() {
        return status;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setStatus(PromoCodeStatus status) {
        this.status = status;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public enum PromoCodeStatus {
        ACTIVE,
        USED,
        EXPIRED
    }
}