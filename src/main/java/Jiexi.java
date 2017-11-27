import com.mongodb.client.MongoCursor;
import com.qinxi.utils.MongoDBUtil;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/9.
 */
public class Jiexi {
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
            String sql = "SELECT * FROM douban_role_statistics_group_count_role_lt_max_movie_count ORDER BY entityUrl,current_Count DESC,movie_Count ASC";
            pst = conn.prepareStatement(sql) ;
            Map<String,String> map = new HashMap<String,String>();
            Map<String,String> map1 = new HashMap<String,String>();
            File file = new File("E:\\mongo\\url.txt");
            File file1 = new File("E:\\mongo\\urlLtRole.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("gbk")));
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1),Charset.forName("gbk")));
            String s = null;
            while((s = br.readLine())!=null) {
                String[] str = s.split("\t");
                String url2 = null;
                String url3 = null;
                String summar = null;
                Document document1 = new Document();
                document1.put("_id", new ObjectId(str[0]));
                MongoCursor<Document> iter1 = MongoDBUtil.getCollection("MovieKnowledgeMap", "clean_douban_movie_basic_info").find(document1).iterator();
                while (iter1.hasNext()) {
                    Document docu = iter1.next();
                    url3 = docu.getString("entityUrl");
                    summar = docu.getString("summariness");
                }
                if(str.length!=1){
                    Document document = new Document();
                    document.put("_id", new ObjectId(str[1]));
                    MongoCursor<Document> iter = MongoDBUtil.getCollection("MovieKnowledgeMap", "clean_douban_movie_basic_info").find(document).iterator();
                    while(iter.hasNext()){
                        url2 = iter.next().getString("entityUrl");
                    }
                    map.put(url3, url2);
                    map1.put(url3, summar);
                }else {
                    map.put(url3,url3);
                    map1.put(url3,summar);
                }
            }
            System.out.print("加载完成");
            br.close();
            rs = pst.executeQuery();
            while(rs.next()){
                String url1 = rs.getString("entityUrl");
                String role = rs.getString("role");
                for(Map.Entry entry:map.entrySet()){
                    if(url1.equals(entry.getValue())){
                        String url2 = (String) entry.getKey();
                        String summ = map1.get(url2);
                        if(summ.contains(role)){
                            wr.write(url2+"\t"+role);
                            wr.newLine();
                        }
                    }
                }
            }
            wr.close();
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
