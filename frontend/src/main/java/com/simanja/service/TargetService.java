package com.simanja.service;

import com.simanja.model.Target;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TargetService — logika tabungan / celengan (dummy data, backend friend can replace)
 */
public class TargetService {

    private static final List<Target> dummyTargets = new ArrayList<>();
    private static int nextId = 5;

    static {
        dummyTargets.add(new Target(1, "Dana Liburan Jepang", "🏖️",
            20_000_000, 15_000_000, LocalDate.of(2024, 12, 1), false, 2));
        dummyTargets.add(new Target(2, "Dana Darurat", "🛡️",
            50_000_000, 50_000_000, LocalDate.of(2024, 2, 1), true, 2));
        dummyTargets.add(new Target(3, "Macbook Pro M3", "💻",
            30_000_000, 12_000_000, LocalDate.of(2025, 3, 1), false, 2));
        dummyTargets.add(new Target(4, "DP Rumah", "🏠",
            150_000_000, 45_000_000, LocalDate.of(2027, 6, 1), false, 2));
    }

    public List<Target> getAllByUser(int userId) {
        return dummyTargets.stream()
            .filter(t -> t.getUserId() == userId)
            .sorted(Comparator.comparing(Target::isAchieved).thenComparing(Target::getDeadline))
            .collect(Collectors.toList());
    }

    public double getTotalTabungan(int userId) {
        return dummyTargets.stream()
            .filter(t -> t.getUserId() == userId)
            .mapToDouble(Target::getCurrentAmount)
            .sum();
    }

    public Target getById(int id) {
        return dummyTargets.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public void isiCelengan(int targetId, double amount) {
        Target target = getById(targetId);
        if (target == null || amount <= 0) return;

        target.setCurrentAmount(target.getCurrentAmount() + amount);
        if (target.getCurrentAmount() >= target.getTargetAmount()) {
            target.setAchieved(true);
            target.setCompletedAt(LocalDate.now());
            target.setCurrentAmount(target.getTargetAmount());
        }
        target.setUpdatedAt(java.time.LocalDateTime.now());
    }

    public Target buatTarget(String nama, String icon, double targetAmount, LocalDate deadline, int userId) {
        Target target = new Target();
        target.setId(nextId++);
        target.setNama(nama);
        target.setIconEmoji(icon);
        target.setTargetAmount(targetAmount);
        target.setCurrentAmount(0);
        target.setDeadline(deadline);
        target.setAchieved(false);
        target.setUserId(userId);
        dummyTargets.add(target);
        return target;
    }

    /** Estimasi tabungan bulanan untuk mencapai target */
    public double estimasiTabunganBulanan(double sisa, LocalDate deadline) {
        if (sisa <= 0 || deadline == null) return 0;
        long months = ChronoUnit.MONTHS.between(LocalDate.now(), deadline);
        if (months <= 0) months = 1;
        return sisa / months;
    }
}
