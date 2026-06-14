package com.simanja.backend.dto.response;

import java.util.Map;

/**
 * DTO response untuk data laporan per bulan (untuk chart/grafik).
 */
public class LaporanResponse {

    private Map<String, Double> pemasukanPerBulan;
    private Map<String, Double> pengeluaranPerBulan;
    private Map<String, Double> pengeluaranPerKategori;

    public LaporanResponse() {}

    public LaporanResponse(Map<String, Double> pemasukanPerBulan,
                           Map<String, Double> pengeluaranPerBulan,
                           Map<String, Double> pengeluaranPerKategori) {
        this.pemasukanPerBulan    = pemasukanPerBulan;
        this.pengeluaranPerBulan  = pengeluaranPerBulan;
        this.pengeluaranPerKategori = pengeluaranPerKategori;
    }

    public Map<String, Double> getPemasukanPerBulan() { return pemasukanPerBulan; }
    public void setPemasukanPerBulan(Map<String, Double> pemasukanPerBulan) {
        this.pemasukanPerBulan = pemasukanPerBulan;
    }

    public Map<String, Double> getPengeluaranPerBulan() { return pengeluaranPerBulan; }
    public void setPengeluaranPerBulan(Map<String, Double> pengeluaranPerBulan) {
        this.pengeluaranPerBulan = pengeluaranPerBulan;
    }

    public Map<String, Double> getPengeluaranPerKategori() { return pengeluaranPerKategori; }
    public void setPengeluaranPerKategori(Map<String, Double> pengeluaranPerKategori) {
        this.pengeluaranPerKategori = pengeluaranPerKategori;
    }
}
