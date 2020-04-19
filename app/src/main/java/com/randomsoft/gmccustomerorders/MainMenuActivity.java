package com.randomsoft.gmccustomerorders;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;

public class MainMenuActivity extends AppCompatActivity {

    //textview to show who is logged in
    @BindView(R.id.txtUserLogged)
    TextView txtUserLogged;

    //image view and textview for creating a new order
    @BindView(R.id.imgNewOrder)
    ImageView imgNewOrder;
    @BindView(R.id.tvNewOrder)
    TextView tvNewOrder;

    //image view for logging out. located next to user logged text
    @BindView(R.id.imgLogOut)
    ImageView imgLogOut;

    //image view and text view to print orders
    @BindView(R.id.imgPrintOrders)
    ImageView imgPrintOrders;
    @BindView(R.id.tvPrintOrders)
    TextView tvPrintOrders;


}
