package com.uexcel.spring.security.repository;

import com.uexcel.spring.security.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {
    List<ResetPassword> findByResetPasswordToken(String token);

}
