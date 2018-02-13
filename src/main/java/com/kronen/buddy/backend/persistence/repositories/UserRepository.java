package com.kronen.buddy.backend.persistence.repositories;

import com.kronen.buddy.backend.persistence.domain.backend.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Returns an Optional User object given a username
     *
     * @param username The username
     * @return An Optional User object
     */
    Optional<User> findByUsername(String username);

    /**
     * Returns an Optional User object given an email
     *
     * @param email The user's email
     * @return An Optional User object
     */
    Optional<User> findByEmail(String email);

    /**
     * Updates an user password
     *
     * @param userId The user id
     * @param password The new password
     */
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :userId")
    void updateUserPassword(@Param("userId") Long userId, @Param("password") String password);

}


