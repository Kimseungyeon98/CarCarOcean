<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
   content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<title>고객 문의</title>
<script type="text/javascript">
	window.onload = function(){
		const myForm = document.getElementById('search_form');
		//이벤트 연결
		myForm.onsubmit = function(){
			const keyword = document.getElementById('keyword');
			if(keyword.value.trim()==''){
				alert('검색어를 입력하세요');
				keyword.value = '';
				keyword.focus();
				return false;
			}
		};
		
	};
</script>
</head>
<body>
	<div class="page-main">
		<jsp:include page="/WEB-INF/views/common/header.jsp"/>
		<div class="container">
		<hr size="1" noshade="noshade" width="100%">
		<h2 class="pt-5">고객 문의</h2>
		
			<form id="search_form" action="list.do" method="get">
				<div>
					<div class="text-start mt-3" style="font-size:15pt;">
						총 ${count}건의 글이 있습니다.
					</div>
				<!-- 검색바 -->
				<div class="d-flex justify-content-end mb-4">
					<select name="keyfield" class="form-select" style="width:auto;">
						<option value="1" <c:if test="${param.keyfield == 1}">selected</c:if>>제목</option>
						<option value="2" <c:if test="${param.keyfield == 2}">selected</c:if>>작성자</option>
						<option value="3" <c:if test="${param.keyfield == 3}">selected</c:if>>내용</option>
					</select>
					<input type="search" size="20" name="keyword" class="form-control rounded" id="keyword" placeholder="Search"
						value="${param.keyword}" style="width:200px;">
					<input type="submit" class="btn btn-warning ms-2" value="검색">
					<input type="button" class="btn btn-warning ms-2" value="검색초기화" onclick="location.href='list.do'">
				</div>
				</div>
			</form>
		<!-- 목록 표 -->
		<c:if test="${count == 0}">
		<div class="result-display">
			표시할 문의내역이 없습니다.
		</div>
		</c:if>
		<c:if test="${count > 0}">
			<table class="table table-hover text-center">
				<thead class="table-light">
				<tr>
					<th>글번호</th>
					<th>처리상태</th>
					<th>제목</th>
					<th>작성자</th>
					<th>작성일</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="qa" items="${list}">
							<tr>
								<c:if test="${!empty user_num && (user_auth == 9 || user_num == qa.mem_num)}">
									<td><a href="detail.do?qa_num=${qa.qa_num}"
										class="text-decoration-none text-dark fw-bold">${qa.qa_num}</a></td>
									<c:if test="${qa.qa_status == 1}">
										<td><a href="detail.do?qa_num=${qa.qa_num}"
											class="text-decoration-none text-dark fw-bold">미처리</a></td>
									</c:if>
									<c:if test="${qa.qa_status == 2}">
										<td><a href="detail.do?qa_num=${qa.qa_num}"
											class="text-decoration-none text-dark fw-bold">처리</a></td>
									</c:if>
									<td><a href="detail.do?qa_num=${qa.qa_num}"
										class="text-decoration-none text-dark fw-bold">${qa.qa_title}</a></td>
									<td><a href="detail.do?qa_num=${qa.qa_num}"
										class="text-decoration-none text-dark fw-bold">${qa.mem_id}</a></td>
									<c:if test="${empty qa.qa_modify}">
										<td><a href="detail.do?qa_num=${qa.qa_num}"
											class="text-decoration-none text-dark fw-bold">${fn:substring(qa.qa_reg,0,10)}</a></td>
									</c:if>
									<c:if test="${!empty qa.qa_modify}">
										<td><a href="detail.do?qa_num=${qa.qa_num}"
											class="text-decoration-none text-dark fw-bold">${fn:substring(qa.qa_modify,0,10)}</a></td>
									</c:if>
								</c:if>
								<c:if test="${empty user_num || (user_auth != 9 && user_num != qa.mem_num)}">
									<td onclick="alert('조회할 수 있는 권한이 없습니다.')"><b>${qa.qa_num}</b></td>
									<c:if test="${qa.qa_status == 1}">
										<td onclick="alert('조회할 수 있는 권한이 없습니다.')"><b>미처리</b></td>
									</c:if>
									<c:if test="${qa.qa_status == 2}">
										<td onclick="alert('조회할 수 있는 권한이 없습니다.')"><b>처리</b></td>
									</c:if>
									<td onclick="alert('조회할 수 있는 권한이 없습니다.')"><b>${qa.qa_title}</b></td>
									<td onclick="alert('조회할 수 있는 권한이 없습니다.')"><b>${qa.mem_id}</b></td>
									<c:if test="${empty qa.qa_modify}">
										<td onclick="alert('조회할 수 있는 권한이 없습니다.')"><b>${fn:substring(qa.qa_reg,0,10)}</b></td>
									</c:if>
									<c:if test="${!empty qa.qa_modify}">
										<td onclick="alert('조회할 수 있는 권한이 없습니다.')"><b>${fn:substring(qa.qa_modify,0,10)}</b></td>
									</c:if>
								</c:if>
							</tr>
						</c:forEach>
				</tbody>
			</table>
			<div class="text-center">${page}</div>
			<!-- 버튼 -->
			<div class="text-end mb-5">
				<c:if test="${!empty user_num && user_auth == 2}">
					<input type="button" class="btn btn-warning" value="글쓰기" onclick="location.href='writeForm.do'">
				</c:if>		
					<input type="button" class="btn btn-warning" value="홈으로" onclick="location.href='${pageContext.request.contextPath}/main/main.do'">
			</div>
		</c:if>
		</div>
		<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
	</div>
</body>
</html>