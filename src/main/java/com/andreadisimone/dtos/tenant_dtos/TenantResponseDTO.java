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
    private Integer nReservations;

    public TenantResponseDTO(Integer idTenant,String tenantName, String surname,String email,   String tenantAddress ) {
        this.email = email;
        this.idTenant = idTenant;
        this.surname = surname;
        this.tenantAddress = tenantAddress;
        this.tenantName = tenantName;
    }

}


