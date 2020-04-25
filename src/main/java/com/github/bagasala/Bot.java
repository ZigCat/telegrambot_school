package com.github.bagasala;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private static Logger l = LoggerFactory.getLogger(Bot.class);
    public boolean send = false;

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        Message message = new Message();

        try {
            telegramBotsApi.registerBot(new Bot());

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }


    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        sendMessage.setChatId(message.getChatId().toString());

        //sendMessage.setReplyToMessageId(message.getMessageId());

        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            Message message = update.getMessage();
            if(message.hasText()){
                if(message.getText().substring(0,4).contains("send")){
                    l.info("sending file to client");
                    String file = message.getText().substring(5);
                    download(message, file);
                }
            } else if(message.hasDocument()){
                l.info("receiving file from client");
                upload(message);
            }
        }
    }


    public void download(Message message, String file_name){
        SendDocument sendDocument = new SendDocument();
        sendDocument.setNewDocument(new File("C:\\Users\\MI\\Desktop\\"+file_name));
        sendDocument.setChatId(message.getChatId());
        sendDocument.setCaption(file_name);
        try {
            sendDocument(sendDocument);
            sendMsg(message, "success output");
            l.info("@@@\tsent file "+file_name);
        } catch (TelegramApiException e) {
            sendMsg(message, "fail output");
        }
    }

    public void upload(Message message){
        String file_id = message.getDocument().getFileId();
        try {
            uploadFile(message.getDocument().getFileName(), file_id);
            sendMsg(message, "success");
            l.info("File successfully downloaded");
        } catch (IOException e) {
            sendMsg(message,"fail");
            l.warn("Something goes wrong, check your code or connection");
        }
    }


    public void uploadFile(String file_name, String file_id) throws IOException{
        l.info("@@@\tdownloading file " + file_name + "to server");
        URL url = new URL("https://api.telegram.org/bot"+getBotToken()+"/getFile?file_id="+file_id);
        BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
        String res = in.readLine();
        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        URL downoload = new URL("https://api.telegram.org/file/bot" + getBotToken() + "/" + file_path);
        String upPath = "C:\\Users\\MI\\Desktop\\";
        FileOutputStream fos = new FileOutputStream(upPath + file_name);
        l.info("@@@\tstart downloading...");
        ReadableByteChannel rbc = Channels.newChannel(downoload.openStream());
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
        l.info("@@@\tfile downloaded on server(success)");
    }


    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public String getBotUsername() {
        return "@StudyControlBot";
    }

    public String getBotToken() {
        return "1213409409:AAFkhvvWj5TG20gmE08WfAj-w4NCstIJTjQ";
    }
}
