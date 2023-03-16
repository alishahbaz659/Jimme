package com.nubar.jime.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nubar.jime.Classes.User;
import com.nubar.jime.MainActivity;
import com.nubar.jime.R;
import com.facebook.FacebookSdk;

import java.util.Arrays;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class activity_login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 101;
    private CallbackManager mcallbackManager;
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    Button btnfb;
    ImageView logoimage;
    private LoginButton loginbutton;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG = "FacebookAuthentication";
    public SweetAlertDialog pDialog;
    SignInButton google_signin;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        pDialog = new SweetAlertDialog(activity_login.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#faa805"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);


        firebaseAuth = FirebaseAuth.getInstance();
        initviews();
        loginbutton.setReadPermissions(Arrays.asList("email", "public_profile"));
        mcallbackManager = CallbackManager.Factory.create();
        loginbutton.registerCallback(mcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                pDialog.dismiss();
                Log.d(TAG, "onCencel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError");
                pDialog.dismiss();
            }
        });
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(activity_login.this, MainActivity.class));
                } else {
                    //updateui(null);
                }
            }
        };
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    firebaseAuth.signOut();
                }
            }
        };

        //google signin functions
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(activity_login.this, gso);

        google_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    public void handleFacebookToken(AccessToken accessToken) {
        Log.d(TAG, "handleFacebookToken" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "sign in with credential is successfull");
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    String url = String.valueOf(user.getPhotoUrl());
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                    User us = new User(user.getDisplayName(), user.getEmail(), user.getUid(), url);
                    ref.setValue(us);
                    startActivity(new Intent(activity_login.this, MainActivity.class));
                    pDialog.dismiss();

                } else {
                    Log.d(TAG, "sign in with credential ui failed");
                    Toast.makeText(activity_login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void initviews() {
        loginbutton = findViewById(R.id.login_button);
        logoimage = findViewById(R.id.logoimage);
        google_signin = (SignInButton) findViewById(R.id.google_sign_in_button);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            logoimage.setClipToOutline(true);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String url = String.valueOf(user.getPhotoUrl());

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                            User us = new User(user.getDisplayName(), user.getEmail(), user.getUid(), url);
                            ref.setValue(us);
                            startActivity(new Intent(activity_login.this, MainActivity.class));
                            pDialog.dismiss();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(activity_login.this, "Error occurred", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        pDialog.show();
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                pDialog.dismiss();
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}