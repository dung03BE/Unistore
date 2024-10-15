package com.dung.UniStore.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;


import java.util.Date;
import java.util.Set;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "fullname", length = 100)
    String fullName;
    @Column(name = "phone_number", length = 50,unique = true,columnDefinition = "VARCHAR(255 COLLATE utf8mb4_unicode_ci")
    String phoneNumber;
    @Column(name = "address", length = 100)
    String address;
    @Column(name = "`password`", length = 100, nullable = false)
    String password;
    @Column(name = "is_active")
    boolean active;
    @Column(name = "date_of_birth")
    Date dateOfBirth;
    @Column(name = "facebook_account_id")
    int facebookAccountId;
    @Column(name = "google_account_id")
    int googleAccountId;
    @ManyToOne
    @JoinColumn(name = "`role_id`")
    Role role;
//    Set<String> roles;
}
