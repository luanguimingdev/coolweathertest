package com.coolweather.android.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.coolweather.android.R;
import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
   /***j活动启动开始加在网络信息了*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position , long id) {
                if(currentlevel== LEVEL_PROVINCE){
                         selectProvince =  provinceList.get(position);
                    queryCities();
                    /***
                     * 查询当前的省份内部的城市信息
                     */
                }else if (currentlevel== LEVEL_CITY){
                        selectCity = cityList.get(position);
                    queryCoubties();
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
                    queryCities();
                    /***
                     * 查询市区城市的信息
                     */

                }else if(currentlevel == LEVEL_CITY){
                    queryProvinces();
                    /***
                     * 查询省的信息
                     */
                }
            }
        });
        /**
         * 加载省的信息
         */
        queryProvinces();
    }
    private  void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            datalist.clear();
            for (Province province:provinceList){
                datalist.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listview.setSelection(0);
            currentlevel = LEVEL_PROVINCE;
        }else{

            String address = "http://guolin.tech/api/china";
            Toast.makeText(getActivity(), "网络请求成功", Toast.LENGTH_SHORT).show();
            queryFromServer(address,"province");
        }
    }
    private void queryCities(){
        titleText.setText(selectProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
       cityList = DataSupport.where("provinceId=?",String .valueOf(selectProvince.getId())).find(City.class);
        if(cityList.size()>0){
            datalist.clear();
            for(City city:cityList){
                datalist.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listview.setSelection(0);
            currentlevel = LEVEL_CITY;
        }else{
            int provinceCode = selectProvince.getProvinceCode();

            String address = "http://guolin.tech/api/china/" + provinceCode;
            Toast.makeText(getActivity(), "网络请求成功", Toast.LENGTH_SHORT).show();
            queryFromServer(address,"city");
        }

    }
    private void queryCoubties(){
        titleText.setText(selectCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityId = ?" ,String.valueOf(selectCity.getId())).find(County.class);
        if(countyList.size()>0){
            datalist.clear();
            for(County county:countyList){
                datalist.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listview.setSelection(0);
            currentlevel = LEVEL_COUNTY;
        }else{
            int provinceCode = selectProvince.getProvinceCode();
            int cityCode = selectCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            Toast.makeText(getActivity(), "网络请求成功", Toast.LENGTH_SHORT).show();
            queryFromServer(address,"county");
        }
    }
    private void queryFromServer(String address,final String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                 getActivity().runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         closeProgressDialog();
                         Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                     }
                 });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                boolean  result = false;
                if("province".equals(type)){
                    Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    Utility.handleCityResponse(responseText,selectProvince.getId());
                }else if("county".equals(type)){
                    Utility.handleCountyResponse(responseText,selectCity.getId());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCoubties();
                            }
                        }
                    });
                }
            }
        });
    }
     private void showProgressDialog(){
         if(progressDialog == null){
             progressDialog = new ProgressDialog(getActivity());
             progressDialog.setMessage("正在加载");
             progressDialog.setCanceledOnTouchOutside(false);
         }
         progressDialog.show();
     }
    private void    closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
