# -*- coding: utf-8 -*-
"""一次性生成琼楼寻踪 V0.1 的矢量与形状 drawable 资源。"""
import os

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
RES = os.path.join(ROOT, "app", "src", "main", "res")
DRAWABLE = os.path.join(RES, "drawable")

ICON_STROKE = {
    "ic_tab_home": "M3,10.8 L12,3.5 L21,10.8 M5.6,9.2 V20.3 H10 V14.2 H14 V20.3 H18.4 V9.2",
    "ic_tab_map": "M9,3.5 L3,6 V20.5 L9,18 L15,20.5 L21,18 V3.5 L15,6 Z M9,3.5 V18 M15,6 V20.5",
    "ic_tab_task": "M3.5,6.5 L5.6,8.6 L9.2,5 M3.5,12.5 L5.6,14.6 L9.2,11 M3.5,18.5 L5.6,20.6 L9.2,17 M12.5,7 H20.5 M12.5,13 H20.5 M12.5,19 H20.5",
    "ic_tab_assistant": "M12,3.2 C7.2,3.2 4.2,6.6 4.2,10.9 c0,1.9 0.7,3.6 1.9,4.9 V20.8 L9.4,18.6 c0.8,0.16 1.7,0.24 2.6,0.24 4.8,0 7.8,-3.4 7.8,-7.9 S16.8,3.2 12,3.2 Z M9.3,10.6 h0.01 M14.7,10.6 h0.01",
    "ic_tab_profile": "M12,11.4 m-3.9,0 a3.9,3.9 0 1,0 7.8,0 a3.9,3.9 0 1,0 -7.8,0 M4.4,20.2 c0,-3.7 3.5,-5.6 7.6,-5.6 s7.6,1.9 7.6,5.6",
    "ic_search": "M11,4.2 a6.8,6.8 0 1,0 0,13.6 a6.8,6.8 0 1,0 0,-13.6 M15.8,15.8 L20.5,20.5",
    "ic_message": "M3.6,5.6 H20.4 V16.1 H12.2 L7.4,20.2 V16.1 H3.6 Z",
    "ic_scan": "M4,8.6 V4.2 H8.4 M15.6,4.2 H20 V8.6 M20,15.4 V19.8 H15.6 M8.4,19.8 H4 V15.4 M3.6,12 H20.4",
    "ic_favorite_outline": "M12,20.4 L4.7,13.1 C2.2,10.6 3,6.9 6.1,5.8 c2,-0.7 4.4,0.1 5.9,2 c1.5,-1.9 3.9,-2.7 5.9,-2 c3.1,1.1 3.9,4.8 1.4,7.3 Z",
    "ic_navigate": "M20.6,3.4 L3.6,10.6 L10.6,13.4 L13.4,20.4 Z",
    "ic_star": "M12,3.4 L14.7,9.2 L21,10.1 L16.4,14.5 L17.6,20.7 L12,17.7 L6.4,20.7 L7.6,14.5 L3,10.1 L9.3,9.2 Z",
    "ic_badge": "M12,3.2 L14.7,5.4 L18.1,5.4 L18.1,8.8 L20.7,11.3 L18.4,13.9 L18.4,17.3 L15,17.3 L12,19.5 L9,17.3 L5.6,17.3 L5.6,13.9 L3.3,11.3 L5.9,8.8 L5.9,5.4 L9.3,5.4 Z M9.3,11.2 L11.2,13.1 L15,9.3",
    "ic_mic": "M12,3.4 a2.7,2.7 0 0,1 2.7,2.7 v5.3 a2.7,2.7 0 0,1 -5.4,0 V6.1 A2.7,2.7 0 0,1 12,3.4 Z M6.4,11 a5.6,5.6 0 0,0 11.2,0 M12,16.6 V20.4 M8.8,20.4 H15.2",
    "ic_back": "M15,4.6 L7.6,12 L15,19.4",
    "ic_more": "M6,12 h0.01 M12,12 h0.01 M18,12 h0.01",
    "ic_location": "M12,2.6 C8.4,2.6 5.6,5.4 5.6,9 c0,4.9 6.4,12.4 6.4,12.4 s6.4,-7.5 6.4,-12.4 C18.4,5.4 15.6,2.6 12,2.6 Z M12,11.7 m-1.9,0 a1.9,1.9 0 1,0 3.8,0 a1.9,1.9 0 1,0 -3.8,0",
    "ic_check": "M4.8,12.4 L9.4,17 L19.2,6.8",
    "ic_clock": "M12,3.4 a8.6,8.6 0 1,0 0,17.2 a8.6,8.6 0 1,0 0,-17.2 M12,6.8 V12 L15.6,14.2",
    "ic_send": "M3.4,11.4 L20.6,4 L13.6,21.2 L11.1,13.6 Z",
    "ic_settings": "M12,8.6 a3.4,3.4 0 1,0 0,6.8 a3.4,3.4 0 1,0 0,-6.8 M12,2.6 V5.2 M12,18.8 V21.4 M2.6,12 H5.2 M18.8,12 H21.4 M5.2,5.2 L7,7 M17,17 L18.8,18.8 M18.8,5.2 L17,7 M7,17 L5.2,18.8",
    "ic_coin": "M12,3.4 a8.6,8.6 0 1,0 0,17.2 a8.6,8.6 0 1,0 0,-17.2 M12,7.4 v9.2 M9.6,9.6 h4 a1.8,1.8 0 0,1 0,3.6 h-4 a1.8,1.8 0 0,0 0,3.6 h4",
    "ic_exp": "M12,2.8 L14.6,9.4 L21.6,12 L14.6,14.6 L12,21.2 L9.4,14.6 L2.4,12 L9.4,9.4 Z",
    "ic_chevron_right": "M9.2,4.8 L16.4,12 L9.2,19.2",
    "ic_camera": "M4,7.6 H8.2 L9.8,5.4 H14.2 L15.8,7.6 H20 V19 H4 Z M12,9.6 a4,4 0 1,0 0,8 a4,4 0 1,0 0,-8",
    "ic_lock": "M6.4,10.4 H17.6 V20 H6.4 Z M8.6,10.4 V7.4 a3.4,3.4 0 0,1 6.8,0 V10.4",
    "ic_offline": "M3,3 L21,21 M6.6,8.6 a11,11 0 0,1 3.6,-2 M2.4,5.4 a16,16 0 0,1 5,-3 M17.4,8.6 a11,11 0 0,1 2.4,3.4 M21.6,5.4 a16,16 0 0,0 -7.6,-3.2 M12,18.6 h0.01",
    "ic_building": "M4,20.4 V6.2 L12,2.4 L20,6.2 V20.4 Z M9,20.4 V13 H15 V20.4 M7.6,9.4 h0.01 M12,9.4 h0.01 M16.4,9.4 h0.01 M7.6,16.4 h0.01 M16.4,16.4 h0.01",
    "ic_pagoda": "M12,2.6 L19,7 H5 Z M7,8 H17 L18.4,12 H5.6 Z M5.6,13 H18.4 L19.6,17 H4.4 Z M4.4,18 H19.6 V20.6 H4.4 Z",
    "ic_temple": "M12,2.6 L20,8 H4 Z M6,9.4 V18 M10,9.4 V18 M14,9.4 V18 M18,9.4 V18 M4,20.4 H20",
    "ic_food": "M6,3.4 V10 a2.4,2.4 0 0,0 4.8,0 V3.4 M8.4,3.4 V20.4 M16,3.4 C18.4,3.4 19.6,5.6 19.6,8.4 C19.6,10.6 18.4,12 16.8,12 V20.4",
    "ic_history": "M12,7.4 V12 L15.4,14 M3.6,12 a8.4,8.4 0 1,0 2.6,-6.1 M3.6,5 V9.4 H8",
    "ic_list": "M8,6.4 H20 M8,12 H20 M8,17.6 H20 M4,6.4 h0.01 M4,12 h0.01 M4,17.6 h0.01",
    "ic_person_add": "M9.6,11.4 m-3.5,0 a3.5,3.5 0 1,0 7,0 a3.5,3.5 0 1,0 -7,0 M2.8,19.6 c0,-3.2 3,-4.9 6.8,-4.9 1,0 1.9,0.1 2.7,0.3 M17.6,14.4 V20.4 M15,17.4 H20.2",
    "ic_calendar": "M4,6.4 H20 V20 H4 Z M4,10.6 H20 M8.4,3.6 V7.4 M15.6,3.6 V7.4",
    "ic_close": "M5.6,5.6 L18.4,18.4 M18.4,5.6 L5.6,18.4",
    "ic_refresh": "M20,12 a8,8 0 1,1 -2.6,-5.9 M20,4 V9.4 H14.6",
    "ic_home_pin": "M3.6,10.6 L12,3.8 L20.4,10.6 M6,9 V20 H18 V9 M10,20 V14 H14 V20",
}

FILLED = {
    "ic_favorite_filled": "M12,20.4 L4.7,13.1 C2.2,10.6 3,6.9 6.1,5.8 c2,-0.7 4.4,0.1 5.9,2 c1.5,-1.9 3.9,-2.7 5.9,-2 c3.1,1.1 3.9,4.8 1.4,7.3 Z",
    "ic_map_pin": "M12,2.6 C8.4,2.6 5.6,5.4 5.6,9 c0,4.9 6.4,12.4 6.4,12.4 s6.4,-7.5 6.4,-12.4 C18.4,5.4 15.6,2.6 12,2.6 Z M12,8.4 a2.4,2.4 0 1,0 0,4.8 a2.4,2.4 0 1,0 0,-4.8",
}

VECTOR_TMPL = """<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:pathData="{path}"
        android:strokeColor="#FF1F1B2E"
        android:strokeWidth="1.8"
        android:strokeLineCap="round"
        android:strokeLineJoin="round" />
</vector>
"""

VECTOR_FILLED_TMPL = """<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:pathData="{path}"
        android:fillColor="#FF1F1B2E" />
</vector>
"""


def write(path, content):
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)


for name, path in ICON_STROKE.items():
    write(os.path.join(DRAWABLE, name + ".xml"), VECTOR_TMPL.format(path=path))

for name, path in FILLED.items():
    write(os.path.join(DRAWABLE, name + ".xml"), VECTOR_FILLED_TMPL.format(path=path))

# ---------- 形状 / 渐变 ----------

SHAPES = {
    "bg_card_white": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="@color/bg_card" />
    <corners android:radius="16dp" />
    <stroke android:width="1dp" android:color="@color/divider" />
</shape>
""",
    "bg_card_white_r12": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="@color/bg_card" />
    <corners android:radius="12dp" />
    <stroke android:width="1dp" android:color="@color/divider" />
</shape>
""",
    "bg_chip": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="@color/bg_chip" />
    <corners android:radius="16dp" />
</shape>
""",
    "bg_search": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="@color/bg_card" />
    <corners android:radius="22dp" />
    <stroke android:width="1dp" android:color="@color/stroke_soft" />
</shape>
""",
    "bg_top_gradient": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <gradient
        android:angle="270"
        android:startColor="@color/bg_top_gradient_start"
        android:endColor="@color/bg_top_gradient_end"
        android:type="linear" />
</shape>
""",
    "bg_splash": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <gradient
        android:angle="315"
        android:startColor="@color/bg_splash_top"
        android:centerColor="#4B36A0"
        android:endColor="@color/bg_splash_bottom"
        android:type="linear" />
</shape>
""",
    "bg_banner": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <gradient
        android:angle="315"
        android:startColor="#8E74FF"
        android:endColor="#5B3FD9"
        android:type="linear" />
    <corners android:radius="20dp" />
</shape>
""",
    "bg_bubble_user": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="@color/brand_primary" />
    <corners
        android:topLeftRadius="18dp"
        android:topRightRadius="18dp"
        android:bottomLeftRadius="18dp"
        android:bottomRightRadius="4dp" />
</shape>
""",
    "bg_bubble_bot": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="@color/bg_card" />
    <corners
        android:topLeftRadius="18dp"
        android:topRightRadius="4dp"
        android:bottomLeftRadius="18dp"
        android:bottomRightRadius="18dp" />
    <stroke android:width="1dp" android:color="@color/divider" />
</shape>
""",
    "bg_badge_dot": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="oval">
    <solid android:color="#FFE5484D" />
    <stroke android:width="1.5dp" android:color="@color/bg_card" />
</shape>
""",
    "bg_stat_circle": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="oval">
    <solid android:color="@color/brand_primary_soft" />
</shape>
""",
    "bg_offline_banner": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="#FFFFF3E0" />
    <corners android:radius="10dp" />
</shape>
""",
    "bg_cover_1": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <gradient android:angle="315" android:startColor="#C9B8FF" android:endColor="#8E74FF" android:type="linear" />
</shape>
""",
    "bg_cover_2": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <gradient android:angle="315" android:startColor="#FFD3B6" android:endColor="#FF9A6C" android:type="linear" />
</shape>
""",
    "bg_cover_3": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <gradient android:angle="315" android:startColor="#A8D8C0" android:endColor="#2FBF71" android:type="linear" />
</shape>
""",
    "bg_cover_4": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <gradient android:angle="315" android:startColor="#AFD8F0" android:endColor="#3BA3E0" android:type="linear" />
</shape>
""",
    "bg_cover_5": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <gradient android:angle="315" android:startColor="#FFD9E4" android:endColor="#F0688C" android:type="linear" />
</shape>
""",
    "bg_cover_6": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <gradient android:angle="315" android:startColor="#D8D2F5" android:endColor="#6B5CB8" android:type="linear" />
</shape>
""",
    "bg_avatar": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="oval">
    <gradient android:angle="315" android:startColor="#C9B8FF" android:endColor="#7C5CFF" android:type="linear" />
</shape>
""",
    "bg_avatar_ring": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="oval">
    <solid android:color="@color/bg_card" />
    <stroke android:width="2dp" android:color="@color/brand_primary_soft" />
</shape>
""",
    "bg_sheet_top": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="@color/bg_card" />
    <corners android:topLeftRadius="22dp" android:topRightRadius="22dp" />
</shape>
""",
    "bg_progress_track": """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="#1AFFFFFF" />
    <corners android:radius="6dp" />
</shape>
""",
}

for name, content in SHAPES.items():
    write(os.path.join(DRAWABLE, name + ".xml"), content)

print("drawables:", len(ICON_STROKE) + len(FILLED) + len(SHAPES))
