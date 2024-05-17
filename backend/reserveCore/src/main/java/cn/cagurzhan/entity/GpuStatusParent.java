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
public class GpuStatusParent {
    private List<GpuStatus> gpus;
    private Integer id;
    private String host;
}
