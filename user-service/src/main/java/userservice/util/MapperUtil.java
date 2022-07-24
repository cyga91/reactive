package userservice.util;

import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;
import userservice.dto.UserDto;
import userservice.model.User;

@UtilityClass
public class MapperUtil {

    public static User mapToUser(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }
}
