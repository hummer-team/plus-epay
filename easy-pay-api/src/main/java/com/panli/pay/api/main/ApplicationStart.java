package com.panli.pay.api.main;


import com.hummer.core.starter.HummerApplicationStart;
import com.hummer.rest.webserver.UndertowServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * application enter.
 * <p>
 * dubbo.xml or dubbo-consumer.xml choose one
 * </p>
 *
 * @author liguo
 **/
@SpringBootApplication(scanBasePackages = "com.panli.pay", exclude = {DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
//@ImportResource(locations = "classpath:dubbo.xml")
@UndertowServer
public class ApplicationStart {

    public static void main(String[] args) {
        HummerApplicationStart.start(ApplicationStart.class, args);
    }

}
