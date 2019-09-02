package com.cenkc.lxftcs.model;

import lombok.*;

/**
 * created by cenkc on 9/2/2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LogEventAlert {
    private String id;
    private Long duration;
    private String type;
    private String host;
    private Boolean alert;
}
