package ddlGenerator.services;

import java.util.ArrayList;
import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.abstracts.services.ICreateExportableSqlFile;

public class CreateExportableSqlFileFactory {
	
	private final List<ICreateExportableSqlFile> list;
	
	public CreateExportableSqlFileFactory() {
		list = new ArrayList<>();
		list.add(new CreateTableDDL());
		list.add(new CreateFKDDL());
		list.add(new CreateStoreProcedureDDL());
		list.add(new CreateFunctionsDDL());
	}
	
	public List<ICreateExportableSqlFile> get() {
		// TODO Auto-generated method stub
		return list;
	}
	
	public ICreateExportableSqlFile get(IDataExportableFile dataExportable) {
		// TODO Auto-generated method stub
		return list.stream().filter(f-> f.apply(dataExportable)).findFirst().get();
	}

}
