package cc.ibooker.zviewpagerlib;

/**
 * 点击事件处理
 */
public class ZViewPagerClickUtil {
    private static long lastClickTime;

    // 判断是否为快速点击事件
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500)
            return true;
        lastClickTime = time;
        return false;
    }
}