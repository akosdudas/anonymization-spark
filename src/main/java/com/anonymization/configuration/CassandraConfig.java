package com.anonymization.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

@Configuration
@PropertySource(value = { "classpath:cassandra.properties" })
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Value("${cassandra.contactpoints}")
    private String CASSANDRA_CONTACTPOINT;

    @Value("${cassandra.port}")
    private Integer CASSANDRA_PORT;

    @Value("${cassandra.keyspace}")
    private String CASSANDRA_KEYSPACE;


    @Override
    protected String getKeyspaceName() {
        return CASSANDRA_KEYSPACE;
    }

    @Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(CASSANDRA_CONTACTPOINT);
        cluster.setPort(CASSANDRA_PORT);

        return cluster;
    }

    @Bean
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        return new BasicCassandraMappingContext();
    }
}
