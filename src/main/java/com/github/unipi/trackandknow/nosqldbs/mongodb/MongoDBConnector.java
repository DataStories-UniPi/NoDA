package com.github.unipi.trackandknow.nosqldbs.mongodb;

import com.github.unipi.trackandknow.nosqldbs.*;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;

public final class MongoDBConnector implements NoSqlDocumentDbConnector {

    private MongoDatabase mongoDatabase;
    private NoSqlDbManager<MongoClient> manager;

    private MongoDBConnector(String host, int port, String username, String password, String database) {

        MongoCredential credential = MongoCredential.createCredential(username, database, password.toCharArray());
        MongoClientOptions options = MongoClientOptions.builder()/*.sslEnabled(true)*/.build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), credential, options);

        mongoClient.getDatabase("").

        mongoDatabase = mongoClient.getDatabase(database);

        System.out.println(mongoClient.listDatabaseNames().first());
        this.;
    }

//    public MongoDBConnector connect() {
//
//        return (NoSqlDbOperators) MongoDBConnector.newMongoDB("", "", "", 9).connect();
//    }

    public NoSqlDbManager getNoSqlDbManager(){
        return manager = NoSqlDbManager.newNoSqlManager(mongoDatabase,NoSqlDb.MONGODB);
    }


    public static NoSqlDbConnector newMongoDBConnector(String host, int port, String username, String password, String database) {
        return new MongoDBConnector(host, port, username, password, database);
    }

//    @Override
//    public NoSqlDbOperators operateOnCollection() {
//        return null;
//    }
}