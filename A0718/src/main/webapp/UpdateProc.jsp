<%@page import="test.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<body>

<%
	request.setCharacterEncoding("utf-8");
%>
<jsp:useBean id="boardbean" class="test.BoardBean">
	<jsp:setProperty name="boardbean" property="*"/>
</jsp:useBean>

<%
	BoardDAO bdao=new BoardDAO();
	String pass=bdao.getPass(boardbean.getNum());
	//디비에 들어있는 패스워드와 update할때 작성했던 패스워드
	
	if(pass.equals(boardbean.getPassword())){
		bdao.updateBoard(boardbean);
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