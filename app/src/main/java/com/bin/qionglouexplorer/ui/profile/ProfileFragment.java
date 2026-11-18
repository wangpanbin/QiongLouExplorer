package com.bin.qionglouexplorer.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.data.repo.ProfileRepository;

/** 个人中心：三统计 + 四入口。 */
public class ProfileFragment extends Fragment {

    private ProfileRepository profileRepo;

    private TextView textNickname;
    private TextView textLevel;
    private TextView textSignature;
    private TextView textExp;
    private TextView textCheckin;
    private TextView textBadge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileRepo = new ProfileRepository(requireContext());

        textNickname = view.findViewById(R.id.text_nickname);
        textLevel = view.findViewById(R.id.text_level);
        textSignature = view.findViewById(R.id.text_signature);
        textExp = view.findViewById(R.id.text_exp);
        textCheckin = view.findViewById(R.id.text_checkin);
        textBadge = view.findViewById(R.id.text_badge);

        view.findViewById(R.id.item_my_task).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), MyTaskActivity.class)));
        view.findViewById(R.id.item_my_favorite).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), FavoriteActivity.class)));
        view.findViewById(R.id.item_offline).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), OfflineActivity.class)));
        view.findViewById(R.id.item_settings).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), SettingsActivity.class)));

        profileRepo.observeProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile == null) {
                return;
            }
            textNickname.setText(profile.nickname);
            textSignature.setText(profile.signature);
            textLevel.setText(getString(R.string.profile_level, profile.level()));
            textExp.setText(String.valueOf(profile.exp));
            textBadge.setText(String.valueOf(profile.badgeCount));
        });

        profileRepo.observeCheckedPointCount().observe(getViewLifecycleOwner(), count ->
                textCheckin.setText(String.valueOf(count == null ? 0 : count)));
    }
}
