package com.andreadisimone.dtos.host_dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class HostRequestDTO {

    @JsonProperty("hostName")
    private String hostName;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("hostAddress")
    private String hostAddress;

    @JsonProperty("isSuperhost")
    private boolean isSuperhost;
}