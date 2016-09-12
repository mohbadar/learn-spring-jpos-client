package lab.aikibo;

import org.apache.log4j.Logger;

import lab.aikibo.services.Client;
import lab.aikibo.config.AppConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 *
 */

public class App
{

    static final Logger logger = Logger.getLogger(App.class);


    public static void main( String[] args )
    {

      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      Client client = (Client) context.getBean("client");
      long startApp = System.currentTimeMillis();
      client.connect2();
      logger.debug("Lama Proses App : " + (System.currentTimeMillis() - startApp) + " ms.");
      //client.connect2();
      //client.inquiry();
    }

    // --- setter getter

    public static Logger getLogger() { return logger; }
}
