package com.tiffin.delivery.dao;

import com.tiffin.delivery.models.TiffinPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiffinPlanDAO extends JpaRepository<TiffinPlan, Long> {
}
