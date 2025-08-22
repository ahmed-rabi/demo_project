package com.example.demo.repository;

import com.example.demo.entity.StaticPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaticPageRepository extends JpaRepository<StaticPage, Integer> {
}