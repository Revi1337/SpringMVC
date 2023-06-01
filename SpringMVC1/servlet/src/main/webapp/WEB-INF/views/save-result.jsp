<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: revi1
  Date: 2023-06-02
  Time: AM 7:26
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<ul>
<%--    <li>id = <%=((Member)request.getAttribute("member")).getId()%></li>--%>
<%--    <li>id = <%=((Member)request.getAttribute("member")).getUsername()%></li>--%>
<%--    <li>id = <%=((Member)request.getAttribute("member")).getAge()%></li>--%>
    <li>id = ${member.getId()}</li>
    <li>id = ${member.getUsername()}</li>
    <li>id = ${member.getAge()}</li>
</ul>
</body>
</html>
