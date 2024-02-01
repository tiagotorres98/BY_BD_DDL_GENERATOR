package ddlGenerator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.dataSource.DataSource;
import ddlGenerator.domain.DataExportableFunctionsFile;

public class StoreProcedureServices {

	private static final Connection conexao = DataSource.getConexao();
	private static final Integer FUNC_NAME = 1;
	private static final Integer SCRIPT = 2;
	private static final String QUERY_FIND_STORE_PROCEDURES = "SELECT NAME,\r\n" + 
			"'IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'''+NAME+''') AND type in (N''P'', N''PC''))\r\n" + 
			"BEGIN\r\n" + 
			"	EXEC dbo.sp_executesql @statement = N''CREATE PROCEDURE '+NAME+' AS'' \r\n" + 
			"END\r\n" + 
			"GO \n ' + REPLACE(OBJECT_DEFINITION(OBJECT_ID),'CREATE PROCEDURE ','ALTER PROCEDURE ')\r\n" + 
			"FROM SYS.PROCEDURES";

	public List<IDataExportableFile> getStoreProcedures() throws SQLException {
		List<IDataExportableFile> resultList = new ArrayList<IDataExportableFile>();
		PreparedStatement state = conexao.prepareStatement(QUERY_FIND_STORE_PROCEDURES);
		ResultSet result = state.executeQuery();
		while (result.next()) {
			resultList.add(DataExportableFunctionsFile.builder().scriptCreation(result.getString(SCRIPT)).name(result.getString(FUNC_NAME))
					.build());
		}
		return resultList;
	}
}
