package com.uexcel.spring.security.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Calendar;
import java.util.Date;
@Entity
@Data
@NoArgsConstructor()
public class UserToken {
    @Id
    @SequenceGenerator(
            name = "token_sequence",
            sequenceName = "token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "token_sequence"
    )
    private Long tokenId;
    @Column(nullable = false)
    private String token;
    private Date expirationTime;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            foreignKey =
            @ForeignKey(name = "user_fk_in_user_token_entity")
    )
    private User user;

    public  UserToken(User user, String token){
        super();
        this.user = user;
        this.token = token;
        this.expirationTime = expiryTime();
    }

    public  UserToken(String token){
        super();
        this.token = token;
        this.expirationTime = expiryTime();
    }

    private Date expiryTime() {
        int expiresInTenMinutes = 10;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expiresInTenMinutes);

        return  new Date(calendar.getTime().getTime());
    }


}
