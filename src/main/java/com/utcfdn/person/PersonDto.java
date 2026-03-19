package com.utcfdn.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private Long id;
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private LocalDate dateOfBirth;
    private String photoFileName;
    private String biography;

    @Builder.Default
    private List<CongregationRelationDto> congregations = new ArrayList<>();

    @Builder.Default
    private List<PersonContactInfoDto> contactInfos = new ArrayList<>();
}
