<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="include/include.jsp"%>

<meta id="_csrf.parameterName" content="${_csrf.token}"/>

<title>로그인 폼 화면</title>
</head>
<body>
	<div class="container" align="center">

		<div class="col-lg-4"></div>

		<div class="col-lg-4">
				<div style="margin-top: 20px;">

			<div class="jumbotron" style="padding-top: 20px;" >

				<!-- 로그인 정보를 숨기면서 전송post -->
				<form name = "form1" method="POST" action = "/login.do">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

					<h3 style="text-align: center;">로그인</h3>
					
					<c:if test="${msg == false }">
						<p style="color:#ff0000;">아이디 또는 비밀번호가 다릅니다.</p>
					</c:if>
					
					<div class="form-group">
						<input type="text" class="form-control" placeholder="아이디"
							name="username">
					</div>

					<div class="form-group">
						<input type="password" class="form-control" placeholder="비밀번호"
							name="password">
					</div>

					<input type="submit" class="btn btn-primary form-control" id="login" 
						value="Login">
				</form>
				<br/>
				${exception}
				
				<a class="nav-link px-2" href="/regist" onclick="window.open(this.href,'회원가입' ,'width=500, height=800'); return false">regist</a>
			</div>
				</div>
		</div>
	</div>
</body>
</html>