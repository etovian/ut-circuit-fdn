package com.utcfdn.congregation;

import com.utcfdn.address.AddressEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class CongregationMapper {

    public CongregationDto toDto(CongregationEntity entity) {
        if (entity == null) {
            return null;
        }

        return CongregationDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .mission(entity.getMission())
                .bannerPhotoDirectory(entity.getBannerPhotoDirectory())
                .addresses(entity.getAddresses() != null ? 
                    entity.getAddresses().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }

    public CongregationAddressDto toDto(CongregationAddress address) {
        if (address == null) {
            return null;
        }

        return CongregationAddressDto.builder()
                .address(toDto(address.getAddress()))
                .addressType(address.getAddressType())
                .build();
    }

    public AddressDto toDto(AddressEntity entity) {
        if (entity == null) {
            return null;
        }

        return AddressDto.builder()
                .id(entity.getId())
                .streetAddress(entity.getStreetAddress())
                .addressLine2(entity.getAddressLine2())
                .city(entity.getCity())
                .state(entity.getState())
                .zipCode(entity.getZipCode())
                .build();
    }
}
