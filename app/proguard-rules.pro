# 琼楼寻踪 · 保留项目自定义规则
# 保留 Room 生成的实体与 DAO
-keep class com.bin.qionglouexplorer.data.local.entity.** { *; }
-keep class com.bin.qionglouexplorer.data.local.** { *; }
# Gson 反射模型
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.bin.qionglouexplorer.data.model.** { *; }
# Gson 泛型
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
# zxing
-keep class com.journeyapps.barcodescanner.** { *; }
