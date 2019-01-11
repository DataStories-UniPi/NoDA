package com.github.unipi.trackandknow.nosqldbs;

import com.github.unipi.trackandknow.nosqldbs.connection.NoSqlDbConnector;
import com.github.unipi.trackandknow.nosqldbs.connection.MongoDBConnector;
import com.github.unipi.trackandknow.nosqldbs.mongodb.MongoDBOperators;
import com.mongodb.MongoClient;
import org.apache.spark.sql.SparkSession;

import java.util.*;

public enum NoSqlDb {

    MONGODB{

        private final Map<MongoDBConnector,MongoClient> connections = new HashMap<>();

        @Override
        public NoSqlDbConnector createNoSqlDbConnector(String host, int port, String username, String password, String database){
            return MongoDBConnector.newMongoDBConnector(host, port, username, password, database);
        }

        @Override
        public NoSqlDbOperators noSqlDbOperators(NoSqlDbConnector connector, String s, SparkSession sparkSession){
            return MongoDBOperators.newMongoDBOperators(MONGODB, connector, s, sparkSession);
        }

        @Override
        public void disconnect(Object o){
            ((MongoClient) o).close();
        }

        @Override
        public int getDefaultPort(){
            return 27017;
        }

        @Override
        public String getDefaultDatabase(){
            return "";
        }

        @Override
        public String getDefaultUsername(){
            return "";
        }

        @Override
        public String getDefaultPassword(){
            return "";
        }

        @Override
        public Object getConnectionOfConnector(NoSqlDbConnector connector) {

            if(connections.containsKey(connector)){
                return connections.get(connector);
            }

            MongoClient c = (MongoClient) connector.createConnection();

            connections.put((MongoDBConnector) connector,c);

            return c;
        }

        @Override
        public boolean closeConnections() {
            connections.forEach((k,v)->{
                v.close();
            });
            connections.clear();
            return true;
        }
    };

    public abstract NoSqlDbConnector createNoSqlDbConnector(String host, int port, String username, String password, String database);

    public abstract NoSqlDbOperators noSqlDbOperators(NoSqlDbConnector connector, String s, SparkSession sparkSession);

    public abstract void disconnect(Object o);

    public String getDefaultHost(){
        return "localhost";
    }

    public abstract int getDefaultPort();

    public abstract String getDefaultDatabase();

    public abstract String getDefaultUsername();

    public abstract String getDefaultPassword();

    public abstract Object getConnectionOfConnector(NoSqlDbConnector connector);

    public abstract boolean closeConnections();

}
