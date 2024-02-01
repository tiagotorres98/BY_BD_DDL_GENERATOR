package ddlGenerator.dataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

	private static DataSource singleton = null;
//	private static final String url = "jdbc:jtds:sqlserver://10.142.15.196:1435/FRT_POC_GR_VINCI";
//	private static final String user = "FERNANDO_RIELO";
//	private static final String password = "fernao@4321";
	
	private static final String url = "jdbc:jtds:sqlserver://localhost:1433/BACEN_3040_V3_DAYCOVAL_DEV";
	private static final String user = "sa";
	private static final String password = "fromtis!Q@W#E";
	
	private static Connection conexao = null;

	private DataSource() {

	}

	private static DataSource getInstance() throws SQLException {
		if (singleton == null) singleton = new DataSource();
		conexao = DriverManager.getConnection(url, user, password);
		return singleton;
	}

	public static Connection getConexao(){
		try {
			getInstance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conexao;
	}
}