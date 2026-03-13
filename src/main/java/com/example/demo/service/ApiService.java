package com.example.demo.service;

import com.example.demo.entity.Api;
import com.example.demo.repository.ApiRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiService {

    @Autowired
    private ApiRepository apiRepository;

    // CREATE
    public Api create(Api api) {
        return apiRepository.save(api);
    }

    // READ ALL
    public List<Api> getAll() {
        return apiRepository.findAll();
    }

    // READ BY ID
    public Api getById(Long id) {
        return apiRepository.findById(id).orElse(null);
    }

    // UPDATE
    public Api update(Long id, Api newApi) {

        Api api = apiRepository.findById(id).orElse(null);

        if (api != null) {

            api.setTen(newApi.getTen());
            api.setPhuongThuc(newApi.getPhuongThuc());
            api.setDuongDan(newApi.getDuongDan());
            api.setHoatDong(newApi.getHoatDong());

            return apiRepository.save(api);
        }

        return null;
    }

    // DELETE
    public void delete(Long id) {
        apiRepository.deleteById(id);
    }

    // ACTIVATE
    public Api activate(Long id) {

        Api api = apiRepository.findById(id).orElse(null);

        if (api != null) {
            api.activate();
            return apiRepository.save(api);
        }

        return null;
    }

    // DEACTIVATE
    public Api deactivate(Long id) {

        Api api = apiRepository.findById(id).orElse(null);

        if (api != null) {
            api.deactivate();
            return apiRepository.save(api);
        }

        return null;
    }
}