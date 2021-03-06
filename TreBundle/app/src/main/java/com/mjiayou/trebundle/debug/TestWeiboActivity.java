package com.mjiayou.trebundle.debug;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mjiayou.trebundle.R;
import com.mjiayou.trecore.base.TCActivity;
import com.mjiayou.trecore.net.RequestAdapter;
import com.mjiayou.trecore.util.ConvertUtil;
import com.mjiayou.trecorelib.bean.TCSinaStatusesResponse;
import com.mjiayou.trecorelib.util.PageUtil;
import com.mjiayou.trecorelib.util.ToastUtil;

import java.util.ArrayList;

import butterknife.InjectView;

public class TestWeiboActivity extends TCActivity {

    @InjectView(R.id.listview)
    PullToRefreshListView mListView;

    private ArrayList<String> mList = new ArrayList<>();
    private ArrayAdapter mAdapter;

    private boolean isAutoLoadMore = false;

    /**
     * startActivity
     */
    public static void open(Context context) {
        Intent intent = new Intent(context, TestWeiboActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_weibo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTitleBar().setTitle("TestWeiboActivity");

        initView();
        mListView.autoPullDownToRefresh();
    }

    @Override
    public void initView() {
        super.initView();

        // 设置下拉刷新模式
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        // 监听下拉上拉事件
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMoreData();
            }
        });

        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(mAdapter);
        // 监听Item点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int positionReal = position - 1;
                ToastUtil.show(mList.get(positionReal));
            }
        });
        // 监听正在滚动事件
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 判断滚动到底部
                if (!isAutoLoadMore && view.getLastVisiblePosition() == (view.getCount() - 3)) {
                    isAutoLoadMore = true;
                    mListView.autoPullUpToLoadMore();
                }
            }
        });
    }

    @Override
    public void getData(int pageNumber) {
        super.getData(pageNumber);
        getRequestAdapter().sinaStatuses(String.valueOf(pageNumber));
    }

    @Override
    public void refreshData() {
        super.refreshData();
        showLoading(true);
        getData(PageUtil.pageReset());
    }

    @Override
    public void loadMoreData() {
        super.loadMoreData();
        getData(PageUtil.pageAdd());
    }

    @Override
    public void callback(Message msg) {
        super.callback(msg);
        switch (msg.what) {
            case RequestAdapter.SINA_STATUSES:
                showLoading(false);
                TCSinaStatusesResponse response = (TCSinaStatusesResponse) msg.obj;
                if (response != null) {
                    if (PageUtil.isPullRefresh()) {
                        mList.clear();
                    }
                    refreshView(response);
                }
                mListView.onRefreshComplete();
                isAutoLoadMore = false;
                break;
        }
    }

    public void refreshView(TCSinaStatusesResponse response) {
        mList.addAll(ConvertUtil.parseStringList(response.getStatuses()));
        mAdapter.notifyDataSetChanged();
    }
}
