package com.simanja.model;

import java.time.LocalDateTime;

/**
 * BaseEntity — demonstrasi Inheritance
 * Semua entitas mewarisi id dan timestamp dari kelas ini
 */
public abstract class BaseEntity {

    protected int id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Abstraction — setiap subclass wajib implementasi
    public abstract String getDisplayName();
}
