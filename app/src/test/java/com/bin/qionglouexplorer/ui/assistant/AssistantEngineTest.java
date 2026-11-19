package com.bin.qionglouexplorer.ui.assistant;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则引擎的核心约束验证：
 * 1. 命中点位时答案必须包含该点位的库内字段
 * 2. 未命中时必须走兜底话术，不允许编造
 */
public class AssistantEngineTest {

    private AssistantEngine engine;

    @Before
    public void setUp() {
        List<CulturePointEntity> points = new ArrayList<>();
        points.add(point("文昌阁", "architecture", "明代", "海南传统楼阁",
                "海口市龙华区中山路", "海南重要的文化地标"));
        points.add(point("西天庙", "folk", "明清以降，历代重修", "海南传统庙宇建筑",
                "海口市龙华区义兴街", "骑楼片区内的民间信仰场所"));
        points.add(point("水巷口", "food", "清代—民国延续至今", "骑楼街巷与市集空间",
                "海口市龙华区水巷口街", "老海口的市井入口"));
        points.add(point("海口骑楼老街", "architecture", "19 世纪末—20 世纪初", "南洋骑楼",
                "海口市龙华区中山路一带", "海口最具代表性的历史文化街区"));
        engine = new AssistantEngine(points);
    }

    private CulturePointEntity point(String name, String category, String era, String style,
                                    String address, String summary) {
        CulturePointEntity p = new CulturePointEntity();
        p.name = name;
        p.category = category;
        p.era = era;
        p.style = style;
        p.address = address;
        p.summary = summary;
        p.description = summary;
        return p;
    }

    @Test
    public void eraQuestionReturnsEraFromKnowledgeBase() {
        String answer = engine.answer("文昌阁建于什么时候？");
        assertTrue(answer, answer.contains("文昌阁"));
        assertTrue(answer, answer.contains("明代"));
    }

    @Test
    public void styleQuestionReturnsStyle() {
        String answer = engine.answer("西天庙的建筑风格是什么");
        assertTrue(answer, answer.contains("海南传统庙宇建筑"));
    }

    @Test
    public void addressQuestionReturnsAddress() {
        String answer = engine.answer("水巷口在哪");
        assertTrue(answer, answer.contains("水巷口街"));
    }

    @Test
    public void recommendIntentListsPoints() {
        String answer = engine.answer("推荐几个必看的地方");
        assertTrue(answer, answer.contains("文昌阁"));
        assertTrue(answer, answer.contains("水巷口"));
    }

    @Test
    public void routeIntentProducesNumberedPlan() {
        String answer = engine.answer("半天怎么逛？");
        assertTrue(answer, answer.contains("半天路线建议"));
        assertTrue(answer, answer.contains("1."));
    }

    @Test
    public void foodIntentUsesFoodCategory() {
        String answer = engine.answer("海口有什么好吃的");
        assertTrue(answer, answer.contains("水巷口"));
        assertFalse(answer, answer.contains("西天庙"));
    }

    @Test
    public void unknownQuestionFallsBackWithoutInventing() {
        String answer = engine.answer("今天天气怎么样");
        assertTrue(answer, answer.contains("超出了本地文化知识库的范围"));
        assertTrue(answer, answer.contains("不敢替你编"));
    }

    @Test
    public void emptyQuestionReturnsFallback() {
        String answer = engine.answer("   ");
        assertNotNull(answer);
        assertTrue(answer, answer.contains("文化知识库"));
    }

    @Test
    public void emptyKnowledgeBaseStillSafelyAnswers() {
        AssistantEngine empty = new AssistantEngine(new ArrayList<>());
        String answer = empty.answer("推荐几个必看的地方");
        assertNotNull(answer);
        assertFalse(answer.isEmpty());
    }

    @Test
    public void longestNameWinsWhenMultipleMentioned() {
        // 「海口骑楼老街」比「骑楼老街」更长，应命中前者
        String answer = engine.answer("海口骑楼老街的建筑年代");
        assertTrue(answer, answer.contains("19 世纪末"));
    }
}
