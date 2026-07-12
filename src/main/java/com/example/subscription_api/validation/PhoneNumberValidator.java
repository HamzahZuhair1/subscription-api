package com.example.subscription_api.validation;

import com.example.subscription_api.exception.InvalidPhoneNumberException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberValidator {

    public void validate(String mobileNumber) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(mobileNumber, null);

            if (!phoneUtil.isValidNumber(number)) {
                throw new InvalidPhoneNumberException("Invalid mobile number: Does not belong to any country or has incorrect length.");
            }

        } catch (NumberParseException e) {
            throw new InvalidPhoneNumberException("Invalid mobile number format.");
        }
    }
}