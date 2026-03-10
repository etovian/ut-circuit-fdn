package com.utcfdn.congregation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CongregationDto {
    private Long id;
    private String name;
    private String description;
    private String mission;
    private String bannerPhotoDirectory;
    private List<String> bannerPhotos;
    private List<CongregationAddressDto> addresses;
}
