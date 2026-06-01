package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.entity.NhatKyKiemToan;
import com.example.demo.enums.NKKT_HanhDong;
import com.example.demo.enums.NKKT_KetQua;
import com.example.demo.enums.NKKT_LoaiMucTieu;
import com.example.demo.enums.NKKT_LoaiTacNhan;
import com.example.demo.repository.NhatKyKiemToanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final NhatKyKiemToanRepository repo;

    public void record(NKKT_HanhDong action,
                       NKKT_LoaiTacNhan actorType,
                       UUID actorId,
                       NKKT_LoaiMucTieu targetType,
                       Long targetId,
                       String newData,
                       String ip) {

        NhatKyKiemToan log = new NhatKyKiemToan();
        log.setLoaiTacNhan(actorType);
        log.setIdTacNhan(actorId);
        log.setHanhDong(action);
        log.setLoaiMucTieu(targetType);
        log.setIdMucTieu(targetId);
        log.setKetQua(NKKT_KetQua.THANH_CONG);
        // log.setDuLieuMoi("{ }");
        log.setDuLieuMoi(Map.of(
            "action", action.name(),
            "targetId", targetId,
            "newData", newData,
            "actorId", actorId,
            "actorType", actorType,
            "targetType", targetType,
            "timestamp", LocalDateTime.now().toString()
        ));
        log.setIp(ip);
        log.setNgayTao(LocalDateTime.now());

        repo.save(log);
    }
}