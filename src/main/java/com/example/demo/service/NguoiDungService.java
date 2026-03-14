package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.AnhVideo;
import com.example.demo.entity.NguoiDung;
import com.example.demo.enums.ND_Trangthaixacthuc;
import com.example.demo.model.ND_CCCD;
import com.example.demo.repository.NguoiDungRepository;
@Service
public class NguoiDungService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    
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

    @Autowired
    private CCCDService cccdService;

    @Autowired
    private AnhVideoService anhVideoService;

    // public NguoiDung taoNguoiDungTuCCCD(String imageUrl) {
        
    //     // 1 đọc CCCD từ python
    //     ND_CCCD cccd = cccdService.scanCCCD(imageUrl);

    //     // 2 tạo người dùng
    //     NguoiDung nd = new NguoiDung();

    //     nd.setCccd(cccd);
    //     nd.setTen(cccd.getHoTen());
    //     nd.setTrangThaiXacThuc(ND_Trangthaixacthuc.CCCD);

    //     nguoiDungRepository.save(nd);

    //     // 3 gán role BUYER
    //     VaiTro roleBuyer = vaiTroRepository.findById(1L).orElseThrow();

    //     VaiTroNguoiDung vtnd = new VaiTroNguoiDung();
    //     vtnd.setNguoiDung(nd);
    //     vtnd.setVaiTro(roleBuyer);

    //     vtndRepository.save(vtnd);

    //     return nd;
    // }

    public NguoiDung taoNguoiDungTuCCCD(UUID userId, String imageUrl) {

        NguoiDung nd = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        // gọi python OCR
        ND_CCCD cccd = cccdService.scanCCCD(imageUrl);

        nd.setCccd(cccd);
        nd.setTen(cccd.getHoTen());

        nd.setTrangThaiXacThuc(ND_Trangthaixacthuc.CCCD);

        AnhVideo av = new AnhVideo();
        av.setLink(imageUrl);
        nd.setAnhVideo_anhCCCD(anhVideoService.create(av));

        return nguoiDungRepository.save(nd);
    }

    public NguoiDung dangKyBangSDT(String sdt) {
        Optional<NguoiDung> existing = nguoiDungRepository.findBySdt(sdt);

        if(existing.isPresent()){
            return existing.get(); // trả user cũ
        }

        NguoiDung nd = new NguoiDung();

        nd.setSdt(sdt);
        nd.setTrangThaiXacThuc(ND_Trangthaixacthuc.PHONE_VERIFIED);

        return nguoiDungRepository.save(nd);
    }

    public NguoiDung datMatKhau(UUID userId, String matKhau, String tenDangNhap) {

        NguoiDung nd = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        nd.setMatKhau(matKhau);

        nd.setTenDangNhap(tenDangNhap);

        nd.setTrangThaiXacThuc(ND_Trangthaixacthuc.CCCD_PASS);

        return nguoiDungRepository.save(nd);
    }

    public List<String> getAllTenDangNhap() {
        return nguoiDungRepository.findAllTenDangNhap();
    }

    // public boolean login(String username, String password) {

    //     Optional<NguoiDung> userOpt = nguoiDungRepository.findByTenDangNhap(username);

    //     if(userOpt.isEmpty()) return false;

    //     NguoiDung user = userOpt.get();

    //     return user.getMatKhau().equals(password);
    // }

    public UUID loginAndGetUserId(String username, String password) {

        NguoiDung user = nguoiDungRepository
                .findByTenDangNhapAndMatKhau(username, password)
                .orElseThrow(() -> new RuntimeException("Sai tên đăng nhập hoặc mật khẩu"));

        return user.getId();
    }

}
