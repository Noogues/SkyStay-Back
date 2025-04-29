package com.example.skystayback.dtos.user;

import com.example.skystayback.enums.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderApartmentVO {

    private String apartment_name;
    private String code;
    private Float amount;
    private Float discount;
    private StatusOrder status;
    private byte[] bill;

}
