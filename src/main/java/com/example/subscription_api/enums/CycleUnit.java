package com.example.subscription_api.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CycleUnit {

    DAY(1),
    WEEK(7),
    MONTH(30),
    YEAR(365);

    private final int daysValue;
}