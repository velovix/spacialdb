package com.tylerscodebase.spatialdb.cli;

import javax.json.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.ProtocolException;

public class Client {

    public static void main(String[] args) throws ProtocolException, IOException {

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter the URL of the database: ");
        URL url = null;
        while (url == null) {
            try {
                url = new URL(stdin.readLine());
            } catch (MalformedURLException e) {
                System.out.println("Invalid URL. Please try again: ");
            }
        }

        while(true) {
            // Ask for the query
            System.out.print("> ");
            String query = stdin.readLine();

            // Create a new connection and send the query
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "text/plain");
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeChars(query);
                wr.flush();
                wr.close();
            } catch (IOException e) {
                System.out.println("Network exception!");
                System.out.println(e.getMessage());
                e.printStackTrace();
                continue;
            }

            // Parse the response
            JsonReader reader = null;
            try {
                reader = Json.createReader(con.getInputStream());
            } catch (IOException e) {
                System.out.println("Network or I/O exception!");
                System.out.println(e.getMessage());
                e.printStackTrace();
                continue;
            }
            JsonObject obj = reader.readObject();

            if (!obj.getString("error").equals("")) {
                System.out.println(obj.getString("error"));
            } else if (!obj.getString("data").equals("")) {
                System.out.println(obj.getJsonObject("data").toString());
            }

            System.out.println(obj.getString("info"));
        }

    }

}
