package com.phosservice.Phosservice.Monitoring;

import com.phosservice.Phosservice.Exceptions.CustomException;
import com.phosservice.Phosservice.QueryComponent.CustomQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CleanUpPolicy {

    Logger LOGGER = LoggerFactory.getLogger(CleanUpPolicy.class);

    @Autowired
    private HealthStatus healthStatus;
    @Autowired
    private CustomQueries customQueries;

    // find out the users who are not active for 30 days
    public void fetchUserFromDbAndRegisterForCleanUp() {
        List<String> getInActiveUser = healthStatus.getInActiveUserListFromUserDB();
        if (getInActiveUser == null
                || getInActiveUser.isEmpty()) {
            LOGGER.info("No Inactive user found");
        } else {
            // register for delete in write db for that user
            for (String username : getInActiveUser) {
                try {
                    customQueries.deleteInActiveUser(username);
                } catch (CustomException ex) {
                    throw new CustomException("Error while deleting in active user ", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            LOGGER.info("Successfully deleted the inactive user details from Gallery {}", getInActiveUser.toString());
        }
    }

}
