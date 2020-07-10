package com.javafx.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Table
@Entity(name = "accounts")
@Data
@Getter
@Setter
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Vui lòng nhập email!")
    @Email(message = "Email không đúng định dạng!")
    private String email;

    @NotBlank(message = "Vui lòng nhập họ tên!")
    private String fullname;

    private String password;

    private String avatar;
    private String phone;
    private String address;
    private String website;
    private String facebook;

    @Column(name = "role_id")
    @NotBlank(message = "Vui lòng chọn loại người dùng!")
    private String roleId;

    @ManyToOne()
    @JoinColumn(name = "role_id",
            insertable = false, updatable = false)
    private Role role;
}
