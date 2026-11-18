package com.bin.qionglouexplorer.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.qionglouexplorer.MainActivity;
import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.adapter.PointAdapter;
import com.bin.qionglouexplorer.adapter.PointHotAdapter;
import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;
import com.bin.qionglouexplorer.data.repo.CultureRepository;
import com.bin.qionglouexplorer.data.repo.MessageRepository;
import com.bin.qionglouexplorer.data.repo.TaskRepository;
import com.bin.qionglouexplorer.ui.detail.DetailActivity;
import com.bin.qionglouexplorer.ui.message.MessageActivity;

import java.util.List;

/** 首页：搜索、Banner、四宫格、热门推荐、附近列表、消息红点。 */
public class HomeFragment extends Fragment {

    private CultureRepository cultureRepo;
    private MessageRepository messageRepo;
    private TaskRepository taskRepo;

    private EditText inputSearch;
    private View messageDot;
    private View groupHot;
    private View groupNear;
    private View groupSearchResult;
    private TextView textSearchSummary;
    private TextView textSearchEmpty;
    private RecyclerView listSearch;

    private PointAdapter searchAdapter;
    private PointAdapter nearAdapter;
    private PointHotAdapter hotAdapter;

    private boolean searchMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cultureRepo = new CultureRepository(requireContext());
        messageRepo = new MessageRepository(requireContext());
        taskRepo = new TaskRepository(requireContext());

        bindViews(view);
        bindAdapters(view);
        bindActions(view);
        observeData();
    }

    private void bindViews(View view) {
        inputSearch = view.findViewById(R.id.input_search);
        messageDot = view.findViewById(R.id.message_dot);
        groupHot = view.findViewById(R.id.group_hot);
        groupNear = view.findViewById(R.id.group_near);
        groupSearchResult = view.findViewById(R.id.group_search_result);
        textSearchSummary = view.findViewById(R.id.text_search_summary);
        textSearchEmpty = view.findViewById(R.id.text_search_empty);
        listSearch = view.findViewById(R.id.list_search_result);
    }

    private void bindAdapters(View view) {
        searchAdapter = new PointAdapter(this::openDetail);
        nearAdapter = new PointAdapter(this::openDetail);
        hotAdapter = new PointHotAdapter(this::openDetail);

        RecyclerView listHot = view.findViewById(R.id.list_hot);
        listHot.setLayoutManager(
                new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        listHot.setAdapter(hotAdapter);

        RecyclerView listNear = view.findViewById(R.id.list_near);
        listNear.setLayoutManager(new LinearLayoutManager(requireContext()));
        listNear.setAdapter(nearAdapter);

        listSearch.setLayoutManager(new LinearLayoutManager(requireContext()));
        listSearch.setAdapter(searchAdapter);
    }

    private void bindActions(View view) {
        view.findViewById(R.id.btn_message).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), MessageActivity.class)));

        view.findViewById(R.id.card_banner).setOnClickListener(v ->
                openPointId(1001));

        view.findViewById(R.id.entry_guide).setOnClickListener(v -> {
            clearSearch();
            // 「景点导览」= 首页的完整点位列表，滚到附近推荐
            View near = getView() == null ? null : getView().findViewById(R.id.group_near);
            if (near != null) {
                near.requestFocus();
            }
        });
        view.findViewById(R.id.entry_task).setOnClickListener(v -> switchTab(R.id.nav_task));
        view.findViewById(R.id.entry_map).setOnClickListener(v -> switchTab(R.id.nav_map));
        view.findViewById(R.id.entry_profile).setOnClickListener(v -> switchTab(R.id.nav_profile));

        view.findViewById(R.id.btn_hot_more).setOnClickListener(v -> switchTab(R.id.nav_map));

        view.findViewById(R.id.btn_search).setOnClickListener(v -> doSearch());
        view.findViewById(R.id.btn_clear_search).setOnClickListener(v -> clearSearch());

        inputSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch();
                return true;
            }
            return false;
        });
        inputSearch.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 && searchMode) {
                    clearSearch();
                }
            }
        });
    }

    private void observeData() {
        cultureRepo.observePoints().observe(getViewLifecycleOwner(), points -> {
            if (searchMode) {
                return;
            }
            hotAdapter.submit(points == null ? null : topRated(points, 6));
            nearAdapter.submit(points == null ? null : nearbySorted(points));
        });

        messageRepo.observeUnreadCount().observe(getViewLifecycleOwner(), count ->
                messageDot.setVisibility(count != null && count > 0 ? View.VISIBLE : View.GONE));
    }

    private List<CulturePointEntity> topRated(List<CulturePointEntity> points, int limit) {
        java.util.List<CulturePointEntity> copy = new java.util.ArrayList<>(points);
        java.util.Collections.sort(copy, (a, b) -> Float.compare(b.rating, a.rating));
        return copy.size() > limit ? copy.subList(0, limit) : copy;
    }

    private List<CulturePointEntity> nearbySorted(List<CulturePointEntity> points) {
        java.util.List<CulturePointEntity> copy = new java.util.ArrayList<>(points);
        java.util.Collections.sort(copy, (a, b) -> Double.compare(
                com.bin.qionglouexplorer.util.GeoUtils.distanceFromAnchor(a.latitude, a.longitude),
                com.bin.qionglouexplorer.util.GeoUtils.distanceFromAnchor(b.latitude, b.longitude)));
        return copy;
    }

    private void doSearch() {
        String keyword = inputSearch.getText().toString().trim();
        if (keyword.isEmpty()) {
            clearSearch();
            return;
        }
        searchMode = true;
        groupSearchResult.setVisibility(View.VISIBLE);
        groupHot.setVisibility(View.GONE);
        groupNear.setVisibility(View.GONE);
        textSearchSummary.setText(String.format("「%s」的搜索结果", keyword));

        cultureRepo.search(keyword).observe(getViewLifecycleOwner(), points -> {
            if (!searchMode) {
                return;
            }
            searchAdapter.submit(points);
            boolean empty = points == null || points.isEmpty();
            textSearchEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        });
    }

    private void clearSearch() {
        searchMode = false;
        inputSearch.setText("");
        groupSearchResult.setVisibility(View.GONE);
        groupHot.setVisibility(View.VISIBLE);
        groupNear.setVisibility(View.VISIBLE);
    }

    private void switchTab(int itemId) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).switchTab(itemId);
        }
    }

    private void openPointId(int pointId) {
        openDetailByPointId(pointId);
    }

    private void openDetail(CulturePointEntity point) {
        openDetailByPointId(point.id);
    }

    private void openDetailByPointId(int pointId) {
        Intent intent = new Intent(requireContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POINT_ID, pointId);
        startActivity(intent);
    }

    /** 简化 TextWatcher，只关心 afterTextChanged。 */
    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }
}
