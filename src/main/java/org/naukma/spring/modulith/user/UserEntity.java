package org.naukma.spring.modulith.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @Min(3)
    @Column(nullable = false, length = 30, unique = true)
    private String username;
    @NotNull
    @Min(6)
    @Column(nullable = false)
    private String password;

    @Email
    @NotNull
    @Column(nullable = false, length = 100)
    private String email;
    @NotNull
    @Column(nullable = false, length = 50)
    private String firstname;
    @NotNull
    @Column(nullable = false, length = 50)
    private String lastname;
}
