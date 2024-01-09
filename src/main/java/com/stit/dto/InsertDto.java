/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stit.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author mike
 */
@Data
public class InsertDto {
    private String insertEmp;
    private String sheetNo;
    private BigDecimal itemNo;
}
