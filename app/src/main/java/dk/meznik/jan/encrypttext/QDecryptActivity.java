package dk.meznik.jan.encrypttext;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import dk.meznik.jan.encrypttext.util.Encryption;

public class QDecryptActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getAction().equals(Intent.ACTION_PROCESS_TEXT)) {
            ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            CharSequence str = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
            String plain = "";
            try {
                plain = Encryption.decryptDefault(str.toString());
            } catch (Exception ex) {
                Toast.makeText(this, "Could not encrypt text: " + ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            if (plain.contains(MainActivity.version)) {
                plain = plain.substring(MainActivity.version.length());
            }
            else {
                plain = "";
            }
            ClipData clipData = ClipData.newPlainText("Text",plain );
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            exitWithResult(plain);
        }
    }
    public void exitWithResult(String data) {

        Intent result = new Intent();
        result.putExtra(Intent.EXTRA_PROCESS_TEXT, data);
        setResult(RESULT_OK, result);
        finish();
    }
}
