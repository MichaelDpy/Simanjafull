package com.simanja.backend.repository;

import com.simanja.backend.model.Target;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TargetRepository — Repository layer untuk akses data Target (celengan).
 */
@Repository
public interface TargetRepository extends JpaRepository<Target, Long> {

    /**
     * Ambil semua target milik user, urut: belum tercapai dulu, lalu berdasarkan deadline.
     */
    List<Target> findByUserIdOrderByAchievedAscDeadlineAsc(Long userId);

    /**
     * Hitung total currentAmount seluruh target milik user (total tabungan).
     */
    @Query("SELECT COALESCE(SUM(t.currentAmount), 0) FROM Target t WHERE t.user.id = :userId")
    Double sumCurrentAmountByUserId(@Param("userId") Long userId);
}
