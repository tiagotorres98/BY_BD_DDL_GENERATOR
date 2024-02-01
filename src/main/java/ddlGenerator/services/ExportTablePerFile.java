package ddlGenerator.services;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.abstracts.services.ICreateExportableSqlFile;
import ddlGenerator.abstracts.services.IExportSqlFile;
import ddlGenerator.domain.FileToExport;
import ddlGenerator.enums.ExportType;

public class ExportTablePerFile implements IExportSqlFile {
	
	private final CreateExportableSqlFileFactory factory;
	
	public ExportTablePerFile() {
		factory = new CreateExportableSqlFileFactory();
	}

	@Override
	public void exportFile(List<IDataExportableFile> dataExportable, String path) {
		AtomicInteger counter = new AtomicInteger(1);
		
		final List<FileToExport> files = new ArrayList<>();
				
		try {
			for (ICreateExportableSqlFile createExportable: factory.get()) {
				createExportable.create(dataExportable,files);
			}
			
			for (FileToExport file : files) {
				FileWriter writer = new FileWriter(new File(setFileName(counter, path,file.getName())));
				writer.write(file.getScript());
				writer.flush();
				writer.close();
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private String setFileName(AtomicInteger counter,String path,String tableName) {
		String result = new String(path).concat("//")
					.concat("R__")
					.concat(StringUtils.repeat("0", howMuchNumbersInCounters() - counter.toString().length()))
					.concat(Integer.toString(counter.getAndIncrement()))
					.concat("_")
					.concat(tableName)
					.concat(sqlExtention());
		return result;
	}

	@Override
	public ExportType getType() {
		// TODO Auto-generated method stub
		return ExportType.TABLE_PER_FILE;
	}

}
