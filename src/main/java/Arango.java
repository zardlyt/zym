import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.util.MapBuilder;
import com.qinxi.utils.ArangodbUtil;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;

import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2017/10/11.
 */
public class Arango {
    public static void main(String[] args){
        try {
            //File file = new File("E:\\mongo\\exist.txt");
            //BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("gbk")));
            InputStream inp = new FileInputStream("E:\\mongo\\dao.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(inp);
            XSSFSheet sheet = workbook.getSheetAt(1);
            XSSFRow row = null;
            XSSFCell cell = null;

            String queryCmmd = "for doc in entity filter @name in doc.formatNames and doc.label == 'figure' " +
                    " and ('actor' in doc.profession or 'director' in doc.profession) " +
                    " return {'_key':doc._key,'name':doc.name, 'formatNames':doc.formatNames}";
            ArangoDatabase db = ArangodbUtil.getDbDouban();
            Map<String, String> nameMap = new HashMap<String, String>();
            int rowNum = sheet.getLastRowNum();
            final String removeRegEx = "(\\(.*?\\))|(（.*?）)|(\\[.*?\\])|(【.*?】)|([(\\p{P}|\\pS|\\pZ)])";
            for(int i = 1;i<=rowNum;i++){
                //if (i % 1000 == 0) {
                    System.out.println(i);
                //}
                row = sheet.getRow(i);
                cell = row.getCell(24);
                String name = row.getCell(0).getStringCellValue();
                name = name.replaceAll(removeRegEx, "").toUpperCase().trim();

                if(cell.getStringCellValue().equals("FAIL")) {
                    ArangoCursor<Document> cursor = db.query(queryCmmd, new MapBuilder()
                            .put("name", name).get(), null, Document.class);
                    while (cursor.hasNext()) {
                        Document doc = cursor.next();
                        String _key = doc.getString("_key");
                        String figureName = doc.getString("name");
                        StringBuilder nameStr = new StringBuilder();
                        nameStr.append(figureName);
                        Collection<String> formatNames = (Collection<String>) doc.get("formatNames");
                        for (String formatName : formatNames) {
                            nameStr.append("\t" + formatName);
                        }
                        nameMap.put(_key, nameStr.toString());
                    }
                }
            }
            FileUtils.writeLines(new File("E:\\mongo\\exist.txt"), nameMap.values());
            ArangodbUtil.shutDownDouban();
            inp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
