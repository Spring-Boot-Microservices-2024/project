package org.naukma.spring.modulith.review;

import jakarta.persistence.*;
import lombok.*;
import org.naukma.spring.modulith.user.UserEntity;

@Entity
@Data
@Table(name = "reviews")
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private UserEntity author;
    private int rating;
    @Column(length = 300)
    private String comment;
}
