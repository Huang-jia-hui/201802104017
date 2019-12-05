//201802104017黄佳慧
package filter;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//通配符*表示对所有的web资源有效
@WebFilter(filterName = "Filter 2",urlPatterns = "/*")
public class Filter20 implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //打印提示信息
        System.out.println("loginFilter begins");
        //强制类型转换成HttpServletRequest类型
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //获得请求的url
        String path = request.getRequestURI();
        //创建Json对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        if(!path.contains("/login")){
            //访问权限验证
            HttpSession session = request.getSession(false);
            //访问任何一个资源之前，都要进行登录验证。如果发现访问者没有请求登录功能，则返回“您没有登录，请登录”
            if(session==null||session.getAttribute("currentUser")==null){
                message.put("message","请登陆或重新登陆");
                //响应message到前端
                response.getWriter().println(message);
                return;
            }

        }
        //执行其他过滤器，若过滤器已经执行完毕，则执行原请求
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("loginFilter ends");
        }
    @Override
    public void destroy() {
    }

}
