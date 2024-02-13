package com.example.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }
    public boolean createMessage(Message m){
        if (m.getMessage_text().isBlank() || m.getMessage_text().length() > 255){
            return false;
        }
        messageRepository.save(m);
        return true;
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(int id){
        for (Message m : messageRepository.findAll()){
            if (m.getMessage_id().equals(id)){
                return m;
            }
        }
        return null;
    }
    
    public boolean deleteMessage(int id){
        Message m = getMessageById(id);
        if (m == null){
            return false;
        }
        messageRepository.delete(m);
        return true;
    }

    public boolean updateMessage(int id, String body){
        if (body.length() > 255 || body.isBlank()){
            return false;
        }
        for (Message me : messageRepository.findAll()){
            if (me.getMessage_id().equals(id)){
                me.setMessage_text(body);
                messageRepository.save(me);
                return true;
            }
        }
        return false;
    }

    public List<Message> getAllMessagesByUser(int id){
        List<Message> messL = new ArrayList<Message>();
        for (Message m : messageRepository.findAll()){
            if (m.getPosted_by().equals(id)){
                messL.add(m);
            }
        }
        return messL;
    }
}
