package ddlGenerator.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.abstracts.services.IExportSqlFile;
import ddlGenerator.enums.ExportType;

public class ExportSqlFile {
	
	private final ExportSqlFileServiceFactory factory;
	
	public ExportSqlFile() throws SQLException {
		this.factory = new ExportSqlFileServiceFactory();
	}

	public void exportFile(List<IDataExportableFile> dataExportable, String path, ExportType type) throws IOException {
		IExportSqlFile service = factory.get(type);
		service.exportFile(dataExportable, path);
	}

}
