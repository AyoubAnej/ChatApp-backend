package org.example.chatapp2.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "ourUser")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    private String full_name;
    private String email;
    private String profile_picture;
    private String password;
//    @OneToMany(mappedBy = "ouruser", cascade = CascadeType.ALL)
//    private List<Notification> notifications = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(Id, user.Id) && Objects.equals(full_name, user.full_name) && Objects.equals(email, user.email) && Objects.equals(profile_picture, user.profile_picture) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, full_name, email, profile_picture, password);
    }
}
