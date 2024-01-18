package com.yjq.programmer.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.*;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IArticleService;
import com.yjq.programmer.service.ICommentService;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private IArticleService articleService;

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private AttentionMapper attentionMapper;

    @Resource
    private CollectMapper collectMapper;

    @Resource
    private ICommentService commentService;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> registerUser(UserDTO userDTO) {

        CodeMsg validate = ValidateEntityUtil.validate(userDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        User user = CopyUtil.copy(userDTO, User.class);

        if(isNameExist(user, "")){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EXIST);
        }
        user.setId(UuidUtil.getShortUuid());
        user.setRoleId(RoleEnum.USER.getCode());
        user.setRegisterTime(new Date());
        if(userMapper.insertSelective(user) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_REGISTER_ERROR);
        }
        return ResponseDTO.successByMsg(true, "Successfully signed up.");
    }

    /**
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> webLogin(UserDTO userDTO) {

        if(CommonUtil.isEmpty(userDTO.getUsername())){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EMPTY);
        }
        if(CommonUtil.isEmpty(userDTO.getPassword())){
            return ResponseDTO.errorByMsg(CodeMsg.PASSWORD_EMPTY);
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(userDTO.getUsername()).andPasswordEqualTo(userDTO.getPassword());
        List<User> userList = userMapper.selectByExample(userExample);
        if(userList == null || userList.size() != 1){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_PASSWORD_ERROR);
        }

        UserDTO selectedUserDto = CopyUtil.copy(userList.get(0), UserDTO.class);
        String token = UuidUtil.getShortUuid();
        selectedUserDto.setToken(token);

        stringRedisTemplate.opsForValue().set("USER_" + token, JSON.toJSONString(selectedUserDto), 3600, TimeUnit.SECONDS);
        return ResponseDTO.successByMsg(selectedUserDto, "Successfully logged in!");
    }

    /**
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> adminLogin(UserDTO userDTO) {

        if(CommonUtil.isEmpty(userDTO.getUsername())){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EMPTY);
        }
        if(CommonUtil.isEmpty(userDTO.getPassword())){
            return ResponseDTO.errorByMsg(CodeMsg.PASSWORD_EMPTY);
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(userDTO.getUsername()).andPasswordEqualTo(userDTO.getPassword()).andRoleIdEqualTo(RoleEnum.ADMIN.getCode());
        List<User> userList = userMapper.selectByExample(userExample);
        if(userList == null || userList.size() != 1){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_PASSWORD_ERROR);
        }

        UserDTO selectedUserDto = CopyUtil.copy(userList.get(0), UserDTO.class);
        String token = UuidUtil.getShortUuid();
        selectedUserDto.setToken(token);

        stringRedisTemplate.opsForValue().set("USER_" + token, JSON.toJSONString(selectedUserDto), 3600, TimeUnit.SECONDS);
        return ResponseDTO.successByMsg(selectedUserDto, "Successfully logged in!");
    }

    /**
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> checkLogin(UserDTO userDTO) {
        if(userDTO == null || CommonUtil.isEmpty(userDTO.getToken())){
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        ResponseDTO<UserDTO> responseDTO = getLoginUser(userDTO.getToken());
        if(responseDTO.getCode() != 0 ) {
            return responseDTO;
        }
        User user = userMapper.selectByPrimaryKey(responseDTO.getData().getId());
        if(user == null) {
            logout(userDTO);
            return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        if(responseDTO.getCode() != 0){
            return responseDTO;
        }
        logger.info("Received response={}", responseDTO.getData());
        return ResponseDTO.success(responseDTO.getData());
    }

    /**
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> logout(UserDTO userDTO) {
        if(!CommonUtil.isEmpty(userDTO.getToken())){

            stringRedisTemplate.delete("USER_" + userDTO.getToken());
        }
        return ResponseDTO.successByMsg(true, "退出Successfully logged in!");
    }

    /**
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<UserDTO>> getUserList(PageDTO<UserDTO> pageDTO) {
        UserExample userExample = new UserExample();

        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }

        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        UserExample.Criteria c1 = userExample.createCriteria();
        if(pageDTO.getParam() != null) {
            UserDTO userDTO = pageDTO.getParam();
            if(userDTO.getRoleId() != null && userDTO.getRoleId() != 0) {
                c1.andUsernameLike("%" + userDTO.getUsername() + "%").andRoleIdEqualTo(userDTO.getRoleId());
            } else {
                c1.andUsernameLike("%" + userDTO.getUsername() + "%");
            }
        }
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());

        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);

        pageDTO.setTotal(pageInfo.getTotal());

        List<UserDTO> userDTOList = CopyUtil.copyList(userList, UserDTO.class);
        pageDTO.setList(userDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveUser(UserDTO userDTO) {

        CodeMsg validate = ValidateEntityUtil.validate(userDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        User user = CopyUtil.copy(userDTO, User.class);
        if(CommonUtil.isEmpty(user.getId())) {

            if(isNameExist(user, "")){
                return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EXIST);
            }
            user.setId(UuidUtil.getShortUuid());
            user.setRegisterTime(new Date());
            if(userMapper.insertSelective(user) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.USER_ADD_ERROR);
            }
        } else {

            if(isNameExist(user, user.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EXIST);
            }
            ResponseDTO<UserDTO> loginUser = getLoginUser(userDTO.getToken());
            if(loginUser.getCode() != 0) {
                return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
            }
            if(userMapper.updateByPrimaryKeySelective(user) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.USER_EDIT_ERROR);
            }
            UserDTO loginUserDTO = loginUser.getData();
            if(user.getId().equals(loginUserDTO.getId())) {

                loginUserDTO = CopyUtil.copy(userMapper.selectByPrimaryKey(user.getId()), UserDTO.class);
                loginUserDTO.setToken(userDTO.getToken());
                stringRedisTemplate.opsForValue().set("USER_" + userDTO.getToken(), JSON.toJSONString(loginUserDTO), 3600, TimeUnit.SECONDS);
            }
        }
        return ResponseDTO.successByMsg(true, "Successfully saved user info.");
    }

    /**
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteUser(UserDTO userDTO) {
        if(CommonUtil.isEmpty(userDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        String[] ids = userDTO.getId().split(",");
        for(String id : ids) {

            ArticleExample articleExample = new ArticleExample();
            articleExample.createCriteria().andUserIdEqualTo(id);
            List<Article> articleList = articleMapper.selectByExample(articleExample);
            for(Article article : articleList) {
                articleService.deleteArticle(CopyUtil.copy(article, ArticleDTO.class));
            }

            LikeExample likeExample = new LikeExample();
            likeExample.createCriteria().andUserIdEqualTo(id);
            likeMapper.deleteByExample(likeExample);

            CollectExample collectExample = new CollectExample();
            collectExample.createCriteria().andUserIdEqualTo(id);
            collectMapper.deleteByExample(collectExample);

            CommentExample commentExample = new CommentExample();
            CommentExample.Criteria c1 = commentExample.createCriteria();
            CommentExample.Criteria c2 = commentExample.createCriteria();
            c1.andFromIdEqualTo(id);
            c2.andToIdEqualTo(id);
            commentExample.or(c2);
            List<Comment> commentList = commentMapper.selectByExample(commentExample);
            for(Comment comment : commentList) {
                commentService.deleteComment(CopyUtil.copy(comment, CommentDTO.class));
            }

            AttentionExample attentionExample = new AttentionExample();
            AttentionExample.Criteria c3 = attentionExample.createCriteria();
            AttentionExample.Criteria c4 = attentionExample.createCriteria();
            c3.andFromIdEqualTo(id);
            c4.andToIdEqualTo(id);
            attentionExample.or(c4);
            attentionMapper.deleteByExample(attentionExample);

            if(userMapper.deleteByPrimaryKey(id) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.USER_DELETE_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "Successfully deleted user info.");
    }

    /**
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> getUserById(UserDTO userDTO) {
        if(CommonUtil.isEmpty(userDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        User user = userMapper.selectByPrimaryKey(userDTO.getId());
        if(user == null) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        return ResponseDTO.success(CopyUtil.copy(user, UserDTO.class));
    }

    /**
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> updateUserInfo(UserDTO userDTO) {
        if(CommonUtil.isEmpty(userDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }

        CodeMsg validate = ValidateEntityUtil.validate(userDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        User user = CopyUtil.copy(userDTO, User.class);

        if(isNameExist(user, userDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_EXIST);
        }
        if(userMapper.updateByPrimaryKeySelective(user) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_EDIT_ERROR);
        }
        User userDB = userMapper.selectByPrimaryKey(userDTO.getId());
        UserDTO userDBDTO = CopyUtil.copy(userDB, UserDTO.class);
        userDBDTO.setToken(userDTO.getToken());
        stringRedisTemplate.opsForValue().set("USER_" + userDBDTO.getToken(), JSON.toJSONString(userDBDTO), 3600, TimeUnit.SECONDS);
        return ResponseDTO.successByMsg(CopyUtil.copy(userDB, UserDTO.class), "Successfully modified user info.");
    }

    /**
     * @return
     */
    @Override
    public ResponseDTO<Integer> getUserTotal() {
        return ResponseDTO.success(userMapper.countByExample(new UserExample()));
    }

    /**
     * @return
     */
    public ResponseDTO<UserDTO> getLoginUser(String token){
        String value = stringRedisTemplate.opsForValue().get("USER_" + token);
        if(CommonUtil.isEmpty(value)){
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        UserDTO selectedUserDTO = JSON.parseObject(value, UserDTO.class);
        return ResponseDTO.success(selectedUserDTO);
    }

    /**
     * @param user
     * @param id
     * @return
     */
    public Boolean isNameExist(User user, String id) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(user.getUsername());
        List<User> selectedUserList = userMapper.selectByExample(userExample);
        if(selectedUserList != null && selectedUserList.size() > 0) {
            if(selectedUserList.size() > 1){
                return true;
            }
            if(!selectedUserList.get(0).getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
