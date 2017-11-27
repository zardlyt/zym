import java.io.*;
import java.nio.charset.Charset;

import com.mongodb.client.MongoCursor;
import com.qinxi.utils.MongoDBUtil;
import org.apache.poi.xssf.usermodel.*;
import org.bson.Document;

public class ExcelUtils {
    public static void main(String[] args){
        try {
            File file = new File("E:\\mongo\\exist.txt");
            File file1 = new File("E:\\mongo\\noexist.txt");
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("gbk")));
            BufferedWriter wr1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1),Charset.forName("gbk")));
            InputStream inp = new FileInputStream("E:\\mongo\\dao.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(inp);
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = null;
            XSSFCell cell = null;
            int rowNum = sheet.getLastRowNum();
            for(int i = 1;i<=rowNum;i++){
                System.out.println("第"+i+"行");
                row = sheet.getRow(i);
                cell = row.getCell(24);
                String name = row.getCell(0).getStringCellValue();
                String yuju = row.getCell(2).getStringCellValue();
                if(cell.getStringCellValue().equals("FAIL")){
                    Document document1 = new Document();
                    document1.put("moreChineseName",name);
                    MongoCursor<Document> iter1 = MongoDBUtil.getCollection("MovieKnowledgeMap", "clean_douban_celebrity_basic_info").find(document1).iterator();
                    if(iter1.hasNext()){
                        wr.write(name+"\t"+yuju);
                        wr.newLine();
                    }else {
                        Document document2 = new Document();
                        document2.put("name",name);
                        MongoCursor<Document> iter2 = MongoDBUtil.getCollection("merge_mtime", "mtime_works").find(document2).iterator();
                        if(iter2.hasNext()){
                            wr.write(name+"\t"+yuju);
                            wr.newLine();
                        }else {
                            Document document3 = new Document();
                            document3.put("name",name);
                            MongoCursor<Document> iter3 = MongoDBUtil.getCollection("kb_basic_info", "douban_baike_star_basic_info").find(document3).iterator();
                            if (iter3.hasNext()){
                                wr.write(name+"\t"+yuju);
                                wr.newLine();
                            }else {
                                Document document4 = new Document();
                                document4.put("name",name);
                                MongoCursor<Document> iter4 = MongoDBUtil.getCollection("kb_basic_info", "mtime_baike_star_basic_info").find(document4).iterator();
                                if (iter4.hasNext()){
                                    wr.write(name+"\t"+yuju);
                                    wr.newLine();
                                }else {
                                    wr1.write(name+"\t"+yuju);
                                    wr1.newLine();
                                }
                            }
                        }
                    }
                }
            }
            wr.close();
            wr1.close();
            inp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}  