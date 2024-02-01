package ddlGenerator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.dataSource.DataSource;
import ddlGenerator.domain.DataExportableTableFile;

public class TablesServices {

	private final ColumnsServices columnsServices;
	private final DefaultColumnsServices defaultColumnsServices;
	
	private static final Connection conexao = DataSource.getConexao();
	private static final Integer TABLE_NAME = 1;
	private static final Integer SCRIPT = 2;
	private static final String QUERY_FIND_TABLES_NAMES = "CREATE TABLE #DADOS_TABELA(COLUMNTYPE VARCHAR(MAX),TABLENAME VARCHAR(MAX),COLUMNNAME VARCHAR(MAX), IS_PK int)\r\n"
			+ "CREATE TABLE #TEMP_CREATE_INSERT(COLUMNTYPE VARCHAR(MAX),TABLENAME VARCHAR(MAX),COLUMNNAME VARCHAR(MAX), IS_PK int)\r\n"
			+ "\r\n" + "INSERT INTO #DADOS_TABELA\r\n" + "SELECT UPPER(CONCAT(C.NAME\r\n" + "				,' '\r\n"
			+ "				,CASE \r\n"
			+ "					WHEN TY.NAME = 'VARCHAR' THEN 'VARCHAR('+IIF(CONVERT(VARCHAR,C.MAX_LENGTH) = '-1','MAX',CONVERT(VARCHAR,C.MAX_LENGTH))+')' \r\n"
			+ "					WHEN TY.NAME = 'NUMERIC'		  THEN 'NUMERIC('+CONVERT(VARCHAR,C.PRECISION)+','+CONVERT(VARCHAR,C.SCALE)+')'\r\n"
			+ "					ELSE TY.NAME\r\n" + "				END\r\n" + "				, ' NOT NULL'\r\n"
			+ ")) AS COLUMNTYPE,OBJECT_NAME(KC.PARENT_OBJECT_ID) AS TABLENAME\r\n"
			+ ", C.NAME COLUMNNAME, 1 as IS_PK\r\n" + "FROM  SYS.KEY_CONSTRAINTS KC\r\n"
			+ "INNER JOIN SYS.INDEX_COLUMNS IC ON KC.PARENT_OBJECT_ID = IC.OBJECT_ID AND KC.UNIQUE_INDEX_ID = IC.INDEX_ID\r\n"
			+ "INNER JOIN   SYS.COLUMNS C ON IC.OBJECT_ID = C.OBJECT_ID AND IC.COLUMN_ID = C.COLUMN_ID\r\n"
			+ "JOIN SYS.TYPES TY ON C.USER_TYPE_ID = TY.USER_TYPE_ID\r\n"
			+ "FULL JOIN SYS.OBJECTS O ON O.OBJECT_ID = C.DEFAULT_OBJECT_ID\r\n"
			+ "FULL JOIN SYS.DEFAULT_CONSTRAINTS DC ON DC.OBJECT_ID = O.OBJECT_ID\r\n"
			+ "WHERE KC.TYPE_DESC = 'PRIMARY_KEY_CONSTRAINT'\r\n" + "\r\n" + "union all\r\n" + "\r\n" + "SELECT \r\n"
			+ "\r\n" + "UPPER(CONCAT(C.NAME\r\n" + "				,' '\r\n" + "				,CASE \r\n"
			+ "					WHEN TY.NAME = 'VARCHAR' THEN 'VARCHAR('+IIF(CONVERT(VARCHAR,C.MAX_LENGTH) = '-1','MAX',CONVERT(VARCHAR,C.MAX_LENGTH))+')' \r\n"
			+ "					WHEN TY.NAME = 'NUMERIC'		  THEN 'NUMERIC('+CONVERT(VARCHAR,C.PRECISION)+','+CONVERT(VARCHAR,C.SCALE)+')'\r\n"
			+ "					ELSE TY.NAME\r\n" + "				END\r\n" + "				, ' NOT NULL'\r\n"
			+ ")) AS COLUMNTYPE,t.name AS TABLENAME\r\n" + ", C.NAME COLUMNNAME, 0 as IS_PK\r\n"
			+ "FROM SYS.TABLES T\r\n" + "INNER JOIN   SYS.COLUMNS C ON T.OBJECT_ID = C.OBJECT_ID\r\n"
			+ "JOIN SYS.TYPES TY ON C.USER_TYPE_ID = TY.USER_TYPE_ID\r\n"
			+ "FULL JOIN SYS.OBJECTS O ON O.OBJECT_ID = C.DEFAULT_OBJECT_ID\r\n"
			+ "FULL JOIN SYS.DEFAULT_CONSTRAINTS DC ON DC.OBJECT_ID = O.OBJECT_ID\r\n"
			+ "LEFT JOIN #DADOS_TABELA D ON T.NAME = D.TABLENAME\r\n"
			+ "WHERE D.TABLENAME IS NULL AND T.NAME IS NOT NULL AND C.column_id=1\r\n" + "\r\n" + "\r\n"
			+ "INSERT INTO #TEMP_CREATE_INSERT(TABLENAME,COLUMNNAME,COLUMNTYPE,IS_PK)\r\n" + "SELECT P.TABLENAME\r\n"
			+ "   , STUFF((SELECT ',' + C.COLUMNNAME \r\n" + "          FROM #DADOS_TABELA AS C\r\n"
			+ "          WHERE C.TABLENAME = P.TABLENAME\r\n" + "		  GROUP BY C.TABLENAME, C.COLUMNNAME\r\n"
			+ "          FOR XML PATH('')), 1, LEN(','), '') AS COLUMNNAME\r\n" + "		  ,\r\n"
			+ "		  STUFF((SELECT ',' + C.COLUMNTYPE \r\n" + "          FROM #DADOS_TABELA AS C\r\n"
			+ "          WHERE C.TABLENAME = P.TABLENAME\r\n" + "		  GROUP BY C.TABLENAME, C.COLUMNTYPE\r\n"
			+ "          FOR XML PATH('')), 1, LEN(','), '') AS COLUMNTYPE,\r\n" + "		  IS_PK\r\n"
			+ "FROM #DADOS_TABELA AS P\r\n" + "GROUP BY P.TABLENAME,P.COLUMNTYPE,IS_PK\r\n" + "\r\n"
			+ "DROP TABLE #DADOS_TABELA\r\n" + "\r\n" + "SELECT\r\n" + "TABLENAME\r\n"
			+ ",'IF NOT EXISTS (SELECT TOP 1 1 FROM SYS.OBJECTS WHERE OBJECT_ID = OBJECT_ID(N'''+TABLENAME+''') AND TYPE IN (N''U'')) BEGIN '+\r\n"
			+ "CONCAT('CREATE TABLE ',TABLENAME,' (',COLUMNTYPE,IIF(MAX(IS_PK)=1,' PRIMARY KEY(' + COLUMNNAME + '))',')'))\r\n"
			+ "+' END' AS SCRIPT\r\n" + "FROM #TEMP_CREATE_INSERT\r\n" + "GROUP BY TABLENAME,COLUMNNAME,COLUMNTYPE\r\n"
			+ "\r\n" + "DROP TABLE #TEMP_CREATE_INSERT\r\n" + "\r\n" + "\r\n" + "\r\n" + "";

	public TablesServices() {
		columnsServices = new ColumnsServices();
		defaultColumnsServices = new DefaultColumnsServices();
	}
	
	public List<IDataExportableFile> getTables() throws SQLException {
		List<IDataExportableFile> resultList = new ArrayList<IDataExportableFile>();
		PreparedStatement state = conexao.prepareStatement(QUERY_FIND_TABLES_NAMES);
		ResultSet result = state.executeQuery();
		while (result.next()) {
			
			List<IDataExportableFile> columnsAndDefaults = new ArrayList<>();
			String tableName = result.getString(TABLE_NAME);
			
			columnsAndDefaults.addAll(columnsServices.getColumns(tableName));
			columnsAndDefaults.addAll(defaultColumnsServices.getColumns(tableName));
			
			resultList.add(DataExportableTableFile.builder()
					.name(tableName)
					.scriptCreation(result.getString(SCRIPT))
					.subContent(columnsAndDefaults)
					.build()
					);
		}
		return resultList;
	}

}
