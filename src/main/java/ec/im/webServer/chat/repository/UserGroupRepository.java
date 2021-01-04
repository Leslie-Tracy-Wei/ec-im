package ec.im.webServer.chat.repository;


import java.util.List;


public interface UserGroupRepository {
    List<Integer> findGroupIdByUserId(Long userId);
}
