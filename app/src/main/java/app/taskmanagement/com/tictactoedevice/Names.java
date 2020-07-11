package app.taskmanagement.com.tictactoedevice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Names extends AppCompatActivity {

     Button btnPlay;

     public static TextView txtScore1, txtScore2;

    // public static EditText edtName1, edtName2;

     static int counter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);


        btnPlay = (Button) findViewById(R.id.btnPlay);

       // edtName1 =(EditText) findViewById(R.id.edtName1);
       // edtName2 = (EditText) findViewById(R.id.edtName2);


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Names.this, MainActivity.class));
                finish();
            }
        });

    }

    public Button getBtnPlay() {
        return btnPlay;
    }

}
