package com.bin.qionglouexplorer.ui.assistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.adapter.ChatAdapter;
import com.bin.qionglouexplorer.data.repo.AssistantRepository;
import com.bin.qionglouexplorer.data.repo.LocalRuleAssistant;
import com.bin.qionglouexplorer.util.UiUtils;

/** AI 导览：本地知识库问答 + 快捷问题芯片 + 语音入口占位。 */
public class AssistantFragment extends Fragment {

    private AssistantRepository assistant;
    private ChatAdapter adapter;
    private RecyclerView listChat;
    private EditText inputQuestion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assistant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 依赖的是接口而非实现：V0.2 换成 RemoteAiAssistant 只需改这一行
        assistant = new LocalRuleAssistant(requireContext());

        listChat = view.findViewById(R.id.list_chat);
        inputQuestion = view.findViewById(R.id.input_question);
        adapter = new ChatAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(requireContext());
        lm.setStackFromEnd(true);
        listChat.setLayoutManager(lm);
        listChat.setAdapter(adapter);

        adapter.add(new ChatAdapter.ChatMessage(getString(R.string.assistant_welcome), false));

        view.findViewById(R.id.btn_send).setOnClickListener(v -> sendCurrentQuestion());
        view.findViewById(R.id.btn_voice).setOnClickListener(v ->
                UiUtils.toast(requireView(), getString(R.string.assistant_voice)));

        inputQuestion.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendCurrentQuestion();
                return true;
            }
            return false;
        });

        buildQuickChips(view);
    }

    private void buildQuickChips(View root) {
        LinearLayout group = root.findViewById(R.id.group_quick);
        String[] questions = getResources().getStringArray(R.array.assistant_quick_questions);
        for (String q : questions) {
            TextView chip = (TextView) LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_filter_chip, group, false);
            chip.setText(q);
            chip.setOnClickListener(v -> ask(q));
            group.addView(chip);
        }
    }

    private void sendCurrentQuestion() {
        String question = inputQuestion.getText().toString().trim();
        if (question.isEmpty()) {
            return;
        }
        inputQuestion.setText("");
        ask(question);
    }

    private void ask(String question) {
        adapter.add(new ChatAdapter.ChatMessage(question, true));
        scrollToBottom();
        assistant.ask(question, answer -> {
            if (!isAdded()) {
                return;
            }
            adapter.add(new ChatAdapter.ChatMessage(answer, false));
            scrollToBottom();
        });
    }

    private void scrollToBottom() {
        if (adapter.getItemCount() > 0) {
            listChat.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }
}
