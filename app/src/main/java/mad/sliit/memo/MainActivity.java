package mad.sliit.memo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import mad.sliit.memo.db.dbAccess;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ListView listView;
    private Button btnAdd;

    private dbAccess databaseAccess;
    private List<Notes> noteActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.databaseAccess = dbAccess.getInstance(this);

        this.listView = (ListView) findViewById(R.id.listView);
        this.btnAdd = (Button) findViewById(R.id.btnAdd);

        this.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClicked();
            }
        });

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notes note = noteActivity.get(position);
                TextView txtMemo = (TextView) view.findViewById(R.id.txtMemo);
                if (note.isFullDisplayed()) {
                    txtMemo.setText(note.getShortText());
                    note.setFullDisplayed(false);
                } else {
                    txtMemo.setText(note.getText());
                    note.setFullDisplayed(true);
                }
            }
        });
    }



    //------------------------------------------------------------------------------/
    @Override
    protected void onResume() {
        super.onResume();
        databaseAccess.open();
        this.noteActivity = databaseAccess.getAllNotes();
        databaseAccess.close();
        MemoAdapter adapter = new MemoAdapter(this, noteActivity);
        this.listView.setAdapter(adapter);
    }


    //------------------------------------------------------------------------------/
    public void onAddClicked() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }


    //------------------------------------------------------------------------------/
    public void onDeleteClicked(Notes memo) {
        databaseAccess.open();
        databaseAccess.delete(memo);
        databaseAccess.close();

        ArrayAdapter<Notes> adapter = (ArrayAdapter<Notes>) listView.getAdapter();
        adapter.remove(memo);
        adapter.notifyDataSetChanged();
    }


    //------------------------------------------------------------------------------/
    public void onEditClicked(Notes note) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("NOTE", note);
        startActivity(intent);
    }


    //------------------------------------------------------------------------------/
    private class MemoAdapter extends ArrayAdapter<Notes> {


        public MemoAdapter(Context context, List<Notes> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_list_item, parent, false);
            }

            ImageView btnEdit = (ImageView) convertView.findViewById(R.id.btnEdit);
            ImageView btnDelete = (ImageView) convertView.findViewById(R.id.btnDelete);
            TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            TextView txtMemo = (TextView) convertView.findViewById(R.id.txtMemo);

            final Notes memo = noteActivity.get(position);
            memo.setFullDisplayed(false);
            txtDate.setText(memo.getDate());
            txtMemo.setText(memo.getShortText());


            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClicked(memo);
                }
            });


            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);

                    final AlertDialog.Builder builder = adb.setMessage(" Are you sure you want to send the note to the trash can?")
                            .setTitle("Delete")
                            .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setCancelable(false)
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onDeleteClicked(memo);
                                }
                            });
                    AlertDialog alertDialog=adb.create();
                    alertDialog.show();
                }
            });
            return convertView;
        }
    }
}