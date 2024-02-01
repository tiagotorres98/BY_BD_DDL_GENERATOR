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

public class ColumnsServices {

	private static final Connection conexao = DataSource.getConexao();
	private static final Integer TABLE_NAME = 1;
	private static final Integer SCRIPT = 2;
	private static final String QUERY_FIND_COLUMNS_NAMES = "SELECT \r\n"
			+ "	'IF NOT EXISTS (SELECT * FROM SYS.COLUMNS C JOIN SYS.TABLES T ON C.OBJECT_ID = T.OBJECT_ID WHERE T.NAME = '\r\n"
			+ "	+''''+T.NAME+''''+' AND C.NAME = '+''''+C.NAME+''') '\r\n" + "	+'BEGIN '\r\n"
			+ "	+ 'ALTER TABLE '+T.NAME+' ADD '+ C.NAME + ' '\r\n" + "	+ CASE \r\n"
			+ "		WHEN TY.NAME = 'VARCHAR' THEN 'VARCHAR('+IIF(CONVERT(VARCHAR,C.MAX_LENGTH) = '-1','MAX',CONVERT(VARCHAR,C.MAX_LENGTH))+')' \r\n"
			+ "		WHEN TY.NAME = 'IMAGE'			  THEN 'IMAGE'\r\n"
			+ "		WHEN TY.NAME = 'TEXT'			  THEN 'TEXT'\r\n"
			+ "		WHEN TY.NAME = 'UNIQUEIDENTIFIER' THEN 'UNIQUEIDENTIFIER'\r\n"
			+ "		WHEN TY.NAME = 'DATE'			  THEN 'DATE'\r\n"
			+ "		WHEN TY.NAME = 'TIME'			  THEN 'TIME'\r\n"
			+ "		WHEN TY.NAME = 'DATETIME2'		  THEN 'DATETIME2'\r\n"
			+ "		WHEN TY.NAME = 'DATETIMEOFFSET'	  THEN 'DATETIMEOFFSET'\r\n"
			+ "		WHEN TY.NAME = 'TINYINT'		  THEN 'TINYINT'\r\n"
			+ "		WHEN TY.NAME = 'SMALLINT'		  THEN 'SMALLINT'\r\n"
			+ "		WHEN TY.NAME = 'INT'			  THEN 'INT'\r\n"
			+ "		WHEN TY.NAME = 'SMALLDATETIME'	  THEN 'SMALLDATETIME'\r\n"
			+ "		WHEN TY.NAME = 'REAL'			  THEN 'REAL'\r\n"
			+ "		WHEN TY.NAME = 'MONEY'			  THEN 'MONEY'\r\n"
			+ "		WHEN TY.NAME = 'DATETIME'		  THEN 'DATETIME'\r\n"
			+ "		WHEN TY.NAME = 'FLOAT'			  THEN 'FLOAT'\r\n"
			+ "		WHEN TY.NAME = 'SQL_VARIANT'	  THEN 'SQL_VARIANT'\r\n"
			+ "		WHEN TY.NAME = 'NTEXT'			  THEN 'NTEXT'\r\n"
			+ "		WHEN TY.NAME = 'BIT'			  THEN 'BIT'\r\n"
			+ "		WHEN TY.NAME = 'DECIMAL'		  THEN 'DECIMAL'\r\n"
			+ "		WHEN TY.NAME = 'NUMERIC'		  THEN 'NUMERIC('+CONVERT(VARCHAR,C.PRECISION)+','+CONVERT(VARCHAR,C.SCALE)+')'\r\n"
			+ "		WHEN TY.NAME = 'SMALLMONEY'		  THEN 'SMALLMONEY'\r\n"
			+ "		WHEN TY.NAME = 'BIGINT'			  THEN 'BIGINT'\r\n"
			+ "		WHEN TY.NAME = 'HIERARCHYID'	  THEN 'HIERARCHYID'\r\n"
			+ "		WHEN TY.NAME = 'GEOMETRY'		  THEN 'GEOMETRY'\r\n"
			+ "		WHEN TY.NAME = 'GEOGRAPHY'		  THEN 'GEOGRAPHY'\r\n"
			+ "		WHEN TY.NAME = 'VARBINARY'		  THEN 'VARBINARY'\r\n"
			+ "		WHEN TY.NAME = 'BINARY'			  THEN 'BINARY'\r\n"
			+ "		WHEN TY.NAME = 'CHAR'			  THEN 'CHAR'\r\n"
			+ "		WHEN TY.NAME = 'TIMESTAMP'		  THEN 'TIMESTAMP'\r\n"
			+ "		WHEN TY.NAME = 'NVARCHAR'		  THEN 'NVARCHAR'\r\n"
			+ "		WHEN TY.NAME = 'NCHAR'			  THEN 'NCHAR'\r\n"
			+ "		WHEN TY.NAME = 'XML'			  THEN 'XML'\r\n"
			+ "		WHEN TY.NAME = 'SYSNAME'		  THEN 'SYSNAME'\r\n" + "	  END\r\n" + "	+ CASE\r\n"
			+ "		WHEN C.is_nullable = 0 AND C.default_object_id > 0 THEN ' NOT NULL DEFAULT '+DC.definition\r\n"
			+ "		ELSE ''\r\n" + "	  END\r\n" + "	+' END'\r\n" + "	,t.name\r\n" + "FROM SYS.COLUMNS C\r\n"
			+ "JOIN SYS.TABLES T ON C.OBJECT_ID = T.OBJECT_ID\r\n"
			+ "JOIN SYS.TYPES TY ON C.USER_TYPE_ID = TY.USER_TYPE_ID\r\n"
			+ "FULL JOIN sys.objects O ON O.object_id = C.default_object_id\r\n"
			+ "FULL JOIN sys.default_constraints DC ON DC.object_id = O.object_id\r\n";

	public List<IDataExportableFile> getColumns(String tableName) throws SQLException {
		List<IDataExportableFile> resultList = new ArrayList<IDataExportableFile>();
		PreparedStatement state = conexao.prepareStatement(QUERY_FIND_COLUMNS_NAMES + "WHERE T.NAME = '"+tableName+"' ORDER BY T.NAME");
		ResultSet result = state.executeQuery();
		while (result.next()) {
			resultList.add(DataExportableTableFile.builder().name(result.getString(SCRIPT)).scriptCreation(result.getString(TABLE_NAME))
					.build());
		}
		return resultList;
	}

}
