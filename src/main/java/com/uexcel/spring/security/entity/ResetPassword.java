package com.uexcel.spring.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.rmi.server.UID;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class ResetPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restTokenId;
    private Date tokenExpiryDate;
    private String resetPasswordToken;
    private String email;

    public ResetPassword(String email){
        this.email = email;
        this.resetPasswordToken = UUID.randomUUID().toString();
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
