package com.example.reversiblecomputation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //id of user, unique identifier used as pk

    @Column(nullable=false)
    private String name;
    //name of user (first+last name) - required

    @Column(nullable=false, unique=true)
    private String email;
    //email id of user - required field

    @Column(nullable=false)
    private String password;
    //password for user - encoded when stored - required

    @Column
    private String description;
    @Column
    private Integer age;
    @Column
    private String location;
    @Column
    @ColumnDefault("false")
    private boolean img;
    //non mandatory fields ^^ - self explanatory
    //img represents profile picture, by default false means no profile picture set when user registers

    @Column
    private Integer views = 0;
    //how many views a user's profile has

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="users_roles",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")})
    private List<Role> roles = new ArrayList<>();

}
