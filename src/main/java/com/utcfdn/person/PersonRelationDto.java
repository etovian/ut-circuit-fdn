package com.utcfdn.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonRelationDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String position;
    private String photoUrl;
}
