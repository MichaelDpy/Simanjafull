package com.simanja.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * BaseEntity — Abstract class demonstrasi Inheritance (PBO) dan Abstraction (PBO).
 * Semua entitas yang memiliki kebutuhan timestamp mewarisi kelas ini.
 * Subclass wajib mengimplementasikan getDisplayName().
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    protected BaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // =============================================
    // Abstraction — setiap subclass wajib implementasi
    // =============================================

    /**
     * Mengembalikan nama tampilan dari entitas.
     * Wajib diimplementasikan oleh setiap subclass (Abstraction).
     */
    public abstract String getDisplayName();

    // =============================================
    // Getter & Setter (Encapsulation)
    // =============================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
