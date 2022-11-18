package com.shubhamwithcode.mymall.Adapters;

import static com.shubhamwithcode.mymall.DeliveryActivity.SELECT_ADDRESS;
import static com.shubhamwithcode.mymall.MyAddressesActivity.refreshitem;
import static com.shubhamwithcode.mymall.ui.accountFragment.MANAGE_ADDRESS;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shubhamwithcode.mymall.Add_Addresses_Activity;
import com.shubhamwithcode.mymall.DBqueries;
import com.shubhamwithcode.mymall.Models.AddressesModel;
import com.shubhamwithcode.mymall.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {

    List<AddressesModel> addressesModelList;
    private int MODE;
    private int priSelectedPosition;
    private int preSelectedPosition = -1;
    private boolean refresh = false;
    private Dialog loadingDialog;
    // private int priSelectedPosition = -1;

    public AddressesAdapter(List<AddressesModel> addressesModelList, int MODE,Dialog loadingDialog) {
        this.addressesModelList = addressesModelList;
        this.MODE = MODE;
        priSelectedPosition = DBqueries.selectedAddress;
        this.loadingDialog = loadingDialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String city = addressesModelList.get(position).getCity();
        String locality = addressesModelList.get(position).getLocality();
        String flatNo = addressesModelList.get(position).getFlatNo();
        String pincode = addressesModelList.get(position).getPincode();
        String landmark = addressesModelList.get(position).getLandmark();
        String name = addressesModelList.get(position).getName();
        String mobileNo = addressesModelList.get(position).getMobileNo();
        String alternateMobileNo = addressesModelList.get(position).getAlternateMobileNo();
        String state = addressesModelList.get(position).getState();
        boolean selected = addressesModelList.get(position).getSelected();

        holder.setData(name, city, pincode, selected, position, mobileNo, alternateMobileNo, flatNo, locality, state, landmark);
    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView FullName;
        TextView Address;
        TextView PinCode;
        ImageView icon;
        LinearLayout optionContainer;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            FullName = itemView.findViewById(R.id.item_addresses_FullName);
            Address = itemView.findViewById(R.id.addresses_item);
            PinCode = itemView.findViewById(R.id.item_addresses_Pincode);
            icon = itemView.findViewById(R.id.item_addresses_icon_view);
            optionContainer = itemView.findViewById(R.id.Option_container);
        }

        private void setData(String userName, String city, String userPincode, boolean selected, int position, String mobileNo, String alternateMobileNo, String flatNo, String locality, String state, String landmark) {

            if (alternateMobileNo.equals("")) {
                FullName.setText(userName + " - " + mobileNo);
            } else {
                FullName.setText(userName + " - " + mobileNo + " or " + alternateMobileNo);
            }
            if (landmark.equals("")) {
                Address.setText(flatNo + " " + locality + " " + city + " " + state);
            } else {
                Address.setText(flatNo + " " + locality + " " + landmark + " " + city + " " + state);
            }

            PinCode.setText(userPincode);


            if (MODE == SELECT_ADDRESS) {
                icon.setImageResource(R.drawable.check);
                if (selected) {
                    icon.setVisibility(View.VISIBLE);
                    priSelectedPosition = position;
                } else {
                    icon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (priSelectedPosition != position) {
                            addressesModelList.get(position).setSelected(true);
                            addressesModelList.get(priSelectedPosition).setSelected(false);
                            refreshitem(priSelectedPosition, position);
                            priSelectedPosition = position;
                            DBqueries.selectedAddress = position;

                        }
                    }
                });


            } else if (MODE == MANAGE_ADDRESS) {
                optionContainer.setVisibility(View.GONE);
                optionContainer.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {////////edit address
                        Intent addAddressIntent = new Intent(itemView.getContext(), Add_Addresses_Activity.class);
                        addAddressIntent.putExtra("INTENT", "update_address");
                        addAddressIntent.putExtra("index", position);
                        itemView.getContext().startActivity(addAddressIntent);
                        refresh = false;
                    }
                });
                optionContainer.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {///////remove address
                        loadingDialog.show();
                        Map<String, Object> addresses = new HashMap<>();
                        int x = 0;
                        int selected = -1;
                        for (int i = 0; i < addressesModelList.size(); i++) {
                            if (i != position) {
                                addresses.put("city_" + x, addressesModelList.get(i).getCity());
                                addresses.put("locality_" + x, addressesModelList.get(i).getLocality());
                                addresses.put("flat_no_" + x, addressesModelList.get(i).getFlatNo());
                                addresses.put("pincode_" + x, addressesModelList.get(i).getPincode());
                                addresses.put("landmark_" + x, addressesModelList.get(i).getLandmark());
                                addresses.put("name_" + x, addressesModelList.get(i).getName());
                                addresses.put("mobile_no_" + x, addressesModelList.get(i).getMobileNo());
                                addresses.put("alternate_mobile_no_" + x, addressesModelList.get(i).getAlternateMobileNo());
                                addresses.put("state_" + x, addressesModelList.get(i).getCity());

                                if (addressesModelList.get(position).getSelected()) {
                                    if (position - 1 >= 0) {
                                        if (x == position) {
                                            addresses.put("selected_" + x, true);
                                            selected = x;
                                        }else {
                                            addresses.put("selected_"+x,addressesModelList.get(i).getSelected());
                                        }
                                    } else {
                                        if (x == 1) {
                                            addresses.put("selected_" + x, true);
                                            selected = x;
                                        }else {
                                            addresses.put("selected_"+x,addressesModelList.get(i).getSelected());
                                        }
                                    }
                                } else {
                                    addresses.put("selected_" + x, addressesModelList.get(i).getSelected());
                                    if (addressesModelList.get(i).getSelected()){
                                        selected = x;
                                    }
                                }
                            }
                        }
                        addresses.put("list_size", x);
                        final int finalSelected = selected;
                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                                .set(addresses).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DBqueries.addressesModelList.remove(position);
                                    if (finalSelected != -1) {
                                        DBqueries.selectedAddress = finalSelected - 1;
                                        DBqueries.addressesModelList.get(finalSelected - 1).setSelected(true);
                                    }else if (DBqueries.addressesModelList.size() == 0){
                                        DBqueries.selectedAddress = -1;
                                    }
                                    notifyDataSetChanged();
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });
                        refresh = false;
                    }
                });
                icon.setImageResource(R.drawable.vertical_menu);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionContainer.setVisibility(View.VISIBLE);
                        if (refresh) {
                            refreshitem(priSelectedPosition, priSelectedPosition);
                        } else {
                            refresh = true;
                        }
                        priSelectedPosition = position;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshitem(priSelectedPosition, priSelectedPosition);
                        priSelectedPosition = -1;
                    }
                });


            }

        }
    }
}
