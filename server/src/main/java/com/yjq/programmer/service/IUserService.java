package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;


public interface IUserService {

    ResponseDTO<Boolean> registerUser(UserDTO userDTO);

    ResponseDTO<UserDTO> webLogin(UserDTO userDTO);

    ResponseDTO<UserDTO> adminLogin(UserDTO userDTO);

    ResponseDTO<UserDTO> checkLogin(UserDTO userDTO);

    ResponseDTO<Boolean> logout(UserDTO userDTO);

    ResponseDTO<PageDTO<UserDTO>> getUserList(PageDTO<UserDTO> pageDTO);

    ResponseDTO<Boolean> saveUser(UserDTO userDTO);

    ResponseDTO<Boolean> deleteUser(UserDTO userDTO);

    ResponseDTO<UserDTO> getUserById(UserDTO userDTO);

    ResponseDTO<UserDTO> updateUserInfo(UserDTO userDTO);

    ResponseDTO<Integer> getUserTotal();

}
