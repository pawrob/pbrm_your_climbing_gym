package pl.ftims.ias.your_climbing_gym.utils.dbconfig;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.logging.SessionLog;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

//@Configuration
//public class EclipseLinkJpaConfiguration extends JpaBaseConfiguration {
//
//    protected EclipseLinkJpaConfiguration(DataSource dataSource, JpaProperties properties,
//                                          ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
//        super(dataSource, properties, jtaTransactionManager);
//    }
//
//
//
//    @Override
//    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
//        return new EclipseLinkJpaVendorAdapter();
//    }
//
//    @Override
//    protected Map<String, Object> getVendorProperties() {
//        Map<String, Object> map = new HashMap<>();
//        map.put(PersistenceUnitProperties.WEAVING, "false");
//        map.put(PersistenceUnitProperties.LOGGING_LEVEL, SessionLog.FINE_LABEL);
//
//        return map;
//    }
//
//
//}