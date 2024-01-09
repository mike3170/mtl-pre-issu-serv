package com.stit.controller;

import com.stit.common.ApiResponse;
import com.stit.common.Pair;
import com.stit.dao.GetMtlInfoDao;
import com.stit.dao.MtlPrepPadDao;
import com.stit.dto.InsertDto;
import com.stit.dto.MtlPrepPadDto;
import com.stit.service.impl.MtlPreIssuPadSvc;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mtlpreppad")
public class MtlPreIssuController {

    @Autowired
    private MtlPreIssuPadSvc svc;


    @PutMapping("insert")
    public ApiResponse insert(@RequestBody List<InsertDto> dtoList) {
        try {
            String result = this.svc.insertData(dtoList);

            return ApiResponse.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 修改
     *
     * @param
     * @return
     */
    @PutMapping("update")
    public ApiResponse update(@RequestBody MtlPrepPadDto dto) {
        try {
            Boolean result = this.svc.updateByRecord(dto);

            return ApiResponse.ok();
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("delete")
    public ApiResponse delete(@RequestBody List<MtlPrepPadDto> dtoList) {
        try {
            System.out.println("Dto list:"+ dtoList);
            String result = this.svc.delete(dtoList);
            return ApiResponse.ok(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }    

    @GetMapping("all")
    public ApiResponse findAll() {
        try {
            List<MtlPrepPadDao> resultList = this.svc.findPrepData();
            return ApiResponse.ok(resultList);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("getmtlseq/{mtlNo}")
    public ApiResponse getMtlSeq(@PathVariable String mtlNo) {
        try {
            List<GetMtlInfoDao> resultList = this.svc.getMtlSeq(mtlNo);
            return ApiResponse.ok(resultList);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("getnewold/{mtlNo}/{mtlSeq}")
    public ApiResponse getNewOld(@PathVariable String mtlNo, @PathVariable String mtlSeq) {
        try {
            List<GetMtlInfoDao> resultList = this.svc.getNewOld(mtlNo, mtlSeq);
            return ApiResponse.ok(resultList);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("getlocation/{mtlNo}/{mtlSeq}/{newOld}")
    public ApiResponse getLocation(@PathVariable String mtlNo, @PathVariable String mtlSeq, @PathVariable String newOld) {
        try {
            List<GetMtlInfoDao> resultList = this.svc.getLocation(mtlNo, mtlSeq, newOld);
            return ApiResponse.ok(resultList);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("chkNewSheet/{sheetTime}")
    public ApiResponse chkNewSheet(@PathVariable String sheetTime) {
        try {
            Boolean result = this.svc.chkNewSheet(sheetTime);
            
            return ApiResponse.ok(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }    

}
