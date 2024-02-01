package ddlGenerator.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.abstracts.domain.IDataExportableStoreProcedureFile;
import ddlGenerator.abstracts.domain.IDataExportableTableFile;
import ddlGenerator.abstracts.services.ICreateExportableSqlFile;
import ddlGenerator.domain.FileToExport;

public class CreateStoreProcedureDDL implements ICreateExportableSqlFile {

	@Override
	public void create(List<IDataExportableFile> dataExportable, List<FileToExport> fileToExport) throws IOException {

		List<IDataExportableFile> toRemove = new ArrayList<>();

		dataExportable.stream().filter(f -> apply(f)).forEach(d -> {
			final StringBuilder builder = new StringBuilder();
			setStoreProcedureCreation(builder,d);
			fileToExport.add(new FileToExport(d.getName(), builder.toString().concat(breakLine())));
			toRemove.add(d);
		});

		dataExportable.removeAll(toRemove);
	}
	
	private void setStoreProcedureCreation(StringBuilder builder, IDataExportableFile data) {
		builder.append(data.getScriptCreation()).append(breakLine());
	}
	
	
	@Override
	public Boolean apply(IDataExportableFile apply) {
		// TODO Auto-generated method stub
		return apply instanceof IDataExportableStoreProcedureFile;
	}

}
