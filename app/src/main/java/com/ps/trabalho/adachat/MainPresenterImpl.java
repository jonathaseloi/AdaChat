package com.ps.trabalho.adachat;

import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ps.trabalho.adachat.model.BotMessage;
import com.ps.trabalho.adachat.model.UsuarioMessage;

/**
 * Created by johnn on 12/12/2017.
 */

public class MainPresenterImpl implements MainPresenter{

    private MainView mainView;
    private ConversationService myConversationService;
    private MessageRequest request;
    private String workspace;
    private ArrayAdapter<String> adapter;

    public MainPresenterImpl(MainView mainView, String user, String password, String workspace, ArrayAdapter<String> adapter) {
        this.mainView = mainView;
        this.myConversationService = new ConversationService(
                "2017-12-07",
                user,
                password
        );
        this.workspace = workspace;
        this.adapter = adapter;
    }

    public void enviarMessage(UsuarioMessage usuarioMessage) {

        mainView.setUserMessage(usuarioMessage);

        request = new MessageRequest.Builder()
                .inputText(usuarioMessage.getTexto())
                .build();

        conversationService(request);
    }

    public void conversationService(MessageRequest request) {
        myConversationService
                .message(workspace, request)
                .enqueue(new ServiceCallback<MessageResponse>() {
                    @Override
                    public void onResponse(MessageResponse response) {
                        BotMessage botMessage = new BotMessage();
                        botMessage.setMessage(response.getText().get(0));
                        mainView.setAdaBotMessage(botMessage);
                    }

                    @Override
                    public void onFailure(Exception e) {}
                });
    }
}
