package org.xmdf.homelabinventory.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    @Query("select r from hi_role r where r.name = ?1")
    Optional<Role> findRoleByName(String name);
}
