package com.qunar.fresh.librarysystem.utils;

import com.qunar.fresh.librarysystem.model.User;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-4-2 Time: 上午11:00 To change this template use File | Settings |
 * File Templates.
 */
public class UserUtils {
    private static final Logger logger = LoggerFactory.getLogger(UserUtils.class);
    private static final String RTX_ATTRIBUTE = "userRtx";
    private static final String LIB_ATTRIBUTE = "userLibId";
    private static final String NAME_ATTRIBUTE = "name";

    private static String url = "http://sso.corp.qunar.com/qunar.php?action=4&user=";
    private static String containString = "\"status\":true,";

    /**
     * 通过sso接口，验证前端发送的用户名称是否有效
     * 
     * @param userRtx
     * @return true，有效： false, 无效
     * @throws IOException
     */
    public static boolean checkUser(String userRtx) throws IOException {
        String html = Jsoup.connect(url + userRtx.trim()).execute().body();
        if (html.contains(containString)) {
            return true;
        }
        return false;
    }

    public static User getUserInfo(String userRtx) throws IOException {
        logger.info("get user info from sso");
        Map<String, String> userInformation = new HashMap<String, String>();
        User user = null;
        String jsString = Jsoup.connect(url + userRtx.trim()).execute().body();

        JSONObject jo = (JSONObject) JSONValue.parse(jsString);
        if (jo.get("status").equals(true)) {

            String userInfo = jo.get("message").toString();
            JSONObject joo = (JSONObject) JSONValue.parse(userInfo);
            Iterator it = joo.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next().toString();
                userInformation.put(key, joo.get(key).toString());
            }
            user = map2User(userInformation);
            return user;
        }
        return user;
    }

    private static User map2User(Map<String, String> map) {
        User user = new User();
        user.setUserRtx(map.get("name"));
        user.setEmail(map.get("email"));
        user.setUserDept(map.get("deptname"));
        user.setMobile(map.get("mobile"));
        user.setUserName(map.get("givenName"));
        return user;
    }

    private static String getCookieValue(String pattern, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (pattern.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }

    /**
     * 获取用户的rtx
     * 
     * @param request
     * @return
     */
    public static String getUserRtx(HttpServletRequest request) {
        String codeRtx = getCookieValue(NAME_ATTRIBUTE, request);
        return EncodeUtils.decodeString(codeRtx);
    }

    public static int getUserLib(HttpServletRequest request) {
        String codeLibId = getCookieValue(LIB_ATTRIBUTE, request);
        return EncodeUtils.decodeInt(codeLibId);
    }

    /**
     * he.chen
     * 
     * @param userRtx
     * @return
     */
    public static String getUserDept(String userRtx) {
        String jsString;
        String userDept = "";
        try {
            jsString = Jsoup.connect(url + userRtx.trim()).execute().body();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsString);
            JsonNode statusNode = jsonNode.findValue("status");
            if (statusNode.asBoolean()) {
                JsonNode messagerNode = jsonNode.findValue("message");
                userDept = messagerNode.findValue("deptname").asText();
            }
        } catch (IOException e) {
            logger.error("请求个人信息的时候连接异常，{}", e);
        }
        return userDept;
    }

    public static String getUserName() {
        return null;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        UserUtils.url = url;
    }

    public static String getContainString() {
        return containString;
    }

    public static void setContainString(String containString) {
        UserUtils.containString = containString;
    }
}
