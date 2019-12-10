//201802104017黄佳慧
package cn.edu.sdjzu.xg.bysj.security;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            User userToCheck = UserService.getInstance().login(username,password);
            if(userToCheck!=null){
                message.put("message","登陆成功");
                HttpSession session = request.getSession();
                //10分钟没有操作，session失效
                session.setMaxInactiveInterval(10*60);
                session.setAttribute("currentUser",userToCheck);
                response.getWriter().println(message);
                //此处应重定向到索引页
                return;
            }else {
                message.put("message","用户名或密码错误");
                response.getWriter().println(message);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
    }

}
