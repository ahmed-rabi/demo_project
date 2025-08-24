package com.example.demo.service;

import com.example.demo.entity.StaticPage;
import com.example.demo.repository.StaticPageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaticPageService {
    private final StaticPageRepository repository;

    public StaticPageService(StaticPageRepository repository) {
        this.repository = repository;
    }

    public List<StaticPage> getAll() {
        return repository.findAll();
    }

    public StaticPage getById(int id) {
        return repository.findById(id).orElse(null);
    }

    public StaticPage save(StaticPage staticPage) {
        return repository.save(staticPage);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }


    public StaticPage getFirstSetting() {
        return repository.findAll().stream().findFirst().orElse(null);
    }
}
