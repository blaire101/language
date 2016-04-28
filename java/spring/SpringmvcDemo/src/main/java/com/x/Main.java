package com.x;

import com.x.demo.main.TaskManager;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Date : 2016-04-11
 */
public class Main {
  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

//        System.out.println(TokenUtil.encrypt("616742"));

    int port = 8080;
    if (args.length > 0) {
      try {
        port = new Integer(args[0]);
      } catch (NumberFormatException e) {
        System.err.println("Invalid port '" + args[0] + "'. Port must be a number.");
        System.exit(1);
      }
    }

    Server server = new Server();
    //System.setProperty("org.eclipse.jetty.util.URI.charset","ISO-8859-1");
    QueuedThreadPool threadPool = new QueuedThreadPool();
    threadPool.setMaxThreads(100);
    server.setThreadPool(threadPool);
    Connector connector = new SelectChannelConnector();
    connector.setPort(port);
    server.setConnectors(new Connector[]{connector});
    WebAppContext context = new WebAppContext();
    context.setContextPath("/");
    context.setResourceBase("src/main/webapp");
    server.setHandler(context);

    try {
      server.start();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}