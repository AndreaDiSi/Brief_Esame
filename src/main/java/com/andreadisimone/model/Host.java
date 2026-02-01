package com.andreadisimone.model;
import com.andreadisimone.model.abstract_class.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Host extends User{
    private Integer idHost;
    private boolean isSuperHost;

}

