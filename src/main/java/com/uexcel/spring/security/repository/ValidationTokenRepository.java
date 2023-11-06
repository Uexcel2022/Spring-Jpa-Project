package com.uexcel.spring.security.repository;

import com.uexcel.spring.security.entity.ValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValidationTokenRepository extends JpaRepository<ValidationToken, Long> {
    List<ValidationToken> findByResetToken(String token);

    @Query(
            value = "SELECT Max(reset_token_id) FROM reset_password WHERE email =:email",
            nativeQuery = true
    )

    Long findUserLastTokenId(@Param("email") String email);



}
