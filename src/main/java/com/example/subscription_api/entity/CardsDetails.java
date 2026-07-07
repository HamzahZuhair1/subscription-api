package com.example.subscription_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cards_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardsDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "card_holder_name", nullable = false)
    private String cardHolderName;

    @Column(name = "card_brand", nullable = false)
    private String cardBrand;

    @Column(name = "last_four_digits", nullable = false, length = 4)
    private String lastFourDigits;

    @Column(name = "payment_token", nullable = false, unique = true)
    private String paymentToken;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;
}