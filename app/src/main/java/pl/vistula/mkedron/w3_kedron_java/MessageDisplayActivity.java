package pl.vistula.mkedron.w3_kedron_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static pl.vistula.mkedron.w3_kedron_java.Consts.SOURCE_KEY;

public class MessageDisplayActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "pl.vistula.mkedron.w3_kedron_java";
    public static final String SOURCE = "MESSAGE_DISPLAY_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_display);

        String extraMessage = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);

        EditText edit = (EditText) findViewById(R.id.messageText);
        edit.setText(extraMessage);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        EditText editText = (EditText) findViewById(R.id.messageText);
        intent.putExtra(EXTRA_MESSAGE, editText.getText().toString());
        intent.putExtra(SOURCE_KEY, SOURCE);
        startActivity(intent);
    }
}
