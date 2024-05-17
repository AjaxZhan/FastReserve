package cn.cagurzhan.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Cagur
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GpuView {
    private Long id;
    private Long serverId; // 冗余字段，没用
    private String serverName;
    private Integer serverVolume;
    private Integer number;
    private String model;
    private String status;
}
