package com.example.demo.controller;

import com.example.demo.annotation.ApiDescription;
import com.example.demo.dto.OrderListDTO;
import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.ReviewRequest;
import com.example.demo.entity.AnhVideoSanPham;
import com.example.demo.entity.ChiTietDonHang;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.SanPham;
import com.example.demo.repository.AnhVideoSanPhamRepository;
import com.example.demo.repository.SanPhamCoSanRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.service.DonHangService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/donhang")
public class DonHangController {

    private final DonHangService service;

    public DonHangController(DonHangService service) {
        this.service = service;
    }

    // @GetMapping
    // @ApiDescription("Truy vấn danh sách toàn bộ đơn hàng và lịch sử giao dịch trên hệ thống")
    // public List<DonHang> getAll() {
    //     return service.getAll();
    // }

    // @GetMapping("/{id}")
    // @ApiDescription("Xem thông tin chi tiết đơn hàng, sản phẩm snapshot và trạng thái thanh toán theo ID")
    // public DonHang getById(@PathVariable Long id) {
    //     return service.getById(id);
    // }

    // @PostMapping
    // @ApiDescription("Khởi tạo đơn hàng mới và thực hiện chốt dữ liệu snapshot (giá, thuế, phí)")
    // public DonHang create(@RequestBody DonHang donHang) {
    //     return service.save(donHang);
    // }

    // @PutMapping("/{id}")
    // @ApiDescription("Cập nhật thông tin đơn hàng hoặc thay đổi trạng thái tiến độ giao dịch")
    // public DonHang update(@PathVariable Long id, @RequestBody DonHang donHang) {
    //     donHang.setId(id);
    //     return service.save(donHang);
    // }

    // @DeleteMapping("/{id}")
    // @ApiDescription("Gỡ bỏ thông tin đơn hàng khỏi danh sách quản lý nghiệp vụ")
    // public void delete(@PathVariable Long id) {
    //     service.delete(id);
    // }


    @PatchMapping("/{orderId}/confirm-received")
    @ApiDescription("Xác nhận đã nhận hàng và lưu đánh giá từ người mua")
    public ResponseEntity<?> confirmReceived(
            @PathVariable Long orderId,
            @RequestBody ReviewRequest reviewRequest, // Backend hứng JSON từ Frontend ở đây
            HttpServletRequest request) {

        // 1. Lấy userId từ token (như các API trước)
        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) return ResponseEntity.status(401).build();
        UUID userId = UUID.fromString(userIdStr);

        // 2. Gọi Service xử lý nghiệp vụ
        try {
            service.confirmAndReview(orderId, userId, reviewRequest);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Chốt đơn và lưu đánh giá thành công!"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
        
    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private AnhVideoSanPhamRepository anhVideoSanPhamRepository;

    @GetMapping("/my-orders")
    @ApiDescription("Lấy danh sách đơn hàng DTO để hiển thị giao diện OrderList")
    public ResponseEntity<List<OrderListDTO>> getMyOrders(
            @RequestParam(required = false) String status, 
            HttpServletRequest request) {

        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) return ResponseEntity.status(401).build();
        UUID userId = UUID.fromString(userIdStr);
        
        List<DonHang> orders;
        if (status == null || "all".equalsIgnoreCase(status)) {
            orders = service.findByNguoiMuaId(userId);
        } else {
            orders = service.findByNguoiMuaIdAndStatus(userId, status);
        }

        // Chuyển đổi từ Entity sang DTO phẳng
        List<OrderListDTO> dtoList = orders.stream().map(order -> {
            OrderListDTO dto = new OrderListDTO();
            dto.setId(order.getId());
            dto.setMaDon(order.getMaDon());
            dto.setStatus(order.getTrangThai());
            dto.setOrderDate(order.getNgayDat());
            dto.setTotalPrice(order.getTienPhaiThanhToan());
            dto.setShopName("Handcraft Shop"); // Hoặc lấy từ NguoiBanId nếu có link bảng

            // Lấy thông tin sản phẩm từ Chi tiết đơn hàng đầu tiên (Vì 1 đơn 1 sản phẩm)
            if (order.getChiTietDonHangs() != null && !order.getChiTietDonHangs().isEmpty()) {
                ChiTietDonHang detail = order.getChiTietDonHangs().get(0);
                dto.setProductName(detail.getTenSanPham());
                dto.setQuantity(detail.getSoLuong());
                dto.setUnitPrice(detail.getDonGia());
                SanPham sp = sanPhamRepository.findById(detail.getSanPhamId()).orElse(null);
                List<AnhVideoSanPham> img = anhVideoSanPhamRepository.findBySanPhamIdAndType(sp.getId(), "IMAGE");
                dto.setProductImg(img.getFirst().getLink()); // Nếu có trường ảnh
            }
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

}