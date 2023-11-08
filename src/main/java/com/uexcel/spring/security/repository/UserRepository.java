package com.uexcel.spring.security.repository;

import com.uexcel.spring.security.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
//    @Query("SELECT s.email FROM User s WHERE s.email = email")
    Optional<User> findUserByEmail(String email);

    @Modifying
    @Transactional
    @Query(
            "UPDATE User s Set s.password =:newPassword " +
                    "WHERE s.userId=:userId"
//            nativeQuery = true
    )
    int updateUserByEmail(
            @Param("userId") Long userid,
            @Param("newPassword") String password
    );

    @Query("SELECT s FROM User s WHERE s.email = email AND " +
            "s.password = password")
    User findByEmailAndPassword(
            String email, String password
    );
}
