package com.edtech.qaservice.config;

import com.amazon.dax.client.dynamodbv2.AmazonDaxClientBuilder;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AWSConfig {
    private String DAX_ENDPOINT = "mydaxcluster.mxyuv9.dax-clusters.ap-southeast-1.amazonaws.com:8111";

    @Value("${amazon.region}")
    private String awsRegion;

    @Value("${amazon.end-point.url}")
    private String awsDynamoDBEndPoint;

    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }

    @Bean
    public DynamoDBMapper daxMapper(AmazonDynamoDB amazonDAX) {
        return new DynamoDBMapper(amazonDAX);
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsDynamoDBEndPoint, awsRegion))
                .withCredentials(amazonAWSCredentialsProvider())
                .build();
    }

    @Bean
    public AmazonDynamoDB amazonDAX() {
        AmazonDaxClientBuilder daxClientBuilder = AmazonDaxClientBuilder.standard();
        daxClientBuilder.withRegion(awsRegion).withEndpointConfiguration(DAX_ENDPOINT);

        return daxClientBuilder.build();
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return  new DefaultAWSCredentialsProviderChain().getCredentials();
    }

}