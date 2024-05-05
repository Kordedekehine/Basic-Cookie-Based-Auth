package com.security.CookieBasedAuth.repository;

import com.security.CookieBasedAuth.entity.User;
import com.security.CookieBasedAuth.helpers.RefreshableCRUDRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends RefreshableCRUDRepository<User, Long> {

   User findByUsername(String username);

//   @Query("SELECT u FROM AppUser u WHERE u.username = :emailOrUsername OR u.email = :emailOrUsername")
@Query("SELECT u FROM User u WHERE u.username = :input OR u.email = :input")
User findByUsernameOrEmail(@Param("input") String input);

   User findFirstById(Long id);

}
