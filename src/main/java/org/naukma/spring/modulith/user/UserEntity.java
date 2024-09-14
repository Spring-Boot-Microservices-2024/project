package org.naukma.spring.modulith.user;

import jakarta.persistence.*;
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
    @Column(nullable = false, length = 30, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false,length = 100)
    private String email;
    @Column(nullable = false,length = 50)
    private String firstname;
    @Column(nullable = false,length = 50)
    private String lastname;
}
