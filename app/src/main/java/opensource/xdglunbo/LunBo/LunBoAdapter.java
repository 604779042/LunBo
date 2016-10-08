package opensource.xdglunbo.LunBo;

/**
 * Created by weien on 2016/10/8.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import opensource.xdglunbo.R;

/**
 * Created by weien on 2016/9/28.
 */

public class LunBoAdapter extends PagerAdapter {
    private List<View> list=new ArrayList<View>();
    private Context context;
    private List<String> imgSrcList=new ArrayList<String>();
    private BitmapUtils bitmapUtils;

    public LunBoAdapter(List<View> list, Context context) {
        this.list=list;
        this.context=context;
        bitmapUtils= BitmapHelp.getBitmapUtils(context);  //配置bitmaps
//        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
//        bitmapUtils.configDefaultLoadFailedImage(R.drawable.bitmap);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
    }
    @Override
    public int getCount() {
        Log.e("listsize",list.size()+"");
        return list.size();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(list.get(position));
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager)container).addView(list.get(position));
        Log.e("instantiateItem-list",list.size()+"");
        bitmapUtils.display(((ImageView)(((FrameLayout)list.get(position)).getChildAt(0))), imgSrcList.get(position));
        return list.get(position);
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
    public void addSrc(List<String> liststr){
        this.imgSrcList=liststr;
    }

}
