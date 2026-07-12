package com.example.subscription_api.entity;

import com.example.subscription_api.enums.CycleUnit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plan_prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "cycle_length", nullable = false)
    private int cycleLength;

    @Enumerated(EnumType.STRING)
    @Column(name = "cycle_unit", nullable = false)
    private CycleUnit cycleUnit;

    @Column(name = "amount", nullable = false)
    private long amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}