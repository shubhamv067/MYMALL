package com.shubhamwithcode.mymall;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.shubhamwithcode.mymall.Adapters.CartAdapter;
import com.shubhamwithcode.mymall.Models.cartitemModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {

    public static List<cartitemModel> cartitemModelList;
    RecyclerView deliveryRecyclerView;
    public static CartAdapter cartAdapter;
    Button ChangeorAddaddresss;
    public static final int SELECT_ADDRESS = 0;
    private TextView totalAmount;
    private TextView fullname;
    private String name,mobileNo;
    private TextView fullAddress;
    private TextView pincode;
    private Button continueBtn;
    public static Dialog loadingDialog;
        private Dialog paymentMethodDialog;
        private TextView codTitle;
        private View divider;
        private ImageView paytm,cod;
        private String paymentMethod = "PAYTM";
        private ConstraintLayout orderConfirmationLayout;
        private ImageView continueShoppingBtn;
        private TextView orderId;
        String order_id;
        private boolean successResponse = false;
        public static boolean fromCart;
        public static boolean codOrderConfirmed = false;

        private FirebaseFirestore firebaseFirestore;
        public static boolean getQtyIDs = true;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_delivery);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        deliveryRecyclerView = findViewById(R.id.Delivery_RecyclerView);
        ChangeorAddaddresss = findViewById(R.id.change_or_add_address_btn);
        totalAmount = findViewById(R.id.Total_cart_Amount);
        fullname = findViewById(R.id.Full_Name);
        fullAddress = findViewById(R.id.Full_Address);
        pincode = findViewById(R.id.pinCode);
        continueBtn = findViewById(R.id.cart_continue_btn);
        orderConfirmationLayout = findViewById(R.id.order_confirmation_layout);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);
        orderId = findViewById(R.id.order_id);

        /////loading dialog
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_bg));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        /////loading dialog

        /////paymentMethod dialog
        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_bg));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        paytm = paymentMethodDialog.findViewById(R.id.paytm_btn);
        cod = paymentMethodDialog.findViewById(R.id.cod_btn);
        codTitle = paymentMethodDialog.findViewById(R.id.cod_btn_title);
        divider = paymentMethodDialog.findViewById(R.id.Divider_payment_method);
        /////paymentMethod dialog
        firebaseFirestore = FirebaseFirestore.getInstance();
        getQtyIDs = true;


        order_id = UUID.randomUUID().toString().substring(0,28);

        LinearLayoutManager layoutManager  = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter(cartitemModelList,totalAmount,false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        ChangeorAddaddresss.setVisibility(View.VISIBLE);
        ChangeorAddaddresss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIDs = false;
                Intent MyAddressIntent = new Intent(DeliveryActivity.this,MyAddressesActivity.class);
                MyAddressIntent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(MyAddressIntent);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Boolean allProductAvailable = true;
              for (cartitemModel cartitemModel : cartitemModelList){
                  if (cartitemModel.isQtyError()){
                      allProductAvailable = false;
                  }
                  if (cartitemModel.getType() == cartitemModel.CART_ITEM) {
                      if (!cartitemModel.isCOD()) {
                          cod.setEnabled(false);
                          cod.setAlpha(0.5f);
                          codTitle.setAlpha(0.5f);
                          divider.setVisibility(View.GONE);
                          break;
                      } else {
                          cod.setEnabled(true);
                          cod.setAlpha(1f);
                          codTitle.setAlpha(1f);
                          divider.setVisibility(View.VISIBLE);
                      }
                  }
              }
              if (allProductAvailable){
                  paymentMethodDialog.show();
              }
            }
        });
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            paymentMethod = "COD";
            placeOrderDetails();
            }
        });

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "PAYTM";
                placeOrderDetails();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        ///accessing quantity
        if (getQtyIDs){
            loadingDialog.show();
            for (int x = 0; x < cartitemModelList.size() - 1; x++) {

                for (int y = 0; y < cartitemModelList.get(x).getProductQuantity();y++) {
                    String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);

                    Map<String, Object> timestamp = new HashMap<>();
                    timestamp.put("time", FieldValue.serverTimestamp());
                    int finalX = x;
                    int finalY = y;
                    firebaseFirestore.collection("PRODUCTS").document(cartitemModelList.get(x).getProductID()).collection("QUANTITY").document(quantityDocumentName).set(timestamp)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {

                                   if (task.isSuccessful()){
                                       cartitemModelList.get(finalX).getQtyIDs().add(quantityDocumentName);

                                       if (finalY + 1 == cartitemModelList.get(finalX).getProductQuantity()) {
                                           firebaseFirestore.collection("PRODUCTS").document(cartitemModelList.get(finalX).getProductID()).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(cartitemModelList.get(finalX).getStockQuantity()).get()
                                                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                           if (task.isSuccessful()) {
                                                               List<String> serverQuantity = new ArrayList<>();
                                                               for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                   serverQuantity.add(queryDocumentSnapshot.getId());
                                                               }
                                                               long AvailableQty = 0;
                                                               boolean noLongerAvailable = true;
                                                               for (String qtyID : cartitemModelList.get(finalX).getQtyIDs()) {
                                                                   cartitemModelList.get(finalX).setQtyError(false);
                                                                   if (!serverQuantity.contains(qtyID)) {
                                                                       if (noLongerAvailable) {
                                                                           cartitemModelList.get(finalX).setInStock(false);
                                                                       } else {
                                                                           cartitemModelList.get(finalX).setQtyError(true);
                                                                           cartitemModelList.get(finalX).setMaxQuantity(AvailableQty);
                                                                           Toast.makeText(DeliveryActivity.this, "Sorry ! all products may not be available in requires quantity..", Toast.LENGTH_SHORT).show();
                                                                       }
                                                                   } else {
                                                                       AvailableQty++;
                                                                       noLongerAvailable = false;
                                                                   }
                                                               }
                                                               cartAdapter.notifyDataSetChanged();
                                                           } else {
                                                               String error = task.getException().getMessage();
                                                               Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                                           }
                                                           loadingDialog.dismiss();
                                                       }
                                                   });
                                       }

                                   }else {
                                       loadingDialog.dismiss();
                                       String error = task.getException().getMessage();
                                       Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                   }
                                }
                            });
                }
            }
        }else {
            getQtyIDs = true;
        }
        ///accessing quantity
        name = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName();
        mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
        if (DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo().equals("")) {
            fullname.setText(name + " - " + mobileNo);
        }else {
            fullname.setText(name + " - " + mobileNo + " or " + DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo());
        }
        String flatNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFlatNo();
        String locality = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLocality();
        String landmark = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLandmark();
        String city = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getState();

        if (landmark.equals("")){
            fullAddress.setText(flatNo +" "+ locality +" "+ city +" "+ state);
        }else {
            fullAddress.setText(flatNo +" "+ locality  +" "+ landmark +" "+ city +" "+ state);
        }
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());

        if (codOrderConfirmed){
            showConfirmationLayout();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
        if (getQtyIDs) {
            for (int x = 0; x < cartitemModelList.size() - 1; x++) {
                if (!successResponse) {
                    for (String qtyID : cartitemModelList.get(x).getQtyIDs()) {
                        int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(cartitemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        if (qtyID.equals(cartitemModelList.get(finalX).getQtyIDs().get(cartitemModelList.get(finalX).getQtyIDs().size() -1))){
                                            cartitemModelList.get(finalX).getQtyIDs().clear();
                                        }
                                    }
                                });
                    }
                }else {
                    cartitemModelList.get(x).getQtyIDs().clear();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (successResponse){
            finish();
            return;
        }
        super.onBackPressed();
    }

    public void showConfirmationLayout(){
        successResponse = true;
        codOrderConfirmed = false;
        getQtyIDs = false;

        for (int x = 0; x < cartitemModelList.size() - 1;x++){
            for (String qtyID : cartitemModelList.get(x).getQtyIDs()){
                firebaseFirestore.collection("PRODUCTS").document(cartitemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).update("user_ID",FirebaseAuth.getInstance().getUid());

            }
        }

        if (MainActivity.mainActivity != null){
            MainActivity.mainActivity.finish();
            MainActivity.mainActivity = null;
            MainActivity.showCart = false;
        }else {
            MainActivity.resetMainActivity = true;
        }

        if (Product_Detail_Activity.productDetailsActivity != null){
            Product_Detail_Activity.productDetailsActivity.finish();
            Product_Detail_Activity.productDetailsActivity = null;
        }

        if (fromCart){
            loadingDialog.show();
            Map<String , Object> updateCartList = new HashMap<>();
            long cartListSize = 0;
            List<Integer> indexList = new ArrayList<>();
            for (int x =0; x < DBqueries.cartList.size(); x++){
                if (!cartitemModelList.get(x).isInStock()){
                    updateCartList.put("product_ID_"+cartListSize,cartitemModelList.get(x).getProductID());
                    cartListSize++;
                }else {
                    indexList.add(x);
                }
            }
            updateCartList.put("list_size",cartListSize);


            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                    .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        for (int x = 0;x < indexList.size(); x++){
                            DBqueries.cartList.remove(indexList.get(x).intValue());
                            DBqueries.cartitemModelList.remove(indexList.get(x).intValue());
                            DBqueries.cartitemModelList.remove(DBqueries.cartitemModelList.size()-1);
                        }
                    }else {
                        String error = task.getException().getMessage();
                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                }
            });
        }
        continueBtn.setEnabled(false);
        ChangeorAddaddresss.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        orderId.setText("Order ID "+ order_id);
        orderConfirmationLayout.setVisibility(View.VISIBLE);
        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void placeOrderDetails(){

        String userID = FirebaseAuth.getInstance().getUid();
        loadingDialog.show();
        for (cartitemModel cartitemModel : cartitemModelList) {
            if (cartitemModel.getType() == cartitemModel.CART_ITEM) {
                Map<String,Object> orderDetails = new HashMap<>();
                orderDetails.put("ORDER ID",order_id);
                orderDetails.put("Product Id",cartitemModel.getProductID());
                orderDetails.put("Product Image",cartitemModel.getProductImage());
                orderDetails.put("Product Title",cartitemModel.getProductTitle());
                orderDetails.put("User Id",userID);
                orderDetails.put("Product Quantity",cartitemModel.getProductQuantity());
                if (cartitemModel.getCuttedPrice() != null) {
                    orderDetails.put("Cutted Price", cartitemModel.getCuttedPrice());
                }else {
                    orderDetails.put("Cutted Price", "");
                }
                orderDetails.put("Product Price",cartitemModel.getProductPrice());
                if (cartitemModel.getSelectedCoupenId() != null) {
                    orderDetails.put("Coupen Id", cartitemModel.getSelectedCoupenId());
                }else {
                    orderDetails.put("Coupen Id", "");
                }
                if (cartitemModel.getDiscountedPrice() != null) {
                    orderDetails.put("Discounted Price", cartitemModel.getDiscountedPrice());
                }else {
                    orderDetails.put("Discounted Price","");
                }
                orderDetails.put("Ordered date",FieldValue.serverTimestamp());
                orderDetails.put("Packed date",FieldValue.serverTimestamp());
                orderDetails.put("Shipped date",FieldValue.serverTimestamp());
                orderDetails.put("Delivered date",FieldValue.serverTimestamp());
                orderDetails.put("Cancelled date",FieldValue.serverTimestamp());
                orderDetails.put("Order Status","Ordered");
                orderDetails.put("payment Method",paymentMethod);
                orderDetails.put("Address",fullAddress.getText());
                orderDetails.put("FullName",fullname.getText());
                orderDetails.put("Pincode",pincode.getText());
                orderDetails.put("Free Coupens",cartitemModel.getFreeCoupens());
                orderDetails.put("Delivery Price",cartitemModelList.get(cartitemModelList.size() -1).getDeliveryPrice());
                orderDetails.put("Cancellation requested",false);

                firebaseFirestore.collection("ORDERS").document(order_id).collection("OrderItems").document(cartitemModel.getProductID())
                .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()){
                            String error = task.getException().getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                Map<String,Object> orderDetails = new HashMap<>();
                orderDetails.put("Total Items",cartitemModel.getTotalItems());
                orderDetails.put("Total Items Price",cartitemModel.getTotalItemPrice());
                orderDetails.put("Delivery Price", cartitemModel.getDeliveryPrice());
                orderDetails.put("Total Amount",cartitemModel.getTotalAmount());
                orderDetails.put("Saved Amount",cartitemModel.getSavedAmount());
                orderDetails.put("Payment Status","not paid");
                orderDetails.put("Order Status","Ordered");
                firebaseFirestore.collection("ORDERS").document(order_id)
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()){
                       if (paymentMethod.equals("PAYTM")){
                           paytm();
                       }else {
                           cod();
                       }
                   }else {
                       String error = task.getException().getMessage();
                       Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                   }
                    }
                });
            }
        }
    }
    private void paytm(){
        getQtyIDs = false;
        paymentMethodDialog.dismiss();
        loadingDialog.show();
        if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }
        String M_id = "JrWKzy67490022218125";
        String customer_id = FirebaseAuth.getInstance().getUid();
        String uri = "https://shubhammaymall.000webhostapp.com/paytm/Paytm-Payments_Paytm_Web_Sample_Kit_PHP-master/paytmgetway/generateChecksum.php";
        String callBackUrl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

        RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("CHECKSUMHASH")){
                        String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                        PaytmPGService paytmPGService = PaytmPGService.getStagingService(uri);
                        Map<String, String> paramMap = new HashMap<String,String>();
                        paramMap.put( "MID",M_id);
                        paramMap.put( "ORDER_ID",order_id);
                        paramMap.put( "CUST_ID",customer_id);
                        paramMap.put( "CHANNEL_ID","WAP");
                        paramMap.put( "TXN_AMOUNT",totalAmount.getText().toString().substring(3,totalAmount.getText().length()-2));
                        paramMap.put( "WEBSITE","WEBSTAGING");
                        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                        paramMap.put( "CALLBACK_URL", callBackUrl);
                        paramMap.put("CHECKSUMHASH",CHECKSUMHASH);

                        PaytmOrder order = new PaytmOrder((HashMap<String, String>) paramMap);

                        paytmPGService.initialize(order,null);
                        paytmPGService.startPaymentTransaction(DeliveryActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                            @Override
                            public void onTransactionResponse(Bundle inResponse) {
                                // Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                                if (inResponse.getString("STATUS").equals("TXN_SUCCESS")){
                                    Map<String,Object> updateStatus = new HashMap<>();
                                    updateStatus.put("Payment Status","Paid");
                                    updateStatus.put("Order Status","Ordered");
                                    firebaseFirestore.collection("ORDERS").document(order_id)
                                    .update(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()){
                                           Map<String,Object> userOrder = new HashMap<>();
                                           userOrder.put("order_id",order_id);
                                           userOrder.put("time",FieldValue.serverTimestamp());
                                           firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(order_id).set(userOrder)
                                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful()){
                                                               showConfirmationLayout();
                                                           }else {
                                                               Toast.makeText(DeliveryActivity.this, "failed to update user's OrderList", Toast.LENGTH_SHORT).show();
                                                           }
                                                       }
                                                   });
                                       }else {
                                           Toast.makeText(DeliveryActivity.this, "Order CANCELLED", Toast.LENGTH_SHORT).show();
                                       }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void networkNotAvailable() {
                                Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void clientAuthenticationFailed(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void someUIErrorOccurred(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onBackPressedCancelTransaction() {
                                Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismiss();
                Toast.makeText(DeliveryActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramMap = new HashMap<String,String>();
                paramMap.put( "MID","JrWKzy67490022218125");
                paramMap.put( "ORDER_ID",order_id);
                paramMap.put( "CUST_ID",customer_id);
                paramMap.put( "CHANNEL_ID","WAP");
                paramMap.put( "TXN_AMOUNT",totalAmount.getText().toString().substring(3,totalAmount.getText().length()-2));
                paramMap.put( "WEBSITE","WEBSTAGING");
                paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                paramMap.put( "CALLBACK_URL", callBackUrl);
                return paramMap;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void cod(){
        getQtyIDs = false;
        paymentMethodDialog.dismiss();
        Intent otpIntent = new Intent(DeliveryActivity.this,activity_otpvarification.class);
        otpIntent.putExtra("mobileNo",mobileNo.substring(0,10));
        otpIntent.putExtra("OrderID",order_id);
        startActivity(otpIntent);
    }
}