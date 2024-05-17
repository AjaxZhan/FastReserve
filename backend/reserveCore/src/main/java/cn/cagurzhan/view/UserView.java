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
public class UserView {
    private Long id;
    private String nickname;
    private String email;
    private String status;
    private String sex;
    private String type;
}
