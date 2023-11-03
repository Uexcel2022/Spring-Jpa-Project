package com.uexcel.spring.security.repository;

import com.uexcel.spring.security.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
   @Query("SELECT s FROM UserToken s WHERE s.token=:token")
    UserToken findByToken(@Param("token")String token);
}
