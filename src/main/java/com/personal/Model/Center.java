package com.personal.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Center{
    public int center_id;
    public String name;
    public String address;
    public String state_name;
    public String district_name;
    public String block_name;
    public int pincode;
    public int latPos;
    @JsonProperty("long")
    public int longPos;
    public String from;
    public String to;
    public String fee_type;
    public List<Session> sessions;
}