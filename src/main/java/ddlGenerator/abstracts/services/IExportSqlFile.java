package ddlGenerator.abstracts.services;

import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.enums.ExportType;

public interface IExportSqlFile {
	
	public default String breakLine() {
		return "\n";
	}

	public default String sqlComment() {
		return " -- ";
	}

	public default String sqlExtention() {
		return ".sql";
	}

	public default Integer howMuchNumbersInCounters() {
		return 4;
	}

	public void exportFile(List<IDataExportableFile> dataExportable, String path);

	public ExportType getType();

}
