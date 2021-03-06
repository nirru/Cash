package com.oxilo.cash.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oxilo.cash.ApplicationController;
import com.oxilo.cash.R;
import com.oxilo.cash.job.AddProductJob;
import com.oxilo.cash.job.UpdateProductJob;
import com.oxilo.cash.modal.Update;
import com.path.android.jobqueue.JobManager;

import org.w3c.dom.Text;

/**
 * Created by ericbasendra on 04/12/15.
 */
public class GeneralDialogFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String mName;
    private String mPrice;
    private String mTax;
    private String id11;

    private TextView nameView, priceView, taxView;

    // Create an instance of the Dialog with the input
    public static GeneralDialogFragment newInstance(String name, String price,String tax,String id11) {
        GeneralDialogFragment frag = new GeneralDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, name);
        args.putString(ARG_PARAM2, price);
        args.putString(ARG_PARAM3, tax);
        args.putString(ARG_PARAM4, id11);
        frag.setArguments(args);
        return frag;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_PARAM1);
            mPrice = getArguments().getString(ARG_PARAM2);
            mTax = getArguments().getString(ARG_PARAM3);
            id11 = getArguments().getString(ARG_PARAM4);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.update_product_dailog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUiWidget(view);
    }

    private void initUiWidget(View view){
        nameView = (TextView)view.findViewById(R.id.action_name);
        priceView = (TextView)view.findViewById(R.id.action_price);
        taxView = (TextView)view.findViewById(R.id.action_tax);

        nameView.setHint("" + mName);
        priceView.setHint("" + mPrice);
        taxView.setHint("" + mTax);


        AppCompatButton btn_cancel= (AppCompatButton)view.findViewById(R.id.action_cancel_btn);
        AppCompatButton btn_update = (AppCompatButton)view.findViewById(R.id.action_update_btn);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dismiss();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameView.setError(null);
                priceView.setError(null);
                taxView.setError(null);

                // Store values at the time of the login attempt.
                String campaign_title = nameView.getText().toString();
                String promotion_message = priceView.getText().toString();
                String start_date = taxView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Check for a valid Campaign Title.
                if (TextUtils.isEmpty(campaign_title)) {
                    nameView.setError(getString(R.string.error_field_required));
                    focusView = nameView;
                    cancel = true;
                }

                // Check for a valid Promotion Message.
                else if (TextUtils.isEmpty(promotion_message)) {
                    priceView.setError(getString(R.string.error_field_required));
                    focusView = priceView;
                    cancel = true;
                }
                // Check for a valid Start Date.
                else if (TextUtils.isEmpty(start_date)) {
                    taxView.setError(getString(R.string.error_field_required));
                    focusView = taxView;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    JobManager jobManager = ApplicationController.getInstance().getJobManager();
                    String id1 = id11;
                    jobManager.addJobInBackground(new UpdateProductJob<Update>(getActivity(),new Update(),nameView.getText().toString(),priceView.getText().toString()
                            ,taxView.getText().toString(),id11));
                    dismiss();

                }


            }
        });
    }
}