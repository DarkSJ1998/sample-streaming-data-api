package com.darksj1998.samplestreamingdataapi.service;

import com.darksj1998.samplestreamingdataapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsersService {

    private final Logger logger = LoggerFactory.getLogger(UsersService.class);
    private final String logHead = "UsersService :: ";

    private final List<User> usersList;

    public UsersService() {
        usersList = new ArrayList<User>();
    }

    public void populateUsersList() {
        logger.info(logHead + "populateUsersList() starting");

        String[] nameArray = {"ABC", "DEF", "GHI", "JKL", "MNO"};
        String[] cityArray = {"ABC_city", "DEF_city", "GHI_city", "JKL_city", "MNO_city"};
        usersList.clear();
        for (int i = 0; i < 5; i++) {
            User newUser = new User(nameArray[i], cityArray[i]);
            usersList.add(newUser);
        }

        logger.info(logHead + "populateUsersList() exiting");
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void getUsersAsync(OutputStream outputStream) {
        logger.info(logHead + "getUsersAsync() starting");

        List<User> usersListSuperset = getUsersList();
        for (int i = 0; i < usersListSuperset.size(); i++) {
            User user = usersListSuperset.get(i);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jsonString = objectMapper.writeValueAsString(user);

                if (i == 0)
                    jsonString = "[ " + jsonString + ", ";
                else if (i < usersListSuperset.size() - 1)
                    jsonString += ", ";
                else if (i == usersListSuperset.size() - 1)
                    jsonString += " ]";

                outputStream.write(jsonString.getBytes());
                outputStream.flush();

                logger.info(logHead + "getUsersAsync() : Added User " + user + " to the response list");
                logger.info(logHead + "getUsersAsync() : Waiting for 2s now");
                Thread.sleep(2000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        logger.info(logHead + "getUsersAsync() exiting");
    }
}
