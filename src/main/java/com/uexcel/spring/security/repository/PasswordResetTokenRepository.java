package com.uexcel.spring.security.repository;

import com.uexcel.spring.security.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    List<PasswordResetToken> findByToken(String token);

    @Query(
            value = "SELECT * FROM password_reset_token WHERE token_id ="  +
                    "(SELECT MAX(token_id) FROM password_reset_token WHERE user_id =:userId)",
            nativeQuery = true
    )
    Optional<PasswordResetToken> getPasswordTokenDetailsByUserId(@Param("userId") Long userId);

    @Query(
            value = "SELECT Max(token_id) FROM password_reset_token WHERE user_id =:userId",
            nativeQuery = true
    )

    Long findUserLastTokenId(@Param("userId") Long userId);


}
