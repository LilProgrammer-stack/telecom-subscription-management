package com.mflores.telecomapp.dto;

import com.mflores.telecomapp.model.BrandType;
import com.mflores.telecomapp.model.Device;
import com.mflores.telecomapp.model.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceAddedResponse {

    private Long deviceId;
    private String model;
    private BrandType brand;
    private String imei;
    private Money price;
    private Boolean locked;
    private Boolean financingEligible;

}
