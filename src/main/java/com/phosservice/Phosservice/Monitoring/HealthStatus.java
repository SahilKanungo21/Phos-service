package com.phosservice.Phosservice.Monitoring;

import com.phosservice.Phosservice.Exceptions.CustomException;
import com.phosservice.Phosservice.Monitoring.HealthCollections.TableHealth;
import com.phosservice.Phosservice.QueryComponent.CustomQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HealthStatus {

    @Autowired
    private CustomQueries customQueries;

    /**
     *  TODO: SCALE DB BASED ON MEMORY
     *  TODO : get the statistics for each user
     *  TODO : get the heartbeat for each user
     */
    //get the table health every day at 10 pm
    public Map<String, Long> getTableHealthReport() {
        HashMap<String, Long> tableHealthData = new HashMap<>();
        try {
            List<TableHealth> tableHealthList = customQueries.getTableHealthCheck();

            for (TableHealth tableHealth : tableHealthList) {
                tableHealthData.put(tableHealth.getTableName(), tableHealth.getMemoryConsumption());
            }
        } catch (CustomException ex) {
            throw new CustomException("Error while fetching the table health report ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return tableHealthData;
    }

    // find out the users who are not active for 30 days
    public List<String> getInActiveUserListFromGalleryDb() {
        return null;
    }
}
