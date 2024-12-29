package com.report.ReportApp.service;

import com.report.ReportApp.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.ArrayList;

@Service
public class UserService {
    private List<User> store = new ArrayList<>();

    public UserService() {
    store.add(new User(UUID.randomUUID().toString(),"pranit@gmail.com","Pranit Dhane"));
        store.add(new User(UUID.randomUUID().toString(),"prajakta@gmail.com","Prajakta Dhane"));
        store.add(new User(UUID.randomUUID().toString(),"hema@gmail.com","Hema Dhane"));
        store.add(new User(UUID.randomUUID().toString(),"laxman@gmail.com","Laxman Dhane"));
        for (User user:store
             ) {
            System.out.println(user.getUserId());
        }
    }

    public List<User> getUserStore() {
        return store;
    }
}
