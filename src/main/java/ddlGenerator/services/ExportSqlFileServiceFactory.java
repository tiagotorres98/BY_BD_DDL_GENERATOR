package ddlGenerator.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ddlGenerator.abstracts.services.IExportSqlFile;
import ddlGenerator.enums.ExportType;

public class ExportSqlFileServiceFactory {

	private List<IExportSqlFile> listServices;

	public ExportSqlFileServiceFactory() throws SQLException {
		listServices = new ArrayList<>();
		listServices.add(new ExportUniqueFile());
		listServices.add(new ExportTablePerFile());
	}

	public IExportSqlFile get(ExportType type) {
		return listServices.stream().filter(f -> f.getType().equals(type)).findFirst().get();
	}

}
