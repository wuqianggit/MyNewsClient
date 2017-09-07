package com.wq.mynewsclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 新闻客户端案例
 * 从服务器获取 新闻数据，并将数据显示在页面上
 */
public class MainActivity extends AppCompatActivity {

    private String serverIp="";
    EditText et=null;
    TextView tv;
    ListView lv;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String str= (String) msg.obj;
           tv.setText(str);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) this.findViewById(R.id.tv);
        lv= (ListView) this.findViewById(R.id.lv);
    }

    //获取数据的点击事件
    public void getContent(View view){
        et= (EditText) this.findViewById(R.id.et_ip);
        if("".equals(et.getText().toString())){
            Toast.makeText(this.getApplicationContext(),"服务器的地址不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        serverIp=et.getText().toString();
        initData();
    }
    //从服务器取数据
    private void initData() {
        new Thread() {
            @Override
            public void run() {
                try{
                    //从服务期获取数据
                    String path = serverIp;
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置超时时间
                    conn.setConnectTimeout(5000);
                    //设置连接方式
                    conn.setRequestMethod("GET");
                    //获取返回的状态码，只有返回成功，才继续进行
                    final int responseCode = conn.getResponseCode();
                    if(responseCode==200){
                        System.out.println("结果返回了");
                        //获取数据
                        InputStream is = conn.getInputStream();
                        //将这个输入流转化为news对象，并将数据显示在手机上
                        final List<News> newsList = XmlParserUtils.parserXml2News(is);
                        //如何将 news中的数据显示在ListView上？
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lv.setAdapter(new MyAdapter(newsList));
                            }
                        });
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    class MyAdapter extends BaseAdapter{
        private List<News> newsList;
        public MyAdapter(List<News> newsList) {
            this.newsList=newsList;
        }

        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View item=null;
            if(view==null){
                //如果传入的view是空，则新建一个view
                item = View.inflate(getApplicationContext(), R.layout.item, null);
                TextView tv_title=item.findViewById(R.id.tv_title);
                tv_title.setText(newsList.get(i).getTitle());
                TextView tv_desc=item.findViewById(R.id.tv_desc);
                tv_desc.setText(newsList.get(i).getDescription());
                final ImageView iv=item.findViewById(R.id.iv);

                //显示图片（再开一个线程，根据地址取图片，然后将图片放在ImageView上）
                new Thread() {
                    @Override
                    public void run() {
                        try{
                            //从服务期获取数据
                            String path = newsList.get(i).getImage();
                            System.out.println("获取图片的地址："+path);
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            //设置超时时间
                            conn.setConnectTimeout(5000);
                            //设置连接方式
                            conn.setRequestMethod("GET");
                            //获取返回的状态码，只有返回成功，才继续进行
                            int responseCode = conn.getResponseCode();
                            if(responseCode==200){
                                //获取数据
                                InputStream is = conn.getInputStream();
                                //将这个输入流转化为news对象，并将数据显示在手机上
                                final Bitmap bitmap = BitmapFactory.decodeStream(is);
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        iv.setImageBitmap(bitmap);
                                    }
                                });
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();
            }else{
                item=view;
            }
            return item;
        }
    }

    //暂时写一个内部的方法，将输入流 转化为 字符串
    private String inputStream2String(InputStream is){
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        String result="";
        try{
            byte[] buff=new byte[1024];
            int len;
            while((len=is.read(buff))!=-1){
                os.write(buff, len, 0);
            }
            result=new String(os.toByteArray());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //暂时写一个内部的方法，将输入流 转化为 字符串
    private String parser2String(InputStream is){
        String result="";
        StringBuilder sb=new StringBuilder();
        try{
            byte[] buff=new byte[1024];
            int len;
            while((len=is.read(buff))!=-1){

            }
            System.out.println(result);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
