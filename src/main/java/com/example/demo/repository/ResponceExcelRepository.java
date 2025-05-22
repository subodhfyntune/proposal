package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ResponceExcel;

@Repository
public interface ResponceExcelRepository extends JpaRepository<ResponceExcel, Integer>{

}
