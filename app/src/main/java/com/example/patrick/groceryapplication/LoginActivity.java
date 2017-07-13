package com.example.patrick.groceryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG="LoginActivity";

    // Firebase instance variable
    private FirebaseAuth firebaseAuth;

    private String username;
    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Sets up Google Sign In
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,this /*ConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        //Initialize Firebase
        firebaseAuth=FirebaseAuth.getInstance();

        silentLogin();

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        signInButton.setVisibility(View.VISIBLE);
    }

    private void handleFireBaseAuthResult(AuthResult authResult){
        if(authResult!=null){
            //Start main app
            FirebaseUser user=authResult.getUser();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
    private void signIn() {
        Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult signInResult=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(signInResult.isSuccess()){
                GoogleSignInAccount googleAccount=signInResult.getSignInAccount();
                firebaseAuthWithGoogle(googleAccount);
            }
            else{
                Log.d(TAG,"Google Sign in Failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        Log.d(TAG,"firebaseAuthWithGoogle: "+acct.getId());
        AuthCredential credential=GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"signInWtihCredential:oncomplete: "+ task.isSuccessful());

                        if(!task.isSuccessful()){
                            Log.w(TAG,"signInWithCredential: ",task.getException());
                            Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }
                    }
                });
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
            default:
                return;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Something borked
        Log.d(TAG,"onConnectionFailed: "+connectionResult);
        Toast.makeText(this,"Google Play Services error.",Toast.LENGTH_SHORT).show();
    }

    private void silentLogin(){
        OptionalPendingResult<GoogleSignInResult> pendingResult=Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(pendingResult!=null){
            handlePendingResult(pendingResult);
        }

    }

    private void handlePendingResult(OptionalPendingResult<GoogleSignInResult> pendingResult){
        if(pendingResult.isDone()){
            GoogleSignInResult signInResult=pendingResult.get();
            silentSignInCompleted(signInResult);
        }
        else{
            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    silentSignInCompleted(googleSignInResult);
                }
            });
        }
    }

    private void silentSignInCompleted(GoogleSignInResult signInResult){
        GoogleSignInAccount signInAccount=signInResult.getSignInAccount();
        if(signInAccount!=null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }
}