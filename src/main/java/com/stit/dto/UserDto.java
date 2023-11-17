package com.stit.dto;

import java.util.List;
import lombok.Data;

@Data
public class UserDto {
    private String userName;
    private List<String> authotity;
}
