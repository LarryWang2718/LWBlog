global.tools = {


    setLoginUser: function (loginUser) {
        global.SessionStorage.set(global.SessionStorage.SESSION_KEY_LOGIN_USER, loginUser);
    },


    getLoginUser: function () {
        return global.SessionStorage.get(global.SessionStorage.SESSION_KEY_LOGIN_USER) || "";
    },


    isEmpty: function (obj) {
        if ((typeof obj === 'string')) {
            return !obj || obj.replace(/\s+/g, "") === ""
        } else {
            return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0);
        }
    },


    isNotEmpty: function (obj) {
        return !this.isEmpty(obj);
    }

};
