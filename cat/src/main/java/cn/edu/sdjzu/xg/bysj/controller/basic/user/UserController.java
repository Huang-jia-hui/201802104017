//201802104017黄佳慧
package cn.edu.sdjzu.xg.bysj.controller.basic.user;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
//映射
@WebServlet("/user.ctl")
public class UserController extends HttpServlet {
    //修改密码
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user_json = JSONUtil.getJSON(request);
        //将JSON字串解析为User对象
        User userToAdd = JSON.parseObject(user_json,User.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加修改后的对象
        try {
            UserService.getInstance().changePassword(userToAdd);
            message.put("message", "修改密码成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        String username = request.getParameter("username");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            if (id_str != null){
                int id = Integer.parseInt(id_str);
                this.responseUser(id,response);
            }else{
                this.responseUserByUserName(username,response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    //响应一个user对象，按照id
    private void responseUser(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找user
        User user = UserService.getInstance().find(id);
        //定义字符串，将user转换为json字串
        String user_json = JSON.toJSONString(user);
        //响应user_json到前端
        response.getWriter().println(user_json);
    }
    //响应一个user对象,按照账户名
    private void responseUserByUserName(String username, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据姓名查找user
        User user = UserService.getInstance().findByUsername(username);
        //定义字符串，将user转换为json字串
        String user_json = JSON.toJSONString(user);
        //响应user_json到前端
        response.getWriter().println(user_json);
    }
}
