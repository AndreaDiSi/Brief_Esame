package com.andreadisimone.model.abstract_class;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class User {
    private String name;
    private String surname;
    private String email;
    private String address;
}
