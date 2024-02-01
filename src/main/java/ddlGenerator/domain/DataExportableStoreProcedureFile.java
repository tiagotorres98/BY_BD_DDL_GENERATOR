package ddlGenerator.domain;

import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.abstracts.domain.IDataExportableStoreProcedureFile;
import ddlGenerator.abstracts.domain.IDataExportableTableFile;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataExportableStoreProcedureFile implements IDataExportableStoreProcedureFile{

	private String name;
	private String scriptCreation;
	private List<IDataExportableFile> subContent;
	
}
