package com.bin.qionglouexplorer.ui.message;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.qionglouexplorer.BaseActivity;
import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.adapter.MessageAdapter;
import com.bin.qionglouexplorer.data.local.entity.MessageEntity;
import com.bin.qionglouexplorer.data.repo.MessageRepository;
import com.bin.qionglouexplorer.util.AppExecutors;

/** 消息中心：系统通知 / 好友消息，进入后置为已读。 */
public class MessageActivity extends BaseActivity {

    private MessageRepository messageRepo;
    private MessageAdapter adapter;

    private TextView tabSystem;
    private TextView tabFriend;
    private TextView textEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        applySystemBarInsets(R.id.message_root, true, true);

        messageRepo = new MessageRepository(this);
        adapter = new MessageAdapter();

        RecyclerView list = findViewById(R.id.list_message);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        tabSystem = findViewById(R.id.tab_system);
        tabFriend = findViewById(R.id.tab_friend);
        textEmpty = findViewById(R.id.text_empty);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        tabSystem.setOnClickListener(v -> selectTab(MessageEntity.TYPE_SYSTEM));
        tabFriend.setOnClickListener(v -> selectTab(MessageEntity.TYPE_FRIEND));

        messageRepo.observeMessages().observe(this, messages -> {
            adapter.submit(messages);
            updateEmpty();
        });

        // 进入页面后置为已读，返回首页红点同步消失
        AppExecutors.io().execute(() -> {
            messageRepo.markAllRead();
        });
    }

    private void selectTab(String type) {
        adapter.setFilter(type);
        boolean system = MessageEntity.TYPE_SYSTEM.equals(type);
        tabSystem.setBackgroundResource(system
                ? R.drawable.bg_tab_selected : R.drawable.bg_chip);
        tabSystem.setTextColor(getColor(system
                ? R.color.brand_primary : R.color.text_secondary));
        tabFriend.setBackgroundResource(system
                ? R.drawable.bg_chip : R.drawable.bg_tab_selected);
        tabFriend.setTextColor(getColor(system
                ? R.color.text_secondary : R.color.brand_primary));
        updateEmpty();
    }

    private void updateEmpty() {
        textEmpty.setVisibility(adapter.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
