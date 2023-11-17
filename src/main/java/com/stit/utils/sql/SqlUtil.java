package com.stit.utils.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JDBC get sql on user's search by criteria, normal matrix variables from web
 * form. xx.xxx.com/myapp/api/dept;deptId>=199;locationId= <=1200. web thing,
 * spring web
 */
@Component
public class SqlUtil {

	//private final Logger log = LogManager.getLogger();

	@Autowired
	private DbUtil dbUtil;

	public SqlUtil() {
	}

	/**
	 * build sql statement
	 *
	 * @param tableName
	 * @param params	Matrixvariable
	 * @return sql
	 */
	public String toSql(Class entityClass, Map<String, List<String>> params, String... orderBy) {
		String tableName = dbUtil.getTableName(entityClass);
		//log.trace("tableName: " + tableName);

		StringBuilder condBuff = new StringBuilder();
		List<Map<String, String>> mdList = dbUtil.getMetaData(tableName);

		Map<String, String> colClassMap = mdList.get(0);
		Map<String, String> camelUnderScoreMap = mdList.get(1);

		List<String> condList = new ArrayList<>();

		List<SqlCond> sqlCondList = dbUtil.parseMatrix2SqlCondList(params);

		for (SqlCond sqlCond : sqlCondList) {
			String column = camelUnderScoreMap.get(sqlCond.column); // jobNo -> job_no

			//class javaType = xxx(column);
			String operation = sqlCond.operation;
			String value = sqlCond.value;

			switch (operation) {
				case "=":
					String javaType = colClassMap.get(column);
					// System.out.println("javaType: " + javaType);

					if (javaType.equals("java.lang.String")) {
						condBuff.setLength(0);
						if (value.contains("%")) {
							condBuff
								.append(column)
								.append(" like ").append("'").append(value.trim()).append("'");
						} else {
							condBuff.append(column).append(" ")
											.append(operation).append(" '").append(value).append("'");
						}
						condList.add(condBuff.toString());

					} else if (javaType.equals("java.sql.Timestamp")) {
						condBuff.setLength(0);
						condBuff.append("to_char(").append(column).append(", 'yyyy-mm-dd')")
										.append(operation).append(" '").append(value).append("'");
						condList.add(condBuff.toString());

					} else {
						condBuff.setLength(0);
						condBuff.append(column).append(" ").append(operation).append(" ").append(value);
						condList.add(condBuff.toString());
					}
					break;

				case "<":
				case "<=":
				case ">":
				case ">=":
					condBuff.setLength(0);
					condBuff.append(column).append(" ").append(operation).append(" ").append(value);
					condList.add(condBuff.toString());
					break;

				case ":":
					condBuff.setLength(0);
					String[] arr = value.split(":");

					condBuff.append(column)
						.append(" between ")
						.append(arr[0]).append(" and ").append(arr[1]);

					condList.add(condBuff.toString());
					break;

				default:
					// log.error("sqlUtil: 落網之魚... " + op);
					System.out.println("sqlUtil: 落網之魚... \" + op");

			} // switch	

		}

		String whereClause = "";
		if (!condList.isEmpty()) {
			whereClause = condList.stream().collect(Collectors.joining(" and ", " where ", ""));
		} else {
			whereClause = "";
		}

		String orderByClause = "";
		if (orderBy != null && orderBy.length > 0) {
			orderByClause = " order by " + String.join(", ", orderBy);
		}

		String sql = "select * from " + tableName + " " + whereClause + " " + orderByClause;

		return sql.trim();
	}

} // class

