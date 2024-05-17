package cn.cagurzhan.view;

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
public class AdminUserInfoView {
    private List<String> permissions;
    private List<String> roles;
    private UserView user;
}
