<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../include/include.jsp"%>
<meta charset="UTF-8">
<title>JPA 게시판</title>

<script>
	$(document).ready(function(){
		$("#btnsave").click(function(){
			var subject = $("#subject").val();
			var content = $("#content").val();
			
			if(subject == "")
			{
				alert("제목을 입력하세요");
				document.form1.subject.focus();
				return;
			}else if(content == "")
			{
				alert("내용을 입력하세요");
				document.form1.content.focus();
				return;
			}else{
				document.form1.action = "${path}/board/update";
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
	
	var fileIdArr = new Array();
	
	function fn_del(fid){
		fileIdArr.push(fid);
		$("#fileNoDel").attr("value", fileIdArr);
		$("#file-input_" + fid).empty();
	}
	
</script>

<style>
	.select_img img{
		margin:20px 0;
		width:800px;
		height: auto;
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
	<form id = "form1" name = "form1" method = "post" enctype="multipart/form-data">
		<div>
			글쓴이<span class="tab">&#9;</span><input name = "userid" size = "40" value = "${dto.userid}" readonly/>
		</div>
		<div>
			제목<span class="tab">&#9;&#9;</span><input name = "subject" size = "80" placeholder="제목을 입력해주세요" value = "${dto.subject}">
		</div>
		<div>
			내용<br/>
			<textarea rows="10" name="content" cols="100">${dto.content}</textarea>
		</div>
		<div>
			<input type="hidden" id="token" data-token-name="${_csrf.headerName}" placeholder="Password" value="${_csrf.token}">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token}"/>
		</div>
		
		<div class="form-group" id = "file-list">
			<a href="#this" onclick="addFile()">파일추가</a>
			
			<div class="file-group">
			</div>
		</div>
		
		<input type="hidden" id="fileNoDel" name="fileNoDel[]" value="">
		
		<c:forEach var="files" items="${file}">
            <div class="file-input" id = "file-input_${files.fid}">
             	${files.filename}
				<input type="hidden" name="FILE_${files.fid}" value="${files.fid}">
				<a href="#this" id="fileDel" onclick="fn_del('${files.fid}')">삭제</a>
            </div>
        </c:forEach>
		
		<div style = "width:650px; text-align:center;">
			<input type = "hidden" name = "bid" value = "${dto.bid}">
			<button type = "button" id = "btnsave">확인</button>
			<button type = "button" onclick = "history.back(-1);">취소</button>
		</div>
	</form>
	</div>
</body>
</html>