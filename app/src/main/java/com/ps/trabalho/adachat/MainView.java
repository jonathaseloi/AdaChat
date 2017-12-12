package com.ps.trabalho.adachat;

import android.widget.ScrollView;

import com.ps.trabalho.adachat.model.BotMessage;
import com.ps.trabalho.adachat.model.UsuarioMessage;

/**
 * Created by johnn on 12/12/2017.
 */

public interface MainView {

    void setUserMessage(UsuarioMessage usuarioMessage);
    void setAdaBotMessage(BotMessage botMessage);
}
