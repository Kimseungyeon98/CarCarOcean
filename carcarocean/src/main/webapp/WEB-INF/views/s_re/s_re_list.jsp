<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 목록</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
<jsp:include page="/WEB-INF/views/common/header.jsp" />
</head>
<body>
<div class="page-main">
	<div class="content-main">
		<h2>게시판 목록</h2>
		<div class="list-space align-right">
			<input type="button" value="글쓰기" 
				onclick="location.href='writeForm.do'"	
					<c:if test="${empty user_num}">disabled="disabled" </c:if> <%-- 비홯성화 시키는 법 --%>
			> <!-- input 닫는 꺽쇄 -->
			<input type="button" value="목록" onclick="location.href='list.do'">
			<input type="button" value="홈으로" onclick="location.href='${pageContext.request.contextPath}/main/main.do'">
		</div>
	</div>
</div>
</body>
</html>