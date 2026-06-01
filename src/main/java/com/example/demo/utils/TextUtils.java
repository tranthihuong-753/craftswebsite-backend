package com.example.demo.utils;

import java.text.Normalizer;

public class TextUtils {

    public static String normalize(String text) {

        if (text == null) return "";

        String temp = Normalizer.normalize(text.toLowerCase(), Normalizer.Form.NFD);
        temp = temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        temp = temp.replace("đ", "d");

        return temp.replaceAll("[^a-z0-9]", "");
    }

}