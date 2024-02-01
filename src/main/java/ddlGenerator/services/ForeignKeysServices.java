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

public class ForeignKeysServices {

	private static final Connection conexao = DataSource.getConexao();
	private static final Integer TABLE_NAME = 1;
	private static final Integer SCRIPT = 2;
	private static final String QUERY_FIND_COLUMNS_NAMES = "SELECT\r\n" + 
			"'IF NOT EXISTS '\r\n" + 
			"+'(SELECT TOP 1 1 '\r\n" + 
			"+'FROM SYS.FOREIGN_KEYS WHERE NAME = '+''''+FK_TABLE.name+''''+')'\r\n" + 
			"+'BEGIN ' \r\n" + 
			"+'ALTER TABLE '+FK_MAIN_TABLE.name+' WITH CHECK ADD  CONSTRAINT '+FK_TABLE.NAME+' FOREIGN KEY('+FK_MAIN_TABLE_COLUMN.name+') REFERENCES  '+FK_REFERENCE_TABLE.NAME+' ('+FK_REFERENCE_TABLE_COLUMN.name+')'\r\n" + 
			"+' END', FK_MAIN_TABLE.name\r\n" + 
			"FROM sys.foreign_keys FK_TABLE\r\n" + 
			"JOIN sys.tables FK_MAIN_TABLE ON FK_MAIN_TABLE.object_id = FK_TABLE.parent_object_id\r\n" + 
			"JOIN sys.tables FK_REFERENCE_TABLE ON FK_REFERENCE_TABLE.object_id = FK_TABLE.referenced_object_id\r\n" + 
			"JOIN sys.foreign_key_columns FK_COLUMNS on FK_COLUMNS.constraint_object_id = FK_TABLE.object_id\r\n" + 
			"JOIN sys.columns FK_MAIN_TABLE_COLUMN on (FK_MAIN_TABLE_COLUMN.object_id = FK_COLUMNS.parent_object_id and FK_MAIN_TABLE_COLUMN.column_id = FK_COLUMNS.parent_column_id)\r\n" + 
			"JOIN sys.columns FK_REFERENCE_TABLE_COLUMN on (FK_REFERENCE_TABLE_COLUMN.object_id = FK_COLUMNS.referenced_object_id and FK_REFERENCE_TABLE_COLUMN.column_id = FK_COLUMNS.referenced_column_id)\r\n";

	public List<IDataExportableFile> getForeignKeys() throws SQLException {
		List<IDataExportableFile> resultList = new ArrayList<IDataExportableFile>();
		PreparedStatement state = conexao.prepareStatement(QUERY_FIND_COLUMNS_NAMES);
		ResultSet result = state.executeQuery();
		while (result.next()) {
			resultList.add(DataExportableForeignKeyFile.builder().name(result.getString(SCRIPT)).scriptCreation(result.getString(TABLE_NAME))
					.build());
		}
		return resultList;
	}
}
