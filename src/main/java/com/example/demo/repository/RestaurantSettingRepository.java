package com.example.demo.repository;

import com.example.demo.entity.RestaurantSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantSettingRepository extends JpaRepository<RestaurantSetting, Integer> {
}
