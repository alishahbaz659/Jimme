package com.nubar.jime.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nubar.jime.Classes.userCareerinfo;
import com.nubar.jime.MainActivity;
import com.nubar.jime.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class activity_purchase_crystals extends AppCompatActivity {
    Button twothirtynine, onethirtynine, sixtynine, twentythree, eleven, four, two;
    private static PayPalConfiguration config;
    String selected_amount;
    FirebaseAuth firebaseAuth;
    ArrayList<userCareerinfo> userCareerinfoslist;
    SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_crystals);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#faa805"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);

        firebaseAuth = FirebaseAuth.getInstance();
        userCareerinfoslist = new ArrayList<>();
        config = new PayPalConfiguration()
                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                // or live (ENVIRONMENT_PRODUCTION)
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
                .clientId("AYJw5VMMnu33pfdzOyzfn2A1iYKh8lM_IsIZQX_6ZZhenGYpDNIx_LWNOoGekKsKc5jJQn8SLFxUBaKQ");
        initViews();
        twothirtynine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_amount = "239.99";
                paypal(239.99);
            }
        });
        onethirtynine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_amount = "139.99";
                paypal(139.99);

            }
        });
        sixtynine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_amount = "69.99";
                paypal(69.99);
            }
        });
        twentythree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_amount = "23.99";
                paypal(23.99);
            }
        });
        eleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_amount = "11.99";
                paypal(11.99);
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_amount = "4.89";
                paypal(4.89);
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_amount = "2.49";
                paypal(2.49);
            }
        });
    }

    public void initViews() {
        twothirtynine = findViewById(R.id.twoThirtynine);
        onethirtynine = findViewById(R.id.onethirtynine);
        sixtynine = findViewById(R.id.sixtynine);
        twentythree = findViewById(R.id.twentythree);
        eleven = findViewById(R.id.eleven);
        four = findViewById(R.id.four);
        two = findViewById(R.id.two);
    }

    public void paypal(double price) {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(price), "USD", "Total price", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(activity_purchase_crystals.this, PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    if (confirm != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(confirm.toJSONObject().toString(4));
                            JSONObject response = new JSONObject(jsonObject.getString("response"));
                            if (selected_amount.equals("239.99")) {
                                get_existing_crystals("12500");
                                startActivity(new Intent(this, MainActivity.class));
                            } else if (selected_amount.equals("139.99")) {
                                get_existing_crystals("7600");
                                startActivity(new Intent(this, MainActivity.class));
                            } else if (selected_amount.equals("69.99")) {
                                get_existing_crystals("2750");
                                startActivity(new Intent(this, MainActivity.class));
                            } else if (selected_amount.equals("23.99")) {
                                get_existing_crystals("1100");
                                startActivity(new Intent(this, MainActivity.class));
                            } else if (selected_amount.equals("11.99")) {
                                get_existing_crystals("580");
                                startActivity(new Intent(this, MainActivity.class));
                            } else if (selected_amount.equals("4.89")) {
                                get_existing_crystals("210");
                                startActivity(new Intent(this, MainActivity.class));
                            } else if (selected_amount.equals("2.49")) {
                                get_existing_crystals("100");
                                startActivity(new Intent(this, MainActivity.class));
                            } else {
                                Toast.makeText(this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, MainActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.
                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    public void get_existing_crystals(final String total_crystal) {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference quer1 = dbref.child("userCareerinfo").child(firebaseAuth.getUid()).child("info");
        quer1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCareerinfoslist.clear();
                if (dataSnapshot.exists()) {
                    userCareerinfo u = dataSnapshot.getValue(userCareerinfo.class);
                    userCareerinfoslist.add(u);
                    String existing_crystal = userCareerinfoslist.get(0).getTotal_crystals();
                    int converted_existing_crystals = Integer.parseInt(existing_crystal);
                    int pkge_crystals = Integer.parseInt(total_crystal);
                    int total_Crys = converted_existing_crystals + pkge_crystals;
                    update_db(total_Crys);

                } else {
                    Toast.makeText(activity_purchase_crystals.this, "Please update your profile first", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void update_db(final int crystals) {
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("userCareerinfo");
        ref1.child(firebaseAuth.getUid()).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.getRef().child("total_crystals").setValue(crystals + "");
                } else {
                    Toast.makeText(activity_purchase_crystals.this, "unknown error occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}