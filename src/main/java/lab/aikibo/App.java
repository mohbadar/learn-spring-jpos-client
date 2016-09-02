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

    static long startTime;
    static long endTime;

    public static void main( String[] args )
    {
      ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
      Client client = (Client) context.getBean("client");
      for(int i=1; i<=100; i++) {
        startTime = System.currentTimeMillis();
        client.connect();
        logger.debug("Lama Waktu Iterasi " + i + " : " + (System.currentTimeMillis() - startTime) + " ms.");
      }
      //client.connect2();
      //client.inquiry();
    }

    // --- setter getter

    public static Logger getLogger() { return logger; }
}
