package mad.sliit.memo;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mad.sliit.memo.db.dbAccess;

public class EditActivity extends ActionBarActivity {


    private EditText edit_Text;

    private Button Save_button;

    private Button Cancel_button;

    private Notes noteActivity;
    //------------------------------------------------------------------------------/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        this.edit_Text = (EditText) findViewById(R.id.etText);

        this.Save_button = (Button) findViewById(R.id.btnSave);

        this.Cancel_button = (Button) findViewById(R.id.btnCancel);



        //------------------------------------------------------------------------------/
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            noteActivity = (Notes) bundle.get("NOTE");
            if(noteActivity != null) {
                this.edit_Text.setText(noteActivity.getText());
            }
        }

        //------------------------------------------------------------------------------/
        this.Save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });


        //------------------------------------------------------------------------------/
        this.Cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder adb=new AlertDialog.Builder(EditActivity.this);

                final AlertDialog.Builder builder = adb.setMessage(" Are you sure you want to cancel?")
                        .setTitle("Cancel")
                        .setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setCancelable(false)
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onCancelClicked();
                            }
                        });
                AlertDialog alertDialog=adb.create();
                alertDialog.show();


            }
        });
    }
    //------------------------------------------------------------------------------/
    public void onSaveClicked() {
        dbAccess databaseAccess = dbAccess.getInstance(this);
        databaseAccess.open();
        if(noteActivity == null) {
            // Add new memo
            Notes temp = new Notes();
            temp.setText(edit_Text.getText().toString());
            databaseAccess.save(temp);
        } else {
            // Update the memo
            noteActivity.setText(edit_Text.getText().toString());
            databaseAccess.update(noteActivity);
        }
        databaseAccess.close();
        this.finish();
    }



    public void onCancelClicked() {
        this.finish();
    }
}
