package ec.im.webServer.chat.mapper;

import ec.im.webServer.base.mapper.GenericMapper;
import ec.im.webServer.chat.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserInfoMapper extends GenericMapper<UserInfo,Long> {

}
