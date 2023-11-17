/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stit.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author mike
 */
@Data
public class PrepTimeDao implements Serializable {

    private BigDecimal prepTimeB;
    private BigDecimal prepTimeE;
}


