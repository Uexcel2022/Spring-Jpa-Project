package com.uexcel.spring.security.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
@Entity
@Data
@NoArgsConstructor
public class UserToken {
    public final int TOKEN_EXPIRATION_TIME = 10;
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

    @OneToOne(cascade = CascadeType.ALL)
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
        this.expirationTime = getExpirationTime();
    }

    public  UserToken(String token){
        super();
        this.token = token;
        this.expirationTime = getExpirationTime();
    }

    private Date getExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, TOKEN_EXPIRATION_TIME);

        return  new Date(calendar.getTime().getTime());
    }
}
