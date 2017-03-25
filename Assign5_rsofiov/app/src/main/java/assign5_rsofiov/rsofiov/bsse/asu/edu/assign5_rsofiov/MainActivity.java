package assign5_rsofiov.rsofiov.bsse.asu.edu.assign5_rsofiov;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        DialogInterface.OnClickListener, TextView.OnEditorActionListener {

    public EditText url, nameP, description, category, ele, lon, lat, addressTitle, addressStreet;
    public Spinner spinner;

    public String[] names;

    public ArrayAdapter<String> adapter;
    private String selectedStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = (EditText) findViewById(R.id.url);
        url.setOnEditorActionListener(this);
        nameP = (EditText) findViewById(R.id.nameP);
        nameP.setOnEditorActionListener(this);
        description = (EditText) findViewById(R.id.description);
        description.setOnEditorActionListener(this);
        ele = (EditText) findViewById(R.id.ele);
        ele.setOnEditorActionListener(this);
        lon = (EditText) findViewById(R.id.lon);
        lon.setOnEditorActionListener(this);
        lat = (EditText) findViewById(R.id.lat);
        lat.setOnEditorActionListener(this);
        addressTitle = (EditText) findViewById(R.id.addressTitle);
        addressTitle.setOnEditorActionListener(this);
        addressStreet = (EditText) findViewById(R.id.addressStreet);
        addressStreet.setOnEditorActionListener(this);
        category = (EditText) findViewById(R.id.category);
        category.setOnEditorActionListener(this);
        try {
            JsonRPCRequestViaHttp names = new JsonRPCRequestViaHttp(new URL(url.getText().toString()), this);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception constructing URL" +
                    url.getText().toString() + " message " + ex.getMessage());
        }
        names = new String[]{"unknown"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(names)));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        try {
            MethodInformation mi = new MethodInformation(this, url.getText().toString(), "getNames",
                    new String[]{});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                    ex.getMessage());
        }

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d(this.getClass().getSimpleName(), "in onClick");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedStudent = spinner.getSelectedItem().toString();
        android.util.Log.d(this.getClass().getSimpleName(), "spinner item selected " + selectedStudent);
        try {
            MethodInformation mi = new MethodInformation(this, url.getText().toString(), "get",
                    new String[]{selectedStudent});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception processing spinner selection: " +
                    ex.getMessage());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // public boolean onEditorAction(TextView v, int actionId, KeyEvent event){


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        android.util.Log.d(this.getClass().getSimpleName(), "onEditorAction: keycode " +
                ((keyEvent == null) ? "null" : keyEvent.toString()) + " actionId " + i);
        if (i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_DONE) {
            android.util.Log.d(this.getClass().getSimpleName(), "entry is: " + textView.getText().toString());
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(this.getClass().getSimpleName(), "in onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        setTitle("Places");
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Log.d(this.getClass().getSimpleName(), "in onOptionsItemSelected: menu_add");
                this.addPlace();
                break;
            case R.id.menu_remove:
                //   Log.d(this.getClass().getSimpleName(), "in onOptionsItemSelected: menu_remove");
                this.removePlace();
                break;
            default:
                break;
        }
        return true;
    }


    //////////CHANGE NAME OF FUNCTION !!!!!!!!!!!!
    public void addPlace() {
        Place newPlace = new Place();
        newPlace.nameP = this.nameP.getText().toString();
        newPlace.category = this.category.getText().toString();
        newPlace.description = this.description.getText().toString();
        newPlace.addressStreet = this.addressStreet.getText().toString();
        newPlace.addressTitle = this.addressTitle.getText().toString();
        newPlace.ele = Double.parseDouble(this.lon.getText().toString());
        newPlace.lat = Double.parseDouble(this.lat.getText().toString());
        newPlace.lon = Double.parseDouble(this.ele.getText().toString());

        newPlace.toJson();
        try {
            MethodInformation mi = new MethodInformation(this, url.getText().toString(), "add",
                    new Object[]{newPlace.toJson()});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                    ex.getMessage());
        }
    }


    public void removePlace() {
        Place removeP = new Place();
        removeP.nameP = this.nameP.getText().toString();
        removeP.category = this.category.getText().toString();
        removeP.description = this.description.getText().toString();
        removeP.addressStreet = this.addressStreet.getText().toString();
        removeP.addressTitle = this.addressTitle.getText().toString();
        removeP.ele = Double.parseDouble(this.lon.getText().toString());
        removeP.lat = Double.parseDouble(this.lat.getText().toString());
        removeP.lon = Double.parseDouble(this.ele.getText().toString());

        removeP.toJson();
        try {
            MethodInformation mi = new MethodInformation(this, url.getText().toString(), "remove",
                    new Object[]{removeP.toJson()});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                    ex.getMessage());
        }
    }


    public void refreshClicked(View v) {
        Log.d(this.getClass().getSimpleName(), "refreshClicked");
        try {
            MethodInformation mi = new MethodInformation(this, url.getText().toString(), "getNames",
                    new String[]{});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                    ex.getMessage());
        }
    }


}