import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.*;


public class Clean {
        public static void main(String[] args) {
            Connection conn = null;
            PreparedStatement pst = null;
            PreparedStatement pst1 = null;
            PreparedStatement pst2 = null;
            ResultSet rs = null;

            try{
                Class.forName("com.mysql.jdbc.Driver") ;
                String url = "jdbc:mysql://10.9.46.103:53306/video?useUnicode=true&characterEncoding=UTF8" ;
                String username = "root" ;
                String password = "root" ;
                conn = DriverManager.getConnection(url, username, password);
                String sql = "SELECT * from aggregate_generic_name where role = ?";
                String sql1 = "INSERT INTO site_relative_adjective (amount,role) values (?,?)";
                String sql2 = "DELETE from aggregate_generic_name where role = ?";
                pst = conn.prepareStatement(sql) ;
                pst1 = conn.prepareStatement(sql1) ;
                pst2 = conn.prepareStatement(sql2) ;
                File file = new File("E:\\mongo\\laji.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("gbk")));
                String s = null;
                while((s = br.readLine())!=null){
                    pst.setString(1,s);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        String role = rs.getString("role");
                        int amount = rs.getInt("amount");
                        pst1.setInt(1,amount);
                        pst1.setString(2,role);
                        pst1.executeUpdate();
                        pst2.setString(1,role);
                        pst2.executeUpdate();
                    }
                }
                br.close();
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                close(rs, pst, conn);
            }


        }

        private static void close(ResultSet rs, Statement st, Connection conn){
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally{
                    if(st != null){
                        try {
                            st.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally{
                            if(conn != null){
                                try {
                                    conn.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
}
