package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

import com.sun.net.httpserver.*;

public class Main {
	public static final int portNumber = 8080;
	public static void main(String[] args) throws IOException, InterruptedException {
        HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);
        server.createContext("/home",(exchange->{
        	exchange.sendResponseHeaders(200, 0);
        		try (OutputStream os = exchange.getResponseBody()) {
            os.write(Files.readAllBytes(new File("home.html").toPath()));
        }}));
        server.createContext("/scheduleBuilder",new HomepageHandler());
        server.start();
        Thread.sleep(10000);
        System.exit(1337);
	}
}
