package com.example.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Stack;

import com.sun.net.httpserver.*;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class Main {
	//public static final int portNumber = Integer.parseInt(System.getenv("PORT"));
	public static void main(String[] args) throws IOException, InterruptedException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, CertificateException {
        System.out.println("Started");
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/home",(exchange->{
            System.out.println("Received");
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "text/html");
        	exchange.sendResponseHeaders(200, 0);
        		try (OutputStream os = exchange.getResponseBody()) {
            os.write(Files.readAllBytes(new File("src/main/resources/static/home.html").toPath()));
        } catch (Exception e) {e.printStackTrace();}
            System.out.println("Sent");}));
        server.start();
        System.out.println("Server Started");
        Thread.sleep(10000);
        System.out.println("Killed");
        System.exit(1337);
	}
}
