package com.kal.vertx2.basicWeb;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Hello world!
 *
 */
public class BasicWebVerticle extends AbstractVerticle
{
	private static final Logger logger=LoggerFactory.getLogger(BasicWebVerticle.class);
	
    public static void main( String[] args )
    {
        Vertx vertx=Vertx.vertx();
        vertx.deployVerticle(new BasicWebVerticle());
        
    }
    
    public void start() {
    	logger.info("verticle started");
//    	vertx.createHttpServer()
//    	.requestHandler(routingContext -> routingContext.response().end("<h1 style='color:red'>Hello Amex </h1>")).listen(5555);
    	
    	Router router=Router.router(vertx);
    	
    	router.get("/second.html").handler(routingContext -> {
    		
    		ClassLoader classLoader=getClass().getClassLoader();
    		File file=new File(classLoader.getResource("webroot/second.html").getFile());
    		
    		String mappedHTML="";
    		
    		try {
				StringBuilder result = new StringBuilder("");

				Scanner scanner = new Scanner(file);

				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					result.append(line).append("\n");
				}

				scanner.close();

				mappedHTML = result.toString();

				mappedHTML = replaceAllTokens(mappedHTML, "{name}", "Tom Jay");

			} catch (IOException e) {
				e.printStackTrace();
          }
    		routingContext.response().putHeader("content-type", "text/html").end(mappedHTML);
    	});
    	router.route().handler(StaticHandler.create().setCachingEnabled(false));
    	vertx.createHttpServer().requestHandler(router::accept).listen(8081);
    }
    
    public String replaceAllTokens(String input, String token, String newValue) {

		String output = input;

		while (output.indexOf(token) != -1) {

			output = output.replace(token, newValue);

		}

		return output;

	}

    
    public void stop() {
    	logger.info("verticle stopped");
    }
}
