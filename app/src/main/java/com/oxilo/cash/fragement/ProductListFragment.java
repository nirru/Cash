package com.oxilo.cash.fragement;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.oxilo.cash.ApplicationController;
import com.oxilo.cash.R;
import com.oxilo.cash.Webservices.GettingProductList;
import com.oxilo.cash.activity.MainActivity;
import com.oxilo.cash.adapter.ProductListAdapter;
import com.oxilo.cash.event.DeleteFinishedEvent;
import com.oxilo.cash.event.ProductResponseFinishedEvent;
import com.oxilo.cash.event.UpdateFinishedEvent;
import com.oxilo.cash.holder.GroupItem;
import com.oxilo.cash.job.DeleteProductJob;
import com.oxilo.cash.job.GettingProductListJob;
import com.oxilo.cash.job.UpdateProductJob;
import com.oxilo.cash.modal.Product;
import com.oxilo.cash.modal.ProductList;
import com.oxilo.cash.modal.Update;
import com.oxilo.cash.ui.EndlessRecyclerOnScrollListener;
import com.oxilo.cash.ui.GeneralDialogFragment;
import com.oxilo.cash.util.ActivityUtils;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.TagConstraint;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends Fragment implements SearchView.OnQueryTextListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    JobManager jobManager;
    RecyclerView recyclerView;
    ProductListAdapter productListAdapter;
    GroupItem groupItem;
     int COUNT=1;
    GeneralDialogFragment generalDialogFragment;
    private boolean isStart= false;
    LinearLayoutManager linearLayoutManager;

    private View mProgressView;
    private View mLoginFormView;

    public ProductListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductListFragment newInstance(String param1, String param2) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (!EventBus.getDefault().isRegistered(this))
         EventBus.getDefault().register(this);
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUiWidget(view);
        init();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
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
        productListAdapter.setOnItemClickListener(new ProductListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (v.getId()==R.id.update) {
                    if (groupItem != null) {
                        ProductList productList = groupItem.productLists.get(position);
                        String id  = productList.getId();
                         generalDialogFragment =
                                GeneralDialogFragment.newInstance(productList.getName(), productList.getPrice(), productList.getTaxclass(),id);
                        generalDialogFragment.show(getFragmentManager(), "dialog");
                    }
                }else if (v.getId()==R.id.delete){
                        if (groupItem != null){
                            ProductList productList = groupItem.productLists.get(position);
                            String id  = productList.getId();
                            jobManager.addJobInBackground(new DeleteProductJob<Update>(getActivity(),new Update(),id));
                        }
                }
                else if (v.getId() == R.id.checkbox){
                    if (groupItem != null){
                        ProductList productList = groupItem.productLists.get(position);
                        CheckBox checkBox = (CheckBox)v.findViewById(R.id.checkbox);
                        productList.setChecked(checkBox.isChecked());
                    }
                }
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        jobManager.cancelJobsInBackground(null, TagConstraint.ALL, GettingProductListJob.TAG);
        jobManager.cancelJobsInBackground(null, TagConstraint.ALL, UpdateProductJob.TAG);
        jobManager.cancelJobsInBackground(null, TagConstraint.ALL, DeleteProductJob.TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);


        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener()
                {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item)
                    {
                        // Do something when collapsed
                        if (groupItem !=null && groupItem.productLists.size() != 0)
                            productListAdapter.filter("");
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item)
                    {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                //adition perform
                AddProductFragement addProductFragement =  AddProductFragement.newInstance("","");
                ActivityUtils.launchFragementWithAnimation(addProductFragement,getActivity());
                break;
            default:
                Toast.makeText(getActivity(),"Not a valid selection",Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
//        final List<ProductList> filteredModelList = filter(groupItem.productLists, query);
//        productListAdapter.animateTo(filteredModelList);
//        recyclerView.scrollToPosition(0);
//        productListAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        productListAdapter.filter(newText);
        return true;
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private void initUiWidget(View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);
        recyclerView = (RecyclerView)view.findViewById(R.id.action_recyle_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

//        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int current_page) {
//                jobManager.addJobInBackground(new GettingProductListJob<Product>(getActivity(),new Product(),current_page));
//            }
//        });

        AppCompatButton invoice_btn = (AppCompatButton) view.findViewById(R.id.invoice_action);
        invoice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ProductList> productLists = new ArrayList<ProductList>();
                for (ProductList productList:groupItem.productLists) {
                    if (productList.getChecked())
                        productLists.add(productList);
                }
                Fragment fragment  = InvoiceFragement.newInstance(productLists,"");
                ActivityUtils.launchFragementWithAnimation(fragment,getActivity());
            }
        });

    }

    private void init(){
        groupItem = new GroupItem();
        jobManager = ApplicationController.getInstance().getJobManager();
        productListAdapter = new ProductListAdapter(groupItem.productLists,getActivity());
        recyclerView.setAdapter(productListAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgress(true);
                jobManager.addJobInBackground(new GettingProductListJob<Product>(getActivity(),new Product(),COUNT));
            }
        }, 10);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(ProductResponseFinishedEvent event) {
        // Update the UI in the main thread
        showProgress(false);
        productListAdapter.addItems(event.getmProduct().getProductList());
        for (ProductList productList: event.getmProduct().getProductList())
        {
            productList.setCurrentCount(1);
            productList.setBasePrice(productList.getPrice());
            productListAdapter.addItem(productList);
        }

    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(UpdateFinishedEvent event) {
        // Update the UI in the main thread
        Intent i = new Intent(getActivity(),MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(DeleteFinishedEvent event) {
        // Update the UI in the main thread
          groupItem.productLists.clear();
          productListAdapter.notifyDataSetChanged();
          jobManager.addJobInBackground(new GettingProductListJob<Product>(getActivity(),new Product(),COUNT));
    }

    private List<ProductList> filter(List<ProductList> models, String query) {
        query = query.toLowerCase();
        final List<ProductList> filteredModelList = new ArrayList<>();
        for (ProductList model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
