package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Cities extends AppCompatActivity implements DialogDelegate
{
    MyApplication mApp;
    ImageButton mAddCity;
    ListView mList;
    ArrayAdapter<String> mAdapter;
    InputDialog mInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mApp = (MyApplication)getApplicationContext();
        mList = findViewById(R.id.ListView);
        mAddCity = findViewById(R.id.add);

        mInput = new InputDialog(this, "Add City");
        mInput.setDelegate(this);

        View.OnClickListener addListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mInput.show();
            }
        };
        mAddCity.setOnClickListener(addListener);

        mAdapter = new ArrayAdapter<String>(mApp, R.layout.my_list_template, android.R.id.text1, mApp.getCityManager())
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view;
                if (convertView == null)
                {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    view = inflater.inflate(R.layout.my_list_template, parent, false);
                }
                else
                    view = convertView;
                TextView textView = view.findViewById(R.id.textView1);
                String city  = mApp.getCityManager().get(position);
                textView.setText(city);
                return view;
            }
        };

        mList.setAdapter(mAdapter);
        mList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id)
            {
                TextView textView = itemClicked.findViewById(R.id.textView1);
                mApp.getWeather().setCity(textView.getText().toString());

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }


    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }


    @Override
    public void onConfirm()
    {
        String city = mInput.getText();
        if (!city.isEmpty())
        {
            if (mApp.getCityManager().indexOf(city) == -1)
            {
                mApp.getCityManager().add(city);
                mAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(this, "City name can't be empty!", Toast.LENGTH_LONG).show();
        }
    }
}