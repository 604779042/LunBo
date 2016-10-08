package opensource.xdglunbo.LunBo;

/**
 * Created by weien on 2016/10/8.
 */

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
/**
 * Created by weien on 2016/9/28.
 */

public class LunBo extends ViewPager {
    private Handler handler;
    private LunboRunnable lunboRunnable;
    private int LunboRight=0;
    private int LunboLeft=1;
    float beginmove=0;
    float finishup=0;
    int position=0;
    @Override
    public Handler getHandler() {
        return handler;
    }
    public void setHandler(Handler handler) {
        this.handler = handler;
        lunboRunnable=new LunboRunnable(LunboRight);
        handler.postDelayed(lunboRunnable,3000);
    }
    public  LunBo(Context context, AttributeSet arrset){
        super(context,arrset);
    }
    public LunBo(Context context){
        super(context);
    }
    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        Log.e("onPageScrolled","position,"+position+",offset"+offset+"");
//        if(offset)
        this.position=position;
        super.onPageScrolled(position, offset, offsetPixels);
    }
    //判断现在的位置然后根据滑动方向决定从哪个方向开始滑动。
    private class LunboRunnable  implements  Runnable{

        public void setDerection(int derection) {
            this.derection = derection;
        }
        int derection;
        public LunboRunnable(int derection) {
            LunBo.this.position=0;
            this.derection=derection;
        }
        @Override
        public void run() {
            if(derection==LunboLeft){ //左滑动
                if(position<=LunBo.this.getAdapter().getCount()&&position>=0) {
                    LunBo.this.setCurrentItem(position--);
                    // position = LunBo.this.getCurrentItem()-1;
                }
                else{
                    position=LunBo.this.getAdapter().getCount();
                }
            }
            if(derection==LunboRight){ //右滑动
                if(position<LunBo.this.getAdapter().getCount()) {
                    LunBo.this.setCurrentItem(position++);
                    //  position = LunBo.this.getCurrentItem()+1;
                }
                else{
                    position=0;
                }
            }
            Log.e("count",LunBo.this.getAdapter().getCount()+"");
            Log.e("currentposition",position+"");
            handler.postDelayed(this,1500);
        }
    }
    //解决左右滑动的问题。

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("MotionEventAction",ev.getAction()+"");
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                beginmove=0;
                finishup=0;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("MotionEventZoom",ev.getX()+"");
                if(beginmove==0) {                    //判断是不是初次点击
                    beginmove = ev.getX();
                }
                break;
            case MotionEvent.ACTION_UP:
                finishup=ev.getX();
                Log.e("movezoom","beginmove"+beginmove+",finishup "+finishup);
                if(finishup>beginmove){  //代表从左向右滑动
                    lunboRunnable.setDerection(LunboLeft);
                }
                else{
                    lunboRunnable.setDerection(LunboRight);
                }
                //   handler.removeCallbacks(lunboRunnable);
                break;
        };

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public int getCurrentItem() {
        //   Log.e("getCurrentItem",getCurrentItem()+"");
        return super.getCurrentItem();
    }
}
