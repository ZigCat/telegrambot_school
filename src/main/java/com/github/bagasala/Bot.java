package com.github.bagasala;

import com.github.bagasala.ormlite.models.*;
import com.github.bagasala.ormlite.services.DateService;
import com.github.bagasala.ormlite.services.HometaskService;
import com.github.bagasala.ormlite.services.SubjectService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import org.json.JSONObject;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {
    private static Logger l = LoggerFactory.getLogger(Bot.class);
    public static Dao<Hometask, Integer> hometaskDao;
    public static Dao<Subject,Integer> subjectDao;
    private Map<String, Integer> map = new HashMap<>();
    private Map<String, Hometask> hometasks = new HashMap<>();



    static {
        try {
            subjectDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Subject.class);
            hometaskDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, Hometask.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        Message message = new Message();

        try {
            telegramBotsApi.registerBot(new Bot());

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {

        LocalTime localTime = LocalTime.now();
        SendMessage sendMessage = new SendMessage();

        try {
            //notification();
            addTeacherInterface(update);
            addStudentInterface(update);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }


//        if(update.hasMessage()){
//            if(update.getMessage().getContact()==null){
//                sendMessage
//                        .setChatId(update.getMessage().getChatId())
//                        .setText("Поделитесь своими контактами, чтобы мы проверили, есть ли вы в базе");
//                try {
//                    sendMessage(sendMessage);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//            } else{
//
//                try {
//                    boolean userInBase = false;
//                    for(UserDb user:userDao.queryForAll()){
//                        if(user.getPhone().equals(update.getMessage().getContact().getPhoneNumber())){
//                            userInBase = true;
//                        }
//                    }
//                    if(!userInBase){
//                        sendMessage
//                                .setChatId(update.getMessage().getChatId())
//                                .setText("Вас нет в базе");
//                        sendMessage(sendMessage);
//                    }
//
//                } catch (SQLException | TelegramApiException throwables) {
//                    throwables.printStackTrace();
//                }
//                update.getMessage().getContact().getPhoneNumber();
//            }
//            //checkIfMessageEqualsSchedule(update);
//
//        }
    }

    public static void notification() throws IOException {
        String urlString = "https://api.telegram.org/bot"+"1213409409:AAFkhvvWj5TG20gmE08WfAj-w4NCstIJTjQ"+"/sendMessage?chat_id="+1203673958+"&text=\"dsad\"";

        String apiToken ="1213409409:AAFkhvvWj5TG20gmE08WfAj-w4NCstIJTjQ";
        String chatId = "@StudyControlBot";
        String text = "Hello world!";

        urlString = String.format(urlString, apiToken, chatId, text);

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
        String response = sb.toString();
// Do what you want with response
    }

    public void checkIfMessageEqualsSchedule(Update update){
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            String photo_name = null;
            SendMessage message = new SendMessage();
            if (message_text.equals("/schedule")) {
                setScheduleButtons(message,chat_id);
                try {
                    sendMessage(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("Понедельник")) {
                photo_name = "monday.jpg";
            }else if(message_text.equals("Вторник")){
                photo_name = "tuesday.jpg";
            } else if(message_text.equals("Среда")){
                photo_name = "wednesday.jpg";
            }else if(message_text.equals("Четверг")){
                photo_name = "thursday.jpg";
            }else if(message_text.equals("Пятница")){
                photo_name = "friday.jpg";
            }else if(message_text.equals("Суббота")){
                photo_name = "saturday.jpg";
            }

            if(photo_name!=null){
                SendPhoto msg = new SendPhoto()
                        .setChatId(chat_id)
                        .setNewPhoto(new File(getDirPath()+photo_name))
                        .setCaption("Photo");
                try {
                    sendPhoto(msg); // Call method to send the photo
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message_text.equals("/hide") || message_text.equals("❌ Закрыть ❌") || message_text.equals("Закрыть")) {
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText("Keyboard hidden");
                ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                msg.setReplyMarkup(keyboardMarkup);
                try {
                    sendMessage(msg); // Call method to send the photo
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getDirPath(){
        return "C:\\Users\\MI\\Desktop\\telegram bot\\";
    }

    public void setScheduleButtons(SendMessage message,long chat_id){
        message.setChatId(chat_id)
                .setText("Выберите день недели");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Понедельник");
        keyboard.add(row);

        row = new KeyboardRow();
        row.add("Вторник");
        keyboard.add(row);

        row = new KeyboardRow();
        row.add("Среда");
        keyboard.add(row);

        row = new KeyboardRow();
        row.add("Четверг");
        keyboard.add(row);

        row = new KeyboardRow();
        row.add("Пятница");
        keyboard.add(row);

        row = new KeyboardRow();
        row.add("Суббота");
        keyboard.add(row);

        row = new KeyboardRow();
        row.add("❌ Закрыть ❌");
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        l.info(message.getReplyMarkup().toString());
    }
    public static void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/posthometask"));
        keyboardFirstRow.add(new KeyboardButton("/homework"));
        keyboardFirstRow.add(new KeyboardButton("/schedule"));
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        sendMessage.setChatId(message.getChatId().toString());

        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void download(Message message, String file_name){
        SendDocument sendDocument = new SendDocument();
        sendDocument.setNewDocument(new File(file_name));
        sendDocument.setChatId(message.getChatId());
        try {
            sendDocument(sendDocument);
            l.info("@@@\tsent file "+file_name);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(String file_name, String file_id) throws IOException{
        l.info("@@@\tdownloading file " + file_name + " to server");
        URL url = new URL("https://api.telegram.org/bot"+getBotToken()+"/getFile?file_id="+file_id);
        BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
        String res = in.readLine();
        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        URL downoload = new URL("https://api.telegram.org/file/bot" + getBotToken() + "/" + file_path);
        String upPath = getDirPath();
        FileOutputStream fos = new FileOutputStream(upPath + file_name);
        l.info("@@@\tstart downloading...");
        ReadableByteChannel rbc = Channels.newChannel(downoload.openStream());
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
        l.info("@@@\tfile downloaded on server(success)");
    }

    public void addStudentInterface(Update update) throws SQLException {
        if(update.hasMessage()){
            Message message = update.getMessage();
            Long chId = message.getChatId();
            String chatId = chId.toString()+"stud";
            if(message.hasText()){
                if(message.getText().equals("/homework")){
                    sendMsg(message, "Введите предмет, по которому хотите получить домашнее задание");
                    l.info("@getting hometask initialized, chatId = "+chatId);
                    map.put(chatId, 1);
                    hometasks.put(chatId, new Hometask());
                } else if(map.containsKey(chatId)){
                    if(map.get(chatId) == 1){
                        l.info("@getting subject name from client, chatId = "+chatId);
                        String text = message.getText();
                        if(SubjectService.getSubjectByName(text) == null){
                            l.info("client sent wrong subject");
                            sendMsg(message, "Неправильное название предмета, повторите попытку");
                        } else {
                            l.info("@setting hometask's subject, chatId = "+chatId);
                            hometasks.get(chatId).setSubject(SubjectService.getSubjectByName(text));
                            hometasks.get(chatId).setId(1);
                            map.put(chatId, 2);
                            sendMsg(message, "Установлен предмет: "+text+"\nВведите дату домашнего задания в формате ГГГГ ММ ДД");
                        }
                    } else if(map.get(chatId) == 2){
                        l.info("@getting homework's date, chatId = "+chatId);
                        String date = message.getText();
                        if(DateService.isValidDate(date)){
                            l.info("@setting hometask's date, chatId = "+chatId);
                            hometasks.get(chatId).setDate(date);
                            Hometask hometask = HometaskService.getByParams(hometasks.get(chatId).getDate(), hometasks.get(chatId).getSubject());
                            if(hometask == null){
                                l.info("@hometask isn't exists, try again");
                                sendMsg(message, "Домашки не существует, повторите попытку снова");
                            } else {
                                sendMsg(message, "Отлично, отправляем домашнее задание!");
                                Runnable task = () -> {
                                    download(message, hometask.getFilePath());
                                };
                                Thread thread = new Thread(task);
                                thread.start();
                                sendMsg(message, "комментарии учителя: "+hometask.getDescription());
                                hometasks.remove(chatId);
                                map.remove(chatId);
                                l.info("@hometask sending cycle ended");
                            }
                        }
                    }
                }
            }
        }
    }

    public void addTeacherInterface(Update update) throws SQLException, IOException {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chId = message.getChatId();
            String chatId = chId.toString()+"teach";
            if (message.hasText()) {
                if (message.getText().equals("/posthometask")) {
                    l.info("@posting hometask initialized, chatId = "+chatId);
                    map.put(chatId, 1);
                    hometasks.put(chatId, new Hometask());
                    sendMsg(message, "Введите название предмета");
                } else if (map.containsKey(chatId)) {
                    if (map.get(chatId) == 1) {
                        String text = message.getText();
                        l.info("@getting subject's name from client chatId = "+chatId);
                        if (SubjectService.getSubjectByName(text) != null) {
                            l.info("@setting subject to hometask chatId = "+chatId);
                            hometasks.get(chatId).setId(1);
                            hometasks.get(chatId).setSubject(SubjectService.getSubjectByName(text));
                            map.put(chatId, 2);
                            sendMsg(message, "Установлен предмет: " + text + ".\nВведите комментарий к домашнему заданию");
                        } else {
                            l.info("client sent wrong subject");
                            sendMsg(message, "Неправильное название предмета, повторите попытку");
                        }
                    } else if (map.get(chatId) == 2) {
                        l.info("@get hometask's description from client chatId = "+chatId);
                        String desc = message.getText();
                        hometasks.get(chatId).setDescription(desc);
                        sendMsg(message, "Отлично, теперь отправьте нам файл с домашним заданием");
                        map.put(chatId, 3);
                    }
                }
            } else if (message.hasDocument()) {
                if (map.get(chatId) == 3) {
                    l.info("@get file from client chatId = "+chatId);
                    String fileName = message.getDocument().getFileName();
                    String fileId = message.getDocument().getFileId();
                    Runnable task = () -> {
                        try {
                            uploadFile(hometasks.get(chatId).getSubject().getName() + "\\" + fileName, fileId);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    };
                    Thread thread = new Thread(task);
                    thread.start();
                    l.info("@setting file to hometask");
                    hometasks.get(chatId).setFilePath(getDirPath() + hometasks.get(chatId).getSubject().getName() + "\\" + fileName);
                    hometasks.get(chatId).setDate(LocalDate.now().format(Hometask.dateTimeFormatter));
                    hometaskDao.create(hometasks.get(chatId));
                    l.info("@hometask object was created");
                    map.remove(chatId);
                    hometasks.remove(chatId);
                    sendMsg(message, "Отлично! Домашнее задание занесено сегодняшним числом в базу!");
                    l.info("@hometask creating cycle ended");
                }
            }
        }
    }

    public String getBotUsername() {
        //put here your own bot's username
        return "@StudyControlBot";
    }

    public String getBotToken() {
        //put here your own bot's token
        return "1213409409:AAEpGc8wuxiF-TRbdKFmyZEy7ltsfFnuBfo";
    }
//    private void log(String first_name, String last_name, String user_id, String txt, String bot_answer) {
//        System.out.println("\n ----------------------------");
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Date date = new Date();
//        System.out.println(dateFormat.format(date));
//        System.out.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
//        System.out.println("Bot answer: \n Text - " + bot_answer);
//    }
}
