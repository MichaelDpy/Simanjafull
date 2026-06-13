package com.simanja.model;

import java.time.LocalDate;

/**
 * Model Target tabungan / celengan — demonstrasi Inheritance
 */
public class Target extends BaseEntity {

    private String nama;
    private String iconEmoji;
    private double targetAmount;
    private double currentAmount;
    private LocalDate deadline;
    private LocalDate completedAt;
    private boolean achieved;
    private int userId;

    public Target() {}

    public Target(int id, String nama, String iconEmoji, double targetAmount,
                  double currentAmount, LocalDate deadline, boolean achieved, int userId) {
        this.id = id;
        this.nama = nama;
        this.iconEmoji = iconEmoji;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
        this.achieved = achieved;
        this.userId = userId;
        if (achieved) {
            this.completedAt = deadline;
        }
    }

    public double getProgressPercent() {
        if (targetAmount <= 0) return 0;
        return Math.min(100, (currentAmount / targetAmount) * 100);
    }

    @Override
    public String getDisplayName() {
        return nama;
    }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getIconEmoji() { return iconEmoji; }
    public void setIconEmoji(String iconEmoji) { this.iconEmoji = iconEmoji; }

    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }

    public double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(double currentAmount) { this.currentAmount = currentAmount; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public LocalDate getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDate completedAt) { this.completedAt = completedAt; }

    public boolean isAchieved() { return achieved; }
    public void setAchieved(boolean achieved) { this.achieved = achieved; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
