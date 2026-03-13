package com.utcfdn.congregation;

import com.utcfdn.address.AddressDto;
import com.utcfdn.address.AddressEntity;
import com.utcfdn.person.PersonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CongregationMapper {

    private final ResourcePatternResolver resourcePatternResolver;
    private final PersonMapper personMapper;
    private final CongregationExternalLinkMapper congregationExternalLinkMapper;

    public CongregationDto toDto(CongregationEntity entity) {
        if (entity == null) {
            return null;
        }

        List<String> bannerPhotos = Collections.emptyList();
        if (entity.getBannerPhotoDirectory() != null && !entity.getBannerPhotoDirectory().isEmpty()) {
            try {
                String locationPattern = "classpath:congregation/banner-photos/" + entity.getBannerPhotoDirectory() + "/*";
                Resource[] resources = resourcePatternResolver.getResources(locationPattern);
                bannerPhotos = Arrays.stream(resources)
                        .filter(Resource::isReadable)
                        .filter(resource -> {
                            String filename = resource.getFilename();
                            return filename != null && (
                                    filename.toLowerCase().endsWith(".webp") ||
                                    filename.toLowerCase().endsWith(".jpg") ||
                                    filename.toLowerCase().endsWith(".jpeg") ||
                                    filename.toLowerCase().endsWith(".png") ||
                                    filename.toLowerCase().endsWith(".jfif")
                            );
                        })
                        .map(resource -> "/api/banner-photos/" + entity.getBannerPhotoDirectory() + "/" + resource.getFilename())
                        .sorted()
                        .collect(Collectors.toList());
            } catch (IOException e) {
                // Ignore or log
            }
        }

        return CongregationDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .mission(entity.getMission())
                .bannerPhotoDirectory(entity.getBannerPhotoDirectory())
                .bannerPhotos(bannerPhotos)
                .addresses(entity.getAddresses() != null ? 
                    entity.getAddresses().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList()) : Collections.emptyList())
                .persons(entity.getPersons() != null ?
                    entity.getPersons().stream()
                        .map(personMapper::toPersonRelationDto)
                        .collect(Collectors.toList()) : Collections.emptyList())
                .externalLinks(entity.getExternalLinks() != null ?
                    entity.getExternalLinks().stream()
                        .map(congregationExternalLinkMapper::toDto)
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
