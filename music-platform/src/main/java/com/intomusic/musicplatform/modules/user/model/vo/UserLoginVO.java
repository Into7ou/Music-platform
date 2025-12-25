// 新建 model/vo/UserLoginVO.java
package com.intomusic.musicplatform.modules.user.model.vo;

import lombok.Data;

@Data
public class UserLoginVO {
    private String token;
    private String username;
    private String nickname;
    private String avatar;
}