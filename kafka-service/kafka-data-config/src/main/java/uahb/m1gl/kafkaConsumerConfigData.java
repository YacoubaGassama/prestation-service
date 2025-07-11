package uahb.m1gl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-consumer-config")
public class kafkaConsumerConfigData {
    private String keyDeserializer;
    private String valueDeserializer;
    private String customerGroupId;
    private String paiementGroupId;
    private String autoOffsetReset;
    private String specificAvroReaderKey;
    private Boolean specificAvroReader;
    private Boolean batchListener;
    private Boolean autoStartup;
    private Integer concurrencyLevel;
    private Integer sessionTimeoutMs;
    private Integer heartbeatIntervalMs;
    private Integer maxPollIntervalMs;
    private Integer maxPollRecords;
    private Integer maxPartitionFetchBytesDefault;
    private Integer maxPartitionFetchBytesBoostFactor;
    private Integer pollTimeoutMs;
}
