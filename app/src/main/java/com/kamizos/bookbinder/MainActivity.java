package com.kamizos.bookbinder;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;


import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kamizos.bookbinder.ui.main.SectionsPagerAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button scanner;
    static RecyclerView isbnrecyclerView;
    static RecyclerView booklistrecyclerView;
    FloatingActionButton fab1;
    FloatingActionButton fab3;
    static TabLayout tabs;

    static String nKey;
    static String nSec;


    DBHelper dbhelper;
    static ArrayList<Book> booklist;
    static ArrayList<ISBN> isbnlist;
    static ISBNView iadapter;
    static BookView badapter;
    IntentIntegrator scan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        booklistrecyclerView = findViewById(R.id.bookrecyclerView);
        isbnrecyclerView = findViewById(R.id.isbnrecyclerView);
        dbhelper = DBHelper.getInstance(getApplicationContext());
        booklist = dbhelper.getbAll();
        isbnlist = dbhelper.getiAll();
        iadapter = new ISBNView(isbnlist);
        badapter = new BookView(getApplicationContext(),booklist);


        try {
             nKey = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("naverApiKeyID");
             nSec = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("naverApiKeySEC");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);



        final NBApiAsyncTask[] task = new NBApiAsyncTask[1];



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }




            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        brefresh();


                        break;

                    case 1:

                        fab3 = findViewById(R.id.fab3);
                        fab3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Fragment2.savePage(booklist);
                                    Snackbar.make(view, "저장 성공!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }catch (Exception e){
                                    Snackbar.make(view, "저장 실패", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            }
                        });
                        break;

                    case 2:

                        //samplecode();
                        irefresh();



                        scanner = findViewById(R.id.scannerbutton);
                        scanner.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                scan = new IntentIntegrator(MainActivity.this);
                                scan.initiateScan();
                            }
                        });




                        fab1 = findViewById(R.id.fab1);
                        fab1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (isbnlist.size() < 1) {
                                    Snackbar.make(view, "검색 할 ISBN이 없습니다. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }else {
                                    Snackbar.make(view, "검색 시작. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    task[0] = new NBApiAsyncTask(MainActivity.this, nKey, nSec, new NBApiCallback() {

                                        @Override
                                        public void onSuccess(ArrayList<Book> booklistResult) {
                                            if (booklistResult.size() < 1) {
                                            }else {
                                                try {
                                                    Boolean alreadyrec = false;
                                                    for (int i = 0; i < booklistResult.size(); i++) {
                                                        for (int j = 0; j < booklist.size(); j++) {
                                                            if (booklist.get(j).getISBN().equals(booklistResult.get(i).getISBN())) {
                                                                alreadyrec = true;
                                                            }
                                                        }
                                                        if (alreadyrec) {
                                                            Toast.makeText(getApplicationContext(), booklistResult.get(i).getISBN() + " 이미 등록된 도서의 ISBN 입니다.", Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            dbhelper.insert(booklistResult.get(i));
                                                            booklist.add(booklistResult.get(i));
                                                        }
                                                        alreadyrec = false;
                                                        for (int j = 0; j < isbnlist.size(); j++) {
                                                            if (isbnlist.get(j).getISBN().equals(booklistResult.get(i).getISBN())) {
                                                                dbhelper.idelete(isbnlist.get(j).getISBN());
                                                                isbnlist.remove(j);
                                                                irefresh();
                                                            }
                                                        }
                                                    }

                                                    Toast.makeText(getApplicationContext(), "리스트 입력 성공!", Toast.LENGTH_SHORT).show();


                                                } catch (Exception e) {
                                                    Toast.makeText(getApplicationContext(), "리스트 입력 실패!" + e.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(getApplicationContext(),"API 파싱 스레드 작업 실패! \n" + e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    task[0].execute(isbnlist);



                                }

                            }
                        });
                        break;

                    case 3:
                        //fab4 = findViewById(R.id.AccountRefresh);

                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,"No contents", Toast.LENGTH_LONG);
            } else { //QR코드, 내용 존재
                try {
                    /* QR 코드 내용*/
                    /* 로직
                     *
                     * 로직 끝 */
                    Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_LONG).show();
                    if (result.getContents() != null) {
                        
                        String resultstr = result.getContents();
                        String comparestr;

                        int compare = 0;
                        if (isbnlist.size() > 0) {
                            for (int i = 0; i < isbnlist.size(); i++) {
                                comparestr = isbnlist.get(i).getISBN().toString().substring(0,13);
                                if (resultstr.equals(comparestr) == true) {
                                    compare = 1;
                                }
                            }
                        }

                        if (compare == 1) {
                            Toast.makeText(this, "중복된 ISBN 입니다.", Toast.LENGTH_LONG).show();
                        }else {
                            ISBN temp = new ISBN(resultstr);
                            isbnlist.add(temp);
                            dbhelper.iinsert(temp);
                        }
                    }
                    scan.initiateScan();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Exception Occured", Toast.LENGTH_LONG);
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        iadapter.notifyDataSetChanged();
    }

    public void brefresh() {
        //booklist.clear();
        //booklist = dbhelper.getbAll();
        //badapter.setbData(booklist);
        badapter.notifyDataSetChanged();
    }

    public void irefresh() {
        //isbnlist.clear();
        //isbnlist = dbhelper.getiAll();
        //iadapter.setiData(isbnlist);
        iadapter.notifyDataSetChanged();
    }

    public void samplecode() {
        ISBN temp = new ISBN("9791161908021");
        isbnlist.add(temp);
        dbhelper.iinsert(temp);
        temp = new ISBN("9788926360200");
        isbnlist.add(temp);
        dbhelper.iinsert(temp);
        temp = new ISBN("8806035000853");
        isbnlist.add(temp);
        dbhelper.iinsert(temp);
        iadapter.notifyDataSetChanged();
    }

}