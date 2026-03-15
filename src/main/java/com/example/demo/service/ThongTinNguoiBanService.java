package com.example.demo.service;

import com.example.demo.dto.api.SellerRegisterRequest;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.TaiKhoanNganHang;
import com.example.demo.entity.ThongTinNguoiBan;
import com.example.demo.enums.TTNB_TrangThai;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.TaiKhoanNganHangRepository;
import com.example.demo.repository.ThongTinNguoiBanRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ThongTinNguoiBanService {

    @Autowired
    private ThongTinNguoiBanRepository repository;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private TaiKhoanNganHangRepository taiKhoanNganHangRepository;

    // CREATE
    public ThongTinNguoiBan create(ThongTinNguoiBan data) {
        return repository.save(data);
    }

    // READ ALL
    public List<ThongTinNguoiBan> getAll() {
        return repository.findAll();
    }

    // READ BY ID
    public ThongTinNguoiBan getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    // UPDATE
    public ThongTinNguoiBan update(UUID id, ThongTinNguoiBan newData) {

        ThongTinNguoiBan old = repository.findById(id).orElse(null);

        if (old != null) {

            old.setNguoiDung(newData.getNguoiDung());
            old.setTienNhanCong(newData.getTienNhanCong());
            old.setTienThuongHieu(newData.getTienThuongHieu());
            old.setTrangThai(newData.getTrangThai());
            old.setAV_banner(newData.getAV_banner());
            old.setAV_hinhNen(newData.getAV_hinhNen());

            return repository.save(old);
        }

        return null;
    }

    // DELETE
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    // tu nguoimua check xem da co tai khoan nguoi ban chua
    public boolean checkNguoiBanByNguoiDungId(UUID ndId)
    {
        Optional<ThongTinNguoiBan> ttnb = repository.findByNguoiDungId(ndId);
        if (ttnb.isPresent()) {
            return true;
        }
        return false;
    }

    @Transactional
    public void registerSeller(SellerRegisterRequest req) {

        // tìm user
        NguoiDung nguoiDung = nguoiDungRepository.findById(req.getNguoiDungId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // tạo ThongTinNguoiBan
        ThongTinNguoiBan ttnb = new ThongTinNguoiBan();
        ttnb.setNguoiDung(nguoiDung);
        ttnb.setTienNhanCong(req.getTienNhanCong());
        ttnb.setTienThuongHieu(req.getTienThuongHieu());
        ttnb.setMaSoThue(req.getMaSoThue());
        ttnb.setTrangThai(TTNB_TrangThai.PENDING);

        ThongTinNguoiBan savedTTNB = repository.save(ttnb);

        // tạo tài khoản ngân hàng
        TaiKhoanNganHang bank = new TaiKhoanNganHang();
        bank.setTtnbId(savedTTNB);
        bank.setMaNganHang(req.getNganHang().getMaNganHang());
        bank.setTenNganHang(req.getNganHang().getTenNganHang());
        bank.setSoTaiKhoan(req.getNganHang().getSoTaiKhoan());
        bank.setTenTaiKhoan(req.getNganHang().getTenTaiKhoan());
        bank.setNgayTao(LocalDateTime.now());

        taiKhoanNganHangRepository.save(bank);
    }

    public UUID getSellerIdByUserId(UUID userId) {

        Optional<ThongTinNguoiBan> seller = repository.findByNguoiDungId(userId);

        if (seller.isPresent()) {
            return seller.get().getId();
        }

        return null;
    }

}