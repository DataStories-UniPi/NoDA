package gr.ds.unipi.noda.api.client.redis;

import gr.ds.unipi.noda.api.client.NoSqlDbSystem;
import gr.ds.unipi.noda.api.core.nosqldb.NoSqlDbConnector;
import gr.ds.unipi.noda.api.redis.RedisConnectionFactory;
import gr.ds.unipi.noda.api.redis.RedisConnector;
import redis.clients.jedis.JedisPoolConfig;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;

public class RedisSystem extends NoSqlDbSystem {

    private final RedisConnector connector;

    @Override
    protected NoSqlDbConnector getConnector() {
        return connector;
    }

    @Override
    public int getDefaultPort() {
        return 6379;
    }

    public static class Builder extends NoSqlDbSystem.Builder<Builder> {
        private final JedisPoolConfig poolConfig;

        private int connectionTimeout = 2000;
        private int soTimeout = 2000;
        private String password = null;
        private int database = 0;
        private String clientName = null;
        private boolean ssl = false;
        private SSLSocketFactory sslSocketFactory = null;
        private SSLParameters sslParameters = null;
        private HostnameVerifier hostnameVerifier = null;

        private int poolSize;


        public Builder() {
            this.poolConfig = initPoolConfig(8);
        }

        public Builder(int poolSize) {
            this.poolConfig = initPoolConfig(poolSize);
        }

        public Builder(JedisPoolConfig poolConfig) {
            this.poolConfig = poolConfig;
        }

        public Builder timeout(int timeout) {
            this.connectionTimeout = timeout;
            this.soTimeout = timeout;
            return this;
        }

        public Builder connectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder soTimeout(int soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder database(int database) {
            this.database = database;
            return this;
        }

        public Builder clientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder ssl(boolean ssl) {
            this.ssl = ssl;
            return this;
        }

        public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

        public Builder sslParameters(SSLParameters sslParameters) {
            this.sslParameters = sslParameters;
            return this;
        }

        public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        @Override
        public NoSqlDbSystem build() {
            return new RedisSystem(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private RedisSystem(Builder builder) {
        super(builder, new RedisConnectionFactory());

        if (getAddresses().size() > 1) {
            try {
                throw new Exception("Up to one master has been defined. One master should be defined");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        connector = RedisConnector.newRedisConnector(getAddresses(), null, builder.poolConfig, builder.connectionTimeout, builder.soTimeout, builder.password, builder.database, builder.clientName, builder.ssl, builder.sslSocketFactory, builder.sslParameters, builder.hostnameVerifier);
    }

    static JedisPoolConfig initPoolConfig(int poolSize) {
        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMaxTotal(poolSize);
        conf.setTestOnBorrow(false);
        conf.setTestOnReturn(false);
        conf.setTestOnCreate(false);
        conf.setTestWhileIdle(false);
        conf.setMinEvictableIdleTimeMillis(60000L);
        conf.setTimeBetweenEvictionRunsMillis(30000L);
        conf.setNumTestsPerEvictionRun(-1);
        conf.setFairness(true);
        return conf;
    }
}
