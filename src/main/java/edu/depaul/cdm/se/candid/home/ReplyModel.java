package edu.depaul.cdm.se.candid.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyModel {
    private String senderId;
    private String name;
    private String date;
    private String text;
}
