package com.example.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.dto.SanPhamCoSanRequest;
import com.example.demo.dto.SanPhamModerationDTO;
import com.example.demo.dto.SanPhamModerationProjection;
import com.example.demo.dto.SellerProductDTO;
import com.example.demo.entity.AnhVideoSanPham;
import com.example.demo.entity.ChungChi;
import com.example.demo.entity.DanhMuc;
import com.example.demo.entity.NhatKyKiemToan;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.entity.ThongTinNguoiBan;
import com.example.demo.enums.LoaiChungChi;
import com.example.demo.enums.LoaiMucTieuChungChi;
import com.example.demo.enums.NKKT_HanhDong;
import com.example.demo.enums.NKKT_LoaiMucTieu;
import com.example.demo.enums.TrangThaiChungChi;
import com.example.demo.enums.TrangThaiSanPham;
import com.example.demo.enums.TrangThaiSanPhamCoSan;
import com.example.demo.repository.AnhVideoSanPhamRepository;
import com.example.demo.repository.ChungChiRepository;
import com.example.demo.repository.DanhMucRepository;
import com.example.demo.repository.NhatKyKiemToanRepository;
import com.example.demo.repository.SanPhamCoSanRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.repository.ThongTinNguoiBanRepository;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    @Autowired
    private NhatKyKiemToanRepository nhatKyKiemToanRepository;

    @Autowired 
    private ThongTinNguoiBanService thongTinNguoiBanService;
    
    
    // TAO SAN PHAM CO SAN 
    // Tạo mới sản phẩm có sẵn
    // loaisanpham duoc admind cap nhat
    // danhmucsanpham duoc admind cap nhat
    @Transactional
    public SanPhamCoSan createSanPhamCoSan(
            SanPhamCoSanRequest request,
            UUID userId
    ) {
        UUID sellerId = thongTinNguoiBanService.getSellerIdByUserId(userId);

        if (sellerId == null) {
            throw new RuntimeException("Bạn chưa có tài khoản người bán");
        }

        ThongTinNguoiBan thongTinNguoiBan = thongTinNguoiBanRepository
            .findById(sellerId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người bán"));

        // 2 lấy danh mục do seller chọn
        DanhMuc danhMuc = danhMucRepository
                .findById(request.getDanhMucId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        // 3 tạo chứng chỉ cho sản phẩm
        ChungChi chungChi = new ChungChi();
        chungChi.setLoai(LoaiChungChi.SAN_PHAM_CO_SAN); 
        chungChi.setLoaiMucTieu(LoaiMucTieuChungChi.SAN_PHAM);
        chungChi.setDiemTrungBinh(0f);
        chungChi.setTongDanhGia(0L);
        chungChi.setTrangThai(request.getTrangThaiChungChi());
        chungChi = chungChiRepository.save(chungChi);        

        // tạo sản phẩm
        SanPham sanPham = new SanPham();
        sanPham.setThongTinNguoiBan(thongTinNguoiBan);
        sanPham.setDanhMuc(danhMuc);
        sanPham.setTrangThai(request.getTrangThaiSanPham());
        sanPham.setSoGioLamViecUocTinh(request.getSoGioLamViecUocTinh());
        sanPham.setChungChi(chungChi);
        sanPham = sanPhamRepository.save(sanPham);

        // lưu ảnh và video
        if (request.getMediaLinks() != null && !request.getMediaLinks().isEmpty()) {
            long order = 1;
            for (String link : request.getMediaLinks()) {

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
        sanPhamCoSan.setMoTa(request.getMoTa());
        sanPhamCoSan.setGia(request.getGia());
        sanPhamCoSan.setCanNang(request.getCanNang());
        sanPhamCoSan.setChieuDai(request.getChieuDai());
        sanPhamCoSan.setChieuRong(request.getChieuRong());
        sanPhamCoSan.setChieuCao(request.getChieuCao());
        sanPhamCoSan.setGiaGoc(request.getGiaGoc());
        sanPhamCoSan.setSoLuongBanDau(request.getSoLuongBanDau());
        sanPhamCoSan.setSoLuongHienTai(request.getSoLuongHienTai());
        sanPhamCoSan.setTrangThai(request.getTrangThaiSPCS());
        sanPhamCoSan.setSanPham(sanPham);

        return sanPhamCoSanRepository.save(sanPhamCoSan);
    }

    public Page<SellerProductDTO> getProducts(
        UUID sellerId,
        String status,
        String search,
        Pageable pageable
    ) {

        TrangThaiSanPhamCoSan trangThai = TrangThaiSanPhamCoSan.valueOf(status);

        Page<SanPhamCoSan> page;

        if (search != null && !search.isEmpty()) {
            page = sanPhamCoSanRepository
                    .searchProducts(sellerId, trangThai, search, pageable);
        } else {            
            page = sanPhamCoSanRepository
                    .findBySanPhamThongTinNguoiBanIdAndTrangThai(sellerId, trangThai, pageable);
        }

        return new PageImpl<>(
            page.getContent().stream().map(spcs -> {

                SellerProductDTO dto = new SellerProductDTO();

                dto.setId(spcs.getId());
                dto.setGia(spcs.getGia());
                dto.setMoTa(spcs.getMoTa());
                dto.setSoLuongBanDau(spcs.getSoLuongBanDau());

                if (spcs.getSanPham().getLoaiSanPham() != null) {
                    dto.setLoaiSanPham(
                            spcs.getSanPham()
                                    .getLoaiSanPham()
                                    .getLoai()
                    );
                }

                dto.setChungChiId(
                        spcs.getSanPham()
                                .getChungChi()
                                .getId()
                );

                Long spId = spcs.getSanPham().getId();

                AnhVideoSanPham av =
                        anhVideoSanPhamRepository
                                .findFirstBySanPhamIdOrderByThuTuAsc(spId)
                                .orElse(null);

                if (av != null) {
                    dto.setImage(av.getLink());
                }

                return dto;

            }).collect(Collectors.toList()),
            pageable,
            page.getTotalElements()
        );
    }

    // LAY SAN PHAM BANG Id 
    public SanPhamCoSan getById(Long id, UUID userId){
        SanPhamCoSan spcs = sanPhamCoSanRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        
        UUID sellerId = thongTinNguoiBanService.getSellerIdByUserId(userId);

        if (sellerId == null) {
            throw new RuntimeException("Bạn chưa có tài khoản người bán");
        }

        if (!spcs.getSanPham()
            .getThongTinNguoiBan()
            .getId()
            .equals(sellerId)) {

            throw new RuntimeException("Không có quyền truy cập sản phẩm này");
        }

        return spcs;

    }

    // UPDATE SAN PHAM BANG Id SAN PHAM
    // updateSanPhamCoSan(id, request)
    @Transactional
    public SanPhamCoSan updateSanPhamCoSan(
            Long id,
            SanPhamCoSanRequest request,
            UUID userId
    ) {

        UUID sellerId = thongTinNguoiBanService.getSellerIdByUserId(userId);

        if (sellerId == null) {
            throw new RuntimeException("Bạn không phải người bán");
        }

        // 1 tìm sản phẩm có sẵn
        SanPhamCoSan sanPhamCoSan = sanPhamCoSanRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // CHECK CHỦ SẢN PHẨM (CỰC QUAN TRỌNG)
        UUID ownerSellerId = sanPhamCoSan
                .getSanPham()
                .getThongTinNguoiBan()
                .getId();

        if (!ownerSellerId.equals(sellerId)) {
            throw new RuntimeException("Bạn không có quyền sửa sản phẩm này");
        }

        // Validate  (chống hack)
        if (request.getGia().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Giá phải > 0");
        }

        if (request.getSoLuongBanDau() <= 0) {
            throw new RuntimeException("Số lượng phải > 0");
        }

        // update thông tin SPCS
        sanPhamCoSan.setMoTa(request.getMoTa());
        sanPhamCoSan.setGia(request.getGia());
        sanPhamCoSan.setGiaGoc(request.getGiaGoc());
        sanPhamCoSan.setCanNang(request.getCanNang());

        sanPhamCoSan.setChieuDai(request.getChieuDai());
        sanPhamCoSan.setChieuRong(request.getChieuRong());
        sanPhamCoSan.setChieuCao(request.getChieuCao());

        sanPhamCoSan.setSoLuongBanDau(request.getSoLuongBanDau());
        sanPhamCoSan.setSoLuongHienTai(request.getSoLuongHienTai());

        sanPhamCoSan.setTrangThai(request.getTrangThaiSPCS());

        // update sản phẩm gốc
        SanPham sanPham = sanPhamCoSan.getSanPham();

        sanPham.setTrangThai(request.getTrangThaiSanPham());
        sanPham.setSoGioLamViecUocTinh(request.getSoGioLamViecUocTinh());

        // update danh mục nếu có thay đổi
        if (request.getDanhMucId() != null) {

            DanhMuc danhMuc = danhMucRepository
                    .findById(request.getDanhMucId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

            sanPham.setDanhMuc(danhMuc);
        }

        sanPhamRepository.save(sanPham);
        
        // update media
        if (request.getMediaLinks() != null) {

            // xóa media cũ
            anhVideoSanPhamRepository.deleteBySanPhamId(sanPham.getId());

            long order = 1;

            for (String link : request.getMediaLinks()) {

                AnhVideoSanPham media = new AnhVideoSanPham();
                media.setSanPham(sanPham);
                media.setLink(link);
                media.setThuTu(order++);

                anhVideoSanPhamRepository.save(media);
            }
        }

        return sanPhamCoSanRepository.save(sanPhamCoSan);
    }

    @Transactional
    public SanPhamCoSan updateSanPhamCoSanTrangThai(Long id, String tt) {

        SanPhamCoSan sanPhamCoSan = sanPhamCoSanRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        TrangThaiSanPhamCoSan ttspcs = TrangThaiSanPhamCoSan.valueOf(tt);
        sanPhamCoSan.setTrangThai(ttspcs);

        SanPham sanPham = sanPhamCoSan.getSanPham();

        if (sanPham != null) {
            TrangThaiSanPham ttsp = TrangThaiSanPham.valueOf(tt);
            sanPham.setTrangThai(ttsp);
            sanPhamRepository.save(sanPham);
        }

        return sanPhamCoSanRepository.save(sanPhamCoSan);
    } 

    // XOA SAN PHAM BANG Id SAN PHAM
    @Transactional
    public void deleteSanPham(Long id, UUID userId) {

        SanPhamCoSan spcs = sanPhamCoSanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // 🔥 Lấy sellerId từ user
        UUID sellerId = thongTinNguoiBanService.getSellerIdByUserId(userId);

        if (sellerId == null) {
            throw new RuntimeException("Bạn chưa có tài khoản người bán");
        }

        // 🔥 Check sản phẩm có thuộc seller này không
        if (!spcs.getSanPham().getThongTinNguoiBan().getId().equals(sellerId)) {
            throw new RuntimeException("Không có quyền xóa sản phẩm này");
        }

        // ✔ Soft delete
        TrangThaiSanPhamCoSan tt = TrangThaiSanPhamCoSan.DA_XOA;
        spcs.setTrangThai(tt);

        SanPham sp = spcs.getSanPham();
        if (sp != null) {
            sp.setTrangThai(TrangThaiSanPham.DA_XOA);
        }
    }

    public Page<SanPhamModerationDTO> getModerationProducts(
            String status,
            String search,
            int page,
            int size,
            String sort
    ) {

        TrangThaiSanPham trangThai = TrangThaiSanPham.valueOf(status);

        Sort sortObj = sort.equalsIgnoreCase("asc")
                ? Sort.by("ngayTao").ascending()
                : Sort.by("ngayTao").descending();

        Pageable pageable = PageRequest.of(page, size, sortObj);

        // 🔥 gọi query FULL JOIN
        Page<SanPhamModerationProjection> pageResult =
                sanPhamCoSanRepository.getModerationProductsFull(trangThai, search, pageable);
                // sanPhamCoSanRepository.getModerationProductsFull(trangThai.name(), search, pageable);

        return new PageImpl<>(
                pageResult.getContent().stream().map(p -> {

                    SanPhamModerationDTO dto = new SanPhamModerationDTO(
                            p.getSpcsId(),
                            p.getAnhSanPham(),
                            p.getTenSeller(),
                            p.getNgayTao()
                    );

                    // chỉ set log khi có (DANG_BAN, VI_PHAM)
                    dto.setNgayXuLy(p.getNgayXuLy());
                    dto.setAdminId(p.getAdminId());

                    // parse JSON ly_do
                    if (p.getSieuDuLieu() != null) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            Map<String, Object> map = mapper.readValue(
                                    p.getSieuDuLieu(),
                                    Map.class
                            );

                            dto.setLyDo((String) map.get("ly_do"));

                        } catch (Exception e) {
                            dto.setLyDo("-");
                        }
                    }

                    return dto;

                }).toList(),
                pageable,
                pageResult.getTotalElements()
        );
    }

    @Transactional
    public SanPhamCoSan updateSanPhamCoSanTrangThai(Long id) {

        SanPhamCoSan sanPhamCoSan = sanPhamCoSanRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        sanPhamCoSan.setTrangThai(TrangThaiSanPhamCoSan.DA_XOA);

        SanPham sanPham = sanPhamCoSan.getSanPham();

        if (sanPham != null) {
            sanPham.setTrangThai(TrangThaiSanPham.DA_XOA);
            sanPhamRepository.save(sanPham);
        }

        return sanPhamCoSanRepository.save(sanPhamCoSan);
    } 

}