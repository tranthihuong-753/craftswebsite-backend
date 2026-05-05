package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import com.example.demo.entity.DanhGia;
import com.example.demo.entity.SanPham;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class SearchSpecification {

    public static Specification<SanPham> build(SearchRequest req) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // JOIN
            Join<Object, Object> spcs = root.join("sanPhamCoSan", JoinType.LEFT);
            Join<Object, Object> category = root.join("danhMuc", JoinType.LEFT);
            Join<Object, Object> shop = root.join("thongTinNguoiBan", JoinType.LEFT);
            Join<Object, Object> address = shop.join("diaChi", JoinType.LEFT);
            Join<Object, Object> reviews = root.join("danhGias", JoinType.LEFT);

            // chỉ sản phẩm đang bán
            predicates.add(cb.equal(root.get("spTrangThai"), "DANG_BAN"));

            // keyword search
            if (req.getQ() != null && !req.getQ().isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(root.get("spTen")),
                        "%" + req.getQ().toLowerCase() + "%"
                ));
            }

            // category
            if (req.getCategoryId() != null) {
                predicates.add(cb.equal(category.get("dmId"), req.getCategoryId()));
            }

            // price
            if (req.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        spcs.get("spcsGia"), req.getMinPrice()
                ));
            }

            if (req.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        spcs.get("spcsGia"), req.getMaxPrice()
                ));
            }

            // province (shop location)
            if (req.getProvince() != null) {
                predicates.add(cb.equal(address.get("thanhPho"), req.getProvince()));
            }

            // rating filter
            if (req.getRating() != null) {
                Subquery<Double> ratingSub = query.subquery(Double.class);
                Root<DanhGia> dg = ratingSub.from(DanhGia.class);

                ratingSub.select(cb.avg(dg.get("diem")))
                        .where(cb.equal(dg.get("sanPham"), root));

                predicates.add(cb.greaterThanOrEqualTo(ratingSub, req.getRating()));
            }

            // type filter
            if (req.getType() != null) {
                predicates.add(cb.equal(root.get("loaiSanPham"), req.getType()));
            }

            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
