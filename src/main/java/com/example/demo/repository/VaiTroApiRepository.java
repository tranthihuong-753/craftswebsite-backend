package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Api;
import com.example.demo.entity.VaiTro;
import com.example.demo.entity.VaiTroApi;
 
public interface VaiTroApiRepository extends JpaRepository<VaiTroApi, Long> {
    List<VaiTroApi> findByVaiTro_Id(Long roleId);

    List<VaiTroApi> findAll();

    boolean existsByVaiTroAndApi (VaiTro vaiTro, Api api);
    
    // Spring sẽ hiểu là: Vào VaiTroApi -> lấy VaiTro -> lấy thuộc tính loai
    List<VaiTroApi> findByVaiTro_Loai(String loai);
    
    // Thêm hàm này để dùng cho logic update sau này
    Optional<VaiTroApi> findByVaiTroAndApi(VaiTro vaiTro, Api api);
}