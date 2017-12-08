package com.ps.trabalho.adachat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ps.trabalho.adachat.model.BotMessage;
import com.ps.trabalho.adachat.model.UsuarioMessage;

public class MainActivity extends AppCompatActivity {

    UsuarioMessage usuarioMessage = new UsuarioMessage();
    BotMessage botMessage = new BotMessage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConversationService myConversationService =
                new ConversationService(
                        "2017-12-07",
                        getString(R.string.username),
                        getString(R.string.password)
                );

        final TextView conversation = (TextView)findViewById(R.id.conversation);
        final EditText userInput = (EditText)findViewById(R.id.user_input);
        final ScrollView scrollView = (ScrollView)findViewById(R.id.svTexto);

        userInput.setOnEditorActionListener(new TextView
                .OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tv,
                                          int action, KeyEvent keyEvent) {
                if(action == EditorInfo.IME_ACTION_DONE) {

                    usuarioMessage.setTexto(userInput.getText().toString());
                    conversation.append(
                            Html.fromHtml("<p><b>You:</b> " + usuarioMessage.getTexto() + "</p>")
                    );

                    userInput.setText("");

                    MessageRequest request = new MessageRequest.Builder()
                            .inputText(usuarioMessage.getTexto())
                            .build();

                    myConversationService
                            .message(getString(R.string.workspace), request)
                            .enqueue(new ServiceCallback<MessageResponse>() {
                                @Override
                                public void onResponse(MessageResponse response) {
                                    botMessage.setMessage(response.getText().get(0));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            conversation.append(
                                                    Html.fromHtml("<p><b>ADA Bot:</b> " +
                                                            botMessage.getMessage() + "</p>")
                                            );
                                        }
                                    });
                                    scrollView.fullScroll(View.FOCUS_DOWN);
                                    scrollView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            scrollView.fullScroll(View.FOCUS_DOWN);
                                        }
                                    });

                                }

                                @Override
                                public void onFailure(Exception e) {}
                            });

                }
                return false;
            }
        });






    }
}
