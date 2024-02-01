package ddlGenerator.services;

import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.abstracts.services.ICreateExportableSqlFile;
import ddlGenerator.abstracts.services.IExportSqlFile;
import ddlGenerator.dataSource.DataSource;
import ddlGenerator.domain.FileToExport;
import ddlGenerator.enums.ExportType;

public class ExportUniqueFile implements IExportSqlFile {

	private final String DATABASE_NAME;

	private final CreateExportableSqlFileFactory factory;

	public ExportUniqueFile() throws SQLException {
		factory = new CreateExportableSqlFileFactory();
		DATABASE_NAME = DataSource.getConexao().getCatalog();
	}

	@Override
	public void exportFile(List<IDataExportableFile> dataExportable, String path) {
		StringBuilder builder = new StringBuilder();
		final List<FileToExport> files = new ArrayList<>();

		try {
			for (ICreateExportableSqlFile createExportable : factory.get()) {
				createExportable.create(dataExportable, files);
			}

			for (FileToExport file : files) {
				builder.append(file.getScript()).append(breakLine()).append("GO").append(breakLine()).append(breakLine());
			}

			FileWriter writer = new FileWriter(new File(setFileName(path)));
			writer.write(builder.toString());
			writer.flush();
			writer.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private String setFileName(String path) {
		String result = new String(path).concat("//")
					.concat(DATABASE_NAME)
					.concat(sqlExtention());
		return result;
	}

	@Override
	public ExportType getType() {
		// TODO Auto-generated method stub
		return ExportType.UNIQUE_FILE;
	}

}
