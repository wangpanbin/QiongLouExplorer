package com.bin.qionglouexplorer.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.qionglouexplorer.BaseActivity;
import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.adapter.PointAdapter;
import com.bin.qionglouexplorer.data.repo.CultureRepository;
import com.bin.qionglouexplorer.ui.detail.DetailActivity;

/** 我的收藏：收藏列表 → 详情。 */
public class FavoriteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);
        applySystemBarInsets(R.id.list_root, true, true);

        ((TextView) findViewById(R.id.text_title)).setText(R.string.favorite_title);
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        TextView empty = findViewById(R.id.text_empty);
        empty.setText(R.string.favorite_empty);

        PointAdapter adapter = new PointAdapter(point -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_POINT_ID, point.id);
            startActivity(intent);
        });

        RecyclerView list = findViewById(R.id.list_points);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        new CultureRepository(this).observeFavorites().observe(this, points -> {
            adapter.submit(points);
            boolean isEmpty = points == null || points.isEmpty();
            empty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            list.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        });
    }
}
