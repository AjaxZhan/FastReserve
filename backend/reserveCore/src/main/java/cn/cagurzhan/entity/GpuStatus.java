package cn.cagurzhan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Cagur
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GpuStatus {
    private Integer index;
    private String name;
    private Integer temperature;
    private Integer used;
    private Integer total;
    private List<Process> processes;
}
