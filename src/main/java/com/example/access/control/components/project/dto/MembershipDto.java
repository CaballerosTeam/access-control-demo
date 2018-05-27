package com.example.access.control.components.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipDto {

    @NotNull
    private Long projectId;

    @NotNull
    private Long personId;
}
