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
public class Test {
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
            File file = new File("E:\\mongo\\urlLtRole.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("gbk")));
            List list = new ArrayList();
            String s = null;
            while((s = br.readLine())!=null){
                list.add(s);
            }
            List<String> list1 = go(list);
            for(String item : list1){
                String[] tmp = item.split("\t");
                pst.setString(1,tmp[0]);
                pst.setString(2,tmp[1]);
                pst.setInt(3, Integer.parseInt(tmp[2]));
                pst.setInt(4, Integer.parseInt(tmp[3]));
                pst.executeUpdate();
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(rs, pst, conn);
        }
    }

   /* https://movie.douban.com/subject/10001418/	福部里志
    https://movie.douban.com/subject/10001427/	秦淮
    https://movie.douban.com/subject/10001427/	马湘兰
    https://movie.douban.com/subject/10001427/	寇白门
    https://movie.douban.com/subject/10001427/	马湘
    https://movie.douban.com/subject/10001455/	武风
    https://movie.douban.com/subject/10001456/	聂政*/

    private static List<String> go(List<String> list ){
        List<String> stores = new ArrayList<String>();
        String lastUrl = "";
        int sort = 1;
        for(String item:list){
            String[] tmp = item.split("\t");
            if(tmp.length ==2){
                if(tmp[0].equals(lastUrl)){
                    sort++;
                    String store = tmp[0] + "\t"  +tmp[1]+ "\t" + sort + "\t" + "1";
                    stores.add(store);
                }else{
                    lastUrl = tmp[0];
                    sort = 1;
                    String store = tmp[0] + "\t"  +tmp[1]+ "\t" + sort + "\t" + "1";
                    stores.add(store);
                }
            }
        }
        return stores;
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
