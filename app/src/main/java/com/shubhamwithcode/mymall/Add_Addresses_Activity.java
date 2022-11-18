package com.shubhamwithcode.mymall;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shubhamwithcode.mymall.Models.AddressesModel;

import java.util.HashMap;
import java.util.Map;

public class Add_Addresses_Activity extends AppCompatActivity {
    private EditText city;
    private EditText locality;
    private EditText flatNo;
    private EditText pincode;
    private EditText landmark;
    private EditText name;
    private EditText mobileNo;
    private EditText alternateMobileNo;
    private Spinner stateSpinner;
    private Button save_btn;
    private Dialog loadingDialog;

    private String[] stateList;
    private String selectedState;

    private boolean updateAddress = false;
    private AddressesModel addressesModel;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_addresses);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add a new address");

        /////loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_bg));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /////loading dialog

        stateList = getResources().getStringArray(R.array.india_states);
        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality);
        flatNo = findViewById(R.id.flat_no);
        pincode = findViewById(R.id.pincode);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.name);
        mobileNo = findViewById(R.id.mobile_no);
        alternateMobileNo = findViewById(R.id.alternate_mobile_no);
        stateSpinner = findViewById(R.id.state_spinner);
        save_btn = findViewById(R.id.save_btn);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stateList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = stateList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (getIntent().getStringExtra("INTENT").equals("update_address")){
            updateAddress = true;
            position = getIntent().getIntExtra("index",-1);
            addressesModel = DBqueries.addressesModelList.get(position);

            city.setText(addressesModel.getCity());
            locality.setText(addressesModel.getLocality());
            flatNo.setText(addressesModel.getFlatNo());
            landmark.setText(addressesModel.getLandmark());
            name.setText(addressesModel.getName());
            mobileNo.setText(addressesModel.getMobileNo());
            alternateMobileNo.setText(addressesModel.getAlternateMobileNo());
            pincode.setText(addressesModel.getPincode());

            for (int i = 0; i < stateList.length;i++){
                if (stateList[i].equals(addressesModel.getState())){
                    stateSpinner.setSelection(i);
                }
            }
            save_btn.setText("Update");
        }else {
            position = DBqueries.addressesModelList.size();
        }

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(city.getText())) {
                    if (!TextUtils.isEmpty(locality.getText())) {
                        if (!TextUtils.isEmpty(flatNo.getText())) {
                            if (!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length() == 6) {
                                if (!TextUtils.isEmpty(name.getText())) {
                                    if (!TextUtils.isEmpty(mobileNo.getText()) && mobileNo.getText().length() == 10) {
                                        loadingDialog.show();

                                        Map<String, Object> addAddress = new HashMap<>();
                                        addAddress.put("city_" + String.valueOf(position + 1),city.getText().toString());
                                        addAddress.put("locality_" + String.valueOf(position + 1),locality.getText().toString());
                                        addAddress.put("flat_no_" + String.valueOf(position + 1),flatNo.getText().toString());
                                        addAddress.put("pincode_" + String.valueOf(position + 1),pincode.getText().toString());
                                        addAddress.put("landmark_" + String.valueOf(position + 1),landmark.getText().toString());
                                        addAddress.put("name_" + String.valueOf(position + 1),name.getText().toString());
                                        addAddress.put("mobile_no_" + String.valueOf(position + 1),mobileNo.getText().toString());
                                        addAddress.put("alternate_mobile_no_" + String.valueOf(position + 1),alternateMobileNo.getText().toString());
                                        addAddress.put("state_" + String.valueOf(position + 1),selectedState);
                                        if (!updateAddress) {
                                            addAddress.put("list_size",(long)DBqueries.addressesModelList.size() + 1);
                                            if (getIntent().getStringExtra("INTENT").equals("manage")){
                                                if (DBqueries.addressesModelList.size() == 0){
                                                    addAddress.put("selected_" + String.valueOf(position + 1),true);
                                                }else {
                                                    addAddress.put("selected_" + String.valueOf(position + 1),false);
                                                }
                                            }else {
                                                addAddress.put("selected_" + String.valueOf(position + 1),true);
                                            }
                                            if (DBqueries.addressesModelList.size() > 0) {
                                                addAddress.put("selected_" + String.valueOf((long) DBqueries.addressesModelList.size() + 1), false);
                                            }
                                        }

                                        FirebaseFirestore.getInstance().collection("USERS")
                                                .document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                                .document("MY_ADDRESSES")
                                                .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if (!updateAddress) {
                                                        if (DBqueries.addressesModelList.size() > 0) {
                                                            DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                                                        }
                                                        DBqueries.addressesModelList.add(new AddressesModel(true, city.getText().toString(), locality.getText().toString(), flatNo.getText().toString(), pincode.getText().toString(), landmark.getText().toString(), name.getText().toString(), mobileNo.getText().toString(), alternateMobileNo.getText().toString(), selectedState));
                                                        if (getIntent().getStringExtra("INTENT").equals("manage")){
                                                            if (DBqueries.addressesModelList.size() == 0){
                                                                DBqueries.selectedAddress = DBqueries.addressesModelList.size() - 1;
                                                            }
                                                        }else {
                                                            DBqueries.selectedAddress = DBqueries.addressesModelList.size() - 1;
                                                        }
                                                    }else {
                                                        DBqueries.addressesModelList.set(position,new AddressesModel(true, city.getText().toString(), locality.getText().toString(), flatNo.getText().toString(), pincode.getText().toString(), landmark.getText().toString(), name.getText().toString(), mobileNo.getText().toString(), alternateMobileNo.getText().toString(), selectedState));
                                                    }
                                                    if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                        Intent AddressesIntent = new Intent(Add_Addresses_Activity.this, DeliveryActivity.class);
                                                        startActivity(AddressesIntent);
                                                    }else {
                                                        MyAddressesActivity.refreshitem(DBqueries.selectedAddress,DBqueries.addressesModelList.size() -1);
                                                    }
                                                 finish();
                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(Add_Addresses_Activity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialog.dismiss();
                                            }
                                        });
                                    }else {
                                        mobileNo.requestFocus();
                                        Toast.makeText(Add_Addresses_Activity.this, "Please provide valid Number", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    name.requestFocus();
                                }
                            }else {
                                pincode.requestFocus();
                                Toast.makeText(Add_Addresses_Activity.this, "Please provide valid pincode", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            flatNo.requestFocus();
                        }
                    }else {
                        locality.requestFocus();
                    }
                }else {
                    city.requestFocus();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


}