package cc.ibooker.zviewpagerlib;

import android.content.Context;
import android.view.View;

/**
 * 管理视图Holder
 * Created by 邹峰立 on 2017/7/3.
 */
public interface Holder<T> {
    // 创建视图View
    View createView(Context context);

    // 更新UI
    void UpdateUI(Context context, int position, T data);
}
