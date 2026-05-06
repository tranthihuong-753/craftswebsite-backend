package com.example.demo.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD) // Cái nhãn này chỉ được phép dán lên trên các Phương thức (Method)
@Retention(RetentionPolicy.RUNTIME) // Hãy giữ cái nhãn này tồn tại cho đến tận lúc chương trình đang chạy
@Documented
public @interface ApiDescription {
    String value();
}