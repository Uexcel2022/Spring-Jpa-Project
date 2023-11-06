package com.uexcel.spring.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class ValidationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resetTokenId;
    private Date tokenExpiryDate;
    private String resetToken;
    private String email;

    public ValidationToken(String email){
        this.email = email;
        this.resetToken = UUID.randomUUID().toString();
        this.tokenExpiryDate = generateExpiryTime();
    }

    public Date generateExpiryTime(){
        //Expiry time is ten minutes
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,10);
        return new Date(calendar.getTime().getTime());
    }

}
