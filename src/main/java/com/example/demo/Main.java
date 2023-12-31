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

		server.createContext("/proj1.png",(exchange->{
			Headers headers = exchange.getResponseHeaders();
			headers.set("Content-Type", "image/png");
			exchange.sendResponseHeaders(200, 0);
			try (OutputStream os = exchange.getResponseBody()) {
				os.write(Files.readAllBytes(new File("src/main/resources/static/proj1.png").toPath()));
			} catch (Exception e) {e.printStackTrace();}}));
		server.createContext("/proj2.png",(exchange->{
			Headers headers = exchange.getResponseHeaders();
			headers.set("Content-Type", "image/png");
			exchange.sendResponseHeaders(200, 0);
			try (OutputStream os = exchange.getResponseBody()) {
				os.write(Files.readAllBytes(new File("src/main/resources/static/proj2.png").toPath()));
			} catch (Exception e) {e.printStackTrace();}}));
		server.createContext("/proj3.png",(exchange->{
			Headers headers = exchange.getResponseHeaders();
			headers.set("Content-Type", "image/png");
			exchange.sendResponseHeaders(200, 0);
			try (OutputStream os = exchange.getResponseBody()) {
				os.write(Files.readAllBytes(new File("src/main/resources/static/proj3.png").toPath()));
			} catch (Exception e) {e.printStackTrace();}}));

        server.createContext("/api",(exchange->{
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
			Map<String, String> queryParams = getQueryParams(exchange.getRequestURI().getQuery());
			String response = "";
			for (Map.Entry<String, String> entry : queryParams.entrySet()){
				try {
					response += getSQL(entry.getValue())+'\n';
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
        	exchange.sendResponseHeaders(200, 0);
        		try (OutputStream os = exchange.getResponseBody()) {
            		os.write(response.getBytes());
        } catch (Exception e) {e.printStackTrace();}}));
        server.start();
        Thread.sleep(36000000);
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
				resultSet.next();
				return resultSet.getString("crn")+','+resultSet.getString("classname")+','+resultSet.getString("begin")+','+resultSet.getString("ending");
			}
		} catch (SQLException e) {e.printStackTrace(); throw e;}
	}
}
