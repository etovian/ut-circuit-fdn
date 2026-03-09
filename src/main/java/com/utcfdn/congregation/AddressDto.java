package com.utcfdn.congregation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    private Long id;
    private String streetAddress;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
}
