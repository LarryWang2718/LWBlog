package com.yjq.programmer.constant;

import java.util.Arrays;
import java.util.List;

public class RuntimeConstant {

	public static List<String> loginExcludePathPatterns = Arrays.asList(
			"/web/user/login",
			"/web/user/register",
			"/common/photo/view",
			"/common/photo/upload_photo",
			"/web/tag/all",
			"/web/category/all",
			"/web/user/check_login",
			"/web/article/list",
			"/web/article/view",
			"/web/comment/list",
			"/web/comment/total",
			"/web/article/judge",
			"/web/user/get",
			"/web/article/hot",
			"/web/article/author",
			"/web/attention/judge",
			"/web/attention/list",
			"/web/attention/all",
			"/web/article/get",
			"/admin/user/login"
	);

}
