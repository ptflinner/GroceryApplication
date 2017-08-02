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

/*
Used the Google Sign In Tutorial by Google.
https://firebase.google.com/docs/auth/android/google-signin
Above link was used as a jumping off point

Used Google Silent Login
https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInApi#silentSignIn(com.google.android.gms.common.api.GoogleApiClient)
 */
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


        //Draws everything after the silentLogin has failed
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        signInButton.setVisibility(View.VISIBLE);
    }

    //Begins the sign in process. Passing the intent to Google functions
    private void signIn() {
        Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned and sign in begins
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

    //Signs in to the firebase server with google credentials
    //Warning logs are displayed if the login fails
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

    //Listens for the sign in button to be pressed
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

    //The below functions enable silent login
    //Allow for the user to not need to access the sign in page beyond the first time or if they logged out
    private void silentLogin(){

        //OptionalPendingResult returns true if a result is found
        OptionalPendingResult<GoogleSignInResult> pendingResult=Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(pendingResult!=null){
            handlePendingResult(pendingResult);
        }

    }

    //If the result exists immediately then it must be processed otherwise it needs to be found
    private void handlePendingResult(OptionalPendingResult<GoogleSignInResult> pendingResult){

        //If it is found then check if it is correct
        if(pendingResult.isDone()){
            GoogleSignInResult signInResult=pendingResult.get();
            silentSignInCompleted(signInResult);
        }
        else{
            //Calls for the result then checks if correct
            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    silentSignInCompleted(googleSignInResult);
                }
            });
        }
    }

    //Checks that the sign in is correct
    //If yes then it begins the main activity
    //Otherwise it completes the function and login page is loaded fully
    private void silentSignInCompleted(GoogleSignInResult signInResult){
        GoogleSignInAccount signInAccount=signInResult.getSignInAccount();
        if(signInAccount!=null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }
}