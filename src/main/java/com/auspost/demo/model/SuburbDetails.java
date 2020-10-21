package com.auspost.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuburbDetails {
    private String name;
    private int postCode;
    private String state;
    private String status;
}
