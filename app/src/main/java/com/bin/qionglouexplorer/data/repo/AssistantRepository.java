package com.bin.qionglouexplorer.data.repo;

/**
 * 文化问答能力抽象。
 * V0.1 由 LocalRuleAssistant 实现（只从本地知识库组装答案，不生成库外内容）；
 * V0.2 换为 RemoteAiAssistant（RAG + 大模型）时，UI 与调用方无需改动。
 */
public interface AssistantRepository {

    interface AnswerCallback {
        void onAnswer(String answer);
    }

    /** 提问并异步返回答案，实现方负责切回主线程。 */
    void ask(String question, AnswerCallback callback);

    /** 供单元测试与调试的同步版本。 */
    String askSync(String question);
}
