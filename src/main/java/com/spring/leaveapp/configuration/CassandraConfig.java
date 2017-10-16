package com.spring.leaveapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraCqlSessionFactoryBean;
import org.springframework.data.cassandra.core.cql.CqlOperations;
import org.springframework.data.cassandra.core.cql.CqlTemplate;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CassandraConfig {

    private List<String> getStartupScripts(){
        return Arrays.asList(
                "CREATE KEYSPACE IF NOT EXISTS leaveap_keyspace" +
                        " WITH durable_writes = true" +
                        " AND replication = {'class': 'SimpleStrategy', 'replication_factor': 3};",

                "USE leaveap_keyspace",

                "CREATE TABLE IF NOT EXISTS employees (" +
                        "  employee_id BIGINT," +
                        "  name VARCHAR," +
                        "  email VARCHAR," +
                        "  password VARCHAR," +
                        "  role VARCHAR," +
                        "  PRIMARY KEY (employee_id));",

                "CREATE TABLE IF NOT EXISTS leaves (" +
                        "  leave_id BIGINT," +
                        "  employee_id BIGINT," +
                        "  start_date DATE," +
                        "  end_date DATE," +
                        "  status VARCHAR," +
                        "  type VARCHAR," +
                        "  PRIMARY KEY ((employee_id, status), leave_id));"
        );
    }

    private List<String> getShutdownScripts(){
        return Arrays.asList("DROP KEYSPACE IF EXISTS leaveap_keyspace");
    }

    @Bean
    public CassandraCqlClusterFactoryBean cluster() {
        CassandraCqlClusterFactoryBean cluster = new CassandraCqlClusterFactoryBean();
        cluster.setContactPoints("127.0.0.1");
        cluster.setPort(9042);
        cluster.setStartupScripts(getStartupScripts());
        cluster.setShutdownScripts(getShutdownScripts());
        return cluster;
    }

    @Bean
    public CassandraCqlSessionFactoryBean session() {
        CassandraCqlSessionFactoryBean session = new CassandraCqlSessionFactoryBean();
        session.setCluster(cluster().getObject());
        session.setKeyspaceName("leaveap_keyspace");
        return session;
    }

    @Bean
    public CqlOperations cqlTemplate() {
        return new CqlTemplate(session().getObject());
    }

}
