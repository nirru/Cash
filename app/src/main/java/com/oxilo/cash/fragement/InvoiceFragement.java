package com.oxilo.cash.fragement;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.oxilo.cash.R;
import com.oxilo.cash.adapter.InvoiceAdapter;
import com.oxilo.cash.modal.ProductList;
import com.oxilo.cash.util.ActivityUtils;
import com.oxilo.cash.util.BluetoothUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InvoiceFragement.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InvoiceFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvoiceFragement extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<ProductList> productList;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    InvoiceAdapter invoiceAdapter;
    Toolbar toolbar;

    private int minValue = 1;
    private int maxValue = Integer.MAX_VALUE;

    private boolean plusButtonIsPressed = false;
    private boolean minusButtonIsPressed = false;
    private final long REPEAT_INTERVAL_MS = 100l;

    private TextView subTotal,total,myLabel;


    public InvoiceFragement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param productLists Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvoiceFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static InvoiceFragement newInstance(ArrayList<ProductList> productLists, String param2) {
        InvoiceFragement fragment = new InvoiceFragement();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, productLists);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            productList = getArguments().getParcelableArrayList(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invoice_fragement, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((InvoiceAdapter)invoiceAdapter).setOnItemClickListener(new InvoiceAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (v.getId() == R.id.plusButton){
                    incrementValue(position);
                }else if (v.getId() == R.id.minusButton){
                    decrementValue(position);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                getFragmentManager().popBackStack();
                return true;
            case R.id.action_print:
                BluetoothUtil bluetoothUtil = new BluetoothUtil(getActivity(),"ABCD",myLabel);
                bluetoothUtil.openBluetooth();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void init(View v){
        toolbar = (Toolbar)v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        subTotal = (TextView) v.findViewById(R.id.subtotal);
        total = (TextView)v.findViewById(R.id.total);
        myLabel = (TextView)v.findViewById(R.id.mylabel);
        invoiceAdapter = new InvoiceAdapter(productList,getActivity());
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.action_recyle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(invoiceAdapter);

        subTotal();
    }

    private void incrementValue(int pos) {
        ProductList product = productList.get(pos);
        if(product.getCurrentCount() < maxValue) {
            product.setCurrentCount(product.getCurrentCount() + 1);
            product.setPrice((Double.valueOf(product.getPrice().toString()) * product.getCurrentCount()) + "");
            invoiceAdapter.notifyItemChanged(pos);
            subTotal();
        }
    }

    private void decrementValue(int pos) {
        ProductList product = productList.get(pos);
        if(product.getCurrentCount() > minValue) {
            product.setCurrentCount(product.getCurrentCount() - 1);
            product.setPrice((Double.valueOf(product.getPrice().toString()) * product.getCurrentCount()) + "");
            invoiceAdapter.notifyItemChanged(pos);
            subTotal();
        }
    }

    private double subTotal(){
        double subtotal = 0;
        for (ProductList product: productList) {
            subtotal += Double.valueOf(product.getPrice());
        }
        subTotal.setText("" + subtotal) ;
        total.setText("" + (int)subtotal);
        return subtotal;
    }

    private int total(){
        int total = 0;
        for (ProductList product: productList) {
            total += Double.valueOf(product.getPrice()).intValue();
        }
        return total;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.invoice_main, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == BluetoothUtil.REQUEST_ENABLE_BT){
            //do some stuff
        }
    }
}
