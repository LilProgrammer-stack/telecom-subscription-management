package com.mflores.telecomapp.dto;
import com.mflores.telecomapp.model.PhoneLineStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneLineResponse {

    private Long phoneLineId;
    private String phoneNumber;
    private PhoneLineStatus phoneLineStatus;
}
