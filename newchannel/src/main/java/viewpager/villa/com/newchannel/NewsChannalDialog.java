package viewpager.villa.com.newchannel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/10/13 0013.
 */
public class NewsChannalDialog extends Dialog {
    private static final String TAG = "NewsChannalDialog";

    public NewsChannalDialog(Context context) {
        this(context,-1);
    }

    public NewsChannalDialog(Context context, int themeResId) {
        super(context, R.style.DialogTheme);
        init();
    }

    private void init() {
        setContentView(R.layout.new_channel_dialog);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width =WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height =WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.TOP;
        setCanceledOnTouchOutside(true);
        getWindow().setAttributes(attributes);
    }
}
