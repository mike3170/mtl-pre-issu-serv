package com.stit.utils.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbUtil {
	@Autowired
	private DataSource ds;

	//private static final Logger log = LogManager.getLogger();
	private final List<Pattern> pattList = new ArrayList<>();

	// >, >=
	private final String gtePatt = "^\\s*(>=?)\\s*(\\d+)\\s*$"; 		// grounCount = 2

	// <, <=
	private final String ltePatt = "^\\s*(<=?)\\s*(\\d+)\\s*$"; 		// grounCount = 2

	// range
	private final String rangePatt = "^\\s*(\\d+)\\s*(:)\\s*(\\d+)\\s*$";   // groupCount = 3

	// date range, groupCount = 3
	private final String dateRangePatt = "^(\\d{4}\\-\\d{2}\\-\\d{2}T\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3}Z)" + "(:)"
		+ "(\\d{4}\\-\\d{2}\\-\\d{2}T\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3}Z)$";

	// number
	private final String numberPatt = "^\\s*(\\d+)\\s*$";   	// groupCount = 1

	// string
	private String stringPatt = "^\\s*(.+)\\s*$";

	public DbUtil() {
		this.pattList.add(Pattern.compile(numberPatt));
		this.pattList.add(Pattern.compile(gtePatt));
		this.pattList.add(Pattern.compile(ltePatt));
		this.pattList.add(Pattern.compile(rangePatt));
		this.pattList.add(Pattern.compile(dateRangePatt));
		this.pattList.add(Pattern.compile(stringPatt));
	}


	public List<Map<String, String>> getMetaData(String tableName) {
		List<Map<String, String>> list = new ArrayList<>();

		String sql = "select * from " + tableName + " where 0 = 1";
		Map<String, String> fieldClassMap = new HashMap<>();
		Map<String, String> camel2UnderScoreMap = new HashMap<>();

		try (Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);) {

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = rsmd.getColumnName(i).toLowerCase();
				fieldClassMap.put(columnName, rsmd.getColumnClassName(i));
				camel2UnderScoreMap.put(toCamel(columnName), columnName);
			}
		} catch (Exception e) {
			// log.error(e);
			e.printStackTrace();
		}

		list.add(fieldClassMap);
		list.add(camel2UnderScoreMap);

		return list;
	}

	/**
	 * Camel to underline. ex, deptId -> dept_id
	 *
	 * @param input
	 * @return
	 */
	public String camel2Underline(String input) {
		String replaceString = input.replaceAll("([A-Z]+)", "\\_$1").toLowerCase();
		return replaceString;
	}

	public String getTableName(Class entityClass) {
		return entityClass.getSimpleName().replaceAll("([a-z])([A-Z]+)", "$1_$2");
	}

	/**
	 * web search, ex xxx.com/dept;deptId=100;locationId=>=100 matrix to tripple
	 * for next phase, normal for build where clause.
	 *
	 * @param params	from web search form
	 * @return list of Triple
	 */
	public List<SqlCond> parseMatrix2SqlCondList(Map<String, List<String>> params) {
		List<SqlCond> sqlCondList = new ArrayList<>();

		params.forEach((column, list) -> {
			String value = list.get(0);
			for (Pattern p : pattList) {
				Matcher m = p.matcher(value);
				if (m.matches()) {
					switch (m.groupCount()) {
						case 1:
							sqlCondList.add(new SqlCond(column, "=", m.group(1)));
							break;
						case 2:
							sqlCondList.add(new SqlCond(column, m.group(1), m.group(2)));
							break;
						case 3:
							sqlCondList.add(new SqlCond(column, m.group(2), m.group(1) + ":" + m.group(3)));
							break;
						default:
							//log.error("DbUtil, pattern has lost fish.." + p.toString());
							System.out.println("DbUtil, pattern has lost fish..\" + p.toString()");
					} // switch

					//logger.info(p.toString());
					break;  // found, break for, next another property
				} else {
					//log.trace(v + "----------------no match " + p.toString());
				}

			} // for

		}); // forEach

		return sqlCondList;
	}

	private String toCamel(String input) {
		StringBuilder sb = new StringBuilder(input);
		for (int i = 0; i < sb.length(); i++) {
			if (sb.charAt(i) == '_') {
				sb.deleteCharAt(i);
				sb.replace(i, i + 1, String.valueOf(Character.toUpperCase(sb.charAt(i))));
			}
		}
		return sb.toString();
	}

} // class
