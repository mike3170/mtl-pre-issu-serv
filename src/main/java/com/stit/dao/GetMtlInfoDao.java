/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stit.dao;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author mike
 */
@Data
public class GetMtlInfoDao  implements Serializable{
    private String mtlSeq;
    private String newOld;
    private String location;
}
