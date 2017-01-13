package beijing.zhihui.huangyueran.cm.zhihuibeijing.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huangyueran on 2016/10/29.
 */
public abstract class BaseFragment extends Fragment {

    protected FragmentActivity mActivity;

    //Fragment创建
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity(); // 获取Fragment当前依赖的Activity

    }

    //初始化 Fragment的布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    //fragment依赖的activity的OnCreate方法执行结束
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化数据
        initDate();
    }

    // 初始化布局，必须由子类实现
    public abstract View initView();

    //初始化数据，由子类实现
    public abstract void initDate();
}
