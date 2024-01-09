package com.stit.service.impl;

import com.stit.common.Pair;
import com.stit.dao.GetMtlInfoDao;
import com.stit.dao.MtlPrepPadDao;
import com.stit.dto.InsertDto;
import com.stit.dto.MtlPrepPadDto;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import java.sql.Types;

/**
 *
 * @author mike
 */
@Service
public class MtlPreIssuPadSvc {

    @Autowired
    private JdbcTemplate jdbc;
    public String insertData(List<InsertDto> dtoList) throws ClassNotFoundException, SQLException {
        
        String success;
        String errMesg;
        Integer sucessCount = 0;
        Integer failCount = 0;
        try {
            for (InsertDto dto : dtoList) {
                SimpleJdbcCall sp = new SimpleJdbcCall(this.jdbc).withProcedureName("SP_INS_MTL_ISSU_PAD");
                MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("pv_sheet_no", dto.getSheetNo(), Types.VARCHAR)
                        .addValue("pn_item_no", dto.getItemNo(), Types.VARCHAR)
                        .addValue("pv_emp_no", dto.getInsertEmp(), Types.VARCHAR);
                System.out.println("dto:"+dto.getSheetNo()+"@"+dto.getItemNo()+"@"+dto.getInsertEmp());
                Map out = sp.execute(params);
                success = (String) out.get("PV_SUCCESS");
                errMesg = (String) out.get("PV_ERROR_MESG");
                System.out.println("sucess:"+ success);
                if (success.equals("Y")) {
                    sucessCount++;
                } else {
                    failCount++;
                }
            }
        } catch (Exception e) {
            System.out.println("err:" + e.getMessage());
        }

        if (sucessCount != 0 && failCount == 0) {
            return "成功:" + sucessCount + "筆";
        } else if (sucessCount == 0 && failCount != 0) {
            return "失敗:" + failCount + "筆";
        } else {
            return "成功:" + sucessCount + "筆 失敗:" + failCount + "筆";
        }

    }
    public Boolean insertData_bak(Pair pair) throws ClassNotFoundException, SQLException {

//        String sql = "insert into mtl_pad_issu_chk(pad_sheet, crea_date, crea_emp)"
//                + "                         values(?, ?, ?)";
//
//        String padSheet = this.findSheetNo();
//        LocalDateTime now = LocalDateTime.now();
//        Timestamp ts = Timestamp.valueOf(now);
//        Object[] data = new Object[]{padSheet,
//            ts,
//            pair.getValue()
//        };
//        int successCount = this.jdbc.update(sql, data);
        Boolean chkResult = this.execChkIssu();

//        if (!chkResult){
//            String delSql = "DELETE mtl_pad_issu_chk WHERE pad_sheet = ? ";
//            int delcount = this.jdbc.update(delSql, padSheet);    
//        }
        return chkResult;

    }

    public Boolean updateByRecord(MtlPrepPadDto dto) throws SQLException {
        Boolean result = this.execChkSp(dto.getSheetNo(), dto.getItemNo(), dto.getSeqNo(), dto.getMtlSeq(), dto.getNewOld(), dto.getLocation(), dto.getIssuQty());

        String confYn = null;
        LocalDateTime confDate = LocalDateTime.now();
        Timestamp ts = null;
        if (dto.getIssuQty() == BigDecimal.ZERO) {
            confYn = "N";
            confDate = null;
            ts = null;
        } else {
            confYn = "Y";
            confDate = LocalDateTime.now();
            ts = Timestamp.valueOf(confDate);
        }

        if (result) {
            /*
            String sql = """
                       UPDATE mtl_prep_pad 
                          SET mtl_seq = ?,
                              new_old = ?,
                              location = ?,
                              issu_qty = ?,
                              conf_yn = 'Y',
                              conf_date = ?
                        WHERE sheet_no = ?
                          AND item_no = ?
                          AND seq_no = ?
                                                       """;
             */
            String sql
                    = "UPDATE mtl_prep_pad "
                    + "  SET mtl_seq = ?, "
                    + "      new_old = ?, "
                    + "      location = ?, "
                    + "      issu_qty = ?, "
                    + "      conf_yn = ?, "
                    + "      conf_date = ? "
                    + " WHERE sheet_no = ? "
                    + "       AND item_no = ? "
                    + "       AND seq_no = ? ";

            int successCount = this.jdbc.update(sql, new Object[]{dto.getMtlSeq(), dto.getNewOld(), dto.getLocation(), dto.getIssuQty(), confYn, ts, dto.getSheetNo(), dto.getItemNo(), dto.getSeqNo()});
            return result;
        } else {
            return result;
        }

    }

    public String delete(List<MtlPrepPadDto> dtoList) {
        String success;
        String errMesg;
        Integer sucessCount = 0;
        Integer failCount = 0;
        try {
            for (MtlPrepPadDto dto : dtoList) {
                SimpleJdbcCall sp = new SimpleJdbcCall(this.jdbc).withProcedureName("SP_PAD_DEL_MTL_PREP");
                MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("pv_sheet_no", dto.getSheetNo(), Types.VARCHAR)
                        .addValue("pn_item_no", dto.getItemNo(), Types.VARCHAR);

                Map out = sp.execute(params);
                success = (String) out.get("PV_SUCCESS");
                errMesg = (String) out.get("PV_ERROR_MESG");
                System.out.println("sucess:"+ success);
                if (success.equals("Y")) {
                    sucessCount++;
                } else {
                    failCount++;
                }
            }
        } catch (Exception e) {
            System.out.println("err:" + e.getMessage());
        }

        if (sucessCount != 0 && failCount == 0) {
            return "成功:" + sucessCount + "筆";
        } else if (sucessCount == 0 && failCount != 0) {
            return "失敗:" + failCount + "筆";
        } else {
            return "成功:" + sucessCount + "筆 失敗:" + failCount + "筆";
        }
    }

    public List<MtlPrepPadDao> findPrepData() {
        String sql = " SELECT p.*, m.mtl_name, m.mtl_spec, SF_GET_EMP_NAME(p.emp_no, 0) emp_name,"
                + "(SELECT parameter_value FROM con_trol WHERE parameter_code = 'MTL_PREP_TIME_B') prep_time_b,"
                + "(SELECT parameter_value FROM con_trol WHERE parameter_code = 'MTL_PREP_TIME_E') prep_time_e"
                + "   FROM mtl_prep_pad p, mtl_mast m"
                + "  WHERE m.mtl_no = p.mtl_no"
                + "    AND issu_yn = 'N'"
                + "ORDER BY p.mtl_no";
        List<MtlPrepPadDao> dao = jdbc.query(sql, new RowMapper<MtlPrepPadDao>() {
            @Override
            public MtlPrepPadDao mapRow(ResultSet rs, int rowNum) throws SQLException {
                MtlPrepPadDao mapper = new MtlPrepPadDao();
                mapper.setSheetNo(rs.getString("sheet_no"));
                mapper.setItemNo(rs.getBigDecimal("item_no"));
                mapper.setSeqNo(rs.getBigDecimal("seq_no"));
                mapper.setMchNo(rs.getString("mch_no"));
                mapper.setEmpNo(rs.getString("emp_no"));
                mapper.setEmpName(rs.getString("emp_name"));
                mapper.setMtlNo(rs.getString("mtl_no"));
                mapper.setMtlName(rs.getString("mtl_name"));
                mapper.setMtlSpec(rs.getString("mtl_spec"));
                mapper.setMtlSeq(rs.getString("mtl_seq"));
                mapper.setNewOld(rs.getString("new_old"));
                mapper.setLocation(rs.getString("location"));
                mapper.setStkQty(rs.getBigDecimal("stk_qty"));
                mapper.setPrepQty(rs.getBigDecimal("prep_qty"));
                mapper.setIssuQty(rs.getBigDecimal("issu_qty"));
                mapper.setConfYn(rs.getString("conf_yn"));
//                mapper.setCreaDate(rs.getTimestamp("conf_date").toLocalDateTime());
                mapper.setCreaEmp(rs.getString("crea_emp"));
                mapper.setCreaDate(rs.getTimestamp("crea_date").toLocalDateTime());
                mapper.setPrepTimeB(rs.getBigDecimal("prep_time_b"));
                mapper.setPrepTimeE(rs.getBigDecimal("prep_time_e"));

                return mapper;
            }
        });

        for (MtlPrepPadDao mtlPrepPadDao : dao) {
            String color = this.getRecordColor(mtlPrepPadDao.getCreaDate());
            mtlPrepPadDao.setRecordColor(color);
        }
        return dao;
    }

    public List<GetMtlInfoDao> getMtlSeq(String mtlNo) {
        String sql = " SELECT mtl_seq"
                + "   FROM mtl_stok m"
                + "  WHERE NOT EXISTS(SELECT 0 "
                + "                     FROM mtl_prep_pad "
                + "                    WHERE m.mtl_no = mtl_no"
                + "                      AND m.mtl_seq = mtl_seq"
                + "                      AND issu_yn = 'N')"
                + "    AND mtl_no = ?"
                + "  ORDER BY mtl_seq";
        Object[] params = {mtlNo};
        List<GetMtlInfoDao> dao = jdbc.query(sql, params, new RowMapper<GetMtlInfoDao>() {
            @Override
            public GetMtlInfoDao mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetMtlInfoDao mapper = new GetMtlInfoDao();
                mapper.setMtlSeq(rs.getString("mtl_seq"));

                return mapper;
            }
        });

        return dao;
    }

    public List<GetMtlInfoDao> getNewOld(String mtlNo, String mtlSeq) {
        String sql = " SELECT DISTINCT new_old"
                + "   FROM mtl_stok"
                + "  WHERE mtl_no = ?"
                + "    AND NVL(mtl_seq,'-@-') = DECODE(?, 'NULL', NVL(mtl_seq,'-@-'), ?)"
                + "  ORDER BY new_old";

        Object[] params = {mtlNo, mtlSeq, mtlSeq};
        List<GetMtlInfoDao> dao = jdbc.query(sql, params, new RowMapper<GetMtlInfoDao>() {
            @Override
            public GetMtlInfoDao mapRow(ResultSet rs, int rowNum) throws SQLException {

                GetMtlInfoDao mapper = new GetMtlInfoDao();
                mapper.setNewOld(rs.getString("new_old"));
                return mapper;
            }
        });

        return dao;
    }

    public List<GetMtlInfoDao> getLocation(String mtlNo, String mtlSeq, String newOld) {
        String sql = " SELECT DISTINCT location"
                + "   FROM mtl_stok"
                + "  WHERE mtl_no = ?"
                + "    AND NVL(mtl_seq,'-@-') = DECODE(?, 'NULL', NVL(mtl_seq,'-@-'), ?)"
                + "    AND new_old = DECODE(?, 'NULL', new_old, ?)"
                + "  ORDER BY location";
        Object[] params = {mtlNo, mtlSeq, mtlSeq, newOld, newOld};
        List<GetMtlInfoDao> dao = jdbc.query(sql, params, new RowMapper<GetMtlInfoDao>() {
            @Override
            public GetMtlInfoDao mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetMtlInfoDao mapper = new GetMtlInfoDao();
                mapper.setLocation(rs.getString("location"));

                return mapper;
            }
        });

        return dao;
    }

    public Boolean execChkSp(String sheetNo, BigDecimal itemNo, BigDecimal seqNo, String mtlSeq, String newOld, String loc, BigDecimal issuQty) {
        String success;
        String errMesg;

        SimpleJdbcCall sp = new SimpleJdbcCall(this.jdbc).withProcedureName("SP_CHK_MTL_STOK_PAD");
        Map out = sp.execute(sheetNo, itemNo, seqNo, mtlSeq, newOld, loc, issuQty);

        success = (String) out.get("PV_SUCCESS");
        errMesg = (String) out.get("PV_ERROR_MESG");
        if (success.equals("Y")) {
            return true;
        } else {
            throw new RuntimeException(errMesg);
        }

    }

    public Boolean execChkIssu() {
        String success;
        String errMesg;

        SimpleJdbcCall sp = new SimpleJdbcCall(this.jdbc).withProcedureName("SP_INS_MTL_ISSU_PAD");
        Map out = sp.execute();

        if (out.isEmpty()) {
            throw new RuntimeException("查無未確認待領用資料");
        }
        success = (String) out.get("PV_SUCCESS");
        errMesg = (String) out.get("PV_ERROR_MESG");
        if (success.equals("Y")) {
            return true;
        } else {
            throw new RuntimeException(errMesg);
        }

    }

    public Boolean chkNewSheet(String dateTime) {
        String result;

        try {
            String sql = " SELECT DISTINCT 'Y'"
                    + "      FROM mtl_prep_pad"
                    + "     WHERE TO_CHAR(crea_date,'YYYY-MM-DD HH24:MI:SS') > ?";
            Object[] params = {dateTime.replace("T", " ")};
            List<String> resultList = jdbc.queryForList(sql, params, String.class);
            System.out.println("result list:" + resultList + "time:"+dateTime);
            if (!resultList.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("ex:" + e);
            throw new RuntimeException(e);
        }

    }

    public String getRecordColor(LocalDateTime dateTime) {
        String color = null;
        Duration duration = Duration.between(dateTime, LocalDateTime.now());
        BigDecimal hours = new BigDecimal(duration.toHours());

        String sqlB = "SELECT parameter_value FROM con_trol WHERE parameter_code = 'MTL_PREP_TIME_B'";
        BigDecimal prepTimeB = this.jdbc.queryForObject(sqlB, BigDecimal.class);

        String sqlE = "SELECT parameter_value FROM con_trol WHERE parameter_code = 'MTL_PREP_TIME_E'";
        BigDecimal prepTimeE = this.jdbc.queryForObject(sqlE, BigDecimal.class);

        if (hours.compareTo(prepTimeB) == -1) {
            color = "G";
        } else if (hours.compareTo(prepTimeB) == 1 && hours.compareTo(prepTimeE) < 1) {
            color = "Y";
        } else if (hours.compareTo(prepTimeE) == 1) {
            color = "R";
        }
        return color;
    }

    public String findSheetNo() {
//        String sql = " SELECT SF_GET_NO('mtl_pad_issu_pad', 'sheet_no'," +
//"                                       TO_CHAR(SYSDATE, 'YYMMDD'), 4)"
//                + "      FROM dual";
        String sql = " SELECT TO_CHAR(SYSDATE, 'YYMMDD')||LPAD(TO_NUMBER(NVL(MAX(SUBSTR(pad_sheet, -4,4)),0)) + 1,4,0)"
                + "      FROM mtl_pad_issu_chk";
        String sheetNo = jdbc.queryForObject(sql, String.class);
        return sheetNo;
    }
}
