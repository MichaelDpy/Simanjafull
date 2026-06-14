package com.simanja.backend.service;

import com.simanja.backend.dto.request.IsiCelenganRequest;
import com.simanja.backend.dto.request.TargetRequest;
import com.simanja.backend.dto.response.TargetResponse;
import com.simanja.backend.exception.ResourceNotFoundException;
import com.simanja.backend.exception.ValidationException;
import com.simanja.backend.model.Target;
import com.simanja.backend.model.User;
import com.simanja.backend.repository.TargetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TargetService — Service layer untuk logika bisnis target tabungan (celengan).
 *
 * Demonstrasi:
 * - Service Layer (MVC)
 * - Validasi data (input + bisnis)
 * - Polymorphism: penggunaan getDisplayName() via override (Target extends BaseEntity)
 */
@Service
@Transactional
public class TargetService {

    private final TargetRepository targetRepository;
    private final UserService userService;

    public TargetService(TargetRepository targetRepository, UserService userService) {
        this.targetRepository = targetRepository;
        this.userService      = userService;
    }

    // =============================================
    // READ
    // =============================================

    /**
     * Ambil semua target milik user.
     * Demonstrasi Polymorphism: stream + lambda + method reference.
     */
    @Transactional(readOnly = true)
    public List<TargetResponse> getAllByUser(String email) {
        User user = userService.getUserEntityByEmail(email);
        return targetRepository.findByUserIdOrderByAchievedAscDeadlineAsc(user.getId())
                .stream()
                .map(TargetResponse::from)  // Polymorphism — memanggil getDisplayName() & getProgressPercent()
                .collect(Collectors.toList());
    }

    /**
     * Ambil detail target berdasarkan ID.
     */
    @Transactional(readOnly = true)
    public TargetResponse getById(Long id, String email) {
        Target target = findAndValidateOwnership(id, email);
        return TargetResponse.from(target);
    }

    /**
     * Hitung total tabungan user.
     */
    @Transactional(readOnly = true)
    public Double getTotalTabungan(String email) {
        User user = userService.getUserEntityByEmail(email);
        Double total = targetRepository.sumCurrentAmountByUserId(user.getId());
        return total != null ? total : 0.0;
    }

    // =============================================
    // CREATE / UPDATE / DELETE / BUSINESS
    // =============================================

    /**
     * Buat target tabungan baru.
     */
    public TargetResponse buatTarget(String email, TargetRequest request) {
        // Validasi bisnis — deadline harus di masa depan
        if (request.getDeadline().isBefore(LocalDate.now())) {
            throw new ValidationException("Deadline target harus di masa depan");
        }

        User user = userService.getUserEntityByEmail(email);

        Target target = new Target(
                request.getNama(),
                request.getIconEmoji() != null ? request.getIconEmoji() : "🎯",
                request.getTargetAmount(),
                0.0,
                request.getDeadline(),
                false,
                user
        );

        Target saved = targetRepository.save(target);
        return TargetResponse.from(saved);
    }

    /**
     * Update data target.
     */
    public TargetResponse update(Long id, String email, TargetRequest request) {
        Target target = findAndValidateOwnership(id, email);

        // Validasi bisnis — target yang sudah tercapai tidak bisa diubah
        if (target.isAchieved()) {
            throw new ValidationException("Target yang sudah tercapai tidak bisa diubah");
        }
        if (request.getDeadline().isBefore(LocalDate.now())) {
            throw new ValidationException("Deadline target harus di masa depan");
        }

        target.setNama(request.getNama());
        if (request.getIconEmoji() != null) {
            target.setIconEmoji(request.getIconEmoji());
        }
        target.setTargetAmount(request.getTargetAmount());
        target.setDeadline(request.getDeadline());
        target.setUpdatedAt(LocalDateTime.now());

        Target saved = targetRepository.save(target);
        return TargetResponse.from(saved);
    }

    /**
     * Hapus target.
     */
    public void hapus(Long id, String email) {
        Target target = findAndValidateOwnership(id, email);
        targetRepository.delete(target);
    }

    /**
     * Isi celengan — menambahkan dana ke target.
     * Demonstrasi Validasi bisnis.
     */
    public TargetResponse isiCelengan(Long id, String email, IsiCelenganRequest request) {
        Target target = findAndValidateOwnership(id, email);

        // Validasi bisnis
        if (target.isAchieved()) {
            throw new ValidationException("Target ini sudah tercapai, tidak perlu diisi lagi");
        }

        double baru = target.getCurrentAmount() + request.getAmount();
        if (baru > target.getTargetAmount()) {
            baru = target.getTargetAmount(); // Cap di target amount
        }

        target.setCurrentAmount(baru);
        target.setUpdatedAt(LocalDateTime.now());

        // Cek apakah sudah mencapai target
        if (baru >= target.getTargetAmount()) {
            target.setAchieved(true);
            target.setCompletedAt(LocalDate.now());
        }

        Target saved = targetRepository.save(target);
        return TargetResponse.from(saved);
    }

    // =============================================
    // Private Helpers
    // =============================================

    private Target findAndValidateOwnership(Long id, String email) {
        Target target = targetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Target", id));

        // Validasi kepemilikan (security check)
        if (!target.getUser().getEmail().equals(email)) {
            throw new ValidationException("Anda tidak memiliki akses ke target ini");
        }
        return target;
    }
}
