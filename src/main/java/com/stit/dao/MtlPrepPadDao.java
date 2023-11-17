/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stit.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author mike
 */
@Data
public class MtlPrepPadDao  implements Serializable{
    private String sheetNo;
    private BigDecimal itemNo;
    private BigDecimal seqNo;
    private String mchNo;
    private String empNo;
    private String empName;
    private String mtlNo;
    private String mtlName;
    private String mtlSpec;
    private String mtlSeq;
    private String newOld;
    private String location;
    private BigDecimal stkQty;
    private BigDecimal prepQty;
    private BigDecimal issuQty;
    private String confYn;
    private LocalDateTime confDate;
    private String issuYn;
    private String issuNo;
    private LocalDateTime issuDate;
    private LocalDateTime editDate;
    private String editEmp;
    private LocalDateTime creaDate;
    private String creaEmp;
    private String recordColor;
    private BigDecimal prepTimeB;
    private BigDecimal prepTimeE;
}
