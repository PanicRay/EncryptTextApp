package dk.meznik.jan.encrypttext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    public static String version = "ver1.0"; // Warning, different cannot talk to each other!
    public static int selection = 0;
    private Switch useDefault = (Switch)findViewById(R.id.switch2);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, EncryptActivity.class);
        startActivity(intent);
        finish();
    }


}
