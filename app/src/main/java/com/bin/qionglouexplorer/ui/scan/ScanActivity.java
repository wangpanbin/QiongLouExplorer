package com.bin.qionglouexplorer.ui.scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.bin.qionglouexplorer.BaseActivity;
import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.data.local.entity.CheckinRecordEntity;
import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;
import com.bin.qionglouexplorer.data.local.entity.ExplorationTaskEntity;
import com.bin.qionglouexplorer.data.repo.CultureRepository;
import com.bin.qionglouexplorer.data.repo.TaskRepository;
import com.bin.qionglouexplorer.ui.detail.DetailActivity;
import com.bin.qionglouexplorer.util.AppExecutors;
import com.bin.qionglouexplorer.util.UiUtils;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

/**
 * 扫码寻宝。
 *
 * 二维码只承载唯一标识 QIONGLOU:POINT:&lt;id&gt;，内容随服务端更新而无需重制二维码。
 * 相机不可用/权限被拒时，手动输入编号是正式功能而非补丁 —— 这让模拟器与演示环境也能跑通闭环。
 */
public class ScanActivity extends BaseActivity {

    public static final String EXTRA_POINT_ID = "extra_point_id";

    private static final String CODE_PREFIX = "QIONGLOU:POINT:";
    private static final int REQ_CAMERA = 1001;

    private DecoratedBarcodeView barcodeView;
    private CultureRepository cultureRepo;
    private TaskRepository taskRepo;
    private boolean handled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        applySystemBarInsets(R.id.scan_root, true, true);

        cultureRepo = new CultureRepository(this);
        taskRepo = new TaskRepository(this);
        barcodeView = findViewById(R.id.barcode_view);

        findViewById(R.id.btn_close).setOnClickListener(v -> finish());
        findViewById(R.id.btn_manual).setOnClickListener(v -> showManualDialog());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startScanning();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQ_CAMERA);
        }
    }

    private void startScanning() {
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result == null || result.getText() == null || handled) {
                    return;
                }
                handleCode(result.getText());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CAMERA) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning();
            } else {
                UiUtils.toast(findViewById(R.id.scan_root),
                        getString(R.string.scan_permission_denied));
                showManualDialog();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            barcodeView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    /** 处理扫码/手动输入得到的字符串。 */
    private void handleCode(String raw) {
        int pointId = parsePointId(raw);
        if (pointId <= 0) {
            handled = false;
            UiUtils.toast(findViewById(R.id.scan_root), getString(R.string.scan_invalid_format));
            return;
        }
        handled = true;
        if (barcodeView != null) {
            barcodeView.pause();
        }
        checkinAndOpen(pointId, CheckinRecordEntity.TYPE_CAMERA_SCAN);
    }

    /** 兼容纯数字、带前缀、以及带 URL 包装的三类输入。 */
    public static int parsePointId(String raw) {
        if (raw == null) {
            return -1;
        }
        String text = raw.trim();
        if (text.isEmpty()) {
            return -1;
        }
        String digits;
        int idx = text.lastIndexOf(CODE_PREFIX);
        if (idx >= 0) {
            digits = text.substring(idx + CODE_PREFIX.length());
        } else if (text.matches("\\d+")) {
            digits = text;
        } else if (text.toUpperCase().startsWith("QIONGLOU")) {
            int colon = text.lastIndexOf(':');
            if (colon < 0 || colon == text.length() - 1) {
                return -1;
            }
            digits = text.substring(colon + 1);
        } else {
            return -1;
        }
        try {
            return Integer.parseInt(digits.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void checkinAndOpen(int pointId, String type) {
        AppExecutors.io().execute(() -> {
            CulturePointEntity point = cultureRepo.loadPointSync(pointId);
            if (point == null) {
                AppExecutors.main(() -> {
                    handled = false;
                    UiUtils.toast(findViewById(R.id.scan_root),
                            getString(R.string.scan_not_found, String.valueOf(pointId)));
                    if (barcodeView != null) {
                        barcodeView.resume();
                    }
                });
                return;
            }
            ExplorationTaskEntity task = taskRepo.loadTaskByPointSync(pointId);
            int rewardExp = task != null ? task.rewardExp : 0;
            int rewardPoints = task != null ? task.rewardPoints : 0;
            taskRepo.submitCheckin(pointId, 0, type, rewardExp, rewardPoints, result -> {
                if (result.duplicated) {
                    UiUtils.toast(findViewById(R.id.scan_root),
                            getString(R.string.scan_already));
                } else {
                    UiUtils.toast(findViewById(R.id.scan_root),
                            getString(R.string.scan_success, result.rewardExp));
                }
                openDetail(pointId);
            });
        });
    }

    private void showManualDialog() {
        EditText input = new EditText(this);
        input.setHint(R.string.scan_manual_hint);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        int pad = UiUtils.dp(this, 20);
        FrameLayout container = new FrameLayout(this);
        container.setPadding(pad, UiUtils.dp(this, 8), pad, 0);
        container.addView(input);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.scan_manual)
                .setView(container)
                .setNegativeButton(R.string.scan_cancel, null)
                .setPositiveButton(R.string.scan_confirm, null)
                .create();
        dialog.show();
        // 覆盖默认点击行为，避免空输入直接关闭
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String value = input.getText().toString().trim();
            if (value.isEmpty()) {
                input.setError(getString(R.string.scan_invalid_format));
                return;
            }
            int pointId = parsePointId(value);
            if (pointId <= 0) {
                input.setError(getString(R.string.scan_invalid_format));
                return;
            }
            dialog.dismiss();
            handled = true;
            checkinAndOpen(pointId, CheckinRecordEntity.TYPE_MANUAL_CODE);
        });
    }

    private void openDetail(int pointId) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POINT_ID, pointId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        barcodeView = null;
    }

    /** 供调试：返回可用编号列表（配合 assets 种子数据）。 */
    static String debugHint(List<CulturePointEntity> points) {
        if (points == null || points.isEmpty()) {
            return "";
        }
        return String.valueOf(points.get(0).id);
    }
}
