<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<title>나에게 온 채팅 보기</title>
<style>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap');
.noto-sans-kr-<uniquifier> {
  font-family: "Noto Sans KR", sans-serif;
  font-optical-sizing: auto;
  font-weight: <weight>;
  font-style: normal;
}
</style>
</head>
<body style="font-family:Noto Sans KR;">
	<jsp:include page="/WEB-INF/views/common/loading.jsp" />
	<div class="container border rounded my-5 px-5">
		<h2 class="text-center p-3">채팅 목록</h2>
		<c:if test="${empty chatList}">
			<div class="text-center my-3">채팅이 없습니다.</div>
		</c:if>
		<c:if test="${!empty chatList}">
			<c:forEach var="chat" items="${chatList}">
			<div class="d-flex justify-content-between bg-light border rounded my-3 p-3">
				<div class="align-items-center fw-bold d-flex">
					<div><span class="badge bg-danger me-2">New</span></div>
					<div><img src="${pageContext.request.contextPath}/upload/${chat.giver.mem_photo}"class="rounded-circle" width="50px" height="50px"></div>
					<div class="ms-4">
						<div class="mb-2">${chat.giver.mem_id}님의 채팅</div>
						<div class="mt-2">${chat.chat_message}</div>
					</div>
				</div>
				<div class="align-self-center">
					<button class="btn btn-warning bnt-lg text-white fw-bold" onclick="location.href='${pageContext.request.contextPath}/chat/chat.do?item_num=${item_num}&chat_giver=${user_num}&chat_receiver=${chat.giver.mem_num}'">대화 참여</button>
				</div>
			</div>
			</c:forEach>
		</c:if>
		<div class="d-flex justify-content-center">
			<button class="btn btn-warning bnt-lg text-white fw-bold my-3" onclick="history.back()">이전으로</button>
		</div>
	</div>
</body>
</html>
