package com.abhi.utitlity;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.spi.Configurable;
import org.hibernate.type.Type;

import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class IdentityUtil implements IdentifierGenerator {

    private final Random random = new Random();
    private String prefix;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        prefix = params.getProperty("prefix_value");
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        int randomValue = random.nextInt(100000);
        return prefix+"_"+randomValue;
    }
}
