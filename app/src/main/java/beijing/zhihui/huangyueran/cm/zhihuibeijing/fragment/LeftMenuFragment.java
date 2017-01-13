package beijing.zhihui.huangyueran.cm.zhihuibeijing.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import beijing.zhihui.huangyueran.cm.zhihuibeijing.MainActivity;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.R;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.base.impl.NewsCenterPager;
import beijing.zhihui.huangyueran.cm.zhihuibeijing.domain.NewsMenu;

/**
 * Created by huangyueran on 2016/10/29.
 */
public class LeftMenuFragment extends BaseFragment {

    @ViewInject(R.id.lv_list)
    private ListView listView;

    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuData; //侧边栏网络数据对象

    private int mCurrentPosition = 0; //当前被选中的Item位置
    private LeftMenuAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
//        listView = (ListView) view.findViewById(R.id.lv_list);
        ViewUtils.inject(this, view);

        return view;
    }

    @Override
    public void initDate() {

    }


    /**
     * 设置侧边栏数据
     *
     * @param data
     */
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data) {
        mCurrentPosition = 0; //当前选中的位置初始化,归0

        // 更新页面
        mNewsMenuData = data;

        adapter = new LeftMenuAdapter();
        listView.setAdapter(adapter);

        // 设置Item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPosition = position; // 更新被选中的位置
                adapter.notifyDataSetChanged(); // 刷新listView

                // 收起侧边栏
                toggle();

                // 侧边栏点击之后，修改新闻中心fragment中的内容
                setCurrentDetailPager(position);
            }
        });

    }

    /**
     * 设置当前菜单详情页
     *
     * @param position
     */
    private void setCurrentDetailPager(int position) {
        //获取新闻中心对象
        MainActivity mainActivity = (MainActivity) mActivity;

        // 获取ContentFragment
        ContentFragment contentFragment = mainActivity.getContentFragment();
        // 获取NewsCenterPager
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        //修改新闻中心fragment布局
        newsCenterPager.setCurrentDetailPager(position);
    }

    /**
     * 打开或关闭侧边栏
     */
    private void toggle() {
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.toggle(); //如果是开，则关。如果是关，则开。
    }

    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return mNewsMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
            TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);

            NewsMenu.NewsMenuData item = getItem(position);
            tvMenu.setText(item.getTitle());

            if (position == mCurrentPosition) {
                // 被选中
                tvMenu.setEnabled(true);
            } else {
                tvMenu.setEnabled(false);
            }

            return view;
        }
    }
}
