package com.voshodnerd.promocode.issue.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
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

    public enum PromoCodeStatus {
        ACTIVE,
        USED,
        EXPIRED
    }
}
