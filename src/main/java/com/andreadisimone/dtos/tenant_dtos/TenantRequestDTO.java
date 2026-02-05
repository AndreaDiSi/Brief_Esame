package com.andreadisimone.dtos.tenant_dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TenantRequestDTO {

    @JsonProperty("tenantName")
    private String tenantName;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("tenantAddress")
    private String tenantAddress;

}
