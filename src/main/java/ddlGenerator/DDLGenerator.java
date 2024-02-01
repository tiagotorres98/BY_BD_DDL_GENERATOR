package ddlGenerator;

import java.io.IOException;
import java.sql.SQLException;

import ddlGenerator.services.CreateDDLByBD;

public class DDLGenerator {

	public static void main(String[] args) throws SQLException, IOException {
		System.setProperty("file.encoding", "UTF-8");
		new CreateDDLByBD();
	}
}