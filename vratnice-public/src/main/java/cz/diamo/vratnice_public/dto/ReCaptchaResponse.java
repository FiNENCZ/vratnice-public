package cz.diamo.vratnice_public.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReCaptchaResponse {

    private boolean success;

    private float score;

    private String action;

    @JsonProperty("challenge_ts")
    private String challengeTs;

    private String hostname;

    @JsonProperty("error-codes")
    List<String> errorCodes;

    private String errorMessage;


}
