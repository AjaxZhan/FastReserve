package cn.cagurzhan.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Cagur
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveViewMy {
    private Long id;
    private String title;
    private Date start;
    private Date end;
    private String comment;
    private String who;
    private Long createBy;
    private Long gpuId;
    private GpuView gpu;
}
