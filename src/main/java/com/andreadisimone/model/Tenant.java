package com.andreadisimone.model;
import com.andreadisimone.model.abstract_class.User;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

public class Tenant extends User{
    private Integer idTenant;
}
