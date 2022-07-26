<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>

<%@ include file="include/include.jsp"%>

<style>
    body {
        background: #f8f8f8;
        padding: 60px 0;
    }
    
    #login-form > div {
        margin: 15px 0;
    }

</style>

<script type="text/javascript">
var checkid = false;
var checkpassword = false;
var checkemail = false;

$(document).ready(function(){
	
	$("#reg_submit").click(function() {
		var id = $("#id").val();
		var password = $("#password").val();
		var repassword = $("#repassword").val();
		
		//json
		var sendData = "id="+id+'&password='+password;


		if(password != repassword)
		{
			alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
		}
		else if(checkid == true && checkemail == true && checkpassword == true)
		{
		$.ajax({
			async : true,
			type : 'POST',
			data:JSON.stringify({
				"id" : $("#id").val(),
				"password" : $("#password").val(),
				"email" : $("#email").val()
			}),
			url : "memberRegist",
			dataType : "json",
			contentType : "application/json; charset=UTF-8",
			beforeSend : function(xhr)
			{
				//이거 안하면 403 error
				//데이터를 전송하기 전에 헤더에 csrf값을 설정한다
				var $token = $("#token");
				xhr.setRequestHeader($token.data("token-name"), $token.val());
			},
			success : function() {
				alert("회원가입성공");
				window.close();
			},
			error : function(request,status,error)
			{
				alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
		}
		else
		{
			alert("사용자 정보가 유효하지 않습니다.");
		}
	});
});

	$(function(){
		$('#id').on('blur', function(){
			var id = $("#id").val();

			if(id == "")
			{
				$("#id_check").text("아이디를 입력하세요");
				$("#id_check").css("color", "red");
				checkid = false;
			}
			else{
			$.ajax({
				async : true,
				type : 'POST',
				data : id,
				url : "idcheck",
				dataType : "json",
				contentType : "application/json; charset=UTF-8",
				beforeSend : function(xhr)
				{
					//이거 안하면 403 error
					//데이터를 전송하기 전에 헤더에 csrf값을 설정한다
					var $token = $("#token");
					xhr.setRequestHeader($token.data("token-name"), $token.val());
				},
				success : function(data)
				{
					if(data.cnt > 0)
					{
						$("#id_check").text("사용중인 아이디");
						$("#id_check").css("color", "red");
						checkid = false;
					}
					else
					{
						$("#id_check").text("사용가능한 아이디");
						$("#id_check").css("color", "blue");
						checkid = true;
					}
				},
				error : function(request,status,error)
				{
					alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			});
				
			}
		});
	});

	$(function(){
		$('#password').on('blur', function(){
			var password = $("#password").val();

			if(password == "")
			{
				$("#id_check").text("비밀번호를 입력하세요");
				$("#id_check").css("color", "red");
				checkpassword = false;
			}
			else{
				checkpassword = true;
			}
		});
	});

	$(function(){
		$('#email').on('blur', function(){
			var email = $("#email").val();

			if(email == "")
			{
				$("#id_check").text("이메일을 입력하세요");
				$("#id_check").css("color", "red");
				checkemail = false;
			}
			else{
				checkemail = true;
			}
		});
	});
	
</script>

</head>
<body>
<c:set var="emailCheck" value="${ECheck}"/>
<div class="container">
	<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
		<div class="panel panel-success">
            <div class="panel-heading">
                <div class="panel-title">회원가입</div>
            </div>
            <div class="panel-body">
                <form id="login-form" name = "form1">
                    <div>
						회원 ID : <input type="text" id = "id" class="form-control" name="id" placeholder="회원ID" autofocus>
                    	<div id = "id_check"></div>
                    </div>
                    <div>
						비밀번호 : <input type="password" class="form-control" id = "password" name="password" placeholder="Password">
                    </div>
                    <div>
						비밀번호 확인 : <input type="password" class="form-control" id = "repassword" name="repassword" placeholder="비밀번호 확인">
                    </div>
                    <div>
						이메일 : <input type="text" id="email" class="form-control" name="email" placeholder="이메일">
                    </div>
					<div>
						<input type="hidden" id="token" data-token-name="${_csrf.headerName}" placeholder="Password" value="${_csrf.token}">
					</div>
                    <div>
                    	<button type="button" id = "reg_submit" class="form-control btn btn-primary">회원가입</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>