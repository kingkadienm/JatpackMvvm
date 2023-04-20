package com.wangzs.base.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LanguageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.wangzs.base.R;
import com.wangzs.base.weight.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @Description 切换语言
 * @Date 2022/4/27 027 14:11
 * @Created by wangzs
 */

public class ChangeLanguageActivity extends BaseActivity {


    public static final String LANGUAGE = "language";
    public static final String DEMO_LANGUAGE_CHANGED_ACTION = "demoLanguageChangedAction";

    private OnItemClickListener onItemClickListener;
    //    private TitleBarLayout titleBarLayout;
    private RecyclerView recyclerView;
    //    private final Map<String, String> languageMap = new HashMap<>();
//    private final List<String> languageList = new ArrayList<>();
    private SelectAdapter adapter;
    private Locale currentLanguage;


    private List<Locale> localeList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_change_language;
    }


    @Override
    protected boolean showTitle() {
        return true;
    }

    @Override
    protected void initView() {
        super.initView();


        recyclerView = findViewById(R.id.theme_recycler_view);
        mTitleView.setTitle(getResources().getString(R.string.str_change_language));

        Locale appliedLanguage = LanguageUtils.getAppliedLanguage();

        currentLanguage = appliedLanguage;
        if (currentLanguage == null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                currentLanguage = getResources().getConfiguration().locale;
            } else {
                currentLanguage = getResources().getConfiguration().getLocales().get(0);
            }
        }

        adapter = new SelectAdapter();

        String simplifiedChinese = "简体中文";
        String english = "English";

        localeList.add(Locale.CHINA);
        localeList.add(Locale.US);

//        languageList.add(simplifiedChinese);
//        languageMap.put(simplifiedChinese, "zh");
//        languageList.add(english);
//        languageMap.put(english, "en");

        int indexOf = localeList.indexOf(currentLanguage);

        adapter.setSelectedItem(indexOf);

//        if (TextUtils.equals(currentLanguage, "zh")) {
//            adapter.setSelectedItem(0);
//        } else {
//            adapter.setSelectedItem(1);
//        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.core_list_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        onItemClickListener = new OnItemClickListener() {
            @Override
            public void onClick(Locale language) {
                if (TextUtils.equals(currentLanguage.getLanguage(), language.getLanguage())) {
                    return;
                } else {
                    currentLanguage = language;
                }
                int indexOf = localeList.indexOf(language);
                adapter.setSelectedItem(indexOf);
                adapter.notifyDataSetChanged();

//                LanguageUtils.applyLanguage(Locale.);
                changeTitleLanguage();
                notifyLanguageChanged();
            }
        };
    }

    private void notifyLanguageChanged() {
        LanguageUtils.updateAppContextLanguage(currentLanguage, new Utils.Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                LogUtils.e("切换语言成功:" + aBoolean);
            }
        });
    }

    private void changeTitleLanguage() {
        mTitleView.setTitle(getResources().getString(R.string.str_change_language));
    }

    @Override
    protected void initData() {
        super.initData();
    }

    public static void startSelectLanguage(Activity activity) {
        Intent intent = new Intent(activity, ChangeLanguageActivity.class);
        activity.startActivity(intent);
    }


    class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.SelectViewHolder> {
        int selectedItem = -1;

        public void setSelectedItem(int selectedItem) {
            this.selectedItem = selectedItem;
        }

        @NonNull
        @Override
        public SelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).
                    inflate(R.layout.core_select_item_layout, parent, false);
            return new SelectViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SelectViewHolder holder, int position) {
            Locale language = localeList.get(position);
            holder.name.setText(language.getLanguage());
            if (selectedItem == position) {
                holder.selectedIcon.setVisibility(View.VISIBLE);
            } else {
                holder.selectedIcon.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(language);
                }
            });
        }

        @Override
        public int getItemCount() {
            return localeList.size();
        }

        class SelectViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            ImageView selectedIcon;

            public SelectViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                selectedIcon = itemView.findViewById(R.id.selected_icon);
            }
        }
    }

    public interface OnItemClickListener {
        void onClick(Locale language);
    }
}