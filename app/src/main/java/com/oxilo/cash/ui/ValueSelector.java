package com.oxilo.cash.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oxilo.cash.R;
import com.oxilo.cash.modal.ProductList;

import java.util.List;

/**
 * Created by ericbasendra on 30/11/15.
 */
public class ValueSelector<T> extends RelativeLayout {

    private int minValue = 0;
    private int maxValue = Integer.MAX_VALUE;
    private double mProductPrice;
    private String mProductName;
    private T productLists;

    private boolean plusButtonIsPressed = false;
    private boolean minusButtonIsPressed = false;
    private final long REPEAT_INTERVAL_MS = 100l;

    View rootView;
    TextView valueTextView;
    EditText productNameView;


    EditText productPriceView;
    View minusButton;
    View plusButton;

    Handler handler = new Handler();

    public ValueSelector(Context context,T productList) {
        super(context);
        this.productLists = productList;
        init(context);
    }


    public ValueSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.value_selector, this);
        valueTextView = (TextView) rootView.findViewById(R.id.valueTextView);
        productNameView = (EditText)rootView.findViewById(R.id.product_name);
        productPriceView = (EditText)rootView.findViewById(R.id.product_price);

        productPriceView.setText(((ProductList)productLists).getPrice()+ ""  );

        Toast.makeText(getContext(),"AsssssCTIC",Toast.LENGTH_LONG).show();

        minusButton = rootView.findViewById(R.id.minusButton);
        plusButton = rootView.findViewById(R.id.plusButton);

        minusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementValue(); //we'll define this method later
            }
        });


        plusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementValue(); //we'll define this method later
            }
        });

        plusButton.setOnLongClickListener(
                new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View arg0) {
                        plusButtonIsPressed = true;
                        handler.post(new AutoIncrementer());
                        return false;
                    }
                }
        );

        plusButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    plusButtonIsPressed = false;
                }
                return false;
            }
        });

        minusButton.setOnLongClickListener(
                new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View arg0) {
                        minusButtonIsPressed = true;
                        handler.post(new AutoDecrementer());
                        return false;
                    }
                }
        );
        minusButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    minusButtonIsPressed = false;
                }
                return false;
            }
        });


    }

    public EditText getProductPriceView() {
        return productPriceView;
    }

    public EditText getProductNameView() {
        return productNameView;
    }

    public String getmProductName() {
        return mProductName;
    }

    public void setmProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public double getmProductPrice() {
        return mProductPrice;
    }

    public void setmProductPrice(double mProductPrice) {
        this.mProductPrice = mProductPrice;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getValue() {
        return Integer.valueOf(valueTextView.getText().toString());
    }

    public void setValue(int newValue) {
        int value = newValue;
        if(newValue < minValue) {
            value = minValue;
        } else if (newValue > maxValue) {
            value = maxValue;
        }

        valueTextView.setText(String.valueOf(value));
    }

    private void incrementValue() {
        int currentVal = Integer.valueOf(valueTextView.getText().toString());
        if(currentVal < maxValue) {
            valueTextView.setText(String.valueOf(currentVal + 1));
        }
    }

    private void decrementValue() {
        int currentVal = Integer.valueOf(valueTextView.getText().toString());
        if(currentVal > minValue) {
            valueTextView.setText(String.valueOf(currentVal - 1));
        }
    }

    private class AutoIncrementer implements Runnable {
        @Override
        public void run() {
            if(plusButtonIsPressed){
                incrementValue();
                handler.postDelayed( new AutoIncrementer(), REPEAT_INTERVAL_MS);
            }
        }
    }
    private class AutoDecrementer implements Runnable {
        @Override
        public void run() {
            if(minusButtonIsPressed){
                decrementValue();
                handler.postDelayed(new AutoDecrementer(), REPEAT_INTERVAL_MS);
            }
        }
    }
}

