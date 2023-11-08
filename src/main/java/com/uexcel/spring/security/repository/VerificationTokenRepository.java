package com.uexcel.spring.security.repository;

import com.uexcel.spring.security.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
//   @Query("SELECT s FROM UserToken s WHERE s.token=:token")
   Optional<VerificationToken> findByToken(String token);

   @Query(
   value = "SELECT * FROM verification_token WHERE token_id ="  +
           "(SELECT MAX(token_id) FROM verification_token WHERE user_id =:userId)",
   nativeQuery = true
   )
   Optional<VerificationToken> getVerificationDetailsUserId(@Param("userId") Long userId);

   @Query(
          value = "SELECT Max(token_id) FROM verification_token WHERE user_id =:userId",
           nativeQuery = true
   )
   Long findUserLastTokenId(@Param("userId") long userId);
}
