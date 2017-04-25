package com.coolweather.android.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.coolweather.android.R;
import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xh on 2017/4/25.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY=2;
    private ProgressDialog progressDialog;
    private TextView titleText ;
    private Button backButton;
    private ListView listview;
    private List<String> datalist = new ArrayList<>();
    private List<Province>  provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectProvince;
    private City selectCity;
    private int currentlevel;
    private ArrayAdapter<String> adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText =(TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listview = (ListView)view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,datalist);
        listview.setAdapter(adapter);
        return view;



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position , long id) {
                if(currentlevel== LEVEL_PROVINCE){
                         selectProvince =  provinceList.get(position);
                    /***
                     * 查询当前的省份内部的城市信息
                     */
                }else if (currentlevel== LEVEL_CITY){
                        selectCity = cityList.get(position);
                    /***
                     * 查询当前城市内部的县信息
                     */
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentlevel==LEVEL_COUNTY){
                    /***
                     * 查询市区城市的信息
                     */

                }else if(currentlevel == LEVEL_CITY){
                    /***
                     * 查询省的信息
                     */
                }
            }
        });
        /**
         * 加载省的信息
         */
    }
    private  void queryProvinces(){
        
    }
}
