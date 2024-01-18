package com.yjq.programmer.bean;

/**
 * Error Code Collection
 */
public class CodeMsg {

    private Integer code;

    private String msg;

    /**
     * @param code
     * @param msg
     */
    private CodeMsg(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg() {

    }

    public Integer getCode() {
        return code;
    }



    public void setCode(Integer code) {
        this.code = code;
    }



    public String getMsg() {
        return msg;
    }



    public void setMsg(String msg) {
        this.msg = msg;
    }


    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg DATA_ERROR = new CodeMsg(-1, "Illegal data!");
    public static CodeMsg VALIDATE_ENTITY_ERROR = new CodeMsg(-2, "");
    public static CodeMsg CAPTCHA_EMPTY = new CodeMsg(-3, "Verification code cannot be empty!");
    public static CodeMsg NO_PERMISSION = new CodeMsg(-4, "You do not have this permission.");
    public static CodeMsg CAPTCHA_ERROR = new CodeMsg(-5, "Verification code is wrong.");
    public static CodeMsg USER_SESSION_EXPIRED = new CodeMsg(-6, "Not logged in or session expired. Please log in again.");
    public static CodeMsg UPLOAD_PHOTO_SUFFIX_ERROR = new CodeMsg(-7, "Wrong photo suffix!");
    public static CodeMsg PHOTO_SURPASS_MAX_SIZE = new CodeMsg(-8, "Size cannot be larger than 2MB.");
    public static CodeMsg PHOTO_FORMAT_NOT_CORRECT = new CodeMsg(-9, "Wrong photo format.");
    public static CodeMsg SAVE_FILE_EXCEPTION = new CodeMsg(-10, "Error saving file.");
    public static CodeMsg FILE_EXPORT_ERROR = new CodeMsg(-11, "Fail to export file.");
    public static CodeMsg SYSTEM_ERROR = new CodeMsg(-12, "System error. Please contact admin.");
    public static CodeMsg NO_AUTHORITY = new CodeMsg(-13, "Sorry, you do not have permission.");
    public static CodeMsg CAPTCHA_EXPIRED = new CodeMsg(-14, "Verification code expired.");
    public static CodeMsg COMMON_ERROR = new CodeMsg(-15, "");
    public static CodeMsg PHOTO_EMPTY = new CodeMsg(-16, "Cannot upload empty photo.");



    public static CodeMsg USER_ADD_ERROR = new CodeMsg(-1000, "Fail to add user.");
    public static CodeMsg USER_NOT_EXIST  = new CodeMsg(-1001, "User does not exist.");
    public static CodeMsg USER_EDIT_ERROR = new CodeMsg(-1002, "Fail to modify user info.");
    public static CodeMsg USER_DELETE_ERROR = new CodeMsg(-1003, "Fail to delete user info.");
    public static CodeMsg USERNAME_EXIST = new CodeMsg(-1004, "User name already exists.");
    public static CodeMsg USERNAME_EMPTY = new CodeMsg(-1005, "Username cannot be empty.");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(-1006, "User password cannot be empty.");
    public static CodeMsg USERNAME_PASSWORD_ERROR = new CodeMsg(-1007, "Username or password is wrong,");
    public static CodeMsg REPASSWORD_EMPTY = new CodeMsg(-1008, "Confirm password cannot be empty.");
    public static CodeMsg REPASSWORD_ERROR = new CodeMsg(-1009, "Confirm password is not the same.");
    public static CodeMsg USER_REGISTER_ERROR = new CodeMsg(-1010, "Fail to register.");
    public static CodeMsg USER_NOT_IS_ADMIN = new CodeMsg(-1011, "Only admin can register into backend!");


    public static CodeMsg CATEGORY_NAME_EXIST = new CodeMsg(-2000, "Article category already exists.");
    public static CodeMsg CATEGORY_ADD_ERROR = new CodeMsg(-2001, "Fail to add article category.");
    public static CodeMsg CATEGORY_NOT_EXIST  = new CodeMsg(-2002, "Article category does not exist");
    public static CodeMsg CATEGORY_EDIT_ERROR = new CodeMsg(-2003, "Fail to modify article category.");
    public static CodeMsg CATEGORY_DELETE_ERROR = new CodeMsg(-2004, "Fail to delete article category.");


    public static CodeMsg TAG_NAME_EXIST = new CodeMsg(-3000, "Article tag already exists.");
    public static CodeMsg TAG_ADD_ERROR = new CodeMsg(-3001, "Fail to add article tag.");
    public static CodeMsg TAG_NOT_EXIST  = new CodeMsg(-3002, "Article tag does not exist");
    public static CodeMsg TAG_EDIT_ERROR = new CodeMsg(-3003, "Fail to modify article tag.");
    public static CodeMsg TAG_DELETE_ERROR = new CodeMsg(-3004, "Fail to delete article tag.");


    public static CodeMsg ARTICLE_ADD_ERROR = new CodeMsg(-4000, "Fail to add article.");
    public static CodeMsg ARTICLE_NOT_EXIST  = new CodeMsg(-4001, "Article does not exist.");
    public static CodeMsg ARTICLE_EDIT_ERROR = new CodeMsg(-4002, "Fail to modify article.");
    public static CodeMsg ARTICLE_DELETE_ERROR = new CodeMsg(-4003, "Fail to delete article.");
    public static CodeMsg ARTICLE_TAG_EMPTY = new CodeMsg(-4004, "Article tag cannot be empty.");
    public static CodeMsg ARTICLE_TAG_OVER = new CodeMsg(-4005, "Cannot have more than 3 tags.");


    public static CodeMsg COMMENT_SUBMIT_ERROR = new CodeMsg(-5000, "Fail to post comment.");
    public static CodeMsg COMMENT_DELETE_ERROR = new CodeMsg(-5001, "Fail to delete comment.");
    public static CodeMsg COMMENT_NOT_EXIST = new CodeMsg(-5002, "Comment does not exist.");
    public static CodeMsg COMMENT_PICK_ERROR = new CodeMsg(-5003, "Fail to adopt solution.");


    public static CodeMsg LIKE_ERROR = new CodeMsg(-6000, "Fail to like.");
    public static CodeMsg UNLIKE_ERROR = new CodeMsg(-6001, "Fail to cancel like.");


    public static CodeMsg COLLECT_ADD_ERROR = new CodeMsg(-7000, "Fail to add to collection.");
    public static CodeMsg COLLECT_REMOVE_ERROR = new CodeMsg(-7001, "Fail to remove from collection.");


    public static CodeMsg ATTENTION_ADD_ERROR = new CodeMsg(-8000, "Fail to follow.");
    public static CodeMsg ATTENTION_REMOVE_ERROR = new CodeMsg(-8001, "Fail to unfollow.");
    public static CodeMsg ATTENTION_AGAIN_ERROR = new CodeMsg(-8002, "Already followed.");
    public static CodeMsg ATTENTION_SELF_ERROR = new CodeMsg(-8003, "You cannot follow yourself.");
}
