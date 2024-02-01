package ddlGenerator.abstracts.services;

import java.io.IOException;
import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.domain.FileToExport;

public interface ICreateExportableSqlFile {

	public default String breakLine() {
		return "\n";
	}
	
	public default String sqlComment() {
		return " -- ";
	}

	public default String sqlExtention() {
		return ".sql";
	}

	public void create(List<IDataExportableFile> dataExportable, List<FileToExport> fileToExport) throws IOException;
	
	public Boolean apply(IDataExportableFile apply);
}
