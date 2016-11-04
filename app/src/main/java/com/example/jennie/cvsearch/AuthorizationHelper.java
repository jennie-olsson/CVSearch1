package com.example.jennie.cvsearch;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;



import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;

/**
 * Created by Jennie on 2016-11-01.
 */

public class AuthorizationHelper {
    public static Credential authorize() throws Exception {
        java.io.File DATA_STORE_DIR =
                new java.io.File("permissionsFile", "PermissionsFolder");
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
       // NetHttpTransport httpTransport = NetHttpTransport.Builder();
      //  FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        // load client secrets
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(AuthorizationHelper.class.getResourceAsStream("/CVSearch-75f94146bf6e.json")));//new InputStreamReader(new FileInputStream("client_secret.json")));
//        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
//                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
//            System.out.println(
//                    "Enter Client ID and Secret from https://code.google.com/apis/console/?api=plus "
//                            + "into plus-cmdline-sample/src/main/resources/client_secret.json");
//            System.exit(1);
//        }
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                transport, JSON_FACTORY, clientSecrets,
                Collections.singleton("https://www.google.com/m8/feeds/"))
                //.setDataStoreFactory(dataStoreFactory)
                .build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("googleAccount");
    }
}
