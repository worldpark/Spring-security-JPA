<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ include file="../include/include.jsp" %>
<%
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	Object principal = auth.getPrincipal();

	String name = "";
	if(principal != null) {
	    name = auth.getName();
	}
%>
<!DOCTYPE html>
<html>
<head>
<title>JPA 게시판</title>

<script>
	$(document).ready(function(){
		
		$("#btndelete").click(function(){
			
			if(confirm("삭제하시겠습니까?"))
			{
				document.form1.action = "${path}/board/delete";
				document.form1.submit();
			}
		});
		
		$("#btnupdate").click(function(){
			document.form1.action = "${path}/board/update_write";
			document.form1.submit();
		});
		
	});
</script>

<style>
	.oriImg{
		width:800px;
		height: auto;
		margin: 20px 0;
	}
	.thumbImg{
	}
</style>
<style>
	body{
		padding-top: 70px;
		padding-left: 30px;
	}
</style>

</head>
<body>
	
	<div style="margin-left:250px">
	<form id = "form1" name = "form1" enctype="multipart/form-data" method="POST" accept-charset="UTF-8">
		<div>
			게시물번호 :<span class="tab">&#9;</span><input id = "bid" name = "bid" size = "40" value = "${dto.bid}" readonly/>
		</div>
		<div>
			작성일자 :<span class="tab">&#9;</span><fmt:formatDate value = "${dto.createdate}" pattern = "yyyy-MM-dd"/>
		</div>
		<div>
			글쓴이<span class="tab">&#9;&#9;</span><input name = "userid" size = "40" value = "${dto.userid}" readonly/>
		</div>
		<div>
			제목<span class="tab">&#9;&#9;&#9;</span><input name = "subject" size = "40" value = "${dto.subject}" readonly>
		</div>
		<div>
			내용<br/>
			<textarea rows="10" name="content" cols="100" readonly>${dto.content}</textarea>
		</div>
		<div>
			<c:choose>
				<c:when test="${!empty file }">
					<c:forEach var="files" items="${file}">
						<a href="#" onclick="window.location='/filedownload?fid=' + ${files.fid}; return false;">${files.filename}</a>
					</c:forEach>
				</c:when>
			</c:choose>
		</div>
		<div>
			<input type="hidden" id="token" data-token-name="${_csrf.headerName}" placeholder="Password" value="${_csrf.token}">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token}"/>
		</div>
		
		
		<c:set var="id" value = "<%=name %>"/>
		<c:set var="creater" value = "${dto.userid}"/>
		
		<c:choose>
    		
			<c:when test = "${id == creater }">
				<div style = "widht:650px; margin-left:250px;">
					<input type = "hidden" name = "bid" value= "${dto.bid}">
					<button type = "button" id = "btnupdate">수정</button>
					<button type = "button" id = "btndelete">삭제</button>
				</div>
    		</c:when>
    		
		</c:choose>
		
		
	</form>
	
	</div>

</body>
</html>