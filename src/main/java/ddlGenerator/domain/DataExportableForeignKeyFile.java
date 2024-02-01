package ddlGenerator.domain;

import java.util.List;

import ddlGenerator.abstracts.domain.IDataExportableFile;
import ddlGenerator.abstracts.domain.IDataExportableForeignKeyFile;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataExportableForeignKeyFile implements IDataExportableForeignKeyFile {

	private String name;
	private String scriptCreation;
	private List<IDataExportableFile> subContent;

}
