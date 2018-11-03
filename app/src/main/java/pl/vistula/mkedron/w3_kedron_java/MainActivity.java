package pl.vistula.mkedron.w3_kedron_java;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import android.provider.ContactsContract.CommonDataKinds.*;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static pl.vistula.mkedron.w3_kedron_java.Consts.SOURCE_KEY;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "pl.vistula.mkedron.MESSAGE";
    public static final String SOURCE = "MAIN_ACTIVITY";

    static String TAG = "MapLocationWpis";
    private static final int PICK_CONTACT_REQUEST = 0;

    private RadioButton messageRadioButton;
    private RadioButton resultRadioButton;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String extraMessage = "";

        final String source = getIntent().getStringExtra(SOURCE_KEY);
        if(MessageDisplayActivity.SOURCE.equals(source)) {
            extraMessage = getIntent().getStringExtra(MessageDisplayActivity.EXTRA_MESSAGE);
        } else if(ResultActivity.SOURCE.equals(source)) {
            extraMessage = getIntent().getStringExtra(ResultActivity.EXTRA_MESSAGE);
        }
        EditText editText = (EditText) findViewById(R.id.dataText);
        editText.setText(extraMessage);

        messageRadioButton = (RadioButton) findViewById(R.id.messageRadioButton);
        resultRadioButton = (RadioButton) findViewById(R.id.resultRadioButton);

        prepareSpinner();

    }

    private void prepareSpinner() {
        spinner = (Spinner) findViewById(R.id.nextViewSpinner);

        ArrayList<String> spinnerEntries = new ArrayList<>();
        spinnerEntries.add(getString(R.string.go_to_message));
        spinnerEntries.add(getString(R.string.go_to_result));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,spinnerEntries);
        spinner.setAdapter(arrayAdapter);

    }


    public void sendMessage(View view) {
        Intent intent = null;

//        if(messageRadioButton.isChecked()) {
//            intent = new Intent(this, MessageDisplayActivity.class);
//        } else if(resultRadioButton.isChecked()) {
//            intent = new Intent(this, ResultActivity.class);
//        } else {
//            Toast.makeText(this, R.string.choose_destination, Toast.LENGTH_SHORT).show();
//            return;
//        }

        if(spinner.getSelectedItem().equals(getString(R.string.go_to_message))) {
            intent = new Intent(this, MessageDisplayActivity.class);
        } else if (spinner.getSelectedItem().equals(getString(R.string.go_to_result))) {
            intent = new Intent(this, ResultActivity.class);
        } else {
            Toast.makeText(this, R.string.choose_destination, Toast.LENGTH_SHORT).show();
            return;
        }
        EditText editText = (EditText) findViewById(R.id.dataText);
        intent.putExtra(EXTRA_MESSAGE, editText.getText().toString());
        intent.putExtra(SOURCE_KEY, SOURCE);
        startActivity(intent);
    }

    public void findContactListener(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_CONTACT_REQUEST) {
            ContentResolver cr = getContentResolver();
            Cursor cursor = cr.query(data.getData(), null, null, null, null);
            if(cursor != null && cursor.moveToFirst()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Uri postal_uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                Cursor postal_cursor  = getContentResolver().query(postal_uri,null,  ContactsContract.Data.CONTACT_ID + "="+id.toString(), null,null);
                while(postal_cursor.moveToNext())
                {
                    String street = postal_cursor.getString(postal_cursor.getColumnIndex(StructuredPostal.STREET));
                    String city = postal_cursor.getString(postal_cursor.getColumnIndex(StructuredPostal.CITY));
                    String contry = postal_cursor.getString(postal_cursor.getColumnIndex(StructuredPostal.COUNTRY));

                    System.out.println(String.format("street = %s, city = %s, country = %s", street,city,contry));

                    Uri gmmIntentUri = Uri.parse("geo:0,0?q="+street+" "+city);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
                postal_cursor.close();
            }
        }
    }

}
