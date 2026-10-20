package com.mflores.telecomapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

//Find the settings that start with app.email and bind their values to the fields in this class.
@ConfigurationProperties(prefix = "app.email")
public class EmailProperties {

    private String verificationBaseUrl;
    private String fromAddress;

    public String getVerificationBaseUrl() {
        return verificationBaseUrl;
    }

    public void setVerificationBaseUrl(String verificationBaseUrl) {
        this.verificationBaseUrl = verificationBaseUrl;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
}
