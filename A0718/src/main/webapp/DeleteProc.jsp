<%@page import="test.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<body>

<%
	String pass=request.getParameter("password");
	int num=Integer.parseInt(request.getParameter("num"));
	
	BoardDAO bdao=new BoardDAO();
	String password=bdao.getPass(num);
	
	if(pass.equals(password)){
		bdao.deleteBoard(num);
		response.sendRedirect("BoardList.jsp");
	}
	else{
%>

	<script type="text/javascript">
			alert("패스워드가 일치하지 않는다. 다시 입력해라.");
			history.go(-1);
	</script>

<%
	}
	
%>

</body>
</html>