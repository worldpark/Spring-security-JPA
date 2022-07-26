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
<meta charset="UTF-8">
<title>JPA 게시판</title>

<script type="text/javascript">
	$(document).ready(function(){
		$("#btnsave").click(function(){
			var subject = $("#subject").val();
			var content = $("#content").val();
			
			if(subject == "")
			{
				alert("제목을 입력하세요");
				document.form1.subject.focus();
			}else if(content == "")
			{
				alert("내용을 입력하세요");
				document.form1.content.focus();
			}else{
				
				document.form1.action = "${path}/board/insert";
				document.form1.submit();
			}
		});
	});
	
	function addFile(){
		var str = "<div class='file-group'><input type='file' name='file'><a href='#this' name='file-delete'>삭제</a></div";
		$("#file-list").append(str);
		$("a[name='file-delete']").on("click", function(e){
			e.preventDefault();
			deleteFile($(this));
		});
	}
	
	function deleteFile(obj){
		obj.parent().remove();
	}
</script>
<style>
	body{
		padding-top: 70px;
		padding-left: 30px;
	}
</style>

<style>
	.select_img img{
		margin:20px 0;
	}
</style>

</head>
<body>
	<div style="margin-left:250px">
	<form name = "form1" enctype="multipart/form-data" method="POST" accept-charset="UTF-8">
		<input type = "hidden" name = "id" value = "<%=name %>"/>
		<div>
			글쓴이<span class="tab">&#9;</span><input id="userid" name = "userid" size = "40" value = "${username}" readonly/>
		</div>
		<div>
			제목<span class="tab">&#9;&#9;</span><input id = "subject" name = "subject" size = "40" placeholder="제목을 입력해주세요">
		</div>
		<div>
			내용<br/>
			<textarea id="content" rows="10" name="content" cols="100" placeholder="내용을 입력해주세요"></textarea>
		</div>
		<div>
			<input type="hidden" id="token" data-token-name="${_csrf.headerName}" placeholder="Password" value="${_csrf.token}">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		</div>
		<div>
		</div>
		
		<div class="form-group" id = "file-list">
			<a href="#this" onclick="addFile()">파일추가</a>
			<div class="file-group">
				<input type="file" id="file" name="file"/><a href="#this" name="file-delete">삭제</a>
			</div>
		</div>
		<div style = "width:650px; text-align:center;">
			<button type = "button" id = "btnsave">확인</button>
			<button type = "button" onclick = "history.back(-1);">취소</button>
		</div>
	</form>
	
	</div>
</body>
</html>