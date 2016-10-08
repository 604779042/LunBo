package opensource.xdglunbo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opensource.xdglunbo.LunBo.BitmapHelp;
import opensource.xdglunbo.LunBo.LunBo;
import opensource.xdglunbo.LunBo.LunBoAdapter;

public class MainActivity extends Activity {
    private List<View> list=new ArrayList<View>();
    private LunBoAdapter lunBoAdapter;
    private LunBo lunBo;
    private ProgressDialog progressDialog;
    private Handler handler=new Handler();
    String url="http://eladies.sina.com.cn/photo/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
    }
    private void Init(){
        list.clear();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("正在加载");
        progressDialog.show();
        BitmapUtils bitmapUtils= BitmapHelp.getBitmapUtils(this);
        lunBo=(LunBo)findViewById(R.id.lunbo);
        loadImgList(url);
    }

    private void loadImgList(String url) {
        new HttpUtils().send(HttpRequest.HttpMethod.GET, url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progressDialog.cancel();
                        int size=getImgSrcList(responseInfo.result).size();
                        Log.e("listsize",size+"");
                        for(int i=0;i<size;i++){
                            FrameLayout frameLayoutLayoutview=new FrameLayout(MainActivity.this);  //总体的view
                            //容纳圆点的Linearlayout
                            LinearLayout cicle=new LinearLayout(MainActivity.this);
                            cicle.setOrientation(LinearLayout.HORIZONTAL);  //水平的。
                            for(int j=0;j<size;j++){            //设置实心点。
                                ImageView imageView=new ImageView(MainActivity.this);
                                if(i==j){
                                    imageView.setImageResource(R.drawable.shi);
                                }
                                else {
                                    imageView.setImageResource(R.drawable.kong);
                                }
                                LinearLayout.LayoutParams imglayoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
                                imglayoutParams.setMargins(4,4,4,4); //设置圆心之间的距离。
                                imageView.setLayoutParams(imglayoutParams);
                                cicle.addView(imageView);
                            }

                            FrameLayout.LayoutParams  layoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM|Gravity.CENTER);
                            layoutParams.bottomMargin=20; //设置底部的距离
                            layoutParams.leftMargin=40;   //左边距离
                            layoutParams.rightMargin=40;  //右边距离
                            cicle.setLayoutParams(layoutParams);
                            ImageView lunbopic=new ImageView(MainActivity.this);
                            frameLayoutLayoutview.addView(lunbopic);
                            frameLayoutLayoutview.addView(cicle);
                            list.add(frameLayoutLayoutview);
                        }
                        lunBoAdapter=new LunBoAdapter(list,MainActivity.this);
                        lunBoAdapter.addSrc(getImgSrcList(responseInfo.result));
                        lunBo.setAdapter(lunBoAdapter);
                        lunBoAdapter.notifyDataSetChanged();//通知listview更新数据
                        lunBo.setHandler(handler);
                    }
                    @Override
                    public void onFailure(HttpException error, String msg) {
                    }
                });
    }

    /**
     * 得到网页中图片的地址
     */
    public static List<String> getImgSrcList(String htmlStr) {
        List<String> pics = new ArrayList<String>();

        String regEx_img = "<img.*?src=\"http://(.*?).jpg\""; // 图片链接地址
        Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            String src = m_image.group(1);
            if (src.length() < 100) {
                pics.add("http://" + src + ".jpg");
            }
        }
        return pics;
    }
}
