package walid.jahin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class OracleConnectionTest {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:oracle:thin:@myprojectdb_medium?TNS_ADMIN=C:/Users/User/Documents/김지형/대학교/SE-LAB/이집트화가개인홈페이지/oracle_db/Wallet_myprojectdb";

        Properties props = new Properties();
        props.put("user", "YOUR_DB_USER"); // ✅ 예: ADMIN
        props.put("password", "YOUR_DB_PASSWORD"); // ✅ 예: DB 생성할 때 입력한 비밀번호
        props.put("oracle.net.ssl_server_dn_match", "true");

        Connection conn = DriverManager.getConnection(url, props);
        System.out.println("✅ Connected successfully!");
        conn.close();
    }
}
