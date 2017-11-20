package servlets;

import database.LocalDB;
import database.UserInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TestServlet extends HttpServlet {
    private static LocalDB localDB = new LocalDB();
    private static ArrayList<UserInfo> all = new ArrayList<>();
    private static ArrayList<UserInfo> family = new ArrayList<>();
    private static ArrayList<UserInfo> friends = new ArrayList<>();
    private static ArrayList<UserInfo> work = new ArrayList<>();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();

        if(uri.equals("/mainpage")) {
            try (PrintWriter out = response.getWriter()) {
                localDB.loadDB();
                out.println("<html>\n<body>\n");
                out.println("<a href=\"/mainpage/all\">all</a>");
                out.println("<a href=\"/mainpage/work\">work</a>");
                out.println("<a href=\"/mainpage/family\">family</a>");
                out.println("<a href=\"/mainpage/friends\">friends</a>");
                out.println(localDB.toString());
                out.println("\n<a href=\"/mainpage/addname\">addname</a>\t<a href=\"/mainpage/addphoneto\">addphone</a>");
                out.println("<a href=\"/mainpage/addtogroup\">addtogroup</a>");
                out.println("</body>\n</html>");
            }
        }
        else if(uri.equals("/mainpage/addname")){
            try(PrintWriter out = response.getWriter()){
                StringBuilder sb = new StringBuilder("");
                out.println("<html>\n<body>\n");
                sb.append("<form method=\"GET\" action=\"/mainpage/addingname\">\n");
                sb.append("Name: <input type=\"text\" name=\"name\">\n");
                sb.append("<input type=\"submit\" value=\"add\">\n");
                sb.append("</form>");
                out.println(sb.toString());
                out.println("</body>\n</html>");
            }
        }
        else if(uri.equals("/mainpage/addingname")){
            localDB.addName(request.getParameter("name"));
            localDB.saveDB();
        }
        else if(uri.equals("/mainpage/addphoneto")){
            try(PrintWriter out = response.getWriter()){
                out.println("<html>\n<body>\n");

                out.println("<p>Add phone to : \n</p>");

                for(String i : localDB.getName()){
                    out.println("<a href=\"/mainpage/addphone?name="+ i + "\">"+ i +"</a>\n");
                    request.setAttribute("name", i);
                    out.println("<p>\n</p>");
                }
                out.println("</body>\n</html>");
            }
        }
        else if(uri.equals("/mainpage/addphone")){
            try(PrintWriter out = response.getWriter()) {
                StringBuilder sb = new StringBuilder("");
                out.println("<html>\n<body>\n");
                sb.append("<form method=\"GET\" action=\"/mainpage/addingphone\">\n");
                sb.append("Phone: <input type=\"text\" name=\"phone\">\n");
                sb.append("<input type=\"submit\" value=\"add\">\n");
                sb.append("<input type=\"hidden\" name=\"name\" value=\"").append
                        (request.getParameter("name")).append("\" >");
                sb.append("</form>");
                out.println(sb.toString());
                out.println("</body>\n</html>");
            }
        }
        else if(uri.equals("/mainpage/addingphone")){
            localDB.addPhone(request.getParameter("name"), request.getParameter("phone"));
            localDB.saveDB();
        }
        else if(uri.equals("/mainpage/addtogroup")){
            try(PrintWriter out = response.getWriter()){
                out.println("<html>\n<body>\n");
                out.println("<p>Choose contact : \n</p>");
                for(String i : localDB.getName()){
                    out.println("<a href=\"/mainpage/choosegroup?name="+ i + "\">"+ i +"</a>\n");
                    out.println("<p>\n</p>");
                }
                out.println("</body>\n</html>");
            }
        }
        else if(uri.equals("/mainpage/choosegroup")){
            try(PrintWriter out = response.getWriter()) {
                StringBuilder sb = new StringBuilder("");
                out.println("<html>\n<body>\n");
                sb.append("<form method=\"GET\" action=\"/mainpage/addedtogroup\">\n");
                sb.append("<input name=\"group\" type=\"radio\" value=\"family\">");
                sb.append("<p> family </p>\n");
                sb.append("<input name=\"group\" type=\"radio\" value=\"friends\">");
                sb.append("<p> friends </p>\n");
                sb.append("<input name=\"group\" type=\"radio\" value=\"work\">");
                sb.append("<p> work </p>\n");
                sb.append("<input type=\"submit\" value=\"add\">\n");
                sb.append("<input type=\"hidden\" name=\"name\" value=\"").append
                        (request.getParameter("name")).append("\" >");
                sb.append("</form>");
                out.println(sb.toString());
                out.println("</body>\n</html>");
            }
        }
        else if(uri.equals("/mainpage/addedtogroup")){
            UserInfo userInfo = new UserInfo(request.getParameter("name"));
            userInfo.addPhone(localDB.getPhones(userInfo.getName()));
            switch (request.getParameter("group")){
                case "family":
                    family.add(userInfo);
                    break;
                case "friends":
                    friends.add(userInfo);
                    break;
                case "work":
                    work.add(userInfo);
                    break;
                default:
                    break;
            }
            try (PrintWriter out = response.getWriter()) {
                out.println("<html>\n<body>\n");
                out.println("<p> added to group </p>");
                out.println("<a href=\"/mainpage\">mainpage</a>");
                out.println("</body>\n</html>");
            }
        }
        else if(uri.equals("/mainpage/all")){
            try (PrintWriter out = response.getWriter()) {
                out.println(getForm(all));
            }
        }
        else if(uri.equals("/mainpage/family")){
            try (PrintWriter out = response.getWriter()) {
                out.println(getForm(family));
            }
        }
        else if(uri.equals("/mainpage/friends")){
            try (PrintWriter out = response.getWriter()) {
                out.println(getForm(friends));
            }
        }
        else if(uri.equals("/mainpage/work")){
            try (PrintWriter out = response.getWriter()) {
                out.println(getForm(work));
            }
        }
    }

    private String getForm(ArrayList<UserInfo> info){
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("<html>\n<body>\n");
        for(UserInfo i : info){
            stringBuilder.append("<p>").append(i.getName()).append("</p>\n");
            stringBuilder.append("<p>").append(i.getPhoneNumbers()).append("</p>\n");
        }
        stringBuilder.append("<a href=\"/mainpage\">mainpage</a>");
        stringBuilder.append("</body>\n</html>");
        return stringBuilder.toString();
    }
}