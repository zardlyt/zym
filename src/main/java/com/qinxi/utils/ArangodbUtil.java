package com.qinxi.utils;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;

/**
 * Created by tangjuan on 2017/6/12.
 */
public class ArangodbUtil {

    private static final String urlDouban = PropertyReaderWriter.getProperty("arango.host");
    private static final Integer portDouban = Integer.parseInt(PropertyReaderWriter.getProperty("arango.port"));
    private static final String dbNameDouban = PropertyReaderWriter.getProperty("arango.dbName.douban");
    private static final String userNameDouban = PropertyReaderWriter.getProperty("arango.userName");
    private static final String passwordDouban = PropertyReaderWriter.getProperty("arango.password");

    private static final String urlMtime = PropertyReaderWriter.getProperty("arango.host");
    private static final Integer portMtime = Integer.parseInt(PropertyReaderWriter.getProperty("arango.port"));
    private static final String dbNameMtime = PropertyReaderWriter.getProperty("arango.dbName.mtime");
    private static final String userNameMtime = PropertyReaderWriter.getProperty("arango.userName");
    private static final String passwordMtime = PropertyReaderWriter.getProperty("arango.password");

    private static final String urlZujian = PropertyReaderWriter.getProperty("arango.host.zu");
    private static final Integer portZujian = Integer.parseInt(PropertyReaderWriter.getProperty("arango.port.zu"));
    private static final String dbNameZujian = PropertyReaderWriter.getProperty("arango.dbName.zu");
    private static final String userNameZujian = PropertyReaderWriter.getProperty("arango.userName.zu");
    private static final String passwordZujian = PropertyReaderWriter.getProperty("arango.password.zu");

    private static ArangoDB arangoDBDouban;

    private static ArangoDB arangoDBMtime;

    private static ArangoDB arangoDBZujian;


    public static ArangoDatabase getDbDouban() {
        arangoDBDouban = new ArangoDB.Builder().host(urlDouban, portDouban).user(userNameDouban).password(passwordDouban).build();

        //判断database是否已经存在，不存在就新创建
        if (!arangoDBDouban.getDatabases().contains(dbNameDouban)) {
            arangoDBDouban.createDatabase(dbNameDouban);
        }
        return arangoDBDouban.db(dbNameDouban);
    }

    public static ArangoDatabase getDbMtime(){

        arangoDBMtime = new ArangoDB.Builder().host(urlMtime, portMtime).user(userNameMtime).password(passwordMtime).build();
        if (!arangoDBMtime.getDatabases().contains(dbNameMtime)) {
            arangoDBMtime.createDatabase(dbNameMtime);
        }
        return arangoDBMtime.db(dbNameMtime);
    }

    public static ArangoDatabase getDbZujian() {
        arangoDBZujian = new ArangoDB.Builder().host(urlZujian, portZujian).user(userNameZujian).password(passwordZujian).build();
        return arangoDBZujian.db(dbNameZujian);
    }

    public static void shutDownDouban() {
        if (arangoDBDouban != null) {
            arangoDBDouban.shutdown();
        }
    }

    public static void shutDownTime() {

        if (arangoDBMtime != null) {
            arangoDBMtime.shutdown();
        }
    }

    public static void shutDownZujian() {
        if (arangoDBZujian != null) {
            arangoDBZujian.shutdown();
        }
    }

}
