package com.github.unipi.trackandknow;

import com.github.unipi.trackandknow.nosqldbs.NoSqlDb;
import com.github.unipi.trackandknow.nosqldbs.NoSqlDbOperators;
import com.github.unipi.trackandknow.nosqldbs.connection.NoSqlDbConnector;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.List;

public class NoSqlDbSystem {

    public static class Builder {

        private final NoSqlDb nsdb;

        private String host;
        private int port;
        private String database; // the name of the database in which the user is defined
        private String username; // the user name
        private String password; // the password as a character array

        public Builder(NoSqlDb nsdb) {

            this.nsdb = nsdb;
            this.host = nsdb.getDefaultHost();
            this.port = nsdb.getDefaultPort();
            this.database = nsdb.getDefaultDatabase();
            this.username = nsdb.getDefaultUsername();
            this.password = nsdb.getDefaultPassword();

        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public NoSqlDbSystem connect(){

            return new NoSqlDbSystem(nsdb,nsdb.createNoSqlDbConnector());
            //return new NoSqlDbSystem(nsdb, nsdb.noSqlDbConnector(host, port, username, password, database).getConnector(), database);
        }



    }

    private final NoSqlDb nsdb;
    private final NoSqlDbConnector connector;
    private final SparkSession sparkSession;


    private NoSqlDbSystem(NoSqlDb nsdb, NoSqlDbConnector connector, SparkSession sparkSession){
        this.nsdb = nsdb;
        this.connector = connector;
        this.sparkSession = sparkSession;
    }

    public void disconnect(){
        nsdb.disconnect(connector);
    }

    public NoSqlDbOperators operateOn(String s){
        return nsdb.noSqlDbOperators(connector,s,sparkSession);
    }

    public static Builder MongoDB(){
        return new NoSqlDbSystem.Builder(NoSqlDb.MONGODB);
    }

    public static void cleanUp(){
        toBeCleaned.forEach(noSqlDb -> );
    }

    private static final List<NoSqlDb> toBeCleaned = new ArrayList<>();

}
