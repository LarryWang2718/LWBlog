global.SessionStorage = {

    SESSION_KEY_LOGIN_USER: "SESSION_KEY_LOGIN_USER",

    get: function (key) {
        let v = sessionStorage.getItem(key);
        if (v && typeof(v) !== "undefined" && v !== "undefined") {
            let params = JSON.parse(v);
            if (params.time) {
                if (params.time > Date.parse(new Date())) {
                    return params.data;
                }
                global.SessionStorage.remove(key);
            }
        }
    },
    set: function (key, data) {
        let params = {
            data: data,
            time: Date.parse(new Date()) + 60 * 1000 * 60
        };
        sessionStorage.setItem(key, JSON.stringify(params));
    },
    remove: function (key) {
        sessionStorage.removeItem(key);
    },
    clearAll: function () {
        sessionStorage.clear();
    }
};

