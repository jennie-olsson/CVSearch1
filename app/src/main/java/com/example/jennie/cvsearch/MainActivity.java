package com.example.jennie.cvsearch;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button mSearchButton;       //well this should be rather self documenting :)
    private EditText mTextInput;        //User input field
    private static final String BUTTON_TEXT = "Search";
    private static final String TEXT_INPUT_MSG ="Enter search parameter(s) here";
    private String mInput;              //Current user input, this could be handled locally
    private TextView mOutputText;       //Used for status echo to user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mTextInput = (EditText)findViewById(R.id.textInput);
        mTextInput.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            /****************************************************************************************************
             *
             * @param TextView v    - Points to the user input text field
             * @param int actionId  - Not used
             * @param KeyEvent event - Test for enter down
             * @return bool         - not used
             *
             * Runs when user presses enter on the keyboard. User could also press the search button
             * Hides the keyboard after enter.
             ****************************************************************************************************/
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                boolean handled = false;
                if(event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    mInput = v.getText().toString();
                    //echo to user (TODO remove from production)
                    Toast.makeText(getApplicationContext(),mInput,Toast.LENGTH_LONG)
                            .show();
                    v.setText("");
                    //hides keyboard
                    InputMethodManager imm = (InputMethodManager)
                            getApplicationContext().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    //init query
                    //getResultFromApi();
                    handled = true;

                }

                return handled;
            }
        });

        mSearchButton = (Button)findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener(){
            /****************************************************************************************************
             * @param View v
             * Pretty much same as the text input callback
             ****************************************************************************************************/
            @Override
            public void onClick(View v){
                mSearchButton.setEnabled(false);
                mInput = mTextInput.getText().toString();
                mTextInput.setText("");
                mOutputText.setText("");
                //getResultFromApi();
                mSearchButton.setEnabled(true);
            }
        });
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
}
