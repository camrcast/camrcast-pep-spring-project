package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import java.util.*;
import org.springframework.web.bind.annotation.*;

import com.example.entity.*;
import com.example.service.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;
    ObjectMapper o;

    @Autowired
    public SocialMediaController(AccountService acc, MessageService mess){
        accountService = acc;
        messageService = mess;
        o = new ObjectMapper();
    }

    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity<String> registerAccount(@RequestBody String body){
        try{
            Account a = o.readValue(body, Account.class);
            int v = accountService.validateAccount(a);
            if (v == 0){
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
            else if (v == -1){
                return new ResponseEntity<String>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<String>(HttpStatus.OK);
        }
        catch(JsonProcessingException e){
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }
 
    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<Account> loginAccount(@RequestBody String body){
        try{
            Account a = o.readValue(body, Account.class);
            Account ac = accountService.accountExists(a);
            if (ac != null){
                return new ResponseEntity<Account>(ac, HttpStatus.OK);
            }
            return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
        }
        catch(JsonProcessingException e){
            return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/messages")
    @ResponseBody
    public ResponseEntity<Message> postMessage(@RequestBody String body){
        try{
            Message m = o.readValue(body, Message.class);
            if (accountService.accountExistsById(m.getPosted_by()) == null){
                return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST); 
            }
            else if (messageService.createMessage(m)){
                return new ResponseEntity<Message>(m, HttpStatus.OK);
            }
            return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
        }
        catch(JsonProcessingException e){
            return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/messages")
    @ResponseBody
    public ResponseEntity<List<Message>> getAllMessages(){
        return new ResponseEntity<List<Message>>(messageService.getAllMessages(), HttpStatus.OK);
    }

    @GetMapping(value = "/messages/{message_id}")
    @ResponseBody
    public ResponseEntity<Message> getMessageById(@PathVariable("message_id") Integer id){
        Message m = messageService.getMessageById(id);
        if (m != null){
            return new ResponseEntity<Message>(m, HttpStatus.OK);
        }
        return new ResponseEntity<Message>(HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/messages/{message_id}")
    @ResponseBody
    public ResponseEntity<Integer> deleteMessage(@PathVariable("message_id") Integer id){
        if (messageService.deleteMessage(id)){
            return new ResponseEntity<Integer>(1, HttpStatus.OK);
        }
        return new ResponseEntity<Integer>(HttpStatus.OK);
    }

    @PatchMapping(value = "/messages/{message_id}")
    @ResponseBody
    public ResponseEntity<Integer> updateMessage(@PathVariable("message_id") Integer id, @RequestBody String body){
        String s = body.substring(18, body.length()-2);
        if (messageService.updateMessage(id, s)){
            return new ResponseEntity<Integer>(1, HttpStatus.OK);
        }
        return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/accounts/{account_id}/messages")
    @ResponseBody
    public ResponseEntity<List<Message>> getAllMessagesByUser(@PathVariable("account_id") Integer id){
        List<Message> messL = messageService.getAllMessagesByUser(id);
        return new ResponseEntity<List<Message>>(messL, HttpStatus.OK);
    }
}
