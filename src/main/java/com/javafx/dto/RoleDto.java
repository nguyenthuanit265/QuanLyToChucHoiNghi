package com.javafx.dto;

import lombok.Data;

@Data
public class RoleDto {
    private int id;
    private String name;
    private String description;

    @Override
    public String toString() {
        return name;
    }
}
