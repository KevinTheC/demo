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
	public static final int portNumber = Integer.parseInt(System.getenv("PORT"));
	public static void main(String[] args) throws IOException, InterruptedException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, CertificateException {
        System.out.println("Started");
        suckme();
        HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);
        server.createContext("/home",(exchange->{
            System.out.println("Received");
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "text/html");
        	exchange.sendResponseHeaders(200, 0);
        		try (OutputStream os = exchange.getResponseBody()) {
            os.write(Files.readAllBytes(new File("testing/classes/static/home.html").toPath()));
        } catch (Exception e) {e.printStackTrace();}
            System.out.println("Sent");}));
        server.start();
        System.out.println("Server Started");
        Thread.sleep(10000);
        System.out.println("Killed");
        System.exit(1337);
	}
    public static void suckme(){
        // Get the current class's name
        String className = Main.class.getName();

        // Replace dots with slashes in the class name to get the relative path
        String relativePath = className.replace('.', '/') + ".class";

        // Try to locate the class file as a resource
        Path classPath = Paths.get(ClassLoader.getSystemResource(relativePath).getPath());
        File f = new File(classPath.toUri());
        File f2 = new File(f.getParent());
        Stack<File> stack = new Stack<>();
        stack.add(f2);
        while (!stack.isEmpty()) {
            f = stack.pop();
            if (f.isDirectory()){
                for (File child : f.listFiles())
                    stack.add(child);
            }
            System.out.println(f.getAbsolutePath());
        }
    }

}
