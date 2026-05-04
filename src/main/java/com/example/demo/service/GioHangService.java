package com.example.demo.service;

import com.example.demo.dto.CartResponse;
import com.example.demo.dto.MediaDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ShopDTO;
import com.example.demo.entity.AnhVideo;
import com.example.demo.entity.GioHang;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.entity.VaiTroNguoiDung;
import com.example.demo.repository.GioHangRepository;
import com.example.demo.repository.SanPhamCoSanRepository;
import com.example.demo.repository.VaiTroNguoiDungRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class GioHangService {

    @Autowired
    private GioHangRepository repository;

    @Autowired
    private VaiTroNguoiDungRepository vaiTroNguoiDungRepository;
    
    @Autowired
    private SanPhamCoSanRepository sanPhamCoSanRepository;
    
    @Autowired
    private GioHangRepository gioHangRepository;
    

    public GioHang add(GioHang gh) {
        gh.setNgayTao(LocalDateTime.now());
        gh.setNgayCapNhat(LocalDateTime.now());
        return repository.save(gh);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // THEM SP VAO GIO HANG 
    public GioHang addToCart(Long spcsId, UUID userId) {

        // lấy VaiTroNguoiDung (user)
        VaiTroNguoiDung vtnd = vaiTroNguoiDungRepository
                .findByNguoiDung_Id(userId)
                .stream()
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Không tìm thấy user"));

        // lấy sản phẩm
        SanPhamCoSan spcs = sanPhamCoSanRepository
                .findById(spcsId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // check đã có trong giỏ chưa
        Optional<GioHang> existing = gioHangRepository
                .findByVaiTroNguoiDung_IdAndSanPham_Id(
                        vtnd.getId(),
                        spcs.getSanPham().getId()
                );

        if (existing.isPresent()) {
            GioHang gh = existing.get();
            gh.setSoLuong(gh.getSoLuong() + 1);
            return gioHangRepository.save(gh);
        }

        // tạo mới
        GioHang gh = new GioHang();
        gh.setVaiTroNguoiDung(vtnd);
        gh.setSanPham(spcs.getSanPham());
        gh.setSoLuong(1);
        gh.setDonGiaSnapshot(spcs.getGia());
        gh.setDuocChon(true);

        return gioHangRepository.save(gh);
    }

    // LAY SAN PHAM THEO USER ID DE HIEN THI TRONG GIO HANG 
    public CartResponse getCart(UUID userId) {

        List<GioHang> items = gioHangRepository.findByVaiTroNguoiDung_NguoiDung_Id(userId);

        Map<UUID, ShopDTO> shopMap = new LinkedHashMap<>();

        for (GioHang gh : items) {

            SanPham sp = gh.getSanPham();
            UUID shopId = sp.getThongTinNguoiBan().getId();
            String shopName = sp.getThongTinNguoiBan().getNguoiDung().getTen();

            ShopDTO shop = shopMap.get(shopId);

            if (shop == null) {
                shop = new ShopDTO();
                shop.setShopId(shopId);
                shop.setShopName(shopName);
                shop.setChecked(true);
                shop.setProducts(new ArrayList<>());
                shop.setTienPhaiThanhToan(gh.getDonGiaSnapshot().multiply(BigDecimal.valueOf(gh.getSoLuong())));
                shop.setPhiVanChuyen(BigDecimal.ZERO); 
                shopMap.put(shopId, shop);
            }

            ProductDTO p = new ProductDTO();
            p.setCartItemId(gh.getId());
            p.setProductId(sp.getId());
            p.setName(sp.getTen());
            p.setPrice(gh.getDonGiaSnapshot());
            p.setQuantity(gh.getSoLuong());
            p.setChecked(gh.getDuocChon());

            // MEDIA
            List<String> images = sp.getAnhVideos().stream()
                    .filter(av -> "IMAGE".equals(av.getType()))
                    .map(av -> av.getLink())
                    .toList();

            List<String> videos = sp.getAnhVideos().stream()
                    .filter(av -> "VIDEO".equals(av.getType()))
                    .map(av -> av.getLink())
                    .toList();


            p.setImageUrls(images);
            p.setVideoUrls(videos);

            shop.getProducts().add(p);

            if (!gh.getDuocChon()) {
                shop.setChecked(false);
            }
        }

        CartResponse res = new CartResponse();
        res.setShops(new ArrayList<>(shopMap.values()));

        return res;
    }

}