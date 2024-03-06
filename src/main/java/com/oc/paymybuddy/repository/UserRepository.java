package com.oc.paymybuddy.repository;

import com.oc.paymybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query("SELECT u.email FROM User u WHERE u.userId IN :userIds")
    List<String> findEmailsByIds(@Param("userIds") List<Long> userIds);

    @Query("SELECT u.userId FROM User u WHERE u.email = :email")
    Long findIdByEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean isAlreadyUsedEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE users AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
