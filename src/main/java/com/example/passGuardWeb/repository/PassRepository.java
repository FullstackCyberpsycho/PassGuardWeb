package com.example.passGuardWeb.repository;

import com.example.passGuardWeb.models.Password;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassRepository extends JpaRepository<Password, Long> {
    List<Password> findByUserId(Long userId);

    @Transactional
    Password deleteByIdAndUserId(Long id, Long userId);
}
