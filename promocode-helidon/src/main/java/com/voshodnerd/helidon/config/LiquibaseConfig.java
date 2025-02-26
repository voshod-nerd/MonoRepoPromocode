package com.voshodnerd.helidon.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import liquibase.integration.jakarta.cdi.CDILiquibase;
import liquibase.integration.jakarta.cdi.CDILiquibaseConfig;
import liquibase.integration.jakarta.cdi.annotations.LiquibaseType;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
@ApplicationScoped
public class LiquibaseConfig {

    private final CDILiquibase liquibase;

    @Inject
    public LiquibaseConfig(CDILiquibase liquibase) {
        this.liquibase = liquibase;
    }

    @Produces
    @LiquibaseType
    public CDILiquibaseConfig createConfig() {
        CDILiquibaseConfig config = new CDILiquibaseConfig();
        config.setChangeLog("changelog-master.xml");
        boolean configShouldRun = Boolean.valueOf(System.getProperty("liquibase.config.shouldRun", "true"));
        config.setShouldRun(configShouldRun);
        config.setDropFirst(true);
        return config;
    }

    @Produces
    @LiquibaseType
    public DataSource createDataSource() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setURL("jdbc:postgresql://localhost:5432/simpledb");
        ds.setCurrentSchema("public");
        ds.setUser("postgres");
        ds.setPassword("admin");
        return ds;
    }

    @Produces
    @LiquibaseType
    public ResourceAccessor create() {
        return new ClassLoaderResourceAccessor(getClass().getClassLoader());
    }
}
