package com.personal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.Model.Center;
import com.personal.Model.Session;
import com.personal.Model.SlotResponse;
import com.personal.Model.Subscription;
import com.personal.cowin.EmailServiceImplementation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableAsync
@Slf4j
public class GetSlotServiceImpl {
  private static final Logger LOGGER = LoggerFactory.getLogger(GetSlotServiceImpl.class);
  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Autowired
  private EmailServiceImplementation emailServiceImplementation;

  @Value("${filter.age.value}")
  private int filterAge;

  @Value("${subscriptions}")
  private String subscriptionData;


  public void processSlotResponseRecords(Map<String, SlotResponse> slotRes) throws JsonProcessingException {

    getSubscriptions().forEach(subscription -> {
      List<Center> centers = new ArrayList<>();
      SlotResponse res = slotRes.get(subscription.getPincode());
      res.getCenters().forEach(center -> {
        Optional<Session> resultSession =
            center.getSessions().stream().filter(session -> session.getMin_age_limit() <= subscription.getAge())
                .findAny();
        if(resultSession.isPresent()){
          centers.add(center);
        }
      });
      if(centers.size()>0){
        sendMailNotfication(centers, subscription.getEmail());
      }
    });
  }

  private void sendMailNotfication(List<Center> res, String email) {
    try {
      String message = MAPPER.writeValueAsString(res);
      log.info(message);
      emailServiceImplementation
          .sendSimpleMessage(email,
              "Available covid centers", message);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

  }

  private List< Subscription> getSubscriptions() throws JsonProcessingException {
    return MAPPER.readValue(subscriptionData, new TypeReference<List<Subscription>>() {
    });

  }
}




