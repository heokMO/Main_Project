<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="hgroup1">
	
	<div id="logo">
		<a href="/"><img src="/resources/images/MALON_logo.png" id=logo_img></a>
	</div>


	<div id="menu_bar">

		<div id="left-menu">
			<a href="/test" id="test">test</a>
			<a href="/userrecommend/list" id="test">추천리스트</a>
			<a href="/" id="myList">마이리스트</a>
		</div>
		
		<div id="middle-menu">
			<input type="text" id="search_word" name="search_word" placeholder="검색어를 입력해주세요">
			<input type="button" id="searchBtn" name="searchBtn" value="검색">
		</div>

		<div id="right-menu" class="menu2">
			<c:choose>
				<c:when test="${sessionScope.sessionId == null}">
					<a href="/member/loginPage">로그인</a>
					<a href="/member/joinForm">회원가입</a>
				</c:when>
				<c:when test="${sessionScope.sessionId != null}">
					<a href="/member/updateDetail">${sessionScope.sessionNick}님</a>
					<a id="logout">로그아웃</a>
				</c:when>
			</c:choose>
		</div>
	</div>	
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
	function logoutConfirm() {
		 if (confirm("로그아웃 하시겠습니까?") == true){    //확인
			 window.location.href = '/member/logout';
		 }else{  
		     return false;
		 }
	}

	$('#logout').click(function(e){
		e.stopPropagation();
		logoutConfirm();
	});

</script>
