package ddlGenerator.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.enums.ExportType;

public class CreateDDLByBD {

	private final TablesServices tablesServices;
	private final ForeignKeysServices foreignKeysServices;
	private final StoreProcedureServices storeProcedureServices;
	private final FunctionsServices functionsServices;
	private final ExportSqlFile exportSqlFile;

	public CreateDDLByBD() throws SQLException, IOException {
		tablesServices = new TablesServices();
		exportSqlFile = new ExportSqlFile();
		foreignKeysServices = new ForeignKeysServices();
		functionsServices = new FunctionsServices();
		storeProcedureServices = new StoreProcedureServices();
		create();
	}

	private void create() throws SQLException, IOException {
		List<IDataExportableFile> dataExportable = new ArrayList<>();
		dataExportable.addAll(tablesServices.getTables());
		dataExportable.addAll(foreignKeysServices.getForeignKeys());
		dataExportable.addAll(storeProcedureServices.getStoreProcedures());
		dataExportable.addAll(functionsServices.getFunctions());
		exportSqlFile.exportFile(dataExportable, "C:\\RELATORIOS_MANAGER", ExportType.UNIQUE_FILE);
	}
}
