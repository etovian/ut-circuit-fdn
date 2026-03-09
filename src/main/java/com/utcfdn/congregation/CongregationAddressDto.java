package com.utcfdn.congregation;

import com.utcfdn.address.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CongregationAddressDto {
    private AddressDto address;
    private AddressType addressType;
}
