package com.darksj1998.samplestreamingdataapi.service;

import com.darksj1998.samplestreamingdataapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsersService {
    private final List<User> usersList;

    public UsersService() {
        usersList = new ArrayList<User>();
    }

    public void populateUsersList() {
        String[] nameArray = {"ABC", "DEF", "GHI", "JKL", "MNO"};
        String[] cityArray = {"ABC_city", "DEF_city", "GHI_city", "JKL_city", "MNO_city"};
        usersList.clear();
        for (int i = 0; i < 5; i++) {
            User newUser = new User(nameArray[i], cityArray[i]);
            usersList.add(newUser);
        }
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void getUsersAsync(OutputStream outputStream) {
        List<User> usersListSuperset = getUsersList();
        for (User user : usersListSuperset) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jsonString = objectMapper.writeValueAsString(user) + "\n";
                outputStream.write(jsonString.getBytes());
                outputStream.flush();
                System.out.println("Added User " + user + " to the response list");
                System.out.println("Waiting for 2s now");
                Thread.sleep(2000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
