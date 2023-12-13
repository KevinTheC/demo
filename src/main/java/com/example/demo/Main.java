package com.example.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.CertificateException;

import com.sun.net.httpserver.*;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class Main {
	public static final int portNumber = Integer.parseInt(System.getenv("PORT"));
	public static void main(String[] args) throws IOException, InterruptedException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, CertificateException {
        System.out.println("Started");
        HttpsServer server = HttpsServer.create(new InetSocketAddress(portNumber), 0);
        char[] password = "password".toCharArray();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream("keystore.jks"), password);

        // Set up key manager factory
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, password);

        // Set up SSL context
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters params) {
                // Get the default SSL context and engine
                SSLContext context = getSSLContext();
                params.setSSLParameters(context.getDefaultSSLParameters());
            }
        });
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
