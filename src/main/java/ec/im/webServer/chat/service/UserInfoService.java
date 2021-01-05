package ec.im.webServer.chat.service;

import ec.im.webServer.base.service.GenericService;
import ec.im.webServer.chat.mapper.UserInfoMapper;
import ec.im.webServer.chat.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService extends GenericService<UserInfo,Long> {
    protected static final Logger logger = LoggerFactory.getLogger(UserInfoService.class);

    public UserInfoService(@Autowired UserInfoMapper userInfoMapper) {
        super(userInfoMapper);
    }

    public UserInfoMapper getMapper() {
        return (UserInfoMapper) super.genericMapper;
    }

}
