<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ include file="include/include.jsp" %>
<%
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
Object principal = auth.getPrincipal();

String name = "";
if (principal != null) {
	name = auth.getName();
}
%>
<!DOCTYPE html>
<html>
<head>
<title>로그인 화면</title>
<script>
	$(document).ready(
			function() {

				$("#emailAuth").click(
						function() {
							$.ajax({
								async : true,
								type : 'POST',
								url : "emailAuth",
								dataType : "text",
								beforeSend : function(xhr) {
									//이거 안하면 403 error
									//데이터를 전송하기 전에 헤더에 csrf값을 설정한다
									var $token = $("#token");
									xhr.setRequestHeader($token
											.data("token-name"), $token.val());
								},
								success : function() {
									alert("사용자 이메일로 인증메일을 보냈습니다. 재 로그인 해주세요");
									$("#logout").trigger("click");
								},
								error : function(request, status, error) {
									alert("code:" + request.status + "\n"
											+ "message:" + request.responseText
											+ "\n" + "error:" + error);
								}
							});
						});

			});
</script>
</head>
<body>
	<div align="center">
		<%=name%>님 로그인 완료 &nbsp; &nbsp;<a href="/board">게시판</a>

		<sec:authorize access="hasRole('ROLE_NUSER')">
			<br/><button id="emailAuth">이메일 인증하기</button>
			<br/><br/>
		</sec:authorize>
		<form action="/logout" method="post">
			<input type="hidden" id="token" data-token-name="${_csrf.headerName}" placeholder="Password" value="${_csrf.token}">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token}"/>
			<input type="submit" id="logout" value="logout">
		</form>
	</div>
</body>
</html>