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

public class DefaultColumnsServices {

	private static final Connection conexao = DataSource.getConexao();
	private static final Integer TABLE_NAME = 1;
	private static final Integer SCRIPT = 2;
	private static final String QUERY_FIND_COLUMNS_NAMES = "SELECT\r\n" + 
			"'IF NOT EXISTS '\r\n" + 
			"+'(SELECT * '\r\n" + 
			"+'FROM SYS.COLUMNS C '\r\n" + 
			"+'JOIN SYS.TABLES T ON C.OBJECT_ID = T.OBJECT_ID '\r\n" + 
			"+'INNER JOIN sys.objects O ON O.object_id = C.default_object_id '\r\n" + 
			"+'WHERE T.name = '+''''+T.name+''''+' AND C.name = '+''''+C.name+''''+') '\r\n" + 
			"+'BEGIN ' \r\n" + 
			"+'ALTER TABLE '+T.name+' ADD  DEFAULT '+DC.definition+' FOR '+C.name\r\n" + 
			"+' END', t.name\r\n" + 
			"FROM SYS.COLUMNS C\r\n" + 
			"JOIN SYS.TABLES T ON C.OBJECT_ID = T.OBJECT_ID\r\n" + 
			"JOIN SYS.TYPES TY ON C.USER_TYPE_ID = TY.USER_TYPE_ID\r\n" + 
			"INNER JOIN sys.objects O ON O.object_id = C.default_object_id\r\n" + 
			"INNER JOIN sys.default_constraints DC ON DC.object_id = O.object_id\r\n";

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
