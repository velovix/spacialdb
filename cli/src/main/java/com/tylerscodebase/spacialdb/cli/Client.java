package com.tylerscodebase.spacialdb.cli;

import javax.json.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Client {

    public static void main(String[] args) throws Exception {

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter the URL of the database: ");
        URL url = new URL(stdin.readLine());

        while(true) {
            // Ask for the query
            System.out.print("> ");
            String query = stdin.readLine();

            // Create a new connection and send the query
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/plain");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeChars(query);
            wr.flush();
            wr.close();

            // Parse the response
            JsonReader reader = Json.createReader(con.getInputStream());
            JsonObject obj = reader.readObject();

            if (!obj.getString("error").equals("")) {
                System.out.println(obj.getString("error"));
            } else if (!obj.getString("data").equals("")) {
                System.out.println(obj.getString("data"));
            }

            System.out.println(obj.getString("info"));
        }

    }

}
