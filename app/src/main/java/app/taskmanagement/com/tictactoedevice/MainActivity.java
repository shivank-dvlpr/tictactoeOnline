package app.taskmanagement.com.tictactoedevice;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button btnSelected, btnReset, btnInvite, btnAccept, btnLogin;

    TextView txtName1, txtName2, txtScore1, txtScore2, txtSymbol;

    String userID;

    EditText edtInviteAccept, edtLogin;

    int cellID;

    Names names = new Names();
    String myEmail;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    int activePlayer = 1; // 1 is for 1st Player , 2 is for 2nd Player
    ArrayList<Integer> player1 = new ArrayList<>(); //  Player 1 Data
    ArrayList<Integer> player2 = new ArrayList<>(); // Player 2 Data
    String playerSession = "";
    String mySample = "X";
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        btnReset = findViewById(R.id.btnReset);


        edtLogin = findViewById(R.id.edtLogin);

        txtSymbol = findViewById(R.id.txtSymbol);

        btnLogin = findViewById(R.id.btnLogin);

        edtInviteAccept = findViewById(R.id.edtInviteAccept);


        // txtName1 = (TextView) findViewById(R.id.txtName1);
        // txtName2 =(TextView) findViewById(R.id.txtName2);

        //txtName1.setText(Names.edtName1.getText().toString() + " --> 'X'");
        // txtName2.setText(Names.edtName2.getText().toString() + " --> 'O'");


        // Firebase
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    userID = user.getUid();
                    myEmail = user.getEmail();
                    btnLogin.setEnabled(false);
                    edtLogin.setText(myEmail);
                    myRef.child("New Users").child(ExactName(myEmail)).child("Request").setValue(userID);
                    GettingRequest();

                } else {
                    Log.d("TAG ", "Null user!");
                }
            }
        };

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private String ExactName(String string) {

        String[] exactName = string.split("@");

        return exactName[0];

    }

    private void playGame(int cellID, Button btnSelected) {
        Log.d("Player: ", String.valueOf(cellID));

        if (activePlayer == 1) {
            btnSelected.setText("X");
            btnSelected.setBackgroundColor(Color.YELLOW);
            btnSelected.setTextColor(Color.BLACK);
            player1.add(cellID);
            activePlayer = 2;

           /* new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    autoPlay();
                }
            },500);  auto play activated*/
        } else if (activePlayer == 2) {
            btnSelected.setText("O");
            btnSelected.setBackgroundColor(Color.BLACK);
            btnSelected.setTextColor(Color.YELLOW);
            player2.add(cellID);
            activePlayer = 1;
        }

        btnSelected.setEnabled(false);
        checkWinner();

    }

    private void checkWinner() {

        int winner = 0;

        // row 1

        if (player1.contains(1) && player1.contains(2) && player1.contains(3)) {
            winner = 1;
        }

        if (player2.contains(1) && player2.contains(2) && player2.contains(3)) {
            winner = 2;
        }


        // row 2

        if (player1.contains(4) && player1.contains(5) && player1.contains(6)) {
            winner = 1;
        }

        if (player2.contains(4) && player2.contains(5) && player2.contains(6)) {
            winner = 2;
        }


        // row 3

        if (player1.contains(7) && player1.contains(8) && player1.contains(9)) {
            winner = 1;
        }

        if (player2.contains(7) && player2.contains(8) && player2.contains(9)) {
            winner = 2;
        }


        // column 1

        if (player1.contains(1) && player1.contains(4) && player1.contains(7)) {
            winner = 1;
        }

        if (player2.contains(1) && player2.contains(4) && player2.contains(7)) {
            winner = 2;
        }


        // column 2

        if (player1.contains(2) && player1.contains(5) && player1.contains(8)) {
            winner = 1;
        }

        if (player2.contains(2) && player2.contains(5) && player2.contains(8)) {
            winner = 2;
        }


        // column 3

        if (player1.contains(3) && player1.contains(6) && player1.contains(9)) {
            winner = 1;
        }

        if (player2.contains(3) && player2.contains(6) && player2.contains(9)) {
            winner = 2;
        }


        // diagonal left

        if (player1.contains(1) && player1.contains(5) && player1.contains(9)) {
            winner = 1;
        }

        if (player2.contains(1) && player2.contains(5) && player2.contains(9)) {
            winner = 2;
        }

        // diagonal right

        if (player1.contains(3) && player1.contains(5) && player1.contains(7)) {
            winner = 1;
        }

        if (player2.contains(3) && player2.contains(5) && player2.contains(7)) {
            winner = 2;
        }


        if (winner != 0) {

            if (winner == 1) {
                // Toast.makeText(this, "Player 1 is Winner", Toast.LENGTH_LONG).show();

                //textView = findViewById(R.id.textview);
                //  textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                activePlayer = 0;

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("X win's")
                        .setMessage("Do you want to Play Again?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false);
                AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.show();
                Names.counter = 0;

            }


            if (winner == 2) {
                // Toast.makeText(this, "Player 2 is Winner", Toast.LENGTH_LONG).show();

                activePlayer = 0;


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("0 win's")
                        .setMessage("Do you want to Play Again?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false);
                AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.show();

            }

        }

    }

    private void autoPlay(int cellID) {

       /* ArrayList<Integer> emptyCell = new ArrayList<>();

        for (int i=1; i < 10; i++){

            if (!(player1.contains(i) || player2.contains(i))){ // checking for empty boxes
                emptyCell.add(i);
            }

        }

        try {
            Random random = new Random();
            int randomIndex = random.nextInt(emptyCell.size()); // in any random empty box
            cellID = emptyCell.get(randomIndex);
        }catch (IllegalArgumentException e){
            Log.e("TAG", "Error" + e);
        }*/


        Button btnselected1;

        switch (cellID) {

            case 1:
                btnselected1 = findViewById(R.id.btn1);
                break;

            case 2:
                btnselected1 = findViewById(R.id.btn2);
                break;

            case 3:
                btnselected1 = findViewById(R.id.btn3);
                break;

            case 4:
                btnselected1 = findViewById(R.id.btn4);
                break;

            case 5:
                btnselected1 = findViewById(R.id.btn5);
                break;

            case 6:
                btnselected1 = findViewById(R.id.btn6);
                break;

            case 7:
                btnselected1 = findViewById(R.id.btn7);
                break;

            case 8:
                btnselected1 = findViewById(R.id.btn8);
                break;

            case 9:
                btnselected1 = findViewById(R.id.btn9);
                break;

            default:
                btnselected1 = findViewById(R.id.btn2); // never happens
                break;

        }

        playGame(cellID, btnselected1);

    }

    private void NewUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Successful Login", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login Failed", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void btnLogin(View view) {

        NewUser(edtLogin.getText().toString(), "1234567");

    }

    public void btnInvite(View view) {

        myRef.child("New Users").child(ExactName(edtInviteAccept.getText().toString())).child("Request").push().setValue(myEmail);

        StartGame(ExactName(edtInviteAccept.getText().toString()) + ":" + ExactName(myEmail));

        mySample = "X";

        txtSymbol.setText("You have 'O'");


    }

    public void btnAccept(View view) {

        myRef.child("New Users").child(ExactName(edtInviteAccept.getText().toString())).child("Request").push().setValue(myEmail);

        StartGame(ExactName(myEmail) + ":" + ExactName(edtInviteAccept.getText().toString()));

        mySample = "O";

        txtSymbol.setText("You Have 'X'");

    }

    private void StartGame(String playerGameID) {

        playerSession = playerGameID;
        myRef.child("Playing").child(playerGameID).removeValue();

        myRef.child("Playing").child(playerGameID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    player1.clear();
                    player2.clear();
                    activePlayer = 2;
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    if (hashMap != null) {

                        String value;

                        for (String key : hashMap.keySet()) {
                            value = (String) hashMap.get(key);
                            if (!value.equals(ExactName(myEmail)))
                                activePlayer = mySample == "X" ? 1 : 2;
                            else
                                activePlayer = mySample == "X" ? 2 : 1;


                            String[] splitID = key.split(":");
                            autoPlay(Integer.parseInt(splitID[1]));

                        }

                    }

                } catch (Exception e) {
                    Log.e("TAG", "Error " + e);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void GettingRequest() {

        myRef.child("New Users").child(ExactName(myEmail)).child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    if (hashMap != null) {

                        String value;

                        for (String key : hashMap.keySet()) {
                            value = (String) hashMap.get(key);
                            edtInviteAccept.setText(value);
                            changeColor(edtInviteAccept); // changing the background color of edit text when someone sents invitation
                            myRef.child("New Users").child(ExactName(myEmail)).child("Request").setValue(userID);
                            break;
                        }

                    }

                } catch (Exception e) {
                    Log.e("TAG", "Error " + e);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void changeColor(EditText editText) {

        editText.setBackgroundColor(Color.GREEN);

    }

    public void btnClick(View view) {

        if (playerSession.length() <= 0) {
            return;
        }


        btnSelected = (Button) view;

        int cellID = 0;

        switch ((btnSelected.getId())) {

            case R.id.btn1:
                cellID = 1;
                break;

            case R.id.btn2:
                cellID = 2;
                break;

            case R.id.btn3:
                cellID = 3;
                break;

            case R.id.btn4:
                cellID = 4;
                break;

            case R.id.btn5:
                cellID = 5;
                break;

            case R.id.btn6:
                cellID = 6;
                break;

            case R.id.btn7:
                cellID = 7;
                break;

            case R.id.btn8:
                cellID = 8;
                break;

            case R.id.btn9:
                cellID = 9;
                break;


        }

        //playGame(cellID, btnSelected);
        myRef.child("Playing").child(playerSession).child("Cell ID:" + cellID).setValue(ExactName(myEmail));
        // Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show(); //For Testing Purpose
    }

}
