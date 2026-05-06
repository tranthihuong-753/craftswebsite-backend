package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.AnhVideo;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.VaiTro;
import com.example.demo.entity.VaiTroNguoiDung;
import com.example.demo.enums.ND_Trangthaixacthuc;
import com.example.demo.enums.TrangThaiVaiTro;
import com.example.demo.model.ND_CCCD;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.VaiTroNguoiDungRepository;
import com.example.demo.repository.VaiTroRepository;
import com.example.demo.security.JwtService;

@Service
public class NguoiDungService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    private CCCDService cccdService;

    @Autowired
    private AnhVideoService anhVideoService;

    @Autowired
    private VaiTroNguoiDungRepository vtndRepository;

    @Autowired
    private JwtService jwtService;
    
    // CREATE
    public NguoiDung createNguoiDung(NguoiDung nguoiDung) {
        return nguoiDungRepository.save(nguoiDung);
    }

    // READ ALL
    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungRepository.findAll();
    }

    // READ BY ID
    public NguoiDung getNguoiDungById(UUID id) {
        return nguoiDungRepository.findById(id).orElse(null);
    }

    // UPDATE
    public NguoiDung updateNguoiDung(UUID id, NguoiDung newNguoiDung) {

        NguoiDung nguoiDung = nguoiDungRepository.findById(id).orElse(null);

        if (nguoiDung != null) {
            nguoiDung.setTen(newNguoiDung.getTen());
            nguoiDung.setSdt(newNguoiDung.getSdt());
            nguoiDung.setTenDangNhap(newNguoiDung.getTenDangNhap());
            nguoiDung.setMatKhau(newNguoiDung.getMatKhau());
            nguoiDung.setTrangThaiXacThuc(newNguoiDung.getTrangThaiXacThuc());
            nguoiDung.setDiaChi(newNguoiDung.getDiaChi());
            nguoiDung.setAnhVideo_anhChanDung(newNguoiDung.getAnhVideo_anhChanDung());
            nguoiDung.setAnhVideo_anhCCCD(newNguoiDung.getAnhVideo_anhCCCD());
            nguoiDung.setVectorCCCD(newNguoiDung.getVectorCCCD());

            return nguoiDungRepository.save(nguoiDung);
        }

        return null;
    }

    // DELETE
    public void deleteNguoiDung(UUID id) {
        nguoiDungRepository.deleteById(id);
    }

    // UPDATE TAI KHOAN LEVEL 2 - SU DUNG CCCD 
    public NguoiDung taoNguoiDungTuCCCD(UUID userId, String imageUrl) {

        NguoiDung nd = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        // gọi python OCR
        ND_CCCD cccd = cccdService.scanCCCD(imageUrl);

        if (cccd == null) {
            throw new RuntimeException("Không đọc được CCCD");
        }

        nd.setCccd(cccd);
        nd.setTen(cccd.getHoTen());

        nd.setTrangThaiXacThuc(ND_Trangthaixacthuc.CCCD);

        AnhVideo av = new AnhVideo();
        av.setLink(imageUrl);
        nd.setAnhVideo_anhCCCD(anhVideoService.create(av));

        // lay anh lam anh chan dung 
        AnhVideo avChanDung = new AnhVideo();
        avChanDung.setLink(imageUrl);
        nd.setAnhVideo_anhChanDung(anhVideoService.create(avChanDung));

        return nguoiDungRepository.save(nd);
    }
 
    // TAO TAI KHOAN LEVEL 1 - SU DUNG SDT 
    public Map<String, Object> dangKyBangSDT(String sdt) {

        // kiểm tra định dạng sdt
        if (sdt == null || !sdt.matches("0\\d{9}")) {
            return null;
        }
        // kiểm tra sdt đã tồn tại chưa
        if (nguoiDungRepository.existsBySdt(sdt)) {
            NguoiDung nd_ = nguoiDungRepository.findBySdt(sdt).orElse(null);

            Map<String, Object> result = new HashMap<>();
            
            UUID userId = nd_.getId();

            List<VaiTroNguoiDung> list =
                    vtndRepository.findByNguoiDung_Id(userId);

            List<String> roles = list.stream()
                    .map(v -> v.getVaiTro().getLoai())
                    .toList();

            String token = jwtService.generateToken(nd_, roles);

            result.put("token", token);
            result.put("user", nd_);

            return result;
        }
        
        Map<String, Object> result = new HashMap<>();

        NguoiDung nd = new NguoiDung();

        nd.setSdt(sdt);
        nd.setTrangThaiXacThuc(ND_Trangthaixacthuc.PHONE_VERIFIED);

        NguoiDung nd_ = nguoiDungRepository.save(nd);

        UUID userId = nd_.getId();

        List<VaiTroNguoiDung> list =
                vtndRepository.findByNguoiDung_Id(userId);

        List<String> roles = list.stream()
                .map(v -> v.getVaiTro().getLoai())
                .toList();

        String token = jwtService.generateToken(nd_, roles);

        result.put("token", token);
        result.put("user", nd_);

        return result;
    }

    // UPDATE TAI KHOAN LEVEL 3 - TAO USERNAME PASSWORD 
    public NguoiDung datMatKhau(UUID userId, String matKhau, String tenDangNhap) {
        if (matKhau == null || matKhau.length() < 8) {
            throw new RuntimeException("Mật khẩu phải từ 8 ký tự trở lên");
        }

        NguoiDung nd = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        nd.setMatKhau(matKhau);

        nd.setTenDangNhap(tenDangNhap);

        nd.setTrangThaiXacThuc(ND_Trangthaixacthuc.CCCD_PASS);

        NguoiDung saved = nguoiDungRepository.save(nd);

        // gán role BUYER
        VaiTroNguoiDung vtnd = new VaiTroNguoiDung();
        vtnd.setNguoiDung(saved);
        vtnd.setVaiTro(vaiTroRepository.findByLoai("BUYER"));
        vtnd.setTrangThai(TrangThaiVaiTro.HOAT_DONG);
        vtndRepository.save(vtnd);

        return saved;
    }

    public List<String> getAllTenDangNhap() {
        return nguoiDungRepository.findAllTenDangNhap();
    }

    // ĐĂNG NHẬP 
    public Map<String, Object> login(String username, String password) {

        Optional<NguoiDung> userOpt =
                nguoiDungRepository.findByTenDangNhapAndMatKhau(username, password);

        if(userOpt.isEmpty()) return null;

        NguoiDung user = userOpt.get();

        if(!user.getMatKhau().equals(password)){
            return null;
        }

        UUID userId = user.getId();

        List<VaiTroNguoiDung> list =
                vtndRepository.findByNguoiDung_Id(userId);

        List<String> roles = list.stream()
                .map(v -> v.getVaiTro().getLoai())
                .toList();

        Map<String, Object> result = new HashMap<>();

        String token = jwtService.generateToken(user, roles);

        result.put("token", token);
        result.put("roles", roles);

        return result;
    }

    // tu id lay anhchandung 
    public AnhVideo getAnhChanDungById(UUID id) {
        return nguoiDungRepository.findAnhChanDungById(id)
                .orElse(null);
    }

    // tu id lay ten nguoi dung
    public String getTenDangNhapById(UUID id) {

        Optional<String> tenOpt = nguoiDungRepository.findTenDangNhapById(id);

        if (tenOpt.isEmpty() || tenOpt.get() == null || tenOpt.get().isBlank()) {
            return "Admin";
        }

        return tenOpt.get();
    }

    public UUID loginAndGetUserId(String username, String password) {

        Optional<NguoiDung> userOpt = nguoiDungRepository.findByTenDangNhapAndMatKhau(username, password);

        if(userOpt.isEmpty()) return null;

        NguoiDung user = userOpt.get();

        if(!user.getMatKhau().equals(password)){
            return null;
        }

        return user.getId();
    }

}
