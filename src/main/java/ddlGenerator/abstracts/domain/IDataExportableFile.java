package ddlGenerator.abstracts.domain;

import java.util.List;

public interface IDataExportableFile {

	public String getName();
	
	public String getScriptCreation();
	
	public List<IDataExportableFile> getSubContent();
	
	public void setSubContent(List<IDataExportableFile> subContent);
	
}
