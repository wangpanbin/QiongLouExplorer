package com.bin.qionglouexplorer.ui.assistant;

import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 琼楼小导游的本地规则引擎。
 *
 * 设计底线：答案只能由知识库字段拼装，**不允许生成库外内容**。
 * 因此所有回答都能追溯到某个点位的 era / style / address / description。
 *
 * 三类意图：
 * 1. 点位事实问答（年代、风格、地址、介绍）
 * 2. 点位推荐（"推荐几个必看的地方"）
 * 3. 路线建议（"半天怎么逛"）
 */
public class AssistantEngine {

    private final List<CulturePointEntity> points;

    public AssistantEngine(List<CulturePointEntity> points) {
        this.points = points == null ? new ArrayList<>() : points;
    }

    public String answer(String question) {
        if (question == null || question.trim().isEmpty()) {
            return fallback();
        }
        String q = question.trim();

        CulturePointEntity matched = findMentionedPoint(q);
        if (matched != null) {
            return answerAboutPoint(matched, q);
        }
        if (isRouteIntent(q)) {
            return routeSuggestion();
        }
        if (isRecommendIntent(q)) {
            return recommend();
        }
        if (isFoodIntent(q)) {
            return answerByCategory("food", "海口骑楼片区的美食集中在市井街巷里：");
        }
        if (isFolkIntent(q)) {
            return answerByCategory("folk", "片区的民俗与信仰空间主要有：");
        }
        if (isArchitectureIntent(q)) {
            return answerByCategory("architecture", "骑楼片区的建筑类点位包括：");
        }
        return fallback();
    }

    // ---------- 意图判定 ----------

    private boolean isRouteIntent(String q) {
        return containsAny(q, "路线", "怎么逛", "怎么走", "半天", "一天", "行程", "安排", "顺序");
    }

    private boolean isRecommendIntent(String q) {
        return containsAny(q, "推荐", "必看", "值得", "去哪", "有什么好玩", "介绍几个");
    }

    private boolean isFoodIntent(String q) {
        return containsAny(q, "美食", "吃", "小吃", "海南粉", "老爸茶", "清补凉");
    }

    private boolean isFolkIntent(String q) {
        return containsAny(q, "民俗", "非遗", "庙", "信仰", "妈祖", "香火");
    }

    private boolean isArchitectureIntent(String q) {
        return containsAny(q, "建筑", "骑楼", "立面", "风格", "山花");
    }

    private boolean isEraIntent(String q) {
        return containsAny(q, "年代", "什么时候", "哪一年", "建于", "建成", "多久", "历史");
    }

    private boolean isStyleIntent(String q) {
        return containsAny(q, "风格", "样式", "什么样子", "特色", "看点");
    }

    private boolean isAddressIntent(String q) {
        return containsAny(q, "地址", "在哪", "位置", "怎么去", "怎么走");
    }

    private boolean containsAny(String q, String... keywords) {
        for (String k : keywords) {
            if (q.contains(k)) {
                return true;
            }
        }
        return false;
    }

    // ---------- 回答构建 ----------

    private CulturePointEntity findMentionedPoint(String q) {
        CulturePointEntity best = null;
        int bestLength = 0;
        for (CulturePointEntity p : points) {
            if (p.name == null) {
                continue;
            }
            if (q.contains(p.name) && p.name.length() > bestLength) {
                best = p;
                bestLength = p.name.length();
            }
        }
        return best;
    }

    private String answerAboutPoint(CulturePointEntity p, String q) {
        StringBuilder sb = new StringBuilder();
        if (isEraIntent(q)) {
            sb.append(p.name).append("的建筑年代是「").append(nullToDash(p.era)).append("」。");
            if (p.style != null && !p.style.isEmpty()) {
                sb.append("建筑风格为").append(p.style).append("。");
            }
        } else if (isStyleIntent(q)) {
            sb.append(p.name).append("的建筑风格是").append(nullToDash(p.style)).append("。");
            if (p.summary != null) {
                sb.append("\n").append(p.summary);
            }
        } else if (isAddressIntent(q)) {
            sb.append(p.name).append("位于").append(nullToDash(p.address)).append("。");
        } else {
            sb.append(p.name).append("：").append(nullToDash(p.summary)).append("\n");
            sb.append("建筑年代：").append(nullToDash(p.era))
                    .append("；风格：").append(nullToDash(p.style)).append("。");
        }
        return sb.toString();
    }

    private String recommend() {
        if (points.isEmpty()) {
            return fallback();
        }
        StringBuilder sb = new StringBuilder("按骑行楼片区的顺序，这几处最值得先看：\n");
        int count = Math.min(4, points.size());
        for (int i = 0; i < count; i++) {
            CulturePointEntity p = points.get(i);
            sb.append(i + 1).append(". ").append(p.name)
                    .append(" —— ").append(nullToDash(p.summary)).append("\n");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private String routeSuggestion() {
        if (points.size() < 3) {
            return fallback();
        }
        StringBuilder sb = new StringBuilder("半天路线建议（约 3 小时步行）：\n");
        List<CulturePointEntity> pick = new ArrayList<>();
        for (String category : new String[]{"history", "architecture", "folk", "food"}) {
            for (CulturePointEntity p : points) {
                if (category.equals(p.category) && !pick.contains(p)) {
                    pick.add(p);
                    break;
                }
            }
        }
        for (int i = 0; i < pick.size(); i++) {
            CulturePointEntity p = pick.get(i);
            sb.append(i + 1).append(". ").append(p.name)
                    .append("（").append(nullToDash(p.era)).append("）\n");
        }
        sb.append("最后在水巷口吃一碗海南粉收尾。");
        return sb.toString();
    }

    private String answerByCategory(String category, String lead) {
        StringBuilder sb = new StringBuilder(lead);
        int count = 0;
        for (CulturePointEntity p : points) {
            if (category.equals(p.category)) {
                sb.append("\n").append(count + 1).append(". ").append(p.name)
                        .append(" —— ").append(nullToDash(p.summary));
                count++;
            }
        }
        if (count == 0) {
            return fallback();
        }
        return sb.toString();
    }

    private String fallback() {
        String hot = "";
        if (!points.isEmpty()) {
            hot = points.get(0).name;
        }
        return "这个问题超出了本地文化知识库的范围，我不敢替你编。\n"
                + "可以试着问我：某个点位的年代与风格（例如「" + hot + "建于什么时候」）、"
                + "片区推荐、半天路线，或者海口的美食与民俗。";
    }

    private String nullToDash(String value) {
        return value == null || value.isEmpty() ? "资料待核实" : value;
    }
}
