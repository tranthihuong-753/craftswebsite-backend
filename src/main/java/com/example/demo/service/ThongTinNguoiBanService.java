package com.example.demo.service;

import com.example.demo.dto.SellerRegisterRequest;
import com.example.demo.entity.DiaChi;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.TaiKhoanNganHang;
import com.example.demo.entity.ThongTinNguoiBan;
import com.example.demo.entity.VaiTro;
import com.example.demo.entity.VaiTroNguoiDung;
import com.example.demo.enums.TTNB_TrangThai;
import com.example.demo.enums.TrangThaiTaiKhoanNganHang;
import com.example.demo.enums.TrangThaiVaiTro;
import com.example.demo.repository.DiaChiRepository;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.TaiKhoanNganHangRepository;
import com.example.demo.repository.ThongTinNguoiBanRepository;
import com.example.demo.repository.VaiTroNguoiDungRepository;
import com.example.demo.repository.VaiTroRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    @Autowired
    private VaiTroRepository vaiTroRepository;
    @Autowired
    private VaiTroNguoiDungRepository vaiTroNguoiDungRepository;
    @Autowired
    private DiaChiRepository diaChiRepository;
    

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
    @Transactional
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
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    // tu nguoimua check xem da co tai khoan nguoi ban chua
    @Transactional
    public boolean checkNguoiBanByNguoiDungId(UUID ndId)
    {
        Optional<ThongTinNguoiBan> ttnb = repository.findByNguoiDungId(ndId);
        if (ttnb.isPresent()) {
            return true;
        }
        return false;
    }

    // TAO TAI KHOAN NGUOI BAN
    // @Transactional
    // public void registerSeller(UUID userId, SellerRegisterRequest req) {

    //     // tìm user
    //     NguoiDung nguoiDung = nguoiDungRepository.findById(userId)
    //             .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

    //     // tạo ThongTinNguoiBan
    //     ThongTinNguoiBan ttnb = new ThongTinNguoiBan();
    //     ttnb.setNguoiDung(nguoiDung);
    //     ttnb.setTienNhanCong(req.getTienNhanCong());
    //     ttnb.setTienThuongHieu(req.getTienThuongHieu());
    //     ttnb.setMaSoThue(req.getMaSoThue());
    //     ttnb.setTrangThai(TTNB_TrangThai.PENDING);

    //     ThongTinNguoiBan savedTTNB = repository.save(ttnb);

    //     // tạo tài khoản ngân hàng
    //     TaiKhoanNganHang bank = new TaiKhoanNganHang();
    //     bank.setTtnbId(savedTTNB);
    //     bank.setMaNganHang(req.getNganHang().getMaNganHang());
    //     bank.setTenNganHang(req.getNganHang().getTenNganHang());
    //     bank.setSoTaiKhoan(req.getNganHang().getSoTaiKhoan());
    //     bank.setTenTaiKhoan(req.getNganHang().getTenTaiKhoan());
    //     bank.setNgayTao(LocalDateTime.now());

    //     taiKhoanNganHangRepository.save(bank);
    // }

    @Transactional
    public void registerSeller(UUID userId, SellerRegisterRequest req) {

        // ===== B1: LẤY USER =====
        NguoiDung user = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        // ===== B2: LẤY ROLE SELLER =====
        VaiTro sellerRole = vaiTroRepository.findByLoai("SELLER");
        if (sellerRole == null) {
            throw new RuntimeException("Không tìm thấy role SELLER");
        }

        // ===== CHECK ĐÃ LÀ SELLER CHƯA =====
        boolean alreadySeller = vaiTroNguoiDungRepository
                .existsByNguoiDung_IdAndVaiTro_Id(userId, sellerRole.getId());

        if (alreadySeller) {
            throw new RuntimeException("User đã là seller");
        }

        // ===== TẠO VAI TRÒ NGƯỜI DÙNG =====
        VaiTroNguoiDung vtnd = new VaiTroNguoiDung();
        vtnd.setNguoiDung(user);
        vtnd.setVaiTro(vaiTroRepository.findByLoai("SELLER"));
        vtnd.setTrangThai(TrangThaiVaiTro.HOAT_DONG);
        vtnd.setNgayDuyet(LocalDateTime.now());

        vaiTroNguoiDungRepository.save(vtnd);

        // ===== B3: TẠO ĐỊA CHỈ =====
        DiaChi dc = new DiaChi();
        dc.setVaiTroNguoiDung(vtnd);
        dc.setTinhThanh(req.getProvince());
        dc.setQuanHuyen(req.getDistrict());
        dc.setPhuongXa(req.getWard());
        dc.setCuThe(req.getCuThe());

        // format địa chỉ full
        dc.setDiaChiDayDu(
            req.getCuThe() + ", " +
            req.getWard() + ", " +
            req.getDistrict() + ", " +
            req.getProvince()
        );

        dc.setThietLapMacDinh(1);
        dc.setStatus(1);

        diaChiRepository.save(dc);

        // ===== B4: TẠO THÔNG TIN NGƯỜI BÁN =====
        ThongTinNguoiBan ttnb = new ThongTinNguoiBan();
        ttnb.setNguoiDung(user);
        ttnb.setTienNhanCong(new BigDecimal(req.getTienNhanCong()));
        ttnb.setTienThuongHieu(new BigDecimal(req.getTienThuongHieu()));
        ttnb.setMaSoThue(req.getMaSoThue());
        ttnb.setTrangThai(TTNB_TrangThai.PENDING); 

        repository.save(ttnb);

        // ===== B5: TẠO TÀI KHOẢN NGÂN HÀNG =====
        TaiKhoanNganHang bank = new TaiKhoanNganHang();
        bank.setTtnbId(ttnb);
        bank.setMaNganHang(req.getMaNganHang());
        bank.setTenNganHang(req.getTenNganHang());
        bank.setSoTaiKhoan(req.getSoTaiKhoan());
        bank.setTenTaiKhoan(req.getTenTaiKhoan());
        bank.setTrangThai(TrangThaiTaiKhoanNganHang.CON_SU_DUNG);

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