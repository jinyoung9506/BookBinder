package com.kamizos.bookbinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieManager;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment4 extends Fragment {

    LinearLayout loginLayout;
    LinearLayout syncLayout;
    EditText loginid;
    EditText loginpw;
    Button loginButton;
    Button signupButton;
    TextView usernameText;
    TextView dataCount;
    ImageButton uploadbt;
    ImageButton downloadbt;
    FloatingActionButton refreshbt;
    String ID;
    String PW;
    Context context;
    public String URL;
    DBHelper dbhelper;



    public Fragment4() {
        // Required empty public constructor
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        loginLayout = getActivity().findViewById(R.id.LoginLayout);
        syncLayout = getActivity().findViewById(R.id.SyncLayout);
        loginid = getActivity().findViewById(R.id.LoginID);
        loginpw = getActivity().findViewById(R.id.LoginPW);
        loginButton = getActivity().findViewById(R.id.LoginBT);
        signupButton = getActivity().findViewById(R.id.SignupBT);
        usernameText = getActivity().findViewById(R.id.UsernameText);
        uploadbt = getActivity().findViewById(R.id.UploadBT);
        downloadbt = getActivity().findViewById(R.id.DownloadBT);
        refreshbt = getActivity().findViewById(R.id.AccountRefresh);
        context = getContext();
        URL = context.getString(R.string.URL);
        dbhelper = DBHelper.getInstance(context);

        final Sync.LoginRequest[] logintask = new Sync.LoginRequest[1];
        final Sync.UploadRequest[] uploadtask = new Sync.UploadRequest[1];
        final Sync.DownloadRequest[] downloadtask = new Sync.DownloadRequest[1];






        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ID = loginid.getText().toString();
                PW = loginpw.getText().toString();

                logintask[0] = new Sync.LoginRequest(context, new Sync.LoginRequestCallback() {
                    @Override
                    public void onSuccess(String resultstring, String id) {
                        if (resultstring.equals("LoginSuccess")) {
                            ID = id;
                            Toast.makeText(context, "로그인 성공", Toast.LENGTH_LONG).show();
                            toggleLayout(ID);
                        }
                        else {
                            Toast.makeText(context, "로그인 실패", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context, "로그인 실패" + e, Toast.LENGTH_LONG).show();
                    }
                }) {

                };

                logintask[0].execute(ID, PW);

            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("회원가입을 위해 웹 페이지로 이동합니다.").setPositiveButton("YES", SignupDialogListener).setNegativeButton("NO", SignupDialogListener).show();
            }
        });



        uploadbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadtask[0] = new Sync.UploadRequest(context, ID, PW, new Sync.UploadRequestCallback() {
                    @Override
                    public void onSuccess(int success, String result) {
                        if (result.equals("success")) {
                            Toast.makeText(context, "업로드 성공 " + success + "건", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(context, "업로드 실패" , Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context, "업로드 실패" + e, Toast.LENGTH_LONG).show();
                    }
                }) {
                };

                DBHelper dbHelper = DBHelper.getInstance(context);
                ArrayList<Book> templist = dbhelper.getbAll();

                int size = templist.size();

                JSONObject[] jasonbook = new JSONObject[size];

                for (int i = 0; i < size; i++) {
                    try {
                        jasonbook[i] = new JSONObject();
                        jasonbook[i].put("username" , ID);
                        jasonbook[i].put("password" , PW);
                        jasonbook[i].put("isbn" , templist.get(i).getISBN());
                        jasonbook[i].put("title" , templist.get(i).getTitle());
                        jasonbook[i].put("booklink" , templist.get(i).getLink());
                        jasonbook[i].put("author" , templist.get(i).getAuthor());
                        jasonbook[i].put("price" , templist.get(i).getPrice());
                        jasonbook[i].put("publisher" , templist.get(i).getPublisher());
                        jasonbook[i].put("date" , templist.get(i).getPubdate());
                        jasonbook[i].put("imagelink" , templist.get(i).getImageURL());
                        jasonbook[i].put("memo" , templist.get(i).getMemo()+" ");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                uploadtask[0].execute(jasonbook);

            }
        });

        downloadbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadtask[0] = new Sync.DownloadRequest(context, new Sync.DownloadRequestCallback() {
                    @Override
                    public void onSuccess(ArrayList<Book> list) {

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context, "다운로드 실패" + e, Toast.LENGTH_LONG).show();
                    }
                });
                downloadtask[0].execute(ID.toString(), PW.toString());
            }
        });

        refreshbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setToLogin();

                ID = loginid.getText().toString();
                PW = loginpw.getText().toString();

                logintask[0] = new Sync.LoginRequest(context, new Sync.LoginRequestCallback() {
                    @Override
                    public void onSuccess(String resultstring, String id) {
                        if (resultstring.equals("LoginSuccess")) {
                            ID = id;
                            Toast.makeText(context, "로그인 성공", Toast.LENGTH_LONG).show();
                            toggleLayout(ID);
                        }
                        else {
                            Toast.makeText(context, "로그인 실패", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context, "로그인 실패" + e, Toast.LENGTH_LONG).show();
                    }
                }) {

                };

                logintask[0].execute(ID, PW);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_4, container, false);
    }

    public void toggleLayout(String id) {
        //loginLayout = getActivity().findViewById(R.id.LoginLayout);
        //syncLayout = getActivity().findViewById(R.id.SyncLayout);

        if ((loginLayout.getVisibility() == getView().GONE) && (syncLayout.getVisibility() == getView().VISIBLE)) {
            //loginLayout.setVisibility(View.VISIBLE);
            //syncLayout.setVisibility(View.GONE);

            setToLogin();
        }
        else if ((loginLayout.getVisibility() == getView().VISIBLE) && (syncLayout.getVisibility() == getView().GONE)) {
            //loginLayout.setVisibility(View.GONE);
            //syncLayout.setVisibility(View.VISIBLE);

            setToSync(id);
        }
        else {
            //loginLayout.setVisibility(View.VISIBLE);
            //syncLayout.setVisibility(View.GONE);

            setToLogin();
        }
    }

    public void setToLogin() {
        //loginLayout = getActivity().findViewById(R.id.LoginLayout);
        //syncLayout = getActivity().findViewById(R.id.SyncLayout);

        loginLayout.setVisibility(View.VISIBLE);
        syncLayout.setVisibility(View.GONE);
    }

    public void setToSync(String id) {
        //loginLayout = getActivity().findViewById(R.id.LoginLayout);
        //syncLayout = getActivity().findViewById(R.id.SyncLayout);

        loginLayout.setVisibility(View.GONE);
        syncLayout.setVisibility(View.VISIBLE);
        usernameText.setText(id);
    }

    DialogInterface.OnClickListener SignupDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(URL + "signup"));
                    startActivity(intent);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;

                default:
                    break;
            }
        }
    };
}
