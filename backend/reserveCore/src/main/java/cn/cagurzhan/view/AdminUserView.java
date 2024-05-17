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
public class AdminUserView {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String status;
    private String sex;
    private String type;
}
