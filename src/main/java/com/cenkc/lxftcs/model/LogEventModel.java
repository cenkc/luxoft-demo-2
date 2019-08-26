package com.cenkc.lxftcs.model;

import lombok.*;

/**
 * created by cenkc on 8/26/2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LogEventModel {

    private String id;
    private String state;
    private String type;
    private String host;
    private Long timestamp;
}
