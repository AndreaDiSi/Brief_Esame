package com.andreadisimone.dtos.host_dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class HostResponseDTO {
    private Integer idHost;
    private String hostName;
    private String surname;
    private String email;
    private String hostAddress;
    private boolean isSuperhost;
    private double avgFeedback;
    private int nFeedback;
}