package com.phosservice.Phosservice.Monitoring.HealthCollections;

import lombok.*;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TableHealth {
    String tableName;
    Long memoryConsumption;
}
