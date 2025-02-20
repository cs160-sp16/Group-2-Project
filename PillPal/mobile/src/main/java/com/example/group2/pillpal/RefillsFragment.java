package com.example.group2.pillpal;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RefillsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RefillsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RefillsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static UserInstance currentUser;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RefillsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RefillsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RefillsFragment newInstance(String param1, String param2) {
        RefillsFragment fragment = new RefillsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = UserInstance.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("UPDATE"));

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            View view = getView();
            Button refillsButton = (Button) view.findViewById(R.id.refills_button);
            refillsButton.setText("Request Refills");

            TextView last_refill = (TextView) view.findViewById(R.id.lastRefillInfo);
            HashMap<String, String> lastRefill = currentUser.refillHistory.get(currentUser.refillHistory.size() - 1);
            last_refill.setText(lastRefill.get("name") + " - " + lastRefill.get("date"));

            LinearLayout refill_confirmation = (LinearLayout) view.findViewById(R.id.refill_confirmation);
            LinearLayout refill_status = (LinearLayout) view.findViewById(R.id.refill_status);

            refill_confirmation.setVisibility(View.GONE);
            refill_status.setVisibility(View.GONE);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.refills_fragment, container, false);

        TextView address_line_one = (TextView) v.findViewById(R.id.addressLineOne);
        TextView address_line_two = (TextView) v.findViewById(R.id.addressLineTwo);
        TextView pill_name = (TextView) v.findViewById(R.id.pillName);
        TextView pill_dosage = (TextView) v.findViewById(R.id.pillDosage);
        TextView last_refill = (TextView) v.findViewById(R.id.lastRefillInfo);

        HashMap<String, String> shipping_address = currentUser.shipAdd;
        String street_name = shipping_address.get("street_name");
        String second_line = shipping_address.get("city") + " " + shipping_address.get("state") + shipping_address.get("zip");
        address_line_one.setText(street_name);
        address_line_two.setText(second_line);

        pill_name.setText(currentUser.prescription.get("name"));
        pill_dosage.setText(currentUser.prescription.get("dosage"));

        if (currentUser.refillHistory != null) {
            HashMap<String, String> lastRefill = currentUser.refillHistory.get(currentUser.refillHistory.size() - 1);
            last_refill.setText(lastRefill.get("name") + " - " + lastRefill.get("date"));
        } else {
            last_refill.setText("None");
        }
        Button refillsButton = (Button) v.findViewById(R.id.refills_button);
        Button confirmationButton = (Button) v.findViewById(R.id.confirmation_button);
        Button statusButton = (Button) v.findViewById(R.id.status_button);

        refillsButton.setOnClickListener(this);
        confirmationButton.setOnClickListener(this);
        statusButton.setOnClickListener(this);

        LinearLayout refill_confirmation = (LinearLayout) v.findViewById(R.id.refill_confirmation);
        LinearLayout refill_status = (LinearLayout) v.findViewById(R.id.refill_status);

        refill_confirmation.setVisibility(View.GONE);
        refill_status.setVisibility(View.GONE);

        if (!currentUser.refillRequested) {
            refillsButton.setText("Request Refills");
        } else {
            refillsButton.setText("View Refill Status");
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        View view = getView();
        LinearLayout refill_status = (LinearLayout) view.findViewById(R.id.refill_status);
        LinearLayout refill_confirmation = (LinearLayout) view.findViewById(R.id.refill_confirmation);
        Button refillsButton = (Button) view.findViewById(R.id.refills_button);
        int button_id = v.getId();
        switch (button_id) {
            case R.id.refills_button:
                if (currentUser.refillRequested) {
                    refill_status.setVisibility(View.VISIBLE);
                } else {
                    refill_confirmation.setVisibility(View.VISIBLE);
                    AlarmManager alarm_manager = (AlarmManager) v.getContext().getSystemService(Context.ALARM_SERVICE);
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat date_formatter = new SimpleDateFormat("MM/dd/yy");

                    Intent status_intent = new Intent(v.getContext(), PhoneToWatchService.class);
                    status_intent.putExtra("DATA", "refill/status");

                    Intent arrival_intent = new Intent(v.getContext(), PhoneToWatchService.class);
                    arrival_intent.putExtra("DATA", "refill/arrival");

                    // ALARM FOR PACKAGE ARRIVAL
//                    Date current_date = new Date();
//                    calendar.setTime(current_date);
//                    calendar.add(Calendar.DATE, 2);
//                    calendar.set(Calendar.HOUR, 11);
//                    calendar.set(Calendar.MINUTE,0);
//                    calendar.set(Calendar.AM_PM, Calendar.AM);
//                    String formatted_date = date_formatter.format(calendar.getTime());

                    Date current_date = new Date();
                    calendar.setTime(current_date);
                    calendar.add(Calendar.SECOND, 15);
                    String formatted_date = date_formatter.format(calendar.getTime());
                    PendingIntent arrival_pending_intent = PendingIntent.getService(v.getContext(), 0, arrival_intent, PendingIntent.FLAG_ONE_SHOT);
                    alarm_manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), arrival_pending_intent);
                    System.out.println(calendar.getTime());
                    status_intent.putExtra("REFILL_ARRIVAL_DATE", formatted_date);
                    // ALARM FOR PACKAGE STATUS
//                    Date current_date = new Date();
//                    calendar.setTime(current_date);
//                    calendar.add(Calendar.DATE, 1);
//                    calendar.set(Calendar.HOUR, 11);
//                    calendar.set(Calendar.MINUTE,0);
//                    calendar.set(Calendar.AM_PM, Calendar.AM);
//
//                    current_date = new Date();
//                    calendar.setTime(current_date);
//                    calendar.add(Calendar.SECOND, 5);
//                    PendingIntent status_pending_intent = PendingIntent.getService(v.getContext(), 0, status_intent, PendingIntent.FLAG_ONE_SHOT);
//                    alarm_manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), status_pending_intent);
//                    System.out.println(calendar.getTime());

                    TextView delivery_date = (TextView) view.findViewById(R.id.delivery_date);
                    delivery_date.setText("Package expected to arrive on: " + formatted_date);

                    // update button
                    refillsButton.setText("View Refill Status");
                    currentUser.refillRequested = true;
                    currentUser.currentRefillRequest.put("name", currentUser.prescription.get("name"));
                    currentUser.currentRefillRequest.put("date", formatted_date);
                }
                break;
            case R.id.confirmation_button:
                refill_confirmation.setVisibility(View.INVISIBLE);
                break;
            case R.id.status_button:
                refill_status.setVisibility(View.INVISIBLE);
                break;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void updateRefillHistory() {
        System.out.println("UPDATING REFILL");
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
}
