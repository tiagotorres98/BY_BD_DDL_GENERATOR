package ddlGenerator.domain;

import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.abstracts.domain.IDataExportableFunctionsFile;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataExportableFunctionsFile implements IDataExportableFunctionsFile{

	private String name;
	private String scriptCreation;
	private List<IDataExportableFile> subContent;
	
}
