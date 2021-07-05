package com.darksj1998.samplestreamingdataapi.controller;

import com.darksj1998.samplestreamingdataapi.model.User;
import com.darksj1998.samplestreamingdataapi.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/users")
public class UsersController {

    private final Logger logger = LoggerFactory.getLogger(UsersController.class);
    private final String logHead = "UsersController :: ";

    @Autowired
    private UsersService usersService;

    @GetMapping
    public List<User> getAllUsers() {
        logger.info(logHead + "getAllUsers() starting");

        // Populating the users list for dev purposes
        usersService.populateUsersList();

        logger.info(logHead + "getAllUsers() exiting");

        return usersService.getUsersList();
    }

    /**
     * Here, we've outsourced the business logic to the Service
     *
     * @return ResponseEntity<StreamingResponseBody>
     */
    @GetMapping("/async/method1")
    public ResponseEntity<StreamingResponseBody> getAllUsersAsyncMethod1() {
        logger.info(logHead + "getAllUsersAsyncMethod1() starting");

        // Populating the users list for dev purposes
        usersService.populateUsersList();

        // We need an Output Stream to write to
        OutputStream outputStream;
        // StreamingResponseBody needs the "writeTo" function to be overridden
        StreamingResponseBody streamingResponseBody = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                usersService.getUsersAsync(outputStream);
            }
        };

        logger.info(logHead + "getAllUsersAsyncMethod1() exiting");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(streamingResponseBody);
    }

    /**
     * Here, we're picking data from Service, and slowly writing it back
     *
     * @return ResponseEntity<StreamingResponseBody>
     */
    @GetMapping("/async/method2")
    public ResponseEntity<StreamingResponseBody> getAllUsersAsyncMethod2() {
        logger.info(logHead + "getAllUsersAsyncMethod2() starting");

        // Populating the users list for dev purposes
        usersService.populateUsersList();
        List<User> usersListSuperset = usersService.getUsersList();

        StreamingResponseBody streamingResponseBody = response -> {
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

                    response.write(jsonString.getBytes());
                    response.flush();

                    logger.info(logHead + "getAllUsersAsyncMethod2() : Added User " + user + " to the response list");
                    logger.info(logHead + "getAllUsersAsyncMethod2() : Waiting for 2s now");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        logger.info(logHead + "getAllUsersAsyncMethod2() exiting");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(streamingResponseBody);
    }
}
