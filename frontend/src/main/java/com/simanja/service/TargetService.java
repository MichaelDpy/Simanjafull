package com.simanja.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.simanja.model.Target;
import com.simanja.util.ApiClient;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * TargetService — Terhubung ke Spring Boot backend via REST API.
 */
public class TargetService {

    record TargetRequest(String nama, String iconEmoji, double targetAmount, LocalDate deadline) {}
    
    record IsiCelenganRequest(double amount) {}

    public List<Target> getAllByUser(int userId) {
        return ApiClient.get("/target", new TypeReference<List<Target>>() {});
    }

    public double getTotalTabungan(int userId) {
        Map<String, Double> resp = ApiClient.get("/target/total-tabungan", new TypeReference<Map<String, Double>>() {});
        return resp.getOrDefault("totalTabungan", 0.0);
    }

    public Target getById(int id) {
        return ApiClient.get("/target/" + id, Target.class);
    }

    public void isiCelengan(int targetId, double amount) {
        if (amount <= 0) return;
        IsiCelenganRequest req = new IsiCelenganRequest(amount);
        ApiClient.post("/target/" + targetId + "/isi", req, Target.class);
    }

    public Target buatTarget(String nama, String icon, double targetAmount, LocalDate deadline, int userId) {
        TargetRequest req = new TargetRequest(nama, icon, targetAmount, deadline);
        return ApiClient.post("/target", req, Target.class);
    }

    /** Estimasi tabungan bulanan untuk mencapai target (pure frontend logic) */
    public double estimasiTabunganBulanan(double sisa, LocalDate deadline) {
        if (sisa <= 0 || deadline == null) return 0;
        long months = ChronoUnit.MONTHS.between(LocalDate.now(), deadline);
        if (months <= 0) months = 1;
        return sisa / months;
    }
}
