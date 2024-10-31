package org.xmdf.homelabinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.xmdf.homelabinventory.domain.Role;
import org.xmdf.homelabinventory.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select r from hi_role r where r.name = ?1")
    Optional<Role> findRoleByName(String name);
}
