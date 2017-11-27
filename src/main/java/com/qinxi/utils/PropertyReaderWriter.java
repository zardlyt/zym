package com.qinxi.utils; /**
 * Created by tangjuan on 2017/6/12.
 */



import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;


public class PropertyReaderWriter {
    private static Map<String, String> propertiesMap = new HashMap<String, String>();
    private static Properties collnameProps = new Properties();
    private static String collnameFilePath = "./graph/src/main/resources/collname.properties";
    private static String weightFilePath = "./graph/src/main/resources/weight_of_properties.properties";
    private static Properties weightProps = new Properties();

    static {
        try {
            String[] resourceFile = new String[]{"/arangodb.properties"};
            for (int i = 0; i < resourceFile.length; i++) {
//                Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource(resourceFile[i]));
                Class<Object> clazz = Object.class;
                InputStream in = clazz.getResourceAsStream(resourceFile[i]);
                Properties props = new Properties();
                props.load(in);
                Map<String, String> tempMap = new HashMap<String,String>();
                for (Entry<Object, Object> entry : props.entrySet()) {
                    propertiesMap.put(entry.getKey().toString(), entry.getValue().toString());
                    tempMap.put(entry.getKey().toString(), entry.getValue().toString());
                }
                if (resourceFile[i].equals("/collname.properties")) {
                    collnameProps.putAll(tempMap);
                } else if (resourceFile[i].equalsIgnoreCase("/weight_of_properties.properties")) {
                    weightProps.putAll(tempMap);
                }
                in.close();
            }

        } catch (IOException e) {
            System.out.println("-------------获取配置文件出错！---------------");
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return propertiesMap.get(key);
    }

    /**
     * 更新（或插入）一对properties信息(主键及其键值)
     * 如果该主键已经存在，更新该主键的值；
     * 如果该主键不存在，则插件一对键值。
     * @param keyname 键名
     * @param keyvalue 键值
     */
    public static void writeProperties(String keyname,String keyvalue) {
        try {
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(collnameFilePath);
            collnameProps.setProperty(keyname, keyvalue);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            collnameProps.store(fos, "Update '" + keyname + "' value");
            fos.close();
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }

    public static void writeWeightProperties(String keyName, String keyValue) {
        try {
            OutputStream fos = new FileOutputStream(weightFilePath);
            weightProps.setProperty(keyName, keyValue);
            weightProps.store(fos, "Update '" + keyName + "' value");
            fos.close();
        } catch (IOException e) {
            System.err.println("权重属性文件更新错误");
        }
    }

    public static void main(String[] args) {
        PropertyReaderWriter propertyReaderWriter = new PropertyReaderWriter();

    }
}

