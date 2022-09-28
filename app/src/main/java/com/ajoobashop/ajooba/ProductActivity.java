package com.ajoobashop.ajooba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ajoobashop.ajooba.Dialogs.QuantityChsDialog;
import com.ajoobashop.ajooba.LoadingDialougs.LoadingDialog;
import com.ajoobashop.ajooba.Model.Products;
import com.ajoobashop.ajooba.Prevalent.Prevalent;
import com.ajoobashop.ajooba.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import io.paperdb.Paper;

public class ProductActivity extends AppCompatActivity {

    Toolbar toolbar_products;

    RecyclerView recyclerView_products;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference ProductRef;

    public String SubCategoryRootName, CategoryRootName;

    private  LoadingDialog loadingDialog;

    private ViewGroup.MarginLayoutParams params_add_toCat_Btn;

    private RelativeLayout.LayoutParams layoutParams_Center;

    private RelativeLayout.LayoutParams layoutParams_start;

    private ImageView cart_Btn, back_Btn_ImgVw, call_Btn;

    private Fragment selectedFragment;
    private Activity selectedActivity;

    private FrameLayout container;

    private ImageView empty_Icon;

    private String currentFragment;
    private TextView badge_No_Cart_Items_Products;

    private Boolean isAvailable;

    private EditText search_EdtTxt;

    private FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    private FirebaseRecyclerAdapter<Products,ProductViewHolder> adapter_SearchFilter;

    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        back_Btn_ImgVw = findViewById(R.id.back_Btn_ImgView);

        badge_No_Cart_Items_Products = findViewById(R.id.Badge_No_Cart_Items_products);

        search_EdtTxt = findViewById(R.id.search_Bar_EdtTxt);
        empty_Icon = findViewById(R.id.empty_ProductsActv_Icon);

        call_Btn = findViewById(R.id.call_Btn_products);

        currentFragment = "null";

        container = findViewById(R.id.fragment_Container_products);

        Paper.init(this);
        toolbar_products = findViewById(R.id.Toolbar_products);

        loadingDialog = new LoadingDialog(this);

        cart_Btn = findViewById(R.id.cart_Btn_products);

        CategoryRootName = getIntent().getStringExtra("valueCategory");
        SubCategoryRootName = getIntent().getStringExtra("valueSubCategory");

        setSupportActionBar(toolbar_products);
        toolbar_products.showOverflowMenu();

        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products").child(CategoryRootName).child(SubCategoryRootName);

        recyclerView_products = findViewById(R.id.recycler_view_products);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_products.setLayoutManager(layoutManager);

        selectedFragment = null;
        selectedActivity = null;


        cart_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(Paper.book().read(Prevalent.UserPhoneKey).equals(""))) {
                    selectedFragment = new CartFragment();
                } else {

                    if (Paper.book().read(Prevalent.UserPhoneKey) != "") {
                        selectedActivity = new ProfileActivity();
                    } else {
                        selectedActivity = new Login_SignUp_Activity();
                    }
                }

                if (selectedFragment != null) {

                    currentFragment = "cart";

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_Container_products, selectedFragment).commit();


                } else if (selectedActivity != null) {

                    Intent intent = new Intent(ProductActivity.this, selectedActivity.getClass());
                    startActivity(intent);
                }
            }
        });

        back_Btn_ImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductActivity.super.onBackPressed();
            }
        });

        if (!(Paper.book().read(Prevalent.UserPhoneKey).toString() == "")){
            setBadgeNumber(Paper.book().read(Prevalent.UserPhoneKey).toString());
        }
        else {
            badge_No_Cart_Items_Products.setVisibility(View.INVISIBLE);
        }


        search_EdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!search_EdtTxt.getText().toString().equals("")) {
                    searchFilteredProducts(editable.toString());
                    search_EdtTxt.clearFocus();
                    search_EdtTxt.setCursorVisible(false);
                }
                else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search_EdtTxt.getRootView().getWindowToken(), 0);
                    search_EdtTxt.clearFocus();
                    search_EdtTxt.setCursorVisible(false);
                    showAllProducts();
                }

            }
        });

        call_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phn_Number = "9665888225";
                makePhoneCall(phn_Number);

            }
        });

    }

    private void makePhoneCall(String number) {

        if (ContextCompat.checkSelfPermission(ProductActivity.this,
                android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProductActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }

    }

    private void setBadgeNumber(String UserPhoneKey) {

        DatabaseReference cart_items_Count_Ref = FirebaseDatabase.getInstance().getReference().child("cart").child(UserPhoneKey);

        cart_items_Count_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>=1){
                    String count = Long.toString(dataSnapshot.getChildrenCount());

                    badge_No_Cart_Items_Products.setVisibility(View.VISIBLE);
                    badge_No_Cart_Items_Products.setText(count);
                }
                else {
                    badge_No_Cart_Items_Products.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {


        if (currentFragment == "cart"){
            currentFragment ="null";
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }

            showAllProducts();

        }

        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        search_EdtTxt.setText("");
        checkAvailability();
        showAllProducts();
        adapter.notifyDataSetChanged();

    }

    private void checkAvailability() {
        loadingDialog.startLoadingDialog();

        ProductRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    showAllProducts();
                } else {
                    recyclerView_products.setVisibility(View.INVISIBLE);
                    loadingDialog.dismissDialog();
                    Toast.makeText(ProductActivity.this, "No products found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchFilteredProducts(String typedString){

        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductRef.orderByChild("pname")
                                .startAt(typedString.substring(0, 1).toUpperCase() + typedString.substring(1))
                                .endAt(typedString.substring(0, 1).toUpperCase() + typedString.substring(1)+"\uf8ff") ,Products.class)
                        .build();

        adapter_SearchFilter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, final int i, @NonNull final Products products) {

                        Picasso.get().load(products.getImage())
                                .into(productViewHolder.product_imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        loadingDialog.dismissDialog();
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });

                        if(!(Paper.book().read(Prevalent.UserPhoneKey).equals(""))) {

                            DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference()
                                    .child("cart")
                                    .child(Paper.book().read(Prevalent.UserPhoneKey).toString());

                            cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(products.getPid()).exists()){
                                        String multiple = dataSnapshot.child(products.getPid()).child("multiple").getValue().toString();



                                        productViewHolder.add_to_cart_Btn.setVisibility(View.INVISIBLE);
                                        productViewHolder.elegant_No_Btn.setVisibility(View.VISIBLE);
                                        productViewHolder.number_elegant.setVisibility(View.VISIBLE);
                                        productViewHolder.number_elegant.setText(multiple.toString());

                                    } else {
                                        try {
                                            if (products.getStatus().equals("notAvailable")){
                                                productViewHolder.add_to_cart_Btn.setVisibility(View.INVISIBLE);
                                                productViewHolder.currentyUnavailable_TxtVw.setVisibility(View.VISIBLE);
                                            } else {
                                                productViewHolder.add_to_cart_Btn.setVisibility(View.VISIBLE);
                                                productViewHolder.elegant_No_Btn.setVisibility(View.INVISIBLE);

                                            }
                                        } catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        productViewHolder.product_Name.setText(products.getPname());
                        productViewHolder.product_Quantity.setText(products.getQuantity1());
                        productViewHolder.product_Quantity_Unit.setText(products.getUnits1());

                        productViewHolder.product_Mrp.setText(products.getMrp1());
                        productViewHolder.product_Rate.setText(products.getRate1());
                        productViewHolder.product_Mrp.setPaintFlags(productViewHolder.product_Mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        productViewHolder.quantity_Chs_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String pName, q1, u1, Mrp1, r1, q2, u2, Mrp2, r2;

                                pName = products.getPname().toString();

                                q1 = products.getQuantity1().toString();
                                u1 = products.getUnits1().toString();
                                Mrp1 = products.getMrp1().toString();
                                r1 = products.getRate1().toString();

                                q2 = products.getQuantity2().toString();
                                u2 = products.getUnits2().toString();
                                Mrp2= products.getMrp2().toString();
                                r2 = products.getRate2().toString();

                                final QuantityChsDialog dialog_Quantity=new QuantityChsDialog(ProductActivity.this,
                                        pName,
                                        q1,
                                        u1,
                                        Mrp1,
                                        r1,
                                        q2,
                                        u2,
                                        Mrp2,
                                        r2);

                                dialog_Quantity.show();

                                dialog_Quantity.quantity1_Btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        productViewHolder.product_Quantity.setText(products.getQuantity1());
                                        productViewHolder.product_Quantity_Unit.setText(products.getUnits1());

                                        productViewHolder.product_Mrp.setText(products.getMrp1());
                                        productViewHolder.product_Mrp.setPaintFlags(productViewHolder.product_Mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                        productViewHolder.product_Rate.setText(products.getRate1());

                                        dialog_Quantity.dismiss();
                                    }
                                });

                                dialog_Quantity.quantity2_Btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        productViewHolder.product_Quantity.setText(products.getQuantity2());
                                        productViewHolder.product_Quantity_Unit.setText(products.getUnits2());

                                        productViewHolder.product_Mrp.setText(products.getMrp2());
                                        productViewHolder.product_Mrp.setPaintFlags(productViewHolder.product_Mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                        productViewHolder.product_Rate.setText(products.getRate2());

                                        dialog_Quantity.dismiss();
                                    }
                                });


                            }
                        });



                        productViewHolder.add_to_cart_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!(Paper.book().read(Prevalent.UserPhoneKey).equals(""))){

                                    addToBasket(Paper.book().read(Prevalent.UserPhoneKey).toString()
                                            , products.getPid()
                                            , products.getPname()
                                            , productViewHolder.product_Quantity.getText().toString()
                                            , productViewHolder.product_Quantity_Unit.getText().toString()
                                            , productViewHolder.product_Mrp.getText().toString()
                                            , productViewHolder.product_Rate.getText().toString()
                                            ,productViewHolder.progressBar
                                            ,productViewHolder.add_to_cart_Btn
                                            ,productViewHolder.elegant_No_Btn
                                            ,productViewHolder.number_elegant
                                            ,productViewHolder.plus_Btn
                                            , productViewHolder.minus_Btn
                                            , productViewHolder.progressBar_for_Elegant
                                            ,products.getCategoryName()
                                            ,products.getImage()
                                            ,products.getSubCategoryName()
                                            , i
                                            , "filter");


                                } else {
                                    Activity selectedActivity;

                                    if (Paper.book().read(Prevalent.UserPhoneKey)!=""){
                                        selectedActivity = new ProfileActivity();
                                    } else {
                                        selectedActivity = new Login_SignUp_Activity();
                                    }
                                    Intent intent = new Intent(ProductActivity.this, selectedActivity.getClass());
                                    startActivity(intent);
                                }
                            }
                        });

                        productViewHolder.plus_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int i = Integer.parseInt(productViewHolder.number_elegant.getText().toString());

                                i = i+1;

                                if (i>6){
                                    Toast.makeText(ProductActivity.this, "Cannot add more than this..", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    plus_elegentNumberChange(productViewHolder.plus_Btn
                                            , productViewHolder.minus_Btn
                                            , productViewHolder.number_elegant
                                            , productViewHolder.progressBar_for_Elegant
                                            , Paper.book().read(Prevalent.UserPhoneKey).toString()
                                            , products.getPid());
                                }
                            }
                        });

                        productViewHolder.minus_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int i = Integer.parseInt(productViewHolder.number_elegant.getText().toString());

                                i = i - 1;

                                if (i < 1) {

                                    remove_From_Cart(productViewHolder.plus_Btn
                                            , productViewHolder.minus_Btn,
                                            productViewHolder.add_to_cart_Btn
                                            , productViewHolder.elegant_No_Btn
                                            , productViewHolder.progressBar_for_Elegant
                                            , products.getPid()
                                            , Paper.book().read(Prevalent.UserPhoneKey).toString());

                                } else {
                                    minus_elegentNumberChange(productViewHolder.plus_Btn
                                            , productViewHolder.minus_Btn
                                            , productViewHolder.number_elegant
                                            , productViewHolder.progressBar_for_Elegant
                                            , Paper.book().read(Prevalent.UserPhoneKey).toString()
                                            , products.getPid());
                                }

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout,parent, false);
                        ProductViewHolder holder= new ProductViewHolder(view);
                        return holder;

                    }
                };

        recyclerView_products.setAdapter(adapter_SearchFilter);
        adapter_SearchFilter.startListening();

    }

    private void showAllProducts() {
        loadingDialog.startLoadingDialog();

        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductRef,Products.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, final int i, @NonNull final Products products) {

                        Picasso.get().load(products.getImage())
                                .placeholder(getDrawable(R.drawable.geometry_bc))
                                .into(productViewHolder.product_imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        loadingDialog.dismissDialog();
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });


                        if(!(Paper.book().read(Prevalent.UserPhoneKey).equals(""))) {

                            DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference()
                                    .child("cart")
                                    .child(Paper.book().read(Prevalent.UserPhoneKey).toString());

                            cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(products.getPid()).exists()){
                                        String multiple = dataSnapshot.child(products.getPid()).child("multiple").getValue().toString();

                                        String quantity = dataSnapshot.child(products.getPid()).child("quantity").getValue().toString();
                                        String units = dataSnapshot.child(products.getPid()).child("units").getValue().toString();

                                        if (quantity.equals(products.getQuantity1()) && (units.equals(products.getUnits1()))){
                                            productViewHolder.product_Quantity.setText(products.getQuantity1());
                                            productViewHolder.product_Quantity_Unit.setText(products.getUnits1());

                                            productViewHolder.product_Mrp.setText(products.getMrp1());
                                            productViewHolder.product_Rate.setText(products.getRate1());
                                            productViewHolder.product_Mrp.setPaintFlags(productViewHolder.product_Mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        } else if (quantity.equals(products.getQuantity2()) && (units.equals(products.getUnits2()))) {

                                            productViewHolder.product_Quantity.setText(products.getQuantity2());
                                            productViewHolder.product_Quantity_Unit.setText(products.getUnits2());

                                            productViewHolder.product_Mrp.setText(products.getMrp2());
                                            productViewHolder.product_Mrp.setPaintFlags(productViewHolder.product_Mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                            productViewHolder.product_Rate.setText(products.getRate2());

                                        } else {
                                            productViewHolder.product_Quantity.setText(products.getQuantity1());
                                            productViewHolder.product_Quantity_Unit.setText(products.getUnits1());

                                            productViewHolder.product_Mrp.setText(products.getMrp1());
                                            productViewHolder.product_Rate.setText(products.getRate1());
                                            productViewHolder.product_Mrp.setPaintFlags(productViewHolder.product_Mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        }

                                        productViewHolder.add_to_cart_Btn.setVisibility(View.INVISIBLE);
                                        productViewHolder.elegant_No_Btn.setVisibility(View.VISIBLE);
                                        productViewHolder.number_elegant.setVisibility(View.VISIBLE);
                                        productViewHolder.number_elegant.setText(multiple.toString());

                                    } else {
                                        try {
                                            if (products.getStatus().equals("notAvailable")){
                                                productViewHolder.add_to_cart_Btn.setVisibility(View.INVISIBLE);
                                                productViewHolder.currentyUnavailable_TxtVw.setVisibility(View.VISIBLE);
                                            } else {
                                                productViewHolder.currentyUnavailable_TxtVw.setVisibility(View.INVISIBLE);
                                                productViewHolder.add_to_cart_Btn.setVisibility(View.VISIBLE);
                                                productViewHolder.elegant_No_Btn.setVisibility(View.INVISIBLE);

                                            }
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            productViewHolder.currentyUnavailable_TxtVw.setVisibility(View.INVISIBLE);
                                            productViewHolder.add_to_cart_Btn.setVisibility(View.VISIBLE);
                                            productViewHolder.elegant_No_Btn.setVisibility(View.INVISIBLE);
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        productViewHolder.product_Name.setText(products.getPname());
                        productViewHolder.product_Quantity.setText(products.getQuantity1());
                        productViewHolder.product_Quantity_Unit.setText(products.getUnits1());

                        productViewHolder.product_Mrp.setText(products.getMrp1());
                        productViewHolder.product_Rate.setText(products.getRate1());
                        productViewHolder.product_Mrp.setPaintFlags(productViewHolder.product_Mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        productViewHolder.quantity_Chs_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String pName, q1, u1, Mrp1, r1, q2, u2, Mrp2, r2;

                                pName = products.getPname().toString();

                                q1 = products.getQuantity1().toString();
                                u1 = products.getUnits1().toString();
                                Mrp1 = products.getMrp1().toString();
                                r1 = products.getRate1().toString();

                                q2 = products.getQuantity2().toString();
                                u2 = products.getUnits2().toString();
                                Mrp2= products.getMrp2().toString();
                                r2 = products.getRate2().toString();

                                final QuantityChsDialog dialog_Quantity=new QuantityChsDialog(ProductActivity.this,
                                        pName,
                                        q1,
                                        u1,
                                        Mrp1,
                                        r1,
                                        q2,
                                        u2,
                                        Mrp2,
                                        r2);

                                dialog_Quantity.show();

                                dialog_Quantity.quantity1_Btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        productViewHolder.product_Quantity.setText(products.getQuantity1());
                                        productViewHolder.product_Quantity_Unit.setText(products.getUnits1());

                                        productViewHolder.product_Mrp.setText(products.getMrp1());
                                        productViewHolder.product_Mrp.setPaintFlags(productViewHolder.product_Mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                        productViewHolder.product_Rate.setText(products.getRate1());

                                        dialog_Quantity.dismiss();
                                    }
                                });

                                dialog_Quantity.quantity2_Btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        productViewHolder.product_Quantity.setText(products.getQuantity2());
                                        productViewHolder.product_Quantity_Unit.setText(products.getUnits2());

                                        productViewHolder.product_Mrp.setText(products.getMrp2());
                                        productViewHolder.product_Mrp.setPaintFlags(productViewHolder.product_Mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                        productViewHolder.product_Rate.setText(products.getRate2());

                                        dialog_Quantity.dismiss();
                                    }
                                });


                            }
                        });



                        productViewHolder.add_to_cart_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!(Paper.book().read(Prevalent.UserPhoneKey).equals(""))){

                                    productViewHolder.add_to_cart_Btn.setEnabled(false);
                                    productViewHolder.add_to_cart_Btn.setAlpha(.7f);

                                    addToBasket(Paper.book().read(Prevalent.UserPhoneKey).toString()
                                            , products.getPid()
                                    , products.getPname()
                                    , productViewHolder.product_Quantity.getText().toString()
                                    , productViewHolder.product_Quantity_Unit.getText().toString()
                                    , productViewHolder.product_Mrp.getText().toString()
                                            , productViewHolder.product_Rate.getText().toString()
                                    ,productViewHolder.progressBar
                                    ,productViewHolder.add_to_cart_Btn
                                    ,productViewHolder.elegant_No_Btn
                                    ,productViewHolder.number_elegant
                                    ,productViewHolder.plus_Btn
                                    , productViewHolder.minus_Btn
                                            , productViewHolder.progressBar_for_Elegant
                                    ,products.getCategoryName()
                                    ,products.getImage()
                                    ,products.getSubCategoryName()
                                    , i
                                    , "all");


                                } else {
                                    Activity selectedActivity;

                                    if (Paper.book().read(Prevalent.UserPhoneKey)!=""){
                                        selectedActivity = new ProfileActivity();
                                    } else {
                                        selectedActivity = new Login_SignUp_Activity();
                                    }
                                    Intent intent = new Intent(ProductActivity.this, selectedActivity.getClass());
                                    startActivity(intent);
                                }
                            }
                        });

                        productViewHolder.plus_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int i = Integer.parseInt(productViewHolder.number_elegant.getText().toString());

                                i = i+1;

                                if (i>6){
                                    Toast.makeText(ProductActivity.this, "Cannot add more than this..", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    plus_elegentNumberChange(productViewHolder.plus_Btn
                                            , productViewHolder.minus_Btn
                                            , productViewHolder.number_elegant
                                            , productViewHolder.progressBar_for_Elegant
                                            , Paper.book().read(Prevalent.UserPhoneKey).toString()
                                            , products.getPid());
                                }
                            }
                        });

                        productViewHolder.minus_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int i = Integer.parseInt(productViewHolder.number_elegant.getText().toString());

                                i = i-1;

                                if (i<1){

                                    remove_From_Cart(productViewHolder.plus_Btn
                                    , productViewHolder.minus_Btn,
                                            productViewHolder.add_to_cart_Btn
                                    , productViewHolder.elegant_No_Btn
                                    ,productViewHolder.progressBar_for_Elegant
                                    ,products.getPid()
                                    , Paper.book().read(Prevalent.UserPhoneKey).toString());

                                }
                                else {
                                    minus_elegentNumberChange(productViewHolder.plus_Btn
                                    , productViewHolder.minus_Btn
                                    , productViewHolder.number_elegant
                                    , productViewHolder.progressBar_for_Elegant
                                    , Paper.book().read(Prevalent.UserPhoneKey).toString()
                                    , products.getPid());
                                }
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout,parent, false);
                        ProductViewHolder holder= new ProductViewHolder(view);
                        return holder;
                    }
                };

        recyclerView_products.setAdapter(adapter);
        adapter.startListening();

    }



    private void  remove_From_Cart(final TextView plus_Btn, final TextView minus_Btn, final Button addToCart_Btn, final RelativeLayout elegant_No_Btn, final ProgressBar progressBar, final String pid, final String UserPhoneKey) {

        progressBar.setVisibility(View.VISIBLE);

        plus_Btn.setEnabled(false);
        minus_Btn.setEnabled(false);
        plus_Btn.setAlpha(.8f);
        minus_Btn.setAlpha(.8f);

        final DatabaseReference cart_Product_Ref = FirebaseDatabase.getInstance().getReference().child("cart").child(UserPhoneKey).child(pid);

        cart_Product_Ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cart_Product_Ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        elegant_No_Btn.setVisibility(View.INVISIBLE);
                        addToCart_Btn.setVisibility(View.VISIBLE);

                        setBadgeNumber(UserPhoneKey);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cart_Product_Ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                elegant_No_Btn.setVisibility(View.INVISIBLE);
                addToCart_Btn.setVisibility(View.VISIBLE);

                setBadgeNumber(UserPhoneKey);

            }
        });

    }
    private void plus_elegentNumberChange(final TextView plus_Btn, final TextView minus_Btn, final TextView number_Elegant, final ProgressBar progressBar, final String UserPhoneKey, final String pid){

        progressBar.setVisibility(View.VISIBLE);

        plus_Btn.setEnabled(false);
        minus_Btn.setEnabled(false);
        plus_Btn.setAlpha(.8f);
        minus_Btn.setAlpha(.8f);

        DatabaseReference elegantNum_Ref = FirebaseDatabase.getInstance().getReference().child("cart").child(UserPhoneKey).child(pid).child("multiple");

        int i = Integer.parseInt(number_Elegant.getText().toString());

        i = i+1;

        elegantNum_Ref.setValue(Integer.toString(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);

                    plus_Btn.setEnabled(true);
                    minus_Btn.setEnabled(true);
                    plus_Btn.setAlpha(1f);
                    minus_Btn.setAlpha(1f);

                    int i = Integer.parseInt(number_Elegant.getText().toString());

                    i = i+1;

                    number_Elegant.setText(Integer.toString(i));

                    setBadgeNumber(UserPhoneKey);

                }
            }
        });

    }

    private void minus_elegentNumberChange(final TextView plus_Btn, final TextView minus_Btn, final TextView number_Elegant, final ProgressBar progressBar, final String UserPhoneKey, final String pid){

        progressBar.setVisibility(View.VISIBLE);

        plus_Btn.setEnabled(false);
        minus_Btn.setEnabled(false);
        plus_Btn.setAlpha(.8f);
        minus_Btn.setAlpha(.8f);

        DatabaseReference elegantNum_Ref = FirebaseDatabase.getInstance().getReference().child("cart").child(UserPhoneKey).child(pid).child("multiple");

        int i = Integer.parseInt(number_Elegant.getText().toString());

        i = i-1;

        elegantNum_Ref.setValue(Integer.toString(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);

                    plus_Btn.setEnabled(true);
                    minus_Btn.setEnabled(true);
                    plus_Btn.setAlpha(1f);
                    minus_Btn.setAlpha(1f);

                    int i = Integer.parseInt(number_Elegant.getText().toString());

                    i = i-1;

                    DatabaseReference cart_RateRef = FirebaseDatabase.getInstance().getReference().child("cart").child(Paper.book().read(Prevalent.UserPhoneKey).toString()).child(pid);

                    cart_RateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int rate;
                            rate = Integer.parseInt((String) dataSnapshot.child("rate").getValue());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    number_Elegant.setText(Integer.toString(i));

                    setBadgeNumber(UserPhoneKey);



                }
            }
        });

    }

    private void addToBasket(final String UserPhoneKey, final String pid, String pname, String quantity, String units, String mrp, final String rate, final ProgressBar progressBar, final Button addtoCart_Btn, final RelativeLayout elegant_No_Btn, final TextView number_elegant, final TextView plus_Btn, final TextView minus_Btn, final ProgressBar progressBar_Elegant
    , String categoryName, String pImage, String subCategoryName, final int index, final String type_Search) {


        params_add_toCat_Btn = (ViewGroup.MarginLayoutParams) addtoCart_Btn.getLayoutParams();



        layoutParams_start =  (RelativeLayout.LayoutParams)addtoCart_Btn.getLayoutParams();
        layoutParams_start.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        layoutParams_start.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        addtoCart_Btn.setLayoutParams(layoutParams_start);


        params_add_toCat_Btn.setMargins(0, 0, 10, 0);

        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference()
                .child("cart")
                .child(UserPhoneKey);

        final HashMap<String, Object> cartMap= new HashMap<>();

        cartMap.put("pid",pid);
        cartMap.put("pname",pname);
        cartMap.put("quantity",quantity);
        cartMap.put("units",units);
        cartMap.put("mrp",mrp);
        cartMap.put("rate",rate);
        cartMap.put("multiple","1");
        cartMap.put("categoryName",categoryName);
        cartMap.put("image",pImage);
        cartMap.put("subCategoryName",subCategoryName);
        cartMap.put("userPhoneKey",Paper.book().read(Prevalent.UserPhoneKey).toString());

        cartReference.child(pid)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            layoutParams_start.removeRule(RelativeLayout.ALIGN_PARENT_START);
                            layoutParams_start.removeRule(RelativeLayout.CENTER_VERTICAL);
                            layoutParams_start.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

                            addtoCart_Btn.setLayoutParams(layoutParams_start);

                            addtoCart_Btn.setAlpha(1);
                            addtoCart_Btn.setEnabled(true);

                            if (type_Search.equals("filter")){
                                adapter_SearchFilter.notifyItemChanged(index);
                            } else if(type_Search.equals("all")){

                                adapter.notifyItemChanged(index);
                            }


                            setBadgeNumber(UserPhoneKey);

                            Toast.makeText(ProductActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search_Products);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();

        searchView.setQueryHint("Search products");
        searchView.setVisibility(View.INVISIBLE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
    return super.onCreateOptionsMenu(menu);
    }


}