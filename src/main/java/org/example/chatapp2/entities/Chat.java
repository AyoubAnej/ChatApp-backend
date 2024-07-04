package org.example.chatapp2.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Chat {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String chatName;
    private String chatImage;

    @Column(name = "is_group")
    private boolean isGroup;

    @Column(name = "created_by")
    @ManyToOne
    private User createdBy;

    @ManyToMany
    private Set<User> users = new HashSet<>();

    @OneToMany
    private List<Message> messages = new ArrayList<>();

}
