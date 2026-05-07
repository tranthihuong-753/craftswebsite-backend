package com.example.demo.controller;

import com.example.demo.annotation.ApiDescription;
import com.example.demo.dto.OrderResponse;
import com.example.demo.entity.DonHang;
import com.example.demo.service.DonHangService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<OrderResponse> confirmReceived(
            @PathVariable Long orderId, 
            Principal principal) {
        
        // Lấy tên đăng nhập (hoặc ID) từ JWT token
        String username = principal.getName(); 
        
        OrderResponse response = service.confirmReceived(orderId, username);
        return ResponseEntity.ok(response);
    }
}