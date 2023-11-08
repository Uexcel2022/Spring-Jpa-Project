package com.uexcel.spring.security.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {
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
    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            optional = false
    )
    @JoinColumn(
            name = "user_id",
           foreignKey =
           @ForeignKey(
                   name = "User_FK_In_VerificationToken_Entity"
           )

    )
    private User user;

    public VerificationToken(User user){
        super();
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.expirationTime = expiryTime();
    }

    public VerificationToken(String token){
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
