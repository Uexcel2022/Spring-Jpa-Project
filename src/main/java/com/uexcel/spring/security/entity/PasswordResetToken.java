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
public class PasswordResetToken {
    @Id
    @SequenceGenerator(
            name = "passwordResetToken_sequence",
            sequenceName = "passwordResetToken_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "passwordResetToken_sequence"
    )
    private Long tokenId;
    private Date tokenExpiryDate;
    private String token;


    @OneToOne(
            cascade = CascadeType.ALL,
            optional = false,
            fetch = FetchType.EAGER
    )

    @JoinColumn(name = "user_id",
            foreignKey =
            @ForeignKey(
                    name = "User_FK_In_Password_Reset_Token_Entity"
            )
    )
    private User user;

    public PasswordResetToken(User user){
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.tokenExpiryDate = generateExpiryTime();
    }

    public PasswordResetToken(String token){
        this.token = token;
        this.tokenExpiryDate = generateExpiryTime();
    }

    private Date generateExpiryTime(){
        //Expiry time is ten minutes
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,10);
        return new Date(calendar.getTime().getTime());
    }

}
