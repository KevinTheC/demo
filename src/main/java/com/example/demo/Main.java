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
import java.sql.*;
import java.util.Map;
import java.util.Stack;

import com.sun.net.httpserver.*;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class Main {
	public static final int portNumber = Integer.parseInt(System.getenv("PORT"));
	public static void main(String[] args) throws IOException, InterruptedException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, CertificateException {
		// JDBC URL, username, and password of the MySQL server
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);
        server.createContext("/home",(exchange->{
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "text/html");
        	exchange.sendResponseHeaders(200, 0);
        		try (OutputStream os = exchange.getResponseBody()) {
            os.write(Files.readAllBytes(new File("src/main/resources/static/home.html").toPath()));
        } catch (Exception e) {e.printStackTrace();}}));

        server.createContext("/project",(exchange->{
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "text/html");
        	exchange.sendResponseHeaders(200, 0);
        		try (OutputStream os = exchange.getResponseBody()) {
            os.write(Files.readAllBytes(new File("src/main/resources/static/index.html").toPath()));
        } catch (Exception e) {e.printStackTrace();}}));

        server.createContext("/api",(exchange->{
			System.out.println("f1");
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
			Map<String, String> queryParams = getQueryParams(exchange.getRequestURI().getQuery());
			System.out.println("f2");
			String response = "";
			for (Map.Entry<String, String> entry : queryParams.entrySet()){
				try {
					response += getSQL(entry.getValue());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			System.out.println("f3");
        	exchange.sendResponseHeaders(200, 0);
        		try (OutputStream os = exchange.getResponseBody()) {
            		os.write(response.getBytes());
        } catch (Exception e) {e.printStackTrace();}
			System.out.println("f4");}));
        server.start();
        Thread.sleep(30000);
        System.out.println("Killed");
        System.exit(1337);
	}
	private static Map<String, String> getQueryParams(String query) {
		return java.util.Arrays.stream(query.split("&"))
				.map(s -> s.split("="))
				.collect(java.util.stream.Collectors.toMap(s -> s[0], s -> s[1]));
	}
	private static String getSQL(String matcher) throws SQLException {
		if (matcher.length()>10)
			throw new RuntimeException();
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://ec2-18-204-162-101.compute-1.amazonaws.com:5432/dda9u1msavb3nm",
				"gemntditxmjbgp","8cbf73aebd2dcef3e7cb53ee76765f4dc018c2c9e971e314adeced39112ad296");
			 Statement statement = connection.createStatement()) {
			// Execute a simple query
			String sqlQuery = "SELECT * FROM classes WHERE classname = '"+matcher+"'";
			try (ResultSet resultSet = statement.executeQuery(sqlQuery)) {
				System.out.println(resultSet);
				return resultSet.toString();
			}
		} catch (SQLException e) {e.printStackTrace(); throw e;}
	}
}
