package com.simanja.backend.repository;

import com.simanja.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository — Repository layer untuk akses data User.
 * Menggunakan Spring Data JPA (JpaRepository) untuk operasi CRUD otomatis.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Cari user berdasarkan email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Cek apakah email sudah terdaftar.
     */
    boolean existsByEmail(String email);
}
