package com.voshodnerd.promocode.issue.model;

import jakarta.persistence.*;

@Entity
class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var value: String = ""

    @Enumerated(EnumType.STRING)
    var status: PromoCodeStatus? = null

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    var campaign: Campaign? = null

    constructor(value: String, status: PromoCodeStatus?, campaign: Campaign?) {
        this.value = value
        this.status = status
        this.campaign = campaign
    }

    enum class PromoCodeStatus {
        ACTIVE,
        USED,
        EXPIRED
    }
}
