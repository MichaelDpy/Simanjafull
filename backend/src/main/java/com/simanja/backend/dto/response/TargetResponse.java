package com.simanja.backend.dto.response;

import com.simanja.backend.model.Target;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * DTO response untuk data Target (celengan).
 */
public class TargetResponse {

    private Long id;
    private String nama;
    private String displayName;     // Dari getDisplayName() — Polymorphism
    private String iconEmoji;
    private Double targetAmount;
    private Double currentAmount;
    private Double progressPercent;  // Dari getProgressPercent() — method bisnis
    private LocalDate deadline;
    private LocalDate completedAt;
    private boolean achieved;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double estimasiTabunganBulanan;

    public TargetResponse() {}

    /**
     * Factory method konversi dari entity Target ke TargetResponse.
     * Menggunakan Polymorphism melalui override getDisplayName() dan getProgressPercent().
     */
    public static TargetResponse from(Target t) {
        TargetResponse dto = new TargetResponse();
        dto.id              = t.getId();
        dto.nama            = t.getNama();
        dto.displayName     = t.getDisplayName();      // Polymorphism — override dari BaseEntity
        dto.iconEmoji       = t.getIconEmoji();
        dto.targetAmount    = t.getTargetAmount();
        dto.currentAmount   = t.getCurrentAmount();
        dto.progressPercent = t.getProgressPercent();  // Method bisnis di entity
        dto.deadline        = t.getDeadline();
        dto.completedAt     = t.getCompletedAt();
        dto.achieved        = t.isAchieved();
        dto.userId          = t.getUser().getId();
        dto.createdAt       = t.getCreatedAt();
        dto.updatedAt       = t.getUpdatedAt();

        // Hitung estimasi tabungan bulanan
        double sisa = t.getTargetAmount() - t.getCurrentAmount();
        if (sisa > 0 && t.getDeadline() != null) {
            long months = ChronoUnit.MONTHS.between(LocalDate.now(), t.getDeadline());
            if (months <= 0) months = 1;
            dto.estimasiTabunganBulanan = sisa / months;
        } else {
            dto.estimasiTabunganBulanan = 0.0;
        }

        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getIconEmoji() { return iconEmoji; }
    public void setIconEmoji(String iconEmoji) { this.iconEmoji = iconEmoji; }

    public Double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(Double targetAmount) { this.targetAmount = targetAmount; }

    public Double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(Double currentAmount) { this.currentAmount = currentAmount; }

    public Double getProgressPercent() { return progressPercent; }
    public void setProgressPercent(Double progressPercent) { this.progressPercent = progressPercent; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public LocalDate getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDate completedAt) { this.completedAt = completedAt; }

    public boolean isAchieved() { return achieved; }
    public void setAchieved(boolean achieved) { this.achieved = achieved; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Double getEstimasiTabunganBulanan() { return estimasiTabunganBulanan; }
    public void setEstimasiTabunganBulanan(Double estimasiTabunganBulanan) {
        this.estimasiTabunganBulanan = estimasiTabunganBulanan;
    }
}
