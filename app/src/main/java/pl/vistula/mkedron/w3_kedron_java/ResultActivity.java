package pl.vistula.mkedron.w3_kedron_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static pl.vistula.mkedron.w3_kedron_java.Consts.SOURCE_KEY;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "pl.vistula.mkedron.w3_kedron_java";
    public static final String SOURCE = "RESULT_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String extraMessage = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);

        EditText edit = (EditText) findViewById(R.id.resultText);
        edit.setText(extraMessage);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        EditText editText = (EditText) findViewById(R.id.resultText);
        intent.putExtra(EXTRA_MESSAGE, editText.getText().toString());
        intent.putExtra(SOURCE_KEY, SOURCE);
        startActivity(intent);
    }
}
