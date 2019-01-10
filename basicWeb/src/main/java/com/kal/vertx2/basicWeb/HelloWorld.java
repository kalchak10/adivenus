package com.kal.vertx2.basicWeb;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Hello world!
 *
 */
public class HelloWorld extends AbstractVerticle
{
	private static final Logger logger=LoggerFactory.getLogger(BasicWebVerticle.class);
	
    public static void main( String[] args )
    {
        Vertx vertx=Vertx.vertx();
        vertx.deployVerticle(new HelloWorld());
        
    }
    
    public void start() {
    	logger.info("verticle started");
//    	vertx.createHttpServer()
//    	.requestHandler(routingContext -> routingContext.response().end("<h1 style='color:red'>Hello Amex </h1>")).listen(5555);
    	
    	Router router=Router.router(vertx);
    	
    	//Api routing
    	
    	router.get("/api/v1/products").handler(this::getAllProducts);
    	
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
    	vertx.createHttpServer().requestHandler(router::accept).listen(8083);
    }
    
    private void getAllProducts(RoutingContext routingContext) {
    	JsonObject responseJson=new JsonObject();
    	JsonArray items=new JsonArray();
    	JsonObject firstItem=new JsonObject();
    	firstItem.put("name", "kalyan");
    	firstItem.put("city", "tirupati");
    	items.add(firstItem);
    	JsonObject secondItem=new JsonObject();
    	secondItem.put("name", "Thomas Jay");
    	secondItem.put("city", "San fransisco");
    	items.add(secondItem);
    	responseJson.put("products", items);
    	
//    	Product firstItem=new Product("kal1","tpt");
//    	Product secondItem=new Product("kal2","LA");
//    	
//    	List<Product> products=new ArrayList<Product>();
//    	
//    	products.add(firstItem);
//    	products.add(secondItem);
    	
    	//responseJson.put("products", responseJson);
    	routingContext.response()
    	.setStatusCode(200)
    	.putHeader("content-type", "application/json")
    	.end(Json.encodePrettily(responseJson));
    	
//    	routingContext.response()
//    	.setStatusCode(400)
//    	.putHeader("content-type", "application/json")
//    	.end(Json.encodePrettily(new JsonObject().put("error", "kalyan says it is an error")));
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
