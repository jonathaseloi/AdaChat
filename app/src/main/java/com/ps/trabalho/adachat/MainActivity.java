package com.ps.trabalho.adachat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.icu.lang.UProperty.INT_START;

public class MainActivity extends AppCompatActivity implements MainView {

    private UsuarioMessage usuarioMessage = new UsuarioMessage();

    @BindView(R.id.lvConversation)
    protected ListView conversation;

    @BindView(R.id.userInput)
    protected EditText userInput;

    private MainPresenter presenter;
    private List<String> conversa = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Listview
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, conversa);
        conversation.setAdapter(adapter);

        //Param.: MainView, Login, Senha, Código do WorkSpace
        presenter = new MainPresenterImpl(this, getString(R.string.username), getString(R.string.password), getString(R.string.workspace), adapter);

        //Colocando funcao enviar no icone done do teclado
        userInput.setOnEditorActionListener(new TextView
                .OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tv,
                                          int action, KeyEvent keyEvent) {
                if(action == EditorInfo.IME_ACTION_DONE) {
                    usuarioMessage.setTexto(userInput.getText().toString());
                    userInput.setText("");

                    presenter.enviarMessage(usuarioMessage);
                }
                return false;
            }
        });

    }

    @Override
    public void setUserMessage(UsuarioMessage usuarioMessage) {
        conversa.add("Você \n   " + usuarioMessage.getTexto());
        adapter.notifyDataSetChanged();
        scrollDown();
    }

    @Override
    public void setAdaBotMessage(final BotMessage botMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                conversa.add("ADA Bot\n " + botMessage.getMessage());
                adapter.notifyDataSetChanged();
                scrollDown();
            }
        });

    }

    public void scrollDown() {
        conversation.setSelection(adapter.getCount() - 1);
    }
}
