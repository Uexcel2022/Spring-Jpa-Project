package com.uexcel.spring.security.repository;

import com.uexcel.spring.security.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
   @Query("SELECT s FROM UserToken s WHERE s.token=:token")
   Optional<UserToken> findByToken(@Param("token")String token);

   @Query(
          value = "SELECT Max(token_id) FROM user_token WHERE user_id =:userId",
           nativeQuery = true
   )
   Long findUserLastTokenId(@Param("userId") long userId);

}
