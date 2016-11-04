package com.example.jennie.cvsearch;


import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.os.Environment;
import android.util.Log;
import android.provider.Contacts;
//import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.widget.AdapterView;

import android.widget.ListAdapter;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;


//import com.google.api.services.people.v1.People;
//
//import com.google.api.services.people.v1.PeopleScopes;
//import com.google.api.services.people.v1.model.ListConnectionsResponse;
//
//import com.google.api.services.people.v1.model.Name;
//import com.google.api.services.people.v1.model.Person;
////import com.google.api.services.people.v1.model.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.gdata.client.*;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthParameters;
import com.google.gdata.client.contacts.*;
import com.google.gdata.data.*;

import com.google.gdata.data.contacts.*;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
import java.io.IOException;
import java.net.URL;

import android.provider.ContactsContract;
////////////////////
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

import com.google.gdata.client.Query;
import com.google.gdata.client.Service;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.Link;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.NoLongerAvailableException;
import com.google.gdata.util.ServiceException;


import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/****************************************************************************************************
 * DriveJavaClient
 * The purpose of this app is to search google drive files CV's containing user specified keywords.
 * The current status is:
 * - The app has permissions to open and download all files from the users
 * google drive account.
 * - The app has permissions to read drive file meta data
 * - The app has permissions to read and write to external storage
 * - Opening / download files is prepared, there are function calls for both functionalities
 * - The user can search for keywords and get a list of results displayed
 * - Clicking on an element in said list will either download or open the corresponding file
 * <p>
 * To be useful the app must implement:
 * - Filter results not located in the folder for CV's (Avalon have a drive folder for CV's)
 * - Add parser for user input, implement boolean functions such that user query "C/C++ asm"
 * returns result for "C/C++" AND "asm". The google Api supports this, but it must be implemented
 * on client side
 * <p>
 * To be awesome the app should:
 * - User functionality to save interesting material in new folder
 * - Log all user queries, could give some cool data on what qualifications avalon needs
 * (maybe android programmers?)
 * - Rework ui so user doesn't get cancer from prolonged use
 * <p>
 * Readability would benefit from breaking up functionality in different files/modules.
 * The onCreate method is to big, should be broken up into separate methods and not nested classes
 * <p>
 * Gradle:
 * - Not all of google services is included as android has a 64k method cap
 * just copy my gradle file and everything should work
 * <p>
 * Pretty much everything concerning account management is taken from developers.google.com
 * If you change package name you will have to set up a new project at console.developers.google.com
 * by supplying packagename, sha1 key and enabling the cloud api
 * Note that if you're using windows, then the keytool.exe is located in your jdk install path
 * default is c:\"program files"\java\jdk.<version>\bin\
 ****************************************************************************************************/
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.database.Cursor;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.google.api.client.googleapis.auth.oauth2.GoogleCredential.fromStream;

///////////////////////////////
public class MainActivity extends AppCompatActivity implements
        EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;//Used for logging into google
    GoogleCredential mCredential2;
    private Button mSearchButton;       //well this should be rather self documenting :)
    private TextView mOutputText;       //Used for status echo to user
    private EditText mTextInput;        //User input field
    private static final String BUTTON_TEXT = "Search";
    ProgressDialog mProgress;           //Not super useful but shows progress (duh)
    private String mInput;              //Current user input, this could be handled locally
    // private TextView mOutputText;       //Used for status echo to user
    private Drive mService = null;    //kinda pointer to results from drive

    private ContactsService myCS;// = new ContactsService("CVSearch");
  //  private People mPeople = null;
    private ListView mResultList;       //List in the ui
    private List<File> mFiles;          //Returned files, mService can handle all functionnallity, remove this
    //Used to figure out which method was caller
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    //Strings used in the ui

    //Permissions
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {DriveScopes.DRIVE_METADATA_READONLY, DriveScopes.DRIVE,  "https://www.google.com/m8/feeds/"/*, "https://www.googleapis.com/auth/contacts.readonly"*/
           /* ,PeopleScopes.CONTACTS_READONLY*/};
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
///////////
//    private URL feedUrl;
//    private static final String username="yourUsername";
//    private static final String pwd="yourPassword";
//    private ContactsService service;
//    /** Directory to store user credentials for this application. */
//    private static final java.io.File DATA_STORE_DIR = new java.io.File(
//            System.getProperty("user.home"), ".credentials/CVSearch");

//    /** Global instance of the {@link FileDataStoreFactory}. */
//    private static FileDataStoreFactory DATA_STORE_FACTORY;
//
//    /** Global instance of the JSON factory. */
//    private static final JsonFactory JSON_FACTORY =
//            JacksonFactory.getDefaultInstance();
//
//    /** Global instance of the HTTP transport. */
//    private static HttpTransport HTTP_TRANSPORT;






    ////////////////


    private ListView lstNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.lstNames = (ListView) findViewById(R.id.resultList);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mTextInput = (EditText) findViewById(R.id.textInput);
        mTextInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /****************************************************************************************************
             * @param TextView v    - Points to the user input text field
             * @param int      actionId  - Not used
             * @param KeyEvent event - Test for enter down
             * @return bool         - not used
             * <p>
             * Runs when user presses enter on the keyboard. User could also press the search button
             * Hides the keyboard after enter.
             ****************************************************************************************************/
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    mInput = v.getText().toString();
                    //echo to user (TODO remove from production)
                    Toast.makeText(getApplicationContext(), mInput, Toast.LENGTH_LONG)
                            .show();
                    v.setText("");
                    //hides keyboard
                    InputMethodManager imm = (InputMethodManager)
                            getApplicationContext().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    //init query
                    getResultFromApi();
                    System.out.println("go");
                    handled = true;

                }

                return handled;
            }
        });

        mSearchButton = (Button) findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            /****************************************************************************************************
             * @param View v
             *             Pretty much same as the text input callback
             ****************************************************************************************************/
            @Override
            public void onClick(View v) {
                mSearchButton.setEnabled(false);
                mInput = mTextInput.getText().toString();
                mTextInput.setText("");
                mOutputText.setText("");
                getResultFromApi();
                mSearchButton.setEnabled(true);
            }
        });

        mOutputText = (TextView) findViewById(R.id.statusText);
        mOutputText.setText("Click the \'" + BUTTON_TEXT + "\' button to search");


        //mResultList = (ListView) findViewById(R.id.resultList);
        System.out.println("f");

        //mResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
////                System.out.println("jhrf2p9");
////                File file = mFiles.get(position);
////                Toast.makeText(getApplicationContext(),
////                        file.getTitle().toString(), Toast.LENGTH_LONG).show();
////                String urlString = "https://docs.google.com/document/d/"
////                        + file.getId();
////                //+ "/edit?usp=sharing";
////                //  +((file.getMimeType().equals("application/vnd.google-apps.document"))?"/export?format=pdf":"");
////                String title = file.getTitle();
////
////                /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
////                * Design choice here, open documents with drive app
////                * Or download file to external storage
////                * Both solutions are prepared:
////                * - Call startActivity to open  google drive
////                * (or rather let the user choose default app
////                * - Call downloadFile to download file to external storage
////                *!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
////
////                //open with google drive
////                Intent i = new Intent(Intent.ACTION_VIEW);
////                i.setData(Uri.parse(urlString));
////                startActivity(i);
//
//                //download file (starts new thread)
//                //downloadFile(file.getId(),title,((file.getMimeType().equals("application/vnd.google-apps.document")?false:true)));
//            }
//        });
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Searching");


        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        myCS = new ContactsService("CVSearch");



//        try {
//            myCS.setUserCredentials("jennie.olsson@avaloninnovation.com", "sommar@16");
//        } catch (AuthenticationException e) {
//            e.printStackTrace();
//        }
       // showContacts();



    }
//    public void showContacts() {
//        // Check the SDK version and whether the permission is already granted or not.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
//            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
//        } else {
//            // Android version is lesser than 6.0 or the permission is already granted.
//
//
//            List<String> contacts = getContactNames();
//           // getlistofcontacts(27);
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
//            lstNames.setAdapter(adapter);
//            try {
//                printAllGroups(myCS);
//            } catch (Exception e){
//                System.out.println("oh no");
//                e.printStackTrace();
//            }
//        }
//    }
//    void printAllGroups(ContactsService contactsService)
//            throws ServiceException, IOException {
//        // Request the feed
//        URL feedUrl = new URL("https://www.google.com/m8/feeds/groups/default/full");
//        ContactGroupFeed resultFeed = contactsService.getFeed(feedUrl, ContactGroupFeed.class);
//
//        for (ContactGroupEntry groupEntry : resultFeed.getEntries()) {
//            System.out.println("Atom Id: " + groupEntry.getId());
//            System.out.println("Group Name: " + groupEntry.getTitle().getPlainText());
//            System.out.println("Last Updated: " + groupEntry.getUpdated());
//
//            System.out.println("Extended Properties:");
//            for (ExtendedProperty property : groupEntry.getExtendedProperties()) {
//                if (property.getValue() != null) {
//                    System.out.println("  " + property.getName() + "(value) = " +
//                            property.getValue());
//                } else if (property.getXmlBlob() != null) {
//                    System.out.println("  " + property.getName() + "(xmlBlob) = " +
//                            property.getXmlBlob().getBlob());
//                }
//            }
//            System.out.println("Self Link: " + groupEntry.getSelfLink().getHref());
//            if (!groupEntry.hasSystemGroup()) {
//                // System groups do not have an edit link
//                System.out.println("Edit Link: " + groupEntry.getEditLink().getHref());
//                System.out.println("ETag: " + groupEntry.getEtag());
//            }
//            if (groupEntry.hasSystemGroup()) {
//                System.out.println("System Group Id: " +
//                        groupEntry.getSystemGroup().getId());
//            }
//        }
//    }
//    private List<String> getContactNames() {
//        List<String> contacts = new ArrayList<>();
//        // Get the ContentResolver
//        ContentResolver cr = getContentResolver();
//        // Get the Cursor of all the contacts
//        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//        //getContentResolver().query(ContactsContract.Groups.CONTENT_URI,
////                null,
////                ContactsContract.Groups.ACCOUNT_NAME + "=? AND " +
////                        ContactsContract.Groups.ACCOUNT_TYPE + "=? AND " +
////                        ContactsContract.Groups.DELETED + "=?",
////                null,
////                ContactsContract.Groups._ID);
//        // Move the cursor to first. Also check whether the cursor is empty or not.
//        if (cursor.moveToFirst()) {
//            // Iterate through the cursor
//            do {
//                // Get the contacts name
//                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                contacts.add(name);
//            } while (cursor.moveToNext());
//        }
//        // Close the curosor
//        cursor.close();
//
//        return contacts;
//    }
//
//    void getlistofcontacts( long groupId ) {
//        String[] cProjection = { ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID };
//
//        Cursor groupCursor = getContentResolver().query(
//                ContactsContract.Data.CONTENT_URI,
//                cProjection,
//                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
//                        + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
//                        + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
//                new String[] { String.valueOf(groupId) }, null);
//        if (groupCursor != null && groupCursor.moveToFirst())
//        {
//            //Toast.makeText(this,"if",1000).show();
//            do
//            {
//
//                int nameCoumnIndex = groupCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//
//                String name = groupCursor.getString(nameCoumnIndex);
//
//                long contactId = groupCursor.getLong(groupCursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));
//
//                Cursor numberCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER }, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
//
//                if (numberCursor.moveToFirst())
//                {
//                    int numberColumnIndex = numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//                    do
//                    {
//                        String phoneNumber = numberCursor.getString(numberColumnIndex);
//
//                        Toast.makeText(this,name+phoneNumber,Toast.LENGTH_LONG).show();
//                    } while (numberCursor.moveToNext());
//                    numberCursor.close();
//                }
//                else
//                {
//                    Toast.makeText(this,"no contact are there",Toast.LENGTH_LONG).show();
//                }
//            } while (groupCursor.moveToNext());
//            groupCursor.close();
//        }
//        else
//        {
//            Toast.makeText(this,"no such group exists",Toast.LENGTH_LONG).show();
//        }
//    }

//    private Cursor getContacts() {
//        // Run query
//        Uri uri = ContactsContract.Contacts.CONTENT_URI;
//        String[] projection = new String[] { ContactsContract.Contacts._ID,
//                ContactsContract.Contacts.DISPLAY_NAME };
//        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
//                + ("1") + "'";
//        String[] selectionArgs = null;
//        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
//                + " COLLATE LOCALIZED ASC";
//
//        return  getContentResolver().query(uri, projection, selection, selectionArgs,
//                sortOrder);
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
//                showContacts();
                new MakeRequestTask(mCredential).execute();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        System.out.println("hrpw");
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void getResultFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            System.out.println("av");
            aquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
            System.out.println("ames ");
        } else if (!isDeviceOnline()) {
            mOutputText.setText("No network connection available");
            System.out.println("No netw available for connection.");
        } else
            new MakeRequestTask(mCredential).execute();

        System.out.println("Nhi.");
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        System.out.println("tion.");
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                System.out.println("null on.");
                mCredential.setSelectedAccountName(accountName);
                getResultFromApi();
            } else {
                System.out.println("No ngi8hpil.");
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            System.out.println("few.");
            //request the get account permission via user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your google account",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    //callback for accountPicker and authorization
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    System.out.println("This app requires Google Play Services." +
                            "Please install Google Play Services and restart" +
                            "this app");
                } else {
                    getResultFromApi();
                }
                break;

            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null
                        && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager
                            .KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultFromApi();
                    }
                }
                break;

            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultFromApi();
                }
                break;
        }
    }


    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void aquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode))
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
    }

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatus) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                MainActivity.this,
                connectionStatus,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

//    public ContactsService authenticateId(){
//
//        GoogleOAuthParameters oAuthParameters;// = null;
//        ContactsService contactService = null;
//
//        try{
//            contactService = new ContactsService("CVSearch");
//            oAuthParameters = new GoogleOAuthParameters();
//            oAuthParameters.setOAuthConsumerKey("ConsumerKey");
//            oAuthParameters.setOAuthConsumerSecret("ConsumerKey");
//            oAuthParameters.setScope("https://www.google.com/m8/feeds");
//            oAuthParameters.setOAuthType(OAuthParameters.OAuthType.TWO_LEGGED_OAUTH);
//            oAuthParameters.addCustomBaseParameter("xoauth_requestor_id", "my ID@gmail.com");
//            contactService.setOAuthCredentials(oAuthParameters,new OAuthHmacSha1Signer());
//            contactService.getRequestFactory().setHeader("GData-Version", "3.0");
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        System.out.println("cp0");
//        return contactService;
//
//    }
//    public static void printAllContacts(ContactsService myService)
//            throws ServiceException, IOException {
//        // Request the feed
//        URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
//        ContactFeed resultFeed = myService.getFeed(feedUrl, ContactFeed.class);
//        // Print the results
//        System.out.println(resultFeed.getTitle().getPlainText());
//        for (ContactEntry entry : resultFeed.getEntries()) {
//            if (entry.hasName()) {
//                Name name = entry.getName();
//                if (name.hasFullName()) {
//                    String fullNameToDisplay = name.getFullName().getValue();
//                    if (name.getFullName().hasYomi()) {
//                        fullNameToDisplay += " (" + name.getFullName().getYomi() + ")";
//                    }
//                    System.out.println("\t\t" + fullNameToDisplay);
//                } else {
//                    System.out.println("\t\t (no full name found)");
//                }
//                if (name.hasNamePrefix()) {
//                    System.out.println("\t\t" + name.getNamePrefix().getValue());
//                } else {
//                    System.out.println("\t\t (no name prefix found)");
//                }
//                if (name.hasGivenName()) {
//                    String givenNameToDisplay = name.getGivenName().getValue();
//                    if (name.getGivenName().hasYomi()) {
//                        givenNameToDisplay += " (" + name.getGivenName().getYomi() + ")";
//                    }
//                    System.out.println("\t\t" + givenNameToDisplay);
//                } else {
//                    System.out.println("\t\t (no given name found)");
//                }
//                if (name.hasAdditionalName()) {
//                    String additionalNameToDisplay = name.getAdditionalName().getValue();
//                    if (name.getAdditionalName().hasYomi()) {
//                        additionalNameToDisplay += " (" + name.getAdditionalName().getYomi() + ")";
//                    }
//                    System.out.println("\t\t" + additionalNameToDisplay);
//                } else {
//                    System.out.println("\t\t (no additional name found)");
//                }
//                if (name.hasFamilyName()) {
//                    String familyNameToDisplay = name.getFamilyName().getValue();
//                    if (name.getFamilyName().hasYomi()) {
//                        familyNameToDisplay += " (" + name.getFamilyName().getYomi() + ")";
//                    }
//                    System.out.println("\t\t" + familyNameToDisplay);
//                } else {
//                    System.out.println("\t\t (no family name found)");
//                }
//                if (name.hasNameSuffix()) {
//                    System.out.println("\t\t" + name.getNameSuffix().getValue());
//                } else {
//                    System.out.println("\t\t (no name suffix found)");
//                }
//            } else {
//                System.out.println("\t (no name found)");
//            }
//            System.out.println("Email addresses:");
//            for (Email email : entry.getEmailAddresses()) {
//                System.out.print(" " + email.getAddress());
//                if (email.getRel() != null) {
//                    System.out.print(" rel:" + email.getRel());
//                }
//                if (email.getLabel() != null) {
//                    System.out.print(" label:" + email.getLabel());
//                }
//                if (email.getPrimary()) {
//                    System.out.print(" (primary) ");
//                }
//                System.out.print("\n");
//            }
//            System.out.println("IM addresses:");
//            for (Im im : entry.getImAddresses()) {
//                System.out.print(" " + im.getAddress());
//                if (im.getLabel() != null) {
//                    System.out.print(" label:" + im.getLabel());
//                }
//                if (im.getRel() != null) {
//                    System.out.print(" rel:" + im.getRel());
//                }
//                if (im.getProtocol() != null) {
//                    System.out.print(" protocol:" + im.getProtocol());
//                }
//                if (im.getPrimary()) {
//                    System.out.print(" (primary) ");
//                }
//                System.out.print("\n");
//            }
//            System.out.println("Groups:");
//            for (GroupMembershipInfo group : entry.getGroupMembershipInfos()) {
//                String groupHref = group.getHref();
//                System.out.println("  Id: " + groupHref);
//            }
//            System.out.println("Extended Properties:");
//            for (ExtendedProperty property : entry.getExtendedProperties()) {
//                if (property.getValue() != null) {
//                    System.out.println("  " + property.getName() + "(value) = " +
//                            property.getValue());
//                } else if (property.getXmlBlob() != null) {
//                    System.out.println("  " + property.getName() + "(xmlBlob)= " +
//                            property.getXmlBlob().getBlob());
//                }
//            }
//            Link photoLink = entry.getContactPhotoLink();
//            String photoLinkHref = photoLink.getHref();
//            System.out.println("Photo Link: " + photoLinkHref);
//            if (photoLink.getEtag() != null) {
//                System.out.println("Contact Photo's ETag: " + photoLink.getEtag());
//            }
//            System.out.println("Contact's ETag: " + entry.getEtag());
//        }
//    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //TODO check what this is supposed to be used for
        System.out.println("o no");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //TODO check what this is supposed to be used for
        System.out.println("No n.");
    }

//////////
//
//    /**
//     * Creates an authorized Credential object.
//     * @return an authorized Credential object.
//     * @throws IOException
//     */
//    public static Credential authorize() throws IOException {
//        // Load client secrets.
//        InputStream in =
//                ContactsService.class.getResourceAsStream("/client_secret.json");
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//
//        // Build flow and trigger user authorization request.'
//
//        GoogleAuthorizationCodeFlow flow =
//                new GoogleAuthorizationCodeFlow.Builder( HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Arrays.asList( "https://www.google.com/m8/feeds/"))
//                     //  .setDataStoreFactory(DATA_STORE_FACTORY)
//                      //  .setAccessType("offline")
//                        .build();
//        Credential credential = new AuthorizationCodeInstalledApp(
//                flow, new LocalServerReceiver()).authorize("user");
//        System.out.println("hioipopöåä");
//               // "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
//        return credential;
//    }

//    private class GetTask extends AsyncTask<Void, Void, Void>{
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            String scope = "https://www.google.com/m8/feeds";
//
////            ContactsService service = new ContactsService("CVSearch");
////            service.setOAuth2Credentials(getCredential(scope, PREF_ACCOUNT_NAME));
//
//
//
//
////            service = new ContactsService("CVSearch");
////            service.getRequestFactory().setHeader("User-Agent", "CVSearch");
////            try {
////                //service.setOAuth2Credentials( );//.setUserCredentials(mCredential);
////
////
////
////                service.setOAuth2Credentials(authorize());
////
////
////
////
//////                URL contactFeedURL = new URL("https://www.google.com/m8/feeds/contacts/default/full");
//////                Query contactFeedQuery = new Query(contactFeedURL);
//////
//////                ContactFeed contactFeed = service.getFeed(contactFeedQuery, ContactFeed.class);
////                //contactsService.setUserCredentials(, mCredential.getSelectedAccount().);
////               // service.
////            } catch (Exception e) {
////                e.printStackTrace();
////                System.out.println("haoripaöd");
////            }
////            try {
////                queryEntries();
////                System.out.println("haoripaöd");
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
//
//            return null;
//        }
//
//    }
//    public GoogleCredential getCredential(ArrayList<String> scopes, String userEmail) throws Exception {
//        GoogleCredential credential = new GoogleCredential.Builder().setTransport(new NetHttpTransport()).setJsonFactory(new JacksonFactory())
//                .setServiceAccountId(this.serviceAccountEmail).setServiceAccountScopes(scopes).setServiceAccountPrivateKeyFromP12File(new java.io.File("xy.p12"))
//                .setServiceAccountUser(userEmail).build();
//        return credential;
//    }
//    private void queryEntries() throws IOException, ServiceException{
//        Query myQuery = new Query(feedUrl);
//        myQuery.setMaxResults(50);
//        myQuery.setStartIndex(1);
//        myQuery.setStringCustomParameter("showdeleted", "false");
//        myQuery.setStringCustomParameter("requirealldeleted", "false");
////      myQuery.setStringCustomParameter("sortorder", "ascending");
////      myQuery.setStringCustomParameter("orderby", "");
//
//
//        try{
//            ContactFeed resultFeed = (ContactFeed)this.service.query(myQuery, ContactFeed.class);
//            for (ContactEntry entry : resultFeed.getEntries()) {
//                printContact(entry);
//                System.out.println("hallooo");
//            }
//            System.out.println("hallooo");
//            System.err.println("Total: " + resultFeed.getEntries().size() + " entries found");
//
//        }
//        catch (NoLongerAvailableException ex) {
//            System.err.println("Not all placehorders of deleted entries are available");
//        }
//
//    }
//    private void printContact(ContactEntry contact) throws IOException, ServiceException{
//        System.err.println("Id: " + contact.getId());
//        if (contact.getTitle() != null)
//            System.out.println("Contact name: " + contact.getTitle().getPlainText());
//        else {
//            System.err.println("Contact has no name");
//        }
//
//        System.err.println("Last updated: " + contact.getUpdated().toUiString());
//        if (contact.hasDeleted()) {
//            System.err.println("Deleted:");
//        }
//
//        //      ElementHelper.printContact(System.err, contact);
//
//        Link photoLink = contact.getLink("http://schemas.google.com/contacts/2008/rel#photo", "image/*");
//        if (photoLink.getEtag() != null) {
//            Service.GDataRequest request = service.createLinkQueryRequest(photoLink);
//
//            request.execute();
//            InputStream in = request.getResponseStream();
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            RandomAccessFile file = new RandomAccessFile("/tmp/" + contact.getSelfLink().getHref().substring(contact.getSelfLink().getHref().lastIndexOf('/') + 1), "rw");
//
//            byte[] buffer = new byte[4096];
//            for (int read = 0; (read = in.read(buffer)) != -1; )
//                out.write(buffer, 0, read);
//            file.write(out.toByteArray());
//            file.close();
//            in.close();
//            request.end();
//        }
//
//        System.err.println("Photo link: " + photoLink.getHref());
//        String photoEtag = photoLink.getEtag();
//        System.err.println("  Photo ETag: " + (photoEtag != null ? photoEtag : "(No contact photo uploaded)"));
//
//        System.err.println("Self link: " + contact.getSelfLink().getHref());
//        System.err.println("Edit link: " + contact.getEditLink().getHref());
//        System.err.println("ETag: " + contact.getEtag());
//        System.err.println("-------------------------------------------\n");
//    }




    /////////////
    /****************************************************************************************************
     * Async task. Sends query to google drive api and populates mResult list with the returned
     * results.
     ****************************************************************************************************/
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {

        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Drive.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("CVSearch")
                    .build();

            //myCS = new ContactsService
//
//            try {
//
////                mCredential2 = new GoogleCredential.Builder()
////                        .setTransport(transport)
////                        .setJsonFactory(jsonFactory)
////                        .setServiceAccountId("cvsearch-146813@appspot.gserviceaccount.com")
////                        .setServiceAccountScopes(Arrays.asList( "https://www.google.com/m8/feeds"))
//
////                        .setServiceAccountPrivateKeyId("30e5873f9b936d8898343f52c9041b6bce2a4de4")
////                        .setServiceAccountUser(credential.getSelectedAccountName())
////                        .build();
//
////                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
////                keystore.load(this.getClass().getClassLoader().getResourceAsStream("CVSearch-fe7d49faf652.json"));
////                PrivateKey pk  = (PrivateKey)keystore.getKey("private_key", "notasecret".toCharArray());
////                      //  .createScoped(Arrays.asList( "https://www.google.com/m8/feeds"));
////                mCredential2 = new GoogleCredential.Builder()
////
////                        .setTransport(transport)
////                        .setJsonFactory(jsonFactory)
////                        .setServiceAccountId("jo-126@cvsearch-146813.iam.gserviceaccount.com")
////                        .setServiceAccountScopes(Arrays.asList( "https://www.google.com/m8/feeds/"))
////                        .setServiceAccountUser( credential.getSelectedAccountName())
////                        .setServiceAccountPrivateKey(pk)
////                        .build();
//
////                mCredential2= new GoogleCredential().
////                        fromStream(MainActivity.class.getResourceAsStream("/client_secret.json")).
////                        createScoped(Arrays.asList( "https://www.google.com/m8/feeds/"));
//
//               // myCS.setUserCredentials(credential.getSelectedAccountName(), CLIENT_SECRET);
//               // System.out.println(mCredential2.getServiceAccountUser());
////      //         mCredential2.refreshToken();
////                myCS.setAuthSubToken(credential.getToken());
////               // myCS.setOAuth2Credentials(mCredential2);
////
//////                myCS.setAuthSubToken(mCredential.getToken());
////                myCS.getRequestFactory().setHeader("User-Agent","CVSearch");
////                mCredential2.refreshToken();
//
////                myCS.setOAuth2Credentials(AuthorizationHelper.authorize());
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//            mPeople = new People.Builder(
//                    transport, jsonFactory, credential)
//                    .setApplicationName("CVSearch")
//                    .build();

        }


        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private List<String> getDataFromApi() throws IOException {
            //get list of 10 files
            List<String> fileInfo = new ArrayList<String>();
            // search in "AE CV" folder for the search phrase
            String[] s = mInput.split(" ");
            String s1 = "'0B6aeN6P9DrE1ODQ4ZTVlM2EtZmY5ZS00ZDVmLWEyZTEtMjg1MjMzMmE2NzZh' in " +
                    "parents";
            for (int i = 0; i < s.length; i++) {
                s1 += " and fullText contains '" + s[i] + "'";
            }
            FileList result = mService.files().list()
                    .setQ(s1)
                    .execute();

            try {
                myCS.setAuthSubToken(mCredential.getToken());
                myCS.getRequestFactory().setHeader("User-Agent","CVSearch");

                showContacts(myCS);
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }

            showContacts(myCS);


//            System.out.println("uj0099999");
//            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//
//            String APPLICATION_NAME = "CVSearch";
//            String SERVICE_ACCOUNT_EMAIL = "user@example.cm";// "my-service-account@developer.gserviceaccount.com";
//            java.io.File p12File = new java.io.File("CVSearch");//"MyProject.p12");
//            System.out.println("gggggggg");
//            GoogleCredential credential = new GoogleCredential.Builder()
//                    .setTransport(httpTransport)
//                    .setJsonFactory(jsonFactory)
//                    .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
//                    .setServiceAccountScopes(
//                            Collections.singleton("https://www.google.com/m8/feeds/"))
//                    .setServiceAccountPrivateKeyFromP12File(p12File)
//                   // .setServiceAccountUser("user@example.com")
//                    .build();
//            System.out.println("joooooo");
//            if (!credential.refreshToken()) {
//                throw new RuntimeException("Failed OAuth to refresh the token");
//            }
//
//            ContactsService service = new ContactsService(APPLICATION_NAME);
//            service.setOAuth2Credentials(credential);
//            System.out.println("hifwjpo");
//            Query gQuery = new Query(new java.net.URL("https://www.google.com/m8/feeds/groups/default/full"));
//            gQuery.setMaxResults(32767);
//            ContactGroupFeed groupFeed = service.query(gQuery, ContactGroupFeed.class);
//
//            for (ContactGroupEntry group : groupFeed.getEntries()) {
//                System.out.println("group: " + group.getTitle().getPlainText());
//
//                Query cQuery = new Query(new java.net.URL("https://www.google.com/m8/feeds/contacts/default/full"));
//                cQuery.setMaxResults(32767);
//                String grpId = group.getId();
//                cQuery.setStringCustomParameter("group", grpId);
//                ContactFeed feed = service.query(cQuery, ContactFeed.class);
//
//                for (ContactEntry contact : feed.getEntries()) {
//                    System.out.println("name: " + contact.getTitle().getPlainText());
//                }
//            }
            // ContactGroupEntry gro = retrieveContactGroup(myCS);
            // Request 10 connections.
//            ListConnectionsResponse response = mPeople.people().connections()
//                    .list("people/me/gr")
//                    .setPageSize(100)
//                    .execute();
//
//            // Print display name of connections if available.
//            List<Person> connections = response.getConnections();
//            if (connections != null && connections.size() > 0) {
//                for (Person person : connections) {
//                    List<Name> names = person.getNames();
//                    if (names != null && names.size() > 0) {
//                        System.out.println("Name: " + person.getNames().get(0)
//                                .getDisplayName());
//                    } else {
//                        System.out.println("No names available for connection.");
//                    }
//                }
//            } else {
//                System.out.println("No connections found.");
//
//            }
//            myCS = authenticateId();
//            System.out.println("cp06");
//            printAllContacts(myCS);
//            System.out.println("cp0555");
           // mResultList.setAdapter(null);
            mFiles = new ArrayList<>();

            List<File> files = result.getItems();
            if (files != null) {
                for (File file : files) {
                    fileInfo.add(String.format(
                            "%s", file.getTitle()));
                    mFiles.add(file);
                }
            }
            return fileInfo;
        }
        public void showContacts(ContactsService contactsService) {
            // Check the SDK version and whether the permission is already granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            } else {

                try {
                    printAllGroups(contactsService);
                    printAllContacts(contactsService, mCredential.getSelectedAccountName());
                } catch (Exception e){
                    System.out.println("oh no");
                    e.printStackTrace();
                }
            }
        }
        void printAllGroups(ContactsService contactsService)
                throws ServiceException, IOException {
            // Request the feed
            URL feedUrl = new URL("https://www.google.com/m8/feeds/groups/default/full");
            ContactGroupFeed resultFeed = contactsService.getFeed(feedUrl, ContactGroupFeed.class);

            for (ContactGroupEntry groupEntry : resultFeed.getEntries()) {
                System.out.println("Atom Id: " + groupEntry.getId());
                System.out.println("Group Name: " + groupEntry.getTitle().getPlainText());
                System.out.println("Last Updated: " + groupEntry.getUpdated());

                System.out.println("Extended Properties:");
                for (ExtendedProperty property : groupEntry.getExtendedProperties()) {
                    if (property.getValue() != null) {
                        System.out.println("  " + property.getName() + "(value) = " +
                                property.getValue());
                    } else if (property.getXmlBlob() != null) {
                        System.out.println("  " + property.getName() + "(xmlBlob) = " +
                                property.getXmlBlob().getBlob());
                    }
                }
                System.out.println("Self Link: " + groupEntry.getSelfLink().getHref());
                if (!groupEntry.hasSystemGroup()) {
                    // System groups do not have an edit link
                    System.out.println("Edit Link: " + groupEntry.getEditLink().getHref());
                    System.out.println("ETag: " + groupEntry.getEtag());
                }
                if (groupEntry.hasSystemGroup()) {
                    System.out.println("System Group Id: " +
                            groupEntry.getSystemGroup().getId());
                }
            }
        }

        public void printAllContacts(ContactsService myService, String myemail)
                throws ServiceException, IOException {
            // Request the feed
            myemail = "default";
            URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/" + myemail + "/full");
            ContactFeed resultFeed = myService.getFeed(feedUrl, ContactFeed.class);
            // Print the results
            System.out.println(resultFeed.getTitle().getPlainText());
            for (ContactEntry entry : resultFeed.getEntries()) {
                if (entry.hasName()) {
                    Name name = entry.getName();
                    if (name.hasFullName()) {
                        String fullNameToDisplay = name.getFullName().getValue();
                        if (name.getFullName().hasYomi()) {
                            fullNameToDisplay += " (" + name.getFullName().getYomi() + ")";
                        }
                        System.out.println("\t\t" + fullNameToDisplay);
                    } else {
                        System.out.println("\t\t (no full name found)");
                    }
                    if (name.hasNamePrefix()) {
                        System.out.println("\t\t" + name.getNamePrefix().getValue());
                    } else {
                        System.out.println("\t\t (no name prefix found)");
                    }
                    if (name.hasGivenName()) {
                        String givenNameToDisplay = name.getGivenName().getValue();
                        if (name.getGivenName().hasYomi()) {
                            givenNameToDisplay += " (" + name.getGivenName().getYomi() + ")";
                        }
                        System.out.println("\t\t" + givenNameToDisplay);
                    } else {
                        System.out.println("\t\t (no given name found)");
                    }
                    if (name.hasAdditionalName()) {
                        String additionalNameToDisplay = name.getAdditionalName().getValue();
                        if (name.getAdditionalName().hasYomi()) {
                            additionalNameToDisplay += " (" + name.getAdditionalName().getYomi() + ")";
                        }
                        System.out.println("\t\t" + additionalNameToDisplay);
                    } else {
                        System.out.println("\t\t (no additional name found)");
                    }
                    if (name.hasFamilyName()) {
                        String familyNameToDisplay = name.getFamilyName().getValue();
                        if (name.getFamilyName().hasYomi()) {
                            familyNameToDisplay += " (" + name.getFamilyName().getYomi() + ")";
                        }
                        System.out.println("\t\t" + familyNameToDisplay);
                    } else {
                        System.out.println("\t\t (no family name found)");
                    }
                    if (name.hasNameSuffix()) {
                        System.out.println("\t\t" + name.getNameSuffix().getValue());
                    } else {
                        System.out.println("\t\t (no name suffix found)");
                    }
                } else {
                    System.out.println("\t (no name found)");
                }
                System.out.println("Email addresses:");
                for (Email email : entry.getEmailAddresses()) {
                    System.out.print(" " + email.getAddress());
                    if (email.getRel() != null) {
                        System.out.print(" rel:" + email.getRel());
                    }
                    if (email.getLabel() != null) {
                        System.out.print(" label:" + email.getLabel());
                    }
                    if (email.getPrimary()) {
                        System.out.print(" (primary) ");
                    }
                    System.out.print("\n");
                }
                System.out.println("IM addresses:");
                for (Im im : entry.getImAddresses()) {
                    System.out.print(" " + im.getAddress());
                    if (im.getLabel() != null) {
                        System.out.print(" label:" + im.getLabel());
                    }
                    if (im.getRel() != null) {
                        System.out.print(" rel:" + im.getRel());
                    }
                    if (im.getProtocol() != null) {
                        System.out.print(" protocol:" + im.getProtocol());
                    }
                    if (im.getPrimary()) {
                        System.out.print(" (primary) ");
                    }
                    System.out.print("\n");
                }
                System.out.println("Groups:");
                for (GroupMembershipInfo group : entry.getGroupMembershipInfos()) {
                    String groupHref = group.getHref();
                    System.out.println("  Id: " + groupHref);
                }
                System.out.println("Extended Properties:");
                for (ExtendedProperty property : entry.getExtendedProperties()) {
                    if (property.getValue() != null) {
                        System.out.println("  " + property.getName() + "(value) = " +
                                property.getValue());
                    } else if (property.getXmlBlob() != null) {
                        System.out.println("  " + property.getName() + "(xmlBlob)= " +
                                property.getXmlBlob().getBlob());
                    }
                }
                Link photoLink = entry.getContactPhotoLink();
                String photoLinkHref = photoLink.getHref();
                System.out.println("Photo Link: " + photoLinkHref);
                if (photoLink.getEtag() != null) {
                    System.out.println("Contact Photo's ETag: " + photoLink.getEtag());
                }
                System.out.println("Contact's ETag: " + entry.getEtag());
            }
        }
//        private List<String> getContactNames() {
//            List<String> contacts = new ArrayList<>();
//            // Get the ContentResolver
//            ContentResolver cr = getContentResolver();
//            // Get the Cursor of all the contacts
//            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//            //getContentResolver().query(ContactsContract.Groups.CONTENT_URI,
////                null,
////                ContactsContract.Groups.ACCOUNT_NAME + "=? AND " +
////                        ContactsContract.Groups.ACCOUNT_TYPE + "=? AND " +
////                        ContactsContract.Groups.DELETED + "=?",
////                null,
////                ContactsContract.Groups._ID);
//            // Move the cursor to first. Also check whether the cursor is empty or not.
//            if (cursor.moveToFirst()) {
//                // Iterate through the cursor
//                do {
//                    // Get the contacts name
//                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                    contacts.add(name);
//                } while (cursor.moveToNext());
//            }
//            // Close the curosor
//            cursor.close();
//
//            return contacts;
//        }
//
//        void getlistofcontacts( long groupId ) {
//            String[] cProjection = { ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID };
//
//            Cursor groupCursor = getContentResolver().query(
//                    ContactsContract.Data.CONTENT_URI,
//                    cProjection,
//                    ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
//                            + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
//                            + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
//                    new String[] { String.valueOf(groupId) }, null);
//            if (groupCursor != null && groupCursor.moveToFirst())
//            {
//                //Toast.makeText(this,"if",1000).show();
//                do
//                {
//
//                    int nameCoumnIndex = groupCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//
//                    String name = groupCursor.getString(nameCoumnIndex);
//
//                    long contactId = groupCursor.getLong(groupCursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));
//
//                    Cursor numberCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                            new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER }, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
//
//                    if (numberCursor.moveToFirst())
//                    {
//                        int numberColumnIndex = numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//                        do
//                        {
//                            String phoneNumber = numberCursor.getString(numberColumnIndex);
//
//                            System.out.println(name+phoneNumber);
//                        } while (numberCursor.moveToNext());
//                        numberCursor.close();
//                    }
//                    else
//                    {
//                        System.out.println("no contact are there");
//                    }
//                } while (groupCursor.moveToNext());
//                groupCursor.close();
//            }
//            else
//            {
//                System.out.println("no such group exists");
//            }
//        }
        @Override
        protected void onPreExecute() {
            mOutputText.setText("DFGHJKL");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
          //  mResultList.setAdapter(null);
            if (output == null || output.size() == 0)
                mOutputText.setText("No results found for '" + mInput + "'");
            else {
                mOutputText.setText("Returned with results for '" + mInput + "'");
                System.out.println("hrpwshresdbnw");
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                        getApplicationContext(), R.layout.list_black_text,TODO: uncomment this.... just testing
//                        output);
                //mResultList.setAdapter(null);
                //mResultList.setAdapter(adapter); TODO: uncomment this.... just testing
                //output.add(0,"Data retrieved using the Drive API");
                //mOutputText.setText(TextUtils.join("\n",output));
//                Uri uri = ContactsContract.Contacts.CONTENT_URI;
//                String[] projection = new String[] { ContactsContract.Contacts._ID,
//                        ContactsContract.Contacts.DISPLAY_NAME };
//                String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
//                        + ("1") + "'";
//                String[] selectionArgs = null;
//                String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
//                        + " COLLATE LOCALIZED ASC";
//
//
//                Cursor mCursor = getContentResolver().query(uri, null, null, null,
//                        null);
//                //startManagingCursor(mCursor);
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                        getApplicationContext(), R.layout.list_black_text,
//                        mCursor.getColumnNames());
//                // now create a new list adapter bound to the cursor.
//                // SimpleListAdapter is designed for binding to a Cursor.
////                ListAdapter adapter = new SimpleCursorAdapter(this, // Context.
////                        R.layout.list_black_text, // Specify the row template
////                        // to use (here, two
////                        // columns bound to the
////                        // two retrieved cursor
////                        // rows).
////                        mCursor, // Pass in the cursor to bind to.
////                        // Array of cursor columns to bind to.
////                        new String[] { ContactsContract.Contacts._ID,
////                                ContactsContract.Contacts.DISPLAY_NAME },
////                        // Parallel array of which template objects to bind to those
////                        // columns.
////                        new int[] { android.R.id.text1, android.R.id.text2 });
//
//                // Bind to our new adapter.
//                //setListAdapter(adapter);
//
//                mResultList.setAdapter(adapter);
//                mCursor.close();
            }

        }

        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException)
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException)
                                    mLastError).getConnectionStatusCode());
                else if (mLastError instanceof UserRecoverableAuthIOException)
                    startActivityForResult(((
                                    UserRecoverableAuthIOException) mLastError).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                else
                    System.out.println("The following error occured:\n" +
                            mLastError.getMessage());
            } else
                mOutputText.setText("Request Cancelled");
            System.out.println("cancell");
        }
    }
}


