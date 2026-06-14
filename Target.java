package com.simanja.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entity Target (celengan/tabungan) — demonstrasi Inheritance (PBO)
 *
 * Inheritance  : extends BaseEntity (mewarisi id, createdAt, updatedAt)
 * Encapsulation: semua field private
 * Abstraction  : mengimplementasikan getDisplayName() dari BaseEntity
 * Polymorphism : override getDisplayName()
 */
@Entity
@Table(name = "target")
public class Target extends BaseEntity {

    // =============================================
    // Fields (Encapsulation — semua private)
    // =============================================

    @Column(nullable = false)
    private String nama;

    @Column(name = "icon_emoji")
    private String iconEmoji;

    @Column(name = "target_amount", nullable = false)
    private Double targetAmount;

    @Column(name = "current_amount", nullable = false)
    private Double currentAmount = 0.0;

    private LocalDate deadline;

    @Column(name = "completed_at")
    private LocalDate completedAt;

    private boolean achieved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // =============================================
    // Konstruktor
    // =============================================

    public Target() {
        super();
    }

    public Target(String nama, String iconEmoji, Double targetAmount,
                  Double currentAmount, LocalDate deadline, boolean achieved, User user) {
        super();
        this.nama          = nama;
        this.iconEmoji     = iconEmoji;
        this.targetAmount  = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline      = deadline;
        this.achieved      = achieved;
        this.user          = user;
        if (achieved) {
            this.completedAt = deadline;
        }
    }

    // =============================================
    // Implementasi abstract method (Abstraction + Polymorphism)
    // =============================================

    @Override
    public String getDisplayName() {
        return nama;
    }

    // =============================================
    // Business method
    // =============================================

    /**
     * Menghitung persentase progress celengan.
     * Demonstrasi method bisnis dalam entity.
     */
    public double getProgressPercent() {
        if (targetAmount == null || targetAmount <= 0) return 0;
        return Math.min(100.0, (currentAmount / targetAmount) * 100.0);
    }

    // =============================================
    // Getter & Setter (Encapsulation)
    // =============================================

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getIconEmoji() { return iconEmoji; }
    public void setIconEmoji(String iconEmoji) { this.iconEmoji = iconEmoji; }

    public Double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(Double targetAmount) { this.targetAmount = targetAmount; }

    public Double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(Double currentAmount) { this.currentAmount = currentAmount; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public LocalDate getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDate completedAt) { this.completedAt = completedAt; }

    public boolean isAchieved() { return achieved; }
    public void setAchieved(boolean achieved) { this.achieved = achieved; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
