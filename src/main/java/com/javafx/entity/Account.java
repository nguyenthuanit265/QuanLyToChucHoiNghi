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
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Vui lòng nhập email!")
    @Email(message = "Email không đúng định dạng!")
    private String email;

    @NotBlank(message = "Vui lòng nhập họ tên!")
    private String username;

    private String password;


    @Column(name = "role_id")
    @NotBlank(message = "Vui lòng chọn loại người dùng!")
    private int roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id",
            insertable = false, updatable = false)
    private Role role;


    @Column(name = "conference_id")
    private int conferenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id",
            insertable = false, updatable = false)
    private Conference conference;

    @Column(name = "is_delete")
    private boolean isDelete = false;

    @Column(name = "is_block")
    private boolean isBlock = false;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roleId='" + roleId + '\'' +
                ", role=" + role +
                ", conferenceId=" + conferenceId +
                ", conference=" + conference +
                ", isDelete=" + isDelete +
                '}';
    }

    public Account(String email, String username, String password, int roleId, int conferenceId) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.conferenceId = conferenceId;
    }

    public Account() {
    }
}
