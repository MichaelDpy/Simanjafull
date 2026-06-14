package com.simanja.backend.dto.response;

/**
 * DTO response untuk summary / ringkasan keuangan di dashboard.
 */
public class SummaryResponse {

    private Double totalPemasukan;
    private Double totalPengeluaran;
    private Double saldo;
    private Double totalTabungan;
    private Long jumlahTransaksi;
    private Long jumlahTarget;

    public SummaryResponse() {}

    public SummaryResponse(Double totalPemasukan, Double totalPengeluaran,
                           Double saldo, Double totalTabungan,
                           Long jumlahTransaksi, Long jumlahTarget) {
        this.totalPemasukan  = totalPemasukan;
        this.totalPengeluaran = totalPengeluaran;
        this.saldo            = saldo;
        this.totalTabungan    = totalTabungan;
        this.jumlahTransaksi  = jumlahTransaksi;
        this.jumlahTarget     = jumlahTarget;
    }

    public Double getTotalPemasukan() { return totalPemasukan; }
    public void setTotalPemasukan(Double totalPemasukan) { this.totalPemasukan = totalPemasukan; }

    public Double getTotalPengeluaran() { return totalPengeluaran; }
    public void setTotalPengeluaran(Double totalPengeluaran) { this.totalPengeluaran = totalPengeluaran; }

    public Double getSaldo() { return saldo; }
    public void setSaldo(Double saldo) { this.saldo = saldo; }

    public Double getTotalTabungan() { return totalTabungan; }
    public void setTotalTabungan(Double totalTabungan) { this.totalTabungan = totalTabungan; }

    public Long getJumlahTransaksi() { return jumlahTransaksi; }
    public void setJumlahTransaksi(Long jumlahTransaksi) { this.jumlahTransaksi = jumlahTransaksi; }

    public Long getJumlahTarget() { return jumlahTarget; }
    public void setJumlahTarget(Long jumlahTarget) { this.jumlahTarget = jumlahTarget; }
}
