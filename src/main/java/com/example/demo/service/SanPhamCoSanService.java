package com.example.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.dto.SanPhamCoSanRequest;
import com.example.demo.dto.SanPhamModerationDTO;
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

    // public Page<SellerProductDTO> getProducts(String status,Pageable pageable) {

    //     TrangThaiSanPham trangThai = TrangThaiSanPham.valueOf(status);
    //     Page<SanPhamCoSan> page = sanPhamCoSanRepository.findBySanPhamTrangThai(trangThai, pageable);

    //     return new PageImpl<>(
    //         page.getContent().stream().map(spcs -> {

    //             SellerProductDTO dto = new SellerProductDTO();

    //             dto.setId(spcs.getId());

    //             dto.setGia(spcs.getGia());

    //             dto.setMoTa(spcs.getMoTa());

    //             dto.setSoLuongBanDau(spcs.getSoLuongBanDau());

    //             if (spcs.getSanPham().getLoaiSanPham() != null) {
    //                 dto.setLoaiSanPham(
    //                     spcs.getSanPham()
    //                         .getLoaiSanPham()
    //                         .getLoai()
    //                 );
    //             } else {
    //                 dto.setLoaiSanPham(null);
    //             }

    //             dto.setChungChiId(
    //                     spcs.getSanPham()
    //                             .getChungChi()
    //                             .getId()
    //             );

    //             Long spId = spcs.getSanPham().getId();

    //             AnhVideoSanPham av =
    //                     anhVideoSanPhamRepository
    //                             .findFirstBySanPhamIdOrderByThuTuAsc(spId)
    //                             .orElse(null);

    //             if (av != null) {
    //                 dto.setImage(av.getLink());
    //             }

    //             return dto;

    //         }).collect(Collectors.toList()),
    //         pageable,
    //         page.getTotalElements()
    //     );
    // }

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

    public SanPhamCoSan getById(Long id) {

        return sanPhamCoSanRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }
    // updateSanPhamCoSan(id, request)
    @Transactional
    public SanPhamCoSan updateSanPhamCoSan(
            Long id,
            SanPhamCoSanRequest request
    ) {

        // 1 tìm sản phẩm có sẵn
        SanPhamCoSan sanPhamCoSan = sanPhamCoSanRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // 2 update thông tin SPCS
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

        // 3 update sản phẩm gốc
        SanPham sanPham = sanPhamCoSan.getSanPham();

        sanPham.setTrangThai(request.getTrangThaiSanPham());
        sanPham.setSoGioLamViecUocTinh(request.getSoGioLamViecUocTinh());

        // 4 update danh mục nếu có thay đổi
        if (request.getDanhMucId() != null) {

            DanhMuc danhMuc = danhMucRepository
                    .findById(request.getDanhMucId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

            sanPham.setDanhMuc(danhMuc);
        }

        // 5 update media
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
    
    public Page<SanPhamModerationDTO> getModerationProducts(
            String status,
            String search,
            int page,
            int size,
            String sort
    ) {

        TrangThaiSanPham trangThai = TrangThaiSanPham.valueOf(status);

        Sort sortObj = sort.equalsIgnoreCase("asc")
                ? Sort.by("sanPham.ngayTao").ascending()
                : Sort.by("sanPham.ngayTao").descending();

        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<SanPhamCoSan> pageResult;

        if (search != null && !search.isEmpty()) {
            pageResult = sanPhamCoSanRepository
                    .findBySanPhamTrangThaiAndTimKiemContaining(trangThai, search, pageable);
        } else {
            pageResult = sanPhamCoSanRepository
                    .findBySanPhamTrangThai(trangThai, pageable);
        }

        return new PageImpl<>(
            pageResult.getContent().stream().map(spcs -> {

                SanPhamModerationDTO dto = new SanPhamModerationDTO(
                        spcs.getId(),
                        null,
                        spcs.getSanPham()
                                .getThongTinNguoiBan()
                                .getNguoiDung()
                                .getTen(),
                        spcs.getSanPham().getNgayTao()
                );

                Long spId = spcs.getSanPham().getId();

                // ảnh
                AnhVideoSanPham av =
                        anhVideoSanPhamRepository
                                .findFirstBySanPhamIdOrderByThuTuAsc(spcs.getSanPham().getId())
                                .orElse(null);

                if (av != null) {
                    dto.setAnhSanPham(av.getLink());
                }

                // 🔥 LẤY LOG THEO STATUS
                // ==============================

                Optional<NhatKyKiemToan> logOpt = Optional.empty();

                if (trangThai == TrangThaiSanPham.DANG_BAN) {

                    // 👉 sản phẩm đã DUYỆT
                    logOpt = nhatKyKiemToanRepository
                            .findTopByIdMucTieuAndLoaiMucTieuAndHanhDongOrderByNgayTaoDesc(
                                    spId,
                                    NKKT_LoaiMucTieu.SAN_PHAM,
                                    NKKT_HanhDong.TAO_SAN_PHAM
                            );

                } else if (trangThai == TrangThaiSanPham.VI_PHAM) {

                    // 👉 sản phẩm VI PHẠM
                    logOpt = nhatKyKiemToanRepository
                            .findTopByIdMucTieuAndLoaiMucTieuAndHanhDongOrderByNgayTaoDesc(
                                    spId,
                                    NKKT_LoaiMucTieu.SAN_PHAM,
                                    NKKT_HanhDong.XOA_SAN_PHAM
                            );
                }

                // ==============================
                // 🔥 MAP LOG → DTO
                // ==============================

                logOpt.ifPresent(log -> {

                    dto.setNgayXuLy(log.getNgayTao());
                    dto.setAdminId(log.getIdTacNhan());

                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        Map<String, Object> map = mapper.readValue(
                                log.getSieuDuLieu(),
                                Map.class
                        );

                        dto.setLyDo((String) map.get("ly_do"));

                    } catch (Exception e) {
                        dto.setLyDo("-");
                    }
                });

                return dto;

            }).toList(),
            pageable,
            pageResult.getTotalElements()
        );
    }

}