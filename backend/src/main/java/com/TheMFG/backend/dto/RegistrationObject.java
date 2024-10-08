package com.TheMFG.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegistrationObject {
    private String firstName;
    private String lastName;
    private String email;
    private Date dt;
}
