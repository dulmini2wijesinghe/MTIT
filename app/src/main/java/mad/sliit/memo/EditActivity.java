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

    //for edit existing notes
    private EditText edit_text;
    private Button Save;
    private Button Cancel;
    private Notes noteActivity;

    @Override
    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        this.edit_text = (EditText)findViewById(R.id.etText);
        this.Save = (Button) findViewById(R.id.btnSave);
        this.Cancel = (Button) findViewById(R.id.btnCancel);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            noteActivity = (Notes)bundle.get("note");

            if(noteActivity !=null){

                this.edit_text.setText(noteActivity.);
            }

        }

        this.Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onSaveClicked();
            }
        });

        this.Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder ad= new AlertDialog(EditActivity.this);
                final AlertDialog.Builder builder = ad.setMessage("Are you sure you want to cancel?").setTitle(""Cancel).setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                }).setCancelable(false).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        onCancelClicked();

                    }
                });

                AlertDialog alertDialog = ad.create();

                alertDialog.show();
            }
        });
    }

    public void onSaveClicked(){

        dbAccess databaseAcc= dbAccess.getInstance(this);
        databaseAcc.getInstance(this);
        databaseAcc.open();

        if(noteActivity==null){

            Notes temp = new Notes();
            temp.setText(edit_text.getText().toString());

            databaseAcc.save(temp);

        }

        else{

            noteActivity.setText(edit_text.getText().toString();
            databaseAcc.update(noteActivity);


        }

        databaseAcc.close();
        this.finish();
    }

    public void onCancelClicked(){

        this.finish();
    }
}
