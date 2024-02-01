package ddlGenerator.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.abstracts.domain.IDataExportableForeignKeyFile;
import ddlGenerator.abstracts.services.ICreateExportableSqlFile;
import ddlGenerator.domain.FileToExport;

public class CreateFKDDL implements ICreateExportableSqlFile {

	private final static String FILE_NAME = "FOREING_KEYS_DATABASE";

	@Override
	public void create(List<IDataExportableFile> dataExportable, List<FileToExport> fileToExport) throws IOException {
		List<IDataExportableFile> toRemove = new ArrayList<>();
		final StringBuilder fkScripts = new StringBuilder();

		dataExportable.stream().filter(f -> apply(f)).forEach(d -> {
			fkScripts.append(d.getScriptCreation()).append(breakLine());
			toRemove.add(d);
		});

		FileToExport file = fileToExport.stream()
				.filter(f -> f.getName().equals(FILE_NAME))
				.findFirst()
				.orElse(new FileToExport(FILE_NAME, ""));

		file.setScript(file.getScript().concat(breakLine()).concat(fkScripts.toString()));

		fileToExport.add(file);

		dataExportable.removeAll(toRemove);
	}

	@Override
	public Boolean apply(IDataExportableFile apply) {
		// TODO Auto-generated method stub
		return apply instanceof IDataExportableForeignKeyFile;
	}

}
