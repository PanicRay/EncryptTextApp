package dk.meznik.jan.encrypttext;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import dk.meznik.jan.encrypttext.util.Encryption;

public class QEncryptActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getAction().equals(Intent.ACTION_PROCESS_TEXT)) {
            CharSequence str = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
            String cipher = "";
            try {
                cipher = Encryption.encryptDefault(MainActivity.version+str.toString());
            } catch (Exception ex) {
                Toast.makeText(this, "Could not encrypt text: " + ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            exitWithResult(cipher);
        }
    }
    public void exitWithResult(String data) {
        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Text",data );
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        Intent result = new Intent();
        result.putExtra(Intent.EXTRA_PROCESS_TEXT, data);
        setResult(RESULT_OK, result);
        finish();
    }
}
