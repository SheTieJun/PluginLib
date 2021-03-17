package me.shetj.plugindemo.click;

import android.util.Log;
import android.view.View;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import me.shetj.plugindemo.R;


@Aspect
public class SingleClickAspect {

    private static int TIME_TAG = R.id.time_tag;

    private final String POINT_CUT = "execution(@me.shetj.plugindemo.click.SingleClick * *(..)) && @annotation(singleClick)";

    @Pointcut(POINT_CUT)
    public void onSingClickMethod(SingleClick singleClick){

    }

    @Around("onSingClickMethod(singleClick)")//连接点替换
    public void doSingleClickMethod(ProceedingJoinPoint joinPoint , SingleClick singleClick)  {
        Log.w("SingleClick", "view:no singleClick");
        View view = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof View) view = (View) arg;
        }
        if (view != null){
            Object tag = view.getTag(TIME_TAG);

            long lastClickTime = tag!=null? (long) tag :0;


            long currentTime = System.currentTimeMillis();

            long value = singleClick.value();

            if (currentTime - lastClickTime > value){
                view.setTag(TIME_TAG,currentTime);
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }


}
