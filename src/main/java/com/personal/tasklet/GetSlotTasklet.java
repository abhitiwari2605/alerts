package com.personal.tasklet;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.Model.Subscription;
import com.personal.cowin.EmailServiceImplementation;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.personal.Model.Center;
import com.personal.Model.Session;
import com.personal.Model.SlotResponse;
import com.personal.service.GetSlotServiceImpl;
import org.springframework.web.client.RestTemplate;

@Component
public class GetSlotTasklet implements Tasklet, StepExecutionListener {

	private static  final ObjectMapper MAPPER = new ObjectMapper();

	private static String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode={0}&date={1}";

	private static String dateFormat = "dd-MM-yyyy";


 
	@Autowired
	GetSlotServiceImpl getSlotServiceImpl;

	@Value("${subscriptions}")
	private String subscriptionData;
	
    @Override
    public void beforeStep(StepExecution stepExecution) {
    	//TODO: logging
    }
 
    @Override
    public RepeatStatus execute(StepContribution stepContribution, 
      ChunkContext chunkContext) throws Exception {
    	
    	Map<String,SlotResponse> slotRes = getSlotResponse(getSubscriptions());
    	getSlotServiceImpl.processSlotResponseRecords(slotRes);
    	return RepeatStatus.FINISHED;
    }
 
    private SlotResponse getSlotsData(String url) {


			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
			HttpEntity<String> entity = new HttpEntity<>(headers);
			String res = restTemplate.exchange(
					"https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=143001&date=09-05-2021",
					HttpMethod.GET, entity, String.class).getBody();
			System.out.println(res);
			SlotResponse response =null;
			try {
				response = MAPPER.readValue(res,SlotResponse.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		return response;
	}

	private Map<String,List<Subscription>> getSubscriptions() throws JsonProcessingException {
		List<Subscription> subscriptions = MAPPER.readValue(subscriptionData, new TypeReference<List<Subscription>>() {
		});
		Map<String,List<Subscription>> subscriptionMap = new HashMap<>();
		subscriptions.forEach(subscription -> {
			if(!subscriptionMap.containsKey(subscription.getPincode())){
				subscriptionMap.put(subscription.getPincode(),new ArrayList<>());
			}
			subscriptionMap.get(subscription.pincode).add(subscription);
		});
		return subscriptionMap;
	}

	private Map<String,SlotResponse> getSlotResponse(Map<String,List<Subscription>> subscriptionMap){
    	String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat));
    	Map<String, SlotResponse> results = new HashMap<>();
    	subscriptionMap.entrySet().forEach(entry-> {
    		String finalUrl = MessageFormat.format(url,entry.getKey(),date);
    		SlotResponse response = getSlotsData(finalUrl);
    		results.put(entry.getKey(),response);
			});
			return results;
	}



	@Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}