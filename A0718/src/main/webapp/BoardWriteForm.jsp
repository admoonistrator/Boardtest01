<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
	request.setCharacterEncoding("utf-8");
%>

<jsp:useBean id="boardbean" class="test.BoardBean">
	<jsp:setProperty property="*" name="boardbean"/>
</jsp:useBean>

<%
	BoardDAO bdao=new BoardDAO();
	bdao.insertBoard(boardbean); //폼에 적었던 값이 테이블에 추가되면서
	response.sendRedirect("BoardList.jsp");//페이지가 이동

%>
</body>
</html>