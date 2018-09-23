package com.ekarabacak.reactivesecurity.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {

    @Id @Generated
    private String id;

    private String username;
    private String password;
    private Set<Role> roles;
}
