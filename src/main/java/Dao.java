import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/12.
 */
public class Dao {
    public static  void  main(String args[]){
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
            String sql = "INSERT into url_role (url,role,sort,fromfile) VALUES (?,?,?,?)";
            pst = conn.prepareStatement(sql) ;
            File file = new File("E:\\mongo\\laji.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("gbk")));
            List list = new ArrayList();
            String s = null;
            while((s = br.readLine())!=null){
                list.add(s);
                /*String[] str = s.split("\t");
                pst.setString(1,str[0]);
                pst.setString(2,str[1]);
                if(str[0].equals()){

                }
                pst.setInt(3,);
                pst.setInt(4,0);
                pst.executeUpdate();*/
            }
            for(int i=0;i<list.size();i++){
                String s1 = (String) list.get(i);
                String s2 = (String) list.get(i+1);
                String[] str1 = s1.split("\t");
                String[] str2 = s2.split("\t");
                if(str1.equals(str2)){


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
