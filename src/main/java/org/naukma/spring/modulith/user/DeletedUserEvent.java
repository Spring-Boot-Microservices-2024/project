package org.naukma.spring.modulith.user;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class DeletedUserEvent {
    private Long userId;
}
