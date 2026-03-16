package com.example.demo.config;

import com.example.demo.entity.DanhMuc;
import com.example.demo.entity.VaiTro;
import com.example.demo.enums.TrangThaiDanhMuc;
import com.example.demo.repository.DanhMucRepository;
import com.example.demo.repository.VaiTroRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final VaiTroRepository vaiTroRepository;

    @Bean
    CommandLineRunner initRoles() {
        return args -> {

            String[] roles = {
                    "BUYER",
                    "SELLER",
                    "SHIPPER",
                    "COMPLIANCE_ADMIN",
                    "SUPPORT_ADMIN",
                    "SUPER_ADMIN"
            };

            for (String role : roles) {
                // check xem ton tai chua roi moi tao 
                if (vaiTroRepository.existsByLoai(role)) {
                    continue;
                }
                VaiTro vt = new VaiTro();
                vt.setLoai(role);
                vaiTroRepository.save(vt);
            }
        };
    }

    private final DanhMucRepository danhMucRepository;

    @Bean
    CommandLineRunner initDanhMuc() {
        return args -> {

            String[] categories = {

                    // Nhóm 1 - mỹ nghệ thủ công
                    "Đồ gỗ handmade",
                    "Đồ tre thủ công",
                    "Đồ mây đan",
                    "Đồ gốm handmade",
                    "Đồ trang trí gỗ",
                    "Tranh handmade",
                    "Tranh thêu tay",
                    "Tranh vẽ tay",
                    "Tượng gỗ thủ công",
                    "Đồ decor handmade",

                    // phụ kiện
                    "Vòng tay handmade",
                    "Dây chuyền handmade",
                    "Bông tai handmade",
                    "Nhẫn handmade",
                    "Phụ kiện tóc handmade",
                    "Túi vải handmade",
                    "Ví handmade",
                    "Móc khóa handmade",
                    "Phụ kiện điện thoại handmade",
                    "Trang sức resin",

                    // vải & may
                    "Khăn len handmade",
                    "Áo len handmade",
                    "Thú bông handmade",
                    "Búp bê handmade",
                    "Đồ trang trí vải",
                    "Túi thêu tay",
                    "Đồ crochet",
                    "Đồ knitting",
                    "Patch vải handmade",
                    "Đồ decor vintage",

                    // Nhóm 2 - thực phẩm handmade
                    "Bánh handmade",
                    "Bánh quy handmade",
                    "Mứt handmade",
                    "Kẹo handmade",
                    "Socola handmade",
                    "Đồ ăn vặt handmade",
                    "Trà thảo mộc handmade",
                    "Cà phê rang xay thủ công",

                    // Nhóm 3 - cá nhân hóa
                    "Quà tặng handmade",
                    "Quà sinh nhật handmade",
                    "Quà lưu niệm handmade",
                    "Quà kỷ niệm handmade",
                    "Đồ decor cá nhân hóa",
                    "Tranh khắc tên",
                    "Trang sức khắc tên",
                    "Đồ DIY bán thành phẩm",
                    "Kit DIY handmade",
                    "Quà handmade theo yêu cầu",
                    "Sản phẩm custom"
            };

            BigDecimal defaultTax = new BigDecimal("0.03"); // 3%

            for (String name : categories) {

                if (danhMucRepository.existsByTen(name)) {
                    continue;
                }

                DanhMuc dm = new DanhMuc();
                dm.setTen(name);
                dm.setMoTa("Danh mục sản phẩm thủ công: " + name);
                dm.setTrangThai(TrangThaiDanhMuc.HOAT_DONG); // nếu enum khác thì sửa lại
                dm.setThueSuatHienTai(defaultTax);

                danhMucRepository.save(dm);
            }
        };
    }

}