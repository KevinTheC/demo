package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

import com.sun.net.httpserver.*;

public class Main {
	public static final int portNumber = Integer.parseInt(System.getenv("PORT"));
	public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Started");
        HttpsServer server = HttpsServer.create(new InetSocketAddress(portNumber), 0);
        server.createContext("/home",(exchange->{
            System.out.println("Received");
        	exchange.sendResponseHeaders(200, 0);
        		try (OutputStream os = exchange.getResponseBody()) {
            os.write(Files.readAllBytes(new File("home.html").toPath()));
            System.out.println("Exchanged");
        }}));
        server.start();
        System.out.println("Server Started");
        Thread.sleep(10000);
        System.out.println("Killed");
        System.exit(1337);
	}
}
