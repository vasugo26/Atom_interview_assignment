package com.hfad.login_flow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class HomeScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private Intent signInIntent;

    Button signoutbtn ;
    Button linkcredbtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        signoutbtn = (Button) findViewById(R.id.signout);
        linkcredbtn = (Button) findViewById(R.id.linkcredentials);

        if(currentUser.isAnonymous()){
            signoutbtn.setVisibility(View.GONE);
        }else{
            linkcredbtn.setVisibility(View.GONE);
        }

    }

    public void onSignOut(View view) {
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //updateUI(null);
                        Toast.makeText(getApplicationContext(),"Signed Out",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HomeScreenActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    }
                });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                // Google Sign In Success
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                LinkAnonymousWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(HomeScreenActivity.this,"SignIn Failed",Toast.LENGTH_SHORT).show();
            }
        }
    }
     private void LinkAnonymousWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
         mAuth.getCurrentUser().linkWithCredential(credential)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             Log.d(TAG, "linkWithCredential:success");
                             FirebaseUser user = task.getResult().getUser();
                             signoutbtn.setVisibility(View.VISIBLE);
                             linkcredbtn.setVisibility(View.GONE);

                         } else {
                             Log.w(TAG, "linkWithCredential:failure", task.getException());
                             Toast.makeText(HomeScreenActivity.this, "Authentication failed.",
                                     Toast.LENGTH_SHORT).show();



                         }


                     }
                 });

     }
    public void onLinkCredentials(View view) {
        signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    }

