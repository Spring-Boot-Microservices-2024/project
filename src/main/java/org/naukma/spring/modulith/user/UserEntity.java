package org.naukma.spring.modulith.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotNull
    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @NotNull
    @Size(min = 6)
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false, length = 50)
    private String firstname;

    @NotNull
    @Column(nullable = false, length = 50)
    private String lastname;
}
