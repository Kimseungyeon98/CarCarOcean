<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<title>myPageMenu</title>
<!-- bootstrap css cdn 링크 -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- bootstrap 아이콘 cdn 링크 -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

</head>
<body>
	<nav class="col-md-2 bg-light sidebar pt-5 pb-5">
			<h4>
				<b>${sub_title}</b>
			</h4>
			<ul class="nav flex-column">
			<li class="nav-item"><a class="nav-link active" href="modifyUserForm.do">회원정보
					</a></li>
			<li class="nav-item"><a class="nav-link" href="myFavoriteCarForm.do">관심차량
			</a></li>
			<li class="nav-item"><a class="nav-link" href="mySellCurrentStatusForm.do">내차팔기신청현황
					</a></li>
			<li class="nav-item"><a class="nav-link" href="myTransactionalForm.do">거래내역
					</a></li>
			<li class="nav-item"><a class="nav-link" href="myQnAForm.do">문의내역
					</a></li>
			<li class="nav-item"><a class="nav-link" href="myWriteBoardForm.do">내가쓴글
					</a></li>
			<li class="nav-item"><a class="nav-link" href="deleteUserForm.do">회원탈퇴
				</a></li>
		</ul>
	</nav>
</body>
</html>