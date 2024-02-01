package ddlGenerator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.dataSource.DataSource;
import ddlGenerator.domain.DataExportableForeignKeyFile;
import ddlGenerator.domain.DataExportableStoreProcedureFile;

public class FunctionsServices {

	private static final Connection conexao = DataSource.getConexao();
	private static final Integer PROC_NAME = 1;
	private static final Integer SCRIPT = 2;
	private static final String QUERY_FIND_FUNCTIONS = "SELECT \r\n" + 
			"    name AS name,\r\n" + 
			"	'IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N''[dbo].['+name+']'') AND type in (N''FN'', N''IF'', N''TF'', N''FS'', N''FT''))\r\n" + 
			"BEGIN\r\n" + 
			"	execute dbo.sp_executesql @statement = N''CREATE FUNCTION [dbo].'+name+'() '+IIF(TYPE = 'FN','RETURNS INT AS BEGIN RETURN 0 ','RETURNS @tabela TABLE (ID INT) AS BEGIN RETURN ')+'END''\r\n" + 
			"END\r\n" + 
			"GO \r\n" + 
			"' + REPLACE(LTRIM(OBJECT_DEFINITION(object_id)),'CREATE FUNCTION ','ALTER FUNCTION ')\r\n" + 
			"FROM \r\n" + 
			"    sys.objects\r\n" + 
			"WHERE \r\n" + 
			"    type IN ('FN','TF')\r\n" + 
			"";

	public List<IDataExportableFile> getFunctions() throws SQLException {
		List<IDataExportableFile> resultList = new ArrayList<IDataExportableFile>();
		PreparedStatement state = conexao.prepareStatement(QUERY_FIND_FUNCTIONS);
		ResultSet result = state.executeQuery();
		while (result.next()) {
			resultList.add(DataExportableStoreProcedureFile.builder().name(result.getString(PROC_NAME)).scriptCreation(result.getString(SCRIPT))
					.build());
		}
		return resultList;
	}
}
