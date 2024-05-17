package cn.cagurzhan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Cagur
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Process {
    private String username;
    private String command;
    private Integer usage;
}
