package com.bin.qionglouexplorer.data.repo;

import android.content.Context;

import com.bin.qionglouexplorer.data.local.AppDatabase;
import com.bin.qionglouexplorer.ui.assistant.AssistantEngine;
import com.bin.qionglouexplorer.util.AppExecutors;

/** V0.1 的本地规则实现：每次提问时加载当前知识库后作答。 */
public class LocalRuleAssistant implements AssistantRepository {

    private final AppDatabase db;

    public LocalRuleAssistant(Context context) {
        this.db = AppDatabase.get(context.getApplicationContext());
    }

    @Override
    public void ask(String question, AnswerCallback callback) {
        AppExecutors.io().execute(() -> {
            String answer = build(question);
            AppExecutors.main(() -> callback.onAnswer(answer));
        });
    }

    @Override
    public String askSync(String question) {
        return build(question);
    }

    private String build(String question) {
        AssistantEngine engine = new AssistantEngine(db.pointDao().loadAll());
        return engine.answer(question);
    }
}
