package com.simanja.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO untuk request isi celengan (menambah currentAmount pada target).
 */
public class IsiCelenganRequest {

    @NotNull(message = "Jumlah tidak boleh kosong")
    @Positive(message = "Jumlah isi celengan harus lebih dari 0")
    private Double amount;

    public IsiCelenganRequest() {}

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
