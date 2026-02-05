package com.andreadisimone.dtos.tenant_dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TenantResponseDTO {
    private Integer idTenant;
    private String tenantName;
    private String surname;
    private String email;
    private String tenantAddress;
}
