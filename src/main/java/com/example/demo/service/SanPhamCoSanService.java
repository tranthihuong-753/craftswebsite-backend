package com.example.demo.service;

import com.example.demo.entity.AnhVideoSanPham;
import com.example.demo.entity.ChungChi;
import com.example.demo.entity.DanhMuc;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.entity.ThongTinNguoiBan;
import com.example.demo.enums.LoaiChungChi;
import com.example.demo.enums.LoaiMucTieuChungChi;
import com.example.demo.enums.TrangThaiChungChi;
import com.example.demo.enums.TrangThaiSanPham;
import com.example.demo.enums.TrangThaiSanPhamCoSan;
import com.example.demo.repository.AnhVideoSanPhamRepository;
import com.example.demo.repository.ChungChiRepository;
import com.example.demo.repository.DanhMucRepository;
import com.example.demo.repository.SanPhamCoSanRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.repository.ThongTinNguoiBanRepository;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class SanPhamCoSanService {

    @Autowired
    private SanPhamCoSanRepository sanPhamCoSanRepository;

    @Autowired
    private ThongTinNguoiBanRepository thongTinNguoiBanRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private ChungChiRepository chungChiRepository;

    @Autowired
    private AnhVideoSanPhamRepository anhVideoSanPhamRepository;
    
    // Tạo mới sản phẩm có sẵn
    // loaisanpham duoc admind cap nhat
    // danhmucsanpham duoc admind cap nhat
    public SanPhamCoSan createSanPhamCoSan(
            String moTa,
            BigDecimal gia,
            Double canNang,
            Double chieuDai,
            Double chieuRong,
            Double chieuCao,
            BigDecimal giaGoc,
            Long soLuongBanDau,
            Long soLuongHienTai,
            TrangThaiSanPhamCoSan trangThaiSPCS,
            UUID sellerId,
            Long danhMucId,
            TrangThaiSanPham trangThaiSanPham,
            Long soGioLamViecUocTinh,
            TrangThaiChungChi trangThaiChungChi,
            List<String> mediaLinks
    ) {
        // lấy người bán
        ThongTinNguoiBan thongTinNguoiBan = thongTinNguoiBanRepository
                .findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người bán"));
        // 2 lấy danh mục do seller chọn
        DanhMuc danhMuc = danhMucRepository
                .findById(danhMucId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        // 3 tạo chứng chỉ cho sản phẩm
        ChungChi chungChi = new ChungChi();
        chungChi.setLoai(LoaiChungChi.SAN_PHAM_CO_SAN); 
        chungChi.setLoaiMucTieu(LoaiMucTieuChungChi.SAN_PHAM);
        chungChi.setDiemTrungBinh(0f);
        chungChi.setTongDanhGia(0L);
        chungChi.setTrangThai(trangThaiChungChi);
        chungChi = chungChiRepository.save(chungChi);        

        // tạo sản phẩm
        SanPham sanPham = new SanPham();
        sanPham.setThongTinNguoiBan(thongTinNguoiBan);
        sanPham.setDanhMuc(danhMuc);
        sanPham.setTrangThai(trangThaiSanPham);
        sanPham.setSoGioLamViecUocTinh(soGioLamViecUocTinh);
        sanPham.setChungChi(chungChi);
        sanPham = sanPhamRepository.save(sanPham);

        // lưu ảnh và video
        if (mediaLinks != null && !mediaLinks.isEmpty()) {
            long order = 1;
            for (String link : mediaLinks) {

                AnhVideoSanPham media = new AnhVideoSanPham();
                media.setSanPham(sanPham);
                media.setLink(link);
                media.setThuTu(order++);

                anhVideoSanPhamRepository.save(media);
            }
        }

        // update id mục tiêu cho chứng chỉ
        chungChi.setIdMucTieu(sanPham.getId());
        chungChiRepository.save(chungChi);

        // tạo sản phẩm có sẵn
        SanPhamCoSan sanPhamCoSan = new SanPhamCoSan();
        sanPhamCoSan.setMoTa(moTa);
        sanPhamCoSan.setGia(gia);
        sanPhamCoSan.setCanNang(canNang);
        sanPhamCoSan.setChieuDai(chieuDai);
        sanPhamCoSan.setChieuRong(chieuRong);
        sanPhamCoSan.setChieuCao(chieuCao);
        sanPhamCoSan.setGiaGoc(giaGoc);
        sanPhamCoSan.setSoLuongBanDau(soLuongBanDau);
        sanPhamCoSan.setSoLuongHienTai(soLuongHienTai);
        sanPhamCoSan.setTrangThai(trangThaiSPCS);
        sanPhamCoSan.setSanPham(sanPham);

        return sanPhamCoSanRepository.save(sanPhamCoSan);
    }

}