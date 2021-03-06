package com.example.DwirTi.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.DwirTi.auth.LoginActivity;
import com.example.DwirTi.R;

import java.util.Map;

/**
 Fenêtre dialog permettant de supprimer le groupe
 */

public class SupprimerGroupeDialog extends Dialog {

    private Activity c;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private Button exitButton, confirmButton;
    private ProgressBar progressBar;
    private String identifiantGroupe;

    public SupprimerGroupeDialog(Activity a, String identifiantGroupe) {
        super(a);
        this.c = a;
        this.identifiantGroupe = identifiantGroupe;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_supprimer_groupe);

        exitButton = (Button) findViewById(R.id.btn_exit_supprimer_modal);
        confirmButton = (Button) findViewById(R.id.btn_confirmer_supprimer_modal);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supprimerGroupe();
            }
        });
    }

    /**
     Permet de supprimer le groupe
     @param
     @return void
     */
    private void supprimerGroupe() {

        DatabaseReference ref = database.getReference("groupes/" + identifiantGroupe + "/users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> usersGroupe = (Map<String, String>) dataSnapshot.getValue();

                for (Map.Entry<String, String> entry : usersGroupe.entrySet()) {
                    database.getReference("users/" + entry.getKey() + "/groupe").setValue(null);
                }
                database.getReference("groupes/" + identifiantGroupe).setValue(null);

                Toast.makeText(c.getApplicationContext(), "Le groupe a été définitivement supprimé.", Toast.LENGTH_LONG).show();
                c.startActivity(new Intent(c, LoginActivity.class));
                c.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

}
